package dk.brics.automaton.oo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;

public class REGEX_SPECIALCHARTest {

	@Test
	public void testREGEX_SPECIALCHAR() {
		RegExp re = new RegExp(".");
		ooregex oore = OORegexConverter.getOORegex(re);
		assertTrue(oore instanceof REGEXP_SPECIALCHAR);
	}

	@Test
	public void testREGEX_SPECIALCHAR2() {
		RegExp re = new RegExp("a.");
		ooregex oore = OORegexConverter.getOORegex(re);
		assertTrue(oore instanceof REGEXP_CONCATENATION);
		assertTrue(((REGEXP_CONCATENATION)oore).exp2 instanceof REGEXP_SPECIALCHAR);
	}

	@Test
	public void testREGEX_oosimpleexp() {
		RegExp re = new RegExp("a\\.");
		ooregex oore = OORegexConverter.getOORegex(re);
		assertTrue(oore instanceof oosimpleexp);
		//ToRegexString.convertToRegexString()
	}
	
}
