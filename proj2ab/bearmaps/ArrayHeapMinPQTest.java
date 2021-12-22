package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQTest {
    public static void main(String[] args) {
        //ArrayHeapMinPQ<Character> ex1 = example1();
        //ArrayHeapMinPQ<Integer> ex2 = example2(100);
        //ex2.printMinHeap();
        //addAllItems(minHeapList);
        //getSmallestTest(minHeapList);
        //removeSmallestTest(minHeapList, 10);
        //sizeTest(minHeapList);
        //changePriorityTest(ex1, 'k', 7);
        //changePriorityTest(ex1, 'y', 0);
        //addTest(minHeapList, 'a', 1.5);
        //addTest(minHeapList, 'd', 5.5);
        //containsTest(minHeapList, 'y');
        addTimeTable(1000000, 25000, 8);
    }

    private static ArrayHeapMinPQ<Character> example1() {
        ArrayHeapMinPQ<Character> minHeapList = new ArrayHeapMinPQ<>();
        minHeapList.add('b', 3);
        minHeapList.add('e', 1);
        minHeapList.add('g', 4);
        minHeapList.add('k', 0);
        minHeapList.add('p', 5);
        minHeapList.add('v', 2);
        minHeapList.add('y', 6);
        return minHeapList;
    }

    private static ArrayHeapMinPQ<Integer> example2(int size) {
        Random random = new Random();
        ArrayHeapMinPQ<Integer> minHeapList = new ArrayHeapMinPQ<>();
        for(int i = 0; i < size; i++) {
            minHeapList.add(i, random.nextDouble());
        }
        return minHeapList;
    }

    private static void printTimingTable(List<Integer> Ns, List<Double> times, List<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    private static void addTimeTable(int heapSize, int baseSize, int addTimes) {
        List<Integer> Ns = new ArrayList<>();
        List<Double> times = new ArrayList<>();
        List<Integer> opCounts = new ArrayList<>();
        for(int i = 0; i < addTimes; i++) {
            int size = baseSize * (int)Math.pow(2, i);
            Ns.add(size);
            opCounts.add(size);
            times.add(addTime(heapSize, size));
        }
        printTimingTable(Ns, times, opCounts);
    }

    private static double addTime(int size, int addTimes) {
        Random ranXY = new Random();
        ArrayHeapMinPQ<Integer> ex2 = example2(size);
        Stopwatch sw = new Stopwatch();
        for(int i = 0; i < addTimes; i++) {
            int nextItem = ranXY.nextInt();
            while(ex2.contains(nextItem)) {
                nextItem = ranXY.nextInt();
            }
            ex2.add(nextItem, ranXY.nextDouble());
        }
        double timeInSeconds = sw.elapsedTime();
        return timeInSeconds;
    }

    private static void getSmallestTest(ArrayHeapMinPQ<Character> minHeapList) {
        System.out.println("The smallest item in this min heap is " + minHeapList.getSmallest());
    }

    private static void removeSmallestTest(ArrayHeapMinPQ<Character> minHeapList, int times) {
        int size = minHeapList.size();
        System.out.println("Remove smallest item " + times + " times: ");
        for(int i = 0; i < Math.min(times, size); i++) {
            System.out.println(minHeapList.removeSmallest());
        }
    }

    private static void sizeTest(ArrayHeapMinPQ<Character> minHeapList) {
        System.out.println("The size of min heap is " + minHeapList.size());
    }

    private static void changePriorityTest(ArrayHeapMinPQ<Character> minHeapList, char item, double priority) {
        System.out.println("Changing item " + item + ", new priority is " + priority);
        minHeapList.changePriority(item, priority);
        System.out.println("After changing priority, list items:");
        minHeapList.printMinHeap();
    }

    private static void addTest(ArrayHeapMinPQ<Character> minHeapList, char item, double priority) {
        System.out.println("Add new item " + item + ", priority " + priority);
        minHeapList.add(item, priority);
        System.out.println("After add new item, list items:");
        minHeapList.printMinHeap();
    }

    private static void containsTest(ArrayHeapMinPQ<Character> minHeapList, char item) {
        System.out.println("Item " + item + " exists in this min heap?  " + minHeapList.contains(item));
    }
}
