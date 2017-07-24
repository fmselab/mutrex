package dk.brics.automaton.oo;

public interface RegexVisitor<T> {

	T visit(REGEXP_UNION r);

	T visit(REGEXP_CONCATENATION r);

	T visit(REGEXP_INTERSECTION r);

	// use REGEXP_REPEAT
	// T visit(REGEXP_OPTIONAL r);

	T visit(REGEXP_REPEAT r);

	T visit(REGEXP_COMPLEMENT r);

	T visit(REGEXP_CHAR r);

	T visit(REGEXP_CHAR_RANGE r);

	T visit(oosimpleexp r);

	T visit(REGEXP_SPECIALCHAR r);

	T visit(REGEXP_AUTOMATON r);

	T visit(REGEXP_INTERVAL r);

	String getCode();
}
