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
      //setting up files to be read
      File sorted = new File("data_sorted.txt");
      Scanner sortedinput = new Scanner(sorted);

      File random = new File("data_random.txt");
      Scanner randinput = new Scanner(random);
      //making output file
      FileWriter outputfile = new FileWriter("output.txt", true);
      PrintWriter output = new PrintWriter(outputfile);

      //setting up heaps to have data added
      MaxHeap<Integer> sortedheap = new MaxHeap<>();
      MaxHeap<Integer> randheap = new MaxHeap<>();

      //making array for optimal method
      int sortedcount = 0;
      Integer sortedarray[] = new Integer[100];

      //adding data into heap and array
      while (sortedinput.hasNext()){
         int intdata = Integer.parseInt(sortedinput.nextLine());
         sortedheap.add(intdata);
         sortedcount++;
         sortedarray[sortedcount - 1] = intdata;
      }

      //does the same thing from above but for the random input instead
      int randcount = 0;
      Integer randarray[] = new Integer[100];

      while (randinput.hasNext()){
         int randdata = Integer.parseInt(randinput.nextLine());
         randheap.add(randdata);
         randcount++;
         randarray[randcount - 1] = randdata;
      }

      MaxHeap<Integer> sortedheapop = new MaxHeap<>(sortedarray); //optimal method version
      MaxHeap<Integer> randheapop = new MaxHeap<>(randarray); //optimal method version

      //start of printing lines on output file
      output.println("sorted input: ");
      output.print("Heap built using sequential insertions: ");
      for (int i = 1; i <= 10; i++){
         if (i != 10){
            output.print(sortedheap.getData(i) + ", ");
         }
         else{
            output.print(sortedheap.getData(i) + ",...");
         }
      }
      output.println("\nNumber of swaps in the heap creation: " + sortedheap.swapsdone);
      //removing 10 elements
      for (int i= 1; i <= 10; i++){
         sortedheap.removeMax();
      }

      output.print("Heap after 10 removals: ");
      for (int i = 1; i <= 10; i++){
         if (i != 10){
            output.print(sortedheap.getData(i) + ", ");
         }
         else{
            output.print(sortedheap.getData(i) + ",...\n");
         }
      }
      output.println("");

      //sorted optimal method output
      output.print("Heap built using optimal method: ");
      for (int i = 1; i <= 10; i++){
         if (i != 10){
            output.print(sortedheapop.getData(i) + ", ");
         }
         else{
            output.print(sortedheapop.getData(i) + ",...");
         }
      }
      output.println("\nNumber of swaps in the heap creation: " + sortedheapop.swapsdone);
      //removing 10 elements
      for (int i= 1; i <= 10; i++){
         sortedheapop.removeMax();
      }

      output.print("Heap after 10 removals: ");
      for (int i = 1; i <= 10; i++){
         if (i != 10){
            output.print(sortedheapop.getData(i) + ", ");
         }
         else{
            output.print(sortedheapop.getData(i) + ",...\n");
         }
      }
      output.println("");

      //random input
      output.println("random input: ");
      output.print("Heap built using sequential insertions: ");
      for (int i = 1; i <= 10; i++){
         if (i != 10){
            output.print(randheap.getData(i) + ", ");
         }
         else{
            output.print(randheap.getData(i) + ",...");
         }
      }
      output.println("\nNumber of swaps in the heap creation: " + randheap.swapsdone);
      //removing 10 elements
      for (int i= 1; i <= 10; i++){
         randheap.removeMax();
      }

      output.print("Heap after 10 removals: ");
      for (int i = 1; i <= 10; i++){
         if (i != 10){
            output.print(randheap.getData(i) + ", ");
         }
         else{
            output.print(randheap.getData(i) + ",...\n");
         }
      }
      output.println("");

      //random input, optimal method
      output.print("Heap built using optimal method: ");
      for (int i = 1; i <= 10; i++){
         if (i != 10){
            output.print(randheapop.getData(i) + ", ");
         }
         else{
            output.print(randheapop.getData(i) + ",...");
         }
      }
      output.println("\nNumber of swaps in the heap creation: " + randheapop.swapsdone);
      //removing 10 elements
      for (int i= 1; i <= 10; i++){
         randheapop.removeMax();
      }

      output.print("Heap after 10 removals: ");
      for (int i = 1; i <= 10; i++){
         if (i != 10){
            output.print(randheapop.getData(i) + ", ");
         }
         else{
            output.print(randheapop.getData(i) + ",...\n");
         }
      }
      output.println("");

      output.close();
      sortedinput.close();
      randinput.close();
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

   //private methods
   private void reheap(int rootIndex)
   {
      boolean done = false;
      T orphan = heap[rootIndex];
      int leftChildIndex = 2 * rootIndex;

      while (!done && (leftChildIndex <= lastIndex) )
      {
         int largerChildIndex = leftChildIndex; // Assume larger
         int rightChildIndex = leftChildIndex + 1;

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
            swapsdone++;
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
   
// . . .
} // end MaxHeap
