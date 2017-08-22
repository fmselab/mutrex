package regex.mutrex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import regex.distinguishing.DistinguishingString;
import regex.mutrex.ds.DSSet;
import regex.mutrex.ds.RegExpSet;
import regex.operators.RegexMutator.MutatedRegExp;

public class MinimizationTechniques {
	private final static Random rnd = new Random();

	/**
	 * TODO: where to perform the shuffle?
	 * 
	 * @param dsS
	 * @param regex
	 */
	public static void MinTest_randomSelection(DSSet dsS, RegExp regex) {
		ArrayList<DistinguishingString> allDSs = new ArrayList<DistinguishingString>(dsS.getDSs());
		// for a mutant, it reports the string that are known to kill it
		Map<RegExp, Set<DistinguishingString>> killedBy = new HashMap<RegExp, Set<DistinguishingString>>();
		// Collections.shuffle(allDSs);
		while (allDSs.size() > 0) {
			// random selection
			Collections.shuffle(allDSs);
			DistinguishingString currDS = allDSs.remove(0);

			RegExpSet mutantsKilledByCurrDS = dsS.getKilledMutants(currDS);
			Map<RegExp, DistinguishingString> coveredBy = new HashMap<RegExp, DistinguishingString>();
			for (RegExp currDsMutant : mutantsKilledByCurrDS) {
				Automaton currDsMutantAut = currDsMutant.toAutomaton();
				// check whether another ds kills the mutant "currDsMutant"
				// killed by "currDS"
				for (DistinguishingString ds : dsS) {
					if (ds != currDS) {
						// check whether ds is already known to kill
						// currDsMutant
						Set<DistinguishingString> killedBySet = killedBy.get(currDsMutant);
						if (killedBySet != null && killedBySet.contains(ds)) {
							coveredBy.put(currDsMutant, ds);
							break;
						}

						// if not, check whether the ds belongs to the symmetric
						// difference
						boolean acceptedByRegex = ds.isConfirming();
						boolean acceptedByMut = currDsMutantAut.run(ds.getDs());
						if (acceptedByRegex != acceptedByMut) {
							coveredBy.put(currDsMutant, ds);// ds kills
															// currDsMutant
							if (killedBySet == null) {
								killedBySet = new HashSet<DistinguishingString>();
								killedBy.put(currDsMutant, killedBySet);
							}
							killedBySet.add(ds);
							break;
						}
					}
				}
			}
			// if all the mutants killed by "currDS" are killed by some other
			// distinguishing string, "currDS" can be removed and all its
			// mutants added to the distinguishing strings they are covered by
			if (coveredBy.size() == mutantsKilledByCurrDS.size()) {
				dsS.remove(currDS);
				for (Entry<RegExp, DistinguishingString> entry : coveredBy.entrySet()) {
					dsS.add(entry.getValue(), Collections
							.singletonList(new MutatedRegExp(String.valueOf(rnd.nextInt()), entry.getKey())));// to
																												// fix
				}
			}
		}
	}

	public static void MinTest_greedy_MaxCoverageFirst(DSSet dsS, RegExp regex) {
		// compute coverage
		TreeSet<CoveredByDs> coverage = new TreeSet<CoveredByDs>();
		for (DistinguishingString currDS : dsS) {
			boolean acceptedByRegex = currDS.isConfirming();
			CoveredByDs coveredByDs = new CoveredByDs(currDS, dsS.getKilledMutants(currDS));
			for (DistinguishingString ds : dsS) {
				if (ds != currDS) {
					for (RegExp m : dsS.getKilledMutants(ds)) {
						boolean acceptedByMut = m.toAutomaton().run(currDS.getDs());
						if (acceptedByRegex != acceptedByMut) {
							coveredByDs.add(m);
						}
					}
				}
			}
			coverage.add(coveredByDs);
		}

		while (coverage.size() > 0) {
			// the first one covers for sure uncovered mutants
			CoveredByDs covByDS = coverage.first();
			Iterator<CoveredByDs> it = coverage.iterator();
			it.next();// skip the first (i.e., the selected one)
			while (it.hasNext()) {
				CoveredByDs otherDS = it.next();
				otherDS.covered.removeAll(covByDS.covered);
				// if the ds does not cover any uncovered mutant, it is removed
				if (otherDS.covered.size() == 0) {
					it.remove();
					dsS.remove(otherDS.ds);
				}
			}
		}
	}
}

class CoveredByDs implements Comparable<CoveredByDs> {
	DistinguishingString ds;
	Set<RegExp> covered;

	public CoveredByDs(DistinguishingString ds, Set<RegExp> killed) {
		this.ds = ds;
		this.covered = killed;
	}

	public void add(RegExp r) {
		covered.add(r);
	}

	/**
	 * It checks whether the current ds covers some mutant not present in
	 * coveredMutants.
	 * 
	 * @param coveredMutants
	 *            mutants covered by other distinguishing strings
	 * @return
	 */
	public boolean containsUncoveredMutant(Set<RegExp> coveredMutants) {
		for (RegExp m : covered) {
			if (!coveredMutants.contains(m)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Distinguishing strings covering more mutants must be considered first
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(CoveredByDs other) {
		return other.covered.size() - covered.size();
	}
}