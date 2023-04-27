public class FindKeysMinimumAttempts implements IFindKeys {
    @Override
    public int[] findKeys(int N, int k, ITreasureExtractor treasureExtractor) {
        // the idea is to perform binary search k times to find the k keys
        int[] keys = new int[N]; // will store our final answer
        int lastFound = N; // the index of the last found key
        for (int keysLeft = k; keysLeft > 0; keysLeft--) {
            int begin = keysLeft - 1;
            // when no keys have been found yet, end = N - 1
            // therefore we initialise lastFound = N
            // alternatively we can use the ternary operator
            int end = lastFound - 1;
            while (begin < end) {
                int[] query = new int[N]; // will store our guess
                int mid = begin + (end - begin) / 2;
                // our query will consist of key 1 to key mid,
                // together with all currently found keys.
                for (int i = 0; i <= mid; i++) {
                    query[i] = 1;
                }
                for (int i = mid + 1; i < N; i++) {
                    query[i] += keys[i];
                }
                if (treasureExtractor.tryUnlockChest(query)) {
                    end = mid;
                } else {
                    begin = mid + 1;
                }
            }
            keys[begin] = 1;
            lastFound = begin;
        }
        return keys;
    }
}