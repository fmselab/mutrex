package regex.mutrex.ds;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

// a regex with its automata (positive and negative)
// useful to avoid to call toAutomaton too many times
public class RegexWAutomata {
	private RegExp mutant;
	private Automaton mAut, negMaut;

	public RegexWAutomata(RegExp m) {
		mutant = m;
	}

	public Automaton getmAut() {
		if (mAut == null) {
			mAut = mutant.toAutomaton();
		}
		return mAut;
	}

	public Automaton getNegMaut() {
		if (negMaut == null) {
			if (mAut == null) {
				mAut = mutant.toAutomaton();
			}
			negMaut = mAut.complement();
		}
		return negMaut;
	}
	public RegExp getMutant() {
		return mutant;
	}
	
	@Override
	public String toString() {
		return mutant.toString();
	}
}