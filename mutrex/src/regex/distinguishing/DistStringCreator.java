package regex.distinguishing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

/**
 * creates a distinguishing string among two regex.
 *
 * @author garganti
 */
public class DistStringCreator {

	/** The Constant SEARCH_MEANINGFUL. */
	public static boolean SEARCH_MEANINGFUL = true;

	// PREF_POSITIVE: it tries to generate a positive distinguishing string
	// first. If not possible, it tries to generate a negative one
	// STRICTLY_POSITIVE: it tries to generate a positive distinguishing string.
	// If this is available, it returns it only if there is no negative
	// distinguishing string. In all the other cases, it returns null
	// ONLY_POSITIVE: it only tries to generate a positive distinguishing string
	// (it returns null if this is not available)
	// PREF_NEGATIVE: it tries to generate a negative distinguishing string
	// first. If not possible, it tries to generate a positive one
	// STRICTLY_NEGATIVE: it tries to generate a negative distinguishing string.
	// If this is available, it returns it only if there is no positive
	// distinguishing string. In all the other cases, it returns null
	// ONLY_NEGATIVE: it only tries to generate a negative distinguishing string
	// (it returns null if this is not available)
	// RANDOM: it randomly generates either a positive or a negative
	// distinguishing string. If one of the two is not available, it returns the
	/**
	 * Gets the ds.
	 *
	 * @param r1
	 *            the r 1
	 * @param r2
	 *            the r 2
	 * @param dsGenPolicy
	 *            the ds gen policy
	 * @param forbiddenWords
	 *            the forbidden words
	 * @return the ds
	 */
	public static DistinguishingString getDS(RegExp r1, RegExp r2, DSgenPolicy dsGenPolicy,
			Set<String> forbiddenWords) {
		return getDS(r1.toAutomaton(), r2.toAutomaton(), dsGenPolicy, forbiddenWords);
	}

	private static DistinguishingString getDS(Automaton a1, Automaton a2, DSgenPolicy dsGenPolicy,
			Set<String> forbiddenWords) {
		switch (dsGenPolicy) {
		case RANDOM:
			// if the generated one is null, it generates the other
			boolean pos = rnd.nextBoolean();
			DistinguishingString dsRnd = pos ? getDS(a1, a2, DSgenPolicy.ONLY_POSITIVE, forbiddenWords)
					: getDS(a1, a2, DSgenPolicy.ONLY_NEGATIVE, forbiddenWords);
			if (dsRnd == null) {
				dsRnd = !pos ? getDS(a1, a2, DSgenPolicy.ONLY_POSITIVE, forbiddenWords)
						: getDS(a1, a2, DSgenPolicy.ONLY_NEGATIVE, forbiddenWords);
			}
			return dsRnd;
		case PREF_POSITIVE:
			DistinguishingString dsPrefPos = getDS(a1, a2, DSgenPolicy.ONLY_POSITIVE, forbiddenWords);
			if (dsPrefPos == null) {
				dsPrefPos = getDS(a1, a2, DSgenPolicy.ONLY_NEGATIVE, forbiddenWords);
			}
			return dsPrefPos;
		case PREF_NEGATIVE:
			DistinguishingString dsPrefNeg = getDS(a1, a2, DSgenPolicy.ONLY_NEGATIVE, forbiddenWords);
			if (dsPrefNeg == null) {
				dsPrefNeg = getDS(a1, a2, DSgenPolicy.ONLY_POSITIVE, forbiddenWords);
			}
			return dsPrefNeg;
		case STRICTLY_POSITIVE:
			// we try to generate the negative ds only of the positive one
			// exists
			DistinguishingString strictPos = getDS(a1, a2, DSgenPolicy.ONLY_POSITIVE, forbiddenWords);
			if (strictPos != null && getDS(a1, a2, DSgenPolicy.ONLY_NEGATIVE) == null) {
				return strictPos;
			}
			return null;
		case STRICTLY_NEGATIVE:
			// we try to generate the positive ds only of the negative one
			// exists
			DistinguishingString strictNeg = getDS(a1, a2, DSgenPolicy.ONLY_NEGATIVE, forbiddenWords);
			if (strictNeg != null && getDS(a1, a2, DSgenPolicy.ONLY_POSITIVE) == null) {
				return strictNeg;
			}
			return null;
		case ONLY_POSITIVE:
			String onlyPosDS = getDS(a1, a2, forbiddenWords);
			return onlyPosDS != null ? new DistinguishingString(onlyPosDS, true) : null;
		case ONLY_NEGATIVE:
			String onlyNegDS = getDS(a2, a1, forbiddenWords);
			return onlyNegDS != null ? new DistinguishingString(onlyNegDS, false) : null;
		default:
			break;
		}
		return null;
	}

