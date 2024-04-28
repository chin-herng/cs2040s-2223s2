///////////////////////////////////
// This is the main shift register class.
// Notice that it implements the ILFShiftRegister interface.
// You will need to fill in the functionality.
///////////////////////////////////

/**
 * class ShiftRegister
 * @author Chong Chin Herng
 * Description: implements the ILFShiftRegister interface.
 */
public class ShiftRegister implements ILFShiftRegister {
    ///////////////////////////////////
    // Create your class variables here
    ///////////
    // //////////////////////
    // The current value of the shift register
    int[] bits;
    // The number of bits
    int size;
    // The bit to be used in the XOR operation
    int tap;

    ///////////////////////////////////
    // Create your constructor here:
    ///////////////////////////////////
    ShiftRegister(int size, int tap) {
        // size and tap must be positive integers, and tap must be at most size - 1
        // Otherwise, throw an exception to indicate illegal argument
        this.size = size;
        this.tap = tap;
    }

    ///////////////////////////////////
    // Create your class methods here:
    ///////////////////////////////////
    /**
     * setSeed
     * @param seed The specified initial seed
     * Description: Sets the shift register to the specified initial seed
     */
    @Override
    public void setSeed(int[] seed) {
        // If the given seed is not of correct size, throw an exception
        // If any of the elements of the given seed is neither 0 nor 1, throw an exception
        this.bits = seed;
    }

    /**
     * shift
     * @return The feedback bit
     * Description: Executes one shift step and returns the least significant bit of resulting
     *              register. The feedback bit is first computed, followed by the shifting
     */
    @Override
    public int shift() {
        // Compute the feedback bit
        int feedbackBit = this.bits[size - 1] ^ this.bits[tap];
        // Shift the bits
        for (int i = size - 1; i > 0; i--) {
            this.bits[i] = this.bits[i - 1];
        }
        // Assign the feedback bit to the least significant bit
        this.bits[0] = feedbackBit;
        // Return the feedback bit
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
            // Multiplying v by 2 effectively inserts a 0 as the least significant bit of the
            // binary representation of v. Then depending on the feedback bit of the shift, this
            // least significant bit is assigned to either 0 or 1, effectively increasing v by 0
            // or 1.
            v = 2 * v + this.shift();
        }
        return v;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = size - 1; i > -1; i--) {
            res.append((char) ('0' + bits[i]));
        }
        return res.toString();
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
