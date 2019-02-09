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
public class AllMutatorsSymmetryTest {

	RegexComparatorByAutomata comparator = new RegexComparatorByAutomata();

	@BeforeClass
	static public void setUp() {
		Logger.getLogger(OORegexConverter.class.getName()).setLevel(Level.OFF);
		AllMutators.disable("PA");
		// once a class has been removed there is no way to get it back from CA
		// if a disable that, I cannot repair the 
		AllMutators.disable("CCR");
		AllMutators.disable("CCA");		
		AllMutators.disable("CA");
		AllMutators.disable("CCN");
	}
	
	@Test
	public void testMutate() {
		testWithRegex("a{2,2}");
		testWithRegex("a{2,3}");
		testWithRegex("a{2}");
		testWithRegex("a{2,}");
		testWithRegex("(a|z){2,3}");
	}

	@Test
	public void testMutateClassInterval() {
		testWithRegex("[a-z]{2,3}");
	}

	@Test
	public void testMutateClassInterval2() {
		Logger.getLogger(OORegexConverter.class.getName()).setLevel(Level.OFF);
		testWithRegex("[a-z]");
	}

	@Test
	public void testMutate4() {
		Logger.getLogger(OORegexConverter.class.getName()).setLevel(Level.OFF);
		testWithRegex("a+");
		testWithRegex("a?");
		testWithRegex("a*");
		testWithRegex("[a-z]+\\@");
		testWithRegex("\\w+\\@[a-zA-Z_]");
	}

	// every mutation can be translated back
	private void testWithRegex(String start) {
		// build the regexp
		RegExp correct = ExtendedRegex.getSimplifiedRegexp(start);
		String correctStr = ToSimpleString.convertToReadableString(correct);
		// build mutations
		Iterator<MutatedRegExp> mutants = AllMutators.mutator.mutateRandom(correct);
		// for each mutation is possible to mutate and go back to correct
		nextMut: while (mutants.hasNext()) {
			MutatedRegExp mut = mutants.next();
			String mutS = ToSimpleString.convertToReadableString(mut.mutatedRexExp);
			// now mutate this one
			Iterator<MutatedRegExp> mtsmts = AllMutators.mutator.mutateRandom(mut.mutatedRexExp);
			// for each mutation is possible
			String considered = "";
			String mtmtString = "";
			while (mtsmts.hasNext()) {
				MutatedRegExp mutmut = mtsmts.next();
				RegExp mtmt = mutmut.mutatedRexExp;
				mtmtString = ToSimpleString.convertToReadableString(mtmt);
				considered += mtmtString + "[" + mutmut.description + "]";
				considered += "{" + OORegexConverter.getOORegex(mutmut.mutatedRexExp).getClass().getSimpleName() + "}";
				considered += ",";
				// if found equal
				// not working: mtmt.equals(correct)
				// not working with automata omparator.compare(mtmt, correct) == 0
				// with strings
				if (mtmtString.equals(correctStr)) {
					System.out.println("this mutation " + mut.description + " " + mutS + " can be translated back to "
							+ correctStr + " by " + mutmut.description);
					continue nextMut;
				}
			}
			// error found
			System.err.println(OORegexConverter.getOORegex(correct).getClass());
			System.err.println(
					"this mutation " + mut.description + " " + mutS + " cannot translated back to " + correctStr);
			System.err.println("mutatants considered " + considered);
		}
	}
}
