package dk.brics.automaton.oo;

public interface VisitableRegex {
	public <T> T accept(RegexVisitor<T> v);
}