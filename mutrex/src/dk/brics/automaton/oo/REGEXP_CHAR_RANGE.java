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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof REGEXP_CHAR_RANGE) {
			return this.from == ((REGEXP_CHAR_RANGE) obj).from && this.to == ((REGEXP_CHAR_RANGE) obj).to;
		}
		return false;
	}
}
