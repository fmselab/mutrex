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

	public synchronized Mutant getNotCoveredByCurrentDAs(Set<DistinguishAutomatonTh> datS) {
		if (!noUncoveredMutants) {
			/*if (mutants.size() == 0) {
				if (itMutants.hasNext()) {
					Mutant mutant = new Mutant(itMutants.next());
					mutants.add(mutant);
					mutant.lock();
					return mutant;
				}
			} else {
				// Collections.shuffle(mutants);
				for (Mutant mutant : mutants) {
					if (!mutant.isCovered && !mutant.isEquivalent() && mutant.visited.containsAll(datS)
							&& !mutant.isLocked()) {
						mutant.lock();
						return mutant;
					}
				}
			}*/
			for (Mutant mutant : mutants) {
				if (!mutant.isCovered && !mutant.isEquivalent() && mutant.visited.containsAll(datS)
						&& !mutant.isLocked()) {
					mutant.lock();
					return mutant;
				}
			}
			if (itMutants.hasNext()) {
				Mutant mutant = new Mutant(itMutants.next());
				mutants.add(mutant);
				mutant.lock();
				return mutant;
			}
		}
		return null;
	}

	public abstract Mutant getMutant(DistinguishAutomatonTh s);

	public void coverMutant(Mutant mutant) {
		mutant.isCovered = true;
		mutant.mutAut = null;
		mutant.mutNegAut = null;
	}

	abstract public void mutantConsidered();
}