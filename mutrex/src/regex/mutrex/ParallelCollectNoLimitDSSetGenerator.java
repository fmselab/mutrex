package regex.mutrex;

import java.util.Iterator;

import regex.operators.RegexMutator.MutatedRegExp;

public class ParallelCollectNoLimitDSSetGenerator extends ParallelCollectDSSetGenerator {
	public static DSSetGenerator generator = new ParallelCollectNoLimitDSSetGenerator();

	@Override
	public MutantsManager getMutantManager(Iterator<MutatedRegExp> mutants) {
		return new MutantsManagerNoLimit(mutants);
	}
}