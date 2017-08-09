package regex.operators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

public class CharacterClassModificationTest extends RegexMutationTest {
	CharacterClassModification mutator = CharacterClassModification.mutator;

	@BeforeClass
	static public void setup() {
		ConsoleHandler ch = new ConsoleHandler();
		Logger.getLogger(OORegexConverter.class.getName()).addHandler(ch);
		Logger.getLogger(OORegexConverter.class.getName()).setLevel(Level.ALL);
	}

	@Test
	public void testMutate() {
		RegExp re = new RegExp("[az]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert res.hasNext();
		System.out.println(res.next());
		assert !res.hasNext();
	}

	@Test
	public void testMutate2() {
		RegExp re = new RegExp("[ac]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert res.hasNext();
		System.out.println(res.next());
		assert !res.hasNext();
	}

	@Test
	public void testMutate3() {
		RegExp re = new RegExp("[ab]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert !res.hasNext();
	}

	@Test
	public void testMutate4() {
		RegExp re = new RegExp("[afm]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert !res.hasNext();
	}

	@Test
	public void testMutate5() {
		RegExp re = new RegExp("[a-z]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert res.hasNext();
		System.out.println(res.next());
		assert !res.hasNext();
	}

	@Test
	public void testMutate6() {
		RegExp re = new RegExp("[a-b]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert !res.hasNext();
	}

	@Test
	public void testMutate7() {
		RegExp re = new RegExp("[z-a]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert res.hasNext();
		System.out.println(res.next());
		assert !res.hasNext();
	}

	@Test
	public void testMutate8() {
		RegExp re = new RegExp("[b-a]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assert res.hasNext();
		System.out.println(res.next());
		assert !res.hasNext();
	}
}
