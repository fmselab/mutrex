package regex.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import dk.brics.automaton.RegExp;
import regex.utils.JoinedIterator;
import regex.utils.JoinedRandomIterator;

public class AllMutators extends RegexMutator {
	public static AllMutators mutator = new AllMutators();

	protected AllMutators() {
		super(new RegexVisitorAdapterList());
	}

	// list of all the mutators that can possibly used
	public static List<RegexMutator> allMutators = new ArrayList<RegexMutator>();
	// those that are defined
	public static List<RegexMutator> definedMutators = new ArrayList<RegexMutator>();

	// probably a file or a plugin mechanism or reflection could be better
	static {
		add(CaseChange.mutator);
		add(CaseAddition.mutator);
		add(MetaChar2Char.mutator);
		add(Char2MetaChar.mutator);
		add(CharacterClassCreation.mutator);
		add(CharacterClassAddition.mutator);
		add(CharacterClassModification.mutator);
		add(RangeModification.mutator);
		add(CharacterClassRestriction.mutator);
		add(PrefixAddition.mutator);
		add(CharacterClassNegation.mutator);
		add(NegatedCharacterClassToOptional.mutator);
		add(NegationAddition.mutator);
		add(QuantifierChange.mutator);
	}

	@Override
	public Iterator<MutatedRegExp> mutate(RegExp re) {
		List<Iterator<MutatedRegExp>> allIterator = new ArrayList<>();
		for (RegexMutator m : allMutators) {
			allIterator.add(m.mutate(re));
		}
		JoinedIterator<MutatedRegExp> joinedIterator = new JoinedIterator<MutatedRegExp>(allIterator);
		return joinedIterator;
	}

	@Override
	public Iterator<MutatedRegExp> mutateRandom(RegExp re) {
		List<Iterator<MutatedRegExp>> allIterator = new ArrayList<>();
		for (RegexMutator m : allMutators) {
			allIterator.add(m.mutateRandom(re));
		}
		JoinedRandomIterator<MutatedRegExp> joinedRandomIterator = new JoinedRandomIterator<MutatedRegExp>(allIterator);
		return joinedRandomIterator;
	}

	private static void add(RegexMutator mutator) {
		allMutators.add(mutator);
		definedMutators.add(mutator);
	}

	@Override
	public String getCode() {
		return "ALL";
	}

	/** enable only these operators */
	public static void enableOnly(String[] operators) {
		allMutators.clear();
		// if null or none, put all
		if (operators == null || operators.length == 0) {
			allMutators.addAll(definedMutators);
		} else {
			List<String> ops = Arrays.asList(operators);
			for (RegexMutator m : definedMutators) {
				if (ops.contains(m.getCode()))
					allMutators.add(m);
			}
		}
	}

	/** disable this operator */
	public static void disable(String op) {
		for (Iterator<RegexMutator> iterator = allMutators.iterator(); iterator.hasNext();) {
			RegexMutator m = iterator.next();
			if (op.equals(m.getCode()))
				iterator.remove();
		}
	}

}