/**
 * Contains static routines for solving the problem of balancing m jobs on p processors
 * with the constraint that each processor can only perform consecutive jobs.
 */
public class LoadBalancing {

    /**
     * Checks if it is possible to assign the specified jobs to the specified number of processors such that no
     * processor's load is higher than the specified query load.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param queryLoad the maximum load allowed for any processor
     * @param p the number of processors
     * @return true iff it is possible to assign the jobs to p processors so that no processor has more than queryLoad load.
     */
    public static boolean isFeasibleLoad(int[] jobSizes, int queryLoad, int p) {
        if (p <= 0 || jobSizes.length == 0) {
            // Invalid input
            return false;
        }
        int processorCount = 1;
        int currentProcessorLoad = 0;
        for (int i = 0; i < jobSizes.length; i++) {
            if (jobSizes[i] > queryLoad) {
                // The job size doesn't fit into even a processor without any load
                return false;
            }
            if (currentProcessorLoad + jobSizes[i] <= queryLoad) {
                // The processor can still take another job
                currentProcessorLoad += jobSizes[i];
            } else {
                // A new processor is needed
                processorCount++;
                currentProcessorLoad = jobSizes[i];
                if (processorCount > p) {
                    // We have used too many processors
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the minimum achievable load given the specified jobs and number of processors.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param p the number of processors
     * @return the maximum load for a job assignment that minimizes the maximum load
     */
    public static int findLoad(int[] jobSizes, int p) {
        if (p <= 0 || jobSizes.length == 0) {
            // Invalid input
            return -1;
        }
        int begin = 0;
        int end = 0;
        for (int i = 0; i < jobSizes.length; i++) {
            end += jobSizes[i];
        }
        // end could've been Integer.MAX_VALUE and that would produce an O(n) solution.
        // However, there could be cases where sum of jobSizes > Integer.MAX_VALUE.
        // Also, the question did ask for the running time in terms of n and M and this
        // implementation allows us to involve M into the running time.
        // One can also argue that if sum of jobSizes > Integer.MAX_VALUE then we should've made
        // use of the long data type...
        while (begin < end) {
            int mid = (begin + end) / 2;
            if (isFeasibleLoad(jobSizes, mid, p)) {
                end = mid;
            } else {
                begin = mid + 1;
            }
        }
        return begin;
    }

    // These are some arbitrary testcases.
    public static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, 100, 80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83},
            {7}
    };

    /**
     * Some simple tests for the findLoad routine.
     */
    public static void main(String[] args) {
        for (int p = 1; p < 30; p++) {
            System.out.println("Processors: " + p);
            for (int[] testCase : testCases) {
                System.out.println(findLoad(testCase, p));
            }
        }
    }
}
