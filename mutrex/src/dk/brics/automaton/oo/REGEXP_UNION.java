package dk.brics.automaton.oo;

import java.util.List;

public class REGEXP_UNION extends oobinregex {

	public REGEXP_UNION(ooregex ooRegex, ooregex ooRegex2) {
		super(ooRegex, ooRegex2);
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}

	// recursively finds the possible parts in which r can be split (or r
	// itself) in case it is an union
	// if it is not an union, return r itself
	public static List<ooregex> splitUnion(ooregex r) {
/*		if (r instanceof REGEXP_UNION) {
			List<ooregex> prefixes = new ArrayList<ooregex>();
			prefixes.addAll(splitUnion(((REGEXP_UNION) r).exp1));
			prefixes.addAll(splitUnion(((REGEXP_UNION) r).exp2));
			return prefixes;
		} else {
			return Collections.singletonList(r);
		}*/
		return oobinregex.split(r, REGEXP_UNION.class);
	}

}