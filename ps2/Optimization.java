/**
 * The Optimization class contains a static routine to find the maximum in an array that changes direction at most once.
 */
public class Optimization {

    /**
     * A set of test cases.
     */
    static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, -100, -80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83}
    };

    /**
     * Returns the maximum item in the specified array of integers which changes direction at most once.
     *
     * @param dataArray an array of integers which changes direction at most once.
     * @return the maximum item in data Array
     */
    public static int searchMax(int[] dataArray) {
        int n = dataArray.length;
        if (n == 0) {
            // invalid input
            return 0;
        }
        if (n == 1) {
            // since we are comparing index 0 and 1, this case needs to be handled separately
            return dataArray[0];
        }
        if (dataArray[0] > dataArray[1]) {
            // the case where the array is V-shaped or decreasing
            return Math.max(dataArray[0], dataArray[n - 1]);
        }
        int begin = 0;
        int end = n - 1;
        while (begin < end) {
            int mid = (begin + end) / 2;
            // the idea is to compare mid with mid - 1 and mid + 1
            // therefore the case where mid = 0 or mid = n - 1 need to be handled separately
            // note that mid would never become n - 1
            if (mid == 0) {
                // condition of the loop ensures begin != mid, this means begin = 0 and end = 1
                return dataArray[1];
            }
            if (dataArray[mid] > dataArray[mid - 1]) {
                if (dataArray[mid] > dataArray[mid + 1]) {
                    // reached the peak
                    return dataArray[mid];
                } else {
                    // have not yet reached the peak
                    begin = mid + 1;
                }
            } else {
                // went past the peak
                end = mid - 1;
            }
        }
        return dataArray[begin];
    }

    /**
     * A routine to test the searchMax routine.
     */
    public static void main(String[] args) {
        for (int[] testCase : testCases) {
            System.out.println(searchMax(testCase));
        }
    }
}
