package binaryTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Consumer;

public class BinaryNode<T extends Comparable<T>> {

    public T key;
    // list of children, dependant on number of directions spec'd in enum
    private ArrayList<BinaryNode<T>> children = new ArrayList<>(Direction.dirs.length);

    // list of parents
    private ArrayList<BinaryNode<T>> parents = new ArrayList<>(Direction.dirs.length);

    // gets the child in the spec'd direction
    private BinaryNode<T> getChild(Direction direction){
        return children.get(direction.idx);
    }

    private void setChild(Direction direction, BinaryNode<T> node){
        children.set(direction.idx, node);
        if (node==null) {
            return;
        }
        node.parents.set(Direction.getDirection(direction.idx+1).idx, this);
    }

    // direction — left, right
    enum Direction{
        Left(0), Right(1);

        public final int idx;
        private static final Direction[] dirs = {Left,Right};
        private Direction(int id) {
            this.idx = id;
        }

        static public Direction getDirection(int id){
            return dirs[(id%2)];
        }
        
    }

    // default constructor sets binary node children
    public BinaryNode() {
        for(Direction _ : Direction.values()){
            children.add(null);
            parents.add(null);
        }
    }

    public BinaryNode(T k) {
        // set binary node children
        this();
        // set key
        key = k;
    }

    // helper fn - insert child in specified direction, return bool for success
    private boolean insertChild(Direction dir, BinaryNode<T> node){
        // if children dne,
        // set children
        if(getChild(dir)==null){
            setChild(dir, node);
            return true;
        }
        // else, can't insert children in place where child already exists!
        return false;
    }

