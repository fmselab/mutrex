package regex.mutrex.ds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import dk.brics.automaton.RegExp;
import regex.operators.RegexMutator.MutatedRegExp;

/** to build set of regexes since regex has no hashcode method */
public class RegExpSet extends HashSet<RegExp> {

	/**
	 * Adds the specified element to this set if it is not already present. More
	 * formally, adds the specified element e to this set if this set contains
	 * no element e2 such that (e==null ? e2==null : e.equals(e2)). If this set
	 * already contains the element, the call leaves the set unchanged and
	 * returns false.
	 */
	// TODO use the WD
	@Override
	public boolean add(RegExp e) {
		// check if already exits. RegExp has no hashcode nor equal
		for (RegExp r : this) {
			// equal if the string are identical
			if (r.toString().equals(e.toString()))
				return false;
		}
		return super.add(e);
	}

	// for every regex in killed mutants, keep track of its origin
	Map<RegExp, List<String>> mutantDescription = new HashMap<RegExp, List<String>>();

	public void addAllWD(List<MutatedRegExp> mutants) {
		for (MutatedRegExp m : mutants) {
			add(m.mutatedRexExp);
			// add
			List<String> l = mutantDescription.get(m.mutatedRexExp);
			if (l == null) {
				l = new ArrayList<>();
				mutantDescription.put(m.mutatedRexExp, l);
			}
			l.add(m.description);
		}
	}

	// TODO , change set
	/**
	 * 
	 * @return the descriptions of the killed regex
	 */
	public List<String> getDescription(dk.brics.automaton.RegExp r) {
		List<String> l = mutantDescription.get(r);
		if (l == null) {
			return Collections.EMPTY_LIST;
		} else {
			return l;
		}

	}
}