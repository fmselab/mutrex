package regex.operators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.oo.REGEXP_CHAR;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.REGEXP_CONCATENATION;
import dk.brics.automaton.oo.REGEXP_REPEAT;
import dk.brics.automaton.oo.REGEXP_UNION;
import dk.brics.automaton.oo.ooregex;
import dk.brics.automaton.oo.oosimpleexp;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

/**
 * In MUTATION 2017 is CC
 *
 */
public class CaseChangeTest extends RegexMutationTest {
	static CaseChange mp = CaseChange.mutator;

	@Test
	public void testMutaterepeat() {
		RegExp re = new RegExp("[a-z]*");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_REPEAT;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		System.out.println(m);
		assertOneEqualTo(m, "[A-Z]*");
		assertOneEqualTo(m, "[A-z]*");
		assertFalse(OneEqualTo(m, "[a-Z]*"));
	}

	@Test
	public void testMutateCharClass() {
		RegExp re = new RegExp("[A-Z]");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_CHAR_RANGE;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		System.out.println(m);
		assertEquals(2, m.size());
		assertOneEqualTo(m, "[a-z]");
		assertOneEqualTo(m, "[A-z]");
		assertFalse(OneEqualTo(m, "[a-Z]"));
	}

	@Test
	public void testMutateCharClass2() {
		RegExp re = new RegExp("[A-z]");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_CHAR_RANGE;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertEquals(2, m.size());
		System.out.println(m);
		assertOneEqualTo(m, "[a-z]");
		assertOneEqualTo(m, "[A-Z]");
		assertFalse(OneEqualTo(m, "[a-Z]"));
	}
	
	@Test
	public void testMutateString() {
		RegExp re = new RegExp("abc");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof oosimpleexp;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		System.out.println(m);
		assertOneEqualTo(m, "Abc");
		assertOneEqualTo(m, "aBc");
		assertOneEqualTo(m, "abC");
	}

	@Test
	public void testMutateChars() {
		RegExp re = new RegExp("a");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_CHAR;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertOneEqualTo(m, "A");
	}

	@Test // Quantas problema from the book
	public void testMutateMix() {
		RegExp re = new RegExp("q[^u]");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_CONCATENATION;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertOneEqualTo(m, "Q[^u]");
		assertOneEqualTo(m, "q[^U]");		
	}

	@Test
	public void testMutateUnion() {
		RegExp re = new RegExp("[a|b]");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_UNION;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertOneEqualTo(m, "[A|b]");
		assertOneEqualTo(m, "[a|B]");
	}

	@Test
	public void testMutateUnionRepeat() {
		RegExp re = new RegExp("(a|b)+");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_REPEAT;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertEquals(2,m.size());
		assertOneEqualTo(m, "(A|b)+");
		assertOneEqualTo(m, "(a|B)+");
	}

	@Test
	public void testMutateNumber() {
		RegExp re = new RegExp("\"0x\"");
		accept(re, "0x");
		acceptNot(re, "0X");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof oosimpleexp;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assert m.size() == 1;
		assertOneEqualTo(m, "0\\X");
	}

	@Test
	public void testExamplePaperSI_mutation2017() {
		RegExp re = new RegExp("a[a-z]*");
		List<MutatedRegExp> mutants = IteratorUtils.iteratorToList(mp.mutate(re));
		for(MutatedRegExp m: mutants) {
			System.out.println(m);
		}
	}
}
