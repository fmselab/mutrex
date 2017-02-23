package regex.mutrex;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicOperations;
import dk.brics.automaton.RegExp;
import regex.distinguishing.DSgenPolicy;
import regex.distinguishing.DistStringCreator;
import regex.distinguishing.DistinguishingString;
import regex.operators.RegexMutator.MutatedRegExp;

/**
 * generates a ds for each mutation and then it collects the ds and keeps track
 * if one kills many
 * 
 * @author garganti
 *
 */
public class MonitoringDSSetgenerator extends DSSetGenerator {
	public static DSSetGenerator generator = new MonitoringDSSetgenerator();

	protected MonitoringDSSetgenerator() {
	}

	@Override
	public void addStringsToDSSet(DSSet result, RegExp r, Iterator<MutatedRegExp> mutants) {
		Set<String> DSsAcceptedByR = new HashSet<String>();
mutLoop: while (mutants.hasNext()) {
			RegExp mutant = mutants.next().mutatedRexExp;
			// monitoring
			Automaton mutAutom = mutant.toAutomaton();
			for (DistinguishingString ds : result) {
				String dsStr = ds.toString();
				boolean acceptedByR = DSsAcceptedByR.contains(dsStr);
				boolean acceptedByMut = BasicOperations.run(mutAutom, dsStr);
				if (acceptedByR != acceptedByMut) {
					// ds also distinguishes r and mutant
					continue mutLoop;
				}
			}
			// generation
			DistinguishingString ds = DistStringCreator.getDS(r, mutant, DSgenPolicy.RANDOM);
			if (ds != null) {
				if (ds.isConfirming()) {
					DSsAcceptedByR.add(ds.getDs());
				}
				result.add(ds, Collections.singletonList(mutant));
			}
		}
		return;
	}
}
