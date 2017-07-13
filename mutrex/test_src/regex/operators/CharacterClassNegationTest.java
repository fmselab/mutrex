package regex.operators;

import java.util.List;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.REGEXP_REPEAT;
import dk.brics.automaton.oo.REGEXP_UNION;
import dk.brics.automaton.oo.ooregex;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

public class CharacterClassNegationTest extends RegexMutationTest {
	static CharacterClassNegation mp = CharacterClassNegation.mutator;

	@Test
	public void testMutateSimpleGroup() {
		RegExp re = new RegExp("[a-z]");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_CHAR_RANGE;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		System.out.println(m);
		assertOneEqualTo(m, "[^a-z]");
	}

	@Test
	public void testMutaterepeat() {
		RegExp re = new RegExp("[a-z]*");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_REPEAT;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		for (MutatedRegExp a : m) {
			System.out.println(a);
		}
		assertOneEqualTo(m, "[^a-z]*");
	}

	@Test
	public void testMutateUnion() {
		RegExp re = new RegExp("[a-zA-Z]");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_UNION;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		for (MutatedRegExp a : m) {
			System.out.println(a);
		}
		assertOneEqualTo(m, "[^a-z]|[A-Z]");
		assertOneEqualTo(m, "[a-z]|[^A-Z]");
		re = new RegExp("[^a-zA-Z]");
		assertOneEqualTo(m, "[^a-zA-Z]");
	}

	@Test
	public void testMutateUnion2() {
		RegExp re = new RegExp("[AB]");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_UNION;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		for (MutatedRegExp a : m) {
			System.out.println(a);
		}
		// voglio anche "[^AB]"
		RegExp reN = new RegExp("[^AB]");
		RunAutomaton ra = new RunAutomaton(reN.toAutomaton());
		assert !ra.run("A");
		assert !ra.run("B");
		assert ra.run("C");
		assert !ra.run("AB");
		assertOneEqualTo(m, "[^AB]");
	}
}
