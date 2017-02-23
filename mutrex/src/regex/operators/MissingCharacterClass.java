package regex.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dk.brics.automaton.oo.REGEXP_CHAR;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.REGEXP_CONCATENATION;
import dk.brics.automaton.oo.REGEXP_REPEAT;
import dk.brics.automaton.oo.ooregex;
import dk.brics.automaton.oo.oosimpleexp;

/**
 * the user forgot [] and used A-B
 * 
 * In MUTATION 2017 is CCC
 * 
 * TODO Qual e' la differenza con PutCharsInRange?
 * Questa sembra sbagliata.
 * 
 * TODO REMOVE
 *
 */
public class MissingCharacterClass extends RegexMutator {
	public static MissingCharacterClass mutator = new MissingCharacterClass();

	private MissingCharacterClass() {
		super(new MissingCharacterClassVisitor());
	}

	private static class MissingCharacterClassVisitor extends RegexVisitorAdapterList {

		@Override
		public List<ooregex> visit(oosimpleexp r) {
			assert false;
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
				// System.out.print("Start index: " + start);
				// System.out.print(" End index: " + end + " ");
				// System.out.println(matcher.group());
			}
			return result;
		}

		@Override // if it is a concatenation
		public List<ooregex> visit(REGEXP_CONCATENATION r) {
			try {
				String e1 = ((oosimpleexp) r.exp1).s;
				REGEXP_REPEAT e2 = (REGEXP_REPEAT) r.exp2;
				REGEXP_CHAR ce2 = (REGEXP_CHAR) e2.getContentExpr();
				int e1l = e1.length();
				if (!e1.endsWith("-") || e1l < 2)
					return Collections.EMPTY_LIST;
				// ends with some char and -
				char firstChar = e1.charAt(e1l - 2);
				if (firstChar > ce2.c)
					return Collections.EMPTY_LIST;
				// the char is ok
				REGEXP_CHAR_RANGE eint = new REGEXP_CHAR_RANGE(firstChar, ce2.c);
				REGEXP_REPEAT efinal = new REGEXP_REPEAT(eint, e2.min, e2.max);
				return Collections.singletonList((ooregex) efinal);
			} catch (ClassCastException cex) {
				return Collections.EMPTY_LIST;
			}
		}
	}
}
/*
 * usando le stringhe <pre> String rs = ToRegexString.convertToRegexString(r);
 * System.out.println(rs); // somtheing like "a-"(\z) Pattern pattern =
 * Pattern.compile("\".-\"\\(\\\\.\\)"); Matcher matcher = pattern.matcher(rs);
 * // check all occurance while (matcher.find()) { int start = matcher.start();
 * int end = matcher.end(); System.out.print("Start index: " + start);
 * System.out.print(" End index: " + end + " ");
 * System.out.println(matcher.group()); //String ns = } return
 * Collections.EMPTY_LIST;
 * 
 */