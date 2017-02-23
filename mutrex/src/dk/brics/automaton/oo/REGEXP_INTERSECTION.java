package dk.brics.automaton.oo;

// interexp	::=	concatexp & interexp	(intersection)	[OPTIONAL]
// also negated char classes are translated to this type of regex
public class REGEXP_INTERSECTION extends oobinregex {

	public REGEXP_INTERSECTION(ooregex ooRegex, ooregex ooRegex2) {
		super(ooRegex, ooRegex2);
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}

	/**
	 * return the character class negated by this in case it is actually a
	 * negated char class. A negated char class [^R] is translated to (.&~(R))
	 * return null if it not a ncc
	 */
	public ooregex getNegatedCharClass() {
		if (exp1 instanceof REGEXP_SPECIALCHAR) {
			REGEXP_SPECIALCHAR firstpart = (REGEXP_SPECIALCHAR) exp1;
			if (firstpart.sc != '.')
				return null;
		}
		if (exp2 instanceof REGEXP_COMPLEMENT) {
			REGEXP_COMPLEMENT secondPart = (REGEXP_COMPLEMENT) exp2;
			ooregex content = secondPart.getContentExpr();
			if (content instanceof REGEXP_CHAR)// [^a]
				return content;
			if (content instanceof REGEXP_CHAR_RANGE)// [^a-z]
				return content;
			if (content instanceof REGEXP_UNION)// [^abc]
				return content;
		}
		return null;
	}
}