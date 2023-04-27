import java.util.ArrayList;
import java.util.Arrays;

public class BellmanFord {
    // DO NOT MODIFY THE TWO STATIC VARIABLES BELOW
    public static int INF = 20000000;
    public static int NEGINF = -20000000;

    int n; // Number of nodes.
    int[] dist;
    // The graph is represented by an edge list.
    // graph[i] = { u, v, w } means the ith edge of the graph
    // connects from u to v with weight w.
    int[][] graph;

    public BellmanFord(ArrayList<ArrayList<IntPair>> adjList) {
        if (adjList == null) {
            throw new IllegalArgumentException("adjList cannot be null!");
        }

        this.dist = new int[adjList.size()];
        this.n = adjList.size();

        int e = 0;
        for (ArrayList<IntPair> x : adjList) {
            e += x.size();
        }
        this.graph = new int[e][3];
        int j = 0;
        for (int i = 0; i < n; i++) {
            for (IntPair x : adjList.get(i)) {
                this.graph[j] = new int[] { i, x.first, x.second };
                j++;
            }
        }

        this.resetDist();
    }

    private boolean relax(int u, int v, int w) {
        // Returns true if estimate for v has changed, and false otherwise.
        if (this.dist[u] == NEGINF) {
            this.dist[v] = NEGINF;
            return true;
        }
        if (this.dist[u] != INF && this.dist[v] > this.dist[u] + w) {
            this.dist[v] = this.dist[u] + w;
            return true;
        }
        return false;
    }

    private void resetDist() {
        Arrays.fill(dist, INF);
    }

    public void computeShortestPaths(int source) {
        if (source < 0 || source >= n) {
            Arrays.fill(this.dist, INF);
            return;
        }

        this.resetDist();

        this.dist[source] = 0;

        for (int i = 0; i <= 2 * n; i++) {
            boolean noChange = true;
            for (int[] e : graph) {
                boolean changed = this.relax(e[0], e[1], e[2]);
                noChange &= !changed;
                if (changed && i >= n - 1) {
                    // Negative weight cycles.
                    this.dist[e[1]] = NEGINF;
                }
            }
//            for (int x : dist) {
//                System.out.print(x + " ");
//            }
//            System.out.println();
            if (noChange) {
                // "Converges".
                return;
            }
        }
    }

    public int getDistance(int node) {
        if (node >= n || node < 0) {
            return INF;
        }

        return this.dist[node];
    }

//    public static void main(String[] args) {
//        ArrayList<ArrayList<IntPair>> adjList = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            adjList.add(new ArrayList<>());
//        }
//        adjList.get(0).add(new IntPair(1, 3));
//        adjList.get(0).add(new IntPair(2, 5));
//        adjList.get(1).add(new IntPair(3, -4));
//        adjList.get(2).add(new IntPair(1, -1));
//        adjList.get(3).add(new IntPair(2, 2));
//        BellmanFord bf = new BellmanFord(adjList);
//        bf.computeShortestPaths(0);
//        System.out.println(bf.getDistance(2));
////        for (int source = 0; source < 4; source++) {
////            bf.computeShortestPaths(source);
////            for (int i = 0; i < 4; i++) {
////                System.out.print(bf.getDistance(i) + " ");
////            }
////            System.out.println();
////        }
//    }
}
