package dk.brics.automaton;

import java.util.List;

import dk.brics.automaton.RegExp;
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
import dk.brics.automaton.oo.RegexVisitor;
import dk.brics.automaton.oo.ooregex;
import dk.brics.automaton.oo.oosimpleexp;

/**
 * 
 * It converts a ooregex to a readable String
 * 
 * @author garganti
 *
 */
public class ToSimpleString extends ToRegexString {

	public static String convertToReadableString(RegExp regex) {
		return convertToReadableString(OORegexConverter.getOORegex(regex));
	}

	public static String convertToReadableString(ooregex r) {
		ToSimpleString v = new ToSimpleString();
		r.accept(v);
		return v.b.toString();
	}

	private ToSimpleString() {
		b = new StringBuilder();
	}

	@Override
	public Void visit(REGEXP_REPEAT r) {
		//
		boolean parenthesis = r.accept(precedence) < r.getContentExpr().accept(precedence);
		visit(r, parenthesis);
		return null;
	}

	@Override
	public Void visit(REGEXP_UNION r) {
		//
		List<ooregex> splits = REGEXP_UNION.splitUnion(r);
		boolean allcharrange = true;
		String charranges = "";
		for (ooregex rs : splits) {
			if (!(rs instanceof REGEXP_CHAR_RANGE)) {
				allcharrange = false;
				break;
			} else {
				charranges += ((REGEXP_CHAR_RANGE) rs).from + "-" + ((REGEXP_CHAR_RANGE) rs).to;
			}
		}
		if (allcharrange)
			b.append("[" + charranges + "]");
		else
			super.visit(r);
		return null;
	}

	@Override
	public Void visit(REGEXP_CHAR r) {
		if (r.c == '.' || r.c == '#' || r.c == '@' || r.c == '+' || r.c == '?' || r.c == '*')
			b.append('\\');
		b.append(r.c);
		return null;
	}

	@Override
	public Void visit(oosimpleexp r) {
		// REGEXP_STRING r
		b.append(r.s);
		return null;
	}

	@Override
	public Void visit(REGEXP_CHAR_RANGE r) {
		b.append("[").append(r.from).append("-").append(r.to).append("]");
		return null;
	}

	private PrecedenceIndex precedence = new PrecedenceIndex();

	// precedenza minore vuol dire che ha piu' precedenza quando parsa
	// ad esempio ab* is parsed as "a(b*)"
	class PrecedenceIndex implements RegexVisitor<Integer> {

		@Override
		public Integer visit(REGEXP_UNION r) {
			return 0;
		}

		@Override
		public Integer visit(REGEXP_INTERSECTION r) {
			return 1;
		}

		@Override
		public Integer visit(REGEXP_CONCATENATION r) {
			return 2;
		}

		@Override
		public Integer visit(REGEXP_REPEAT r) {
			return 4;
		}

		@Override
		public Integer visit(REGEXP_COMPLEMENT r) {
			return 4;
		}

		@Override
		public Integer visit(REGEXP_CHAR_RANGE r) {
			return 3;
		}

		@Override
		public Integer visit(oosimpleexp r) {
			// if only a char has a lower precedence 2???
			// so a+ is not traslated as (a)+
			if (r.s.length() == 1) return -1;
			else return 6;
		}

		@Override
		public Integer visit(REGEXP_SPECIALCHAR r) {
			return 7;
		}

		// a single char does not need parenthesis
		@Override
		public Integer visit(REGEXP_CHAR r) {
			return -1;
		}

		@Override
		public Integer visit(REGEXP_AUTOMATON r) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Integer visit(REGEXP_INTERVAL r) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getCode() {
			return "PrecedenceIndex";
		}
	}
}