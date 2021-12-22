package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {

    public static void main(String[] args) {
        System.out.println("Timing table for Kd-Tree Construction");
        constructionTime(31250);
        System.out.println("Timing table for Naive Nearest");
        naiveNearestTime(125);
        System.out.println("Timing table for Kd-Tree Nearest");
        KDTNearestTime(31250);
        System.out.println("Test finding nearest point by Naive Nearest and Kd-Tree Nearest");
        findNearestP();

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

    private static void constructionTime(int baseSize) {
        List<Integer> Ns = new ArrayList<>();
        List<Double> times = new ArrayList<>();
        List<Integer> opCounts = new ArrayList<>();
        for(int i = 0; i < 7; i++) {
            int size = baseSize * (int)Math.pow(2, i);
            Ns.add(size);
            opCounts.add(size);
            times.add(constructKDTree(size));
        }
        printTimingTable(Ns, times, opCounts);
    }

    private static Double constructKDTree(int size) {
        Random ranXY = new Random();
        ArrayList<Point> KDTlist = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            KDTlist.add(new Point(ranXY.nextDouble(), ranXY.nextDouble()));
        }
        Stopwatch sw = new Stopwatch();
        KDTree tree = new KDTree(KDTlist);
        double timeInSeconds = sw.elapsedTime();
        return timeInSeconds;
    }

    private static void naiveNearestTime(int baseSize) {
        int ops = 1000000;
        List<Integer> Ns = new ArrayList<>();
        List<Double> times = new ArrayList<>();
        List<Integer> opCounts = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            int size = baseSize * (int)Math.pow(2, i);
            Ns.add(size);
            opCounts.add(ops);
            times.add(naiveNearest(size, ops));
        }
        printTimingTable(Ns, times, opCounts);
    }

    private static double naiveNearest(int size, int ops) {
        Random ranXY = new Random();
        ArrayList<Point> npsList = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            npsList.add(new Point(ranXY.nextDouble(), ranXY.nextDouble()));
        }
        NaivePointSet nps = new NaivePointSet(npsList);
        Stopwatch sw = new Stopwatch();
        for(int i = 0; i < ops; i++) {
            nps.nearest(ranXY.nextDouble(), ranXY.nextDouble());
        }
        double timeInSeconds = sw.elapsedTime();
        return timeInSeconds;
    }

    private static void KDTNearestTime(int baseSize) {
        int ops = 1000000;
        List<Integer> Ns = new ArrayList<>();
        List<Double> times = new ArrayList<>();
        List<Integer> opCounts = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            int size = baseSize * (int)Math.pow(2, i);
            Ns.add(size);
            opCounts.add(ops);
            times.add(KDTNearest(size, ops));
        }
        printTimingTable(Ns, times, opCounts);
    }

    private static double KDTNearest(int size, int ops) {
        Random ranXY = new Random();
        ArrayList<Point> KDTList = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            KDTList.add(new Point(ranXY.nextDouble(), ranXY.nextDouble()));
        }
        KDTree KDtree = new KDTree(KDTList);
        Stopwatch sw = new Stopwatch();
        for(int i = 0; i < ops; i++) {
            KDtree.nearest(ranXY.nextDouble(), ranXY.nextDouble());
        }
        double timeInSeconds = sw.elapsedTime();
        return timeInSeconds;
    }

    private static void findNearestP() {
        Random ranXY = new Random();
        ArrayList<Point> treeList = new ArrayList<>();
        for(int i = 0; i < 1000000; i++) {
            treeList.add(new Point(ranXY.nextDouble(),ranXY.nextDouble()));
        }
        KDTree treeTwo = new KDTree(treeList);
        NaivePointSet nps = new NaivePointSet(treeList);
        double goal_X = ranXY.nextDouble();
        double goal_Y = ranXY.nextDouble();
        Point goal = new Point(goal_X, goal_Y);
        Point nearestP1 = treeTwo.nearest(goal_X, goal_Y);
        Point nearestP2 = nps.nearest(goal_X, goal_Y);
        try {
            assertEquals(Point.distance(nearestP1, goal),
                    Point.distance(nearestP2, goal), 0.00000001);
            System.out.println("Success! The nearest point is: ");
            System.out.println("KDTree result: ");
            System.out.println(nearestP1);
            System.out.println("NaivePointSet result: ");
            System.out.println(nearestP2);
        }
        catch(AssertionError e) {
            System.out.println("Fail!");
            System.out.println("KDTree result: " + nearestP1);
            System.out.println("NaivePointSet result: " + nearestP2);
        }
    }
    /*
    private static void test1() {
        ArrayList<Point> treeList = new ArrayList<>();
        treeList.add(new Point(2,3));
        treeList.add(new Point(4,2));
        treeList.add(new Point(4,5));
        treeList.add(new Point(3,3));
        treeList.add(new Point(1, 5));
        treeList.add(new Point(4,4));
        KDTree treeOne = new KDTree(treeList);
        NaivePointSet nps = new NaivePointSet(treeList);
        Point nearestP1 = treeOne.nearest(0, 7);
        Point nearestP2 = nps.nearest(0, 7);
        assertEquals(Point.distance(nearestP1, new Point(0, 7)),
                Point.distance(nearestP2, new Point(0, 7)), 0);
        //System.out.println(nearestP1);
        //System.out.println(nearestP2);
    }

     */
}
