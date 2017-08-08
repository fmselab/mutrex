package regex.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import dk.brics.automaton.oo.ToRegexString;
import dk.brics.automaton.oo.oobinregex;
import dk.brics.automaton.oo.ooregex;
import dk.brics.automaton.oo.oosimpleexp;

/**
 * returns the list of mutations. If an expression has no mutations, then the
 * empty list is returned
 * 
 * @author garganti
 *
 */
class RegexVisitorAdapterList implements RegexVisitor<List<ooregex>> {

	@Override
	public List<ooregex> visit(REGEXP_UNION r) {
		return visitBinaryExpression(r, REGEXP_UNION.class);
	}

	@Override
	public List<ooregex> visit(REGEXP_CONCATENATION r) {
		return visitBinaryExpression(r, REGEXP_CONCATENATION.class);
	}

	@Override
	public List<ooregex> visit(REGEXP_INTERSECTION r) {
		return visitBinaryExpression(r, REGEXP_INTERSECTION.class);
	}

	private List<ooregex> visitBinaryExpression(oobinregex r, Class<? extends oobinregex> clazz) {
		List<ooregex> m1 = r.exp1.accept(this);
		assert m1 != null : ToRegexString.convertToRegexString(r.exp1) + " of class " + r.exp1.getClass().getName()
				+ " visitor " + this.getClass().getName();
		List<ooregex> m2 = r.exp2.accept(this);
		assert m2 != null;
		// if both have not mutated the content
		if (m1.isEmpty() && m2.isEmpty())
			return m1;
		List<ooregex> result = new ArrayList<>();
		for (ooregex r1 : m1) {
			result.add(oobinregex.makeBinExpression(clazz, r1, r.exp2));
		}
		for (ooregex r2 : m2) {
			result.add(oobinregex.makeBinExpression(clazz, r.exp1, r2));
		}
		return result;
	}

	@Override
	public List<ooregex> visit(REGEXP_REPEAT r) {
		List<ooregex> m1 = r.getContentExpr().accept(this);
		List<ooregex> result = new ArrayList<ooregex>();
		for (ooregex r1 : m1) {
			result.add(REGEXP_REPEAT.sameREPEAT_Type(r,r1));
		}
		return result;
	}

	@Override
	public List<ooregex> visit(REGEXP_COMPLEMENT r) {
		List<ooregex> result = r.getContentExpr().accept(this);
		if (result.isEmpty())
			return Collections.EMPTY_LIST;
		List<ooregex> fresult = new ArrayList<>();
		for (ooregex o : result) {
			fresult.add(new REGEXP_COMPLEMENT(o));
		}
		return fresult;
	}

	@Override
	public List<ooregex> visit(REGEXP_CHAR r) {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<ooregex> visit(REGEXP_CHAR_RANGE r) {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<ooregex> visit(oosimpleexp r) {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<ooregex> visit(REGEXP_SPECIALCHAR r) {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<ooregex> visit(REGEXP_AUTOMATON r) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not supported yet");
	}

	@Override
	public List<ooregex> visit(REGEXP_INTERVAL r) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not supported yet");
	}

	@Override
	public String getCode() {
		return "ALL";
	}
}