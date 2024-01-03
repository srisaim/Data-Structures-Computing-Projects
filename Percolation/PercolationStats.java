import stdlib.StdOut;
import stdlib.StdRandom;
import stdlib.StdStats;

public class PercolationStats {

    private int m; // Create Independent int m;
    private double[] x; // Create double[] x for m experiments.
    private Percolation per; // Create Percolation per.

    // Performs m independent experiments on an n x n percolation system.
    // Initialize instance variables.
    public PercolationStats(int n, int m) {
        x = new double[m];
        this.m = m;
        int count = 0;

        // If n is <= 0 and m <= 0, the constructor throws "Illegal n or m."
        if (n <= 0 || m <= 0) {
            throw new IllegalArgumentException("Illegal n or m");
        }

        // Choose a site (i, j) at random and open it if it isn't already.
        // Calculate percolation threshold and store in x[].
        for (int i = 0; i < m; i++) {
            count = 0;
            per = new UFPercolation(n);
            while (!per.percolates()) {
                int r1 = StdRandom.uniform(0, n);
                int r2 = StdRandom.uniform(0, n);
                if (!per.isOpen(r1, r2)) {
                    count++;
                    per.open(r1, r2);
                }
            }
            x[i] = count / (double) (n * n);
        }
    }

    // Returns sample mean of percolation threshold.
    public double mean() {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum = sum + x[i];
        }
        double mean = sum/m;
        return mean;
    }

    // Returns sample standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(x);
    }

    // Returns low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(m);
    }

    // Returns high endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(m);
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, m);
        StdOut.printf("Percolation threshold for a %d x %d system:\n", n, n);
        StdOut.printf("  Mean                = %.3f\n", stats.mean());
        StdOut.printf("  Standard deviation  = %.3f\n", stats.stddev());
        StdOut.printf("  Confidence interval = [%.3f, %.3f]\n", stats.confidenceLow(),
                stats.confidenceHigh());
    }
}