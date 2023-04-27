import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Random;

public class TSPGraphTester {

    private static final String[] inputFiles = {
            "tenpoints.txt",
            "twentypoints.txt",
            "fiftypoints.txt",
            "hundredpoints.txt",
            "1000points.txt",
            "10000points.txt", // comment out this line to run the test faster
    };

    private static final int[] inputSizes = {
            10,
            20,
            50,
            100,
            1000,
            10000, // comment out this line to run the test faster
    };

    /**
     * @author Xiaoyun
     */
    private static void initLargeInput() {
        testFileGenerator.generateNPointsFile(100, 1000);
        testFileGenerator.generateNPointsFile(100, 10000);
    }

    /**
     * Creates a valid tour for the given map.
     *
     * @param map The map to create a valid tour for.
     * @author Xiaoyun
     */
    private static void createValidTour(TSPMap map) {
        for (int i = 0; i < map.getCount(); i++)
            map.setLink(i, i + 1 < map.getCount() ? i + 1 : 0, false);
    }

    /**
     * @author Xiaoyun
     */
    @Test
    public void testTimeComplexityMST() {
        initLargeInput();

        TSPGraph graph = new TSPGraph();

        // Record the times for each input size
        long[] times = new long[inputFiles.length];
        for (int i = 0; i < inputFiles.length; i++) {
            String filename = inputFiles[i];

            TSPMap map = new TSPMap(filename);
            long startTime = System.nanoTime();
            graph.MST(map);
            long endTime = System.nanoTime();
            times[i] = endTime - startTime;
        }

        // Compare the time growth to the expected O(n^2 * log n) complexity
        testNSquareLogNTimeComplexity(times);
    }

    /**
     * @author Xiaoyun
     */
    @Test
    public void testTimeComplexityTSP() {
        initLargeInput();

        TSPGraph graph = new TSPGraph();

        // Record the times for each input size
        long[] times = new long[inputFiles.length];
        for (int i = 0; i < inputFiles.length; i++) {
            String filename = inputFiles[i];

            TSPMap map = new TSPMap(filename);
            long startTime = System.nanoTime();
            graph.TSP(map);
            long endTime = System.nanoTime();
            times[i] = endTime - startTime;
        }

        // Compare the time growth to the expected O(n^2 * log n) complexity
        testNSquareLogNTimeComplexity(times);
    }

    /**
     * @author Ernest
     */
    @Test
    public void testValidTour1() {
        TSPMap map = new TSPMap("tenpoints.txt");
        createValidTour(map);
        map.setLink(9, 5, false);
        TSPGraph graph = new TSPGraph();

        boolean expected = false;
        boolean result = graph.isValidTour(map);

        System.out.println("This tour is valid: " + result);
        assertEquals(expected, result);
    }

    /**
     * @author Ernest
     */
    @Test
    public void testValidTour2() {
        TSPMap map = new TSPMap("tenpoints.txt");
        createValidTour(map);
        map.eraseLink(9, false);
        TSPGraph graph = new TSPGraph();

        boolean expected = false;
        boolean result = graph.isValidTour(map);

        System.out.println("This tour is valid: " + result);
        assertEquals(expected, result);
    }

    /**
     * @author Ernest
     */
    @Test
    public void testValidTour3() {
        TSPMap map = new TSPMap("tenpoints.txt");
        createValidTour(map);
        map.setLink(9, 1, false);
        TSPGraph graph = new TSPGraph();

        boolean expected = false;
        boolean result = graph.isValidTour(map);

        System.out.println("This tour is valid: " + result);
        assertEquals(expected, result);
    }

    /**
     * @author Xiaoyun
     */
    @Test
    public void testValidTour4() {
        TSPMap map = new TSPMap("tenpoints.txt");
        createValidTour(map);
        map.setLink(0, 2, false);
        TSPGraph graph = new TSPGraph();

        boolean expected = false;
        boolean result = graph.isValidTour(map);

        System.out.println("This tour is valid: " + result);
        assertEquals(expected, result);
    }

    /**
     * @author Xiaoyun
     */
    @Test
    public void testValidTour5() {
        TSPMap map = new TSPMap("tenpoints.txt");
        createValidTour(map);
        TSPGraph graph = new TSPGraph();

        boolean expected = true;
        boolean result = graph.isValidTour(map);

        System.out.println("This tour is valid: " + result);
        assertEquals(expected, result);
    }

