package dk.brics.automaton;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.brics.automaton.RegExp.Kind;

/* <pre>
* repeatexp	::=	repeatexp ?	(zero or one occurrence)	
	|	repeatexp *	(zero or more occurrences)	
	|	repeatexp +	(one or more occurrences)	
	|	repeatexp {n}	(n occurrences)	
	|	repeatexp {n,}	(n or more occurrences)	
	|	repeatexp {n,m}	(n to m occurrences, including both)
* </pre>
*/

public class OORegexConverter_REGEXP_REPEATTest {

	@Test
	public void testREGEXP_REPEATOoregex() {
		checkRepeatExpr("A?", 0, 0, Kind.REGEXP_OPTIONAL);
		checkRepeatExpr("A*", 0, 0, Kind.REGEXP_REPEAT);
		checkRepeatExpr("A+", 1, 0, Kind.REGEXP_REPEAT_MIN);
		checkRepeatExpr("A{3}", 3, 3, Kind.REGEXP_REPEAT_MINMAX);
		checkRepeatExpr("A{3,}", 3, 0, Kind.REGEXP_REPEAT_MIN);
		checkRepeatExpr("A{3,5}", 3, 5, Kind.REGEXP_REPEAT_MINMAX);
	}

	private void checkRepeatExpr(String s, int min, int max, Kind regexpKind) {
		RegExp re = new RegExp(s);
		// they are also equal to the subexpression in brics regex
		assertEquals(min, re.min);
		assertEquals(max, re.max);
		assertEquals(regexpKind, re.kind);
	}

}
