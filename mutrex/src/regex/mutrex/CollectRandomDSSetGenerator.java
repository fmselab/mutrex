package regex.mutrex;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import dk.brics.automaton.RegExp;
import regex.operators.RegexMutator.MutatedRegExp;
import regex.utils.IteratorUtils;

/**
 * generates a ds that tries to kill as many mutants as possible, in random order
 * SHUFFLE
 * 
 * @author garganti
 *
 */
public final class CollectRandomDSSetGenerator extends CollectDSSetGeneratorNoLimit {
	private static Logger logger = Logger.getLogger(CollectRandomDSSetGenerator.class.getName());
	public static DSSetGenerator generator = new CollectRandomDSSetGenerator();

	private CollectRandomDSSetGenerator() {
	}

	@Override
	public void addStringsToDSSet(DSSet dsset, RegExp regex, Iterator<MutatedRegExp> mutants) {
		List<MutatedRegExp> iteratorToList = IteratorUtils.iteratorToList(mutants);
		Collections.shuffle(iteratorToList);
		super.addStringsToDSSet(dsset,regex, iteratorToList.iterator());
	}
}