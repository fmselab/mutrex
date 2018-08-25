package dk.brics.automaton.oo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** a copncatenation bewteen two ooregexes */
public class REGEXP_CONCATENATION extends oobinregex {

	private REGEXP_CONCATENATION(ooregex ooRegex, ooregex ooRegex2) {
		super(ooRegex, ooRegex2);
		// cannot two string or chars combied
		assert !(ooRegex instanceof oosimpleexp && ooRegex2 instanceof oosimpleexp) : "ooRegex: "
				+ ((oosimpleexp) ooRegex).s + "\nooRegex2: " + ((oosimpleexp) ooRegex2).s;
		assert !(ooRegex instanceof oosimpleexp && ooRegex2 instanceof REGEXP_CHAR) : "ooRegex: "
				+ ((oosimpleexp) ooRegex).s + "\nooRegex2: " + ((REGEXP_CHAR) ooRegex2).c;
		assert !(ooRegex instanceof REGEXP_CHAR && ooRegex2 instanceof oosimpleexp) : "ooRegex: "
				+ ((REGEXP_CHAR) ooRegex).c + "\nooRegex2: " + ((oosimpleexp) ooRegex2).s;
		assert !(ooRegex instanceof REGEXP_CHAR && ooRegex2 instanceof REGEXP_CHAR) : "ooRegex: "
				+ ((REGEXP_CHAR) ooRegex).c + "\nooRegex2: " + ((REGEXP_CHAR) ooRegex2).c;
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}

	// recursively finds the possible parts in which r can be split (or r
	// itself) in case it is an concatenation
	// if it is not an concatenation, return r itself
	// TODO unite with splitUnion
	public static List<ooregex> splitConcatenation(ooregex r) {
		if (r instanceof REGEXP_UNION) {
			List<ooregex> prefixes = new ArrayList<ooregex>();
			prefixes.addAll(splitConcatenation(((REGEXP_CONCATENATION) r).exp1));
			prefixes.addAll(splitConcatenation(((REGEXP_CONCATENATION) r).exp2));
			return prefixes;
		} else {
			return Collections.singletonList(r);
		}
	}

	/** concatenaet two regex : if can be anything */
	public static ooregex makeREGEXP_CONCATENATION(ooregex r1, ooregex r2) {
		// see method static RegExp makeConcatenation(RegExp exp1, RegExp exp2) {
		// if ((exp1.kind == Kind.REGEXP_CHAR || exp1.kind == Kind.REGEXP_STRING) &&
		// (exp2.kind == Kind.REGEXP_CHAR || exp2.kind == Kind.REGEXP_STRING))
		// return makeString(exp1, exp2);
		ooregex result = tryConcatenationTwoString(r1, r2);
		if (result != null)
			return result;

//		// if one is a concatation and in between there is string
//		//	r.kind = Kind.REGEXP_CONCATENATION;
//		//	if (exp1.kind == Kind.REGEXP_CONCATENATION && 
//		//		(exp1.exp2.kind == Kind.REGEXP_CHAR || exp1.exp2.kind == Kind.REGEXP_STRING) && 
//		//		(exp2.kind == Kind.REGEXP_CHAR || exp2.kind == Kind.REGEXP_STRING)) {
//				r.exp1 = exp1.exp1;
//				r.exp2 = makeString(exp1.exp2, exp2);
//			} else if ((exp1.kind == Kind.REGEXP_CHAR || exp1.kind == Kind.REGEXP_STRING) && 
//					   exp2.kind == Kind.REGEXP_CONCATENATION && 
//					   (exp2.exp1.kind == Kind.REGEXP_CHAR || exp2.exp1.kind == Kind.REGEXP_STRING)) {
//				r.exp1 = makeString(exp1, exp2.exp1);
//				r.exp2 = exp2.exp2;
//			} else {

		if (r1 instanceof REGEXP_CONCATENATION) {
			// CONC(CONC(A,B),C) => CONC(A, B+ C) 
			ooregex res1 = tryConcatenationTwoString(((REGEXP_CONCATENATION) r1).exp2, r2);
			if (res1 != null) {
				return new REGEXP_CONCATENATION(((REGEXP_CONCATENATION) r1).exp1, res1);
			}
		}
		if (r2 instanceof REGEXP_CONCATENATION) {
			ooregex res2 = tryConcatenationTwoString(r1, ((REGEXP_CONCATENATION) r2).exp1);
			if (res2 != null) {
				// r1.exp1 cannot be string or char
				return new REGEXP_CONCATENATION(r2, ((REGEXP_CONCATENATION) r2).exp2);
			}
		}
		// TODO one case is missing: two strings in between
		// otherwise make the union
		return new REGEXP_CONCATENATION(r1, r2);
	}

	/**
	 * try to concatene in one string
	 * 
	 * @param r1
	 * @param r2
	 * @return null if it not possible
	 */
	private static ooregex tryConcatenationTwoString(ooregex r1, ooregex r2) {
		if (r1 instanceof oosimpleexp && r2 instanceof oosimpleexp) {
			return oosimpleexp.createoosimpleexp(((oosimpleexp) r1).s + ((oosimpleexp) r2).s);
		}
		if (r1 instanceof oosimpleexp && r2 instanceof REGEXP_CHAR) {
			return oosimpleexp.createoosimpleexp(((oosimpleexp) r1).s + ((REGEXP_CHAR) r2).c);
		}
		if (r1 instanceof REGEXP_CHAR && r2 instanceof oosimpleexp) {
			return oosimpleexp.createoosimpleexp(((REGEXP_CHAR) r1).c + ((oosimpleexp) r2).s);
		}
		if (r1 instanceof REGEXP_CHAR && r2 instanceof REGEXP_CHAR) {
			return oosimpleexp.createoosimpleexp(Character.toString(((REGEXP_CHAR) r1).c) + ((REGEXP_CHAR) r2).c);
		}
		return null;
	}
}
