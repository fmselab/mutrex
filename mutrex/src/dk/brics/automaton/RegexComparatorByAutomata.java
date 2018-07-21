package dk.brics.automaton;

import java.util.Comparator;

/** compare two regex by using their automaton. NON FUNZIONA CON GLI AUTOMI
 * 
 * @author garganti
 *
 */
public class RegexComparatorByAutomata implements Comparator<RegExp>{

	@Override
	public int compare(RegExp arg0, RegExp arg1) {
		Automaton a0 = arg0.toAutomaton();
		Automaton a1 = arg1.toAutomaton();
		if (a0.equals(a1)) return 0;
		return a0.getNumberOfStates() - a1.getNumberOfStates();
		
	}

}
