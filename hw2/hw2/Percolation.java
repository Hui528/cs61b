package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
   private Boolean[] openOrNot;
   private WeightedQuickUnionUF unionSets;
   private int numberOfOpenSites;
   private int size;

   public Percolation(int N)                // create N-by-N grid, with all sites initially blocked
   {
      if(N <= 0) {
         throw new IllegalArgumentException("N must be positive!");
      }
      openOrNot = new Boolean[N * N];
      for(int i = 0; i < N * N; i++) {
         openOrNot[i] = false;
      }
      unionSets = new WeightedQuickUnionUF(N * N);
      numberOfOpenSites = 0;
      size = N;
   }

   private int onedpos(int row, int col) {
      if(row < 0 || row > size - 1) {
         throw new IllegalArgumentException("row must be bigger than 0 and smaller than N.");
      }
      if(col < 0 || col > size - 1) {
         throw new IllegalArgumentException("col must be bigger than 0 and smaller than N.");
      }
      if(row == 0) {
         return col;
      }
      return row * size + col;
   }

   public void open(int row, int col)       // open the site (row, col) if it is not open already
   {
      if(isOpen(row, col) == false) {
         openOrNot[onedpos(row, col)] = true;
         numberOfOpenSites += 1;
      }
      if(row > 0) {
         if(isOpen(row - 1, col) == true) {
            unionSets.union(onedpos(row - 1, col), onedpos(row, col));
         }
      }
      if(row < size - 1) {
         if(isOpen(row + 1, col) == true) {
            unionSets.union(onedpos(row + 1, col), onedpos(row, col));
         }
      }
      if(col > 0) {
         if(isOpen(row, col - 1) == true) {
            unionSets.union(onedpos(row, col - 1), onedpos(row, col));
         }
      }
      if(col < size - 1) {
         if(isOpen(row, col + 1) == true) {
            unionSets.union(onedpos(row, col + 1), onedpos(row, col));
         }
      }
   }
   public boolean isOpen(int row, int col)  // is the site (row, col) open?
   {
      return openOrNot[onedpos(row, col)];
   }

   public boolean isFull(int row, int col)  // is the site (row, col) full?
   {
      if(row == 0) {
         if(isOpen(row, col) == true) {
            return true;
         }
      }
      else {
         for (int i = 0; i < size; i++) {
            if (unionSets.connected(onedpos(0, i), onedpos(row, col))) {
               return true;
            }
         }
      }
      return false;
   }

   public int numberOfOpenSites()           // number of open sites
   {
      return numberOfOpenSites;
   }

   public boolean percolates()              // does the system percolate?
   {
      for(int i = 0; i < size; i++) {
         if(isFull(size - 1, i)) {
            return true;
         }
      }
      return false;
   }

   public static void main(String[] args)   // use for unit testing (not required, but keep this here for the autograder)
   {

   }
}
