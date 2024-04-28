/**
 * Game Tree class
 *
 * This class contains the basic code for implementing a Game Tree. It includes functionality to load a game tree.
 */

public class GameTree {

    // Standard enumerations for tic-tac-toe variations
    public enum Player {ONE, TWO}

    public enum Game {Regular, Misere, NoTie, Arbitrary}

    // Some constants for tic-tac-toe
    final static int bsize = 3;
    final static int btotal = bsize * bsize;
    final static char EMPTYCHAR = '_';

    // Specifies the values for the arbitrary variant
    final int[] valArray = {1, -2, 1, -3, -2, 2, -2, -1, 1};

    /**
     * Simple TreeNode class.
     *
     * This class holds the data for a node in a game tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    static class TreeNode {
        public String name = "";
        public TreeNode[] children = null;
        public int numChildren = 0;
        public int value = Integer.MIN_VALUE;
        public boolean leaf = false;

        // Empty constructor for building an empty node
        TreeNode() {}

        // Simple constructor for build a node with a name and a leaf specification
        TreeNode(String s, boolean l) {
            name = s;
            leaf = l;
            children = new TreeNode[btotal];
            for (int i = 0; i < btotal; i++) {
                children[i] = null;
            }
            numChildren = 0;
        }
    }

    // Root of the tree - public to facilitate grading
    public TreeNode root = null;

    //--------------
    // Utility Functions
    //--------------

    // Simple function for determining the other player
    private Player other(Player p) {
        if (p == Player.ONE) return Player.TWO;
        else return Player.ONE;
    }

    // Draws a board using the game state stored as the name of the node.
    public void drawBoard(String s) {
        System.out.println("-------");
        for (int j = 0; j < bsize; j++) {
            System.out.print("|");
            for (int i = 0; i < bsize; i++) {
                char c = s.charAt(i + 3 * j);
                if (c != EMPTYCHAR)
                    System.out.print(c);
                else System.out.print(" ");
                System.out.print("|");
            }
            System.out.println();
            System.out.println("-------");
        }
    }

    /**
     * readTree reads a game tree from the specified file. If the file does not exist, cannot be found, or there is
     * otherwise an error opening or reading the file, it prints out "Error reading file" along with additional error
     * information.
     *
     * @param fName name of file to read from
     */
    public void readTree(String fName) {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(fName));
            root = readTree(reader);
        } catch (java.io.IOException e) {
            System.out.println("Error reading file: " + e);
        }
    }

    // returns 'X' if there is a line of three-X's, 'O' if there is a line of three-O's,
    // and ' ' if there are no lines
    private char checkLines(String state) {
        // horizontal rows
        for (int i = 0; i < 9; i += 3) {
            char c = state.charAt(i);
            if (state.charAt(i + 1) == c && state.charAt(i + 2) == c) {
                return c;
            }
        }

        // vertical rows
        for (int i = 0; i < 3; i++) {
            char c = state.charAt(i);
            if (state.charAt(i + 3) == c && state.charAt(i + 6) == c) {
                return c;
            }
        }

        // diagonal rows
        char c = state.charAt(4);
        if (state.charAt(0) == c && state.charAt(8) == c) {
            return c;
        }
        if (state.charAt(2) == c && state.charAt(6) == c) {
            return c;
        }

        return ' ';
    }

    // Helper function: reads a game tree from the specified reader
    private TreeNode readTree(java.io.BufferedReader reader) throws java.io.IOException {
        String s = reader.readLine();
        if (s == null) {
            throw new java.io.IOException("File ended too soon.");
        }
        TreeNode node = new TreeNode();
        node.numChildren = Integer.parseInt(s.substring(0, 1));
        node.children = new TreeNode[node.numChildren];
        node.leaf = (s.charAt(1) == '1');
        node.value = Integer.MIN_VALUE;
        node.name = s.substring(3);
        if (node.leaf) {
            char v = s.charAt(2);
            node.value = Character.getNumericValue(v);
            node.value--;
            char l = checkLines(node.name);
            // method 1
//            if (l == ' ') {
//                node.value = (node.value >= -2 ? 1 : -1);
//            }

            // method 2
            if (l == 'X') {
                node.value = Integer.MAX_VALUE;
            } else if (l == 'O') {
                node.value = Integer.MIN_VALUE;
            }
        }

        for (int i = 0; i < node.numChildren; i++) {
            node.children[i] = readTree(reader);
        }
        return node;
    }

    /**
     * findValue determines the value for every node in the game tree and sets the value field of each node. If the root
     * is null (i.e., no tree exists), then it returns Integer.MIN_VALUE.
     *
     * @return value of the root node of the game tree
     */
    int findValue() {
        // edge case
        if (root == null) {
            return Integer.MIN_VALUE;
        }

        return findValue(root, Player.ONE);
    }

    // Helper function: finds value at a certain state and player
    int findValue(TreeNode state, Player player) {
        // edge case
        if (state.leaf) {
            return state.value;
        }

        // divide, conquer and merge
        state.value = findValue(state.children[0], other(player));
        for (int i = 1; i < state.numChildren; i++) {
            int currentValue = findValue(state.children[i], other(player));
            boolean updateStateValue;
            if (player == Player.ONE) {
                updateStateValue = currentValue > state.value;
            } else {
                updateStateValue = currentValue < state.value;
            }
            if (updateStateValue) {
                state.value = currentValue;
            }
        }

        return state.value;
    }


    // Simple main for testing purposes
    public static void main(String[] args) {
        GameTree tree = new GameTree();
        tree.readTree("variants/notie.txt");
        System.out.println(tree.findValue());
    }

}
