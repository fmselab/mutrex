package dk.brics.automaton.oo;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;

public class oosimpleexpTest {

	@Test
	public void testOosimpleexp() {
		ooregex abc = OORegexConverter.getOORegex("abc");
		System.out.println(abc.getClass());
		abc = OORegexConverter.getOORegex("\"abc\"");
		System.out.println(abc.getClass());
	}

}
