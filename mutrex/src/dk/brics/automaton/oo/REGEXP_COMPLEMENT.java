package dk.brics.automaton.oo;

public class REGEXP_COMPLEMENT extends oounaryregex {

	public REGEXP_COMPLEMENT(ooregex e) {
		super(e);
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}
}