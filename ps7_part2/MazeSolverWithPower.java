import java.util.ArrayList;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 1, 0 }, // South
			{ 0, 1 }, // East
			{ 0, -1 } // West
	};

	public Maze maze;
	private int rows;
	private int columns;
	private int[] shortestFreq;

	public MazeSolverWithPower() {
		// Unused constructor.
	}

	@Override
	public void initialize(Maze maze) {
		if (maze == null) {
			throw new IllegalArgumentException("bruh you're initialising a null maze");
		}
		this.rows = maze.getRows();
		this.columns = maze.getColumns();
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				maze.getRoom(i, j).onPath = false;
			}
		}
		this.maze = maze;
		this.shortestFreq = new int[this.rows * this.columns];
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

	private void drawPath(int[][][][] parent, int startRow, int startCol, int curRow, int curCol, int curSup) {
		this.maze.getRoom(curRow, curCol).onPath = true;
		if (startRow != curRow || startCol != curCol) {
			int nextRow = parent[curRow][curCol][curSup][0];
			int nextCol = parent[curRow][curCol][curSup][1];
			int nextSup = parent[curRow][curCol][curSup][2];
			this.drawPath(parent, startRow, startCol, nextRow, nextCol, nextSup);
		}
	}

	private Integer BFS(int startRow, int startCol, int endRow, int endCol, int superpowers) {
		Integer res = null;

		// Initialise for BFS.
		// Shortest path is recorded only when a room is visited for the first time,
		// therefore we need to remember when a room has been visited.
		boolean[][] visitedRoom = new boolean[this.rows][this.columns];
		// Unfortunately, our state is no longer just rooms,
		// therefore we need a separate array to remember when a state has been visited.
		boolean[][][] visited = new boolean[this.rows][this.columns][superpowers + 1];
		// parent[a][b][c] = { x, y, z } means the state representing room (a, b) and superpower c
		// has parent state representing room (x, y) and superpower z.
		int[][][][] parent = new int[this.rows][this.columns][superpowers + 1][3];
		ArrayList<int[]> frontier = new ArrayList<>();
		frontier.add(new int[] { startRow, startCol, superpowers });
		visited[startRow][startCol][superpowers] = true;

		for (int hops = 0; !frontier.isEmpty(); hops++) {
			ArrayList<int[]> nextFrontier = new ArrayList<>();
			int unvisitedCount = 0; // Number of unvisited rooms in the current frontier.

			for (int[] v : frontier) {
				int row = v[0];
				int col = v[1];
				int sup = v[2];

				if (!visitedRoom[row][col]) {
					// We found the shortest path to (row, col).
					unvisitedCount++;
					if (row == endRow && col == endCol) {
						// We found the shortest path to (endRow, endCol).
						res = hops;
						this.drawPath(parent, startRow, startCol, row, col, sup);
					}
					visitedRoom[row][col] = true;
				}

				for (int direction = 0; direction < 4; direction++) {
					int newRow = row + DELTAS[direction][0];
					int newCol = col + DELTAS[direction][1];
					boolean canGo = this.canGo(row, col, direction);
					boolean isNotBorder = newRow >= 0 && newCol >= 0 && newRow < this.rows && newCol < this.columns;

					if (canGo && !visited[newRow][newCol][sup]) {
						// No walls to break.
						visited[newRow][newCol][sup] = true;
						nextFrontier.add(new int[] { newRow, newCol, sup });
						parent[newRow][newCol][sup] = new int[] { row, col, sup };
					} else if (!canGo && sup > 0 && isNotBorder && !visited[newRow][newCol][sup - 1]) {
						// There is a wall to break.
						visited[newRow][newCol][sup - 1] = true;
						nextFrontier.add(new int[] { newRow, newCol, sup - 1 });
						parent[newRow][newCol][sup - 1] = new int[] { row, col, sup };
					}
				}
			}
			this.shortestFreq[hops] = unvisitedCount;
			frontier = nextFrontier;
		}

		// res is automatically null when the BFS didn't reach our destination.
		return res;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		return this.pathSearch(startRow, startCol, endRow, endCol, 0);
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		// No exceptions need to be thrown.
		return k >= this.shortestFreq.length || k < 0 ? 0 : this.shortestFreq[k];
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int superpowers) throws Exception {
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		// Resets everything with the same maze.
		this.initialize(this.maze);

		return this.BFS(startRow, startCol, endRow, endCol, superpowers);
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);
			solver.pathSearch(0, 0, 0, 4, 2);

			for (int k = 0; k <= 10; ++k) {
				System.out.println("Steps " + k + " Rooms: " + solver.numReachable(k));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
