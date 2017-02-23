package regex.operators;

import java.util.Collections;
import java.util.List;

import dk.brics.automaton.oo.REGEXP_CHAR;
import dk.brics.automaton.oo.REGEXP_SPECIALCHAR;
import dk.brics.automaton.oo.ooregex;

/**
 * the expression contains a metachar that is substituted by a char. The error
 * is the use of a metachar instead of a char
 *
 * In MUTATION 2017 is M2C
 *
 */
public class MetaChar2Char extends RegexMutator {
	public static MetaChar2Char mutator = new MetaChar2Char();

	private MetaChar2Char() {
		super(new MetaChar2CharVisitor());
	}

	private static class MetaChar2CharVisitor extends RegexVisitorAdapterList {
		// metachars are in REGEXP_SPECIALCHAR
		@Override
		public List<ooregex> visit(REGEXP_SPECIALCHAR r) {
			// convert to simple char class
			return Collections.singletonList((ooregex) new REGEXP_CHAR(r.sc));
		}

		@Override
		public String getCode() {
			return "M2C";
		}
	}

	@Override
	public String getCode() {
		return "M2C";
	}
}