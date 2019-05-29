package regex.oracle;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;

/** it represents an oracle given by a regex */
public class AutomaticOracle implements RegexOracle {
	private Automaton automaton;
	private RunAutomaton automatonRunner;

	public AutomaticOracle(String regex) {
		this(new RegExp(regex));
	}

	public AutomaticOracle(RegExp regExp) {
		assert regExp != null;
		automaton = regExp.toAutomaton();
		automatonRunner = new RunAutomaton(automaton);
	}

	@Override
	public boolean accept(String s) {
		return automatonRunner.run(s);
	}
}
