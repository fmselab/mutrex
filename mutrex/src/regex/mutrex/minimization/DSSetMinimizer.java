package regex.mutrex.minimization;

import dk.brics.automaton.RegExp;
import regex.mutrex.ds.DSSet;

public abstract class DSSetMinimizer {

	public abstract void minimize(DSSet dsS, RegExp regex);

}
