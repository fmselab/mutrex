package regex.mutrex.parallel;

import java.util.Iterator;

import regex.mutrex.DSSetGenerator;
import regex.operators.RegexMutator.MutatedRegExp;

public class ParallelCollectNoLimitDSSetGenerator extends ParallelCollectDSSetGenerator {
	public static DSSetGenerator generator = new ParallelCollectNoLimitDSSetGenerator();

	@Override
	public MutantsManager getMutantManager(Iterator<MutatedRegExp> mutants) {
		return new MutantsManagerNoLimit(mutants);
	}
}