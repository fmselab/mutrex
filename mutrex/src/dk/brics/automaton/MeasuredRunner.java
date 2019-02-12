package dk.brics.automaton;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.Set;

/** runs a string with an automaton and returns the integer that indicates how many chars (or states?) has visited 
 * 
 * @author garganti
 *
 */
public class MeasuredRunner{

	/*+ results from running a string with an automaton*/
	static class RunResult{
		boolean accept;
		int nChar;
		public RunResult(boolean accepted, int nChar) {
			this.accept = accepted;
			this.nChar = nChar;
		}}
		
	/**like BasicOperations.run
	 */
	static public RunResult run(Automaton a, String s) {
		if (a.isSingleton()) {
			String singleton = a.singleton;
			int i = 0;
			for(; i < Integer.min(s.length(), singleton.length()); i++) {				
				if (singleton.charAt(i) != s.charAt(i))
					return new RunResult(false,i);
			}
			// same length ??
			if (s.length() == singleton.length())
				return new RunResult(true, i);
			else 
				return new RunResult(false, i);
		}if (a.deterministic) {
			State p = a.initial;
			int i = 0;
			for (; i < s.length(); i++) {
				State q = p.step(s.charAt(i));
				if (q == null)
					return new RunResult(false,i);
				p = q;
			}
			// mah
			return new RunResult(p.accept, i);
		} else {
			Set<State> states = a.getStates();
			Automaton.setStateNumbers(states);
			LinkedList<State> pp = new LinkedList<State>();
			LinkedList<State> pp_other = new LinkedList<State>();
			BitSet bb = new BitSet(states.size());
			BitSet bb_other = new BitSet(states.size());
			pp.add(a.initial);
			ArrayList<State> dest = new ArrayList<State>();
			boolean accept = a.initial.accept;
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				accept = false;
				pp_other.clear();
				bb_other.clear();
				for (State p : pp) {
					dest.clear();
					p.step(c, dest);
					for (State q : dest) {
						if (q.accept)
							accept = true;
						if (!bb_other.get(q.number)) {
							bb_other.set(q.number);
							pp_other.add(q);
						}
					}
				}
				LinkedList<State> tp = pp;
				pp = pp_other;
				pp_other = tp;
				BitSet tb = bb;
				bb = bb_other;
				bb_other = tb;
			}
			// NOT finished yet
			assert false;
			// TODO use a number
			return new RunResult(accept, 0);
		}
	}

}
