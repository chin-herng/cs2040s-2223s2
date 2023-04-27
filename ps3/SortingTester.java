import java.util.Arrays;
import java.util.Random;

public class SortingTester {
    // EXAM_COUNT is the number of "exams" in checkSort.
    // TEST_SIZE is the size used in order to test the sorters to determine which is which.
    private static final int EXAM_COUNT = 10;
    private static final int TEST_SIZE = 5000;

    public static boolean checkSort(ISort sorter, int size) {
        // the idea is to randomly generated a fixed number of "exams" to the sorter.
        // the sorter is expected to pass all the "exams".
        // when EXAM_COUNT is large, the algorithm is slow but accurate and vice versa.
        KeyValuePair[] testArray = new KeyValuePair[size];
        Random r = new Random();

        for (int examCount = 0; examCount < EXAM_COUNT; examCount++) {
            for (int i = 0; i < size; i++) {
                testArray[i] = new KeyValuePair(r.nextInt(), 0);
            }
            sorter.sort(testArray);
            for (int i = 1; i < size; i++) {
                if (testArray[i].compareTo(testArray[i - 1]) < 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isStable(ISort sorter, int size) {
        // we use an array of the form [1, 1, ..., 1, 0]
        // an array of the form [0, 0, ..., 0] (all zeros) will not work because
        // selection sort will not do anything to an already-sorted array
        // due to the randomization in quick sort, this method might not be 100% accurate
        // but most of the time, this method returns false for quick sort algorithms
        KeyValuePair[] testArray = new KeyValuePair[size];

        for (int i = 0; i < size - 1; i++) {
            testArray[i] = new KeyValuePair(1, i);
        }
        testArray[size - 1] = new KeyValuePair(0, size - 1);
        sorter.sort(testArray);
        for (int i = 2; i < size; i++) {
            if (testArray[i].getValue() < testArray[i - 1].getValue()) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        ISort sortingObjectA = new SorterA();
        ISort sortingObjectB = new SorterB();
        ISort sortingObjectC = new SorterC();
        ISort sortingObjectD = new SorterD();
        ISort sortingObjectE = new SorterE();
        ISort sortingObjectF = new SorterF();

        System.out.println("Who is Dr. Evil?");
        System.out.print("A sorts ");
        System.out.println(checkSort(sortingObjectA, TEST_SIZE) ? "correctly" : "incorrectly");
        System.out.print("B sorts ");
        System.out.println(checkSort(sortingObjectB, TEST_SIZE) ? "correctly" : "incorrectly");
        System.out.print("C sorts ");
        System.out.println(checkSort(sortingObjectC, TEST_SIZE) ? "correctly" : "incorrectly");
        System.out.print("D sorts ");
        System.out.println(checkSort(sortingObjectD, TEST_SIZE) ? "correctly" : "incorrectly");
        System.out.print("E sorts ");
        System.out.println(checkSort(sortingObjectE, TEST_SIZE) ? "correctly" : "incorrectly");
        System.out.print("F sorts ");
        System.out.println(checkSort(sortingObjectF, TEST_SIZE) ? "correctly" : "incorrectly");
        System.out.println("SorterB is the Dr. Evil!");

        System.out.println();

        System.out.println("Test for stability");
        System.out.print("SorterA is ");
        System.out.print(isStable(sortingObjectA, TEST_SIZE) ? "stable." : "not stable.");
        System.out.println();
        System.out.print("SorterC is ");
        System.out.print(isStable(sortingObjectC, TEST_SIZE) ? "stable." : "not stable.");
        System.out.println();
        System.out.print("SorterD is ");
        System.out.print(isStable(sortingObjectD, TEST_SIZE) ? "stable." : "not stable.");
        System.out.println();
        System.out.print("SorterE is ");
        System.out.print(isStable(sortingObjectE, TEST_SIZE) ? "stable." : "not stable.");
        System.out.println();
        System.out.print("SorterF is ");
        System.out.print(isStable(sortingObjectF, TEST_SIZE) ? "stable." : "not stable.");
        System.out.println();

        System.out.println();

        System.out.print("Check how stable algorithms perform on ascending, descending, ");
        System.out.println("and special (of the form [1, 1, ..., 1, 0]) arrays");
        KeyValuePair[] ascA = new KeyValuePair[TEST_SIZE];
        KeyValuePair[] dscA = new KeyValuePair[TEST_SIZE];
        KeyValuePair[] specialA = new KeyValuePair[TEST_SIZE];
        KeyValuePair[] ascC = new KeyValuePair[TEST_SIZE];
        KeyValuePair[] dscC = new KeyValuePair[TEST_SIZE];
        KeyValuePair[] specialC = new KeyValuePair[TEST_SIZE];
        KeyValuePair[] ascF = new KeyValuePair[TEST_SIZE];
        KeyValuePair[] dscF = new KeyValuePair[TEST_SIZE];
        KeyValuePair[] specialF = new KeyValuePair[TEST_SIZE];
        for (int i = 0; i < TEST_SIZE; i++) {
            ascA[i] = new KeyValuePair(i, 0);
            ascC[i] = new KeyValuePair(i, 0);
            ascF[i] = new KeyValuePair(i, 0);
            dscA[i] = new KeyValuePair(TEST_SIZE - i - 1, 0);
            dscC[i] = new KeyValuePair(TEST_SIZE - i - 1, 0);
            dscF[i] = new KeyValuePair(TEST_SIZE - i - 1, 0);
            specialA[i] = new KeyValuePair((i == TEST_SIZE - 1 ? 0 : 1), 0);
            specialC[i] = new KeyValuePair((i == TEST_SIZE - 1 ? 0 : 1), 0);
            specialF[i] = new KeyValuePair((i == TEST_SIZE - 1 ? 0 : 1), 0);
        }
        System.out.print("Cost of sorting an ascending (sorted) array using SorterA: ");
        System.out.println(sortingObjectA.sort(ascA));
        System.out.print("Cost of sorting a descending array using SorterA: ");
        System.out.println(sortingObjectA.sort(dscA));
        System.out.print("Cost of sorting the special array using SorterA: ");
        System.out.println(sortingObjectA.sort(specialA));
        System.out.print("Cost of sorting an ascending (sorted) array using SorterC: ");
        System.out.println(sortingObjectC.sort(ascC));
        System.out.print("Cost of sorting a descending array using SorterC: ");
        System.out.println(sortingObjectC.sort(dscC));
        System.out.print("Cost of sorting the special array using SorterC: ");
        System.out.println(sortingObjectC.sort(specialC));
        System.out.print("Cost of sorting an ascending (sorted) array using SorterF: ");
        System.out.println(sortingObjectF.sort(ascF));
        System.out.print("Cost of sorting a descending array using SorterF: ");
        System.out.println(sortingObjectF.sort(dscF));
        System.out.print("Cost of sorting the special array using SorterF: ");
        System.out.println(sortingObjectF.sort(specialF));
        System.out.println("SorterA is stable and doesn't have worst or best case => Merge Sort");
        System.out.println("SorterC is stable and slow on special arrays => Bubble Sort");
        System.out.println("SorterF is stable and fast on special arrays => Insertion Sort");

        System.out.println();

        System.out.println("Checking D and E against special arrays of size 3");
        KeyValuePair[] specialD = new KeyValuePair[3];
        KeyValuePair[] specialE = new KeyValuePair[3];
        int failCountD = 0;
        int failCountE = 0;

        for (int testCount = 0; testCount < 10; testCount++) {
            for (int i = 0; i < 3; i++) {
                specialD[i] = new KeyValuePair(i == 2 ? 0 : 1, i);
                specialE[i] = new KeyValuePair(i == 2 ? 0 : 1, i);
            }
            sortingObjectD.sort(specialD);
            sortingObjectE.sort(specialE);
            if (specialD[2].getValue() < specialD[1].getValue()) {
                failCountD++;
            }
            if (specialE[2].getValue() < specialE[1].getValue()) {
                failCountE++;
            }
        }
        System.out.print("SorterD failed to maintain relative positions " + failCountD + "/10");
        System.out.println(" times => Selection Sort");
        System.out.print("SorterE failed to maintain relative positions " + failCountE + "/10");
        System.out.println(" times => Quick Sort");
    }
}
