package regex.operators;

import java.util.Collections;
import java.util.List;

import dk.brics.automaton.oo.REGEXP_CHAR;
import dk.brics.automaton.oo.REGEXP_SPECIALCHAR;
import dk.brics.automaton.oo.ooregex;
import dk.brics.automaton.oo.oosimpleexp;

/**
 * substitutes a char (. # @) with the correspondent metachar. The fault is the
 * the user has wrongly used a char instead of a metachar.
 *
 * In MUTATION 2017 is C2M
 * 
 */
public class Char2MetaChar extends RegexMutator {
	public static Char2MetaChar mutator = new Char2MetaChar();

	private Char2MetaChar() {
		super(new Char2MetaCharVisitor());
	}

	private static class Char2MetaCharVisitor extends RegexVisitorAdapterList {
		@Override
		public List<ooregex> visit(REGEXP_CHAR r) {
			// if equal to some metachar
			if (r.isMetaChar()) {
				// System.out.println(r);
				return Collections.singletonList((ooregex) new REGEXP_SPECIALCHAR(r.c));
			} else {
				// not a meta char
				return Collections.EMPTY_LIST;
			}
		}

		@Override
		public List<ooregex> visit(oosimpleexp r) {
			// TODO???
			if (r.s.contains("-")) {
				return Collections.EMPTY_LIST;
			}
			return Collections.EMPTY_LIST;
		}

		@Override
		public String getCode() {
			return "C2M";
		}
	}

	@Override
	public String getCode() {
		return "C2M";
	}
}