	/**
	 * Gets the ds.
	 *
	 * @param r1
	 *            the r 1
	 * @param r2
	 *            the r 2
	 * @param dsGenPolicy
	 *            the ds gen policy
	 * @return the ds
	 */
	public static DistinguishingString getDS(RegExp r1, RegExp r2, DSgenPolicy dsGenPolicy) {
		return getDS(r1, r2, dsGenPolicy, Collections.EMPTY_SET);
	}

	public static DistinguishingString getDS(Automaton a1, Automaton a2, DSgenPolicy dsGenPolicy) {
		return getDS(a1, a2, dsGenPolicy, Collections.EMPTY_SET);
	}

	/**
	 * Gets the ds.
	 *
	 * @param r1
	 *            the r 1
	 * @param r2
	 *            the r 2
	 * @return the ds
	 */
	public static String getDS(RegExp r1, RegExp r2) {
		return getDS(r1, r2, Collections.EMPTY_SET);
	}

	/**
	 * Gets the ds.
	 *
	 * @param r1
	 *            the r 1
	 * @param r2
	 *            the r 2
	 * @param forbiddenWords
	 *            the forbidden words
	 * @return the ds
	 */
	public static String getDS(RegExp r1, RegExp r2, Set<String> forbiddenWords) {
		return getDS(r1.toAutomaton(), r2.toAutomaton(), forbiddenWords);
	}

	public static String getDS(Automaton a1, Automaton a2, Set<String> forbiddenWords) {
		Automaton a1MinusA2 = a1.minus(a2);
		for (String initWord : forbiddenWords) {
			String word = initWord;
			RegExp wordRgx = null;
			try {
				wordRgx = new RegExp(word);
			} catch (Exception e) {
			}
			if (wordRgx == null) {
				// System.out.println("init " + word);
				word = word.replaceAll("\\\\", "\\\\\\\\");
				// System.out.println(word);
				word = word.replaceAll("\\[", "\\\\[");
				word = word.replaceAll("\\]", "\\\\]");
				word = word.replaceAll("\\(", "\\\\(");
				word = word.replaceAll("\\)", "\\\\)");
				word = word.replaceAll("\\.", "\\\\.");
			}
			try {
				wordRgx = new RegExp(word);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("wrong. initWord: " + initWord + "\nmodified: " + word);
				return null;
			}
			// assert wordRgx != null;
			Automaton wordAut = wordRgx.toAutomaton();
			assert wordAut != null;
			a1MinusA2 = a1MinusA2.minus(wordAut);
		}
		String word = getExample(a1MinusA2);
		// System.out.println("\t" + word + "\n");
		return word;
	}

	/**
	 * build the ds between two regexes.
	 *
	 * @param r1
	 *            the r 1
	 * @param r2
	 *            the r 2
	 * @return the ds couple
	 */
	public static DistinguishingStringsCouple getDScouple(RegExp r1, RegExp r2) {
		Automaton a1 = r1.toAutomaton();
		Automaton a2 = r2.toAutomaton();
		// a1 and not a2 - confirming string (of r1)
		Automaton na2ia1 = a2.complement().intersection(a1);
		String ds1 = getExample(na2ia1);
		// a2 and not a1 - rejecting string (of r1)
		String ds2 = getExample(a1.complement().intersection(a2));
		return new DistinguishingStringsCouple(new DistinguishingString(ds1, true),
				new DistinguishingString(ds2, false));
	}

