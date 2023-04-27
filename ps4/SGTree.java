/**
 * ScapeGoat Tree class
 *
 * This class contains some of the basic code for implementing a ScapeGoat tree.
 * This version does not include any of the functionality for choosing which node
 * to scapegoat.  It includes only code for inserting a node, and the code for rebuilding
 * a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     *
     * This class holds the data for a node in a binary tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;

        TreeNode(int k) {
            key = k;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {
        // edge cases
        if (node == null || child == null) {
            return 0;
        }
        if (child == Child.LEFT && node.left == null) {
            return 0;
        }
        if (child == Child.RIGHT && node.right == null) {
            return 0;
        }

        // divide and conquer
        TreeNode targetChild = (child == Child.LEFT ? node.left : node.right);
        int countLeft = countNodes(targetChild, Child.LEFT);
        int countRight = countNodes(targetChild, Child.RIGHT);

        // merge
        return 1 + countLeft + countRight;
    }

    /**
     * Builds an array of nodes in the specified subtree
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    public TreeNode[] enumerateNodes(TreeNode node, Child child) {
        TreeNode[] traversal = new TreeNode[this.countNodes(node, child)];
        this.enumerateNodes(node, child, traversal, 0);
        return traversal;
    }

    // Helper function: given a node, a "child direction" (left or right), an array, an index,
    // performs an in-order traversal on the corresponding child of the node,
    // records the list of nodes traversed on the array starting from the index,
    // returns the next index after the end of this traversal.
    int enumerateNodes(TreeNode node, Child child, TreeNode[] traversal, int i) {
        // edge cases
        if (node == null || child == null) {
            return i;
        }
        if (child == Child.LEFT && node.left == null) {
            return i;
        }
        if (child == Child.RIGHT && node.right == null) {
            return i;
        }

        // divide, conquer and merge
        TreeNode targetChild = (child == Child.LEFT ? node.left : node.right);
        int newI = this.enumerateNodes(targetChild, Child.LEFT, traversal, i);
        traversal[newI] = targetChild;
        return this.enumerateNodes(targetChild, Child.RIGHT, traversal, newI + 1);
    }

    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode buildTree(TreeNode[] nodeList) {
        return buildTree(nodeList, 0, nodeList.length - 1);
    }

    // Helper function: given a list of nodes nodeList, indices left and right,
    // builds a balanced binary search tree consisting of nodes in nodeList[left..right].
    TreeNode buildTree(TreeNode[] nodeList, int left, int right) {
        // I assumed that nodeList is sorted according to keys.

        // edge cases
        if (right - left + 1 <= 0) {
            return null;
        }
        if (right - left + 1 == 1) {
            return new TreeNode(nodeList[left].key);
        }

        // divide
        int mid = left + (right - left)/2;
        TreeNode root = new TreeNode(nodeList[mid].key);

        // conquer and merge
        root.left = buildTree(nodeList, left, mid - 1);
        root.right = buildTree(nodeList, mid + 1, right);

        return root;
    }

    /**
    * Rebuilds the specified subtree of a node
    * 
    * @param node the part of the subtree to rebuild
    * @param child specifies which child is the root of the subtree to rebuild
    */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }
    }

    /**
    * Inserts a key into the tree
    *
    * @param key the key to insert
    */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        TreeNode node = root;

        while (true) {
            if (key <= node.key) {
                if (node.left == null) break;
                node = node.left;
            } else {
                if (node.right == null) break;
                node = node.right;
            }
        }

        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }
    }


    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 4; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root, Child.RIGHT);
    }
}
