package regex.mutrex;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import dk.brics.automaton.RegExp;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

/** generates a ds for each mutation and then it collects the ds and keeps track if one kills many
 * 
 * @author garganti
 *
 */
public final class PlainDSRandomSetgenerator extends PlainDSSetgenerator {

	public static DSSetGenerator generator = new PlainDSRandomSetgenerator();
	
	private PlainDSRandomSetgenerator(){}

	@Override
	public void addStringsToDSSet(DSSet dsset, RegExp regex, Iterator<MutatedRegExp> mutants) {
		List<MutatedRegExp> iteratorToList = IteratorUtils.iteratorToList(mutants);
		Collections.shuffle(iteratorToList);
		super.addStringsToDSSet(dsset,regex, iteratorToList.iterator());
	}
}
