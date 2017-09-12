package regex.operators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;
import dk.brics.automaton.oo.REGEXP_CONCATENATION;
import dk.brics.automaton.oo.ooregex;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

public class NegatedCharacterClassToOptionalTest extends RegexMutationTest {
	static NegatedCharacterClassToOptional mutator = NegatedCharacterClassToOptional.mutator;

	@Test
	public void testMutateIraq1() {
		// RegExp re = new RegExp("q[^u](.)*");
		RegExp re = new RegExp("q[^u]");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_CONCATENATION;
		RunAutomaton ar = new RunAutomaton(re.toAutomaton());
		assertTrue(ar.run("qi"));
		assertFalse(ar.run("q"));
		assertFalse(ar.run("qu"));
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mutator.mutate(re));
		assert m.size() == 1;
		RegExp mutation = m.get(0).mutatedRexExp;
		System.out.println(mutation);
		RunAutomaton arm = new RunAutomaton(mutation.toAutomaton());
		assertTrue(arm.run("qi"));
		assertTrue(arm.run("q"));
		assertFalse(arm.run("qu"));
	}

	@Test
	public void testMutateIraq2() {
		RegExp re = new RegExp("q[^u](.)*");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_CONCATENATION;
		RunAutomaton ar = new RunAutomaton(re.toAutomaton());
		assertTrue(ar.run("qizzz"));
		assertFalse(ar.run("quota"));
		assertFalse(ar.run("qu"));
		assertFalse(ar.run("q"));
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mutator.mutate(re));
		assert m.size() == 0;
		// in questo caso non riesco a modificare in modo che prenda o u o
		// niente perche' se metto
		// opzionale q[^u]?(.)*, allora accettera' anche quota
	}

	@Test
	public void testMutateIraq3() {
		RegExp re = new RegExp("[^q]*q[^u]");
		ooregex oore = OORegexConverter.getOORegex(re);
		assert oore instanceof REGEXP_CONCATENATION;
		RunAutomaton ar = new RunAutomaton(re.toAutomaton());
		assertTrue(ar.run("Iraqi"));
		assertFalse(ar.run("Iraq"));
		assertFalse(ar.run("Alquanto"));
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mutator.mutate(re));
		assert m.size() == 1;
		RegExp mutation = m.get(0).mutatedRexExp;
		System.out.println(mutation);
		RunAutomaton arm = new RunAutomaton(mutation.toAutomaton());
		assertTrue(arm.run("Iraqi"));
		assertTrue(arm.run("Iraq"));
		assertFalse(arm.run("Alquanto"));
	}

	@Test
	public void test_a_z() {
		RegExp re = new RegExp("[a-z][a-z][^a-z]");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mutator.mutate(re));
		System.out.println(m);
		assertTrue(m.size() == 1);
	}

	@Test
	public void test_a_z2() {
		RegExp re = new RegExp("[a-z][^a-z][a-z]");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mutator.mutate(re));
		System.out.println(m);
		assertTrue(m.size() == 1);
	}

	@Test
	public void test_a_z3() {
		RegExp re = new RegExp("[^a-z][a-z][a-z]");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mutator.mutate(re));
		System.out.println(m);
		assertTrue(m.size() == 1);
	}

	@Test
	public void test_a_z4() {
		RegExp re = new RegExp("[a-z]|[^0-9]");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mutator.mutate(re));
		System.out.println(m);
		assertTrue(m.size() == 1);
	}

	@Test
	public void test_a_z5() {
		RegExp re = new RegExp("[^a-z]|[0-9]");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mutator.mutate(re));
		System.out.println(m);
		assertTrue(m.size() == 1);
	}

	@Test
	public void test_a_z6() {
		RegExp re = new RegExp("[^a-z]|[^0-9]");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mutator.mutate(re));
		System.out.println(m);
		assertTrue(m.size() == 2);
	}

	@Test
	public void test_a_z7() {
		RegExp re = new RegExp("[^a-z]|[^A-Z]");
		List<MutatedRegExp> m = IteratorUtils.iteratorToList(mutator.mutate(re));
		System.out.println(m);
		assertTrue(m.size() == 2);
	}

	@Test
	public void testExamplePaperSI_mutation2017() {
		RegExp re = new RegExp(".*q[^u]");
		List<MutatedRegExp> mutants = IteratorUtils.iteratorToList(mutator.mutate(re));
		for (MutatedRegExp m : mutants) {
			System.out.println(m);
		}
		assertEquals(1, mutants.size());
	}
}