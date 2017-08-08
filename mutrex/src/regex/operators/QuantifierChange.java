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
			// optional
			if ((r.min == 0 && r.max == 1)) {
				result.add(new REGEXP_REPEAT(contentExpr, 0, 0));// Any
				result.add(new REGEXP_REPEAT(contentExpr, 1, -10));// AtLeastOnce
				return result;
			}
			// Any
			if (min == -1 && max == -1) {
				result.add(REGEXP_REPEAT.REGEXP_OPTIONAL(contentExpr));// Optional
				result.add(new REGEXP_REPEAT(contentExpr, 1,-10));// AtLeastOnce
				return result;
			}
			assert min > -1;
			// AtLeastOnce
			if (min == 1 && max == -1) {
				result.add(REGEXP_REPEAT.REGEXP_OPTIONAL(contentExpr));// Optional
				result.add(new REGEXP_REPEAT(contentExpr, 0, -1));// Any
				return result;
			}
			// AtLeastNtimes
			if (min > 1 && max == -1) {
				result.add(REGEXP_REPEAT.REGEXP_OPTIONAL(contentExpr));// Optional
				result.add(new REGEXP_REPEAT(contentExpr, 0, -1));// Any
				result.add(new REGEXP_REPEAT(contentExpr, 1,-10));// AtLeastOnce
				result.add(new REGEXP_REPEAT(contentExpr, min - 1,-20));//// AtLeastN-1times
				result.add(new REGEXP_REPEAT(contentExpr, min + 1,-20));//// AtLeastN+1times
				return result;
			}
			// exactly n times
			if (min == max) {
				assert min > 0;
				if (min > 1) {
					// (n - 1) times
					result.add(new REGEXP_REPEAT(contentExpr, min - 1, max - 1));
					// (n + 1) times
					result.add(new REGEXP_REPEAT(contentExpr, min + 1, max + 1));
				}
				return result;
			}
			if (min > 0) {
				result.add(new REGEXP_REPEAT(contentExpr, min - 1, max));
			}
			if (min < max) {
				result.add(new REGEXP_REPEAT(contentExpr, min + 1, max));
			}
			if (max != -1) {
				assert max != Integer.MAX_VALUE;
				result.add(new REGEXP_REPEAT(contentExpr, min, max + 1));
				if (max > min) {
					result.add(new REGEXP_REPEAT(contentExpr, min, max - 1));
				}
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