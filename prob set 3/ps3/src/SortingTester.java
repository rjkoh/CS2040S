public class SortingTester {
    public static boolean checkSort(ISort sorter, int size) {

        KeyValuePair[] testarr = new KeyValuePair[size];

        for (int i = 0; i < size; i++) {
            testarr[i] = new KeyValuePair((int) Math.floor(Math.random() * size + 1), i);
        }

        sorter.sort(testarr);

        for (int i = 0; i < size - 1; i++) {
            if (testarr[i].getKey() > testarr[i+1].getKey()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isStable(ISort sorter, int size) {

        KeyValuePair[] testarr = new KeyValuePair[size];

        for (int i = 0; i < size; i++) {
            testarr[i] = new KeyValuePair((int) Math.floor(Math.random() * size + 1/2), i);
        }

        sorter.sort(testarr);

        for (int i = 0; i < size - 1; i++) {
            if (testarr[i].getKey() == testarr[i+1].getKey() && testarr[i].getValue() > testarr[i+1].getValue()) {
                return false;
            }
        }
        return true;
    }

    public static double SortUnsorted(ISort sorter, int size) {

        StopWatch unsortedwatch = new StopWatch();

        KeyValuePair[] unsorted = new KeyValuePair[size];

        for (int i = 0; i < size; i++) {
            unsorted[i] = new KeyValuePair((int) Math.floor(Math.random() * size + 1), i);
        }

        unsortedwatch.start();
        sorter.sort(unsorted);
        unsortedwatch.stop();

        return unsortedwatch.getTime();
    }

    public static double SortSorted(ISort sorter, int size) {

        StopWatch sortedwatch = new StopWatch();

        KeyValuePair[] sorted = new KeyValuePair[size];

        for (int i = 0; i < size; i++) {
            sorted[i] = new KeyValuePair(i, i);
        }

        sortedwatch.start();
        sorter.sort(sorted);
        sortedwatch.stop();

        return sortedwatch.getTime();
    }

    public static double SortRevSorted(ISort sorter, int size) {

        StopWatch revsortedwatch = new StopWatch();

        KeyValuePair[] revsorted = new KeyValuePair[size];

        for (int i = 0; i < size; i++) {
            revsorted[i] = new KeyValuePair(size - i, i);
        }

        revsortedwatch.start();
        sorter.sort(revsorted);
        revsortedwatch.stop();

        return revsortedwatch.getTime();
    }

    public static double SortAlmSorted(ISort sorter, int size) {

        StopWatch almsortedwatch = new StopWatch();

        KeyValuePair[] almsorted = new KeyValuePair[size];

        for (int i = 0; i < size; i++) {
            almsorted[i] = new KeyValuePair(i, i);
        }
        almsorted[size - 1] = new KeyValuePair(0, size - 1);

        almsortedwatch.start();
        sorter.sort(almsorted);
        almsortedwatch.stop();

        return almsortedwatch.getTime();
    }

    public static void main(String[] args) {
        // TODO: implement this
        // Create a stopwatch

        StopWatch watch = new StopWatch();
        ISort sortingObject = new SorterC();
        int size10k = 10000;
        int size100k = 100000;


        // Do the sorting for array size 10000
        watch.start();
        System.out.println("CheckSort: " + checkSort(sortingObject, size100k));
        watch.stop();
        System.out.println("isStable: " + isStable(sortingObject, size100k));
        System.out.println("Time to sort random array of size 100k: " + watch.getTime());


        // 10k unsorted vs 100k unsorted
        double size10kunsorted = SortUnsorted(sortingObject, size10k);
        double size100kunsorted = SortUnsorted(sortingObject, size100k);
        System.out.println("ratio 100k unsorted / 10k unsorted: " + (size100kunsorted / size10kunsorted));



        // reverse sorted list vs sorted (100k)
        double sortedtime = SortSorted(sortingObject, size100k);
        double revsortedtime = SortRevSorted(sortingObject, size100k);
        System.out.println("ratio 100k reverse sorted / sorted: " + (revsortedtime / sortedtime));

        /* for comparing BubbleSort and InsertionSort
        double almsorted1k = SortAlmSorted(sortingObject, 1000);
        double almsorted10k = SortAlmSorted(sortingObject, 10000);
        System.out.println("Ratio almost sorted 10k / 1k: " + (almsorted10k / almsorted1k));
        */

        /* for bubblesort
        // Create a stopwatch
        StopWatch watch = new StopWatch();
        ISort sortingObject = new SorterC();
        int size1k = 1000;
        int size10k = 10000;

        // Do the sorting for array size 100
        watch.start();
        System.out.println("CheckSort: " + checkSort(sortingObject, size10k));
        watch.stop();
        System.out.println("isStable: " + isStable(sortingObject, size10k));
        System.out.println("Time to sort random array of size 10k: " + watch.getTime());


        // 1k unsorted vs 10k unsorted
        double size10kunsorted = SortUnsorted(sortingObject, size1k);
        double size100kunsorted = SortUnsorted(sortingObject, size10k);
        System.out.println("ratio 10k unsorted / 1k unsorted: " + (size10kunsorted / size10kunsorted));



        // reverse sorted list vs sorted (10k)
        double sortedtime = SortSorted(sortingObject, size10k);
        double revsortedtime = SortRevSorted(sortingObject, size10k);
        System.out.println("ratio 10k reverse sorted / sorted: " + (revsortedtime / sortedtime));
         */
    }
}
