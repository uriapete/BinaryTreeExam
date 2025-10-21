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

      System.out.println("\nNEXT - FLIP");
      intRoot.FlipFromHere();

      intRoot.printMap();
      
      intRoot.FlipUpsideDown();

      System.out.println("\nNEXT - UPSIDE DOWN");
      intRoot.printMapReverse();

      System.out.println("\nPROOF WE DIDN'T JUST REVERSE THE LEVELS IN DISPLAY ONLY");
      intRoot.printMap();
      System.out.println("As you can see, we tried to print the regular way from the former root, but stops as the root is now the grandest child.");

	}

}
