import java.util.ArrayList;
import java.util.Arrays;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';
    final int NUMBERS_COUNT = 10;
    final int ALPHABETS_COUNT = 26;
    final int CHILDREN_COUNT = 62;
    private final TrieNode root;

    private class TrieNode {
        int[] presentChars = new int[62]; // not sure about the use of this
        TrieNode[] children = new TrieNode[CHILDREN_COUNT];
        boolean flag = false; // true flag denotes end of string
    }

    public Trie() {
        this.root = new TrieNode();
    }

    // maps each alphanumeric character to a unique integer between 0 and 61
    private int charToIdx(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + NUMBERS_COUNT;
        }
        if (c >= 'a' && c <= 'z') {
            return c - 'a' + NUMBERS_COUNT + ALPHABETS_COUNT;
        }
        // should not reach here
        return 0;
    }

    // maps each integer between 0 and 61 to a unique alphanumeric character
    private char idxToChar(int i) {
        if (i >= this.charToIdx('0') && i <= this.charToIdx('9')) {
            return (char) (i + '0');
        }
        if (i >= this.charToIdx('A') && i <= this.charToIdx('Z')) {
            return (char) (i - NUMBERS_COUNT + 'A');
        }
        if (i >= this.charToIdx('a') && i <= this.charToIdx('z')) {
            return (char) (i - NUMBERS_COUNT - ALPHABETS_COUNT + 'a');
        }
        // should not reach here
        return '?';
    }

    // Helper function
    private void insert(String s, TrieNode node, int strIdx) {
        if (strIdx == s.length()) {
            // we have reached the end, indicate the current node to be the end of a string
            node.flag = true;
            return;
        }

        int charIdx = this.charToIdx(s.charAt(strIdx));

        if (node.children[charIdx] == null) {
            // no more child nodes to follow, add another node to represent the next character
            node.children[charIdx] = new TrieNode();
        }
        this.insert(s, node.children[charIdx], strIdx + 1);
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        this.insert(s, this.root, 0);
    }

    // Helper function
    private boolean contains(String s, TrieNode node, int strIdx) {
        if (strIdx == s.length()) {
            // we have reached the end, check whether the flag is correct
            return node.flag;
        }

        int charIdx = this.charToIdx(s.charAt(strIdx));

        if (node.children[charIdx] == null) {
            // no more child nodes to follow, the string is not in the trie
            return false;
        }

        return this.contains(s, node.children[charIdx], strIdx + 1);
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        return this.contains(s, this.root, 0);
    }

    // Helper function
    private void prefixSearch(String s, ArrayList<String> results, int limit,
        StringBuilder sb, int strIdx, TrieNode node) {
        if (results.size() == limit) {
            return;
        }

        if (strIdx >= s.length() || s.charAt(strIdx) == WILDCARD) {
            if (strIdx >= s.length() && node.flag) {
                // we found a string
                results.add(sb.toString());
            }

            // next character can be anything
            for (int i = 0; i < CHILDREN_COUNT; i++) {
                if (node.children[i] != null) {
                    sb.append(this.idxToChar(i));
                    this.prefixSearch(s, results, limit, sb, strIdx + 1,
                        node.children[i]);
                    // the internet claims that setLength works fine and is faster than delete
                    sb.setLength(sb.length() - 1);
                }
            }
        } else {
            // follow the next character
            char next = s.charAt(strIdx);
            if (node.children[charToIdx(next)] != null) {
                sb.append(next);
                this.prefixSearch(s, results, limit, sb, strIdx + 1,
                        node.children[this.charToIdx(next)]);
                sb.setLength(sb.length() - 1);
            }
        }
    }

    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        StringBuilder sb = new StringBuilder();
        this.prefixSearch(s, results, limit, sb, 0, this.root);
    }


    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");

        String[] result1 = t.prefixSearch("pe", 10);
        String[] result2 = t.prefixSearch("pe.", 10);

        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        System.out.println(Arrays.toString(result1));
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
        System.out.println(Arrays.toString(result2));
    }
}
