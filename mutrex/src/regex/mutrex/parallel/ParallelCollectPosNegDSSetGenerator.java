package regex.mutrex.parallel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import regex.distinguishing.DistinguishingString;
import regex.mutrex.DSSet;
import regex.mutrex.DSSetGenerator;
import regex.mutrex.DistinguishingAutomaton;
import regex.operators.RegexMutator.MutatedRegExp;

/**
 * generates a ds that tries to kill as many mutants as possible. Parallel
 * version
 * 
 */
public class ParallelCollectPosNegDSSetGenerator extends DSSetGenerator {
	private static Logger logger = Logger.getLogger(ParallelCollectPosNegDSSetGenerator.class.getName());
	public static DSSetGenerator generator = new ParallelCollectPosNegDSSetGenerator();

	protected ParallelCollectPosNegDSSetGenerator() {
	}

	@Override
	public void addStringsToDSSet(DSSet dsS, RegExp regex, Iterator<MutatedRegExp> mutants) {
		Automaton rgxAut = regex.toAutomaton();
		Automaton rexNegAut = rgxAut.complement();
		MutantsManager mutantsManager = new MutantsManager(mutants);
		DATrunner datRunnerPos = new DATrunner(regex, rgxAut, rexNegAut, true, mutantsManager, dsS);
		DATrunner datRunnerNeg = new DATrunner(regex, rgxAut, rexNegAut, false, mutantsManager, dsS);
		datRunnerPos.start();
		datRunnerNeg.start();
		try {
			datRunnerPos.join();
			datRunnerNeg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return;
	}

	class DATrunner extends Thread {
		private boolean positive;
		private MutantsManager mutantsManager;
		private RegExp regex;
		private Automaton rgxAut;
		private Automaton rexNegAut;
		private DSSet dsS;

		public DATrunner(RegExp regex, Automaton rgxAut, Automaton rexNegAut, boolean positive, MutantsManager mutantsManager, DSSet dsS) {
			this.regex = regex;
			this.rgxAut = rgxAut;
			this.rexNegAut = rexNegAut;
			this.positive = positive;
			this.mutantsManager = mutantsManager;
			this.dsS = dsS;
		}

		@Override
		public void run() {
			while (mutantsManager.areThereUncoveredMutants()) {
				DistinguishAutomatonTh dat = new DistinguishAutomatonTh(new DistinguishingAutomaton(regex, rgxAut, rexNegAut, positive),
						mutantsManager);
				dat.start();
				try {
					dat.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				DistinguishingAutomaton da = dat.getDa();
				List<RegExp> daCoveredMuts = da.getMutants();
				if (daCoveredMuts.size() > 0) {
					dsS.add(new DistinguishingString(da.getExample(), positive), daCoveredMuts);
				}
			}
		}
	}

	class DistinguishAutomatonTh extends Thread {
		private DistinguishingAutomaton da;
		private MutantsManager mutantsManager;
		private boolean run;

		public DistinguishAutomatonTh(DistinguishingAutomaton da, MutantsManager mutantsManager) {
			this.da = da;
			this.mutantsManager = mutantsManager;
			run = true;
		}

		@Override
		public void run() {
			while (run) {
				Mutant mutant = mutantsManager.getMutant(this);
				if (mutant != null) {
					if (da.add(mutant.getMutant(), null, null)) {
						mutantsManager.coverMutant(mutant);
					} else if (da.getMutants().size() == 0) {
						if (da.isPositive()) {
							mutant.setTestedPositiveWithR();
						} else {
							mutant.setTestedNegativeWithR();
						}
					}
					mutant.unlock();
				}
			}
		}

		public void stopThread() {
			run = false;
		}

		public DistinguishingAutomaton getDa() {
			return da;
		}
	}

	class MutantsManager {
		private Iterator<MutatedRegExp> itMutants;
		private List<Mutant> mutants;
		boolean noUncoveredMutants;

		public MutantsManager(Iterator<MutatedRegExp> itMutants) {
			this.itMutants = itMutants;
			this.mutants = new ArrayList<Mutant>();
		}

		public boolean areThereUncoveredMutants() {
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

		public synchronized Mutant getMutant(DistinguishAutomatonTh s) {
			boolean stopDA = true;
			if (!noUncoveredMutants) {
				if (itMutants.hasNext()) {
					RegExp rgx = itMutants.next().mutatedRexExp;
					Mutant mutant = new Mutant(rgx);
					mutant.setVisitedDA(s);
					mutant.lock();
					mutants.add(mutant);
					stopDA = !itMutants.hasNext() && mutants.size() == 0;
					return mutant;
				} else {
					Collections.shuffle(mutants);
					for (Mutant m : mutants) {
						if (!m.isEquivalent() && !m.isCovered && !m.hasVisitedDA(s)) {
							stopDA = false;
							if (!m.isLocked()) {
								m.setVisitedDA(s);
								m.lock();
								return m;
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

		public void coverMutant(Mutant mutant) {
			mutant.isCovered = true;
		}
	}

	class Mutant {
		private RegExp mutant;
		Set<DistinguishAutomatonTh> visited;
		private boolean locked;
		private boolean testedPositiveWithR;
		private boolean testedNegativeWithR;
		boolean isCovered;

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

		public RegExp getMutant() {
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
}