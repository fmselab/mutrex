package regex.operators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static regex.operators.MetaChar2Char.mutator;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import dk.brics.automaton.RegExp;
import dk.brics.automaton.oo.ToSimpleString;
import regex.distinguishing.DistStringCreator;
import regex.distinguishing.DistinguishingStringsCouple;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

public class MetaChar2CharTest extends RegexMutationTest {

	@Test
	public void testMutateIPAddess() {
		RegExp re = new RegExp("[0-9]{3}.[0-9]{3}");
		accept(re, "123.144", "123A144");

		Iterator<MutatedRegExp> res = mutator.mutate(re);//
		RegExp corrected = res.next().mutatedRexExp;
		System.out.println(corrected);
		DistinguishingStringsCouple ds = DistStringCreator.getDScouple(re, corrected);
		System.out.println(ds.toString());
		accept(corrected, "123.144");
		acceptNot(corrected, "123A144");
		RegExp rec = new RegExp("[0-9]{3}\".\"[0-9]{3}");
		accept(rec, "123.144");
		acceptNot(rec, "123A144");
		RegExp rec2 = new RegExp("[0-9]{3}\\.[0-9]{3}");
		accept(rec2, "123.144");
		acceptNot(rec2, "123A144");
	}

	@Test
	public void testMutate() {
		RegExp re = new RegExp("a|.");
		accept(re, "a", ".", "b");
		// mutate this expression
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		RegExp corrected = res.next().mutatedRexExp;
		System.out.println(corrected + " vs " + re);
		DistinguishingStringsCouple ds = DistStringCreator.getDScouple(re, corrected);
		System.out.println(ds.toString());
		accept(corrected, "a", ".");
		acceptNot(corrected, "b");
	}

	@Test
	public void testMutateAt() {
		RegExp re = new RegExp("@a");
		accept(re, "a", "ba", "@a");
		// mutate this expression
		Iterator<MutatedRegExp> res = mutator.mutate(re);
		List<MutatedRegExp> iteratorToList = IteratorUtils.iteratorToList(res);
		System.out.println(iteratorToList);
		RegExp corrected = iteratorToList.get(0).mutatedRexExp;
		System.out.println(corrected + " vs " + re);
		DistinguishingStringsCouple ds = DistStringCreator.getDScouple(re, corrected);
		System.out.println(ds.toString());
		accept(corrected, "@a");
		acceptNot(corrected, "a", "ba");
	}

	@Test
	public void testMutatePlusMC() {
		// + as metachar to + as char
		RegExp re = new RegExp("1+1=2");
		accept(re, "11=2", "1111=2");
		acceptNot(re, "1+1=2");
		// mutate this expression
		Iterator<MutatedRegExp> res = MetaChar2Char.mutator.mutate(re);
		assertTrue(res.hasNext());
		RegExp corrected = res.next().mutatedRexExp;
		System.out.println(ToSimpleString.convertToReadableString(corrected) + " vs "
				+ ToSimpleString.convertToReadableString(re));
		System.out.println(corrected + " vs " + re);
		DistinguishingStringsCouple ds = DistStringCreator.getDScouple(re, corrected);
		System.out.println(ds.toString());
		accept(corrected, "1+1=2");
		acceptNot(corrected, "1111=2", "11=2");
	}

	@Test
	public void testCharRange() {
		// + as metachar to + as char
		RegExp re = new RegExp("[a-b]");
		// mutate this expression
		List<MutatedRegExp> res = IteratorUtils.iteratorToList(MetaChar2Char.mutator.mutate(re));
		assertTrue(!res.isEmpty());
		RegExp corrected = res.get(0).mutatedRexExp;
		System.out.println(ToSimpleString.convertToReadableString(corrected) + " vs "
				+ ToSimpleString.convertToReadableString(re));
		assertOneEqualTo(res, "[a\\-b]");
	}

	@Test
	public void testExamplePaperSI_mutation2017() {
		RegExp re = new RegExp("[0-9]{3}.[0-9]{3}");
		accept(re, "113A123", "113.123");
		acceptNot(re, "112");
		List<MutatedRegExp> mutants = IteratorUtils.iteratorToList(MetaChar2Char.mutator.mutate(re));
		for(MutatedRegExp m: mutants) {
			System.out.println(m);
		}
		assertEquals(1, mutants.size());
		MutatedRegExp m = mutants.get(0);
		accept(m.mutatedRexExp, "113.123");
		acceptNot(m.mutatedRexExp, "112","113A123");
	}

	@Test
	public void testExamplePaperSI_mutation2017_2() {
		RegExp re = new RegExp("[a-b]+");
		accept(re, "aaa", "bbb");
		acceptNot(re, "a+", "+");
		List<MutatedRegExp> mutants = IteratorUtils.iteratorToList(MetaChar2Char.mutator.mutate(re));
		for(MutatedRegExp m: mutants) {
			System.out.println(m);
		}
		assertEquals(1, mutants.size());
		MutatedRegExp m = mutants.get(0);
		accept(m.mutatedRexExp, "a+");
		acceptNot(m.mutatedRexExp, "aaa");
	}
}