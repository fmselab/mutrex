package dk.brics.automaton.oo;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;

public class CharacterClassTest {

	@Test
	public void testCharclassInterval() {
		RegExp re = new RegExp("[a-b]");
		ooregex oore = OORegexConverter.getOORegex(re);
		System.out.println(oore);
		System.out.println(oore.getClass().getSimpleName());
	}

	@Test
	public void testCharclassUnion() {
		RegExp re = new RegExp("[ab]");
		ooregex oore = OORegexConverter.getOORegex(re);
		System.out.println(oore);
		System.out.println(oore.getClass().getSimpleName());
	}

	@Test
	public void testCharclassString() {
		RegExp re = new RegExp("ab");
		ooregex oore = OORegexConverter.getOORegex(re);
		System.out.println(oore);
		System.out.println(oore.getClass().getSimpleName());
	}
}
