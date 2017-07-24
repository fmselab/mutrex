/**
 * 
 */
package dk.brics.automaton.oo;

/**
 * <pre>
 * simpleexp ::= .... | 
 * 		. (any single character) | 
 * 		# (the empty language) [OPTIONAL] | 
 * 		@ (any string) [OPTIONAL] ....</pre>
 * 
 */
public class REGEXP_SPECIALCHAR extends ooregex {
	// special char
	public char sc;

	public REGEXP_SPECIALCHAR(char c) {
		// REGEXP_ANYCHAR:
		// REGEXP_EMPTY:
		// REGEXP_ANYSTRING:
		sc = c;
		assert c == '.' || c == '#' || c == '@';
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brics.automaton.oo.VisitableRegex#accept(dk.brics.automaton.oo.
	 * RegexVisitor)
	 */
	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}
}
