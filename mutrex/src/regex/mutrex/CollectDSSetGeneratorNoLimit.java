package regex.mutrex;

import regex.mutrex.ds.DSSetGenerator;
import regex.mutrex.ds.DistinguishingAutomaton;

public class CollectDSSetGeneratorNoLimit extends CollectDSSetGenerator {
	public static DSSetGenerator generator = new CollectDSSetGeneratorNoLimit();

	protected CollectDSSetGeneratorNoLimit() {
	}

	@Override
	boolean stop(DistinguishingAutomaton da) {
		return false;
	}
}