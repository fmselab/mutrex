package regex.operators;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.oo.ToSimpleString;
import dk.brics.automaton.oo.ooregex;

public abstract class RegexMutator {
	
	private RegexVisitorAdapterList mutator;

	protected RegexMutator(RegexVisitorAdapterList v) {
		mutator = v;
	}

	/** given a regex, it builds all the possible mutations */
	public Iterator<MutatedRegExp> mutate(RegExp re) {
		return mutate(re,false);
	}

	public Iterator<MutatedRegExp> mutateRandom(RegExp re) {
		return mutate(re,true);
	}

	private Iterator<MutatedRegExp> mutate(RegExp re, boolean shuffle) {
		List<ooregex> results = OORegexConverter.getOORegex(re).accept(mutator);
		if (shuffle) Collections.shuffle(results);
		final Iterator<ooregex> resultsOO = results.iterator();
		return new Iterator<MutatedRegExp>() {

			@Override
			public boolean hasNext() {
				return resultsOO.hasNext();
			}

			@Override
			public MutatedRegExp next() {
				RegExp s = OORegexConverter.convertBackToRegex(resultsOO.next());
				// return new
				// MutatedRegExp(mutator.getClass().getEnclosingClass().getSimpleName(),
				// new RegExp(s));
				return new MutatedRegExp(mutator.getCode(), s);
			}
		};
	}


	static public class MutatedRegExp {// extends RegExp{
		public String description;
		public RegExp mutatedRexExp;

		public MutatedRegExp(String simpleName, RegExp regex) {
			// super(regex);
			description = simpleName;
			mutatedRexExp = regex;
		}

		@Override
		public String toString() {
			return description + ": " + ToSimpleString.convertToReadableString(mutatedRexExp);
		}
	}

	public abstract String getCode();
}