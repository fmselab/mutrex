package dk.brics.automaton.oo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;

public class CharacterClassTest {

	@Test
	public void testCharclassInterval() {
		RegExp re = new RegExp("[a-b]");
		ooregex oore = OORegexConverter.getOORegex(re);
		System.out.println(oore);
	}

	@Test
	public void testCharclassUnion() {
		RegExp re = new RegExp("[ab]");
		ooregex oore = OORegexConverter.getOORegex(re);
		System.out.println(oore);
	}
}
