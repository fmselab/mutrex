package regex.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.oo.REGEXP_CHAR;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.REGEXP_CONCATENATION;
import dk.brics.automaton.oo.REGEXP_REPEAT;
import dk.brics.automaton.oo.REGEXP_SPECIALCHAR;
import dk.brics.automaton.oo.ooregex;
import dk.brics.automaton.oo.oosimpleexp;

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

		/*
		 * from tutorial: If you want to use any of these characters as a literal in a
		 * regex, you need to escape them with a backslash. If you want to match
		 * „1+1=2”, the correct regex is «1\+1=2». Otherwise, the plus sign will have a
		 * special meaning.
		 */
		static private char charQuantifier[] = { '+', '?', '*' };

		// other metachars as well like + *, ?
		@Override
		public List<ooregex> visit(REGEXP_REPEAT r) {
			String q = r.getQuantifier();
			// only if char
			if (q.length() == 1) {
				List<ooregex> result = new ArrayList<>();
				// if q is equal to a possible char
				for (char a : charQuantifier) {
					if (q.charAt(0) == a) {
						List<ooregex> insideMutations = r.getContentExpr().accept(this);
						REGEXP_CHAR rc = new REGEXP_CHAR(a);
						for (ooregex rm : insideMutations) {
							result.add(new REGEXP_CONCATENATION(rm, rc));
						}
						result.add(new REGEXP_CONCATENATION(r.getContentExpr(), rc));
					}
				}
				return result;
			} else {
				return super.visit(r);
			}
		}

		/*@Override
		public List<ooregex> visit(REGEXP_CHAR_RANGE r) {
			// put the - with escape
			String s = "[" + r.from+ "\\-"+ r.to+ "]";			
			return Collections.singletonList(OORegexConverter.getOOExtRegex(s));
		}*/
		
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