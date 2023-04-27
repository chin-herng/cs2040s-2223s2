/**
 * ScapeGoat Tree class
 * <p>
 * This class contains some basic code for implementing a ScapeGoat tree. This version does not include any of the
 * functionality for choosing which node to scapegoat. It includes only code for inserting a node, and the code for
 * rebuilding a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     * <p>
     * This class holds the data for a node in a binary tree.
     * <p>
     * Note: we have made things public here to facilitate problem set grading/testing. In general, making everything
     * public like this is a bad idea!
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;
        int weight = 0;

        TreeNode(int k) {
            key = k;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree.
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
     * Builds an array of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    TreeNode[] enumerateNodes(TreeNode node, Child child) {
        // Improved from O(n log n) in PS4 to O(n) in PS5.
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
     * Builds a tree from the list of nodes Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    TreeNode buildTree(TreeNode[] nodeList) {
        // This helper function improves the running time of buildTree from O(n log n) to O(n).
        return buildTree(nodeList, 0, nodeList.length - 1);
    }

    // Helper function: given a list of nodes nodeList, indices left and right,
    // builds a balanced binary search tree consisting of nodes in nodeList[left..right].
    TreeNode buildTree(TreeNode[] nodeList, int left, int right) {
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
     * Determines if a node is balanced. If the node is balanced, this should return true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        // edge case
        if (node == null) {
            return true;
        }

        int leftWeight = (node.left == null ? 0 : node.left.weight);
        int rightWeight = (node.right == null ? 0 : node.right.weight);

        // avoids multiplying or dividing integers by non-integer numbers
        return (3 * leftWeight <= 2 * node.weight && 3 * rightWeight <= 2 * node.weight);
    }

    // given a node u and a "child direction" (left or right), fix the weights of
    // all the nodes in the subtree rooted at the corresponding child of u.
    void fixWeights(TreeNode u, Child child) {
        // edge case
        if (u == null || child == null) {
            return;
        }
        if (child == Child.LEFT && u.left == null) {
            return;
        }
        if (child == Child.RIGHT && u.right == null) {
            return;
        }

        // divide and conquer
        TreeNode targetChild = (child == Child.LEFT ? u.left : u.right);
        fixWeights(targetChild, Child.LEFT);
        fixWeights(targetChild, Child.RIGHT);

        // merge
        int leftWeight = (targetChild.left == null ? 0 : targetChild.left.weight);
        int rightWeight = (targetChild.right == null ? 0 : targetChild.right.weight);
        u.weight = leftWeight + rightWeight + 1;
    }

    /**
     * Rebuilds the specified subtree of a node.
     *
     * @param node  the part of the subtree to rebuild
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
        // Fix the weights of all the nodes of the subtree rooted at child
        fixWeights(node, child);
    }

    // given a newly inserted key, a node and the node's parent,
    // find the highest unbalanced node along the node-to-newly-inserted-leaf path,
    // then rebuilds the corresponding subtree.
    void rebalance(int key, TreeNode node, TreeNode parent) {
        // edge cases
        if (key == node.key) {
            return;
        }
        if (parent != null && !checkBalance(node)) {
            this.rebuild(parent, node == parent.left ? Child.LEFT : Child.RIGHT);
            return;
        }

        if (key < node.key) {
            this.rebalance(key, node.left, node);
        } else {
            this.rebalance(key, node.right, node);
        }
    }

    /**
     * Inserts a key into the tree.
     *
     * @param key the key to insert
     */
    public void insert(int key) {
        this.root = this.insert(key, this.root);
        this.rebalance(key, this.root, null);
    }

    // Helper function: given a key and a node,
    // insert the key into the subtree rooted at the node.
    TreeNode insert(int key, TreeNode node) {
        if (node == null || key == node.key) {
            // a duplication insertion leaves the tree unchanged
            node = new TreeNode(key);
        } else if (key < node.key) {
            node.left = this.insert(key, node.left);
        } else {
            node.right = this.insert(key, node.right);
        }
        int leftWeight = (node.left == null ? 0 : node.left.weight);
        int rightWeight = (node.right == null ? 0 : node.right.weight);
        node.weight = leftWeight + rightWeight + 1;
        return node;
    }


    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root, Child.RIGHT);
    }
}
