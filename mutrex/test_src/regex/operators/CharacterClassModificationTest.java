package regex.operators;

import java.util.Iterator;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import regex.operators.RegexMutator.MutatedRegExp;

public class CharacterClassModificationTest extends RegexMutationTest {
	CharacterClassModification mutator = CharacterClassModification.mutator;

	@BeforeClass
	static public void setup() {
		ConsoleHandler ch = new ConsoleHandler();
		Logger.getLogger(OORegexConverter.class.getName()).addHandler(ch);
		Logger.getLogger(OORegexConverter.class.getName()).setLevel(Level.ALL);
	}

	@Test
	public void testAddInterval() {
		RegExp re = new RegExp("[ac]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert res.hasNext();
		System.out.println(res.next());
		assert !res.hasNext();
	}

	@Test
	public void testAddInterval2() {
		RegExp re = new RegExp("[ab]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert !res.hasNext();
	}

	@Test
	public void testAddInterval3() {
		RegExp re = new RegExp("[ca]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert !res.hasNext();
	}

	@Test
	public void testAddInterval4() {
		RegExp re = new RegExp("[afm]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert !res.hasNext();
	}

	@Test
	public void testRemoveInterval() {
		RegExp re = new RegExp("[a-z]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert res.hasNext();
		System.out.println(res.next());
		assert !res.hasNext();
	}

	@Test
	public void testRemoveInterval2() {
		RegExp re = new RegExp("[a-b]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert !res.hasNext();
	}

	@Test
	public void testRemoveInterval3() {
		RegExp re = new RegExp("[z-a]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert res.hasNext();
		System.out.println(res.next());
		assert !res.hasNext();
	}

	@Test
	public void testRemoveInterval4() {
		RegExp re = new RegExp("[b-a]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert res.hasNext();
		System.out.println(res.next());
		assert !res.hasNext();
	}
}
