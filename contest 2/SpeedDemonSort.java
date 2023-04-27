import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class SpeedDemonSort {

    static class Reader {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public Reader(String file_name) throws IOException {
            din = new DataInputStream(
                    new FileInputStream(file_name));
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public String readLine() throws IOException {
            byte[] buf = new byte[5000];
            int cnt = 0, c;
            while ((c = read()) != -1) {
                if (c == '\n') {
                    if (cnt != 0) {
                        break;
                    } else {
                        continue;
                    }
                }
                buf[cnt++] = (byte) c;
            }
            return new String(buf, 0, cnt);
        }

        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ') {
                c = read();
            }
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (neg)
                return -ret;
            return ret;
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0,
                    BUFFER_SIZE);
            if (bytesRead == -1)
                buffer[0] = -1;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead)
                fillBuffer();
            return buffer[bufferPointer++];
        }
    }

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
        HashMap<String, Integer> freq = new HashMap<>();
        try {
            Reader s = new Reader(fileName);
//            StopWatch swRead = new StopWatch();
//            swRead.start();
            for (int n = s.nextInt(); n > 0; n--) {
                char[] line = s.readLine().toCharArray();
                Arrays.sort(line);
                freq.merge(new String(line), 1, Integer::sum);
            }
//            swRead.stop();
//            System.out.println("Reading took " + swRead.getTime() + " seconds.");
        } catch (Exception e) {
            System.out.println(e);
        }
        int res = 0;
        for (Integer value : freq.values()) {
            res += (value * (value - 1)) >> 1;
        }
//        sw.stop();
//        System.out.println("Total took " + sw.getTime() + " seconds.");
        return res;
    }

    // DO NOT EDIT this method for contest submission, as it will used by the grader
    public static void main(String[] args){
        SpeedDemonSort dataProcessor = new SpeedDemonSort();
        int answer = dataProcessor.processData(args[0]);    // Expects input file name as CLI argument
        System.out.println(answer);
    }
}
