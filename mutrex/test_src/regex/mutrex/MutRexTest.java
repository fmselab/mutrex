package regex.mutrex;

import static org.junit.Assert.assertNotNull;

import java.util.Map.Entry;

import org.junit.Test;

import regex.distinguishing.DistinguishingString;

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
		for (Entry<DistinguishingString, RegExpSet> d : x.dsKilledMutant.entrySet()) {
			RegExpSet value = d.getValue();
			assert !value.isEmpty();
			System.out.println(d.getKey() + " kills " + value);
		}
	}
}
