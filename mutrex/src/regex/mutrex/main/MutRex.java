package regex.mutrex.main;

import regex.mutrex.CollectDSSetGeneratorNoLimit;
import regex.mutrex.MonitoringDSSetgenerator;
import regex.mutrex.BasicDSSetgenerator;
import regex.mutrex.ds.DSSet;
import regex.mutrex.ds.DSSetGenerator;
import regex.mutrex.parallel.DAsParallelCollectDSSetGenerator;
import regex.mutrex.parallel.mutant.MutantParallelCollectDSSetGenerator;

/** generates all the string to kill the mutation of a given regex */
public class MutRex {
	static DSSetGenerator[] generators = { BasicDSSetgenerator.generator, MonitoringDSSetgenerator.generator,
			CollectDSSetGeneratorNoLimit.generator };

	/** build the distinguishing strings + mutant killed */
	public static DSSet generateStrings(String extregex, GeneratorType type) {
		switch (type) {
		case BASIC:
			return BasicDSSetgenerator.generator.generateDSSet(extregex);
		case MONITORING:
			return MonitoringDSSetgenerator.generator.generateDSSet(extregex);
		case COLLECTING_NO_LIMIT:
			return CollectDSSetGeneratorNoLimit.generator.generateDSSet(extregex);
		case COLLECTING_QUIT_AFTER_N:
			throw new RuntimeException("Use constructor");
		case COLLECTING_PAR_DAs:
			return DAsParallelCollectDSSetGenerator.generator.generateDSSet(extregex);
		case COLLECTING_PAR_MUTs:
			return MutantParallelCollectDSSetGenerator.generator.generateDSSet(extregex);
		default:
			throw new RuntimeException();
		}
	}

	public static DSSet generateStrings(String extregex, String type) {
		return generateStrings(extregex, GeneratorType.valueOf(type));
	}
}
