import java.util.HashMap;
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
	private int order;
	private final int COUNT_ASCII = 256;
	// Since the NULL character does not count as a character, the frequency of each kgram is
	// conveniently stored at index 0 of freq.get(kgram).
	private HashMap<String, int[]> freq;

	// Use this to generate random numbers as needed
	private Random generator = new Random();

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;

	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		this.order = order;
		this.freq = new HashMap<>();

		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		for (int i = 0; i < text.length() - this.order; i++) {
			// The current kgram we are trying to store into freq
			String curSubstring = text.substring(i, i + this.order);

			if (this.freq.containsKey(curSubstring)) {
				// freq already has this kgram, so we increment its frequency as well as the
				// frequency of the next character following this kgram.
				int[] value = this.freq.get(curSubstring);
				value[0]++;
				value[text.charAt(i + this.order)]++;
			} else {
				// freq doesn't have this kgram yet, so we create a new array, increment the
				// relevant indices and store it in freq.
				int[] newValue = new int[COUNT_ASCII];
				newValue[0] = 1;
				newValue[text.charAt(i + this.order)] = 1;
				this.freq.put(curSubstring, newValue);
			}
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		return this.freq.containsKey(kgram) ? this.freq.get(kgram)[0] : 0;
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		return this.freq.containsKey(kgram) ? this.freq.get(kgram)[c] : 0;
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		if (!this.freq.containsKey(kgram)) {
			return NOCHARACTER;
		}

		int randInt = this.generator.nextInt(this.getFrequency(kgram));
		int acc = 0; // accumulates frequencies until larger than randInt

		for (char c = 1; c < COUNT_ASCII; c++) {
			int currFreq = this.getFrequency(kgram, c);
			acc += currFreq;
			if (currFreq > 0 && randInt < acc) {
				return c;
			}
		}
		// should not reach here
		return '?';
	}
}
