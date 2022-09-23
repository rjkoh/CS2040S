import com.sun.source.tree.Tree;

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
        int weight;

        TreeNode(int k) {
            key = k;
            weight = 1;
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
        // TODO: Implement this
        // O(n) time O(1) space
        if (child.LEFT == child) {
            if (node.left != null) {
                return 1 + countNodes(node.left, child.LEFT) + countNodes(node.left, child.RIGHT);
            } else {
                return 0;
            }
        } else {
            if (node.right != null) {
                return 1 + countNodes(node.right, child.LEFT) + countNodes(node.right, child.RIGHT);
            } else {
                return 0;
            }
        }
    }

    /**
     * Builds an array of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    TreeNode[] enumerateNodes(TreeNode node, Child child) {
        // TODO: Implement this
        // O(n) time and space
        TreeNode first;
        if (child == child.LEFT) {
            first = node.left;
        } else {
            first = node.right;
        }
        if (first == null) {
            return null;
        } else {
            int nodes = countNodes(node, child);
            TreeNode[] arr = new TreeNode[nodes];
            enumhelper(arr, first, 0);
            return arr;
        }
    }
    public int enumhelper(TreeNode[] arr, TreeNode node, int curr) {
        if (node == null) {
            return curr;
        } else {
            // fills in left portion into arr, then mid, then right
            int mid = enumhelper(arr, node.left, curr);
            arr[mid] = node;
            mid += 1;
            int last = enumhelper(arr, node.right, mid);
            return last;
        }
    }

    /*
            previous implementation O(nlogn)
            TreeNode[] left = enumerateNodes(first, child.LEFT);
            TreeNode[] right = enumerateNodes(first, child.RIGHT);
            if (left != null && right != null) {
                int leftlen = left.length;
                for (int i = 0; i < leftlen; i++) {
                    arr[i] = left[i];
                }
                arr[leftlen] = first;
                for (int i = 0; i < right.length; i++) {
                    arr[i + 1 + leftlen] = right[i];
                }
            } else if (left == null && right != null) {
                arr[0] = first;
                for (int i = 0; i < right.length; i++) {
                    arr[i + 1] = right[i];
                }
            } else if (left != null && right == null) {
                int leftlen = left.length;
                for (int i = 0; i < leftlen; i++) {
                    arr[i] = left[i];
                }
                arr[leftlen] = first;
            } else {
                arr[0] = first;
            }
            return arr;
             */

    /**
     * Builds a tree from the list of nodes Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    TreeNode buildTree(TreeNode[] nodeList) {
        // TODO: Implement this
        // O(n) time and O(1) space
        if (nodeList.length != 0) {
            int nodes = nodeList.length;
            return buildhelper(nodeList, 0, nodes);
        } else {
            return null;
        }
    }
    public TreeNode buildhelper(TreeNode[] nodeList, int start, int length) {
        if (length != 0) {
            // fill up left end of list, then add mid, then right
            int mid = length / 2 + start;
            TreeNode rt = nodeList[mid];
            rt.weight = length;

            rt.left = buildhelper(nodeList, start, mid - start);

            rt.right = buildhelper(nodeList, mid + 1,  length - (mid - start) - 1);

            return rt;
        } else {
            return null;
        }
    }

            /*
            previous implementation O(nlogn)
            TreeNode root = nodeList[mid];
            TreeNode[] left = new TreeNode[mid];
            for (int i = 0; i < mid; i++) {
                left[i] = nodeList[i];
            }
            TreeNode[] right = new TreeNode[nodes - mid - 1];
            for (int i = 0; i < nodes - mid - 1; i++) {
                right[i] = nodeList[mid + 1 + i];
            }
            root.left = buildTree(left);
            root.right = buildTree(right);
            return root;
            */

    /**
     * Determines if a node is balanced. If the node is balanced, this should return true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        // TODO: Implement this
        // O(n)
        // if node is empty or is leaf
        if (node == null || (node.left == null && node.right == null)) {
            return true;
        // node is internal node
        } else {
            // node is out of balance
            if ((node.left != null && node.left.weight > node.weight * 2/3) ||
                    (node.right != null && node.right.weight > node.weight * 2/3)) {
                return false;
            // check if children are out of balance
            } else {
                return checkBalance(node.left) && checkBalance(node.right);
            }
        }
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
    }

    /**
     * Inserts a key into the tree.
     *
     * @param key the key to insert
     */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        TreeNode node = root;
        TreeNode inserted = new TreeNode(key);

        while (true) {
            node.weight++;
            if (key <= node.key) {
                if (node.left == null) {
                    break;
                } else {
                    node = node.left;
                    node.weight++;
                }
            } else {
                if (node.right == null) {
                    break;
                } else {
                    node = node.right;
                    node.weight++;
                }
            }
        }

        if (key <= node.key) {
            node.left = inserted;
        } else {
            node.right = inserted;
        }

        TreeNode unbalanced = this.root;
        while (true) {
            if (key <= unbalanced.key) {
                if (unbalanced.left == null) {
                    break;
                } else if (!checkBalance(unbalanced.left)) {
                    rebuild(unbalanced, Child.LEFT);
                }
                unbalanced = unbalanced.left;
            } else {
                if (unbalanced.right == null) {
                    break;
                } else if (!checkBalance(unbalanced.right)) {
                    rebuild(unbalanced, Child.RIGHT);
                }
                unbalanced = unbalanced.right;
            }
        }
    }

    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        /*
        TreeNode rt = new TreeNode(1);
        tree.root = rt;
        System.out.println(tree.enumerateNodes(rt, Child.LEFT));
         */

        tree.root = new TreeNode(0);
        for (int i = 1; i < 5; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root, Child.RIGHT);


    }
}
