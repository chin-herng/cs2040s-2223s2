class Sorter {
    public static boolean isGreaterThan(String s1, String s2) {
        if (s1.length() == 0) {
            // an empty string is lexicographically smaller than any nonempty string
            // and is lexicographically equal to another empty string
            return false;
        }
        if (s2.length() == 0) {
            // any nonempty string is lexicographically greater than an empty string
            return false;
        }
        if (s1.length() == 1 || s2.length() == 1) {
            // compare the first characters
            if (s1.charAt(0) > s2.charAt(0)) {
                return true;
            }
            return false;
        }

        String actual1 = "" + s1.charAt(0) + s1.charAt(1);
        String actual2 = "" + s2.charAt(0) + s2.charAt(1);

        return actual1.compareTo(actual2) > 0;
    }

    public static void sortStrings(String[] arr) {
        // we need to implement a stable sorting algorithm that sorts in place
        // that leaves us with insertion sort and bubble sort
        // practically insertion sort is almost always preferred
        int n = arr.length;

        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0 && isGreaterThan(arr[j - 1], arr[j]); j--) {
                String temp = arr[j - 1];
                arr[j - 1] = arr[j];
                arr[j] = temp;
            }
        }
    }
}
