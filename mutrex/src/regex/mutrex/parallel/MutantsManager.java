package regex.mutrex.parallel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import regex.operators.RegexMutator.MutatedRegExp;

public class MutantsManager {
	private static Logger logger = Logger.getLogger(MutantsManager.class.getName());
	protected Iterator<MutatedRegExp> itMutants;
	List<Mutant> mutants;
	protected boolean noUncoveredMutants;
	//private Random rnd = new Random(System.currentTimeMillis());
	protected boolean stop = false;
	private int runningThs = 0;

	public MutantsManager(Iterator<MutatedRegExp> itMutants) {
		this.itMutants = itMutants;
		this.mutants = new ArrayList<Mutant>();
	}

	public synchronized void decrRunningThs() {
		runningThs--;
		notifyAll();
	}

	public synchronized Mutant getNotCoveredByCurrentDAs(Set<DistinguishingAutomatonTh> datS) {
		boolean exit = false;
		while(!exit) {
			while (runningThs >= Runtime.getRuntime().availableProcessors()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			exit = true;
			boolean stop = true;
			for (Mutant mutant : mutants) {
				if (!mutant.isCovered && !mutant.isEquivalent()) {
					stop = false;
					if(mutant.visited.containsAll(datS)) {
						exit = false;
						if(!mutant.isLocked()) {
							mutant.lock();
							runningThs++;
							return mutant;
						}
					}
				}
			}
			if (itMutants.hasNext()) {
				Mutant mutant = new Mutant(itMutants.next());
				mutants.add(mutant);
				mutant.lock();
				runningThs++;
				return mutant;
			}
			if(stop) {
				this.stop = true;
				return null;
			}
			if(!exit) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public synchronized Mutant getMutant(DistinguishingAutomatonTh s) {
		while (runningThs >= Runtime.getRuntime().availableProcessors()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// System.out.println(runningThs);
		boolean stopDA = true;
		if (!noUncoveredMutants) {
			if (itMutants.hasNext()) {
				Mutant mutant = new Mutant(itMutants.next());
				mutant.setVisitedDA(s);
				mutant.lock();
				mutants.add(mutant);
				//mutants.add(rnd.nextInt(mutants.size()), mutant);
				logger.log(Level.INFO, "getting next mutant " + mutant);
				runningThs++;
				return mutant;
			} else {
				//Collections.shuffle(mutants);
				for (Mutant mutant : mutants) {
					if (!mutant.isCovered && !mutant.isEquivalent() && !mutant.hasVisitedDA(s)) {
						stopDA = false;
						if (!mutant.isLocked()) {
							mutant.setVisitedDA(s);
							mutant.lock();
							logger.log(Level.INFO, "getting mutant " + mutant);
							runningThs++;
							return mutant;
						}
					}
				}
			}
		}
		if (stopDA) {
			s.stopThread();
		}
		return null;
	}

	public void mutantConsidered() {
		decrRunningThs();
	}

	public void coverMutant(Mutant mutant) {
		mutant.isCovered = true;
		mutant.mutAut = null;
		mutant.mutNegAut = null;
	}
}