package regex.operators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static regex.operators.QuantifierChange.mutator;

import java.util.List;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.oo.REGEXP_REPEAT;
import dk.brics.automaton.oo.ooregex;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

public class QuantifierChangeTest extends RegexMutationTest {

	private List<MutatedRegExp> test(RegExp re) {
		System.out.println("Original " + re);
		List<MutatedRegExp> list = IteratorUtils.iteratorToList(mutator.mutate(re));
		System.out.println(list);
		return list;
	}

	@Test
	public void testAtLeastOnce() {
		RegExp re = new RegExp("a+");// min = 1, max = -1
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		assertTrue(OneEqualTo(m, "a*"));
	}

	@Test
	public void testAtLeastOnce2() {
		RegExp re = new RegExp("a{1,}");// min = 1, max = 0
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		assertTrue(OneEqualTo(m, "a*"));
	}

	@Test
	public void testAtLeastNtimes() {
		RegExp re = new RegExp("a{4,}");// min = 4, max = -1
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		assertTrue(OneEqualTo(m, "a*"));
	}

	@Test
	public void testAny() {
		RegExp re = new RegExp("a*");// min = -1, max = -1
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_REPEAT;
		REGEXP_REPEAT rr = (REGEXP_REPEAT) oore;
		assertEquals(0, rr.min);
		assertEquals(0, rr.max);
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		assertTrue(OneEqualTo(m, "a*"));
	}

	@Test
	public void testNoMoreThanOnce() {
		RegExp re = new RegExp("a?");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_REPEAT;
		REGEXP_REPEAT rr = (REGEXP_REPEAT) oore;
		assertEquals(1, rr.min);
		assertEquals(0, rr.max);
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		assertTrue(OneEqualTo(m, "a*"));
	}

	@Test
	public void testNtimes() {
		RegExp re = new RegExp("a{3,3}");
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		assertTrue(OneEqualTo(m, "a*"));
	}

	@Test
	public void testNMtimes() {
		RegExp re = new RegExp("a{3,5}");
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		assertTrue(OneEqualTo(m, "a*"));
	}

	@Test
	public void testNMtimesConsecutive() {
		RegExp re = new RegExp("a{3,4}");
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		assertTrue(OneEqualTo(m, "a*"));
	}

	@Test
	public void testMultiple() {
		RegExp re = new RegExp("a*b*");
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		assertTrue(OneEqualTo(m, "a*"));
	}

	@Test
	public void testNeg() {
		RegExp re = new RegExp("^a*");
		List<MutatedRegExp> m = test(re);
		assertEquals(2, m.size());
		assertTrue(OneEqualTo(m, "a*"));
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
}