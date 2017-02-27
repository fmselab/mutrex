package regex.mutrex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import regex.distinguishing.DistStringCreator;

public class DistinguishingAutomaton {
	public boolean positive;
	private Automaton content;
	List<RegExp> mutatedRegexes;
	//solution 2: invalidating the da
	//boolean isActive;

	/**
	 * 
	 * @param rexAut
	 * @param b
	 *            positive? those that generate strings accepted by regex
	 *            and rejected by mutations are positive // those that
	 *            generate strings rejected by regex and accepted by //
	 *            mutations
	 */
	//public DistinguishingAutomaton(RegExp regex, Automaton rexAut, Automaton rexNegAut, boolean b) {
	public DistinguishingAutomaton(RegexWAutomata r, boolean b) {
		positive = b;
		//content = b ? rexAut : rexAut.complement();
		
		if(positive) {
			content = r.getmAut();
		}
		else {
			content = r.getNegMaut();
		}
		
		mutatedRegexes = new ArrayList<>();
		//solution 2: invalidating the da
		//isActive = true;
	}

	/**
	 * Adds the mutant
	 *
	 * @param mutant
	 *            the mutant (as regex)
	 * @param mAut
	 *            the automaton (passed in order to avoid to build each
	 *            automata many times)
	 * @param negMaut
	 *            the negated automaton (passed in order to avoid to build each
	 *            automata many times)
	 * @return true, if successful
	 */
	//public boolean add(RegExp mutant, Automaton mAut, Automaton negMaut) {
	public boolean add(RegexWAutomata mutant) {
		// try to add
		Automaton result;
		if (positive) {
			result = content.intersection(mutant.getNegMaut());
		} else {
			result = content.intersection(mutant.getmAut());
		}
		// if no intersection is possible
		if (result.isEmpty()) {
			return false;
		}
		mutatedRegexes.add(mutant.mutant);
		//System.out.println(result.getNumberOfStates() + "\t" + result.getNumberOfTransitions());
		content = result;
		//content.reduce();
		//content.minimize();
		//System.out.println(this + "\t" + content.getNumberOfStates() + "\t" + content.getNumberOfTransitions());
		//System.out.println(content.getNumberOfStates() + "\t" + content.getNumberOfTransitions());
		//System.out.println();
		return true;
	}

	public String getExample() {
		return DistStringCreator.getExample(content);
	}

	public boolean isPositive() {
		return positive;
	}

	public List<RegExp> getMutants() {
		return Collections.unmodifiableList(mutatedRegexes);
	}
	
	// a regex with its automata (positive and negative
	public static class RegexWAutomata{
		RegExp mutant;
		
		public RegexWAutomata(RegExp m){
			mutant = m;
		}
		public Automaton getmAut() {
			if (mAut == null) {
				mAut = mutant.toAutomaton();
			}
			return mAut;
		}
		Automaton mAut, negMaut;
		
		public Automaton getNegMaut() {
			if (negMaut == null) {
				if (mAut == null) {
					mAut = mutant.toAutomaton();
				}
				negMaut = mAut.complement();
			}
			return negMaut;
		}
		
	}
}

