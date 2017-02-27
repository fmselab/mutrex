package regex.mutrex;

import regex.mutrex.ds.DistinguishingAutomaton;

public class CollectDSSetGeneratorQuitAfterN extends CollectDSSetGenerator {
	private int numCollectedMutantTH;

	public CollectDSSetGeneratorQuitAfterN(int numCollectedMutantTH) {
		this.numCollectedMutantTH = numCollectedMutantTH;
	}

	@Override
	boolean stop(DistinguishingAutomaton da) {
		return da.size() >= numCollectedMutantTH;
	}
}