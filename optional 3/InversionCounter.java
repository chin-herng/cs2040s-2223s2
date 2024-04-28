import java.util.Arrays;

class InversionCounter {

    public static long countSwaps(int[] arr) {
        if (arr.length == 1) {
            return 0;
        }

        int n = arr.length;
        int mid = (n - 1)/2;
        int[] l = Arrays.copyOf(arr, mid + 1);
        int[] r = new int[n - mid - 1];
        for (int i = mid + 1; i < n; i++) {
            r[i - mid - 1] = arr[i];
        }
        long leftCount = countSwaps(l);
        long rightCount = countSwaps(r);
        if (mid + 1 >= 0) {
            System.arraycopy(l, 0, arr, 0, mid + 1);
        }
        for (int i = mid + 1; i < n; i++) {
            arr[i] = r[i - mid - 1];
        }
        return leftCount + rightCount + mergeAndCount(arr, 0, mid, mid + 1, n - 1);
    }

    /**
     * Given an input array so that arr[left1] to arr[right1] is sorted and arr[left2] to arr[right2] is sorted
     * (also left2 = right1 + 1), merges the two so that arr[left1] to arr[right2] is sorted, and returns the
     * minimum amount of adjacent swaps needed to do so.
     */
    public static long mergeAndCount(int[] arr, int left1, int right1, int left2, int right2) {
        int lIndex = 0;
        int rIndex = 0;
        long count = 0;
        long displacement = 0;
        int[] l = Arrays.copyOf(arr, right1 - left1 + 2);
        int[] r = new int[right2 - left2 + 2];

        if (right2 + 1 - left2 >= 0) {
            System.arraycopy(arr, left2, r, 0, right2 + 1 - left2);
        }
        l[right1 - left1 + 1] = Integer.MAX_VALUE;
        r[right2 - left2 + 1] = Integer.MAX_VALUE;
        for (int i = 0; i < right2 - left1 + 1; i++) {
            if (l[lIndex] <= r[rIndex]) {
                arr[i] = l[lIndex];
                lIndex++;
                count += displacement;
            } else {
                arr[i] = r[rIndex];
                rIndex++;
                displacement++;
            }
        }

        return count;
    }
}
