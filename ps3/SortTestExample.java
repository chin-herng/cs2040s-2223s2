
/**
 * This class is a simple example for how to use the sorting classes.
 * It sorts three numbers, and measures how long it takes.
 */
public class SortTestExample {
    public static void main(String[] args) {
        // Create three key value pairs
        KeyValuePair[] testArray1 = new KeyValuePair[3];
        testArray1[0] = new KeyValuePair(10, 20);
        testArray1[1] = new KeyValuePair(5, 20);
        testArray1[2] = new KeyValuePair(8, 20);
        KeyValuePair[] testArray2 = new KeyValuePair[3];
        testArray2[0] = new KeyValuePair(10, 20);
        testArray2[1] = new KeyValuePair(5, 20);
        testArray2[2] = new KeyValuePair(8, 20);
        //Create a Sorter
        ISort sortingObject = new SorterA();

        // Do the sorting
        long sortCost1 = sortingObject.sort(testArray1);
        long sortCost2 = sortingObject.sort(testArray2);

        System.out.print(sortCost1 + " " + sortCost2);

//        System.out.println(testArray[0]);
//        System.out.println(testArray[1]);
//        System.out.println(testArray[2]);
//        System.out.println("Sort Cost: " + sortCost);
    }
}
