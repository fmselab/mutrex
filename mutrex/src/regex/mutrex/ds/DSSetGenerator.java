package regex.mutrex.ds;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import dk.brics.automaton.ExtendedRegex;
import dk.brics.automaton.RegExp;
import regex.distinguishing.DistinguishingString;
import regex.operators.AllMutators;
import regex.operators.RegexMutator.MutatedRegExp;

/** generate a set of DS that kills the mutants of a given regex 
 * it extends the observable so it can be observed during the generation of tests
 * */

public abstract class DSSetGenerator extends Observable {

	/**
	 * Generate DS set.
	 *
	 * @param regex the regex (with extended syntax)
	 * @return the DS set
	 */
	public final DSSet generateDSSet(String regex) {
		RegExp re = ExtendedRegex.getSimplifiedRegexp(regex);
		DSSet dsS = new DSSet(){
			@Override
			public synchronized void add(DistinguishingString ds, List<MutatedRegExp> mutants) {
				super.add(ds, mutants);
				// notify the observers
				setChanged();
				notifyObservers(ds);
			}
		};
		addStringsToDSSet(dsS, re, AllMutators.mutator.mutate(re));
		return dsS;
	}

	public abstract void addStringsToDSSet(DSSet dsset,  RegExp regex, Iterator<MutatedRegExp> mutations);
	
}