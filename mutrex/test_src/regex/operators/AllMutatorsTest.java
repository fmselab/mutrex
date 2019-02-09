package regex.operators;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.brics.automaton.ExtendedRegex;
import dk.brics.automaton.OORegexConverter;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RegexComparatorByAutomata;
import dk.brics.automaton.ToSimpleString;
import regex.operators.RegexMutator.MutatedRegExp;

/** che the simmetry of the mutation*/
public class AllMutatorsTest {

	RegexComparatorByAutomata comparator = new RegexComparatorByAutomata();

	@BeforeClass
	static public void setUp() {
		Logger.getLogger(OORegexConverter.class.getName()).setLevel(Level.OFF);
		//AllMutators.disable("PA");
		// once a class has been removed there is no way to get it back from CA
		// if a disable that, I cannot repair the 
		//AllMutators.disable("CCR");
		//AllMutators.disable("CCA");		
		//AllMutators.disable("CA");
		//AllMutators.disable("CCN");
	}
	
	@Test
	public void testMutate() {
		testWithRegex("[a-a]{1,1}[n-n]{1,1}g{1,1}e{1,1}l{1,1}o{1,1}");
	}

	// every mutation can be translated back
	private void testWithRegex(String start) {
		// build the regexp
		RegExp correct = ExtendedRegex.getSimplifiedRegexp(start);
		// build mutations
		Iterator<MutatedRegExp> mutants = AllMutators.mutator.mutateRandom(correct);
		// for each mutation is possible to mutate and go back to correct
		while (mutants.hasNext()) {
			MutatedRegExp mut = mutants.next();
			String mutS = ToSimpleString.convertToReadableString(mut.mutatedRexExp);
			System.out.println(mutS);
		}
	}
}
