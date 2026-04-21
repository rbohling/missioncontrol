// ArrayList is a list that can grow and shrink as we add/remove items
import java.util.ArrayList;
// List is the general type that ArrayList implements — we use it as the variable type
import java.util.List;

// Processor contains all the "math" methods — sorting, searching, and statistics
// All methods are "static" which means we can call them without creating a Processor object
public class Processor {

    // bubbleSort sorts a list of integers from smallest to largest
    // It works by repeatedly comparing two neighbors and swapping them if they're out of order
    public static List<Integer> bubbleSort(List<Integer> data) {
        // Make a copy of the original list so we don't change the caller's data
        List<Integer> list = new ArrayList<>(data);
        // n is how many numbers are in the list
        int n = list.size();
        // Outer loop: each full pass moves the next-largest number to its correct spot
        for (int i = 0; i < n - 1; i++) {
            // Inner loop: compare each pair of neighbors in the unsorted section
            // After each outer pass, the last i elements are already in the right place
            for (int j = 0; j < n - i - 1; j++) {
                // If the left neighbor is bigger than the right neighbor, swap them
                if (list.get(j) > list.get(j + 1)) {
                    int temp = list.get(j);          // save the left value temporarily
                    list.set(j, list.get(j + 1));    // put the right value in the left spot
                    list.set(j + 1, temp);            // put the saved left value in the right spot
                }
            }
        }
        // Return the fully sorted list
        return list;
    }

    // linearSearch looks for a target number by checking every element one at a time
    // Works on any list — sorted or unsorted — but can be slow for large lists
    public static int linearSearch(List<Integer> data, int target) {
        // Go through every index from the start to the end
        for (int i = 0; i < data.size(); i++) {
            // If this element matches what we're looking for, return its position
            if (data.get(i) == target) {
                return i;
            }
        }
        // If we finished the loop without finding it, return -1 to signal "not found"
        return -1;
    }

    // binarySearch looks for a target in a SORTED list by repeatedly cutting the search area in half
    // Much faster than linear search for large lists — but the list MUST be sorted first
    public static int binarySearch(List<Integer> data, int target) {
        // left is the starting index of the area we're still searching
        int left = 0;
        // right is the ending index of the area we're still searching
        int right = data.size() - 1;

        // Keep searching as long as the search area has at least one element
        while (left <= right) {
            // Find the middle index of the current search area
            // We write it this way (instead of (left+right)/2) to avoid integer overflow
            int mid = left + (right - left) / 2;
            // Get the value at the middle position
            int value = data.get(mid);

            // If the middle value is exactly what we want, we're done — return its index
            if (value == target) {
                return mid;
            }

            // If the middle value is too small, the target must be in the right half
            if (value < target) {
                left = mid + 1;  // move the left boundary past the middle
            } else {
                // If the middle value is too big, the target must be in the left half
                right = mid - 1; // move the right boundary before the middle
            }
        }

        // If we exit the loop without finding the target, it's not in the list
        return -1;
    }

    // formatList turns a list of integers into a single string with spaces between numbers
    // For example: [3, 7, 2] becomes "3 7 2"
    public static String formatList(List<Integer> data) {
        // StringBuilder is like a String but lets us efficiently append pieces to it
        StringBuilder builder = new StringBuilder();
        // Loop through every element in the list
        for (int i = 0; i < data.size(); i++) {
            // Add the current number to our growing string
            builder.append(data.get(i));
            // Add a space after every number EXCEPT the last one
            if (i < data.size() - 1) {
                builder.append(" ");
            }
        }
        // Convert the StringBuilder into a regular String and return it
        return builder.toString();
    }

    // calculateStats computes count, min, max, and average of the dataset
    public static String calculateStats(List<Integer> data) {
        // count is simply how many numbers are in the list
        int count = data.size();
        // Start min and max as the first element — we'll update them as we scan the list
        int min = data.get(0);
        int max = data.get(0);
        // sum will accumulate the total of all numbers so we can compute the average
        int sum = 0;

        // Visit every number in the list
        for (int value : data) {
            // If this number is smaller than our current min, it becomes the new min
            if (value < min) {
                min = value;
            }
            // If this number is larger than our current max, it becomes the new max
            if (value > max) {
                max = value;
            }
            // Add this number to our running total
            sum += value;
        }

        // Divide sum by count to get the average
        // Casting sum to double first makes sure we get a decimal answer, not just a whole number
        double average = (double) sum / count;

        // Build and return a formatted multi-line string with all four statistics
        // %d means insert an integer, %.2f means insert a decimal rounded to 2 places
        // %n is a newline that works on any operating system
        return String.format(
                "Count = %d%nMin = %d%nMax = %d%nAverage = %.2f",
                count, min, max, average
        );
    }
}
