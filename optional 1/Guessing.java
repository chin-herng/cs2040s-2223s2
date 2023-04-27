public class Guessing {

    // Your local variables here
    private int low = 0;
    private int high = 1000;

    /**
     * Implement how your algorithm should make a guess here
     */
    public int guess() {
        if (low <= high) {
            return (low + high) / 2;
        } else {
            return low;
        }
    }

    /**
     * Implement how your algorithm should update its guess here
     */
    public void update(int answer) {
        int mid = (low + high) / 2;
        if (answer == 1) {
            high = mid - 1;
        } else {
            low = mid + 1;
        }
    }
}
