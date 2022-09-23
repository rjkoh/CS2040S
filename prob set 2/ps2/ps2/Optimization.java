/**
 * The Optimization class contains a static routine to find the maximum in an array that changes direction at most once.
 */
public class Optimization {

    /**
     * A set of test cases.
     */
    static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, -100, -80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83}
    };

    /**
     * Returns the maximum item in the specified array of integers which changes direction at most once.
     *
     * @param dataArray an array of integers which changes direction at most once.
     * @return the maximum item in data Array
     */
    public static int searchMax(int[] dataArray) {
        // TODO: Implement this
        //  O(Logn)
        int len = dataArray.length;
        if (len == 1) {
            return dataArray[0];
        } else if (len == 2 || dataArray[0] > dataArray[1]) {
            return dataArray[0] > dataArray[len-1] ? dataArray[0] : dataArray[len-1];
        } else {
            int start = 0;
            int end = len - 1;
            int mid = (start + end) / 2;
            while (start <= end) {
                if (start == end) {
                    return dataArray[start];
                } else if (end - start == 1) {
                    return dataArray[start] > dataArray[end] ? dataArray[start] : dataArray[end];
                } else if ((dataArray[mid - 1] < dataArray[mid]) && (dataArray[mid] > dataArray[mid + 1])) {
                    break;
                } else if (dataArray[mid - 1] > dataArray[mid]) {
                    end = mid;
                    mid = (start + end) / 2;
                } else {
                    start = mid;
                    mid = (start + end) / 2;
                }
            }
            return dataArray[mid];

        }

    }

    /**
     * A routine to test the searchMax routine.
     */
    public static void main(String[] args) {
        for (int[] testCase : testCases) {
            System.out.println(searchMax(testCase));
        }

    }
}
