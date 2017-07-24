package dk.brics.automaton;

public class RegExpWithEquals {
	private RegExp rgx;

	public RegExpWithEquals(RegExp rgx) {
		this.rgx = rgx;
	}

	public RegExp getRgx() {
		return rgx;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RegExpWithEquals) {
			return ((RegExpWithEquals) obj).rgx.toAutomaton().equals(rgx.toAutomaton());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public String toString() {
		return rgx.toString();
	}
}