    /**
     * @author Xiaoyun
     */
    @Test
    public void testTimeComplexityIsValidTour() {
        initLargeInput();

        TSPGraph graph = new TSPGraph();

        // Record the times for each input size
        long[] times = new long[inputFiles.length];
        for (int i = 0; i < inputFiles.length; i++) {
            String filename = inputFiles[i];

            TSPMap map = new TSPMap(filename);
            createValidTour(map);
            long startTime = System.nanoTime();
            graph.isValidTour(map);
            long endTime = System.nanoTime();
            times[i] = endTime - startTime;
        }

        // Compare the time growth to the expected O(n) complexity
        testLinearTimeComplexity(times);
    }

    /**
     * @author Xiaoyun
     */
    @Test
    public void testEarlyTerminationIsValidTour() {
        initLargeInput();

        TSPGraph graph = new TSPGraph();

        // Record the times for each input size
        double[] times = new double[inputFiles.length];
        for (int i = 0; i < inputFiles.length; i++) {
            String filename = inputFiles[i];

            TSPMap map = new TSPMap(filename);
            long startTime = System.nanoTime();
            graph.isValidTour(map);
            long endTime = System.nanoTime();
            double timeInSeconds = (endTime - startTime) / 1_000_000_000.0;
            times[i] = timeInSeconds;
            System.out.printf("Input size: %d, Time taken: %.7f seconds%n", inputSizes[i], timeInSeconds);
        }

        testConstantTimeComplexity(times);
    }

    /**
     * @author Xiaoyun
     */
    @Test
    public void testTourDistance1() {
        TSPMap map = new TSPMap("tenpoints.txt");
        createValidTour(map);
        TSPGraph graph = new TSPGraph();

        String expected = "740.85868";
        double result = graph.tourDistance(map);

        System.out.println("Tour distance: " + result);
        assertEquals(expected, String.format("%.5f", result));
    }

    /**
     * @author Xiaoyun
     */
    @Test
    public void testTourDistance2() {
        TSPMap map = new TSPMap("tenpoints.txt");
        createValidTour(map);
        map.setLink(9, 1, false);
        TSPGraph graph = new TSPGraph();

        String expected = "-1.0";
        double result = graph.tourDistance(map);

        System.out.println("Tour distance: " + result);
        assertEquals(expected, String.format("%.1f", result));
    }

    /**
     * @author Xiaoyun
     */
    @Test
    public void testTourDistance3() {
        String[] inputFiles = {
                "tenpoints.txt",
                "twentypoints.txt",
                "fiftypoints.txt",
                "hundredpoints.txt",
        };

        int[] inputSizes = { 10, 20, 50, 100 };

        String[] expected = {
                "740.85868",
                "938.36805",
                "2318.49430",
                "5686.74371",
        };

        TSPGraph graph = new TSPGraph();

        for (int i = 0; i < inputFiles.length; i++) {
            TSPMap map = new TSPMap(inputFiles[i]);
            createValidTour(map);
            double result = graph.tourDistance(map);
            System.out.printf("Input size: %d, Tour distance: %.5f%n", inputSizes[i], result);
            assertEquals(expected[i], String.format("%.5f", result));
        }
    }

    /**
     * @author Xiaoyun
     */
    @Test
    public void testTimeComplexityTourDistance() {
        initLargeInput();

        TSPGraph graph = new TSPGraph();

        // Record the times for each input size
        long[] times = new long[inputFiles.length];
        for (int i = 0; i < inputFiles.length; i++) {
            String filename = inputFiles[i];

            TSPMap map = new TSPMap(filename);
            createValidTour(map);
            long startTime = System.nanoTime();
            graph.tourDistance(map);
            long endTime = System.nanoTime();
            times[i] = endTime - startTime;
        }

        // Compare the time growth to the expected O(n) complexity
        testLinearTimeComplexity(times);
    }

