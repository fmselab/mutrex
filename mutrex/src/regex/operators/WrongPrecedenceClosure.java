package regex.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.brics.automaton.oo.REGEXP_CONCATENATION;
import dk.brics.automaton.oo.REGEXP_REPEAT;
import dk.brics.automaton.oo.oobinregex;
import dk.brics.automaton.oo.ooregex;

/**
 * Because the closure operator is of highest precedence, it is often necessary
 * to put parentheses around the R, and write (R)*.
 * 
 * TODO nome closure ???
 */
public class WrongPrecedenceClosure extends RegexMutator {

	public static WrongPrecedenceClosure mutator = new WrongPrecedenceClosure();

	private WrongPrecedenceClosure() {
		super(new WrongPrecedenceClosureVisitor());
	}

	static class WrongPrecedenceClosureVisitor extends RegexVisitorAdapterList {

		@Override
		public List<ooregex> visit(REGEXP_CONCATENATION r) {
			List<ooregex> epres = oobinregex.split(r, REGEXP_CONCATENATION.class);
			//System.out.println(epres);
			List<ooregex> result = new ArrayList<>();
			for (int i = 0; i < epres.size() - 1; i++) {
				List<ooregex> checkPair = checkPair(epres.get(i), epres.get(i + 1));
				// rebuild the concatenate expression
				// where instead of left and right, add
				for (ooregex br : checkPair) {
					result.add(unite(epres, i, br));
				}
			}
			return result;
		}

		private ooregex unite(List<ooregex> epres, int mid, ooregex br) {
			//System.out.println("unite " + epres + " mid " + mid + " br" + br);
			ooregex result = null;
			// copy those on the left
			for (int i = 0; i < mid; i++) {
				if (result == null)
					result = epres.get(i);
				else
					result = new REGEXP_CONCATENATION(result, epres.get(i));
			}
			// copy epres
			if (result == null)
				result = br;
			else
				result = new REGEXP_CONCATENATION(result, br);
			// copy on the right
			for (int i = mid + 2; i < epres.size(); i++) {
				if (result == null)
					result = epres.get(i);
				else
					result = new REGEXP_CONCATENATION(result, epres.get(i));
			}
			return result;
		}

		// left and right are two consecutive regex in a concatenation
		private List<ooregex> checkPair(ooregex left, ooregex right) {
			if (right instanceof REGEXP_REPEAT) {
				REGEXP_REPEAT rr = (REGEXP_REPEAT) right;
				String quantifier = rr.getQuantifier();
				if (quantifier.equals("*") || quantifier.equals("?") || quantifier.equals("+")) {
					System.out.println(rr);
					List<ooregex> result = new ArrayList<>();
					List<ooregex> recursive = left.accept(this);
					for (ooregex i : recursive) {
						result.add(new REGEXP_CONCATENATION(i, right));
					}
					ooregex concatenate = new REGEXP_CONCATENATION(left, rr.getContentExpr());
					result.add(REGEXP_REPEAT.sameREPEAT_Type(rr, concatenate));
					return result;
				}
			}
			return Collections.EMPTY_LIST;
		}
	}

	@Override
	public String getCode() {
		return "WP";
	}

}
