import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
/**
   A class that implements the ADT maxheap by using an array.
 
   @author Frank M. Carrano
   @author Timothy M. Henry
   @version 5.0
*/
public final class MaxHeap<T extends Comparable<? super T>>
             implements MaxHeapInterface<T>
{
   private T[] heap;      // Array of heap entries; ignore heap[0]
   private int lastIndex; // Index of last entry and number of entries
   private boolean integrityOK = false;
   private int swapsdone; //holds how many swaps were done. added "swapsdone++" in the add method
	private static final int DEFAULT_CAPACITY = 25;
	private static final int MAX_CAPACITY = 10000;
   

   public static void main(String[] args) throws IOException
   {
      File sorted = new File("data_sorted.txt");
      Scanner sortedinput = new Scanner(sorted);

      File random = new File("data_random.txt");
      Scanner randinput = new Scanner(random);

      FileWriter outputfile = new FileWriter("output.txt", true);
      PrintWriter output = new PrintWriter(outputfile);

      MaxHeap<Integer> sortedheap = new MaxHeap<>();
      //MaxHeap<Integer> sortedheapop = new MaxHeap<>(); optimal method version
      MaxHeap<Integer> randheap = new MaxHeap<>();
      //MaxHeap<Integer> randheapop = new MaxHeap<>(); // optimal method version

      int sortedcount = 0;
      Integer[] testarray = new Integer[2];
      testarray[0] = 1;
      testarray[1] = 2;
      Integer sortedarray[] = new Integer[100];

      while (sortedinput.hasNext()){
         int intdata = Integer.parseInt(sortedinput.nextLine());
         sortedheap.add(intdata);
         sortedcount++;
         sortedarray[sortedcount - 1] = intdata;
         
      }


      //for (int i = 1; i <= sortedcount; i++){
         //int intdata = Integer.parseInt(sortedinput.nextLine());
         //sortedarray[i] = intdata;
      //}

      MaxHeap<Integer> sortedheapop = new MaxHeap<>(sortedarray); //optimal method version

      for (int i = 1; i <= 10; i++){
         //System.out.println(sortedheap.getData(i) + ", ");
         output.print(sortedheap.getData(i) + ", ");

         //System.out.println(sortedheapop.getData(i));

         //output.print(sortedheapop.getData(i) + ", ");
      }
      output.println("");
      output.println(sortedheap.swapsdone);

      output.println("");

      for (int i = 1; i <= 10; i++){
         //System.out.println(sortedheap.getData(i) + ", ");
         output.print(sortedheapop.getData(i) + ", ");

         //System.out.println(sortedheapop.getData(i));

         //output.print(sortedheapop.getData(i) + ", ");
      }

      output.println("");
      output.println(sortedheapop.swapsdone);
      output.close();


      /*
      for (int i = 1; i <= sortedheapop.getSize(); i++){
         System.out.print(sortedheapop.getData(i) + ", ");
      }
*/
      //PrintWriter outputFile = new PrintWriter("src/testing.txt");
   }
   
   public MaxHeap()
   {
      this(DEFAULT_CAPACITY); // Call next constructor
   } // end default constructor
   
   public MaxHeap(int initialCapacity)
   {
      // Is initialCapacity too small?
      if (initialCapacity < DEFAULT_CAPACITY)
         initialCapacity = DEFAULT_CAPACITY;
      else // Is initialCapacity too big?
         checkCapacity(initialCapacity);
      
      // The cast is safe because the new array contains null entries
      @SuppressWarnings("unchecked")
      T[] tempHeap = (T[])new Comparable[initialCapacity + 1];
      heap = tempHeap;
      lastIndex = 0;
      integrityOK = true;
   } // end constructor

   public MaxHeap(T[] entries)
   {
      this(entries.length); // Call other constructor
      lastIndex = entries.length;
      // Assertion: integrityOK = true

      // Copy given array to data field
      for (int index = 0; index < entries.length; index++)
         heap[index + 1] = entries[index];

      // Create heap
      for (int rootIndex = lastIndex / 2; rootIndex > 0; rootIndex--){
         reheap(rootIndex);
      }
         //reheap(rootIndex);
   } // end constructor

   public void add(T newEntry)
   {
      checkIntegrity();        // Ensure initialization of data fields
      int newIndex = lastIndex + 1;
      int parentIndex = newIndex / 2;
      while ( (parentIndex > 0) && newEntry.compareTo(heap[parentIndex]) > 0)
      {
       heap[newIndex] = heap[parentIndex];
         newIndex = parentIndex;
       parentIndex = newIndex / 2;
       swapsdone++;
      } // end while

      heap[newIndex] = newEntry;
      lastIndex++;
      ensureCapacity();
   } // end add

   public T removeMax()
   {
      checkIntegrity();             // Ensure initialization of data fields
      T root = null;

      if (!isEmpty())
      {
         root = heap[1];            // Return value
         heap[1] = heap[lastIndex]; // Form a semiheap
         lastIndex--;               // Decrease size
         reheap(1);                 // Transform to a heap
      } // end if

      return root;
   } // end removeMax

   public T getMax()
   {
		checkIntegrity();
      T root = null;
      if (!isEmpty())
         root = heap[1];
      return root;
   } // end getMax

   public boolean isEmpty()
   {
      return lastIndex < 1;
   } // end isEmpty

   public int getSize()
   {
      return lastIndex;
   } // end getSize

   public void clear()
   {
		checkIntegrity();
      while (lastIndex > -1)
      {
         heap[lastIndex] = null;
         lastIndex--;
      } // end while
      lastIndex = 0;
   } // end clear

   private void reheap(int rootIndex)
   {
      boolean done = false;
      T orphan = heap[rootIndex];
      int leftChildIndex = 2 * rootIndex;

      while (!done && (leftChildIndex <= lastIndex) )
      {
         int largerChildIndex = leftChildIndex; // Assume larger
         int rightChildIndex = leftChildIndex + 1;
         swapsdone++;

         if ( (rightChildIndex <= lastIndex) &&
               heap[rightChildIndex].compareTo(heap[largerChildIndex]) > 0)
         {
            largerChildIndex = rightChildIndex;
         } // end if

         if (orphan.compareTo(heap[largerChildIndex]) < 0)
         {
            heap[rootIndex] = heap[largerChildIndex];
            rootIndex = largerChildIndex;
            leftChildIndex = 2 * rootIndex;
         }
      else
         done = true;
   } // end while

   heap[rootIndex] = orphan;
   } // end reheap

   private void ensureCapacity(){
      if (lastIndex >= heap.length - 1)//If array is full, double its size
      {
         int newLength = 2 * heap.length;
         checkCapacity(newLength);
         heap = Arrays.copyOf(heap, newLength);
      }//end if
   }//end ensureCapacity

   private void checkCapacity(int capacity)
   {
      if (capacity > MAX_CAPACITY)
         throw new IllegalStateException("Attempt to create a MaxHeap whose " +
                                         "capacity exeeds allowed " +
                                         "maximum of " + MAX_CAPACITY);
   } // end checkCapacity

   private void checkIntegrity()
      {
         if (!integrityOK)
            throw new SecurityException("MaxHeap object is corrupt.");
      } // end checkIntegrity

   private T getData(int x)
   {
      return heap[x];
   }
   
   private int getSwaps(){
      return swapsdone;
   }
   
// Private methods
// . . .
} // end MaxHeap
