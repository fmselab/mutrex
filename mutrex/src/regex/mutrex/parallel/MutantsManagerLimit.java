package regex.mutrex.parallel;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import regex.operators.RegexMutator.MutatedRegExp;

public class MutantsManagerLimit extends MutantsManager {
	private static Logger logger = Logger.getLogger(MutantsManagerLimit.class.getName());
	private int runningThs = 0;

	public MutantsManagerLimit(Iterator<MutatedRegExp> itMutants) {
		super(itMutants);
	}

	public synchronized void decrRunningThs() {
		runningThs--;
		if (runningThs < Runtime.getRuntime().availableProcessors()) {
			notifyAll();
		}
	}

	@Override
	public synchronized boolean areThereUncoveredMutants() {
		if (noUncoveredMutants)
			return false;
		if (itMutants.hasNext()) {
			synchronized (this) {
				notifyAll();
			}
			return true;
		} else {
			for (Mutant m : mutants) {
				if (!m.isEquivalent() && !m.isCovered) {
					synchronized (this) {
						notifyAll();
					}
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

	@Override
	public synchronized Mutant getMutant(DistinguishAutomatonTh s) {
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
				logger.log(Level.INFO, "getting next mutant " + mutant);
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

	@Override
	public void mutantConsidered() {
		decrRunningThs();
	}
}