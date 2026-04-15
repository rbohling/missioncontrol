import java.util.ArrayList;
import java.util.List;

public class DatasetProcessor {

    public static List<Integer> mergeSort(List<Integer> data) {
        if (data.size() <= 1) {
            return new ArrayList<>(data);
        }

        int mid = data.size() / 2;
        List<Integer> left = mergeSort(data.subList(0, mid));
        List<Integer> right = mergeSort(data.subList(mid, data.size()));

        return merge(left, right);
    }

    private static List<Integer> merge(List<Integer> left, List<Integer> right) {
        List<Integer> merged = new ArrayList<>();
        int i = 0;
        int j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i) <= right.get(j)) {
                merged.add(left.get(i));
                i++;
            } else {
                merged.add(right.get(j));
                j++;
            }
        }

        while (i < left.size()) {
            merged.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            merged.add(right.get(j));
            j++;
        }

        return merged;
    }

    public static int linearSearch(List<Integer> data, int target) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == target) {
                return i;
            }
        }
        return -1;
    }

    public static int binarySearch(List<Integer> data, int target) {
        int left = 0;
        int right = data.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int value = data.get(mid);

            if (value == target) {
                return mid;
            }

            if (value < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    public static String formatList(List<Integer> data) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            builder.append(data.get(i));
            if (i < data.size() - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    public static String calculateStats(List<Integer> data) {
        int count = data.size();
        int min = data.get(0);
        int max = data.get(0);
        int sum = 0;

        for (int value : data) {
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
            sum += value;
        }

        double average = (double) sum / count;

        return String.format(
                "Count = %d%nMin = %d%nMax = %d%nAverage = %.2f",
                count, min, max, average
        );
    }
}
