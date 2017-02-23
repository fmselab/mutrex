package regex.operators.stringbased;

import java.util.ArrayList;
import java.util.List;

import dk.brics.automaton.RegExp;
import dk.brics.automaton.oo.RegexVisitor;

abstract public class RegexMutationStringBased implements RegexVisitor {

	public List<RegExp> mutate(RegExp r){
		List<RegExp> result = new ArrayList<>();
		List<String> strings = mutateToString(r);
		for(String s: strings){
			result.add(new RegExp(s));
		} 
		return result;
	}

	protected abstract List<String> mutateToString(RegExp r); 
}