package dk.brics.automaton;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.Assert;

public class RegexComparatorByAutomataTest {

	@Test
	public void testCompare() {
		RegexComparatorByAutomata comparator = new RegexComparatorByAutomata();
		RegExp arg0 = ExtendedRegex.getSimplifiedRegexp("a{2,2}");
		RegExp arg1 = ExtendedRegex.getSimplifiedRegexp("(a|A){0,2}");
		// they must be different
		assertTrue(comparator.compare(arg0, arg1) != 0);
	}

}
