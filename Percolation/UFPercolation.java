import dsa.WeightedQuickUnionUF;
import stdlib.In;
import stdlib.StdOut;

// An implementation of the Percolation API using the UF data structure.
public class UFPercolation implements Percolation {

    private int n; // Create Percolation int n.
    private boolean[][] open; // Create Percolation boolean[][] open.
    private int openSites; // Create Percolation int openSites.

    private WeightedQuickUnionUF uf; // Create Union-find rep uf which is source.
    private WeightedQuickUnionUF uf2; // Create Union-find rep uf2 which is sink.

    // Constructs an n x n percolation system, with all sites blocked.
    public UFPercolation(int n) {
        this.n = n;
        this.openSites = openSites;
        open = new boolean[n][n];
        uf = new WeightedQuickUnionUF(n * n + 2);
        uf2 = new WeightedQuickUnionUF(n * n + 1);

        // Connects the source to the sink.
        for (int i = 0; i <= n - 1; i++) {
            uf.union(0, encode(0, i));
            uf.union((n * n) + 1, encode((n - 1), i));
        }
    }

    // Opens site (i, j) if it is not already open.
    // "Illegal i or j" will be thrown if i and j are out of bounds.
    public void open(int i, int j) {
        if (i < 0 || j < 0 && i >= n || j >= n) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }

        // Opens the site if it isn't open already and will increment openSites.
        if (!isOpen(i, j)) {
            open[i][j] = true;
            openSites++;
        }

        // This will check all the bounds north, west, east, and south.
        // This will connect uf sites in first or last rows with source and sink.
        // If any neighbor sites are open, the uf site will also connect with it.
        if ((i - 1) >= 0 && (i - 1) < n && isOpen(i - 1, j)) {
            uf.union(encode(i, j), encode(i - 1, j));
            uf2.union(encode(i, j), encode(i - 1, j));
        }
        if ((i + 1) >= 0 && (i + 1) < n && isOpen(i + 1, j)) {
            uf.union(encode(i, j), encode(i + 1, j));
            uf2.union(encode(i, j), encode(i + 1, j));
        }
        if ((j - 1) >= 0 && (j - 1) < n && isOpen(i, j - 1)) {
            uf.union(encode(i, j), encode(i, j - 1));
            uf2.union(encode(i, j), encode(i, j - 1));
        }
        if ((j + 1) >= 0 && (j + 1) < n && isOpen(i, j + 1)) {
            uf.union(encode(i, j), encode(i, j + 1));
            uf2.union(encode(i, j), encode(i, j + 1));
        }
    }

    // Returns true if site (i, j) is open, and false otherwise.
    public boolean isOpen(int i, int j) {
        if (i < 0 || j < 0 && i >= n || j >= n) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        return open[i][j];
    }

    // Returns true if site (i, j) is full, and false otherwise.
    // Will check if site is full if it is open and is connected to source.
    public boolean isFull(int i, int j) {
        if (i < 0 || j < 0 && i >= n || j >= n) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        return uf.connected(encode(i, j), 0) && isOpen(i, j)
                && isOpen(i, j) && uf2.connected(encode((n - 1), j), 0);
    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Returns true if this system percolates, and false otherwise.
    // Percolates if sink is connected to source.
    public boolean percolates() {
        return uf.connected((n * n) + 1, 0);
    }

    // Returns an integer ID (1...n) for site (i, j).
    private int encode(int i, int j) {
        int num = (n * i) + 1 + j;
        return num;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        UFPercolation perc = new UFPercolation(n);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.printf("%d x %d system:\n", n, n);
        StdOut.printf("  Open sites = %d\n", perc.numberOfOpenSites());
        StdOut.printf("  Percolates = %b\n", perc.percolates());
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.printf("  isFull(%d, %d) = %b\n", i, j, perc.isFull(i, j));
        }
    }
}