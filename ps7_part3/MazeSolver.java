import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;

public class MazeSolver implements IMazeSolver {
	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;
	private static final boolean OLD_RULE = false, NEW_RULE = true;
	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getSouthWall,
			Room::getEastWall,
			Room::getWestWall
	);
	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 1, 0 }, // South
			{ 0, 1 }, // East
			{ 0, -1 } // West
	};

	private Maze maze;
	// Represents our grid graph as a 2D-array of nodes.
	// Now we can easily map from coordinates to nodes.
	private Node[][] graph;
	private TreeSet<Node> ts; // TreeSet maintains priority order.
	private boolean rule;

	private class Node implements Comparable<Node> {
		// Encapsulates a room with its dist (estimate for Dijkstra's algorithm).
		int row;
		int col;
		int dist;

		public Node(int row, int col, int dist) {
			this.row = row;
			this.col = col;
			this.dist = dist;
		}

		@Override
		public int compareTo(Node n) {
			if (this.dist == n.dist) {
				// This part ensures uniqueness of the entries in TreeSet.
				return this.row == n.row ? this.col - n.col : this.row - n.row;
			}
			return this.dist - n.dist;
		}

		public void relax() {
			// Relaxes all outgoing edges. In an undirected graph, all edges are outgoing (and incoming).
			for (int direction = 0; direction < 4; direction++) {
				int nextRow = this.row + DELTAS[direction][0];
				int nextCol = this.col + DELTAS[direction][1];
				if (!MazeSolver.this.outOfMaze(nextRow, nextCol)) {
					Node w = MazeSolver.this.graph[nextRow][nextCol];
					int estimate = MazeSolver.this.computeEstimate(this.row, this.col, this.dist, direction);
					if (w.dist > estimate) {
						// remove(w) does nothing if w is not in the TreeSet.
						MazeSolver.this.ts.remove(w);
						w.dist = estimate;
						MazeSolver.this.ts.add(w);
					}
				}
			}
		}
	}

	public MazeSolver() {
		// Unused constructor.
	}

	@Override
	public void initialize(Maze maze) {
		int rows = maze.getRows();
		int cols = maze.getColumns();
		this.maze = maze;
		this.graph = new Node[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				this.graph[i][j] = new Node(i, j, Integer.MAX_VALUE);
			}
		}
		this.ts = new TreeSet<>();
	}

	private int computeEstimate(int row, int col, int dist, int direction) {
		// An obstacle is a thing between two rooms (a Wall Spirit, a True Wall or an empty space).
		int obstacle = WALL_FUNCTIONS.get(direction).apply(this.maze.getRoom(row, col));
		if (obstacle == EMPTY_SPACE) {
			return dist + 1;
		}
		if (obstacle == TRUE_WALL) {
			// Avoid overflows by not adding dist to obstacle directly.
			return Integer.MAX_VALUE;
		}
		return this.rule == NEW_RULE ? Math.max(dist, obstacle) : dist + obstacle;
	}

	private Integer dijkstra(int startRow, int startCol, int endRow, int endCol, int startDist) {
		Node start = this.graph[startRow][startCol];
		this.ts.add(start);
		start.dist = startDist;
		while (!this.ts.isEmpty()) {
			Node w = this.ts.pollFirst(); // Why is it called poll?
			if (w == this.graph[endRow][endCol]) {
				// Unlike 7.1 and 7.2, we can terminate early as we only care about the destination.
				return w.dist;
			}
			w.relax();
		}

		// No path is found.
		return null;
	}

	private boolean outOfMaze(int row, int col) {
		return row < 0 || col < 0 || row >= this.maze.getRows() || col >= this.maze.getColumns();
	}

	private void validateAndReset(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (this.maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (this.outOfMaze(startRow, startCol) || this.outOfMaze(endRow, endCol)) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		this.initialize(this.maze);
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// Code compartmentalisation at its finest.
		this.validateAndReset(startRow, startCol, endRow, endCol);
		this.rule = OLD_RULE;

		return this.dijkstra(startRow, startCol, endRow, endCol, 0);
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		this.validateAndReset(startRow, startCol, endRow, endCol);
		this.rule = NEW_RULE;

		return this.dijkstra(startRow, startCol, endRow, endCol, 0);
	}

	private void DFSVisit(boolean[][] visited, int curRow, int curCol) {
		for (int direction = 0; direction < 4; direction++) {
			int nextRow = curRow + DELTAS[direction][0];
			int nextCol = curCol + DELTAS[direction][1];
			int obstacle = WALL_FUNCTIONS.get(direction).apply(MazeSolver.this.maze.getRoom(curRow, curCol));
			if (obstacle != TRUE_WALL && !visited[nextRow][nextCol]) {
				visited[nextRow][nextCol] = true;
				this.DFSVisit(visited, nextRow, nextCol);
			}
		}
	}

	private boolean canReach(int startRow, int startCol, int endRow, int endCol) {
		boolean[][] visited = new boolean[this.maze.getRows()][this.maze.getColumns()];
		visited[startRow][startCol] = true;
		this.DFSVisit(visited, startRow, startCol);
		return visited[endRow][endCol];
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		this.validateAndReset(startRow, startCol, endRow, endCol);
		this.rule = NEW_RULE;

		// We first ignore the special room.
		// This captures the case where the shortest path does not go through the special room.
		Integer ignoreSpecial = this.dijkstra(startRow, startCol, endRow, endCol, 0);
		if (ignoreSpecial != null && this.canReach(startRow, startCol, sRow, sCol)) {
			// If we can reach the special room, the path we take to reach it does not matter.
			// We look at the shortest path from the special room to the destination.
			Integer specialToEnd = this.dijkstra(sRow, sCol, endRow, endCol, -1);
			return specialToEnd == null ? ignoreSpecial : Math.min(ignoreSpecial, specialToEnd);
		}
		// If we can never reach the destination, the answer is null = ignoreSpecial.
		// If we cannot reach the special room, the answer is still ignoreSpecial.
		return ignoreSpecial;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("haunted-maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);
			for (int i = 0; i < maze.getRows(); i++) {
				for (int j = 0; j < maze.getColumns(); j++) {
					System.out.print(solver.pathSearch(0, 0, i, j) + " ");
				}
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
