public class Solution {
    private final AVLTree avl;

    public static class Quest implements Comparable<Quest> {
        private final long energy;
        private final long value;

        public Quest(long energy, long value) {
            this.energy = energy;
            this.value = value;
        }

        public long getEnergy() {
            return this.energy;
        }

        public long getValue() {
            return this.value;
        }

        @Override
        public int compareTo(Quest other) {
            if (this.energy == other.energy) {
                if (this.value == other.value) {
                    return 0;
                }
                return (this.value < other.value ? -1 : 1);
            }
            return (this.energy < other.energy ? -1 : 1);
        }
    }

    public static class TreeNode {
        private Quest key;
        private TreeNode left;
        private TreeNode right;
        private int height;

        public TreeNode(Quest key) {
            this.key = key;
        }
    }

    public static class AVLTree {
        private TreeNode root;

        private int height(TreeNode node) {
            return (node == null ? -1 : node.height);
        }

        private void updateHeight(TreeNode node) {
            node.height = Math.max(this.height(node.left), this.height(node.right)) + 1;
        }

        private int balanceFactor(TreeNode node) {
            return this.height(node.left) - this.height(node.right);
        }

        private TreeNode rotateLeft(TreeNode node) {
            TreeNode rightChild = node.right;
            node.right = rightChild.left;
            rightChild.left = node;
            this.updateHeight(node);
            this.updateHeight(rightChild);
            return rightChild;
        }

        private TreeNode rotateRight(TreeNode node) {
            TreeNode leftChild = node.left;
            node.left = leftChild.right;
            leftChild.right = node;
            this.updateHeight(node);
            this.updateHeight(leftChild);
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

        private TreeNode insert(Quest key, TreeNode node) {
            if (node == null) {
                node = new TreeNode(key);
            } else if (key.compareTo(node.key) <= 0) {
                node.left = this.insert(key, node.left);
            } else {
                node.right = this.insert(key, node.right);
            }
            this.updateHeight(node);
            return this.rebalance(node);
        }

        public void insert(Quest key) {
            this.root = this.insert(key, this.root);
        }

        private TreeNode findMin(TreeNode node) {
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }

        private TreeNode delete(Quest key, TreeNode node) {
            if (node == null) {
                return null;
            }
            if (key.compareTo(node.key) < 0) {
                node.left = this.delete(key, node.left);
            } else if (key.compareTo(node.key) > 0) {
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
            this.updateHeight(node);
            return this.rebalance(node);
        }

        public void delete(Quest key) {
            this.root = this.delete(key, this.root);
        }

        private TreeNode findQuest(long remainingEnergy, TreeNode node, TreeNode cur) {
            if (node == null) {
                return cur;
            }
            if (node.key.getEnergy() <= remainingEnergy) {
                cur = node;
                return findQuest(remainingEnergy, node.right, cur);
            }
            return findQuest(remainingEnergy, node.left, cur);
        }

        public TreeNode findQuest(long remainingEnergy) {
            return this.findQuest(remainingEnergy, this.root, null);
        }
    }

    public Solution() {
        this.avl = new AVLTree();
    }

    void add(long energy, long value) {
        this.avl.insert(new Quest(energy, value));
    }

    long query(long remainingEnergy) {
        long result = 0;
        while (remainingEnergy > 0) {
            TreeNode toClear = this.avl.findQuest(remainingEnergy);
            if (toClear == null) {
                break;
            }
            remainingEnergy -= toClear.key.getEnergy();
            if (remainingEnergy < 0) {
                break;
            }
            result += toClear.key.getValue();
            this.avl.delete(toClear.key);
        }
        return result;
    }

}
