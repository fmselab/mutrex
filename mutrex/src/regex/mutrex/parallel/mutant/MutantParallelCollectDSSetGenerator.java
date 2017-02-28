package regex.mutrex.parallel.mutant;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import dk.brics.automaton.RegExp;
import regex.mutrex.ds.DSSet;
import regex.mutrex.ds.DSSetGenerator;
import regex.mutrex.ds.DistinguishingAutomaton;
import regex.mutrex.ds.DistinguishingAutomaton.RegexWAutomata;
import regex.operators.RegexMutator.MutatedRegExp;

public class MutantParallelCollectDSSetGenerator extends DSSetGenerator {
	private static Logger logger = Logger.getLogger(MutantParallelCollectDSSetGenerator.class.getName());
	public static DSSetGenerator generator = new MutantParallelCollectDSSetGenerator();
	int numRunningMutants = 0;

	public synchronized Mutant getMutant(Iterator<MutatedRegExp> mutants) {
		while (mutants.hasNext()) {
			if (numRunningMutants >= Runtime.getRuntime().availableProcessors()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			} else {
				numRunningMutants++;
				return new Mutant(mutants.next());
			}
		}
		return null;
	}

	public synchronized void decreaseRunningMuts() {
		numRunningMutants--;
	}

	@Override
	public void addStringsToDSSet(DSSet dsS, RegExp regex, Iterator<MutatedRegExp> mutants) {
		DasManager dasManager = new DasManager(regex);
		Mutant mutant = null;
		while ((mutant = getMutant(mutants)) != null) {
			new MutTh(mutant, dasManager, this).start();
		}
	}
}

class MutTh extends Thread {
	Mutant mutant;
	DasManager dasManager;
	MutantParallelCollectDSSetGenerator gen;

	public MutTh(Mutant mutant, DasManager dasManager, MutantParallelCollectDSSetGenerator gen) {
		this.mutant = mutant;
		this.dasManager = dasManager;
		this.gen = gen;
	}
	
	@Override
	public void run() {
		List<Boolean> trueFalse = Arrays.asList(true, false);
		boolean isCovered = false;
		while(!isCovered) {
			DistinguishingAutomatonClass dac = dasManager.getDA(mutant);
			if(dac == null) {
				Collections.shuffle(trueFalse);
				for (boolean b : trueFalse) {
					DistinguishingAutomaton newDa = new DistinguishingAutomaton(dasManager.regexWithAutomata, b);
					if (newDa.add(mutant.mutant)) {
						DistinguishingAutomatonClass newDaC = new DistinguishingAutomatonClass(newDa);
						dasManager.addDA(newDaC);
						break;
					}
				}
				isCovered = true;
				mutant.isEquivalent = true;
			}
			else {
				if(dac.da.add(mutant.mutant)) {
					isCovered = true;
				}
				dasManager.unlock(dac);
			}
		}
		gen.decreaseRunningMuts();
	}
	
}

class DasManager {
	Set<DistinguishingAutomatonClass> daS = new HashSet<DistinguishingAutomatonClass>();
	List<Boolean> trueFalse = Arrays.asList(true, false);
	RegexWAutomata regexWithAutomata;

	public DasManager(RegExp regex) {
		regexWithAutomata = new RegexWAutomata(regex);
	}

	public synchronized void addDA(DistinguishingAutomatonClass da) {
		daS.add(da);
	}

	public synchronized void unlock(DistinguishingAutomatonClass da) {
		da.locked = false;
		notifyAll();
	}
	
	public synchronized DistinguishingAutomatonClass getDA(Mutant mutant) {
		Set<DistinguishingAutomatonClass> checkedByMut = mutant.visited;
		while(true) {
			boolean addNewDa = true;
			for (DistinguishingAutomatonClass da : daS) {
				if (!checkedByMut.contains(da)) {
					addNewDa = false;
					if (!da.locked) {
						da.locked = true;
						return da;
					}
				}
			}
			if(addNewDa) {
				return null;
			}
			else {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class DistinguishingAutomatonClass {
	DistinguishingAutomaton da;
	boolean locked = false;

	public DistinguishingAutomatonClass(DistinguishingAutomaton da) {
		this.da = da;
	}
}

class Mutant {
	RegexWAutomata mutant;
	Set<DistinguishingAutomatonClass> visited;
	boolean isEquivalent;

	public Mutant(MutatedRegExp mutatedRegExp) {
		this.mutant = new RegexWAutomata(mutatedRegExp.mutatedRexExp);
		visited = new HashSet<DistinguishingAutomatonClass>();
	}

	public void setVisitedDA(DistinguishingAutomatonClass da) {
		visited.add(da);
	}

	public boolean hasVisitedDA(DistinguishingAutomatonClass dat) {
		return visited.contains(dat);
	}

	public RegExp getRegex() {
		return mutant.getMutant();
	}
}