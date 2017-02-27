package regex.mutrex.parallel;

import java.util.HashSet;
import java.util.Set;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import regex.mutrex.ds.DistinguishingAutomaton.RegexWAutomata;
import regex.operators.RegexMutator.MutatedRegExp;

public class Mutant {
	private RegexWAutomata mutant;
	Set<DistinguishAutomatonTh> visited;
	private boolean locked;
	private boolean testedPositiveWithR;
	private boolean testedNegativeWithR;
	boolean isCovered;
	Automaton mutAut;
	Automaton mutNegAut;

	public Mutant(MutatedRegExp mutatedRegExp) {
		this.mutant = new RegexWAutomata(mutatedRegExp.mutatedRexExp);
		visited = new HashSet<DistinguishAutomatonTh>();
	}

	public void setVisitedDA(DistinguishAutomatonTh da) {
		visited.add(da);
	}

	public boolean hasVisitedDA(DistinguishAutomatonTh dat) {
		return visited.contains(dat);
	}

	public boolean isLocked() {
		return locked;
	}

	public void lock() {
		locked = true;
	}

	public void unlock() {
		locked = false;
	}

	public RegExp getRegex() {
		return mutant.getMutant();
	}

	public RegexWAutomata getRegexWithAutomata() {
		return mutant;
	}

	public void setTestedPositiveWithR() {
		this.testedPositiveWithR = true;
	}

	public void setTestedNegativeWithR() {
		this.testedNegativeWithR = true;
	}

	public boolean isEquivalent() {
		return testedPositiveWithR && testedNegativeWithR;
	}
}