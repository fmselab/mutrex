package dk.brics.automaton;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExtendedRegexTest {

	@Test
	public void test_w() {
		RegExp res = ExtendedRegex.getSimplifiedRegexp("jjj\\w");
		Automaton automaton = res.toAutomaton();
		assertTrue(automaton.run("jjja"));		
	}
	@Test
	public void test_s() {
		RegExp res = ExtendedRegex.getSimplifiedRegexp("\\sA");
		Automaton automaton = res.toAutomaton();
		assertTrue(automaton.run(" A"));		
		assertTrue(automaton.run("\tA"));		
		assertFalse(automaton.run("A"));		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRegExp(){
		new RegExp("\"a");
	}
}