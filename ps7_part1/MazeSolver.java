import java.util.ArrayList;
import java.util.HashMap;

public class MazeSolver implements IMazeSolver {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static final int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	public Maze maze;
	private boolean[][] visited;
	private int[][][] parent; // parent[i][j] = [x, y] means that the parent of (i, j) is (x, y).
	private HashMap<Integer, Integer> hopFreq; // answers numReachable in expected O(1) time.

	public MazeSolver() {
		// I actually did not use this constructor...
	}

	@Override
	public void initialize(Maze maze) {
		int rows = maze.getRows();
		int columns = maze.getColumns();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				maze.getRoom(i, j).onPath = false;
			}
		}
		this.maze = maze;
		this.visited = new boolean[rows][columns];
		this.parent = new int[rows][columns][2];
		this.hopFreq = new HashMap<>();
	}

	private boolean canGo(int row, int col, int dir) {
		switch (dir) {
			case NORTH:
				return !this.maze.getRoom(row, col).hasNorthWall();
			case SOUTH:
				return !this.maze.getRoom(row, col).hasSouthWall();
			case EAST:
				return !this.maze.getRoom(row, col).hasEastWall();
			case WEST:
				return !this.maze.getRoom(row, col).hasWestWall();
			default:
				return false;
		}
	}

	private void drawPath(int startRow, int startCol, int curRow, int curCol) {
		this.maze.getRoom(curRow, curCol).onPath = true;
		if (startRow != curRow || startCol != curCol) {
			this.drawPath(startRow, startCol, this.parent[curRow][curCol][0], this.parent[curRow][curCol][1]);
		}
	}

	private Integer BFS(int startRow, int startCol, int endRow, int endCol) {
		Integer res = null;

		// Initialise for BFS.
		ArrayList<int[]> frontier = new ArrayList<>();
		frontier.add(new int[] { startRow, startCol });
		this.visited[startRow][startCol] = true;

		for (int hops = 0; !frontier.isEmpty(); hops++) {
			// Computes hopFreq for O(1) numReachable later on.
			this.hopFreq.put(hops, frontier.size());

			ArrayList<int[]> nextFrontier = new ArrayList<>();
			for (int[] v : frontier) {
				int row = v[0];
				int col = v[1];

				if (row == endRow && col == endCol) {
					// We reached the destination, update res to be returned later and draw the path.
					// Meanwhile, we continue the BFS to finish the computation of hopFreq.
					res = hops;
					this.drawPath(startRow, startCol, endRow, endCol);
				}

				for (int direction = 0; direction < 4; direction++) {
					int newRow = row + DELTAS[direction][0];
					int newCol = col + DELTAS[direction][1];
					if (this.canGo(row, col, direction) && !this.visited[newRow][newCol]) {
						this.visited[newRow][newCol] = true;
						this.parent[newRow][newCol][0] = row;
						this.parent[newRow][newCol][1] = col;
						nextFrontier.add(new int[]{newRow, newCol});
					}
				}
			}
			frontier = nextFrontier;
		}

		// res is automatically null when the BFS didn't reach our destination.
		return res;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		// Resets everything with the same maze.
		this.initialize(this.maze);

		return this.BFS(startRow, startCol, endRow, endCol);
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		// No exceptions need to be thrown.
		return this.hopFreq.getOrDefault(k, 0);
	}

	public static void main(String[] args) {
		// Do remember to remove any references to ImprovedMazePrinter before submitting
		// your code!
		try {
			Maze maze = Maze.readMaze("maze-empty.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			for (int i = 0; i < maze.getRows(); i++) {
				for (int j = 0; j < maze.getColumns(); j++) {
					for (int k = 0; k < maze.getRows(); k++) {
						for (int l = 0; l < maze.getColumns(); l++) {
							System.out.println(solver.pathSearch(i, j, k, l));
							ImprovedMazePrinter.printMaze(maze, i, j);
							for (int m = 0; m <= 9; ++m) {
								System.out.println("Steps " + m + " Rooms: " + solver.numReachable(m));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
