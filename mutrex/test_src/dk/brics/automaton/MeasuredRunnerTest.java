package dk.brics.automaton;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.brics.automaton.MeasuredRunner.RunResult;

public class MeasuredRunnerTest {

	@Test
	public void testSingleton() {
		Automaton a = new RegExp("aaa").toAutomaton(true);
		assert a.isSingleton();
		// accepted
		RunResult res = MeasuredRunner.run(a , "aaa");
		assertTrue(res.accept);
		assertEquals(3, res.nChar);
		// rejected for length
		res = MeasuredRunner.run(a , "aa");
		assertFalse(res.accept);
		assertEquals(2, res.nChar);
		res = MeasuredRunner.run(a , "aaaaa");
		assertFalse(res.accept);
		assertEquals(3, res.nChar);
		//
		res = MeasuredRunner.run(a , "b");
		assertFalse(res.accept);
		assertEquals(0, res.nChar);
		res = MeasuredRunner.run(a , "aabc");
		assertFalse(res.accept);
		assertEquals(2, res.nChar);
	}
	@Test
	public void testDeterministic() {
		Automaton a = new RegExp("[a-b]{3}").toAutomaton(true);
		assert a.isSingleton();
		// accepted
		RunResult res = MeasuredRunner.run(a , "aaa");
		assertTrue(res.accept);
		assertEquals(3, res.nChar);
		// rejected for length
		res = MeasuredRunner.run(a , "aa");
		assertFalse(res.accept);
		assertEquals(2, res.nChar);
		res = MeasuredRunner.run(a , "aaaaa");
		assertFalse(res.accept);
		assertEquals(3, res.nChar);
		//
		res = MeasuredRunner.run(a , "b");
		assertFalse(res.accept);
		assertEquals(0, res.nChar);
		res = MeasuredRunner.run(a , "aabc");
		assertFalse(res.accept);
		assertEquals(2, res.nChar);
	}

}
