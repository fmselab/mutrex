package regex.mutrex.ds;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import regex.distinguishing.DistinguishingString;
import regex.operators.RegexMutator.MutatedRegExp;

/**
 * set of distinguishing configurations together with the regexes the are able
 * to distinguish.
 * 
 * @author garganti
 *
 */
public class DSSet implements Iterable<DistinguishingString> {

	private Map<DistinguishingString, RegExpSet> dsKilledMutant = new HashMap<DistinguishingString, RegExpSet>();

	/**
	 * add killed mutants with their description
	 * 
	 * @param ds
	 * @param mutants
	 *            together with their description (in futuro dovremmo usare
	 *            questa)
	 */
	public synchronized void add(DistinguishingString ds, List<MutatedRegExp> mutants) {
		assert ds != null;
		assert !mutants.isEmpty();
		RegExpSet list = dsKilledMutant.get(ds);
		if (list == null) {
			// System.out.println("new\t" + ds);
			list = new RegExpSet();
			dsKilledMutant.put(ds, list);
		}
		/*
		 * else { assert mutants.size() == 1; System.out.println("present\t" +
		 * ds + "\t" + mutants.get(0).description + "\t" + mutants.get(0)); }
		 */
		list.addAllWD(mutants);
	}

	/** number of tests */
	public int size() {
		return dsKilledMutant.size();
	}

	@Override
	public Iterator<DistinguishingString> iterator() {
		return dsKilledMutant.keySet().iterator();
	}

	public RegExpSet getKilledMutants(DistinguishingString ds) {
		return dsKilledMutant.get(ds);
	}

	public Set<DistinguishingString> getDSs() {
		return dsKilledMutant.keySet();
	}

	public DistinguishingString getDS(int index) {
		if (index < size()) {
			return (DistinguishingString) dsKilledMutant.keySet().toArray()[index];
		}
		return null;
	}

	public void remove(DistinguishingString ds) {
		dsKilledMutant.remove(ds);
	}
}
