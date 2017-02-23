package regex.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.ooregex;
import dk.brics.automaton.oo.oosimpleexp;

/**
 * the user forgot [] and used "A-B" (a simple string) instead of [A-B]
 *
 * In MUTATION 2017 is CCC
 * 
 * TODO Qual e' la differenza con MissingCharacterClass?
 * Questa sembra migliore.
 *
 */
public class PutCharsInRange extends RegexMutator {
	public static PutCharsInRange mutator = new PutCharsInRange();

	private PutCharsInRange() {
		super(new OutOfOneInCharRangeVisitor());
	}

	static class OutOfOneInCharRangeVisitor extends RegexVisitorAdapterList {

		@Override
		public List<ooregex> visit(oosimpleexp r) {
			String s = r.s;
			if (s.matches("\\w-\\w")) {
				// three chars
				assert s.length() == 3;
				assert s.charAt(1) == '-';
				char start = s.charAt(0);
				char end = s.charAt(2);
				if (start < end) {
					return Collections.singletonList((ooregex) new REGEXP_CHAR_RANGE(start, end));
				}
			}

			//from MissingCharacterClass
			List<ooregex> result = new ArrayList<>();
			// find pattern of type C1-C2 where C1 and C2 are consecutive chars
			Pattern pattern = Pattern.compile(".-.");
			Matcher matcher = pattern.matcher(r.s);
			// check all occurrences
			while (matcher.find()) {
				int start = matcher.start();
				char startChar = r.s.charAt(start);
				int end = matcher.end();
				char endChar = r.s.charAt(end - 1);
				if (endChar > startChar) {
					// build the
					REGEXP_CHAR_RANGE rint = new REGEXP_CHAR_RANGE(startChar, endChar);
					result.add(rint);
				}
			}
			return result;
		}
	}
}