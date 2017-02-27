package regex.mutrex.parallel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import regex.operators.RegexMutator.MutatedRegExp;

abstract class MutantsManager {
	protected Iterator<MutatedRegExp> itMutants;
	List<Mutant> mutants;
	protected boolean noUncoveredMutants;

	public MutantsManager(Iterator<MutatedRegExp> itMutants) {
		this.itMutants = itMutants;
		this.mutants = new ArrayList<Mutant>();
	}

	public abstract boolean areThereUncoveredMutants();
	public abstract Mutant getNotCoveredByCurrentDAs(Set<DistinguishAutomatonTh> datS);
	public abstract Mutant getMutant(DistinguishAutomatonTh s);

	public void coverMutant(Mutant mutant) {
		mutant.isCovered = true;
		mutant.mutAut = null;
		mutant.mutNegAut = null;
	}

	abstract public void mutantConsidered();
}