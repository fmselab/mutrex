package regex.distinguishing;

/**
 * The Class DistinguishingString.
 */
public class DistinguishingString {

	/** The ds. */
	private String ds;

	/** The is confirming. */
	private boolean isConfirming;

	/**
	 * Instantiates a new distinguishing string.
	 *
	 * @param ds
	 *            the ds
	 * @param isConfirming
	 *            the is confirming
	 */
	public DistinguishingString(String ds, boolean isConfirming) {
		this.ds = ds;
		this.isConfirming = isConfirming;
	}

	/**
	 * Gets the ds.
	 *
	 * @return the ds
	 */
	public String getDs() {
		return ds;
	}

	/**
	 * Checks if is confirming.
	 *
	 * @return true, if is confirming
	 */
	public boolean isConfirming() {
		return isConfirming;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\"" + ds + "\" (" + (isConfirming ? "CONF" : "REJECT") + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ds == null) ? 0 : ds.hashCode());
		result = prime * result + (isConfirming ? 1231 : 1237);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistinguishingString other = (DistinguishingString) obj;
		if (ds == null) {
			if (other.ds != null)
				return false;
		} else if (!ds.equals(other.ds))
			return false;
		if (isConfirming != other.isConfirming)
			return false;
		return true;
	}
}