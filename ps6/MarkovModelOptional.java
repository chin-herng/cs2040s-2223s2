import java.util.Map;
import java.util.HashMap;
import java.util.Random;

/**
 * This is the main class for a Word-based Markov Model.
 * Assume that the text will contain ASCII characters in the range [1,255].
 */
public class MarkovModelOptional {
    // Here, a kgram refers to a k-word string, where k is the order
    private final int order;
    // Frequency of each kgram
    private final HashMap<String, Integer> freq;
    // Frequency of each word following each kgram
    private final HashMap<String, HashMap<String, Integer>> afterFreq;

    // Generate random numbers
    private Random generator = new Random();

    // This is a special symbol to indicate no string
    public static final String NOSTRING = "";

    /**
     * Constructor for MarkovModel class.
     *
     * @param order the number of words to identify for the Markov Model sequence
     * @param seed the seed used by the random number generator
     */
    public MarkovModelOptional(int order, long seed) {
        this.order = order;
        this.freq = new HashMap<>();
        this.afterFreq = new HashMap<>();

        // Initialize the random number generator
        this.generator.setSeed(seed);
    }

    /**
     * Builds the Markov Model based on the specified text string.
     */
    public void initializeText(String text) {
        // Splits the text into space-separated "words"
        String[] words = text.split("\\s+");

        // Appends a space to each word
        for (int i = 0; i < words.length; i++) {
            words[i] += " ";
        }

        for (int i = 0; i < words.length - this.order; i++) {
            // Constructs the current kgram and store it in curWords
            StringBuilder sb = new StringBuilder(words[i]);
            for (int j = 1; j < this.order; j++) {
                sb.append(words[i + j]);
            }
            String curWords = sb.toString();

            // Increment the current kgram's frequency
            this.freq.merge(curWords, 1, Integer::sum);
            if (this.afterFreq.containsKey(curWords)) {
                // Increment the frequency of the word following the current kgram
                HashMap<String, Integer> value = this.afterFreq.get(curWords);
                value.merge(words[i + this.order], 1, Integer::sum);
            } else {
                // Create a new HashMap for the current kgram
                HashMap<String, Integer> newValue = new HashMap<>();
                newValue.put(words[i + this.order], 1);
                this.afterFreq.put(curWords, newValue);
            }
        }
    }

    /**
     * Returns the number of times the specified "kgram" appeared in the text.
     */
    public int getFrequency(String kgram) {
        return this.freq.getOrDefault(kgram, 0);
    }

    /**
     * Returns the number of times the string c appears immediately after the specified "kgram".
     */
    public int getFrequency(String kgram, String s) {
        return this.afterFreq.containsKey(kgram) ? this.afterFreq.get(kgram).getOrDefault(s, 0) : 0;
    }

    /**
     * Generates the next string from the Markov Model.
     * Return NOCHARACTER if the "kgram" is not in the table, or if there is no
     * valid string following the "kgram".
     */
    public String nextString(String kgram) {
        if (!this.freq.containsKey(kgram)) {
            return NOSTRING;
        }

        // Accumulates frequencies until larger than randInt,
        // same idea as the basic character-based model.
        int acc = 0;
        int randInt = this.generator.nextInt(this.getFrequency(kgram));

        for (Map.Entry<String, Integer> entry : this.afterFreq.get(kgram).entrySet()) {
            int currFreq = entry.getValue();
            acc += currFreq;
            if (currFreq > 0 && randInt < acc) {
                return entry.getKey();
            }
        }

        // should not reach here
        return "?";
    }
}
