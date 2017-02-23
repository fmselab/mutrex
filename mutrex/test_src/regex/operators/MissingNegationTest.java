package regex.operators;

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

public class MissingNegationTest extends RegexMutationTest {

	@Test
	public void testSingleChar() {
		RegExp re = new RegExp("A");
		accept(re, "A");
		acceptNot(re, "B", ".", "^", "b");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		RegExp corrected = res.next().mutatedRexExp;
		// 
		accept(corrected, "B", ".", "a", "b");
		acceptNot(corrected, "A");
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
}
