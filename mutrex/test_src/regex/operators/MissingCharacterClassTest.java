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

public class MissingCharacterClassTest extends RegexMutationTest {
	MissingCharacterClass mutator = MissingCharacterClass.mutator;

	@BeforeClass
	static public void setup() {
		ConsoleHandler ch = new ConsoleHandler();
		Logger.getLogger(OORegexConverter.class.getName()).addHandler(ch);
		Logger.getLogger(OORegexConverter.class.getName()).setLevel(Level.ALL);
	}

	@Test
	public void testMutateCorrect() {
		RegExp re = new RegExp("a-z");
		accept(re, "a-z");
		acceptNot(re, "a", "b", "z");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		System.out.println(IteratorUtils.iteratorToList(mutator.mutate(re)));
		RegExp corrected = res.next().mutatedRexExp;
		accept(corrected, "a", "b", "c");
	}

	@Test
	public void testMutateCorrect2() {
		RegExp re = new RegExp("a-z[a-z]");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assertTrue(IteratorUtils.iteratorToList(mutator.mutate(re)).size() == 1);
		System.out.println(IteratorUtils.iteratorToList(mutator.mutate(re)));
	}

	@Test
	public void testMutateNotRange() {
		RegExp re = new RegExp("a-;");
		acceptNot(re, "a", "b", "z");
		Iterator<MutatedRegExp> res = mutator.mutate(re);//
		assertFalse(res.hasNext());
	}
}
