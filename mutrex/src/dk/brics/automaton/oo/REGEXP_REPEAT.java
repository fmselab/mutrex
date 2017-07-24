package dk.brics.automaton.oo;

/**
 * <pre>
 * repeatexp	::=	repeatexp ?	(zero or one occurrence)	
	|	repeatexp *	(zero or more occurrences)	
	|	repeatexp +	(one or more occurrences)	
	|	repeatexp {n}	(n occurrences)	
	|	repeatexp {n,}	(n or more occurrences)	
	|	repeatexp {n,m}	(n to m occurrences, including both)
 * </pre>
 * 
 * @author garganti
 *
 */
final public class REGEXP_REPEAT extends oounaryregex {
	public int min;
	public int max;

	/**
	 * 
	 * @param ooRegex
	 */
	public REGEXP_REPEAT(ooregex ooRegex, int min) {
		this(ooRegex, min, -1);
	}

	public REGEXP_REPEAT(ooregex ooRegex, int min, int max) {
		super(ooRegex);
		this.min = min;
		this.max = max;
		//
		assert min >= 0 : min; // zero or 1 or a finite number (never =-1)
		assert max == -1 || max > 0 : max;
		// max is always greater than min unless is -1
		assert max == -1 || max >= min : "max = " + max + "\nmin = " + min;
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}

	static public REGEXP_REPEAT REGEXP_OPTIONAL(ooregex ooRegex) {
		return new REGEXP_REPEAT(ooRegex, 0, 1);
	}

	public boolean isOptional() {
		return min == 0 && max == 1;
	}
}