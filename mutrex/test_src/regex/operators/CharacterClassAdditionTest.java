package regex.operators;

import static org.junit.Assert.assertEquals;

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
		assertEquals(2, m.size());
		assertOneEqualTo(m, "([\\a-\\z]|[\\A-\\Z])");
		assertOneEqualTo(m, "([\\a-\\z]|[\\0-\\9])");
	}

	@Test
	public void test_a_z_A_Z() {
		RegExp re = new RegExp("[a-zA-Z]");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		for (MutatedRegExp a : m) {
			System.out.println(a);
		}
		assertEquals(1, m.size());
		assertOneEqualTo(m, "([\\a-\\z]|[\\A-\\Z]|[\\0-\\9])");
	}

	@Test
	public void test_a_z_A_Z_0_9() {
		RegExp re = new RegExp("[a-zA-Z0-9]");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertEquals(0, m.size());
	}
}