    /**
     * @author Xiaoyun
     */
    @Test
    public void testEarlyTerminationTourDistance() {
        initLargeInput();

        TSPGraph graph = new TSPGraph();

        // Record the times for each input size
        double[] times = new double[inputFiles.length];
        for (int i = 0; i < inputFiles.length; i++) {
            String filename = inputFiles[i];

            TSPMap map = new TSPMap(filename);
            long startTime = System.nanoTime();
            graph.tourDistance(map);
            long endTime = System.nanoTime();
            double timeInSeconds = (endTime - startTime) / 1_000_000_000.0;
            times[i] = timeInSeconds;
            System.out.printf("Input size: %d, Time taken: %.7f seconds%n", inputSizes[i], timeInSeconds);
        }

        testConstantTimeComplexity(times);
    }

    /**
     * No matter how many tests you failed, this test will never fail you.
     * You can do it!
     * But if you are really stuck, try go for a walk, or have a cup of coffee.
     * Or go watch Suzume, it's amazing!
     * -
     * Inspired by the coin flipping scene between Tanjiro and Kanao in
     * Demon Slayer: Kimetsu no Yaiba. (And screw you uncultured spell checker)
     *
     * @author Xiaoyun
     */
    @Test
    public void testYouCanDoIt() {
        Random rnd = new Random();
        double threshold = 0.001;
        int count = 0;

        boolean expected = true;
        boolean result = rnd.nextDouble() < threshold;

        while (result != expected) {
            System.out.printf("You may have failed %d %s.\n", ++count, count == 1 ? "time" : "times");
            threshold *= 3;
            result = rnd.nextDouble() < threshold;
        }

        System.out.println("But you can do it in the end!");
        assertEquals(expected, result); // indeed "The value of result is always true"
    }

    /**
     * Tests if the time taken for the function to run is constant.
     * The time taken for the function to run is expected to be O(1).
     *
     * @param times The time taken for the function to run for each input size
     * @author Xiaoyun
     */
    private void testConstantTimeComplexity(double[] times) {
        // Tolerance value to account for small variations in time measurements
        double tolerance = 1;
        double upperBound = times[0] * (1 + tolerance);

        for (int i = 1; i < times.length; i++)
            assertTrue("Time complexity is not constant", times[i] <= upperBound);
    }

    /**
     * Tests if the time taken for the function to run is linearly proportional to
     * the input size.
     * The time taken for the function to run is expected to be O(n).
     *
     * @param times The time taken for the function to run for each input size
     * @author Xiaoyun
     */
    private void testLinearTimeComplexity(long[] times) {
        for (int i = 1; i < times.length; i++) {
            double previousN = inputSizes[i - 1];
            double currentN = inputSizes[i];
            double previousTime = times[i - 1];
            double currentTime = times[i];

            double expectedTimeGrowth = currentN / previousN;
            double actualTimeGrowth = currentTime / previousTime;

            // You may want to use a tolerance value to account for small variations in time
            // measurements
            double tolerance = 1;
            double upperBound = expectedTimeGrowth * (1 + tolerance);
            System.out.printf("Test %d:\nTime taken: %.7f\nExpected time growth: %f\nActual time " +
                            "growth: " +
                            "%f%n",
                    i, times[i] / 1_000_000_000.0, expectedTimeGrowth, actualTimeGrowth);
            assertTrue("Time complexity is worse than O(n)", actualTimeGrowth <= upperBound);
        }
    }

    /**
     * Tests that the time complexity of the given algorithm is O(n^2 * log n).
     *
     * @param times The times taken for each input size.
     * @author Xiaoyun
     */
    private void testNSquareLogNTimeComplexity(long[] times) {
        for (int i = 1; i < times.length; i++) {
            double previousN = inputSizes[i - 1];
            double currentN = inputSizes[i];
            double previousTime = times[i - 1];
            double currentTime = times[i];

            double expectedTimeGrowth = Math.pow(currentN / previousN, 2) * (Math.log(currentN) / Math.log(previousN));
            double actualTimeGrowth = currentTime / previousTime;

            // You may want to use a tolerance value to account for small variations in time
            // measurements
            double tolerance = 0.25;
            double upperBound = expectedTimeGrowth * (1 + tolerance);
            System.out.printf("Test %d:\nTime taken: %.7f\nExpected time growth: %f\nActual time " +
                            "growth: " +
                            "%f%n",
                    i, times[i] / 1_000_000_000.0, expectedTimeGrowth, actualTimeGrowth);
            assertTrue("Time complexity is worse than O(n^2 * log n)", actualTimeGrowth <= upperBound);
        }
    }
}