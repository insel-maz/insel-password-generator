package de.inseltroll.passwordgenerator;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author maz
 */
public class Generator {

	// shameless stolen from java.util.Random
	private static long seedUniquifier = 8682522807148012L;

	// shameless stolen from java.util.Random
	public static long createSeed() {
		seedUniquifier *= 181783497276652981L;
		return seedUniquifier ^ System.nanoTime();
	}
	
	private static Set<Character> range(char from, char to) {
		int i = (int)from;
		int j = (int)to;
		Set<Character> set = new LinkedHashSet<>();
		for (; i <= j; ++i) {
			set.add((char)i);
		}
		return set;
	}
	
	private static Set<Character> charSet(char[] chars) {
		Set<Character> set = new LinkedHashSet<>();
		for (char c : chars) {
			set.add(c);
		}
		return set;
	}
	
	private long seed;
	private final Random random;
	
	private final Set<Character> upperAlphas;
	private final Set<Character> lowerAlphas;
	private final Set<Character> numerics;
	private final Set<Character> specials;
	private final Set<Character> printableAsciis;
	private final Set<Character> homoglyphs;
	
	public int length;
	public boolean useAlphaFirst;
	public boolean omitHomoglyphs;
	public boolean useUpperAlpha;
	public boolean useLowerAlpha;
	public boolean useNumeric;
	public boolean useSpecial;
	public boolean usePrintableAscii;
	
	public Generator() {
		seed = createSeed();
		random = new Random(seed);
		
		upperAlphas = range('A', 'Z');
		lowerAlphas = range('a', 'z');
		numerics = range('0', '9');
		specials = charSet("!§$%&/()=?*+-".toCharArray());
		printableAsciis = range((char)33, (char)126);
		homoglyphs = charSet("01LlIiOo".toCharArray());
	}
	
	public void applyNewSeed() {
		seed = createSeed();
	}
	
	public String generate() {
		Set<Character> alphas = new LinkedHashSet<>();
		if (useUpperAlpha || usePrintableAscii) {
			alphas.addAll(upperAlphas);
		}
		if (useLowerAlpha || usePrintableAscii) {
			alphas.addAll(lowerAlphas);
		}
		
		Set<Character> chars = new LinkedHashSet<>(alphas);
		if (useNumeric) {
			chars.addAll(numerics);
		}
		if (useSpecial) {
			chars.addAll(specials);
		}
		if (usePrintableAscii) {
			chars.addAll(printableAsciis);
		}
		
		if (omitHomoglyphs) {
			alphas.removeAll(homoglyphs);
			chars.removeAll(homoglyphs);
		}
		
		ArrayList<Character> alphasList = new ArrayList<>(alphas);
		ArrayList<Character> charsList = new ArrayList<>(chars);
		
		random.setSeed(seed);
		
		char[] result = new char[length];
		for (int i = 0; i < length; ++i) {
			ArrayList<Character> list = i == 0 && useAlphaFirst ? alphasList : charsList;
			
			int index = random.nextInt(list.size());
			result[i] = list.get(index);
		}
		
		return String.valueOf(result);
	}
	
}
