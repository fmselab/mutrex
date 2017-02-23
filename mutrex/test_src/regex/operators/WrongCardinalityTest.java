package regex.operators;

import static regex.operators.QuantifierChange.mutator;

import java.util.Iterator;

import org.junit.Test;

import dk.brics.automaton.RegExp;
import regex.operators.RegexMutator.MutatedRegExp;

public class WrongCardinalityTest extends RegexMutationTest {

	private void test(RegExp re) {
		System.out.println("Original " + re);
		Iterator<MutatedRegExp> m = mutator.mutate(re);
		while(m.hasNext()) {
			System.out.println(m.next());
		}
	}

	@Test
	public void testAtLeastOnce() {
		RegExp re = new RegExp("a+");//min = 1, max = -1
		test(re);		
	}

	@Test
	public void testAtLeastOnce2() {
		RegExp re = new RegExp("a{1,}");//min = 1, max = -1
		test(re);
	}

	@Test
	public void testAtLeastNtimes() {
		RegExp re = new RegExp("a{4,}");//min = 4, max = -1
		test(re);
	}

	@Test
	public void testAny() {
		RegExp re = new RegExp("a*");//min = -1, max = -1
		test(re);
	}

	@Test
	public void testNoMoreThanOnce() {
		RegExp re = new RegExp("a?");
		test(re);
	}

	@Test
	public void testNtimes() {
		RegExp re = new RegExp("a{3,3}");
		test(re);
	}

	@Test
	public void testNMtimes() {
		RegExp re = new RegExp("a{3,5}");
		test(re);
	}

	@Test
	public void testNMtimesConsecutive() {
		RegExp re = new RegExp("a{3,4}");
		test(re);
	}

	@Test
	public void testMultiple() {
		RegExp re = new RegExp("a*b*");
		test(re);
	}

	@Test
	public void testNeg() {
		RegExp re = new RegExp("^a*");
		test(re);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testEmpty() {
		RegExp re = new RegExp("a{3, 2}");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testEmpty2() {
		RegExp re = new RegExp("a{-1, 2}");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testEmpty3() {
		RegExp re = new RegExp("a{0, -1}");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testEmpty4() {
		RegExp re = new RegExp("a{0, 0}");
	}
}