package regex.mutrex;

import java.util.Iterator;
import java.util.logging.Logger;

import regex.operators.RegexMutator.MutatedRegExp;

/**
 * Limit threads
 * 
 */
public class ParallelCollectLimitThDSSetGenerator extends ParallelCollectDSSetGenerator {
	static Logger logger = Logger.getLogger(ParallelCollectLimitThDSSetGenerator.class.getName());
	public static DSSetGenerator generator = new ParallelCollectLimitThDSSetGenerator();

	@Override
	public MutantsManager getMutantManager(Iterator<MutatedRegExp> mutants) {
		return new MutantsManagerLimit(mutants);
	}
}