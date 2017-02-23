package regex.operators;

import java.util.Collections;
import java.util.List;

import dk.brics.automaton.oo.REGEXP_CHAR;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.REGEXP_COMPLEMENT;
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
		public List<ooregex> visit(REGEXP_CHAR r) {
			return Collections.singletonList((ooregex) new REGEXP_COMPLEMENT(r));
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