package regex.mutrex;

import java.util.ArrayList;
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
import regex.distinguishing.DistinguishingString;
import regex.operators.RegexMutator.MutatedRegExp;

/**
 * generates a ds that tries to kill as many mutants as possible.
 * 
 * Limit threads
 * 
 */
public class ParallelCollectLimitThDSSetGenerator extends DSSetGenerator {
	private static Logger logger = Logger.getLogger(ParallelCollectLimitThDSSetGenerator.class.getName());
	public static DSSetGenerator generator = new ParallelCollectLimitThDSSetGenerator();

	protected ParallelCollectLimitThDSSetGenerator() {
	}

	@Override
	public void addStringsToDSSet(DSSet dsS, RegExp regex, Iterator<MutatedRegExp> mutants) {
		List<Boolean> trueFalse = Arrays.asList(true, false);
		// Automaton rgxAut = regex.toAutomaton();
		Automaton rexAut = null;
		Automaton rexNegAut = null;
		MutantsManager mutantsManager = new MutantsManager(mutants);
		int nDAs = 0;// number of created distinguishing automata (da)
		Set<DistinguishAutomatonTh> datS = new HashSet<DistinguishAutomatonTh>();
		while (mutantsManager.areThereUncoveredMutants()) {
			// mutant not covered by the created distinguishing automata
			// Mutant mutant = mutantsManager.getNotCoveredByCurrentDAs(nDAs);
			Mutant mutant = mutantsManager.getNotCoveredByCurrentDAs(datS);
			if (mutant != null) {
				assert mutant.isLocked();
				// randomly generate a positive or negative da
				DistinguishAutomatonTh dat = null;
				Collections.shuffle(trueFalse);
				logger.log(Level.INFO, "new da for " + mutant);
				for (boolean b : trueFalse) {
					DistinguishingAutomaton newDa = new DistinguishingAutomaton(regex, rexAut, rexNegAut, b);
					if (newDa.add(mutant.getRegex(), mutant.mutAut, mutant.mutNegAut)) {
						assert newDa.getMutants().size() == 1;
						assert DistStringCreator.getDS(regex, mutant.getRegex(), DSgenPolicy.RANDOM) != null;
						dat = new DistinguishAutomatonTh(newDa, mutantsManager, dsS);
						datS.add(dat);
						mutant.setVisitedDA(dat);
						mutantsManager.coverMutant(mutant);
						mutant.unlock();
						dat.start();
						nDAs++;
						break;
					}
				}
				// if no da has been created, it means that the mutant is
				// equivalent (tested both with positive and negative das)
				if (dat == null) {
					logger.log(Level.INFO, "Equivalent " + mutant);
					mutant.setTestedPositiveWithR();
					mutant.setTestedNegativeWithR();
					assert mutant.isEquivalent();
					mutant.unlock();
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
		/*
		 * if (this.getClass().desiredAssertionStatus()) { assertCheck(regex,
		 * mutantsManager, dsS); }
		 */
		return;
	}

	class DistinguishAutomatonTh extends Thread {
		private DistinguishingAutomaton da;
		private MutantsManager mutantsManager;
		private boolean run;
		private DSSet dsS;

		private DistinguishAutomatonTh(DistinguishingAutomaton da, MutantsManager mutantsManager, DSSet dsS) {
			this.da = da;
			this.mutantsManager = mutantsManager;
			run = true;
			this.dsS = dsS;
			assert da.getMutants().size() == 1;
		}

		@Override
		public void run() {
			while (run) {
				Mutant mutant = mutantsManager.getMutant(this);
				if (mutant != null) {
					if (da.add(mutant.getRegex(), mutant.mutAut, mutant.mutNegAut)) {
						logger.log(Level.INFO, "Adding " + mutant);
						assert da.getMutants().size() > 1;
						mutantsManager.coverMutant(mutant);
					}
					mutant.unlock();
				}
				mutantsManager.decrRunningThs();
			}
			List<RegExp> daCoveredMuts = da.getMutants();
			assert daCoveredMuts.size() > 0;
			dsS.add(new DistinguishingString(da.getExample(), da.positive), daCoveredMuts);
			da = null;
			logger.log(Level.INFO, da + " exiting");
		}

		private void stopThread() {
			run = false;
		}
	}

	private class MutantsManager {
		private Iterator<MutatedRegExp> itMutants;
		private List<Mutant> mutants;
		private boolean noUncoveredMutants;
		int runningThs = 0;

		private MutantsManager(Iterator<MutatedRegExp> itMutants) {
			this.itMutants = itMutants;
			this.mutants = new ArrayList<Mutant>();
		}

		public synchronized void decrRunningThs() {
			runningThs--;
			if (runningThs < Runtime.getRuntime().availableProcessors()) {
				notifyAll();
			}
		}

		private synchronized boolean areThereUncoveredMutants() {
			if (noUncoveredMutants)
				return false;
			if (itMutants.hasNext()) {
				return true;
			} else {
				for (Mutant m : mutants) {
					if (!m.isEquivalent() && !m.isCovered) {
						return true;
					}
				}
			}
			noUncoveredMutants = true;
			synchronized (this) {
				notifyAll();
			}
			return false;
		}

		// private synchronized Mutant getNotCoveredByCurrentDAs(int n) {
		private synchronized Mutant getNotCoveredByCurrentDAs(Set<DistinguishAutomatonTh> datS) {
			if (!noUncoveredMutants) {
				if (mutants.size() == 0) {
					if (itMutants.hasNext()) {
						Mutant mutant = new Mutant(itMutants.next());
						mutants.add(mutant);
						mutant.lock();
						return mutant;
					}
				} else {
					// Collections.shuffle(mutants);
					for (Mutant mutant : mutants) {
						if (!mutant.isCovered && !mutant.isEquivalent() && mutant.visited.containsAll(datS)
								&& !mutant.isLocked()) {
							mutant.lock();
							return mutant;
						}
					}
				}
			}
			return null;
		}

		private synchronized Mutant getMutant(DistinguishAutomatonTh s) {
			if (runningThs >= Runtime.getRuntime().availableProcessors()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			runningThs++;
			// System.out.println(runningThs);
			boolean stopDA = true;
			if (!noUncoveredMutants) {
				if (itMutants.hasNext()) {
					Mutant mutant = new Mutant(itMutants.next());
					mutant.setVisitedDA(s);
					mutant.lock();
					mutants.add(mutant);
					return mutant;
				} else {
					//Collections.shuffle(mutants);
					for (Mutant mutant : mutants) {
						if (!mutant.isCovered && !mutant.isEquivalent() && !mutant.hasVisitedDA(s)) {
							stopDA = false;
							if (!mutant.isLocked()) {
								mutant.setVisitedDA(s);
								mutant.lock();
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

		void coverMutant(Mutant mutant) {
			mutant.isCovered = true;
			mutant.mutAut = null;
			mutant.mutNegAut = null;
		}
	}

	private class Mutant {
		MutatedRegExp mutant;
		Set<DistinguishAutomatonTh> visited;
		private boolean locked;
		private boolean testedPositiveWithR;
		private boolean testedNegativeWithR;
		boolean isCovered;
		Automaton mutAut;
		Automaton mutNegAut;

		public Mutant(MutatedRegExp mutatedRegExp) {
			this.mutant = mutatedRegExp;
			visited = new HashSet<DistinguishAutomatonTh>();
		}

		public void setVisitedDA(DistinguishAutomatonTh da) {
			visited.add(da);
		}

		public boolean hasVisitedDA(DistinguishAutomatonTh dat) {
			return visited.contains(dat);
		}

		public boolean isLocked() {
			return locked;
		}

		public void lock() {
			locked = true;
		}

		public void unlock() {
			locked = false;
		}

		public RegExp getRegex() {
			return mutant.mutatedRexExp;
		}

		public void setTestedPositiveWithR() {
			this.testedPositiveWithR = true;
		}

		public void setTestedNegativeWithR() {
			this.testedNegativeWithR = true;
		}

		public boolean isEquivalent() {
			return testedPositiveWithR && testedNegativeWithR;
		}
	}
}