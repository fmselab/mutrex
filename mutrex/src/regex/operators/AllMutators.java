package regex.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dk.brics.automaton.RegExp;
import regex.utils.JoinedIterator;

public class AllMutators extends RegexMutator {
	public static AllMutators mutator = new AllMutators();

	protected AllMutators() {
		super(new RegexVisitorAdapterList());
	}

	// list of all the mutators that can possibly used
	public static List<RegexMutator> allMutators = new ArrayList<RegexMutator>();

	// probably a file or a plugin mechanism or reflection could be better
	static {
		add(CaseChange.mutator);
		add(CaseUnsensetiveness.mutator);
		add(MetaChar2Char.mutator);
		add(Char2MetaChar.mutator);
		//add(MissingCharacterClass.mutator);//questa sembra sbagliata
		add(PutCharsInRange.mutator);//questa sembra migliore
		add(CharClassAddition.mutator);
		add(OutOfOneInCharRange.mutator);
		add(CharacterClassRestriction.mutator);
		add(MissingPrefix.mutator);
		add(MissingNegatedCharacterClass.mutator);
		add(NegatedCharacterClassEmpty.mutator);
		add(MissingNegation.mutator);
		add(WrongCardinality.mutator);
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

	private static void add(RegexMutator mutator) {
		allMutators.add(mutator);
	}

	@Override
	public String getCode() {
		return "ALL";
	}
}