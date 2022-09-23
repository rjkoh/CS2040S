import java.util.ArrayList;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';
    TrieNode root;

    private class TrieNode {
        // TODO: Create your TrieNode class here.
        // arr of children nodes, with 0-9 for 0-9; 10-35 for A-Z; 36-61 for a-z
        TrieNode[] children;
        char key;
        boolean end;

        // for creating a new root - no children
        public TrieNode() {
            this.children = new TrieNode[62];
            this.end = false;
        }

        // for creating a new internal node
        public TrieNode(char c) {
            this.children = new TrieNode[62];
            this.key = c;
            this.end = false;
        }

        public char keyat(int i) {
            return this.children[i].key;
        }

        public void setChild(int i, char c) {
            TrieNode child = new TrieNode(c);
            this.children[i] = child;
        }

        public TrieNode getChild(int i) {
            return this.children[i];
        }

        public void setend(){
            this.end = true;
        }

        public boolean isEnd() {
            return this.end;
        }
    }

    public Trie() {
        // TODO: Initialise a trie class here.
        this.root = new TrieNode();
    }

    public int toindex(char c) {
        int ascii = (int) c;

        // 0-9
        if (ascii >= 48 && ascii <= 57) {
            return ascii - 48;
        }
        // A-Z
        else if (ascii >= 65 && ascii <= 90) {
            return ascii - 55;
        }
        // a-z
        else if (ascii >= 97 && ascii <= 122) {
            return ascii - 61;
        } else {
            return -1;
        }
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        // TODO
        TrieNode node = this.root;
        int length = s.length();
        int i = 0;
        while (i < length) {
            char c = s.charAt(i);
            int cint = toindex(c);
            // if current char alr exists as a child, move onto that child
            if (node.getChild(cint) != null && c == node.keyat(cint)) {
                node = node.getChild(cint);
                i++;
            }
            // insert that child into children arr
            else {
                node.setChild(cint, c);
                i++;
                node = node.getChild(cint);
            }
        }
        node.setend();
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        // TODO
        TrieNode node = this.root;
        int length = s.length();
        int i = 0;
        while (i < length) {
            char c = s.charAt(i);
            int cint = toindex(c);
            // if current char alr exists as a child, move onto that child and continue search
            if (node.getChild(cint) != null && c == node.keyat(cint)) {
                node = node.getChild(cint);
                i++;
            }
            // if current char doesnt exist, return false
            else {
                return false;
            }
        }

        return node.isEnd();
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
        // TODO
        pshelper(s, results, limit, 0, this.root, new StringBuilder());
    }

    void pshelper(String s, ArrayList<String> results, int limit, int depth, TrieNode node, StringBuilder accum) {
        // traversed all nodes
        if (results.size() >= limit) {
            // do nothing, end of method
        // traversed until last node for s
        } else if (s.length() <= depth) {
            // can add results and end of string
            if (node.isEnd()) {
                results.add(accum.toString());
                // add any more words that have the current word inside it i.e. more chars along the path
                for (TrieNode child : node.children) {
                    if (child != null) {
                        pshelper(s, results, limit, depth, child, new StringBuilder(accum).append(child.key));
                    }

                    if (results.size() == limit) break;
                }
            } else {
                // yet to reach end of string, recursively loop through children if it exists
                for (TrieNode child : node.children) {
                    if (child != null) {
                        pshelper(s, results, limit, depth, child, new StringBuilder(accum).append(child.key));
                    }

                    if (results.size() == limit) break;
                }
            }
        // yet to traverse all nodes for s
        } else {
            char c = s.charAt(depth);
            // if normal character
            if (c != WILDCARD) {
                int cint = toindex(c);
                if (cint > 0) {
                    TrieNode child = node.getChild(cint);
                    // child carrying key exists
                    if (child != null) {
                        pshelper(s, results, limit, depth + 1, child, accum.append(child.key));
                    } else {
                        // do nothing, end of path, no suggestions
                    }
                }

            // c is wildcard
            } else {
                for (TrieNode child : node.children) {
                    if (child != null) {
                        pshelper(s, results, limit, depth + 1, child, new StringBuilder(accum).append(child.key));
                    }
                }
            }
        }
    }


    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("a");
        t.insert("abba");
        t.insert("abbde");
        t.insert("abcd");
        t.insert("abcdef");
        t.insert("abed");
        t.insert("dbec");

        /*
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
        */
        for (String str:  t.prefixSearch("", 10)) {
            System.out.println(str);
        }
        // String[] result2 = ;
        // result1 should be: t.prefixSearch("pe", 10)
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
