public class Solution {
    private final AVLTree avl;
    private final Score[] scores;

    public static class Score implements Comparable<Score> {
        private long solved;
        private long penalty;

        public Score() {
            this.solved = 0;
            this.penalty = 0;
        }

        public Score(long solved, long penalty) {
            this.solved = solved;
            this.penalty = penalty;
        }

        public void change(long penalty) {
            this.solved++;
            this.penalty += penalty;
        }

        @Override
        public int compareTo(Score other) {
            if (this.solved == other.solved) {
                if (this.penalty == other.penalty) {
                    return 0;
                }
                return (this.penalty < other.penalty ? 1 : -1);
            }
            return (this.solved < other.solved ? -1 : 1);
        }
    }

    public static class TreeNode {
        private Score key;
        private TreeNode left;
        private TreeNode right;
        private int height;
        private int weight = 1;
        private int count = 1;

        public TreeNode(Score key) {
            this.key = key;
        }
    }
    
    public static class AVLTree {
        private TreeNode root;

        private int height(TreeNode node) {
            return (node == null ? -1 : node.height);
        }

        private int weight(TreeNode node) {
            return (node == null ? 0 : node.weight);
        }

        private void updateHW(TreeNode node) {
            node.height = Math.max(this.height(node.left), this.height(node.right)) + 1;
            node.weight = this.weight(node.left) + this.weight(node.right) + node.count;
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

        private TreeNode insert(Score key, TreeNode node) {
            if (node == null) {
                node = new TreeNode(new Score(key.solved, key.penalty));
            } else if (key.compareTo(node.key) < 0) {
                node.right = this.insert(key, node.right);
            } else if (key.compareTo(node.key) > 0) {
                node.left = this.insert(key, node.left);
            } else {
                node.count++;
            }
            this.updateHW(node);
            return this.rebalance(node);
        }

        public void insert(Score key) {
            this.root = this.insert(key, this.root);
        }

        private TreeNode findMin(TreeNode node) {
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }

        private TreeNode delete(Score key, TreeNode node) {
            if (node == null) {
                return null;
            }
            if (key.compareTo(node.key) < 0) {
                node.right = this.delete(key, node.right);
            } else if (key.compareTo(node.key) > 0) {
                node.left = this.delete(key, node.left);
            } else {
                node.count--;
                if (node.count == 0) {
                    if (node.left == null && node.right == null) {
                        node = null;
                    } else if (node.left == null) {
                        node = node.right;
                    } else if (node.right == null) {
                        node = node.left;
                    } else {
                        TreeNode successor = this.findMin(node.right);
                        node.key = successor.key;
                        node.count = successor.count;
                        successor.count = 1;
                        node.right = this.delete(successor.key, node.right);
                    }
                }
            }
            if (node == null) {
                return null;
            }
            this.updateHW(node);
            return this.rebalance(node);
        }

        public void delete(Score key) {
            this.root = this.delete(key, this.root);
        }

        private int rank(Score key, TreeNode node) {
            if (key.compareTo(node.key) < 0) {
                return this.weight(node.left) + node.count + this.rank(key, node.right);
            } else if (key.compareTo(node.key) > 0) {
                return this.rank(key, node.left);
            } else {
                return this.weight(node.left) + 1;
            }
        }

        public int rank(Score key) {
            return this.rank(key, this.root);
        }
    }

    public Solution(int numTeams) {
        this.avl = new AVLTree();
        this.scores = new Score[numTeams];
        for (int i = 0; i < numTeams; i++) {
            Score initial = new Score();
            scores[i] = initial;
            this.avl.insert(initial);
        }
    }

    public int update(int team, long newPenalty) {
        if (team > 0 && team <= this.scores.length) {
            this.avl.delete(this.scores[team - 1]);
            this.scores[team - 1].change(newPenalty);
            this.avl.insert(this.scores[team - 1]);
        }
        return this.avl.rank(scores[0]);
    }
}
