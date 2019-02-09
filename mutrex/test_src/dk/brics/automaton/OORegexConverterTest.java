package dk.brics.automaton;

import static dk.brics.automaton.OORegexConverter.escapeDq;
import static dk.brics.automaton.OORegexConverter.getOORegex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import dk.brics.automaton.RegExp.Kind;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.ooregex;
import dk.brics.automaton.oo.oosimpleexp;

public class OORegexConverterTest {

	@Test
	public void testGetOORegexAB() {
		test("[a-b]");
	}

	@Test
	public void testGetOORegexDw() {
		test("\\w");
	}

	@Test
	public void testGetOO() {
		test("\"--help\"");
	}
	
	@Test
	public void testGetQUote() {
		test("lib(64)?/lib((.&~(\\/)))*\\\\.so\\\".(.)*");
	}
	
	
	
	private void test(String reg) {
		// get the regex
		RegExp re = new RegExp(reg);
		// transform to objects
		ooregex oor = getOORegex(re);
		// come faccio ad essere sicuro di acer convertiro in modo corretto
		// una possibilita' e' convertire indietro in stringhe
		// questo ancora fallisce
		// back to string
		String rs = ToRegexString.convertToRegexString(oor);
		System.out.println(reg + " --> " +rs);
		assertEquals(reg, rs);
	}

	@Test
	public void testInterval() {
		RegExp re = new RegExp("[a-z]");
		assertEquals(RegExp.Kind.REGEXP_CHAR_RANGE, re.kind);
		ooregex reoo = OORegexConverter.getOORegex(re);
		assertTrue(reoo instanceof REGEXP_CHAR_RANGE);
	}

	@Test
	public void testMissedRange() {
		RegExp re = new RegExp("a-z");
		assertEquals(RegExp.Kind.REGEXP_STRING, re.kind);
		ooregex reoo = OORegexConverter.getOORegex(re);
		assertTrue(reoo instanceof oosimpleexp);
	}

	@Test
	public void testComplement() {
		RegExp re = new RegExp("~[a-z]");
		assertEquals(RegExp.Kind.REGEXP_COMPLEMENT, re.kind);
		RunAutomaton ra = new RunAutomaton(re.toAutomaton());
		// assert ra.
		// ooregex reoo = OORegexConverter.getOORegex(re);
		// assertTrue(reoo instanceof REGEXP_COMPLEMENT);
	}

	@Test
	public void testNegateCharClass() {
		RegExp re = new RegExp("[^a-zA-Z]");
		// purtroppo costruisce una intesection
		assertEquals(RegExp.Kind.REGEXP_INTERSECTION, re.kind);
		System.out.println(re);
	}
	
	@Test
	public void testGetOORegex1(){ // questo dà errore
		String reg = "a\"";
		RegExp rr = new RegExp(reg);
		System.out.println(rr);
		ooregex r = OORegexConverter.getOORegex(reg);
	}

	@Test
	public void testGetOORegex2(){ // questo non dà errore la prima volta che chiamo il costruttore ma la seconda sì
		String reg = "a\\\"";
		RegExp rr = new RegExp(reg);
		System.out.println(rr.getClass().getCanonicalName());
		System.out.println(rr);
		ooregex r = OORegexConverter.getOORegex(rr.toString());
		System.out.println(r);
	}

	// problems with to string of RegExp
	@Test
	public void problemRegExpToString() throws IOException {
		String reg = "a\\\"";
		System.out.println(reg);
		RegExp regexR = new RegExp(reg);
		System.out.println(regexR.toString());
		System.out.println(regexR.kind);		
		RegExp regexR2 = new RegExp(regexR.toString());
	}
	
	@Test
	public void testEscape() throws IOException {
		
		assertEquals("aaa",escapeDq("aaa", false));	
		assertEquals("\"aaa\"",escapeDq("\"aaa\"", false));	
		assertEquals("a\\\"aa",escapeDq("a\"aa", false));	
		assertEquals("a\\\"aa",escapeDq("a\\\"aa", false));	
		assertEquals("a\\\"",escapeDq("a\\\"", false));	
		assertEquals("\"aaa\"",escapeDq("\"aaa\"", false));	
		assertEquals("\\\"aaa\"",escapeDq("\\\"aaa\"", false));	
		//
		assertEquals("\"aaa\\\"AAA",escapeDq("\"aaa\"AAA", false)); // only the intermediate
		//
		assertEquals("\\\"aaa\\\"AAA",escapeDq("\"aaa\"AAA", true)); 
		assertEquals("\\\"aaa\\\"",escapeDq("\"aaa\"", true));	
		assertEquals("\\\"aaa\\\"",escapeDq("\\\"aaa\"", true));	

	}
	
	@Test
	public void testStringwEsc(){
		RegExp re = new RegExp("1\\+1=2");
		assertTrue(re.kind == Kind.REGEXP_STRING);
		System.out.println(re.s);

	}
	
	
	
}