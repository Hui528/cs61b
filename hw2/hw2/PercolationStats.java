package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
   int gridSize;
   int expNums;
   Percolation perc;
   double[] fractions;
   StdRandom next;
   StdStats ss;
   public PercolationStats(int N, int T, PercolationFactory pf)   // perform T independent experiments on an N-by-N grid
   {
      if(N <= 0) {
         throw new IllegalArgumentException("N must be positive!");
      }
      if(T <= 0) {
         throw new IllegalArgumentException("T must be positive!");
      }
      gridSize = N;
      expNums = T;
      fractions = new double[T];
      for(int i = 0; i < T; i++) {
         perc = pf.make(gridSize);
         while(!perc.percolates()) {
            int position = next.uniform(gridSize * gridSize);
            perc.open(position / gridSize, position % gridSize);
         }
         fractions[i] = perc.numberOfOpenSites() / (double) (gridSize * gridSize);
      }
   }
   public double mean()                                           // sample mean of percolation threshold
   {
      return ss.mean(fractions);
   }
   public double stddev()                                         // sample standard deviation of percolation threshold
   {
      return ss.stddev(fractions);
   }
   public double confidenceLow()                                  // low endpoint of 95% confidence interval
   {
      return mean() - (1.96 * stddev()) / Math.sqrt(expNums);
   }
   public double confidenceHigh()                                 // high endpoint of 95% confidence interval
   {
      return mean() + (1.96 * stddev()) / Math.sqrt(expNums);
   }
}
