import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MazeGenerator {
	private static final int OLDEST = 0, MIDDLE = 1, NEWEST = 2, RANDOM = 3;
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;

	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 1, 0 }, // South
			{ 0, 1 }, // East
			{ 0, -1 } // West
	};

	private static Room[] carve(Room firstRoom, Room secondRoom, int dir) {
		boolean[] hasWall = new boolean[] {
			firstRoom.hasNorthWall(),
			firstRoom.hasSouthWall(),
			firstRoom.hasEastWall(),
			firstRoom.hasWestWall(),
			secondRoom.hasNorthWall(),
			secondRoom.hasSouthWall(),
			secondRoom.hasEastWall(),
			secondRoom.hasWestWall()
		};

		switch (dir) {
			case NORTH:
				return new Room[] {
					new Room(false, hasWall[1], hasWall[2], hasWall[3]),
					new Room(hasWall[4], false, hasWall[6], hasWall[7])
				};
			case SOUTH:
				return new Room[] {
						new Room(hasWall[0], false, hasWall[2], hasWall[3]),
						new Room(false, hasWall[5], hasWall[6], hasWall[7])
				};
			case EAST:
				return new Room[] {
						new Room(hasWall[0], hasWall[1], false, hasWall[3]),
						new Room(hasWall[4], hasWall[5], hasWall[6], false)
				};
			case WEST:
				return new Room[] {
						new Room(hasWall[0], hasWall[1], hasWall[2], false),
						new Room(hasWall[4], hasWall[5], false, hasWall[7])
				};
			default:
				throw new IllegalArgumentException();
		}
	}

	private static int chooseIndex(int n, int cellSelection) {
		switch (cellSelection) {
			case OLDEST:
				return 0;
			case MIDDLE:
				return n / 2;
			case NEWEST:
				return n - 1;
			case RANDOM:
				return new Random().nextInt(n);
			default:
				throw new IllegalArgumentException();
		}
	}

	public static Maze generateMaze(int rows, int columns, int cellSelection) {
		return MazeGenerator.generateMaze(rows, columns, 0, cellSelection);
	}

	public static Maze generateMaze(int rows, int columns, int seed, int cellSelection) {
		Random random = new Random(seed);
		Room[][] rooms = new Room[rows][columns];
		ArrayList<int[]> cells = new ArrayList<>();
		boolean[][] isInCells = new boolean[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				rooms[i][j] = new Room(true, true, true, true);
			}
		}

		// Selects a random cell and adds it to the list.
		int startX = random.nextInt(rows);
		int startY = random.nextInt(columns);
		isInCells[startX][startY] = true;
		cells.add(new int[] { startX, startY });

		while (!cells.isEmpty()) {
			// Selects the cell to operate on.
			int index = MazeGenerator.chooseIndex(cells.size(), cellSelection);
			int x = cells.get(index)[0];
			int y = cells.get(index)[1];

			// Looks for an unvisited neighbor.
			List<Integer> intList = Arrays.asList(NORTH, SOUTH, EAST, WEST);
			Collections.shuffle(intList);
			for (Integer direction : intList) {
				int newX = x + DELTAS[direction][0];
				int newY = y + DELTAS[direction][1];
				if (newX >= 0 && newY >= 0 && newX < rows && newY < columns && !isInCells[newX][newY]) {
					// Carves a passage between the current cell and that neighbor.
					Room[] carvedRooms = MazeGenerator.carve(rooms[x][y], rooms[newX][newY], direction);
					rooms[x][y] = carvedRooms[0];
					rooms[newX][newY] = carvedRooms[1];

					// Adds the neighbor to the list.
					isInCells[newX][newY] = true;
					cells.add(new int[] { newX, newY });

					// Indicates that an unvisited neighbor was found.
					index = -1;
					break;
				}
			}
			// If no such neighbor is found, deletes the given cell from the list before continuing.
			if (index > -1) {
				cells.remove(index);
			}
		}

		return new Maze(rooms);
	}

	public static void main(String[] args) {
		Maze maze = MazeGenerator.generateMaze(10, 38, RANDOM);
		MazeSolver solver = new MazeSolver();
		solver.initialize(maze);
		try {
			solver.pathSearch(0, 0, 9, 37);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ImprovedMazePrinter.printMaze(maze, 0, 0);
	}
}
