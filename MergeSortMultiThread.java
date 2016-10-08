package mergesortmultithread;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * MergeSortMultiThread extends the RecursiveTask from the
 * Java concurrency library and implements a ForkJoinPool. The recursive merge
 * sort is still used, however, it is done by splitting index values and passing
 * them, along with arrays, to two Forked MergeSortMultiThread task.
 */
public class MergeSortMultiThread extends RecursiveTask<int[]> {

   private final int[] array;
   private final int[] temp;
   private final int start;
   private final int end;

   /*
    * Constructor: The MergeSortMultiThread accepts the current input array,
    * a temp array to assist in the sorting, and the start and end indexes
    * of the curent array.
    */
   MergeSortMultiThread(int[] array, int[] temp, int start, int end) {
      this.array = array;
      this.temp = temp;
      this.start = start;
      this.end = end;

   }

   /*
    * The abtract compute() method takes the place of the mergeSort() and
    * combineParts() methods of the single-threaded version of this program.
    * If the length of the array is above the threshold of the number of 
    * available processors, the halfway point is found, and recursive tasks
    * are instantiated. The two objects are passed to an invokeAll method, 
    * which Forks the given tasks. They are then Joined when the merge()
    * method is called to sort and combine the arrays.
    */
   @Override
   public int[] compute() {

      //If length of array is lower than the threshold of 10000.
      if (end - start <= 10000) {
         Arrays.sort(array);// ... then just do a sequential sort.
         return array;
      } else {
         int halfway = start + (end - start) / 2;
         MergeSortMultiThread ms1 = new MergeSortMultiThread(array, temp, start, halfway);
         MergeSortMultiThread ms2 = new MergeSortMultiThread(array, temp, halfway + 1, end);
         //Forks the given tasks, returning when isDone holds for each task. 
         ms1.fork();
         int[] comp = ms2.compute();

         //join() is passed to the mergeResults method, and it returns the result of the computation when it is done.
         mergeResults(ms1.join(), comp, this.start, halfway, this.end);
      }
      return array;
   }

   /*
    * mergeResults takes the input array, the temp array helper, and
    * the start, middle, and ending indexes.
    */
   private void mergeResults(int[] sortArray, int[] tempArray, int start, int middle, int ending) {
      for (int i = start; i <= ending; i++) {
         tempArray[i] = sortArray[i];
      }
      //sort
      int i = start, j = middle + 1;
      for (int k = start; k <= end; k++) {
         if (i > middle) {
            sortArray[k] = tempArray[j++];
         } else if (j > end) {
            sortArray[k] = tempArray[i++];
         } else if (tempArray[i] < tempArray[j]) {
            sortArray[k] = tempArray[i++];
         } else {
            sortArray[k] = tempArray[j++];
         }
      }

   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) throws FileNotFoundException {

      //Will be used to control the number of threads in the ForkJoinPool
      int forkTasks = Runtime.getRuntime().availableProcessors() + 1;
      double start;
      double end;

      start = 0.0;
      end = 0.0;
      //Get user input. How many numbers to sort?
      Scanner keyboard = new Scanner(System.in);
      System.out.println("How many numbers do you want to merge sort: ");
      int number = keyboard.nextInt();
      //Generate that many numbers within the range of 1 to 1,000,000,000.
      Random data = new java.util.Random();
      ArrayList<Integer> inputNumbers = new ArrayList<>();
      for (int i = 0; i < number; i++) {
         inputNumbers.add(data.nextInt(10000000));
      }
      //Convert ArrayList to array.
      int[] array = new int[inputNumbers.size()];
      for (int i = 0; i < array.length; i++) {
         array[i] = inputNumbers.get(i);
      }
      //instantiate the tempArray with the length of the input Array.
      int[] tempArray = new int[array.length];

      System.out.println("Input Array size: " + array.length);
      //Start and Stop timer and call sort method.

      start = System.nanoTime();
      ForkJoinPool forkJoin = new ForkJoinPool(forkTasks);
      MergeSortMultiThread ms = new MergeSortMultiThread(array, tempArray, 0, array.length - 1);
      forkJoin.execute(ms);
      int[] outputArray = ms.join();
      end = (System.nanoTime() - start) / 1000000;
      System.out.println((char) 27 + "[34m" + end
              + " milliseconds for recursive sort function and output in multi threaded program"
              + (char) 27 + "[0m");
      /*
       * Print sorted array to console. Larger numbers may take a while to print. 
       * Output was left out of the timer because I am printing at the very
       * end, and outputtng large numbers skewed the results and took away
       * from the value of seeing the time for just the sorting.
       */
      if (number >= 500000) {
         System.out.println("Printing that many numbers may take a while: ");
      }
      System.out.println(Arrays.toString(outputArray));


   }
}
