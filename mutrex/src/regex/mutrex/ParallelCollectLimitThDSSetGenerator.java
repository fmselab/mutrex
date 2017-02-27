package regex.mutrex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
		//Automaton rgxAut = regex.toAutomaton();
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
				Automaton mAut = null;
				Automaton negMaut = null;
				// randomly generate a positive or negative da
				DistinguishAutomatonTh dat = null;
				Collections.shuffle(trueFalse);
				for (boolean b : trueFalse) {
					//DistinguishingAutomaton newDa = new DistinguishingAutomaton(rgxAut, b);
					DistinguishingAutomaton newDa = new DistinguishingAutomaton(regex, rexAut, rexNegAut, b);
					if (newDa.add(mutant.getRegex(), mAut, negMaut)) {
						assert newDa.getMutants().size() == 1;
						assert DistStringCreator.getDS(regex, mutant.getRegex(), DSgenPolicy.RANDOM) != null;
						dat = new DistinguishAutomatonTh(newDa, mutantsManager, dsS);
						//System.out.println("getNotCoveredByCurrentDAs " + mutant + " " + mutant.getRegex() + " covered by " + dat);
						datS.add(dat);
						mutant.setVisitedDA(dat);
						mutant.isCovered = true;
						mutant.unlock();
						dat.start();
						nDAs++;
						break;
					}
				}
				// if no da has been created, it means that the mutant is
				// equivalent (tested both with positive and negative das)
				if (dat == null) {
					// System.out.println("Equiv " + mutant.getRegex());
					mutant.setTestedPositiveWithR();
					mutant.setTestedNegativeWithR();
					assert mutant.isEquivalent();
					//System.out.println("getNotCoveredByCurrentDAs " + mutant + " " + mutant.getRegex() + " equivalent");
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
		/*if (this.getClass().desiredAssertionStatus()) {
			assertCheck(regex, mutantsManager, dsS);
		}*/
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
					if (da.add(mutant.getRegex(), null, null)) {
						//System.out.println("getMutant " + mutant + " " + mutant.getRegex() + "covered by " + this);
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
			if(runningThs < Runtime.getRuntime().availableProcessors()) {
				notifyAll();
			}
		}

		private boolean areThereUncoveredMutants() {
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
			return false;
		}

		// private synchronized Mutant getNotCoveredByCurrentDAs(int n) {
		private synchronized Mutant getNotCoveredByCurrentDAs(Set<DistinguishAutomatonTh> datS) {
			/*if(runningThs >= Runtime.getRuntime().availableProcessors()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			runningThs++;*/
			if (!noUncoveredMutants) {
				if (mutants.size() == 0) {
					if (itMutants.hasNext()) {
						RegExp rgx = itMutants.next().mutatedRexExp;
						Mutant mutant = new Mutant(rgx);
						mutants.add(mutant);
						mutant.lock();
						return mutant;
					}
				} else {
					Collections.shuffle(mutants);
					for (Mutant mutant : mutants) {
						// if (!mutant.isEquivalent() && !mutant.isCovered &&
						// mutant.visited.size() == n) {
						if (!mutant.isCovered && !mutant.isEquivalent() && mutant.visited.containsAll(datS) && !mutant.isLocked()) {
							mutant.lock();
							return mutant;
						}
					}
				}
			}
			return null;
		}

		private synchronized Mutant getMutant(DistinguishAutomatonTh s) {
			if(runningThs >= Runtime.getRuntime().availableProcessors()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			runningThs++;
			//System.out.println(runningThs);
			boolean stopDA = true;
			if (!noUncoveredMutants) {
				if (itMutants.hasNext()) {
					RegExp rgx = itMutants.next().mutatedRexExp;
					Mutant mutant = new Mutant(rgx);
					mutant.setVisitedDA(s);
					mutant.lock();
					mutants.add(mutant);
					return mutant;
				} else {
					Collections.shuffle(mutants);
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
		private RegExp mutant;
		Set<DistinguishAutomatonTh> visited;
		private boolean locked;
		private boolean testedPositiveWithR;
		private boolean testedNegativeWithR;
		boolean isCovered;
		Automaton mutAut;
		Automaton mutNegAut;

		public Mutant(RegExp mutant) {
			this.mutant = mutant;
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
			return mutant;
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

	private static void assertCheck(RegExp regex, MutantsManager mutantsManager, DSSet dsS) {
		assert !mutantsManager.areThereUncoveredMutants();
		assert !mutantsManager.itMutants.hasNext();
		int numEquiv = 0;
		for (Mutant m : mutantsManager.mutants) {
			assert !m.isLocked();
			assert (m.isEquivalent() && !m.isCovered) || (!m.isEquivalent() && m.isCovered);
			if (m.isEquivalent()) {
				numEquiv++;
				assert DistStringCreator.getDS(regex, m.getRegex(), DSgenPolicy.RANDOM) == null: m.getRegex();
			} else {
				assert DistStringCreator.getDS(regex, m.getRegex(), DSgenPolicy.RANDOM) != null: m.getRegex();
			}
		}
		//System.out.println("\nnumEquiv " + numEquiv);
		Set<String> mSet = new HashSet<String>();
		for (Mutant m : mutantsManager.mutants) {
			mSet.add(m.getRegex().toString());
		}
		//System.out.println("size before " + mSet.size());
		int numCovered = 0;
		for (DistinguishingString s : dsS) {
			for (RegExp r : dsS.getKilledMutants(s)) {
				assert DistStringCreator.getDS(regex, r, DSgenPolicy.RANDOM) != null: r;
				while(mSet.remove(r.toString()));
				numCovered++;
			}
		}
		//System.out.println("numCovered " + numCovered);
		//System.out.println("size after " + mSet.size());
		for (String r : mSet) {
			assert DistStringCreator.getDS(regex, new RegExp(r), DSgenPolicy.RANDOM) == null: r;
		}
	}
}