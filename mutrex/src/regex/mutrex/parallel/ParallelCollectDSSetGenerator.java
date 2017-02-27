package regex.mutrex.parallel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import regex.distinguishing.DSgenPolicy;
import regex.distinguishing.DistStringCreator;
import regex.mutrex.ds.DSSet;
import regex.mutrex.ds.DSSetGenerator;
import regex.mutrex.ds.DistinguishingAutomaton;
import regex.mutrex.ds.DistinguishingAutomaton.RegexWAutomata;
import regex.operators.RegexMutator.MutatedRegExp;

abstract public class ParallelCollectDSSetGenerator extends DSSetGenerator {
	private static Logger logger = Logger.getLogger(ParallelCollectDSSetGenerator.class.getName());

	abstract public MutantsManager getMutantManager(Iterator<MutatedRegExp> mutants);
	
	@Override
	public void addStringsToDSSet(DSSet dsS, RegExp regex, Iterator<MutatedRegExp> mutants) {
		List<Boolean> trueFalse = Arrays.asList(true, false);
		Automaton rexAut = null;
		Automaton rexNegAut = null;
		MutantsManager mutantsManager = getMutantManager(mutants);
		Set<DistinguishAutomatonTh> datS = new HashSet<DistinguishAutomatonTh>();
		while (mutantsManager.areThereUncoveredMutants()) {
			// mutant not covered by the created distinguishing automata
			Mutant mutant = mutantsManager.getNotCoveredByCurrentDAs(datS);
			if (mutant != null) {
				assert mutant.isLocked();
				// randomly generate a positive or negative da
				DistinguishAutomatonTh dat = null;
				Collections.shuffle(trueFalse);
				for (boolean b : trueFalse) {
					RegexWAutomata r= new RegexWAutomata(regex);
					//DistinguishingAutomaton newDa = new DistinguishingAutomaton(regex, rexAut, rexNegAut, b);
					DistinguishingAutomaton newDa = new DistinguishingAutomaton(r, b);
					RegexWAutomata m= new RegexWAutomata(mutant.getRegex());					
					if (newDa.add(m)) {
						logger.log(Level.INFO, "new da for " + mutant);
						assert newDa.getMutants().size() == 1;
						assert DistStringCreator.getDS(regex, mutant.getRegex(), DSgenPolicy.RANDOM) != null;
						dat = new DistinguishAutomatonTh(newDa, mutantsManager, dsS);
						datS.add(dat);
						mutant.setVisitedDA(dat);
						mutantsManager.coverMutant(mutant);
						mutant.unlock();
						dat.start();
						mutantsManager.mutantConsidered();
						break;
					}
				}
				// if no da has been created, it means that the mutant is
				// equivalent (tested both with positive and negative das)
				if (dat == null) {
					logger.log(Level.INFO, "Equiv " + mutant);
					mutant.setTestedPositiveWithR();
					mutant.setTestedNegativeWithR();
					assert mutant.isEquivalent();
					mutant.unlock();
					mutantsManager.mutantConsidered();
					assert DistStringCreator.getDS(regex, mutant.getRegex(), DSgenPolicy.RANDOM) == null;
				}
			}
		}
		for (DistinguishAutomatonTh dat : datS) {
			try {
				dat.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// assert mutantsManager.mutants.parallelStream().allMatch(m ->
		// (m.isCovered || m.isEquivalent()));
		if (this.getClass().desiredAssertionStatus()) {
			for (Mutant m : mutantsManager.mutants) {
				assert m.isCovered || m.isEquivalent();
			}
		}
	}
}

/*class RandomList<T> implements Iterable<T> {
	private List<T> elements;

	public RandomList() {
		elements = new ArrayList<T>();
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			// Java 8
			// List<Integer> indexes = IntStream.range(0, elements.size() -
			// 1).boxed().collect(Collectors.toList());
			List<Integer> indexes;
			{
				indexes = new ArrayList<Integer>();
				for (int i = 0; i < elements.size(); i++) {
					indexes.add(i);
				}
			}

			Random rnd = new Random();

			@Override
			public T next() {
				int i = rnd.nextInt(indexes.size());
				T element = elements.get(indexes.remove(i));
				return element;
			}

			@Override
			public boolean hasNext() {
				return indexes.size() > 0;
			}
		};
	}
}*/