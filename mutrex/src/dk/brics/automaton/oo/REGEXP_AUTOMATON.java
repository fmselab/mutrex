package dk.brics.automaton.oo;

// 	< <identifier> >	(named automaton)	[OPTIONAL]
public class REGEXP_AUTOMATON extends ooregex {
	String namedAutomaton;

	public REGEXP_AUTOMATON(String s) {
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}
}