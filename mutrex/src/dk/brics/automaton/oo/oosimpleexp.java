package dk.brics.automaton.oo;

/* simpleexp	::=	charexp --> REGEX_CHAR		
	|	.	(any single character)	--> REGEXP_SPECIALCHAR
	|	#	(the empty language)	[OPTIONAL] --> REGEXP_SPECIALCHAR
	|	@	(any string)	[OPTIONAL] --> REGEXP_SPECIALCHAR
	|	" <Unicode string without double-quotes> "	(a string)	
	|	( )	(the empty string)	
	|	( unionexp )	(precedence override)	
// IN OTHER CLASSES
	|	< <identifier> >	(named automaton)	[OPTIONAL]
	|	<n-m>	(numerical interval)	[OPTIONAL]*/

// The reserved characters used in the (enabled) syntax must be escaped with backslash (\) or double-quotes ("...")

// represents a STRING that matches with the STRING
public class oosimpleexp extends ooregex {

	/** in case s contains only a char, return a REGEXP_CHAR instead */
	public static ooregex createoosimpleexp(String s) {
		if (s.length() == 1)
			return new REGEXP_CHAR(s.charAt(0));
		else
			return new oosimpleexp(s);
	}

	public String s;

	/**
	 * 
	 * @param s does NOT contain escaped chars, since in brics, this is not possible (special chars in strings are meant normal chars).
	 * If you call Regexp("1\\+2") -> string 1+2
	 */
	private oosimpleexp(String s) {
		/* non posso avere una stringa che contenga un metachar CON escape*/
		assert !isEscaped('.', s): "escaped . in " + s;
		assert !isEscaped('#', s);
		assert !isEscaped('@', s): s ;
		assert !isEscaped('+', s) : "escaped + in " + s;
		assert !isEscaped('*', s);
		assert !isEscaped('?', s);
		this.s = s;
		assert s.length() > 1 : "not valid empty string"; // or > 1???
	}

	/* return true IFF c is contained in s AND c is escaped */ 
	private boolean isEscaped(char c, String s) {
		// if it is not present or it is the first char, then it cannot be escaped
		if (s.indexOf(c) <= 0 ) return false;
		for (int index = s.indexOf(c); index >= 0; index = s.indexOf(c, index + 1)) {
			// not accurate since it could be \\\\a and a is still not escaped in that case 
			if (s.charAt(index-1) != '\\') {
				return false;
			}
		}
		return true;
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}
}
