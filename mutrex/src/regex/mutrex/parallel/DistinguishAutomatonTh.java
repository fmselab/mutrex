package regex.mutrex.parallel;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import dk.brics.automaton.RegExp;
import regex.distinguishing.DistinguishingString;
import regex.mutrex.DSSet;
import regex.mutrex.DistinguishingAutomaton;

public class DistinguishAutomatonTh extends Thread {
	private static Logger logger = Logger.getLogger(DistinguishAutomatonTh.class.getName());
	private DistinguishingAutomaton da;
	private MutantsManager mutantsManager;
	private boolean run;
	private DSSet dsS;

	public DistinguishAutomatonTh(DistinguishingAutomaton da, MutantsManager mutantsManager, DSSet dsS) {
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
			mutantsManager.mutantConsidered();
		}
		List<RegExp> daCoveredMuts = da.getMutants();
		assert daCoveredMuts.size() > 0;
		dsS.add(new DistinguishingString(da.getExample(), da.positive), daCoveredMuts);
		da = null;
		logger.log(Level.INFO, da + " exiting");
	}

	public void stopThread() {
		run = false;
	}
}