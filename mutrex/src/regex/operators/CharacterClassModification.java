package regex.operators;

import java.util.ArrayList;
import java.util.List;

import dk.brics.automaton.oo.REGEXP_CHAR;
import dk.brics.automaton.oo.REGEXP_CHAR_RANGE;
import dk.brics.automaton.oo.REGEXP_UNION;
import dk.brics.automaton.oo.ooregex;

/**
 * The user wrote [az] but meant [a-z]
 * or
 * The user wrote [a-z] but meant [az]
 */
public class CharacterClassModification extends RegexMutator {
	public static CharacterClassModification mutator = new CharacterClassModification();

	private CharacterClassModification() {
		super(new CharacterClassModificationVisitor());
	}

	static class CharacterClassModificationVisitor extends RegexVisitorAdapterList {

		@Override
		public List<ooregex> visit(REGEXP_UNION r) {
			List<ooregex> parts = REGEXP_UNION.splitUnion(r);
			ArrayList<ooregex> result = new ArrayList<ooregex>();
			if(parts.size() == 2) {
				ooregex first = parts.get(0);
				ooregex second = parts.get(1);
				if(first instanceof REGEXP_CHAR && second instanceof REGEXP_CHAR) {
					REGEXP_CHAR c1 = (REGEXP_CHAR)first;
					REGEXP_CHAR c2 = (REGEXP_CHAR)second;
					if(c1.c < (c2.c - 1)) {
						result.add(new REGEXP_CHAR_RANGE(c1.c, c2.c));
					}
				}
			}
			return result;
		}

		@Override
		public List<ooregex> visit(REGEXP_CHAR_RANGE r) {
			ArrayList<ooregex> result = new ArrayList<ooregex>();
			if(r.from != (r.to - 1)) {
				result.add(new REGEXP_UNION(new REGEXP_CHAR(r.from), new REGEXP_CHAR(r.to)));
			}
			return result;
		}

		@Override
		public String getCode() {
			return "CCM";
		}
	}

	@Override
	public String getCode() {
		return "CCM";
	}
}