package binaryTree;

import java.util.function.Consumer;

public class BinaryNode<T extends Comparable> {

    public T key;
    BinaryNode<T> left;
    BinaryNode<T> right;
    public BinaryNode<T> parent;

    public BinaryNode() {
    }

    public BinaryNode(T k) {
        key = k;
    }

    // helper fn - insert left child, return bool for success
    private boolean insertLeft(BinaryNode<T> node) {
        if (left == null) {
            node.parent = this;
            left = node;
            return true;
        }
        return false;
    }
    // helper fn - insert right child, return bool for success

    private boolean insertRight(BinaryNode<T> node) {
        if (right == null) {
            node.parent = this;
            right = node;
            return true;
        }
        return false;
    }

    public boolean insertKey(T k) {
        if (k == key) {
            return false;
        }
        return insertNode(new BinaryNode<T>(k));
    }

    public boolean insertNode(BinaryNode<T> node) {
        if (node.key.equals(key)) {
            return false;
        }
        boolean newChild;

        // if key needs to go left, try to insertLeft and recursive call (keep going down the tree) if left exists
        if (node.key.compareTo(key) < 0) {
            newChild = insertLeft(node);
            if (newChild) {
                return newChild;
            }
            return left.insertNode(node);
        }
        // do same as above but for the right
        if (node.key.compareTo(key) > 0) {
            newChild = insertRight(node);
            if (newChild) {
                return newChild;
            }
            return right.insertNode(node);
        }

        return false;
    }

    protected void traverseInOrder(Consumer<BinaryNode> operation) {
        if (left != null) {
            left.traverseInOrder(operation);
        }
        operation.accept(this);
        if (right != null) {
            right.traverseInOrder(operation);
        }
    }

    public void printInOrder() {
        traverseInOrder((BinaryNode node) -> {
            System.out.println(node.key);
        });
    }

    // Follow the steps below to use this file
    // 1. In the main file, create an instance of this class:
    // NewClass1 instance1 = new NewClass1();
    // 2. Call the method to get the greeting message:
    // System.out.println(instance1.sayHelloFromNewClass());
    public String sayHelloFromNewClass() {
        return "Hello from New Class 1";
    }

}
