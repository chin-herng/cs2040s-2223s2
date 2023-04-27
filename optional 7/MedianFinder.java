//import java.util.TreeMap;
//import java.util.TreeSet;

public class MedianFinder {

    private final AVLTree avl;

    public static class TreeNode {
        private int key;
        private TreeNode left;
        private TreeNode right;
        private int height;
        private int weight;

        public TreeNode(int key) {
            this.key = key;
        }
    }

    public static class AVLTree {
        private TreeNode root;

        public TreeNode getRoot() {
            return this.root;
        }

        private int height(TreeNode node) {
            return (node == null ? -1 : node.height);
        }

        private int weight(TreeNode node) {
            return (node == null ? 0 : node.weight);
        }

        private void updateHW(TreeNode node) {
            node.height = Math.max(this.height(node.left), this.height(node.right)) + 1;
            node.weight = this.weight(node.left) + this.weight(node.right) + 1;
        }

        private int balanceFactor(TreeNode node) {
            return this.height(node.left) - this.height(node.right);
        }

        private TreeNode rotateLeft(TreeNode node) {
            TreeNode rightChild = node.right;
            node.right = rightChild.left;
            rightChild.left = node;
            this.updateHW(node);
            this.updateHW(rightChild);
            return rightChild;
        }

        private TreeNode rotateRight(TreeNode node) {
            TreeNode leftChild = node.left;
            node.left = leftChild.right;
            leftChild.right = node;
            this.updateHW(node);
            this.updateHW(leftChild);
            return leftChild;
        }

        private TreeNode rebalance(TreeNode node) {
            int balanceFactor = this.balanceFactor(node);
            if (balanceFactor > 1) {
                if (this.balanceFactor(node.left) < 0) {
                    node.left = this.rotateLeft(node.left);
                }
                node = this.rotateRight(node);
            } else if (balanceFactor < -1) {
                if (this.balanceFactor(node.right) > 0) {
                    node.right = this.rotateRight(node.right);
                }
                node = this.rotateLeft(node);
            }
            return node;
        }

        private TreeNode insert(int key, TreeNode node) {
            if (node == null) {
                node = new TreeNode(key);
            } else if (key <= node.key) {
                node.left = this.insert(key, node.left);
            } else {
                node.right = this.insert(key, node.right);
            }
            this.updateHW(node);
            return this.rebalance(node);
        }

        public void insert(int key) {
            this.root = this.insert(key, this.root);
        }

        private TreeNode findMin(TreeNode node) {
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }

        private TreeNode delete(int key, TreeNode node) {
            if (node == null) {
                return null;
            }
            if (key < node.key) {
                node.left = this.delete(key, node.left);
            } else if (key > node.key) {
                node.right = this.delete(key, node.right);
            } else if (node.left == null && node.right == null) {
                node = null;
            } else if (node.left == null) {
                node = node.right;
            } else if (node.right == null) {
                node = node.left;
            } else {
                TreeNode successor = this.findMin(node.right);
                node.key = successor.key;
                node.right = this.delete(successor.key, node.right);
            }
            if (node == null) {
                return null;
            }
            this.updateHW(node);
            return this.rebalance(node);
        }

        public void delete(int key) {
            this.root = this.delete(key, this.root);
        }

        private TreeNode select(int k, TreeNode node) {
            int leftWeight = this.weight(node.left);
            if (k == leftWeight + 1) {
                return node;
            }
            if (k < leftWeight + 1) {
                return this.select(k, node.left);
            }
            return this.select(k - leftWeight - 1, node.right);
        }

        public TreeNode select(int k) {
            return this.select(k, this.root);
        }
    }

    public MedianFinder() {
        this.avl = new AVLTree();
    }

    public void insert(int x) {
        this.avl.insert(x);
    }

    public int getMedian() {
        int n = this.avl.weight(this.avl.getRoot());
        int k = (n % 2 == 1 ? (n + 1) / 2 : n / 2 + 1);
        int res = this.avl.select(k).key;
        this.avl.delete(res);
        return res;
    }

}
