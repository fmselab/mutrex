package dk.brics.automaton.oo;

// 	|	<n-m>	(numerical interval)	[OPTIONAL]
public class REGEXP_INTERVAL extends ooregex {
	public int digits, min, max;

	public REGEXP_INTERVAL(int digits, int min, int max) {
		this.digits = digits;
		this.min = min;
		this.max = max;
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}
}