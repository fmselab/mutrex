package regex.operators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static regex.operators.QuantifierChange.mutator;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.oo.REGEXP_REPEAT;
import dk.brics.automaton.oo.ooregex;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;
import regex.utils.RegexExamplesTaker;

public class QuantifierChangeTest extends RegexMutationTest {

	@BeforeClass
	static public void setup() {
		Logger.getLogger(OORegexConverter.class.getName()).setLevel(Level.OFF);
	}

	
	private List<MutatedRegExp> test(RegExp re) {
		System.out.println("Original " + re);
		List<MutatedRegExp> list = IteratorUtils.iteratorToList(mutator.mutate(re));
		System.out.println(list);
		return list;
	}

	@Test
	public void testAtLeastOnce() {
		RegExp re = new RegExp("a+");// min = 1, max = -1
		testAtLeastOnceHelper(re);
	}

	@Test
	public void testAtLeastOnce2() {
		RegExp re = new RegExp("a{1,}");// min = 1, max = -1
		testAtLeastOnceHelper(re);
	}

	private void testAtLeastOnceHelper(RegExp re) {
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		assertTrue(OneEqualTo(m, "a*"));
		assertTrue(OneEqualTo(m, "a?"));
	}


	@Test
	public void testAtLeastNtimes() {
		RegExp re = new RegExp("a{4,}");// min = 4, max = -1
		List<MutatedRegExp> m = test(re);
		System.out.println(m);
		assertEquals(4, m.size());
		assertTrue(OneEqualTo(m, "a{3,}"));
		assertTrue(OneEqualTo(m, "a{5,}"));
		assertTrue(OneEqualTo(m, "a{0,4}"));
		assertTrue(OneEqualTo(m, "a{4}"));
	}

	@Test
	public void testAny() {
		RegExp re = new RegExp("a*");
		testAnyHelper(re);
	}

	@Test
	public void testAny2() {
		RegExp re = new RegExp("a{0,}");
		testAnyHelper(re);
	}


	private void testAnyHelper(RegExp re) {
		// min = 0, max = -1
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_REPEAT;
		REGEXP_REPEAT rr = (REGEXP_REPEAT) oore;
		assertEquals(0, rr.min);
		assertEquals(-1, rr.max); // ininite 
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		System.out.println(m);
		assertTrue(OneEqualTo(m, "a+"));
		assertTrue(OneEqualTo(m, "a?"));
	}

	@Test
	public void testNoMoreThanOnce() {
		RegExp re = new RegExp("a?");
		testNoMoreThanOnceHelper(re);
	}

	@Test
	public void testNoMoreThanOnce2() {
		RegExp re = new RegExp("a{0,1}")  ;
		testNoMoreThanOnceHelper(re);
	}

	// min = 0, max = 1
	private void testNoMoreThanOnceHelper(RegExp re) {
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_REPEAT;
		REGEXP_REPEAT rr = (REGEXP_REPEAT) oore;
		assertEquals(0, rr.min);
		assertEquals(1, rr.max);
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		System.out.println(m);
		assertTrue(OneEqualTo(m, "a+"));
		assertTrue(OneEqualTo(m, "a*"));
	}

	
	@Test
	public void testNtimes() {
		RegExp re = new RegExp("a{3}");
		//RegExp re = new RegExp("a{3,3}");
		testNtimesHelper(re);
	}
	@Test
	public void testNtimes2() {
		RegExp re = new RegExp("a{3,3}");
		testNtimesHelper(re);
	}


	private void testNtimesHelper(RegExp re) {
		List<MutatedRegExp> m = test(re);
		assertEquals(4, m.size());
		assertTrue(OneEqualTo(m, "a{2}"));
		assertTrue(OneEqualTo(m, "a{4}"));
		assertTrue(OneEqualTo(m, "a{3,}"));
		assertTrue(OneEqualTo(m, "a{0,3}"));
	}

	@Test
	public void testNMtimes() {
		RegExp re = new RegExp("a{3,5}");
		List<MutatedRegExp> m = test(re);
		assertEquals(4, m.size());
		System.out.println(m);
		assertTrue(OneEqualTo(m, "a{2,5}"));
		assertTrue(OneEqualTo(m, "a{4,5}"));
		assertTrue(OneEqualTo(m, "a{3,4}"));
		assertTrue(OneEqualTo(m, "a{3,6}"));
	}

	@Test
	public void testNMtimesConsecutive() {
		RegExp re = new RegExp("a{3,4}");
		List<MutatedRegExp> m = test(re);
		assertEquals(4, m.size());
	}

	@Test
	public void testMultiple() {
		RegExp re = new RegExp("a*b*");
		List<MutatedRegExp> m = test(re);
		assertEquals(4, m.size());
		System.out.println(m);
		assertTrue(OneEqualTo(m, "a?b*"));
		assertTrue(OneEqualTo(m, "a+b*"));
		assertTrue(OneEqualTo(m, "a*b?"));
		assertTrue(OneEqualTo(m, "a*b+"));		
	}

	@Test
	public void testNeg() {
		RegExp re = new RegExp("^a*");
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		assertTrue(OneEqualTo(m, "^a?"));
		assertTrue(OneEqualTo(m, "^a+"));
	}

	@Test
	public void testExactlyOne() {
		RegExp re = new RegExp("a{1}")  ;
		List<MutatedRegExp> m = test(re);
		assertEquals(3, m.size());
		assertTrue(OneEqualTo(m, "a{2}"));
		assertTrue(OneEqualTo(m, "a{1,}"));
		assertTrue(OneEqualTo(m, "a{0,1}"));
	}
	
	@Test
	public void testmin0() {
		RegExp re = new RegExp("a{0,10}")  ;
		List<MutatedRegExp> m = test(re);
		assertEquals(3, m.size());
		assertTrue(OneEqualTo(m, "a{1,10}"));
		assertTrue(OneEqualTo(m, "a{0,9}"));
		assertTrue(OneEqualTo(m, "a{0,11}"));
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testEmpty() {
		new RegExp("a{3, 2}");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmpty2() {
		new RegExp("a{-1, 2}");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmpty3() {
		new RegExp("a{0, -1}");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmpty4() {
		new RegExp("a{0, 0}");
	}

	@Test
	public void testR2() throws IOException {
		RegExp re = RegexExamplesTaker.readExampleRegex("someExamples", "r2");
		List<MutatedRegExp> m = test(re);
	}
}