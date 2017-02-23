package regex.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.oo.REGEXP_CHAR;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.REGEXP_CONCATENATION;
import dk.brics.automaton.oo.oobinregex;
import dk.brics.automaton.oo.ooregex;
import dk.brics.automaton.oo.oosimpleexp;
import regex.operators.CaseAddition.CaseChar;

/**
 * TODO
 * 
 * It changes the case.
 *
 * In MUTATION 2017 is CC
 */
public class CaseChange extends RegexMutator {
	public static CaseChange mutator = new CaseChange();

	private CaseChange() {
		super(new CaseChangeVisitor());
	}

	static class CaseChangeVisitor extends RegexVisitorAdapterList {

		@Override
		public List<ooregex> visit(REGEXP_CHAR_RANGE r) {
			// add also the
			CaseChar fromCase = CaseAddition.caseChar(r.from);
			if (fromCase == CaseChar.NOT_ALPHABETIC)
				return Collections.EMPTY_LIST;
			CaseChar toCase = CaseAddition.caseChar(r.to);
			if (toCase == CaseChar.NOT_ALPHABETIC)
				return Collections.EMPTY_LIST;
			if (fromCase == toCase) {
				REGEXP_CHAR_RANGE ooRegexChangedCase = new REGEXP_CHAR_RANGE(CaseAddition.changeCase(r.from),
						CaseAddition.changeCase(r.to));
				return Collections.singletonList((ooregex) ooRegexChangedCase);
			}
			return Collections.EMPTY_LIST;
		}

		@Override
		public List<ooregex> visit(REGEXP_CHAR r) {
			Character changeCase = CaseAddition.changeCase(r.c);
			if (changeCase == null) {
				return Collections.EMPTY_LIST;
			}
			ooregex ooRegexChangedCase = new REGEXP_CHAR(changeCase);
			return Collections.singletonList(ooRegexChangedCase);
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
				Character nc = CaseAddition.changeCase(mid);
				// if the char cannot be changed in the case, leave as it is
				if (nc == null)
					continue;
				parts.add(OORegexConverter.getOORegex(nc.toString()));
				if (i < r.s.length() - 1) {
					String last = r.s.substring(i + 1, r.s.length());
					parts.add(new oosimpleexp(last));
				}
				result.add(oobinregex.makeBinExpression(REGEXP_CONCATENATION.class, parts));
			}
			return result;
		}

		@Override
		public String getCode() {
			return "CC";
		}
	}

	@Override
	public String getCode() {
		return "CC";
	}
}