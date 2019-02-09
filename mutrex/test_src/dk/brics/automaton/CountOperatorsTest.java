package dk.brics.automaton;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.brics.automaton.RegExp.Kind;

public class CountOperatorsTest {

	private void compare(String rgxAsStr) {
		RegExp rgx = ExtendedRegex.getSimplifiedRegexp(rgxAsStr);
		int numOperators = RegExpCounter.countOperators(rgx);
		int sum = 0;
		for(Kind kind: RegExp.Kind.values()) {
			sum += RegExpCounter.countOperators(kind.toString(),rgx);
		}
		assertEquals(numOperators, sum);
	}

	@Test
	public void test() {
		compare("[a-z]");
		compare("[a-z]+");
		compare("[a-z]\\.");
	}
}