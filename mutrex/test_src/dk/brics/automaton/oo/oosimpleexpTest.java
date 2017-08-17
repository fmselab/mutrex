package dk.brics.automaton.oo;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;

public class oosimpleexpTest {

	@Test
	public void testOosimpleexp() {
		// both generate exaclty the same expression???
		ooregex abc = OORegexConverter.getOORegex("abc");
		System.out.println(((oosimpleexp)(abc)).s);
		abc = OORegexConverter.getOORegex("\"abc\"");
		System.out.println(((oosimpleexp)(abc)).s);
		abc = OORegexConverter.getOORegex("\\\"abc\\\"");
		System.out.println(((oosimpleexp)(abc)).s);
	}

}
