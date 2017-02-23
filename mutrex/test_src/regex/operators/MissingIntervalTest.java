package regex.operators;

import static regex.operators.MissingCharacterClass.mutator;

import java.util.Iterator;

import org.junit.Test;

import dk.brics.automaton.RegExp;
import regex.operators.RegexMutator.MutatedRegExp;

public class MissingIntervalTest extends RegexMutationTest {

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
		RegExp re = new RegExp("a-z+"); // questa non funziona perche' lo prende a-(z)+
		accept(re, "a-z", "a-zzzz");
		acceptNot(re, "a", "b", "d");
		Iterator<MutatedRegExp> res = mutator.mutate(re);//
		RegExp corrected = res.next().mutatedRexExp;
		System.out.println(corrected);
		//
		accept(corrected, "a", "b", "z", "aaa", "abbbaz");
		acceptNot(corrected, "a-z", ".", "a-");
	}	
}
