package regex.operators;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

/*
 *  */
public class WrongPrecTest extends RegexMutationTest {
	static WrongPrecedenceClosure mp = WrongPrecedenceClosure.mutator;

	@BeforeClass
	static public void setup() {
		Logger.getLogger(OORegexConverter.class.getName()).setLevel(Level.OFF);
	}

	
	@Test
	public void testMutaterepeat() {
		RegExp re = new RegExp("ab*");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertEquals(1,m.size());
		assertOneEqualTo(m, "(ab)*");
	}
	@Test
	public void testMutaterepeat2() {
		RegExp re = new RegExp("ab*cd*");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		System.out.println(m);
		assertEquals(2,m.size());
		assertOneEqualTo(m, " ab*(cd)*");
	}
	
	@Test
	public void testMutaterepeatMid() {
		RegExp re = new RegExp("ab*Pippo");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertEquals(1,m.size());
		assertOneEqualTo(m, "(ab)*Pippo");
	}

}
