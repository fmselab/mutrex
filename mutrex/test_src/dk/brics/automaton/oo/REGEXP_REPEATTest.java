package dk.brics.automaton.oo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;

/* <pre>
* repeatexp	::=	repeatexp ?	(zero or one occurrence)	
	|	repeatexp *	(zero or more occurrences)	
	|	repeatexp +	(one or more occurrences)	
	|	repeatexp {n}	(n occurrences)	
	|	repeatexp {n,}	(n or more occurrences)	
	|	repeatexp {n,m}	(n to m occurrences, including both)
* </pre>
*/

public class REGEXP_REPEATTest {

	@Test
	public void testREGEXP_REPEATOoregex() {
		checkRepeatExpr("A?", 0, 1);
		checkRepeatExpr("A*", 0, -1);
		checkRepeatExpr("A+", 1, -1);
		checkRepeatExpr("A{3}", 3, 3);
		checkRepeatExpr("A{3,}", 3, -1);
		checkRepeatExpr("A{3,5}", 3, 5);
	}

	private ooregex checkRepeatExpr(String s, int min, int max) {
		RegExp re = new RegExp(s);
		ooregex r1 = OORegexConverter.getOORegex(re);
		assertTrue(r1 instanceof REGEXP_REPEAT);
		assertEquals(min, ((REGEXP_REPEAT) r1).min);
		assertEquals(max, ((REGEXP_REPEAT) r1).max);
		return r1;
	}
	
	// negative min
	@Test(expected=AssertionError.class)
	public void testwrong1REGEXP_REPEAT() {
		ooregex ooRegex = new REGEXP_CHAR('A');
		new REGEXP_REPEAT(ooRegex ,-3,10);
	}
	
	@Test(expected=AssertionError.class)
	public void testwrong1bisREGEXP_REPEAT() {
		ooregex ooRegex = new REGEXP_CHAR('A');
		new REGEXP_REPEAT(ooRegex ,-1,10);
	}
	// negative max
	@Test(expected=AssertionError.class)
	public void testwrong2REGEXP_REPEAT() {
		ooregex ooRegex = new REGEXP_CHAR('A');
		new REGEXP_REPEAT(ooRegex ,1,-5);
	}
	
	// min > max
	@Test(expected=AssertionError.class)
	public void testwrong3REGEXP_REPEAT() {
		ooregex ooRegex = new REGEXP_CHAR('A');
		new REGEXP_REPEAT(ooRegex ,3,2);
	}

}
