package regex.operators;

import static org.junit.Assert.assertEquals;
import static regex.operators.NegationAddition.mutator;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.ooregex;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

public class NegationAdditionTest extends RegexMutationTest {

	@Test
	public void testSingleChar() {
		RegExp re = new RegExp("A");
		accept(re, "A");
		acceptNot(re, "B", ".", "^", "b");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		RegExp corrected = res.next().mutatedRexExp;
		System.out.println(corrected);
		accept(corrected, "B", ".", "a", "b");
		acceptNot(corrected, "A");
	}

	@Test
	public void testTwoChars() {
		RegExp re = new RegExp("ab");
		accept(re, "ab");
		acceptNot(re, "a", "b", "", "abb");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		RegExp corrected = res.next().mutatedRexExp;
		System.out.println(corrected);
		accept(corrected, "cb", "db", "Ab", " b");
		acceptNot(corrected, "ab");
	}

	@Test
	public void testInterval() {
		RegExp re = new RegExp("[A-Z]");
		accept(re, "A", "B", "F", "Z");
		acceptNot(re, "a", ".", "^", "b");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		RegExp corrected = res.next().mutatedRexExp;
		System.out.println(corrected);
		accept(corrected, "a", ".", "^", "b");
		acceptNot(corrected, "A", "B", "F", "Z");
	}

	@Test
	public void testMutateSimpleGroup() {
		RegExp re = new RegExp("[a-z]");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_CHAR_RANGE;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mutator.mutate(re));
		System.out.println(m);
		assertOneEqualTo(m, "[^a-z]");
	}

	@Test
	public void testMutateCC() {
		RegExp re = new RegExp("(5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mutator.mutate(re));
		System.out.println(m.size());
	}

	@Test
	public void testExamplePaperSI_mutation2017() {
		RegExp re = new RegExp("a");
		List<MutatedRegExp> mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		for (MutatedRegExp m : mutants) {
			System.out.println(m);
		}
		assertEquals(1, mutants.size());
	}

	@Test
	public void testExamplePaperSI_mutation2017_2() {
		RegExp re = new RegExp("[A-Z][a-z]");
		List<MutatedRegExp> mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		for (MutatedRegExp m : mutants) {
			System.out.println(m);
		}
		assertEquals(2, mutants.size());
	}
}
