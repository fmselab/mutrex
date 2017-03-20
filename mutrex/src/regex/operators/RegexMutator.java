package regex.operators;

import java.util.Iterator;
import java.util.List;

import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.oo.RegexVisitor;
import dk.brics.automaton.oo.ToRegexString;
import dk.brics.automaton.oo.ToSimpleString;
import dk.brics.automaton.oo.ooregex;

public abstract class RegexMutator {

	private RegexVisitor<List<ooregex>> mutator;

	protected RegexMutator(RegexVisitor<List<ooregex>> v) {
		mutator = v;
	}

	/** given a regex, it builds all the possible mutations */
	public Iterator<MutatedRegExp> mutate(RegExp re) {
		final Iterator<ooregex> resultsOO = OORegexConverter.getOORegex(re).accept(mutator).iterator();
		return new Iterator<MutatedRegExp>() {

			@Override
			public boolean hasNext() {
				return resultsOO.hasNext();
			}

			@Override
			public MutatedRegExp next() {
				String s = ToRegexString.convertToRegexString(resultsOO.next());
				//return new MutatedRegExp(mutator.getClass().getEnclosingClass().getSimpleName(), new RegExp(s));
				return new MutatedRegExp(mutator.getCode(),s);
			}
			
		};
	}	
	
	static public class MutatedRegExp{// extends RegExp{
		public String description;
		public RegExp mutatedRexExp;
		
		public MutatedRegExp(String simpleName, RegExp regex) {
			//super(regex);
			description = simpleName;
			mutatedRexExp = regex;
		}

		public MutatedRegExp(String simpleName, String s) {
			this(simpleName, new RegExp(s));
		}

		@Override
		public String toString() {
			return description + ": " + ToSimpleString.convertToReadableString(mutatedRexExp);
		}
	}

	public abstract String getCode();
}