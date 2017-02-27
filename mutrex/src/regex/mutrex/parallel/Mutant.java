package regex.mutrex.parallel;

import java.util.HashSet;
import java.util.Set;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import regex.operators.RegexMutator.MutatedRegExp;

public class Mutant {
	private MutatedRegExp mutant;
	Set<DistinguishAutomatonTh> visited;
	private boolean locked;
	private boolean testedPositiveWithR;
	private boolean testedNegativeWithR;
	boolean isCovered;
	Automaton mutAut;
	Automaton mutNegAut;

	public Mutant(MutatedRegExp mutatedRegExp) {
		this.mutant = mutatedRegExp;
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
		return mutant.mutatedRexExp;
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