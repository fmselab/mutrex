package regex.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.REGEXP_UNION;
import dk.brics.automaton.oo.ToRegexString;
import dk.brics.automaton.oo.ooregex;

/**
 * the user forgot a negation in a character class [...] -> [^...]. to be more
 * precise, we generate several negation of the character classes in the []
 * 
 * In MUTATION 2017 is CCN
 *
 */
public class CharacterClassNegation extends RegexMutator {
	public static CharacterClassNegation mutator = new CharacterClassNegation();

	private CharacterClassNegation() {
		super(new CharacterClassNegationVisitor());
	}

	static class CharacterClassNegationVisitor extends RegexVisitorAdapterList {

		@Override
		public List<ooregex> visit(REGEXP_CHAR_RANGE r) {
			// build the new negated
			RegExp nr = new RegExp("[^" + r.from + "-" + r.to + "]");
			return Collections.singletonList(OORegexConverter.getOORegex(nr));
		}

		// what to convert [R1r2r3] into [^R1r2r3]
		// in regex diventa cosi' (.&~((\A|\B)))
		@Override
		public List<ooregex> visit(REGEXP_UNION r) {
			List<ooregex> partial = new ArrayList<>(super.visit(r));
			// add the negation for all
			String content = "";
			List<ooregex> parts = REGEXP_UNION.splitUnion(r);
			for (ooregex p : parts) {
				if (content.length() > 0)
					content += "|";
				content += ToRegexString.convertToRegexString(p);
			}
			// all except what is listed [^ ALL]
			RegExp nr = new RegExp("(.&~(" + content + "))");
			partial.add(OORegexConverter.getOORegex(nr));
			return partial;
		}

		@Override
		public String getCode() {
			return "CCN";
		}
	}

	@Override
	public String getCode() {
		return "CCN";
	}
}
