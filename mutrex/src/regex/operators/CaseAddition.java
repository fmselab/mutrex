package regex.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.oo.REGEXP_CHAR;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.REGEXP_CONCATENATION;
import dk.brics.automaton.oo.REGEXP_UNION;
import dk.brics.automaton.oo.oobinregex;
import dk.brics.automaton.oo.ooregex;
import dk.brics.automaton.oo.oosimpleexp;

/**
 * we want to make the expression case unsensitive
 *
 * In MUTATION 2017 is CA
 */
public class CaseAddition extends RegexMutator {
	public static CaseAddition mutator = new CaseAddition();

	private CaseAddition() {
		super(new CaseAdditionVisitor());
	}

	static class CaseAdditionVisitor extends RegexVisitorAdapterList {

		@Override
		public List<ooregex> visit(REGEXP_CHAR_RANGE r) {
			// add also the
			CaseChar fromCase = caseChar(r.from);
			if (fromCase == CaseChar.NOT_ALPHABETIC)
				return Collections.EMPTY_LIST;
			CaseChar toCase = caseChar(r.to);
			if (toCase == CaseChar.NOT_ALPHABETIC)
				return Collections.EMPTY_LIST;
			if (fromCase == toCase) {
				REGEXP_CHAR_RANGE ooRegexChangedCase = new REGEXP_CHAR_RANGE(changeCase(r.from), changeCase(r.to));
				return Collections.singletonList((ooregex) new REGEXP_UNION(r, ooRegexChangedCase));
			}
			return Collections.EMPTY_LIST;
		}

		@Override
		public List<ooregex> visit(REGEXP_CHAR r) {
			Character changeCase = changeCase(r.c);
			if (changeCase == null) {
				return Collections.EMPTY_LIST;
			}
			ooregex ooRegexChangedCase = new REGEXP_CHAR(changeCase);
			return Collections.singletonList((ooregex) new REGEXP_UNION(r, ooRegexChangedCase));
		}

		@Override
		public List<ooregex> visit(oosimpleexp r) {
			List<ooregex> result = new ArrayList<>();
			for (int i = 0; i < r.s.length(); i++) {
				List<ooregex> parts = new ArrayList<>();
				if (i > 0) {
					String first = r.s.substring(0, i);
					parts.add(new oosimpleexp(first));
				}
				char mid = r.s.charAt(i);
				Character nc = changeCase(mid);
				// if the char cannot be changed in the case, leave as it is
				if (nc == null)
					continue;
				parts.add(OORegexConverter.getOORegex("[" + mid + nc + "]"));
				if (i < r.s.length() - 1) {
					String last = r.s.substring(i + 1, r.s.length());
					parts.add(new oosimpleexp(last));
				}
				// only one char
				if (parts.size() == 1)
					result.add(parts.get(0));
				else 
					result.add(oobinregex.makeBinExpression(REGEXP_CONCATENATION.class, parts));
			}
			return result;
		}

		@Override
		public String getCode() {
			return "CA";
		}
	}

	public static CaseChar caseChar(char c) {
		if (Character.isLowerCase(c))
			return CaseChar.LOWERCASE;
		if (Character.isUpperCase(c))
			return CaseChar.UPPERCASE;
		return CaseChar.NOT_ALPHABETIC;
	}

	public static Character changeCase(char c) {
		if (Character.isLowerCase(c))
			return Character.toUpperCase(c);
		if (Character.isUpperCase(c))
			return Character.toLowerCase(c);
		return null;
	}

	enum CaseChar {
		UPPERCASE, LOWERCASE, NOT_ALPHABETIC
	}

	@Override
	public String getCode() {
		return "CA";
	}
}
