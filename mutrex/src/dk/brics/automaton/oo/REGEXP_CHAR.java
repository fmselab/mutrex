package dk.brics.automaton.oo;

/**charexp	::=	<Unicode character>	(a single non-reserved character)	
	|	\ <Unicode character> 	(a single character)*/
public class REGEXP_CHAR extends ooregex {
	public char c;

	public REGEXP_CHAR(char c) {
		this.c = c;
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}

	/**
	 * .	(any single character)	
		|	#	(the empty language)	[OPTIONAL]
		|	@	(any string)	[OPTIONAL]
	*/
	public boolean isMetaChar(){
		return c == '.' || c == '#' || c == '@'; 
	}
}
