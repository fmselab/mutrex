package dk.brics.automaton.oo;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.ToRegexString;

public class ToRegexStringTest {

	@Test
	public void test() {
		// very strange problems with --help
		// if its contains a doubel quote, it must be escaped !!!
		ooregex r = OORegexConverter.getOOExtRegex("\"a");
		String s = ToRegexString.convertToRegexString(r);
		System.out.println(s);
		RegExp newRegex = new RegExp(s);
	}

}
