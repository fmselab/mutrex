package regex.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.REGEXP_UNION;
import dk.brics.automaton.oo.ooregex;

/**
 *
 * In MUTATION 2017 is CCA
 *
 */
public class CharClassAddition extends RegexMutator {
	public static CharClassAddition mutator = new CharClassAddition();
	private static Logger logger = Logger.getLogger(CharClassAddition.class.getName());
	private static REGEXP_CHAR_RANGE[] intervals = new REGEXP_CHAR_RANGE[] {
			(REGEXP_CHAR_RANGE) OORegexConverter.getOORegex(new RegExp("[a-z]")),
			(REGEXP_CHAR_RANGE) OORegexConverter.getOORegex(new RegExp("[A-Z]")),
			(REGEXP_CHAR_RANGE) OORegexConverter.getOORegex(new RegExp("[0-9]")) };

	private CharClassAddition() {
		super(new IntervalAdditionVisitor());
	}

	static class IntervalAdditionVisitor extends RegexVisitorAdapterList {

		/* (non-Javadoc)
		 * @see regexrepair.operators.RegexVisitorAdapterList#visit(dk.brics.automaton.oo.REGEXP_CHAR_RANGE)
		 * 
		 * It can generate equivalent mutants.
		 * We should avoid building something as [a-z]|[A-Z]
		 * 
		 */
		@Override
		public List<ooregex> visit(REGEXP_CHAR_RANGE r) {
			ArrayList<ooregex> result = new ArrayList<ooregex>();
			for (REGEXP_CHAR_RANGE i : intervals) {
				if (!r.toString().equals(i.toString())) {
					result.add(new REGEXP_UNION(r, i));
				}
			}
			return result;
		}

		@Override
		public String getCode() {
			return "CCA";
		}
	}

	@Override
	public String getCode() {
		return "CCA";
	}
}
