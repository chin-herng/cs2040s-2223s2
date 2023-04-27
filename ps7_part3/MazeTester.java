import static org.junit.Assert.*;
import org.junit.Test;

public class MazeTester {
    @Test
    public void testPathSearch1() {
        try {
            Maze maze = Maze.readMaze("maze-empty.txt");
            IMazeSolver solver = new MazeSolver();
            solver.initialize(maze);
            int expected = 6;
            int answer = solver.pathSearch(0, 0, 3, 3);
            assertEquals(expected, answer);

        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("no exceptions", "exceptions");
        }
    }

    @Test
    public void testPathSearch2() {
        try {
            Maze maze = Maze.readMaze("maze-dense.txt");
            IMazeSolver solver = new MazeSolver();
            solver.initialize(maze);
            Integer expected = null;
            Integer answer = solver.pathSearch(0, 0, 3, 3);
            assertEquals(expected, answer);

        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("no exceptions", "exceptions");
        }
    }

    @Test
    public void testPathSearch3() {
        try {
            Maze maze = Maze.readMaze("maze-dense.txt");
            IMazeSolver solver = new MazeSolver();
            solver.initialize(maze);
            Integer expected = 0;
            Integer answer = solver.pathSearch(0, 0, 0, 0);
            assertEquals(expected, answer);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("no exceptions", "exceptions");
        }
    }

    @Test
    public void testPathSearch4() {
        try {
            Maze maze = Maze.readMaze("haunted-maze-simple.txt");
            IMazeSolver solver = new MazeSolver();
            solver.initialize(maze);
            Integer expected = (int) 'ü' + (int) 'W' + (int) 'û' + 1;
            Integer answer = solver.pathSearch(0, 0, 0, 4);
            assertEquals(expected, answer);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("no exceptions", "exceptions");
        }
    }

    @Test
    public void testPathSearch5() {
        try {
            Maze maze = Maze.readMaze("haunted-maze-sample.txt");
            IMazeSolver solver = new MazeSolver();
            solver.initialize(maze);
            Integer expected = 314;
            Integer answer = solver.pathSearch(0, 1, 0, 5);
            assertEquals(expected, answer);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("no exceptions", "exceptions");
        }
    }

    @Test
    public void testPathSearch6() {
        try {
            Maze maze = Maze.readMaze("test1.txt");
            MazeSolver solver = new MazeSolver();
            solver.initialize(maze);
            Integer expected = (int) '!' * 5 + 2;
            Integer answer = solver.pathSearch(0, 0, 0, 5);
            assertEquals(expected, answer);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("no exceptions", "exceptions");
        }
    }
    @Test
    public void testPathSearch7() {
        try {
            Maze maze = Maze.readMaze("test2.txt");
            MazeSolver solver = new MazeSolver();
            solver.initialize(maze);
            Integer expected = (int) '!' * 7 + 1;
            Integer answer = solver.pathSearch(0, 0, 3, 5);
            assertEquals(expected, answer);
        } catch (Exception e) {
            e.printStackTrace();
                assertEquals("no exceptions", "exceptions");
        }
    }

    @Test
    public void testPathSearch8() {
        try {
            Maze maze = Maze.readMaze("haunted-maze-sample.txt");
            IMazeSolver solver = new MazeSolver();
            solver.initialize(maze);
            Integer expected = 314;
            Integer answer = solver.pathSearch(0, 1, 1, 4);
            assertEquals(expected, answer);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("no exceptions", "exceptions");
        }
    }

    @Test
    public void testPathSearch9() {
        try {
            Maze maze = Maze.readMaze("haunted-maze-sample.txt");
            IMazeSolver solver = new MazeSolver();
            solver.initialize(maze);
            Integer expected = 314;
            Integer answer = solver.pathSearch(1, 4, 0, 1);
            assertEquals(expected, answer);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("no exceptions", "exceptions");
        }
    }

    @Test
    public void testPathSearch10() {
        try {
            Maze maze = Maze.readMaze("haunted-maze-sample.txt");
            IMazeSolver solver = new MazeSolver();
            solver.initialize(maze);
            Integer expected = 309;
            Integer answer = solver.pathSearch(1, 3, 0, 2);
            assertEquals(expected, answer);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("no exceptions", "exceptions");
        }
    }

    @Test
    public void testPathSearch11() {
        try {
            Maze maze = Maze.readMaze("haunted-maze-sample.txt");
            IMazeSolver solver = new MazeSolver();
            solver.initialize(maze);
            Integer expected = 303;
            Integer answer = solver.pathSearch(1, 2, 0, 3);
            assertEquals(expected, answer);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("no exceptions", "exceptions");
        }
    }

    @Test
    public void testPathSearch12() {
        try {
            Maze maze = Maze.readMaze("haunted-maze-sample.txt");
            IMazeSolver solver = new MazeSolver();
            solver.initialize(maze);
            Integer expected = 314;
            Integer answer = solver.pathSearch(0, 4, 1, 1);
            assertEquals(expected, answer);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("no exceptions", "exceptions");
        }
    }
}