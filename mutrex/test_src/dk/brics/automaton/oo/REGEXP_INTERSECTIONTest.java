package dk.brics.automaton.oo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;

public class REGEXP_INTERSECTIONTest {

	@Test
	public void testGetNegatedCharClassC() {
		RegExp re = new RegExp("[^a]");
		REGEXP_INTERSECTION oore = (REGEXP_INTERSECTION) OORegexConverter.getOORegex(re);
		ooregex negatedCharClass = oore.getNegatedCharClass();
		assertTrue(negatedCharClass.toString().equals("\\a"));
	}
	@Test
	public void testGetNegatedCharClassCC() {
		RegExp re = new RegExp("[^a-c]");
		REGEXP_INTERSECTION oore = (REGEXP_INTERSECTION) OORegexConverter.getOORegex(re);
		ooregex negatedCharClass = oore.getNegatedCharClass();
		assertTrue(negatedCharClass.toString().equals("[\\a-\\c]"));
	}
	@Test
	public void testGetNegatedCharClassCC3() {
		RegExp re = new RegExp("[^a-cde]");
		REGEXP_INTERSECTION oore = (REGEXP_INTERSECTION) OORegexConverter.getOORegex(re);
		ooregex negatedCharClass = oore.getNegatedCharClass();
		assertTrue(negatedCharClass.toString().equals("(([\\a-\\c]|\\d)|\\e)"));
	}

}
