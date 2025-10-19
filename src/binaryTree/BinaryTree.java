package binaryTree;

public class BinaryTree {

	public static void main(String[] args) {
      // 1. In the main file, create an instance of this class:
      BinaryNode<Integer> intRoot = new BinaryNode<>(4);
      
      intRoot.insertKey(2);
      intRoot.insertKey(7);
      intRoot.insertKey(1);
      intRoot.insertKey(3);
      intRoot.insertKey(6);
      intRoot.insertKey(9);
//      intRoot.insertKey(10);
//      intRoot.insertKey(11);

	  System.out.println();
      
      intRoot.printInOrder();
      System.out.println();
      System.out.println("NEXT");
      
      intRoot.printMap();

	}

}
