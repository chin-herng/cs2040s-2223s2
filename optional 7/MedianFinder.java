import java.util.Comparator;
import java.util.TreeSet;

public class MedianFinder {

    int id = 0;
    Comparator<int[]> comparator = (x, y) -> x[0] == y[0] ? x[1] - y[1] : x[0] - y[0];
    TreeSet<int[]> left = new TreeSet<>(comparator);
    TreeSet<int[]> right = new TreeSet<>(comparator);

    public MedianFinder() {

    }

    public void insert(int x) {
        if (!right.isEmpty() && x < right.first()[0]) {
            left.add(new int[] {x, id});
        } else {
            right.add(new int[] {x, id});
        }

        if (right.size() == left.size() + 2) {
            left.add(right.pollFirst());
        } else if (left.size() == right.size() + 1) {
            right.add(left.pollLast());
        }

        id++;
    }

    public int getMedian() {
        int res = right.pollFirst()[0];

        if (left.size() == right.size() + 1) {
            right.add(left.pollLast());
        }

        return res;
    }
}
