package regex.mutrex.parallel;

import java.util.HashSet;
import java.util.Set;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import regex.mutrex.ds.RegexWAutomata;
import regex.operators.RegexMutator.MutatedRegExp;

public class MutantForDasParallelCollector {
	private RegexWAutomata mutant;
	Set<DistinguishingAutomatonTh> visited;
	private boolean locked;
	private boolean testedPositiveWithR;
	private boolean testedNegativeWithR;
	boolean isCovered;
	Automaton mutAut;
	Automaton mutNegAut;
	String description;

	public MutantForDasParallelCollector(MutatedRegExp mutatedRegExp) {
		this.mutant = new RegexWAutomata(mutatedRegExp.mutatedRexExp);
		visited = new HashSet<DistinguishingAutomatonTh>();
		description = mutatedRegExp.description;
	}

	public void setVisitedDA(DistinguishingAutomatonTh da) {
		visited.add(da);
	}

	public boolean hasVisitedDA(DistinguishingAutomatonTh dat) {
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