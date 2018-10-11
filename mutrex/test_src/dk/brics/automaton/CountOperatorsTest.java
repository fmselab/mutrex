package dk.brics.automaton;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.brics.automaton.RegExp.Kind;

public class CountOperatorsTest {

	private void compare(String rgxAsStr) {
		RegExp rgx = ExtendedRegex.getSimplifiedRegexp(rgxAsStr);
		int numOperators = rgx.countOperators();
		RegExpForCounting rgxForCount = new RegExpForCounting(rgx);
		int sum = 0;
		for(Kind kind: RegExp.Kind.values()) {
			sum += rgxForCount.countOperators(kind);
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