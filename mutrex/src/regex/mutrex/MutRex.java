package regex.mutrex;

/** generates all the string to kill the mutation of a given regex */
public class MutRex {

	public static DSSetGenerator[] generators = { PlainDSSetgenerator.generator, MonitoringDSSetgenerator.generator,
			CollectDSSetGeneratorNoLimit.generator, ParallelCollectPosNegDSSetGenerator.generator, ParallelCollectDSSetGenerator.generator };

	/** build the distinguishing strings + mutant killed */
	public static DSSet generateStrings(String extregex, GeneratorType type) {
		switch (type) {
		case BASIC:
			return PlainDSSetgenerator.generator.generateDSSet(extregex);
		case MONITORING:
			return MonitoringDSSetgenerator.generator.generateDSSet(extregex);
		case COLLECTING_NO_LIMIT:
			return CollectDSSetGeneratorNoLimit.generator.generateDSSet(extregex);
		case COLLECTING_QUIT_AFTER_N:
			throw new RuntimeException("Use constructor");
		case COLLECTING_PAR_ONLY_POS_AND_NEG:
			return ParallelCollectPosNegDSSetGenerator.generator.generateDSSet(extregex);
		case COLLECTING_PAR:
			return ParallelCollectDSSetGenerator.generator.generateDSSet(extregex);
		case COLLECTING_PAR2:
			return ParallelCollectLimitThDSSetGenerator.generator.generateDSSet(extregex);
		default:
			throw new RuntimeException();
		}
	}

	public static DSSet generateStrings(String extregex, String type) {
		return generateStrings(extregex, GeneratorType.valueOf(type));
	}
}
