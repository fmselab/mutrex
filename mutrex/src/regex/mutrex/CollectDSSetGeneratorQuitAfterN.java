package regex.mutrex;

public class CollectDSSetGeneratorQuitAfterN extends CollectDSSetGenerator {
	private int numCollectedMutantTH;

	public CollectDSSetGeneratorQuitAfterN(int numCollectedMutantTH) {
		this.numCollectedMutantTH = numCollectedMutantTH;
	}

	@Override
	boolean stop(DistinguishingAutomaton da) {
		return da.mutatedRegexes.size() >= numCollectedMutantTH;
	}
}