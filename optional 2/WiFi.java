import java.util.Arrays;

class WiFi {

    /**
     * Implement your solution here
     */
    public static double computeDistance(int[] houses, int numOfAccessPoints) {
        Arrays.sort(houses);
        double begin = 0;
        double end = (houses[houses.length - 1] - houses[0]) / 2.0;
        while (end - begin >= 0.5) {
            double mid = (begin + end) / 2;
            // if I write mid = (begin + end) / 2.0, the code fails one of the private test cases
            // and I don't know why
            if (coverable(houses, numOfAccessPoints, mid)) {
                end = mid;
            } else {
                begin = mid;
            }
        }
        return begin;
    }

    /**
     * Implement your solution here
     */
    public static boolean coverable(int[] houses, int numOfAccessPoints, double distance) {
        Arrays.sort(houses);
        int n = houses.length;
        int accessPointCount = 1;
        int currentLength = 0;
        for (int i = 1; i < n; i++) {
            currentLength -= houses[i - 1] - houses[i];
            if (currentLength > 2 * distance) {
                accessPointCount++;
                if (accessPointCount > numOfAccessPoints) {
                    return false;
                }
                currentLength = 0;
            }
        }
        return true;
    }
}
