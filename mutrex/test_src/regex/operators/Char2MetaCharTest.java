package regex.operators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Iterator;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.oo.ToRegexString;
import regex.distinguishing.DistStringCreator;
import regex.distinguishing.DistinguishingStringsCouple;
import regex.operators.RegexMutator.MutatedRegExp;

/**
 * In MUTATION 2017 is C2M
 *
 */
public class Char2MetaCharTest extends RegexMutationTest {
	private static Char2MetaChar mutator = Char2MetaChar.mutator;

	@Test
	public void testMutateDot() {
		RegExp re = new RegExp("-|\\.");
		accept(re, "-", ".");
		acceptNot(re, "a", "b");
		Iterator<MutatedRegExp> res = mutator.mutate(re);//
		RegExp corrected = res.next().mutatedRexExp;
		System.out.println(corrected);
		DistinguishingStringsCouple ds = DistStringCreator.getDScouple(re, corrected);
		System.out.println(ds.toString());
		//
		accept(corrected, "-", ".", "a", "b");
	}

	@Test
	public void testMutateRepeat() {
		RegExp re = new RegExp("[a\\.]+");
		accept(re, "a...", ".aaa", "aaa");
		acceptNot(re, "bbb", "bb.");
		// mutate this expression
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assertTrue(res.hasNext());
		RegExp corrected = res.next().mutatedRexExp;
		assertFalse(res.hasNext());
		System.out.println(corrected);
		accept(corrected, "a....", "bbbb", "egeeprgk", "bdfdfdg");
	}

	@Test
	public void testMutateIPAddess() {
		RegExp re = new RegExp("[0-9]{3}.[0-9]{3}");
		System.out.println(ToRegexString.convertToRegexString(OORegexConverter.getOORegex(re)));
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assertFalse(res.hasNext());
	}

	@Test
	public void testCharMutateMinus() {
		RegExp re = new RegExp("\\.{3}");
		// accept(re, "a...", ".aaa", "aaa");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		while (res.hasNext()) {
			System.out.println(res.next());
		}
	}

	@Test
	public void testMutateGraffa() {
		RegExp re = new RegExp("\\.}");
		// accept(re, "-", ".");
		// acceptNot(re, "a", "b");
		Iterator<MutatedRegExp> res = mutator.mutate(re);//
		RegExp corrected = res.next().mutatedRexExp;
		System.out.println(corrected);
		DistinguishingStringsCouple ds = DistStringCreator.getDScouple(re, corrected);
		System.out.println(ds.toString());
		//
		accept(corrected, "-", ".", "a", "b");
	}

	@Test
	public void testMutatePlus() {
		// + as char to + as metachar
		RegExp re = new RegExp("1\\+1=2");
		accept(re, "1+1=2");
		// mutate this expression
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assertTrue(res.hasNext());
	}

	
}