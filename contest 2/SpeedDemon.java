import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class SpeedDemon {

    /**
     * Returns the number of pairs of entries containing an identical multiset,
     * as described in the pdf.
     *
     * @param fileName name of the file containing the input
     * @return number of pairs
     */
    public int processData(String fileName) {
//        StopWatch sw = new StopWatch();
//        sw.start();
        HashMap<ArrayList<Integer>, Integer> freq2 = new HashMap<>();
        try {
            BufferedReader bufferedDataFile = new BufferedReader(new FileReader(fileName));
//            StopWatch swRead = new StopWatch();
//            swRead.start();
            for (int n = Integer.parseInt(bufferedDataFile.readLine()); n > 0; n--) {
                int i = 0;
                String line = bufferedDataFile.readLine();
                ArrayList<Integer> freq = new ArrayList<>();
                for (; i < 94; i++) {
                    freq.add(0);
                }
                for (i = 0; i < line.length(); i++) {
                    int charIdx = line.charAt(i) - 33;
                    freq.set(charIdx, freq.get(charIdx) + 1);
                }
                freq2.merge(freq, 1, Integer::sum);
            }
//            swRead.stop();
//            System.out.println("Reading took " + swRead.getTime() + " seconds.");
        } catch (Exception e) {
            System.out.println(e);
        }
        int res = 0;
        for (Integer value : freq2.values()) {
            res += (value * (value - 1)) >> 1;
        }
//        sw.stop();
//        System.out.println("Total took " + sw.getTime() + " seconds.");
        return res;
    }

    // DO NOT EDIT this method for contest submission, as it will used by the grader
    public static void main(String[] args){
        SpeedDemon dataProcessor = new SpeedDemon();
        int answer = dataProcessor.processData(args[0]);    // Expects input file name as CLI argument
        System.out.println(answer);
    }
}
