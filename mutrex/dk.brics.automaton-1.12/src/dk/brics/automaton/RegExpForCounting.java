package dk.brics.automaton;

import dk.brics.automaton.RegExp.Kind;

public class RegExpForCounting {
	private RegExp rgx;

	public RegExpForCounting(RegExp rgx) {
		this.rgx = rgx;
	}

	public int countOperators(String operator) {
		int counter = 0;
		boolean selected = false;
		switch (rgx.kind) {
		case REGEXP_UNION:
			selected = selected || (operator.equals(Kind.REGEXP_UNION.toString()));
		case REGEXP_CONCATENATION:
			selected = selected || (operator.equals(Kind.REGEXP_CONCATENATION.toString()));
		case REGEXP_INTERSECTION:
			selected = selected || (operator.equals(Kind.REGEXP_INTERSECTION.toString()));
			counter = (selected ? 1 : 0);
			counter += new RegExpForCounting(rgx.exp1).countOperators(operator);
			counter += new RegExpForCounting(rgx.exp2).countOperators(operator);
			break;
		case REGEXP_OPTIONAL:
			selected = selected || (operator.equals(Kind.REGEXP_OPTIONAL.toString()));
		case REGEXP_REPEAT:
			selected = selected || (operator.equals(Kind.REGEXP_REPEAT.toString()));
		case REGEXP_REPEAT_MIN:
			selected = selected || (operator.equals(Kind.REGEXP_REPEAT_MIN.toString()));
		case REGEXP_REPEAT_MINMAX:
			selected = selected || (operator.equals(Kind.REGEXP_REPEAT_MINMAX.toString()));
		case REGEXP_COMPLEMENT:
			selected = selected || (operator.equals(Kind.REGEXP_COMPLEMENT.toString()));
			counter = (selected ? 1 : 0) + new RegExpForCounting(rgx.exp1).countOperators(operator);
			break;
		case REGEXP_CHAR_RANGE:
			selected = selected || (operator.equals(Kind.REGEXP_CHAR_RANGE.toString()));
		case REGEXP_ANYCHAR:
			selected = selected || (operator.equals(Kind.REGEXP_ANYCHAR.toString()));
		case REGEXP_EMPTY:
			selected = selected || (operator.equals(Kind.REGEXP_EMPTY.toString()));
		case REGEXP_ANYSTRING:
			selected = selected || (operator.equals(Kind.REGEXP_ANYSTRING.toString()));
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
