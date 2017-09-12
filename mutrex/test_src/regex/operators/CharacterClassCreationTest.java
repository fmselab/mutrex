package regex.operators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

public class CharacterClassCreationTest extends RegexMutationTest {
	CharacterClassCreation mutator = CharacterClassCreation.mutator;

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

	@Test
	public void testSimplePlain() {
		RegExp re = new RegExp("a-z");
		accept(re, "a-z");
		acceptNot(re, "a", "b", "d");
		Iterator<MutatedRegExp> res = mutator.mutate(re);//
		RegExp corrected = res.next().mutatedRexExp;
		System.out.println(corrected);
		accept(corrected, "a", "b", "z");
		acceptNot(corrected, "a-z", ".", "aa");
	}

	@Test
	public void testRepeat() {
		RegExp re = new RegExp("a-z+"); // questa non funziona perche' lo prende
										// a-(z)+
		accept(re, "a-z", "a-zzzz");
		acceptNot(re, "a", "b", "d");
		Iterator<MutatedRegExp> res = mutator.mutate(re);//
		RegExp corrected = res.next().mutatedRexExp;
		System.out.println(corrected);
		//
		accept(corrected, "a", "b", "z", "aaa", "abbbaz");
		acceptNot(corrected, "a-z", ".", "a-");
	}

	@Test
	public void testExamplePaperSI_mutation2017() {
		RegExp re = new RegExp("(0-9)+");
		accept(re, "0-9", "0-90-9");
		acceptNot(re, "0", "1", "9", "-");
		List<MutatedRegExp> mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		for(MutatedRegExp m: mutants) {
			System.out.println(m);
		}
		assertEquals(1, mutants.size());
	}

	@Test
	public void testExamplePaperSI_mutation2017corretto() {
		RegExp re = new RegExp("(0-9)+");
		accept(re, "0-9", "0-90-9");
		List<MutatedRegExp> mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		for(MutatedRegExp m: mutants) {
			System.out.println(m);
		}
		assertEquals(1, mutants.size());
		RegExp corrected = mutants.get(0).mutatedRexExp;
		//
		accept(corrected, "00000", "12345");
		acceptNot(corrected, "0-9");
		
	}
}
