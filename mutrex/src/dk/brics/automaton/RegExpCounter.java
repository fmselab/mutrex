package dk.brics.automaton;

import dk.brics.automaton.RegExp.Kind;

public class RegExpCounter {

	public static int countOperators(RegExp rgx) {
		int counter = 0;
		switch (rgx.kind) {
		case REGEXP_UNION:
		case REGEXP_CONCATENATION:
		case REGEXP_INTERSECTION:
			counter = 1;
			counter += countOperators(rgx.exp1);
			counter += countOperators(rgx.exp2);
			break;
		case REGEXP_OPTIONAL:
		case REGEXP_REPEAT:
		case REGEXP_REPEAT_MIN:
		case REGEXP_REPEAT_MINMAX:
		case REGEXP_COMPLEMENT:
			counter = 1 + countOperators(rgx.exp1);
			break;
		case REGEXP_CHAR_RANGE:
		case REGEXP_ANYCHAR:
		case REGEXP_EMPTY:
		case REGEXP_ANYSTRING:
		case REGEXP_INTERVAL:
			counter = 1;
			break;
		case REGEXP_AUTOMATON:
			break;
		case REGEXP_CHAR:
			break;
		case REGEXP_STRING:
			break;
		}
		return counter;
	}

	public static int countOperators(String operator, RegExp rgx) {
		int counter = 0;
		boolean selected = false;
		switch (rgx.kind) {
		case REGEXP_UNION:
			selected = selected || (operator.equals(Kind.REGEXP_UNION.toString()));
			counter = (selected ? 1 : 0);
			counter += countOperators(operator, rgx.exp1);
			counter += countOperators(operator, rgx.exp2);
			break;
		case REGEXP_CONCATENATION:
			selected = selected || (operator.equals(Kind.REGEXP_CONCATENATION.toString()));
			counter = (selected ? 1 : 0);
			counter += countOperators(operator, rgx.exp1);
			counter += countOperators(operator, rgx.exp2);
			break;
		case REGEXP_INTERSECTION:
			selected = selected || (operator.equals(Kind.REGEXP_INTERSECTION.toString()));
			counter = (selected ? 1 : 0);
			counter += countOperators(operator, rgx.exp1);
			counter += countOperators(operator, rgx.exp2);
			break;
		case REGEXP_OPTIONAL:
			selected = selected || (operator.equals(Kind.REGEXP_OPTIONAL.toString()));
			counter = (selected ? 1 : 0) + countOperators(operator, rgx.exp1);
			break;
		case REGEXP_REPEAT:
			selected = selected || (operator.equals(Kind.REGEXP_REPEAT.toString()));
			counter = (selected ? 1 : 0) + countOperators(operator, rgx.exp1);
			break;
		case REGEXP_REPEAT_MIN:
			selected = selected || (operator.equals(Kind.REGEXP_REPEAT_MIN.toString()));
			counter = (selected ? 1 : 0) + countOperators(operator, rgx.exp1);
			break;
		case REGEXP_REPEAT_MINMAX:
			selected = selected || (operator.equals(Kind.REGEXP_REPEAT_MINMAX.toString()));
			counter = (selected ? 1 : 0) + countOperators(operator, rgx.exp1);
			break;
		case REGEXP_COMPLEMENT:
			selected = selected || (operator.equals(Kind.REGEXP_COMPLEMENT.toString()));
			counter = (selected ? 1 : 0) + countOperators(operator, rgx.exp1);
			break;
		case REGEXP_CHAR_RANGE:
			selected = selected || (operator.equals(Kind.REGEXP_CHAR_RANGE.toString()));
			counter = (selected ? 1 : 0);
			break;
		case REGEXP_ANYCHAR:
			selected = selected || (operator.equals(Kind.REGEXP_ANYCHAR.toString()));
			counter = (selected ? 1 : 0);
			break;
		case REGEXP_EMPTY:
			selected = selected || (operator.equals(Kind.REGEXP_EMPTY.toString()));
			counter = (selected ? 1 : 0);
			break;
		case REGEXP_ANYSTRING:
			selected = selected || (operator.equals(Kind.REGEXP_ANYSTRING.toString()));
			counter = (selected ? 1 : 0);
			break;
		case REGEXP_INTERVAL:
			selected = selected || (operator.equals(Kind.REGEXP_INTERVAL.toString()));
			counter = (selected ? 1 : 0);
			break;
		case REGEXP_AUTOMATON:
			break;
		case REGEXP_CHAR:
			break;
		case REGEXP_STRING:
			break;
		}
		return counter;
	}
}
