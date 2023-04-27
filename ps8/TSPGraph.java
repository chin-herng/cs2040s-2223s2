import java.util.ArrayList;

public class TSPGraph implements IApproximateTSP {
    // children.get(i) is the list of children of node i.
    ArrayList<ArrayList<Integer>> children = new ArrayList<>();

    private void checkNullMap(TSPMap map) {
        if (map == null) {
            throw new IllegalArgumentException("map cannot be null");
        }
        // Assuming that if map is not null, it will be correctly initialised
        // (it actually may not be initialised properly, if the file provided is not found).
    }

    @Override
    public void MST(TSPMap map) {
        this.checkNullMap(map);

        int start = 0; // The starting node for Prim's algorithm.
        int pointCount = map.getCount();
        TreeMapPriorityQueue<Double, Integer> pq = new TreeMapPriorityQueue<>();
        // Could have also reset children only in a TSP call, but this "makes more sense" I guess.
        children = new ArrayList<>();

        // Initialise priority queue.
        for (int i = 0; i < pointCount; i++) {
            pq.add(i, Double.MAX_VALUE);
            this.children.add(new ArrayList<>());
        }
        pq.decreasePriority(start, 0.0);

        while (!pq.isEmpty()) {
            Integer v = pq.extractMin();

            if (v != start) {
                // Update the children list of v's parent
                this.children.get(map.getLink(v)).add(v);
            }

            // In a clique, every other node is adjacent to every other node.
            for (int w = 0; w < pointCount; w++) {
                double weight = map.pointDistance(v, w);

                // Checking for w != v can reduce one function call of lookup().
                if (w != v && pq.lookup(w) != null && pq.lookup(w) > weight) {
                    pq.decreasePriority(w, weight);

                    // setLink essentially records the parent of w.
                    map.setLink(w, v, false);
                }
            }
        }
        map.redraw();
    }

    private int DFS(TSPMap map, int current, int lastVisited) {
        if (current != 0) {
            map.setLink(lastVisited, current, false);
        }

        lastVisited = current;
        for (Integer v : this.children.get(current)) {
            lastVisited = this.DFS(map, v, lastVisited);
        }

        return lastVisited; // Returns the last newly visited node (first encounter) from the current call.
    }

    @Override
    public void TSP(TSPMap map) {
        this.checkNullMap(map);

        MST(map);
        map.setLink(this.DFS(map, 0, -1), 0);
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        this.checkNullMap(map);

        int current = 0; // We start at node 0.
        boolean[] visited = new boolean[map.getCount()];

        // Follow the links until there are no more links or a cycle is detected.
        while (current != -1 && !visited[current]) {
            visited[current] = true;
            current = map.getLink(current);
        }

        // Make sure that all cities are visited.
        for (boolean visitedCity : visited) {
            if (!visitedCity) {
                return false;
            }
        }

        // Make sure that the path ends back at node 0.
        return current == 0;
    }

    @Override
    public double tourDistance(TSPMap map) {
        this.checkNullMap(map);

        if (!isValidTour(map)) {
            return -1;
        }

        double res = 0;
        int current = 0; // We start at node 0.
        boolean started = false; // Whether the tracing has started.

        while (!started || current != 0) {
            if (current == 0) {
                // The first time this happens, the tracing has not yet started.
                started = true;
            }
            int next = map.getLink(current);
            res += map.pointDistance(current, next);
            current = next;
        }

        return res;
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "fiftypoints.txt");
        TSPGraph graph = new TSPGraph();

        graph.MST(map);
//         graph.TSP(map);
//         System.out.println(graph.isValidTour(map));
        // System.out.println(graph.tourDistance(map));
    }

//    public static void main(String[] args) {
//        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "./tenpoints.txt");
////       map.setLink(0, 1);
////       map.setLink(1, 2);
////       map.setLink(2, 3);
////       map.setLink(3, 4);
////       map.setLink(4, 0);
////       map.setLink(5, 6);
////       map.setLink(6, 7);
////       map.setLink(7, 8);
////       map.setLink(8, 9);
////       map.setLink(9, 5);
//
//       map.setLink(0, 1);
//       map.setLink(1, 2);
//       map.setLink(2, 3);
//       map.setLink(3, 4);
//       map.setLink(4, 5);
//       map.setLink(5, 6);
//       map.setLink(6, 7);
//       map.setLink(7, 8);
//       map.setLink(8, 9);
//       map.eraseLink(9);
//
////        map.setLink(0, 1);
////        map.setLink(1, 2);
////        map.setLink(2, 3);
////        map.setLink(3, 4);
////        map.setLink(4, 5);
////        map.setLink(5, 6);
////        map.setLink(6, 7);
////        map.setLink(7, 8);
////        map.setLink(8, 9);
////        map.setLink(9, 1);
//        TSPGraph graph = new TSPGraph();
//        // graph.MST(map);
//        // graph.MST(map);
//        System.out.println(graph.isValidTour(map));
//        // System.out.println(graph.tourDistance(map));
//    }
}
