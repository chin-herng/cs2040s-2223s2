import java.util.HashMap;

public class Solution {
    public static int solve(int[] arr) {
        if (arr == null) {
            return 0;
        }

        int n = arr.length;
        int maxLength = -1;
        // HashMap records the index at which we have seen the element last time.
        HashMap<Integer, Integer> index = new HashMap<>();

        // Two pointers: i and j are start and end of our contiguous segment respectively.
        for (int i = 0, j = 0; i < n; i++, j++) {
            while (j < n && !index.containsKey(arr[j])) {
                // If we start from i, how far can j go?
                index.put(arr[j], j);
                j++;
            }
            maxLength = Math.max(maxLength, j - i);

            if (j == n) {
                // Further increase to i only makes the segment shorter.
                return maxLength;
            }

            // Now we move our starting index i, "unseeing" some of the elements.
            while (i < index.get(arr[j])) {
                index.remove(arr[i]);
                i++;
            }

            // Update the HashMap.
            index.put(arr[j], j);
        }

        // n = 0.
        return 0;
    }
}