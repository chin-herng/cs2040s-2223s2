import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * This class generates a file with a given number of points.
 * The file is named as the number of points + "points.txt".
 * The first line of the file is the maximum coordinate of the points.
 * The second line of the file is the number of points.
 * The following lines are the points, each line is a point with the format
 * "x,y".
 * The points are generated randomly.
 *
 * @author Xiaoyun
 */
public class testFileGenerator {

    public static void generateNPointsFile(double maxCoordinate, int numberOfPoints) {
        String fileName = numberOfPoints + "points.txt";
        // Check if the file already exists
        File file = new File(fileName);
        if (file.exists()) return;


        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write max coordinate
            bufferedWriter.write(String.valueOf(maxCoordinate));
            bufferedWriter.newLine();

            // Write number of points
            bufferedWriter.write(String.valueOf(numberOfPoints));
            bufferedWriter.newLine();

            Random random = new Random();

            // Generate and write points
            for (int i = 0; i < numberOfPoints; i++) {
                double x = random.nextDouble() * maxCoordinate;
                double y = random.nextDouble() * maxCoordinate;
                bufferedWriter.write(String.format("%.2f,%.2f", x, y));
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing file: " + e);
        }
    }

    public static void main(String[] args) {
        generateNPointsFile(100.0, 1000);
        System.out.println(1000 + "points.txt created.");
    }
}