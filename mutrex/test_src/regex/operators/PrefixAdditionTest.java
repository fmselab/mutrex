package regex.operators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;
import dk.brics.automaton.oo.REGEXP_UNION;
import dk.brics.automaton.oo.ooregex;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

public class PrefixAdditionTest extends RegexMutationTest {
	static PrefixAddition mp = PrefixAddition.mutator;

	@Test
	public void testMutateZeroOrMore() {
		// with 0 no mutation is applied
		zeroMore("[a-zA-Z0-9]*");
	}

	@Test
	public void testMutateZeroOrMore2() {
		// with 0 no mutation is applied
		zeroMore("[a-zA-Z0-9]{0,}");
	}

	private void zeroMore(String rexps) {
		RegExp re = new RegExp(rexps);
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertEquals(3, m.size());
		assertOneEqualTo(m, "[a-zA-Z][a-zA-Z0-9]*");
		assertOneEqualTo(m, "[a-z0-9][a-zA-Z0-9]*");
		assertOneEqualTo(m, "[A-Z0-9][a-zA-Z0-9]*");
	}

	@Test
	public void testMutatePlus() {
		plus("[a-zA-Z0-9]+");
	}

	@Test
	public void testMutatePlus0() {
		plus("[a-zA-Z0-9]{1,}");
	}

	private void plus(String s) {
		RegExp re = new RegExp(s);
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertOneEqualTo(m, "[a-zA-Z][a-zA-Z0-9]*");
		assertOneEqualTo(m, "[a-z0-9][a-zA-Z0-9]*");
		assertOneEqualTo(m, "[A-Z0-9][a-zA-Z0-9]*");
	}

	@Test
	public void testMutateN() {
		enne("[a-zA-Z0-9]{2}");
	}

	@Test
	public void testMutateNN() {
		enne("[a-zA-Z0-9]{2,2}");
	}

	private void enne(String s) {
		RegExp re = new RegExp(s);
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertOneEqualTo(m, "[a-zA-Z][a-zA-Z0-9]{1}");
		assertOneEqualTo(m, "[a-z0-9][a-zA-Z0-9]{1}");
		assertOneEqualTo(m, "[A-Z0-9][a-zA-Z0-9]{1}");
	}


	@Test
	public void testMutateNGT1() {
		RegExp re = new RegExp("[a-zA-Z0-9]{2,}");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertOneEqualTo(m, "[a-zA-Z][a-zA-Z0-9]{1,}");
		assertOneEqualTo(m, "[a-z0-9][a-zA-Z0-9]{1,}");
		assertOneEqualTo(m, "[A-Z0-9][a-zA-Z0-9]{1,}");
	}

	@Test
	public void testMutateNM() {
		RegExp re = new RegExp("[a-zA-Z0-9]{2,6}");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertOneEqualTo(m, "[a-zA-Z][a-zA-Z0-9]{1,5}");
		assertOneEqualTo(m, "[a-z0-9][a-zA-Z0-9]{1,5}");
		assertOneEqualTo(m, "[A-Z0-9][a-zA-Z0-9]{1,5}");
	}

	@Test
	public void testMutate0M() {
		RegExp re = new RegExp("[a-zA-Z0-9]{0,5}");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertOneEqualTo(m, "[a-zA-Z][a-zA-Z0-9]{0,4}");
		assertOneEqualTo(m, "[a-z0-9][a-zA-Z0-9]{0,4}");
		assertOneEqualTo(m, "[A-Z0-9][a-zA-Z0-9]{0,4}");
	}
	
	
	@Test
	public void testMutateQM() {
		// no prexi is added if the rgexe is not a repeatition
		RegExp re = new RegExp("[a-zA-Z0-9]?");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		System.out.println(m);
		assertTrue(m.isEmpty());
	}

	@Test
	public void testMutateN1() {
		// no prexi is added if the rgexe is not a repeatition
		RegExp re = new RegExp("[a-zA-Z0-9]{1}") ;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		System.out.println(m);
		assertTrue(m.isEmpty());
	}
	
	@Test
	public void testMutateNoSplit() {
		// with 0 no mutation is applied
		RegExp re = new RegExp("[a-z]+");
		Iterator<MutatedRegExp> m = mp.mutate(re);
		assertFalse(m.hasNext());
	}

	@Test
	public void testMutateValidID() {
		String AB19 = "az19";
		String NUMLETTERS = "19az";
		RegExp re = new RegExp("[a-z0-9]+");
		RunAutomaton ra = new RunAutomaton(re.toAutomaton());
		assertTrue(ra.run(AB19));
		assertTrue(ra.run(NUMLETTERS));
		//
		Iterator<MutatedRegExp> m = mp.mutate(re);
		// for the first, it must start with a letter
		// test fails because it returns that starting with a number
		// FIXME
		RegExp mutation = m.next().mutatedRexExp;
		System.out.println(mutation);
		ra = new RunAutomaton(mutation.toAutomaton());
		assertTrue(ra.run(AB19));
		assertFalse(ra.run(NUMLETTERS));
		mutation = m.next().mutatedRexExp;
		System.out.println(mutation);
		// it must start with a number
		ra = new RunAutomaton(mutation.toAutomaton());
		assertFalse(ra.run(AB19));
		assertTrue(ra.run(NUMLETTERS));
	}

	@Test
	public void testMutate3() {
		RegExp re = new RegExp("AAA[a-zA-Z0-9]+");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertOneEqualTo(m, "AAA[a-zA-Z][a-zA-Z0-9]*");
		assertOneEqualTo(m, "AAA[a-z0-9][a-zA-Z0-9]*");
		assertOneEqualTo(m, "AAA[A-Z0-9][a-zA-Z0-9]*");
	}

	@Test
	public void testMutate4() {
		// no prexi is added if the rgexe is not a repeatition
		RegExp re = new RegExp("[a-zA-Z0-9]");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		System.out.println(m);
		assertTrue(m.isEmpty());
	}
	
	// if it is not a repeat, no fault is given
	@Test
	public void testGetsubsets() {
		ooregex r = OORegexConverter.getOORegex(new RegExp("[a-zA-Z0-9]"));
		assertTrue(r instanceof REGEXP_UNION);
		List<RegExp> parts = OORegexConverter.convertBackToRegex(REGEXP_UNION.splitUnion(r));
		assertEquals(3, parts.size());
		System.out.println(parts);
		assertOneRegexEqualTo(parts, "[a-z]");
		assertOneRegexEqualTo(parts, "[A-Z]");
		assertOneRegexEqualTo(parts, "[0-9]");
	}

	@Test
	public void testGetsubsetsExplUnion() {
		ooregex r = OORegexConverter.getOORegex(new RegExp("[a-z]|[A-Z]|[0-9]"));
		assertTrue(r instanceof REGEXP_UNION);
		List<RegExp> parts = OORegexConverter.convertBackToRegex(REGEXP_UNION.splitUnion(r));
		assertEquals(3, parts.size());
		System.out.println(parts);
		assertOneRegexEqualTo(parts, "[a-z]");
		assertOneRegexEqualTo(parts, "[A-Z]");
		assertOneRegexEqualTo(parts, "[0-9]");
	}
}