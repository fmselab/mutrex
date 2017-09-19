package dk.brics.automaton;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import dk.brics.automaton.RegExp.Kind;
import dk.brics.automaton.oo.REGEXP_AUTOMATON;
import dk.brics.automaton.oo.REGEXP_CHAR;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.REGEXP_COMPLEMENT;
import dk.brics.automaton.oo.REGEXP_CONCATENATION;
import dk.brics.automaton.oo.REGEXP_INTERSECTION;
import dk.brics.automaton.oo.REGEXP_INTERVAL;
import dk.brics.automaton.oo.REGEXP_REPEAT;
import dk.brics.automaton.oo.REGEXP_SPECIALCHAR;
import dk.brics.automaton.oo.REGEXP_UNION;
import dk.brics.automaton.oo.ToRegexString;
import dk.brics.automaton.oo.ooregex;
import dk.brics.automaton.oo.oosimpleexp;

/**
 * convert a RegExp to objects of the hierarchy ooregex (instead of using the
 * fields and flags to identify the type of regex)
 * 
 */
public class OORegexConverter {

	public static Logger logger = Logger.getLogger(OORegexConverter.class.getName());

	// take a regex and build its objects
	public static ooregex getOORegex(String reg) {
		//reg = escapeDq(reg);
		return getOORegex(new RegExp(reg));
	}

	// from an extended regex (with some metachars and so on)
	public static ooregex getOOExtRegex(String reg) {
		//reg = escapeDq(reg);
		return getOORegex(ExtendedRegex.getSimplifiedRegexp(reg));
	}

	// escape a double quote if it is not already escaped and it is not at the end
	// or beginning of a string
	public static String escapeDq(String reg, boolean includingDelimiter) {
		int start = includingDelimiter ? 0 : 1;
		// if it is the first char and 
		if (start == 0 && reg.charAt(0) == '"') {
			assert includingDelimiter;
			reg = "\\" + reg;
		}
		// from the second char 
		for (int i = start; i <  reg.length() - start; i++) {
			if (reg.charAt(i) == '"' && reg.charAt(i - 1) != '\\') {
				// TODO check that \\ is not escaped itself
				reg = reg.substring(0, i).concat("\\").concat(reg.substring(i, reg.length()));
				i++;
			}
		}
		return reg;
	}

	/** convert a list of ooregex back to list of RegExp */
	public static List<RegExp> convertBackToRegex(List<ooregex> resultsOO) {
		List<RegExp> exps = new ArrayList<>();
		for (ooregex r : resultsOO) {
			exps.add(convertBackToRegex(r));
		}
		return exps;
	}

	/** convert a ooregex back to a RegExp */
	public static RegExp convertBackToRegex(ooregex r) {
		String s = ToRegexString.convertToRegexString(r);
		//s = escapeDq(s, true);
		try{
			return new RegExp(s);
		}catch (Exception e) {
			System.err.println("cannot buil regex from " + s);
			throw e;
		}
	}

	// take a regex and build its objects
	public static ooregex getOORegex(RegExp reg) {
		Kind kind = reg.kind;
		ooregex result;
		switch (kind) {
		case REGEXP_UNION:
			result = new REGEXP_UNION(getOORegex(reg.exp1), getOORegex(reg.exp2));
			break;
		case REGEXP_CONCATENATION:
			result = new REGEXP_CONCATENATION(getOORegex(reg.exp1), getOORegex(reg.exp2));
			break;
		case REGEXP_INTERSECTION:
			result = new REGEXP_INTERSECTION(getOORegex(reg.exp1), getOORegex(reg.exp2));
			// all repeat expressions
			break;
		case REGEXP_OPTIONAL:
			// we don't use the class optional anymore, only repeat
			result = REGEXP_REPEAT.REGEXP_OPTIONAL(getOORegex(reg.exp1)); // '?'
			break;
		case REGEXP_REPEAT:
			result = REGEXP_REPEAT.REGEXP_REPEAT(getOORegex(reg.exp1));
			break;
		case REGEXP_REPEAT_MIN:
			result = REGEXP_REPEAT.REGEXP_REPEAT_MIN_N(getOORegex(reg.exp1), reg.min);
			break;
		case REGEXP_REPEAT_MINMAX:
			result = REGEXP_REPEAT.REGEXP_REPEAT_MINMAX_N(getOORegex(reg.exp1), reg.min, reg.max);
			break;
		case REGEXP_COMPLEMENT:
			result = new REGEXP_COMPLEMENT(getOORegex(reg.exp1));
			break;
		case REGEXP_CHAR:
			result = new REGEXP_CHAR(reg.c);
			break;
		case REGEXP_CHAR_RANGE:
			result = new REGEXP_CHAR_RANGE(reg.from, reg.to);
			break;
		case REGEXP_ANYCHAR:
			result = new REGEXP_SPECIALCHAR('.');
			break;
		case REGEXP_EMPTY:
			result = new REGEXP_SPECIALCHAR('#');
			break;
		case REGEXP_STRING:
			result = new oosimpleexp(reg.s);
			break;
		case REGEXP_ANYSTRING:
			result = new REGEXP_SPECIALCHAR('@');
			break;
		case REGEXP_AUTOMATON:
			result = new REGEXP_AUTOMATON(reg.s);
			break;
		case REGEXP_INTERVAL:
			result = new REGEXP_INTERVAL(reg.digits, reg.min, reg.max);
			break;
		default:
			throw new RuntimeException();
		}
		OORegexConverter.logger.info("building from regex " + reg.toString() + " object " + result.getClass());
		return result;
	}

}