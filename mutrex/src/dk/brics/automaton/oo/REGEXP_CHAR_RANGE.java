package dk.brics.automaton.oo;

// charclass	::=	charexp - charexp	(character range, including end-points)	
public class REGEXP_CHAR_RANGE extends ooregex {
	
	public char from;
	public char to;

	public REGEXP_CHAR_RANGE(char f, char t) {
		from = f;
		to = t;
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}
}
