package regex.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.brics.automaton.oo.REGEXP_CHAR;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.REGEXP_COMPLEMENT;
import dk.brics.automaton.oo.REGEXP_INTERSECTION;
import dk.brics.automaton.oo.REGEXP_SPECIALCHAR;
import dk.brics.automaton.oo.ooregex;

/**
 * the user forgot a negation
 * 
 * In MUTATION 2017 is NA
 *
 */
public class NegationAddition extends RegexMutator {
	public static NegationAddition mutator = new NegationAddition();

	private NegationAddition() {
		super(new NegationAdditionVisitor());
	}

	static class NegationAdditionVisitor extends RegexVisitorAdapterList {

		@Override
		public List<ooregex> visit(REGEXP_CHAR_RANGE r) {
			return Collections.singletonList((ooregex) new REGEXP_COMPLEMENT(r));
		}
	
		@Override
		public List<ooregex> visit(REGEXP_INTERSECTION r) {
			//System.out.println(r.exp1.getClass().getSimpleName());
			//System.out.println(r.exp2.getClass().getSimpleName());
			ArrayList<ooregex> list = new ArrayList<ooregex>();
			if(r.exp1 instanceof REGEXP_SPECIALCHAR && r.exp2 instanceof REGEXP_COMPLEMENT) {
				list.add(((REGEXP_COMPLEMENT)r.exp2).getContentExpr());
			}
			list.addAll(super.visit(r));
			return list;
		}

		@Override
		public List<ooregex> visit(REGEXP_CHAR r) {
			return Collections.singletonList((ooregex) new REGEXP_COMPLEMENT(r));
		}

		@Override
		public List<ooregex> visit(REGEXP_COMPLEMENT r) {
			// complement of complement
			ooregex contentExpr = r.getContentExpr();
			// only if they would be actually mutated
			if (contentExpr instanceof REGEXP_CHAR_RANGE || contentExpr instanceof REGEXP_CHAR)
				return Collections.singletonList(contentExpr);
			else
				return Collections.EMPTY_LIST;
		}

		@Override
		public String getCode() {
			return "NA";
		}
	}

	@Override
	public String getCode() {
		return "NA";
	}
}