    // helper fn - same as below override, but first arg is dir enum instead of int idx
    private boolean insertChild(int diridx, BinaryNode<T> node){
        return insertChild(Direction.getDirection(diridx), node);
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

    // swaps children and parent.
    private void swapChildParent(){
        ArrayList<BinaryNode<T>> newParent = this.children;
        this.children = this.parents;
        this.parents = newParent;
    }

    // goes thru the tree by level, top to bottom
    protected void traverseByLevel(Consumer<BinaryNode<T>> operation){
        // queue of nodes
        LinkedList<BinaryNode<T>> nodeQueue = new LinkedList<>();

        // init queue with this
        nodeQueue.add(this);

        // while the queue is not empty
        while (!nodeQueue.isEmpty()) {
            // next in line...
            BinaryNode<T> curr = nodeQueue.remove();

            // perform op on the next in line
            operation.accept(curr);

            // add all existing children in queue
            for(Direction dir : Direction.dirs){
                BinaryNode<T> nextChild = curr.getChild(dir);
                if (nextChild!=null) {
                    nodeQueue.add(nextChild);
                }
            }
        }
    }

    public void printByLevel(){
        HashMap<String,Integer> info = new HashMap<>();
        info.put("count", 0);
        info.put("currLvl", 0);
        traverseByLevel((BinaryNode<T> node) -> {
            System.out.print(node.key + " ");
            info.replace("count", info.get("count")+1);
            if (info.get("count")>=Math.pow(Direction.values().length, info.get("currLvl")+1)-1) {
                info.replace("currLvl", info.get("currLvl")+1);
                System.out.println();
            }
        });
    }

    // traverse. visit left descendants, then this, then right descendants.
    protected void traverseInOrder(Consumer<BinaryNode<T>> operation) {
        if (getChild(Direction.Left) != null) {
            getChild(Direction.Left).traverseInOrder(operation);
        }
        operation.accept(this);
        if (getChild(Direction.Right) != null) {
            getChild(Direction.Right).traverseInOrder(operation);
        }
    }

    // traverse: visit left descendants, then right descendants, then this.
    protected void traverseChildrenFirst(Consumer<BinaryNode<T>> operation) {
        if (getChild(Direction.Left) != null) {
            getChild(Direction.Left).traverseInOrder(operation);
        }
        if (getChild(Direction.Right) != null) {
            getChild(Direction.Right).traverseInOrder(operation);
        }
        operation.accept(this);
    }

    public void printInOrder() {
        traverseInOrder((BinaryNode<T> node) -> {
            System.out.println(node.key);
        });
    }

    public LinkedList<ArrayList<BinaryNode<T>>> getTreeMap(){
        // return var
        LinkedList<ArrayList<BinaryNode<T>>> map = new LinkedList<>();

        // add root to map
        ArrayList<BinaryNode<T>> rootList = new ArrayList<>(1);
        rootList.add(this);
        map.add(rootList);
        
        int level = 0;
        
        // for each level...
        while (true) {
            // get the current level
            ArrayList<BinaryNode<T>> currLevel = map.getLast();

            // prepare the next level
            ++level;
            map.add(new ArrayList<>((int)Math.pow(children.size(), level)));
            ArrayList<BinaryNode<T>> nextLevel = map.getLast();

            // assume all children are null until otherwise
            boolean currLvlAllNull = true;

            // for each node in the current level...
            // append all children to the next level
            // add two nulls if the current node is null
            for (BinaryNode<T> node : currLevel) {
                if (node == null) {
                    for (int j = 0; j < 2; j++) {
                        nextLevel.add(null);
                    }
                    continue;
                }
                currLvlAllNull=false;
                map.getLast().addAll(node.children);
            }

            // if the current level is all empty, remove the all node levels, return
            if(currLvlAllNull){
                for (int i = 0; i < 2; i++) {
                    map.removeLast();
                }
                return map;
            }
        }
    }

    public LinkedList<ArrayList<BinaryNode<T>>> getTreeMapReverse(){
        // return var
        LinkedList<ArrayList<BinaryNode<T>>> map = new LinkedList<>();

        // add root to map
        ArrayList<BinaryNode<T>> rootList = new ArrayList<>(1);
        rootList.add(this);
        map.add(rootList);
        
        int level = 0;
        
        // for each level...
        while (true) {
            // get the current level
            ArrayList<BinaryNode<T>> currLevel = map.getLast();

            // prepare the next level
            ++level;
            map.add(new ArrayList<>((int)Math.pow(parents.size(), level)));
            ArrayList<BinaryNode<T>> nextLevel = map.getLast();

            // assume all parents are null until otherwise
            boolean currLvlAllNull = true;

            // for each node in the current level...
            // append all parents to the next level
            // add two nulls if the current node is null
            for (BinaryNode<T> node : currLevel) {
                if (node == null) {
                    for (int j = 0; j < 2; j++) {
                        nextLevel.add(null);
                    }
                    continue;
                }
                currLvlAllNull=false;
                map.getLast().addAll(node.parents);
            }

            // if the current level is all empty, remove the all node levels, return
            if(currLvlAllNull){
                for (int i = 0; i < 2; i++) {
                    map.removeLast();
                }
                return map.reversed();
            }
        }
    }

    // generates a map from this as root down, then displays it.
    public void printMap(){
        ArrayList<ArrayList<BinaryNode<T>>> map = new ArrayList<>(this.getTreeMap());
        printMap(map);

    }

    // static variable that takes a map and displays it.
    public static <Type extends Comparable<Type>>void printMap(ArrayList<ArrayList<BinaryNode<Type>>> map){

        // how many spaces between each element on the last level.
        int numSpaces = 5;

        // prepare list of level displays
        String[] levelDispList = new String[map.size()];

        // for each level (going in reverse)
        for (int idx = map.size()-1; idx >= 0; --idx) {
            // get current level
            ArrayList<BinaryNode<Type>> level = map.get(idx);

            // prepare current level display (null -> "")
            levelDispList[idx]="";
            
            // indent the line
            for (int i = 0; i < numSpaces/2; i++) {
                levelDispList[idx]+=" ";
            }
            
            // for each node on level, print it and then spacing between
            for (var node : level) {
                if (node == null) {
                    // System.out.print("|");
                    levelDispList[idx]+="|";
                }else{
                    // System.out.print(node.key);
                    levelDispList[idx]+=node.key;
                }
                for (int i = 0; i < numSpaces; i++) {
                    // System.out.print(" ");
                    levelDispList[idx]+=" ";
                }
            }
            
            // each layer above is (2n)+1, n = spacing of layer below
            numSpaces*=2;
            numSpaces++;
        }

        for (var lvlStr : levelDispList) {
            System.out.println(lvlStr);
        }
    }

    public void FlipFromHere(){
        // swaps left and right
        BinaryNode<T> origLeft = getChild(Direction.Left);
        setChild(Direction.Left, getChild(Direction.Right));
        setChild(Direction.Right, origLeft);

        // do the same for all children
        for (var node : children) {
            if (node==null) {
                continue;
            }
            node.FlipFromHere();
        }
    }

    // bottom up, swap children and parents.
    public void FlipUpsideDown(){
        traverseChildrenFirst(
            (var node)-> {
                node.swapChildParent();
            }
        );
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
