import java.util.Arrays;

/**
 * class ModifiedShiftRegister
 * @author Chong Chin Herng
 * Description: Modifies ShiftRegister to take a text string and convert it to a binary string
 *              to use as a seed.
 */
public class ModifiedShiftRegister implements ModifiedILFShiftRegister {
    ///////////////////////////////////
    // Create your class variables here
    ///////////
    // //////////////////////
    // The current value of the shift register
    int[] bits;
    // The bit to be used in the XOR operation
    int tap;

    ///////////////////////////////////
    // Create your constructor here:
    ///////////////////////////////////
    ModifiedShiftRegister(int tap) {
        // Assume tap is a positive integer
        this.tap = tap;
    }

    ///////////////////////////////////
    // Create your class methods here:
    ///////////////////////////////////
    /**
     * setSeed
     * @param s The given text string
     * Description: Sets the shift register to the binary string converted from the given text
     *              string
     */
    @Override
    public void setSeed(String s) {
        int size = 16 * s.length(); // java char size is 16 bits
        // If size is no larger than tap, throw an exception
        this.bits = new int[size];
        // Convert String to char[]
        char[] charArray = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            charArray[i] = s.charAt(i);
        }
        String[] binaryStrings = new String[s.length()];
        for (int i = 0; i < s.length(); i++) {
            // Convert each char to a binary string.
            binaryStrings[i] = Integer.toBinaryString(charArray[i]);
        }
        int j = 0;
        // Assign the bits of the shift register using the binary strings
        for (String binaryString : binaryStrings) {
            for (int i = 0; i < 16 - binaryString.length(); i++) {
                bits[j] = 0;
                j++;
            }
            for (int i = 0; i < binaryString.length(); i++) {
                bits[j] = Character.getNumericValue(binaryString.charAt(i));
                j++;
            }
        }
    }

    /**
     * shift
     * @return The feedback bit
     * Description: Executes one shift step and returns the least significant bit of resulting
     *              register. The feedback bit is first computed, followed by the shifting
     */
    @Override
    public int shift() {
        int size = this.bits.length;
        int feedbackBit = this.bits[size - 1] ^ this.bits[tap];
        for (int i = size - 1; i > 0; i--) {
            this.bits[i] = this.bits[i - 1];
        }
        this.bits[0] = feedbackBit;
        return feedbackBit;
    }

    /**
     * generate
     * @param k The number of bits to be extracted
     * @return The extracted bits converted from binary into an integer
     * Description: Extracts k bits from the shift register, then converts them to base 10.
     *              v is initialised to be 0. Every time there is a new bit from shift, v is
     *              multiplied by two, and possibly added by 1, depending on the bit from shift.
     */
    @Override
    public int generate(int k) {
        int v = 0;
        for (int i = 0; i < k; i++) {
            v = 2 * v + this.shift();
        }
        return v;
    }

    /**
     * Returns the integer representation for a binary int array.
     * @param array
     * @return
     */
    private int toDecimal(int[] array) {
        // TODO:
        return 0;
    }
}
