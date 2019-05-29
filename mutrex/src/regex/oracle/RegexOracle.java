package regex.oracle;

// given an example, it is able to accept or reject
// the cached version is in another class
// this one has no memory
public interface RegexOracle {

	/** is string s accepted by the oracle */
	public abstract boolean accept(String s);

}
