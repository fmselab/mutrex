package regex.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.brics.automaton.ExtendedRegex;
import dk.brics.automaton.RegExp;

public class RegexExamplesTaker {

	// take a regex in the folder of the experiments by name
	static public RegExp readExampleRegex(String category, String name) throws IOException {
		String line = readExampleRegexesString(category, name);
		RegExp regex = ExtendedRegex.getSimplifiedRegexp(line);
		return regex;
	}

	/**
	 * 
	 * @param category
	 *            (file name)
	 * @param name
	 *            name of the regex (not simplified)
	 * @return
	 * @throws IOException
	 */
	static public String readExampleRegexesString(String category, String name) throws IOException {
		FileReader fr = new FileReader("examples/" + category + ".txt");
		BufferedReader reader = new BufferedReader(fr);
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith(name)) {
				line = line.substring(name.length() + 1).trim();
				reader.close();
				return line;
			}
		}
		reader.close();
		throw new RuntimeException("expression " + name + " not found");
	}

	// take a regex in the folder of the experiments by name
	// simplified
	static public List<Pair<String, RegExp>> readExamplesRegex(String category) throws IOException {
		List<Pair<String, RegExp>> result = new ArrayList<>();
		List<Pair<String, String>> reader = readExamplesRegexAsStrings(category, true);
		for (Pair<String, String> r : reader) {
			// System.out.println(r);
			try {
				RegExp regex = ExtendedRegex.getSimplifiedRegexp(r.second);
				result.add(new Pair<String, RegExp>(r.first, regex));
			} catch (Exception e) {
				System.err.println("error reading " + r.toString() + " " + e.getMessage());
				continue;
			}
		}
		return result;
	}

	// take a regex in the folder of the experiments by name
	/**
	 * Read examples regex as strings.
	 *
	 * @param category
	 *            the category
	 * @param simplify
	 *            simplify the metachars like \s ...
	 * @return the list
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	// it also simplifies the regex
	static public List<Pair<String, String>> readExamplesRegexAsStrings(String category, boolean simplify)
			throws IOException {
		FileReader fr = new FileReader("examples/" + category + ".txt");
		List<Pair<String, String>> result = new ArrayList<>();
		BufferedReader reader = new BufferedReader(fr);
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("//"))
				continue;
			if (line.isEmpty())
				continue;
			int sepPos = line.indexOf(':');
			assert sepPos > 0 : line;
			String regex = line.substring(sepPos + 1).trim();
			result.add(new Pair<String, String>(line.substring(0, sepPos),
					simplify ? ExtendedRegex.simplifyRegex(regex) : regex));
		}
		reader.close();
		return result;
	}

	static public class Pair<T, S> {
		public T first;
		public S second;

		public Pair(T t, S s) {
			first = t;
			second = s;
		}

		@Override
		public String toString() {
			return first + ":" + second;
		}
		
		@Override
		public int hashCode() {
			return first.hashCode() + second.hashCode();
		}
		
	}
}