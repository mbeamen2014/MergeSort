package mergesort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Class Description: MergeSort is a single-threaded version
 * of the merge-sort algorithm. An array of integers will be
 */
public class MergeSort {

   private int[] tempArray;
   private int[] sortArray;

   /*
    * Default constructor
    */
   public MergeSort() {
   }

   /*
    * sort accepts the integer array containing the numbers that need to be sorted
    * and instantiates the temp array with the length of the original input array.
    * It then calls the mergeSort() method to begin the recursive merge sort algorithm
    * @param nums
    */
   public void sort(int[] nums) {
      int i = 0;
      this.sortArray = nums;
      this.tempArray = new int[sortArray.length];
      mergeSort(i, nums.length - 1);
   }

   /*
    * mergeSort accepts the first and last index from the input array..These
    * will be used to determine a halfway point so that the mergeSort can be split
    * into two recursive calls until the start index is no longer lower than the ending 
    * index.
    * @param start
    * @param end
    */
   private void mergeSort(int start, int end) {

      if (start < end) {

         //Index of the halfway point in the array.
         int halfway = start + (end - start) / 2;
         //Recurisve calls to merge sort, one for the first half of the array and one for the second half.
         mergeSort(start, halfway);
         mergeSort(halfway + 1, end);
         //Pass all three points to a method that will sort and combine the split arrays.
         combineParts(start, halfway, end);
      }

   }

   /*
    * combineParts will accept the beginning, middle, and ending indexes of the current
    * input array. It will populate the temp array and then sort the numbers
    * from the separate arrays
    */
   private void combineParts(int start, int middle, int end) {
      for (int i = start; i <= end; i++) {
         tempArray[i] = sortArray[i];
      }

      /*
       * Compare the start index in the left temp array to the starting index
       * of the right temp array (i.e., the midway point between the larger array).
       * If the left temp array value is lesser or equal to the right temp array value,
       * put the left value in first index of the sortArray. 
       * Else, put the value in the start of the right array in the sortArray.
       */
      int i = start, j = middle + 1, k = start;

      while (i <= middle && j <= end) {
         if (tempArray[i] <= tempArray[j]) {
            sortArray[k] = tempArray[i];
            i++;
         } else {
            sortArray[k] = tempArray[j];
            j++;
         }
         k++;
      }
      while (i <= middle) {
         sortArray[k] = tempArray[i];
         k++;
         i++;
      }
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {

     
         //Get user input. How many numbers to sort?
         Scanner keyboard = new Scanner(System.in);
         System.out.println("How many numbers do you want to merge sort: ");
         int number = keyboard.nextInt();
         //Generate that many nunbers from 1 to 1,000,000,000.
         Random data = new java.util.Random();
         ArrayList<Integer> inputNumbers = new ArrayList<>();
         for (int i = 0; i < number; i++) {
            inputNumbers.add(data.nextInt(10000000));
         }
         //Convert ArrayList to array.
         int[] array = new int[inputNumbers.size()];
         for (int j = 0; j < array.length; j++) {
            array[j] = inputNumbers.get(j);
         }

         System.out.println("Input Array size: " + array.length);
         //Start and Stop timer and call sort method.         
         double start = System.nanoTime();
         MergeSort ms = new MergeSort();
         ms.sort(array);
         double end = (System.nanoTime() - start) / 1000000;
         System.out.println((char)27 +  "[34m" + end + 
                            " milliseconds for recursive sort function and output in single threaded program"
                            + (char)27 + "[0m");                                    

         /*
          * Print sorted array to console. Larger numbers may take a while to print. 
          * Output was left out of the timer because I am printing at the very
          * end, and outputtng large numbers skewed the results and took away
          * from the value of seeing the time for just the sorting.
          */

            if (number >= 500000) {
               System.out.println("Printing that many numbers may take a while: ");
            }
            System.out.println(Arrays.toString(array));
   
   }
}
