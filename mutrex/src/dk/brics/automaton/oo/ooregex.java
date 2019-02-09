package dk.brics.automaton.oo;

import dk.brics.automaton.ToRegexString;

// OO representation of the the regex (useful to use visitor pattern)
public abstract class ooregex implements VisitableRegex {

	@Override
	final public String toString() {
		return ToRegexString.convertToRegexString(this);
	}
}
