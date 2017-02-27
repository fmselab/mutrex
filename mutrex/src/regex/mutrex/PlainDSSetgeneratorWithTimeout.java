package regex.mutrex;

import java.util.Collections;
import java.util.Iterator;

import dk.brics.automaton.RegExp;
import regex.distinguishing.DSgenPolicy;
import regex.distinguishing.DistStringCreator;
import regex.distinguishing.DistinguishingString;
import regex.mutrex.ds.DSSet;
import regex.mutrex.ds.DSSetGenerator;
import regex.operators.RegexMutator.MutatedRegExp;

/**
 * generates a ds for each mutation and then it collects the ds and keeps track
 * if one kills many
 * 
 * with timeout
 *
 */
public class PlainDSSetgeneratorWithTimeout extends DSSetGenerator {
	public static DSSetGenerator generator = new PlainDSSetgeneratorWithTimeout();
	public static long TIMEOUT = 1; 

	protected PlainDSSetgeneratorWithTimeout() {
	}

	@Override
	public void addStringsToDSSet(DSSet result, RegExp regex, Iterator<MutatedRegExp> mutants) {
		while (mutants.hasNext()) {
			Generator g = new Generator(mutants.next().mutatedRexExp, regex, this);
			g.start();
			try {
				g.join(TIMEOUT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(g.isAlive()) {
				System.err.println("interrupt");
				//assert g.ds == null;
				g.interrupt();
			}
			if (g.ds != null) {
				result.add(g.ds, Collections.singletonList(g.mutant));
			}
		}
		return;
	}
	
}

class Generator extends Thread {
		RegExp mutant;
		DistinguishingString ds;
		RegExp regex;
		PlainDSSetgeneratorWithTimeout generator;

		public Generator(RegExp mutant, RegExp regex, PlainDSSetgeneratorWithTimeout generator) {
			this.mutant = mutant;
			this.regex = regex;
			this.generator = generator;
		}

		@Override
		public void run() {
			// generate a distinguishing string
			ds = DistStringCreator.getDS(regex, mutant, DSgenPolicy.RANDOM);
		}
	}