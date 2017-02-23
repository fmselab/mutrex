package regex.distinguishing;

// TODO: Auto-generated Javadoc
/**
 *  a couple of strings, one is confirming the other rejecting.
 */
public class DistinguishingStringsCouple {
	
	/** The ds 2. */
	private DistinguishingString ds1, ds2;

	/**
	 * Instantiates a new distinguishing strings couple.
	 *
	 * @param ds1 the ds1
	 * @param ds2 the ds2
	 */
	DistinguishingStringsCouple(DistinguishingString ds1, DistinguishingString ds2) {
		assert ds1.isConfirming() && !ds2.isConfirming();
		this.ds1 = ds1;
		this.ds2 = ds2;
	}

	/**
	 * Gets the confirming DS 
	 *
	 * @return the confirming
	 */
	DistinguishingString getConfirming1() {
		return ds1;
	}

	/**
	 * Gets the configrming of the second and rejecting of the first
	 *
	 * @return the rejecting
	 */
	DistinguishingString getConfirming2() {
		return ds2;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ds1 + " : " + ds2;
	}
}