import java.util.Arrays;

class Sorter {
    public static boolean isGreaterThan(String s1, String s2) {
        if (s1.isEmpty()) {
            // an empty string is lexicographically smaller than any nonempty string
            // and is lexicographically equal to another empty string
            return false;
        }
        if (s2.isEmpty()) {
            // any nonempty string is lexicographically greater than an empty string
            return false;
        }
        if (s1.length() == 1 || s2.length() == 1) {
            // compare the first characters
            return s1.charAt(0) > s2.charAt(0);
        }

        String actual1 = "" + s1.charAt(0) + s1.charAt(1);
        String actual2 = "" + s2.charAt(0) + s2.charAt(1);

        return actual1.compareTo(actual2) > 0;
    }

    private static void mergeSort(String[] arr, int l, int r) {
        if (l == r) {
            return;
        }

        int m = l + (r - l) / 2;
        mergeSort(arr, l, m);
        mergeSort(arr, m + 1, r);

        String[] res = new String[r - l + 1];
        int i = l;
        int j = m + 1;
        int k = 0;

        while (i <= m && j <= r) {
            if (isGreaterThan(arr[i], arr[j])) {
                res[k] = arr[j];
                j++;
            } else {
                res[k] = arr[i];
                i++;
            }
            k++;
        }

        while (i <= m) {
            res[k] = arr[i];
            i++;
            k++;
        }

        while (j <= r) {
            res[k] = arr[j];
            j++;
            k++;
        }

        System.arraycopy(res, 0, arr, l, r - l + 1);
    }

    public static void sortStrings(String[] arr) {
        // merge sort
        int n = arr.length;
        mergeSort(arr, 0, n - 1);
    }

    public static void main(String[] args) {
        String[] arr = {"a", "b", "c", "d", "e", "f"};
        sortStrings(arr);
        System.out.println(Arrays.toString(arr));
    }
}
