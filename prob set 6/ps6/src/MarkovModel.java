import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {

	// Use this to generate random numbers as needed
	private Random generator = new Random();

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;

	// to store number of times a char appears after a string
	public HashMap<String, int[]> freqchar;
	// to store number of times a string appears in the given text
	public HashMap<String, Integer> freqstr;
	private int order;
	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		this.freqchar = new HashMap<>();
		this.freqstr = new HashMap<>();
		this.order = order;
		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		// Build the Markov model here
		// iterate through all possible strings of size k
		for (int i = 0; i < text.length() - this.order; i++) {
			// generate string of size k to use as key
			String kgram = text.substring(i, i + this.order);
			// create bucket in hashtable if the bucket has not yet been initialised (no value)
			if (!this.freqchar.containsKey(kgram) && !this.freqstr.containsKey(kgram)) {
				this.freqchar.put(kgram, new int[256]);
				this.freqstr.put(kgram, 0);
			}
			// get ascii character to map to count in count array of hash table
			char next = text.charAt(i + this.order);
			int ascii = (int) next;
			int[] freqc = this.freqchar.get(kgram);
			freqc[ascii]++;
			// increment count of string
			int freqs = this.freqstr.get(kgram);
			freqs++;
			this.freqstr.replace(kgram, freqs);
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		if (kgram.length() == this.order && this.freqstr.containsKey(kgram)) {
			return this.freqstr.get(kgram);
		} else {
			return 0;
		}
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		if (kgram.length() == this.order && this.freqchar.containsKey(kgram)) {
			int ascii = (int) c;
			int[] freqc = this.freqchar.get(kgram);
			return freqc[ascii];
		} else {
			return 0;
		}
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		// See the problem set description for details
		// on how to make the random selection.
		if (this.freqchar.containsKey(kgram)) {
			int totalcount = 0;
			int[] count = this.freqchar.get(kgram);
			for (int i = 0; i < 256; i++) {
				totalcount += count[i];
			}

			int random = generator.nextInt(totalcount);
			char c = NOCHARACTER;

			for (int i = 0; i < 256; i++) {
				random -= count[i];
				if (random < 0) {
					c = (char) i;
					break;
				}
			}
			return c;
		} else {
			return NOCHARACTER;
		}
	}

	/*
			char[] chars = new char[totalcount];
			int j = 0;
			for (int i = 0; i < 256; i++) {
				if (count[i] != 0) {
					int end = j + count[i];
					char c = (char) i;
					while (j < end) {
						chars[j] = c;
						j++;
					}
				}
			}

			int random = generator.nextInt(totalcount);
			return chars[random];
	 */
}