	/**
	 * Is r1 a generalization of r2?
	 * 
	 * @param r1
	 * @param r2
	 * @return
	 */
	public static boolean isGeneralization(RegExp r1, RegExp r2) {
		DistinguishingString pos = getDS(r1, r2, DSgenPolicy.ONLY_POSITIVE);
		if (pos != null) {
			DistinguishingString neg = getDS(r1, r2, DSgenPolicy.ONLY_NEGATIVE);
			return neg == null;
		}
		return false;
	}

	/**
	 * Is r1 a specialization of r2?
	 * 
	 * @param r1
	 * @param r2
	 * @return
	 */
	public static boolean isSpecialization(RegExp r1, RegExp r2) {
		return isGeneralization(r2, r1);
	}

	/**
	 * Are r1 and r2 arbitrary edits of each other?
	 * 
	 * @param r1
	 * @param r2
	 * @return
	 */
	public static boolean isArbitraryEdit(RegExp r1, RegExp r2) {
		return getDS(r1, r2, DSgenPolicy.ONLY_POSITIVE) != null && getDS(r1, r2, DSgenPolicy.ONLY_NEGATIVE) != null;
	}

	/**
	 * Are r1 and r2 equivalent?
	 * 
	 * @param r1
	 * @param r2
	 * @return
	 */
	public static boolean areEquivalent(RegExp r1, RegExp r2) {
		return getDS(r1, r2, DSgenPolicy.RANDOM) == null;
	}

	/** The rnd. */
	private static Random rnd = new Random();

	// returns an example for a
	// it tries to build a meaningful/readable example
	// use of getStrings and then search the most readable is unfeasible
	// because it returns ALL
	/**
	 * Gets the example.
	 *
	 * @param a
	 *            the a
	 * @return the example
	 */
	// the accepted string
	public static String getExample(Automaton a) {
		// get the first example provided by the library
		String result = a.getShortestExample(true);
		// String ex = getExample(a, a.getInitialState(), new HashSet<State>(), new
		// ArrayList<Transition>());
		// System.out.println(result);
		// System.out.println(ex);
		// System.out.println();

		if (result == null) {
			return null;
		}
		if (SEARCH_MEANINGFUL) {
			// try to make it nicer
			char[] nicerChars = { 'A', 'B', 'a', '1', ')' }; // to be complete
			//
			boolean nicest;
			do {
				nicest = true;
				// till there is a ugly char
				if (!result.contains("\0"))
					break;
				for (char c : nicerChars) {
					// replace the ugly char
					String mod = result.replaceFirst("\0", Character.toString(c));
					// it is accepted
					if (a.run(mod)) {
						result = mod;
						nicest = false;
						break;
					}
				}
			} while (!nicest);
		}
		return result;
		// return ex;
	}

	public static String getExample(Automaton a, State s, Set<State> visited, List<Transition> test) {
		visited.add(s);
		if (s.isAccept()) {
			StringBuilder sb = new StringBuilder();
			for (Transition t : test) {
				sb.append((char) (rnd.nextInt((t.getMax() - t.getMin()) + 1) + t.getMin()));
			}
			assert a.run(sb.toString());
			return sb.toString();
		} else {
			ArrayList<Transition> trans = new ArrayList<Transition>(s.getTransitions());
			Collections.shuffle(trans);
			for (Transition t : trans) {
				if (!visited.contains(t.getDest())) {
					test.add(t);
					String res = getExample(a, t.getDest(), visited, test);
					if (res != null) {
						return res;
					}
					test.remove(t);
				}
			}
		}
		return null;
	}
}
