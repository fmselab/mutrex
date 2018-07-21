package regex.operators;

import java.util.ArrayList;
import java.util.List;

import dk.brics.automaton.oo.REGEXP_REPEAT;
import dk.brics.automaton.oo.ooregex;

/**
 * In MUTATION 2017 is QC
 *
 */
public class QuantifierChange extends RegexMutator {
	
	public static QuantifierChange mutator = new QuantifierChange();

	private QuantifierChange() {
		super(new QuantifierChangeVisitor());
	}

	private static class QuantifierChangeVisitor extends RegexVisitorAdapterList {

		@Override
		public List<ooregex> visit(REGEXP_REPEAT r) {
			ooregex contentExpr = r.getContentExpr();
			int min = r.min;
			int max = r.max;
			assert min >= -1 && max >= -1;
			List<ooregex> result = new ArrayList<ooregex>();
			// optional ?
			if ((r.min == 0 && r.max == 1)) {
				result.add(REGEXP_REPEAT.REGEXP_REPEAT(contentExpr));// Any *
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MIN(contentExpr));// AtLeastOnce +
				return result;
			}
			// Any *
			if (min == 0 && max == -1) {
				result.add(REGEXP_REPEAT.REGEXP_OPTIONAL(contentExpr));// Optional
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MIN(contentExpr));// AtLeastOnce
				return result;
			}
			assert min > -1;
			// AtLeastOnce +
			if (min == 1 && max == -1) {
				result.add(REGEXP_REPEAT.REGEXP_OPTIONAL(contentExpr));// Optional
				result.add(REGEXP_REPEAT.REGEXP_REPEAT(contentExpr));// Any
				//{1,} -> {2,}
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MIN_N(contentExpr, 2));
				return result;
			}
			// AtLeastNtimes {n,}
			if (min > 1 && max == -1) {
				//result.add(REGEXP_REPEAT.REGEXP_OPTIONAL(contentExpr));// Optional
				//result.add(REGEXP_REPEAT.REGEXP_REPEAT(contentExpr));// Any
				//result.add(REGEXP_REPEAT.REGEXP_REPEAT_MIN(contentExpr));// AtLeastOnce
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MIN_N(contentExpr, min - 1));// AtLeastN-1times
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MIN_N(contentExpr, min + 1));// AtLeastN+1times
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MINMAX_N(contentExpr,min, min)); // exactly n times
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MINMAX_N(contentExpr,0, min)); // max n times
				return result;
			}
			// exactly n times {n}
			if (min == max) {
				assert min > 0;
				if (min != 1) {
					assert min > 1;
					// (n - 1) times
					result.add(REGEXP_REPEAT.REGEXP_REPEAT_MINMAX_N(contentExpr, min - 1, max - 1));
					result.add(REGEXP_REPEAT.REGEXP_REPEAT_MINMAX_N(contentExpr, min - 1, max));
				} 
				// at max ntimes {0,n}
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MINMAX_N(contentExpr, 0,min));
				// (n + 1) times
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MINMAX_N(contentExpr, min + 1, max + 1));
				// at least ntimes {n,}
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MIN_N(contentExpr, min));
				//
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MINMAX_N(contentExpr, min, max+1));
				return result;
			}
			assert (max != -1) & max > min;
			// from n to m: {n,m}			
			if (min > 0) {
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MINMAX_N(contentExpr, min - 1, max));
			}
			result.add(REGEXP_REPEAT.REGEXP_REPEAT_MINMAX_N(contentExpr, min + 1, max));
			result.add(REGEXP_REPEAT.REGEXP_REPEAT_MINMAX_N(contentExpr, min, max + 1));
			result.add(REGEXP_REPEAT.REGEXP_REPEAT_MINMAX_N(contentExpr, min, max - 1));
			if (min == 0) {
				// {0,n} -> {n,n}
				// to be symmetric {2,2} - can be mutated to {0,2}
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MINMAX_N(contentExpr, max, max));
				// also {n,} tomke symmetric 
				result.add(REGEXP_REPEAT.REGEXP_REPEAT_MIN_N(contentExpr, max));
			}
			return result;
		}

		@Override
		public String getCode() {
			return "QC";
		}
	}

	@Override
	public String getCode() {
		return "QC";
	}
}