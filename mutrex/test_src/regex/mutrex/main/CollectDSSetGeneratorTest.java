package regex.mutrex.main;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import regex.mutrex.CollectDSSetGeneratorNoLimit;

public class CollectDSSetGeneratorTest {

	@Test
	public void testGenerateDSSet() {
		MutRexTest.generateAndCheck("ab", CollectDSSetGeneratorNoLimit.generator);
	}

	@Test
	public void testCollect() {
		// voglio testare se e' possibile il collect in questo caso
		// costruisco un automa che rifiuta entrambe in un solo colpo
		RegExp r1 = new RegExp("Ab");
		RegExp r2 = new RegExp("aB");
		Automaton intersect = r1.toAutomaton().complement().intersection(r2.toAutomaton().complement());
		assertFalse(intersect.isEmpty());
	}
}
