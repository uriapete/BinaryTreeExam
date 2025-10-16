package binaryTree;

import java.util.ArrayList;
import java.util.function.Consumer;

public class BinaryNode<T extends Comparable<T>> {

    public T key;
    // list of children, left or right
    private final ArrayList<BinaryNode<T>> children = new ArrayList<BinaryNode<T>>(2);

    // direction — left, right
    enum Direction{
        Left(0), Right(1);

        public final int idx;
        private static Direction[] dirs = {Left,Right};
        private Direction(int id) {
            this.idx = id;
        }

        public Direction getDirection(int id){
            return dirs[(id%2)];
        }
        
    }

    // default constructor sets binary node children
    public BinaryNode() {
        for(Direction d : Direction.values()){
            children.add(null);
        }
    }

    public BinaryNode(T k) {
        // set binary node children
        this();
        // set key
        key = k;
    }

    // helper fn - same as below override, but first arg is dir enum instead of int idx
    private boolean insertChild(Direction dir, BinaryNode<T> node){
        return insertChild(dir.idx, node);
    }

    // helper fn - insert child in specified direction, return bool for success
    private boolean insertChild(int diridx, BinaryNode<T> node){
        // if children dne,
        // set children
        if (children.get(diridx) == null) {
            children.set(diridx, node);
            return true;
        }
        // else, can't insert children in place where child already exists!
        return false;
    }

    // creates a key and inserts it as a descendant (if key does not already exist in this subtree)
    public boolean insertKey(T k) {
        if (k == key) {
            return false;
        }
        return insertNode(new BinaryNode<>(k));
    }

    // inserts a node as child
    public boolean insertNode(BinaryNode<T> node) {
        // if the new child's key is the same as this one's, NO!
        if (node.key.equals(key)) {
            return false;
        }

        // this will signal if op is successful
        boolean newChild;

        // get idx of new child...
        // get value for key comparison: <0 if left, >0 if right
        // add +1: 0<=x<1 for left, 1<x<=2
        // floor and clamp: 0 for left, 1 for right. this is now our child index.
        int childIdx = (int)Math.floor(Math.clamp(node.key.compareTo(key)+1, 0, 1));

        // insert child
        newChild = insertChild(childIdx, node);
        if (newChild) {
            return newChild;
        }
        
        // if it didn't work, try descendents
        return children.get(childIdx).insertNode(node);
    }

    // traverse. visit left descendants, then this, then right descendants.
    protected void traverseInOrder(Consumer<BinaryNode<T>> operation) {
        if (children.get(Direction.Left.idx) != null) {
            children.get(Direction.Left.idx).traverseInOrder(operation);
        }
        operation.accept(this);
        if (children.get(Direction.Right.idx) != null) {
            children.get(Direction.Right.idx).traverseInOrder(operation);
        }
    }

    public void printInOrder() {
        traverseInOrder((BinaryNode<T> node) -> {
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
