package dk.brics.automaton.oo;

public class REGEXP_CONCATENATION extends oobinregex {

	public REGEXP_CONCATENATION(ooregex ooRegex, ooregex ooRegex2) {
		super(ooRegex, ooRegex2);
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}	
}