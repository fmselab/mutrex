package regex.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.brics.automaton.oo.REGEXP_CONCATENATION;
import dk.brics.automaton.oo.REGEXP_INTERSECTION;
import dk.brics.automaton.oo.REGEXP_REPEAT;
import dk.brics.automaton.oo.oobinregex;
import dk.brics.automaton.oo.ooregex;

/**
 * [^...] should be [^...] or none.
 * 
 * In MUTATION 2017 is NCCO
 */
public class NegatedCharacterClassToOptional extends RegexMutator {
	public static NegatedCharacterClassToOptional mutator = new NegatedCharacterClassToOptional();

	private NegatedCharacterClassToOptional() {
		super(new NegatedCharacterClassToOptionalVisitor());
	}

	static class NegatedCharacterClassToOptionalVisitor extends RegexVisitorAdapterList {

		@Override
		public List<ooregex> visit(REGEXP_INTERSECTION r) {
			ooregex rcontent = r.getNegatedCharClass();
			if (rcontent == null)
				return Collections.EMPTY_LIST;
			// build the optional
			return Collections.singletonList((ooregex) REGEXP_REPEAT.REGEXP_OPTIONAL(r));
		}

		@Override
		public List<ooregex> visit(REGEXP_CONCATENATION r) {
			// only the last one in a concatenation
			/*
			 * List<ooregex> m2 = r.exp2.accept(this); assert m2 != null; // if
			 * both have not mutated the content if (m2.isEmpty()) { return m2;
			 * } List<ooregex> result = new ArrayList<>(); for (ooregex r2 : m2)
			 * { result.add(oobinregex.makeBinExpression(REGEXP_CONCATENATION.
			 * class, r.exp1, r2)); } return result;
			 */

			List<ooregex> m1 = r.exp1.accept(this);
			assert m1 != null;
			List<ooregex> result = new ArrayList<>();
			for (ooregex r1 : m1) {
				result.add(oobinregex.makeBinExpression(REGEXP_CONCATENATION.class, r1, r.exp2));
			}

			List<ooregex> m2 = r.exp2.accept(this);
			assert m2 != null;
			for (ooregex r2 : m2) {
				result.add(oobinregex.makeBinExpression(REGEXP_CONCATENATION.class, r.exp1, r2));
			}
			return result;
		}

		@Override
		public String getCode() {
			return "NCCO";
		}
	}

	@Override
	public String getCode() {
		return "NCCO";
	}
}