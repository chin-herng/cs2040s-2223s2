import java.util.HashMap;

public class Solution {
    public static int solve(int[] arr, int target) {
        int res = 0;
        HashMap<Integer, Integer> freq = new HashMap<>();
        for (int x : arr) {
            int left = target - x;
            if (freq.containsKey(left) && freq.get(left) > 0) {
                res++;
                freq.put(left, freq.get(left) - 1);
            } else {
                freq.merge(x, 1, Integer::sum);
            }
        }

        return res;
    }
}
