package regex.operators;

import java.util.List;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.oo.REGEXP_CHAR;
import dk.brics.automaton.oo.REGEXP_CONCATENATION;
import dk.brics.automaton.oo.REGEXP_REPEAT;
import dk.brics.automaton.oo.REGEXP_UNION;
import dk.brics.automaton.oo.ooregex;
import dk.brics.automaton.oo.oosimpleexp;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

/**
 * In MUTATION 2017 is CA
 *
 */
public class CaseUnsensetivenessTest extends RegexMutationTest {

	static CaseAddition mp = CaseAddition.mutator;

	@Test
	public void testMutaterepeat() {
		RegExp re = new RegExp("[a-z]*");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_REPEAT;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		System.out.println(m);
		assertOneEqualTo(m, "[a-zA-Z]*");
	}

	@Test
	public void testMutateString() {
		RegExp re = new RegExp("abc");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof oosimpleexp;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		System.out.println(m);
		assertOneEqualTo(m, "[Aa]bc");
	}

	@Test
	public void testMutateChars() {
		RegExp re = new RegExp("a");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_CHAR;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertOneEqualTo(m, "[Aa]");
	}

	@Test // Quantas problema from the book
	public void testMutateMix() {
		RegExp re = new RegExp("q[^u]");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_CONCATENATION;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertOneEqualTo(m, "[Qq][^u]");
	}

	@Test // Quantas problema from the book
	public void testMutateUnion() {
		RegExp re = new RegExp("[a|b]");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_UNION;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assertOneEqualTo(m, "[Qq][^u]");
	}
	
	@Test // 
	public void testMutateNumber() {
		RegExp re = new RegExp("\"0x\"");
		accept(re, "0x");
		acceptNot(re, "0X");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof oosimpleexp;
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mp.mutate(re));
		assert m.size() == 1;
		assertOneEqualTo(m, "0(\\x|\\X)");		
		accept(m.get(0).mutatedRexExp, "0x", "0X");
	}
}
