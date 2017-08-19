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
import java.util.TreeMap;
import java.util.TreeSet;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import regex.distinguishing.DistinguishingString;
import regex.mutrex.ds.DSSet;
import regex.mutrex.ds.RegExpSet;
import regex.operators.RegexMutator.MutatedRegExp;

public class MinimizationTechniques {
	private final static Random rnd = new Random();

	public static void MinTest_randomSelection(DSSet dsS, RegExp regex) {
		ArrayList<DistinguishingString> allDSs = new ArrayList<DistinguishingString>(dsS.getDSs());
		Collections.shuffle(allDSs);
		Map<RegExp, Set<DistinguishingString>> killedBy = new HashMap<RegExp, Set<DistinguishingString>>();
		while (allDSs.size() > 0) {
			DistinguishingString currDS = allDSs.remove(0);
			RegExpSet killedMutants = dsS.getKilledMutants(currDS);
			Map<RegExp, DistinguishingString> coveredBy = new HashMap<RegExp, DistinguishingString>();
			for (RegExp currDsMutant : killedMutants) {
				Automaton currDsMutantAut = currDsMutant.toAutomaton();
				// check whether another ds kills the mutant "currDsMutant"
				// killed by "currDS"
				for (DistinguishingString ds : dsS) {
					if (ds != currDS) {
						Set<DistinguishingString> killedBySet = killedBy.get(currDsMutant);
						if (killedBySet != null && killedBySet.contains(ds)) {
							coveredBy.put(currDsMutant, ds);
							break;
						}
						boolean acceptedByRegex = ds.isConfirming();
						boolean acceptedByMut = currDsMutantAut.run(ds.getDs());
						if (acceptedByRegex != acceptedByMut) {
							coveredBy.put(currDsMutant, ds);
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
			if (coveredBy.size() == killedMutants.size()) {
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
		TreeSet<CoveredByDs> kills = new TreeSet<CoveredByDs>();
		for (DistinguishingString currDS : dsS) {
			boolean acceptedByRegex = currDS.isConfirming();
			CoveredByDs killedMutants = new CoveredByDs(currDS, dsS.getKilledMutants(currDS));
			for (DistinguishingString ds : dsS) {
				if (ds != currDS) {
					for (RegExp m : dsS.getKilledMutants(ds)) {
						boolean acceptedByMut = m.toAutomaton().run(currDS.getDs());
						if (acceptedByRegex != acceptedByMut) {
							killedMutants.add(m);
						}
					}
				}
			}
			kills.add(killedMutants);
		}
		Iterator<CoveredByDs> it = kills.iterator();
		Set<RegExp> covered = new HashSet<RegExp>();
		while (it.hasNext()) {
			CoveredByDs covByDS = it.next();
			Set<RegExp> killedMuts = covByDS.killed;
			//System.out.println(killedMuts.size());
			if (new HashSet<RegExp>(killedMuts).removeAll(covered)) {
				//System.out.println("Covered");
				it.remove();
				dsS.remove(covByDS.ds);
			} else {
				covered.addAll(killedMuts);
			}
		}
		for (CoveredByDs ds : kills) {
			if (dsS.getDSs().contains(ds.ds)) {
				for (RegExp m : ds.killed) {
					dsS.add(ds.ds, Collections.singletonList(new MutatedRegExp(String.valueOf(rnd.nextInt()), m)));
				}
			}
		}
	}
}

class CoveredByDs implements Comparable<CoveredByDs> {
	DistinguishingString ds;
	Set<RegExp> killed;

	public CoveredByDs(DistinguishingString ds, Set<RegExp> killed) {
		this.ds = ds;
		this.killed = killed;
	}

	public void add(RegExp r) {
		killed.add(r);
	}

	@Override
	public int compareTo(CoveredByDs other) {
		return other.killed.size() - killed.size();
	}
}