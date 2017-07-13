package regex.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import dk.brics.automaton.oo.REGEXP_UNION;
import dk.brics.automaton.oo.ooregex;

/**
 * The original expression contains a \w which is substituted by a subset. The
 * \w metacharacter is used to find a word character. A word character is a
 * character from a-z, A-Z, 0-9, including the _ (underscore) character.
 *
 * In MUTATION 2017 is CCR
 *
 */
public class CharacterClassRestriction extends RegexMutator {
	public static CharacterClassRestriction mutator = new CharacterClassRestriction();
	private static Logger logger = Logger.getLogger(CharacterClassRestriction.class.getName());

	private CharacterClassRestriction() {
		super(new CharacterClassRestrictionVisitor());
	}

	static class CharacterClassRestrictionVisitor extends RegexVisitorAdapterList {

		@Override
		public List<ooregex> visit(REGEXP_UNION r) {
			/*
			 * List<ooregex> l1 = r.exp1.accept(this); List<ooregex> l2 =
			 * r.exp2.accept(this); logger.info(r.exp1.toString() + " => " +
			 * l1.toString()); // add independently (split the union)
			 * ArrayList<ooregex> result = new ArrayList<ooregex>(); // all the
			 * variations of e1 for (ooregex e1 : l1) { result.add(new
			 * REGEXP_UNION(e1, r.exp2)); } result.add(r.exp1);
			 * result.addAll(l1); for (ooregex e2 : l2) { result.add(new
			 * REGEXP_UNION(r.exp1, e2)); } result.add(r.exp2);
			 * result.addAll(l2); return result;
			 */

			ArrayList<ooregex> result = new ArrayList<ooregex>();
			List<ooregex> parts = REGEXP_UNION.splitUnion(r);
			for (int i = 0; i < parts.size(); i++) {
				ooregex res = null;
				for (int j = 0; j < parts.size(); j++) {
					if (j != i) {
						if (res == null) {
							res = parts.get(j);
						} else {
							res = new REGEXP_UNION(res, parts.get(j));
						}
					}
				}
				result.add(res);
				List<ooregex> subRes = parts.get(i).accept(this);
				for (ooregex p : subRes) {
					result.add(new REGEXP_UNION(p, res));
				}
			}
			return result;
		}

		@Override
		public String getCode() {
			return "CCR";
		}
	}

	@Override
	public String getCode() {
		return "CCR";
	}
}