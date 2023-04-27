import java.util.Set;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        int[] seed = { 0, 0, 1, 1, 1, 0, 1, 0, 0 };
        for (int tap = 0; tap < 9; tap++) {
            ShiftRegister sr = new ShiftRegister(9, tap);
            sr.setSeed(seed);
            Set<String> st = new HashSet<>();
            System.out.print(tap + " ");
            int i = 0;
            String cur = sr.toString();
            while (!st.contains(cur)) {
                st.add(cur);
                sr.shift();
                cur = sr.toString();
                i++;
            }
            System.out.println(i);
        }
    }
}
