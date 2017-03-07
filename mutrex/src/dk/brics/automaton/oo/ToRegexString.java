package dk.brics.automaton.oo;

/**
 * 
 * It converts a ooregex to a STring that can be parsed by RegExp in order to
 * build the correct RegeExp. The code is copied from the original code to
 * String for RegExp
 * 
 * @author garganti
 *
 */
public class ToRegexString implements RegexVisitor<Void> {
	protected StringBuilder b;

	public static String convertToRegexString(ooregex r) {
		ToRegexString v = new ToRegexString();
		r.accept(v);
		return v.b.toString();
	}

	protected ToRegexString() {
		b = new StringBuilder();
	}

	@Override
	public Void visit(REGEXP_UNION r) {
		visit(r, true);
		return null;
	}

	protected void visit(REGEXP_UNION r, boolean parenthesis) {
		if (parenthesis) b.append("(");
		r.exp1.accept(this);
		if (parenthesis) b.append("|");
		r.exp2.accept(this);
		if (parenthesis) b.append(")");
	}

	@Override
	public Void visit(REGEXP_CONCATENATION r) {
		r.exp1.accept(this);
		r.exp2.accept(this);
		return null;
	}

	@Override
	public Void visit(REGEXP_INTERSECTION r) {
		b.append("(");
		r.exp1.accept(this);
		b.append("&");
		r.exp2.accept(this);
		b.append(")");
		return null;
	}

	@Override
	public Void visit(REGEXP_REPEAT r) {
		visit(r,true);
		return null;
	}

	protected void visit(REGEXP_REPEAT r,boolean parenthesis) {
		if (parenthesis) b.append("(");
		r.exp.accept(this);
		if (parenthesis) b.append(")");
		if (r.isOptional()) {
			//	(zero or one occurrence)
			b.append("?");
		} else {
			if (r.max < 0) {
				if (r.min <= 0) {
					// both are unset
					b.append("*");
				} else {
					if (r.min == 1)
						b.append("+");
					// REGEXP_REPEAT_MIN
					else
						b.append("{").append(r.min).append(",}");
				}
			} else {
				// REGEXP_REPEAT_MINMAX
				b.append("{").append(r.min).append(",").append(r.max).append("}");
			}
		}
	}

	@Override
	public Void visit(REGEXP_COMPLEMENT r) {
		b.append("~(");
		r.exp.accept(this);
		b.append(")");
		return null;
	}

	@Override
	public Void visit(REGEXP_CHAR r) {
		b.append("\\").append(r.c);
		return null;
	}

	@Override
	public Void visit(REGEXP_CHAR_RANGE r) {
		b.append("[\\").append(r.from).append("-\\").append(r.to).append("]");
		return null;
	}

	@Override
	public Void visit(oosimpleexp r) {
		// REGEXP_STRING r
		b.append("\"").append(r.s).append("\"");
		return null;
	}

	@Override
	public Void visit(REGEXP_SPECIALCHAR r) {
		b.append(r.sc);
		return null;
	}

	@Override
	public Void visit(REGEXP_AUTOMATON r) {
		b.append("<").append(r.namedAutomaton).append(">");
		return null;
	}

	@Override
	public Void visit(REGEXP_INTERVAL r) {
		String s1 = Integer.toString(r.min);
		String s2 = Integer.toString(r.max);
		b.append("<");
		if (r.digits > 0)
			for (int i = s1.length(); i < r.digits; i++)
				b.append('0');
		b.append(s1).append("-");
		if (r.digits > 0)
			for (int i = s2.length(); i < r.digits; i++)
				b.append('0');
		b.append(s2).append(">");
		return null;
	}

	@Override
	public String getCode() {
		return "ToRegexString";
	}
}