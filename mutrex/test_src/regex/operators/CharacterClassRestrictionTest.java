package regex.operators;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.logging.ConsoleHandler;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.brics.automaton.ExtendedRegex;
import dk.brics.automaton.RegExp;
import regex.operators.RegexMutator.MutatedRegExp;

public class CharacterClassRestrictionTest {
	private CharacterClassRestriction mp = CharacterClassRestriction.mutator;

	@BeforeClass
	static public void setup() {
		ConsoleHandler ch = new ConsoleHandler();
		// Logger.getLogger(OORegexConverter.class.getName()).addHandler(ch);
		// Logger.getLogger(OORegexConverter.class.getName()).setLevel(Level.ALL);
		// Logger.getLogger().setLevel(Level.ALL);
	}

	@Test
	public void testMutate() {
		RegExp re = ExtendedRegex.getSimplifiedRegexp("\\w");
		Iterator<MutatedRegExp> res = mp.mutate(re);
		while (res.hasNext()) {
			System.out.println(res.next());
		}
	}

	@Test
	public void testUnion2() {
		RegExp re = new RegExp("a-f|0-9");
		Iterator<MutatedRegExp> res = mp.mutate(re);
		int counter = 0;
		while (res.hasNext()) {
			System.out.println(res.next());
			counter++;
		}
		assertEquals(2, counter);
	}

	@Test
	public void testUnion3() {
		RegExp re = new RegExp("a-f|0-9|p-t");
		Iterator<MutatedRegExp> res = mp.mutate(re);
		int counter = 0;
		while (res.hasNext()) {
			System.out.println(res.next());
			counter++;
		}
		assertEquals(3, counter);
	}

	@Test
	public void testUnion4() {
		RegExp re = new RegExp("a-f|!(0-2|4-6)|p-t");
		Iterator<MutatedRegExp> res = mp.mutate(re);
		int counter = 0;
		while (res.hasNext()) {
			System.out.println(res.next());
			counter++;
		}
		assertEquals(5, counter);
	}
}