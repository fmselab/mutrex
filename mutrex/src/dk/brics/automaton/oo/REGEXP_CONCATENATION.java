package dk.brics.automaton.oo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class REGEXP_CONCATENATION extends oobinregex {

	public REGEXP_CONCATENATION(ooregex ooRegex, ooregex ooRegex2) {
		super(ooRegex, ooRegex2);
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

}