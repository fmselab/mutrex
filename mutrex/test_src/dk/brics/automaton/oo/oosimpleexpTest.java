package dk.brics.automaton.oo;

import static org.junit.Assert.fail;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;

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

	@Test
	public void testOosimpleexpWMetaChar() {
		ooregex abc = oosimpleexp.createoosimpleexp("a\\+");
		System.out.println(((oosimpleexp)(abc)).s);
		RegExp r = new RegExp("a\\+");
		System.out.println(r.toAutomaton().run("a+"));
		try {
			oosimpleexp.createoosimpleexp("a+");
			fail();
		}catch (AssertionError e) {
		}
	}

}
