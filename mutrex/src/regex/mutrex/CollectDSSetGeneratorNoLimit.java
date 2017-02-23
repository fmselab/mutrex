package regex.mutrex;

public class CollectDSSetGeneratorNoLimit extends CollectDSSetGenerator {
	public static DSSetGenerator generator = new CollectDSSetGeneratorNoLimit();

	protected CollectDSSetGeneratorNoLimit() {
	}

	@Override
	boolean stop(DistinguishingAutomaton da) {
		return false;
	}
}