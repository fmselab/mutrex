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
 * Warninng: in brics this have differnt kinds (a possible euqal min and max
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
	// A? (Kind.REGEXP_OPTIONAL) -> 0, 1 	(zero or one occurrence)
	// A* (Kind.REGEXP_REPEAT) -> 0, -1 (infinite)
	// A+ (Kind.REGEXP_REPEAT_MIN) -> 1, -1 (infinite)
	// A{n} (Kind.REGEXP_REPEAT_MINMAX) -> n, n
	// A{n,} (Kind.REGEXP_REPEAT_MIN) -> n, -1 (infinite)
	// A{n,m} (Kind.REGEXP_REPEAT_MINMAX) --> n, m

	public REGEXP_REPEAT(ooregex ooRegex, int min, int max) {
		super(ooRegex);
		this.min = min;
		this.max = max;

		assert min >= 0 : min; // zero or 1 or a finite number (never =-1)
		assert max == -1 || max > 0 : max; // never 0
		// max is always greater than min unless is -1
		assert max == -1 || max >= min : "max = " + max + "\nmin = " + min;
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
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
			assert min > 0 && max > 0 && max > min;
			// REGEXP_REPEAT_MINMAX
			return ("{" + min + "," + max + "}");
		}
	}

}