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
		assertTrue(res.isAccept());
		assertEquals(3, res.getnChar());
		// rejected for length
		res = MeasuredRunner.run(a , "aa");
		assertFalse(res.isAccept());
		assertEquals(2, res.getnChar());
		res = MeasuredRunner.run(a , "aaaaa");
		assertFalse(res.isAccept());
		assertEquals(3, res.getnChar());
		//
		res = MeasuredRunner.run(a , "b");
		assertFalse(res.isAccept());
		assertEquals(0, res.getnChar());
		res = MeasuredRunner.run(a , "aabc");
		assertFalse(res.isAccept());
		assertEquals(2, res.getnChar());
	}
	@Test
	public void testDeterministic() {
		Automaton a = new RegExp("[a-b]{3}").toAutomaton(true);
		assert a.isSingleton();
		// accepted
		RunResult res = MeasuredRunner.run(a , "aaa");
		assertTrue(res.isAccept());
		assertEquals(3, res.getnChar());
		// rejected for length
		res = MeasuredRunner.run(a , "aa");
		assertFalse(res.isAccept());
		assertEquals(2, res.getnChar());
		res = MeasuredRunner.run(a , "aaaaa");
		assertFalse(res.isAccept());
		assertEquals(3, res.getnChar());
		//
		res = MeasuredRunner.run(a , "b");
		assertFalse(res.isAccept());
		assertEquals(0, res.getnChar());
		res = MeasuredRunner.run(a , "aabc");
		assertFalse(res.isAccept());
		assertEquals(2, res.getnChar());
	}

}
