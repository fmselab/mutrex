package regex.mutrex.minimization;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import regex.distinguishing.DistinguishingString;
import regex.mutrex.ds.DSSet;
import regex.mutrex.ds.RegExpSet;
import regex.operators.RegexMutator.MutatedRegExp;

/**
 * minimize the DSSet using mintest as in the paper written by Ammann et  alt.
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
		Automaton regexAut = regex.toAutomaton();
		while (initSize > 0) {
			DistinguishingString currDS = dsS.getDS(rnd.nextInt(dsS.size()));
			RegExpSet killedMutants = dsS.getKilledMutants(currDS);
			Map<RegExp, DistinguishingString> coveredBy = new HashMap<RegExp, DistinguishingString>();
			for (RegExp currDsMutant : killedMutants) {
				Automaton currDsMutantAut = currDsMutant.toAutomaton();
				Automaton simmDiff = currDsMutantAut.minus(regexAut).union(regexAut.minus(currDsMutantAut));
				for (DistinguishingString ds : dsS) {
					if (ds != currDS) {
						if (simmDiff.run(ds.getDs())) {
							coveredBy.put(currDsMutant, ds);
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
