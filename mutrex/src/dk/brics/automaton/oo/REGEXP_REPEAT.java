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
 * Warning: in brics this have different kinds (a possible equal min and max)
 * 
 * @author garganti
 *
 */
final public class REGEXP_REPEAT extends oounaryregex {

	// used for initinite
	public static final int infinite = -1;

	// TODO put them private (or at least with accessor)
	public int min;
	public int max;

	// NOT in accordance with brics regex
	// because in brics they have different kind
	// A? (Kind.REGEXP_OPTIONAL) -> 0, 1 (zero or one occurrence)
	// A* (Kind.REGEXP_REPEAT) -> 0, -1 (infinite)
	// A+ (Kind.REGEXP_REPEAT_MIN) -> 1, -1 (infinite)
	// A{n} (Kind.REGEXP_REPEAT_MINMAX) -> n, n
	// A{n,} (Kind.REGEXP_REPEAT_MIN) -> n, -1 (infinite)
	// A{n,m} (Kind.REGEXP_REPEAT_MINMAX) --> n, m
	REGEXP_REPEAT(ooregex ooRegex, int min, int max) {
		super(ooRegex);
		this.min = min;
		this.max = max;

		assert min >= 0 : min; // zero or 1 or a finite number (never =-1)
		assert max == -1 || max > 0 : max; // never 0
		// max is always greater than min unless is -1
		assert max == -1 || max >= min : "max = " + max + "\nmin = " + min;
	}

	/**
	 * build the ? repeat operator.
	 * 
	 * @param ooRegex
	 * @return
	 */
	static public REGEXP_REPEAT REGEXP_OPTIONAL(ooregex ooRegex) {
		return new REGEXP_REPEAT(ooRegex, 0, 1);
	}

	/**
	 * build the * repeat operator.
	 * 
	 * @param ooRegex
	 * @return
	 */
	static public REGEXP_REPEAT REGEXP_REPEAT(ooregex ooRegex) {
		return new REGEXP_REPEAT(ooRegex, 0, -1);
	}

	/**
	 * build the A+ repeat operator. (min =1)
	 * 
	 * @param ooRegex
	 * @return
	 */
	static public REGEXP_REPEAT REGEXP_REPEAT_MIN(ooregex ooRegex) {
		return REGEXP_REPEAT_MIN_N(ooRegex, 1);
	}

	/**
	 * build the A{n,} repeat operator. (n can be 0)
	 * 
	 * @param ooRegex
	 * @return
	 */
	static public REGEXP_REPEAT REGEXP_REPEAT_MIN_N(ooregex ooRegex, int min) {
		assert min >= 0;
		return new REGEXP_REPEAT(ooRegex, min, -1);
	}

	/**
	 * build the A{n} repeat operator.
	 * 
	 * @param ooRegex
	 * @return
	 */
	static public REGEXP_REPEAT REGEXP_REPEAT_MINMAX_N(ooregex ooRegex, int n) {
		assert n >= 1;
		return new REGEXP_REPEAT(ooRegex, n, n);
	}

	/**
	 * build the A{n,m} repeat operator.
	 * 
	 * @param ooRegex
	 * @return
	 */
	static public REGEXP_REPEAT REGEXP_REPEAT_MINMAX_N(ooregex ooRegex, int min, int max) {
		assert min >= 0 : "min = " + min;
		assert max >= min : "max = " + max + "min = " + min; // or strictly >?
		return new REGEXP_REPEAT(ooRegex, min, max);
	}

	/**
	 * when I want to build the same type of repetition to make the constructor
	 * private and min and max private rtype give min and max r the real content
	 */
	static public REGEXP_REPEAT sameREPEAT_Type(REGEXP_REPEAT rtype, ooregex r) {
		return new REGEXP_REPEAT(r, rtype.min, rtype.max);
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}

	// return the quantifier as a string (like ?, + , {3}...
	public String getQuantifier() {
		// check if it is optional
		if (min == 0 && max == 1) {
			// (zero or one occurrence)
			return "?";
		} else if (min == 0 && max == -1) {
			// both are unset
			return "*";
		} else if (min == 1 && max == -1) {
			return "+";
		} else if (min == max && min > 0) {
			return ("{" + min + "}");
		} else if (min > 0 && max == -1) {
			// REGEXP_REPEAT_MIN
			return ("{" + min + ",}");
		} else {
			assert min >= 0 && max > 0 && max > min;
			// REGEXP_REPEAT_MINMAX
			return ("{" + min + "," + max + "}");
		}
	}

	/**
	 * return the regex repeat with a cardinality -1
	 * returns null if not possible
	 */
	public REGEXP_REPEAT minus1() {
		int newMax = this.max == -1 ? -1 : this.max - 1;
		if (newMax == 0) {
			return null;
		}
		int newMin = this.min == 0 ? 0 : this.min - 1;
		// min = 1 becomes 1 + 0 or more
		// min becomes 1 + min-1 or more
		return new REGEXP_REPEAT(this.exp, newMin , newMax);
	}

}