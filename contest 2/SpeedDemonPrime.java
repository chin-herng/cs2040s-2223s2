import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class SpeedDemonPrime {
    int[] primes = {139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709};
    class Reader {
        private final DataInputStream din;
        private final byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader(String file_name) throws IOException {
            din = new DataInputStream(
                    new FileInputStream(file_name));
            buffer = new byte[1 << 16];
        }

        public long readLine() throws IOException {
            long key = 1;
            byte c;
            while ((c = read()) != '\n') {
                key *= primes[c - 33];
            }
            return key;
        }

        public int nextInt() throws IOException {
            int ret = 0;
            byte c;
            while ((c = read()) != '\n') {
                ret = ret * 10 + c - '0';
            }
            return ret;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead) {
                bytesRead = din.read(buffer, bufferPointer = 0, 1 << 16);
            }
            return buffer[bufferPointer++];
        }
    }

    public int processData(String fileName) {
        HashMap<Long, Integer> freq = new HashMap<>();
        int res = 0;
        try {
            Reader s = new Reader(fileName);
            for (int n = s.nextInt(); n > 0; n--) {
                long key = s.readLine();
                res += freq.getOrDefault(key, 0);
                freq.merge(key, 1, Integer::sum);
            }
        } catch (Exception e) {
            System.out.println();
        }
        return res;
    }

    public static void main(String[] args){
        SpeedDemonPrime dataProcessor = new SpeedDemonPrime();
        int answer = dataProcessor.processData(args[0]);
        System.out.println(answer);
    }
}
