package dk.brics.automaton;

// extend regex with some extra metachar
// . 	Find a single character, except newline or line terminator => ALREADY SUPPORTED
//\w 	Find a word character => ADDED
//\W 	Find a non-word character
//\d 	Find a digit  => ADDED
//\D 	Find a non-digit character
//\s 	Find a whitespace character
//\S 	Find a non-whitespace character
//\b 	Find a match at the beginning/end of a word
//\B 	Find a match not at the beginning/end of a word
//\0 	Find a NUL character
//\n 	Find a new line character
//\f 	Find a form feed character
//\r 	Find a carriage return character
//\t 	Find a tab character
//\v 	Find a vertical tab character
//\xxx 	Find the character specified by an octal number xxx
//\xdd 	Find the character specified by a hexadecimal number dd
//\\uxxxx (con un solo \) 	Find the Unicode character specified by a hexadecimal number xxxx

/**
 * This class allows to build a regex from a string with more metachar than
 * those supported by RegExp dk.brics.automaton library
 * 
 * @author garganti
 */
public class ExtendedRegex {

	public static RegExp getSimplifiedRegexp(String s) {
		String ss = simplifyRegex(s);
		//ss = OORegexConverter.escapeDq(ss);
		//System.err.println(ss);
		return new RegExp(ss);
	}

	public static String simplifyRegex(String s) {
		// word character
		s = s.replaceAll("\\\\w", "[a-zA-Z0-9_]");
		// \d is replaced by [0-9]
		s = s.replaceAll("\\\\d", "[0-9]");
		// \s is replaces by [ \t\r\n\f]
		s = s.replaceAll("\\\\s", "[ \t\r\n\f]");
		// TODO other metachars
		return s;
	}
}