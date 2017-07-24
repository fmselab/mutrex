package dk.brics.automaton.oo;

public abstract class oounaryregex extends ooregex {
	protected ooregex exp;

	protected oounaryregex(ooregex e) {
		exp = e;
	}

	public ooregex getContentExpr() {
		return exp;
	}
}