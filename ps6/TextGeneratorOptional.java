import java.io.FileInputStream;

/**
 * This class is used to generated text using a Markov Model
 */
public class TextGenerator {

    // For testing, we will choose different seeds
    private static long seed;

    // Sets the random number generator seed
    public static void setSeed(long s) {
        seed = s;
    }

    /**
     * Reads in the file and builds the MarkovModel.
     *
     * @param order the order of the Markov Model
     * @param fileName the name of the file to read
     * @param model the Markov Model to build
     */
    public static void buildModel(int order, String fileName, MarkovModel model) {
        // Get ready to parse the file.
        // StringBuffer is used instead of String as appending character to String is slow
        StringBuilder text = new StringBuilder();

        // Loop through the text
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            // Determine the size of the file, in bytes
            int fileSize = inputStream.available();

            // Read in the file, one character at a time.
            for (int i = 0; i < fileSize; i++) {
                // Read a character
                char c = (char) inputStream.read();
                text.append(c);
            }

            // Make sure that length of input text is longer than requested Markov order
            if (text.toString().split("\\s+").length < order) {
                System.out.println("Text is shorter than specified Markov Order.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Problem reading file " + fileName + ".");
            return;
        }

        // Build Markov Model of order from text
        model.initializeText(text.toString());
    }

    /**
     * generateText outputs to stdout text of the specified length based on the specified seedText
     * using the given Markov Model.
     *
     * @param model the Markov Model to use
     * @param seedText the initial "kgram" used to generate text
     * @param length the length of the text to generate
     */
    public static void generateText(MarkovModel model, String seedText, int length) {
        // Use the first order words of the text as the starting string
        StringBuffer kgram = new StringBuffer();
        kgram.append(seedText);

        // Generate length words
        String currKgram = seedText;
        String strToAppend;
        int outLength = seedText.toString().split("\\s+").length;
        while (outLength < length) {
            // Get the next string from "kgram" sequence.
            strToAppend = model.nextString(currKgram);

            if (!strToAppend.equals(MarkovModel.NOSTRING)) {
                kgram.append(strToAppend);

                // Update current kgram
                int firstSpaceIdx = 0;
                while (firstSpaceIdx < currKgram.length() && currKgram.charAt(firstSpaceIdx) != ' ') {
                    firstSpaceIdx++;
                }
                currKgram = firstSpaceIdx == currKgram.length() ? "" : currKgram.substring(firstSpaceIdx + 1) + strToAppend;

                outLength++;
            } else {
                // This prefix has never appeared in the text.
                // Yes, give up.
                System.out.print(kgram);
                return;
            }
        }

        // Output the generated characters, not including the initial seed.
        System.out.print(kgram);
//        System.out.println("Serve immediately.");
    }

    /**
     * The main routine.  Takes 3 arguments:
     * args[0]: the order of the Markov Model
     * args[1]: the length of the text to generate
     * args[2]: the filename for the input text
     */
    public static void main(String[] args) {
        // Check that we have three parameters
        if (args.length != 3) {
            System.out.println("Number of input parameters are wrong.");
        }

        // Get the input:
        int order = Integer.parseInt(args[0]);
        int length = Integer.parseInt(args[1]);
        String fileName = args[2];

        // Create the model
//        setSeed(4927);
        MarkovModel markovModel = new MarkovModel(order, seed);
        buildModel(order, fileName, markovModel);
        String seedText = "hi. you're ";

        // Generate text
        generateText(markovModel, seedText, length);
    }
}