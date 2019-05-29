package regex.oracle;

import java.util.HashSet;
import java.util.Set;

/**
 * it is an oracle that keeps memory of the string it has been asked
 * 
 * @author garganti
 *
 */
public class CachedOracle implements RegexOracle {
	private Set<String> acceptedByOracle;
	private Set<String> rejectedByOracle;
	private RegexOracle oracle;

	// the real oracle to ask in case there is new string to evaluate
	public CachedOracle(RegexOracle oracle) {
		acceptedByOracle = new HashSet<String>();
		rejectedByOracle = new HashSet<String>();
		this.oracle = oracle;
	}

	/**
	 * it keeps track also of the string previously evaluated to reduce the number
	 * of evaluations
	 * 
	 * @param s
	 * @return
	 */
	@Override
	public final boolean accept(String s) {
		// if already evaluated
		if (acceptedByOracle.contains(s)) {
			return true;
		} else if (rejectedByOracle.contains(s)) {
			return false;
		} else {
			// not evaluated yet
			boolean isAccepted = oracle.accept(s);
			if (isAccepted) {
				acceptedByOracle.add(s);
			} else {
				rejectedByOracle.add(s);
			}
			return isAccepted;
		}
	}

	public Set<String> getAcceptedByOracle() {
		return acceptedByOracle;
	}

	public Set<String> getRejectedByOracle() {
		return rejectedByOracle;
	}

	public int getNumEvaluatedStrings() {
		return acceptedByOracle.size() + rejectedByOracle.size();
	}

	public int getNumAcceptedStrings() {
		return acceptedByOracle.size();
	}

	public RegexOracle getOracle() {
		return oracle;
	}
}
