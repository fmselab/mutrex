package regex.mutrex.main;

import regex.mutrex.BasicDSSetgenerator;
import regex.mutrex.CollectDSSetGeneratorNoLimit;
import regex.mutrex.MonitoringDSSetgenerator;
import regex.mutrex.ds.DSSet;
import regex.mutrex.ds.DSSetGenerator;
import regex.mutrex.parallel.DAsParallelCollectDSSetGenerator;
import regex.mutrex.parallel.mutant.MutantParallelCollectDSSetGenerator;
import regex.operators.AllMutators;

/** generates all the string to kill the mutation of a given regex */
public class MutRex {
	static DSSetGenerator[] generators = { BasicDSSetgenerator.generator, MonitoringDSSetgenerator.generator,
			CollectDSSetGeneratorNoLimit.generator };

	/** build the distinguishing strings + mutant killed */
	public static DSSet generateStrings(String extregex, String type, String[] operators, String orientation) {
		// set the mutation operators
		AllMutators.enableOnly(operators);
		return generateStrings(extregex, GeneratorType.valueOf(type), GeneratorType.Orientation.valueOf(orientation));
	}

	/**
	 *  build the distinguishing strings + mutant killed.
	 *
	 * @param extregex the extregex
	 * @param type the type
	 * @return the DS set
	 */
	public static DSSet generateStrings(String extregex, GeneratorType type) {
		return generateStrings(extregex, type, GeneratorType.Orientation.RANDOM);
	}
	
	public static DSSet generateStrings(String extregex, GeneratorType type, GeneratorType.Orientation orientation) {
		switch (type) {
		case BASIC:
			switch (orientation) {
			case RANDOM:
				return BasicDSSetgenerator.generator.generateDSSet(extregex);
			case PREF_ACCEPT:
				return BasicDSSetgenerator.generatorPOS.generateDSSet(extregex);
			case PREF_REJECT:
				return BasicDSSetgenerator.generatorNEG.generateDSSet(extregex);
			default:
				throw new RuntimeException();
			}
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
}
