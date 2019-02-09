package dk.brics.automaton.oo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.brics.automaton.ExtendedRegex;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.ToSimpleString;

public class ToSimpleStringTest {

	@Test
	public void testConvertToRegexStringSomeExamplesOmo() {
		// these must be converted back to exactly the same string
		checkOmoTrasformation("[a-b]");		
		checkOmoTrasformation("a+");		
		checkOmoTrasformation("a*");		
		checkOmoTrasformation("ab*");		
		checkOmoTrasformation("(ab)*");		
		checkOmoTrasformation("[a-zA-Z]*");	
		checkOmoTrasformation("[a-b]+");	
	}

	@Test
	public void testConvertToRegexStringSomeExamplesOmo2() {
		// these must be converted back to exactly the same string
		checkOmoTrasformation("[a-zA-Z]*");		
	}

	@Test
	public void testConvertToRegexStringSomeExamplesEquivalent() {
		assertEquals("(a|b)*", convertBack("[ab]*"));
		assertEquals("ab*", convertBack("a(b*)"));
	}
	
	
	private void checkOmoTrasformation(String s){
		// convert
		assertEquals(s, convertBack(s));
	}


	private String convertBack(String s) {
		RegExp regex = ExtendedRegex.getSimplifiedRegexp(s);		
		String result = ToSimpleString.convertToReadableString(regex);
		return result;
	}
		
}
