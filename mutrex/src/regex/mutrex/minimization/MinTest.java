package regex.mutrex.minimization;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import regex.distinguishing.DistinguishingString;
import regex.mutrex.ds.DSSet;
import regex.mutrex.ds.RegExpSet;
import regex.operators.RegexMutator.MutatedRegExp;

/**
 * Questo e' codice vecchio. 
 * Da cancellare.
 * Nuova versione in: MinimizationTechniques.java
 *
 */
public class MinTest extends DSSetMinimizer{

	public static MinTest instance = new MinTest();

	private MinTest() {

	}
	@Override
	public void minimize(DSSet dsS, RegExp regex) {
			int initSize = dsS.size();
			Random rnd = new Random();
			Map<RegExp, Set<DistinguishingString>> killedBy = new HashMap<RegExp, Set<DistinguishingString>>();
			while (initSize > 0) {
				DistinguishingString currDS = dsS.getDS(rnd.nextInt(dsS.size()));
				RegExpSet killedMutants = dsS.getKilledMutants(currDS);
				Map<RegExp, DistinguishingString> coveredBy = new HashMap<RegExp, DistinguishingString>();
				for (RegExp currDsMutant : killedMutants) {
					Automaton currDsMutantAut = currDsMutant.toAutomaton();
					for (DistinguishingString ds : dsS) {
						if (ds != currDS) {
							Set<DistinguishingString> killedBySet = killedBy.get(currDsMutant);
							if(killedBySet != null && killedBySet.contains(ds)) {
								coveredBy.put(currDsMutant, ds);
								break;
							}
							boolean acceptedByRegex = ds.isConfirming();
							boolean acceptedByMut = currDsMutantAut.run(ds.getDs());
							if (acceptedByRegex != acceptedByMut) {
								coveredBy.put(currDsMutant, ds);
								if(killedBySet == null) {
									killedBySet = new HashSet<DistinguishingString>();
									killedBy.put(currDsMutant, killedBySet);
								}
								killedBySet.add(ds);
								break;
							}
						}
					}
				}
				if (coveredBy.size() == killedMutants.size()) {
					for (Entry<RegExp, DistinguishingString> entry : coveredBy.entrySet()) {
						dsS.add(entry.getValue(), Collections
								.singletonList(new MutatedRegExp(String.valueOf(rnd.nextInt()), entry.getKey())));// to
																													// fix
					}
					dsS.remove(currDS);
				}
				initSize--;
			}
		}
	
}

