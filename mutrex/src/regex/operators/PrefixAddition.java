package regex.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.brics.automaton.oo.REGEXP_CONCATENATION;
import dk.brics.automaton.oo.REGEXP_REPEAT;
import dk.brics.automaton.oo.REGEXP_UNION;
import dk.brics.automaton.oo.oobinregex;
import dk.brics.automaton.oo.ooregex;

/**
 * the regex is a repeat something, but it is missing a prefix that should be
 * added. The prefix is obtained by subsetting the first element of the regex
 * 
 * In MUTATION 2017 is PA
 * 
 * @author garganti
 *
 */
public class PrefixAddition extends RegexMutator {
	public static PrefixAddition mutator = new PrefixAddition();

	private PrefixAddition() {
		super(new PrefixAdditionVisitor());
	}

	static class PrefixAdditionVisitor extends RegexVisitorAdapterList {

		@Override
		public List<ooregex> visit(REGEXP_REPEAT r) {
			List<ooregex> parts = REGEXP_UNION.splitUnion(r.getContentExpr());
			// apply only if splittable
			// and min > 0. If min==0 o cambio la cardinalita' con la
			// concatenazione, oppure non ha senso mettere prefisso opzionale
			if (parts.size() == 1 || r.min == 0) {
				return Collections.EMPTY_LIST;
			}
			// find prefixes possible (buy subsetting the parts)
			List<ooregex> prefixes = new ArrayList<ooregex>();
			for (ooregex p : parts) {
				// build all except p
				List<ooregex> allButOne = new ArrayList<ooregex>(parts);
				allButOne.remove(p);
				// build the union if necessary
				if (allButOne.size() > 1) {
					prefixes.add(oobinregex.makeBinExpression(REGEXP_UNION.class, allButOne));
				} else {
					prefixes.add(allButOne.get(0));
				}
			}
			List<ooregex> result = new ArrayList<ooregex>();
			REGEXP_REPEAT newRepeat = r.minus1();
			if (newRepeat == null) {
				return Collections.EMPTY_LIST;
			}
			for (ooregex p : prefixes) {
				result.add(new REGEXP_CONCATENATION(p, newRepeat));
			}
			return result;
		}

		@Override
		public String getCode() {
			return "PA";
		}
	}

	@Override
	public String getCode() {
		return "PA";
	}
}