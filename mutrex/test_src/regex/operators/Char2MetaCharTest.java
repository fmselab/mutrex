package regex.operators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.ToRegexString;
import dk.brics.automaton.ToSimpleString;
import dk.brics.automaton.oo.ooregex;
import regex.distinguishing.DistStringCreator;
import regex.distinguishing.DistinguishingStringsCouple;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;
import regex.utils.RegexExamplesTaker;

/**
 * In MUTATION 2017 is C2M
 *
 */
public class Char2MetaCharTest extends RegexMutationTest {
	private static Char2MetaChar mutator = Char2MetaChar.mutator;

	@Test
	public void testAPlus() {
		ooregex oor = OORegexConverter.getOOExtRegex("a\\+");
		System.out.println(ToSimpleString.convertToReadableString(oor));
		System.out.println(oor.getClass());
		// Iterator<MutatedRegExp> res = mutator.mutate(oor);
	}

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
		// still to do !
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
		List<MutatedRegExp> iteratorToList = IteratorUtils.iteratorToList(res);
		System.out.println(iteratorToList);
		assertOneEqualTo(iteratorToList, "1+1=2");
	}

	@Test
	public void testMutateQuantifiers0() {
		// + as char to + as metachar
		RegExp re = new RegExp("A\\?B\\*C");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		System.out.println(IteratorUtils.iteratorToList(res));
	}

	@Test
	public void testMutateQuantifiers() {
		// + as char to + as metachar
		RegExp re = new RegExp("quanto\\?1\\+1=2\\*3");
		accept(re, "quanto?1+1=2*3");
		// mutate this expression
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assertTrue(res.hasNext());
		// System.out.println();
		List<MutatedRegExp> list = IteratorUtils.iteratorToList(res);
		assertOneEqualTo(list, "(quanto)?1\\+1=2\\*3");
		assertOneEqualTo(list, "(quanto\\?1)+1=2\\*3");
		assertOneEqualTo(list, "(quanto\\?1\\+1=2)*3");
		// C2M: quanto?1+(1=2)*3,
		// C2M: (quanto?1)+1=2*3
		// C2M: (quanto?1+1=2)*3
		// C2M: quanto?1+(1=2)*3,
		// C2M: quanto?(1)+1=2*3,
		// C2M: quanto?(1+1=2)*3,
		// C2M: (quanto)?1+1=2*3
	}

	@Test
	public void testMutateMinus() {
		// - as char to - as metachar
		// NOT to mutate
		RegExp re = new RegExp("az-");
		// mutate this expression
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assertFalse(res.hasNext());
		re = new RegExp("-abc");
		res = mutator.mutate(re);
		assertFalse(res.hasNext());
		// to mutate
		re = new RegExp("a\\-z");
		res = mutator.mutate(re);
		assertTrue(res.hasNext());
		assertOneEqualTo(IteratorUtils.iteratorToList(res), "[a-z]");
		// more complex
		re = new RegExp("pippo\\-zorro");
		res = mutator.mutate(re);
		// System.out.println(IteratorUtils.iteratorToList(res));
		assertTrue(res.hasNext());
		assertOneEqualTo(IteratorUtils.iteratorToList(res), "pipp[o-z]orro");
	}

	@Test
	public void testSQLquery() throws IOException {
		RegExp re = RegexExamplesTaker.readExampleRegex("someExamples", "SQLquery");
		mutator.mutate(re);
	}

	@Test
	public void testMutateDonotMutateMinus() {
		// - as char to - as metachar
		// NOT to mutate
		RegExp re = new RegExp("z\\-a");
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		assertFalse(res.hasNext());
	}

	// this gives an error because it contains a "- which is split but the
	// other
	// part remains without double quote
	@Test
	public void testMutateMinusDubleQuote() {
		// NOT to mutate
		RegExp re = new RegExp("\\\"--help\\\"");
		List<MutatedRegExp> l = IteratorUtils.iteratorToList(mutator.mutate(re));
		for (MutatedRegExp o : l) {
			System.out.println(o);
		}
	}

	@Test
	public void testExamplePaperSI_mutation2017() {
		RegExp re = new RegExp("\\.{3}");
		List<MutatedRegExp> mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		for (MutatedRegExp m : mutants) {
			System.out.println(m);
		}
		assertEquals(1, mutants.size());
	}

	@Test
	public void testExampleAT() {
		// as char
		RegExp re = new RegExp("\\@");
		List<MutatedRegExp> mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		assertEquals(1, mutants.size());
		// in string
		re = new RegExp("a\\@");
		mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		assertEquals(1, mutants.size());
		assertEquals("a@", ToSimpleString.convertToReadableString(mutants.get(0).mutatedRexExp));
		re = new RegExp("[a-z]\\+@");
		mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		assertEquals(1, mutants.size());
		assertEquals("[a-z]+@", ToSimpleString.convertToReadableString(mutants.get(0).mutatedRexExp));
		re = new RegExp("[a-z]+\\@");
		mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		assertEquals(1, mutants.size());
		assertEquals("[a-z]+@", ToSimpleString.convertToReadableString(mutants.get(0).mutatedRexExp));
	}

	@Test
	public void testExampleAT2() {
		// as char
		RegExp re = new RegExp("[a-z]\\+\\@");
		List<MutatedRegExp> mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		for (MutatedRegExp m : mutants) {
			System.out.println(m);
		}
		assertEquals(2, mutants.size());
	}

	@Test
	public void testSpecialChars() {
		RegExp re = new RegExp("\\+");
		List<MutatedRegExp> mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		assertEquals(0, mutants.size());
		re = new RegExp("\\+\\@");
		mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		for (MutatedRegExp m : mutants) {
			System.out.println(m);
		}
		assertEquals(1, mutants.size());
	}

	@Test
	public void test() {
		RegExp re = new RegExp("(((~(\\d)|([\\a-\\f]|[\\A-\\f]))){2}\\:){0,5}((\\d|(~([\\A-\\F])|[\\A-\\F]))){0,2}");
		List<MutatedRegExp> mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		for (MutatedRegExp m : mutants) {
			System.out.println(m);
		}
	}

}