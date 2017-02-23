package regex.operators;

import java.util.List;

import org.junit.Test;

import dk.brics.automaton.RegExp;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

public class CharacterClassAdditionTest extends RegexMutationTest {
	static CharacterClassAddition mp = CharacterClassAddition.mutator;

	@Test
	public void test_a_z() {
		RegExp re = new RegExp("[a-z]");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		for (MutatedRegExp a : m) {
			System.out.println(a);
		}
	}

	@Test
	public void test_a_z_A_Z() {
		RegExp re = new RegExp("[a-zA-Z]");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		for (MutatedRegExp a : m) {
			System.out.println(a);
		}
	}
}