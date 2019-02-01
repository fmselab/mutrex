package regex.mutrex.main;

import static org.junit.Assert.assertNotNull;

import java.util.Iterator;

import org.junit.Test;

import regex.distinguishing.DistinguishingString;
import regex.mutrex.ds.DSSet;
import regex.mutrex.ds.DSSetGenerator;
import regex.mutrex.ds.RegExpSet;

public class MutRexTest {

	@Test
	public void testPlainGeneratorWeb() {
		DSSet DSs = MutRex.generateStrings("[A-Za-z]{6}", GeneratorType.BASIC);
		for (DistinguishingString ds : DSs) {
			assertNotNull(ds);
			assertNotNull(ds.getDs());
		}
	}

	@Test
	public void testGenerateAPlus() {
		generate("a+");
	}

	@Test
	public void testGenerateaorb() {
		generate("a|b");
	}

	@Test
	public void testGenerateAB() {
		generate("ab");
	}

	@Test
	public void testGenerateAsterisco() {
		generate("*");
	}

	@Test
	public void testGenerateAAster() {
		generate("a\\*");
	}

	@Test
	public void testGenerateW() {
		generate("\\w*");
	}

	
	@Test
	public void testGeneratePoint() {
		generate("a\\.");
	}
		

	@Test
	public void testString() {
		generate("\\\"--help\\\"");
	}
	
	private void generate(String regex) {
		for (DSSetGenerator dsgen : MutRex.generators) {
			generateAndCheck(regex, dsgen);
		}
	}

	/**
	 * 
	 * @param regex
	 * @param dsgen
	 */
	static void generateAndCheck(String regex, DSSetGenerator dsgen) {
		DSSet x = dsgen.generateDSSet(regex);
		Iterator<DistinguishingString> it = x.iterator();
		while(it.hasNext()) {
			DistinguishingString ds = it.next();
			RegExpSet value = x.getKilledMutants(ds);
			assert !value.isEmpty();
			System.out.println(ds + " kills " + value);
		}
	}
}
