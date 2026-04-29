import java.util.ArrayList;
import java.util.List;

public class Processor {

    public static List<Integer> bubbleSort(List<Integer> data) {
        List<Integer> list = new ArrayList<>(data);
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j) > list.get(j + 1)) {
                    int temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
        return list;
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
