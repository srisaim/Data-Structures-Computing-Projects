import dsa.WeightedQuickUnionUF;
import stdlib.In;
import stdlib.StdOut;

// An implementation of the Percolation API using a 2D array.
public class ArrayPercolation implements Percolation {

    private int n; // Create Percolation int n.
    private boolean[][] open; // Create Percolation boolean[][] open.
    private boolean[][] full; // Create Percolation boolean[][] full.
    private int openSites; // Create Percolation int openSites.

    // Constructs an n x n percolation system, with all sites blocked.
    // Initializes instance variables.
    public ArrayPercolation(int n) {
        this.n = n;
        open = new boolean[n][n];
        this.openSites = openSites;
    }

    // Opens site (i, j) if it is not already open.
    public void open(int i, int j) {

        // If i and j are out of bounds, it throws "Illegal i or j."
        if (i < 0 || j < 0 && i >= n || j >= n) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        // If the site isn't open already, it will open and increment openSites.
        if (!isOpen(i, j)) {
            open[i][j] = true;
            openSites++;
        }
    }

    // Returns true if site (i, j) is open, and false otherwise.
    public boolean isOpen(int i, int j) {

        // Throws "Illegal i or j," if out of bounds.
        if (i < 0 || j < 0 && i >= n || j >= n) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        return open[i][j];
    }

    // Returns true if site (i, j) is full, and false otherwise.
    // Creates n x n array of booleans called full.
    // Calls floodFill() on every site of first row.
    // Returns full[i][j].
    public boolean isFull(int i, int j) {
        if (i < 0 || j < 0 && i >= n || j >= n) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        full = new boolean[i][j];
        for (int c = 0; c <= n - 1; c++) {
            floodFill(full, i, j);
        }
        return full[i][j];
    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Returns true if this system percolates, and false otherwise.
    public boolean percolates() {
        int n = 1;
        while (n != (open.length - 1)) {
            if (isFull(open.length - 1, n)) {
                return true;
            }
        }
        return false;
    }

    // Recursively flood fills full[][] using depth-first exploration, starting at (i, j).
    // If i or j are out of bounds, return.
    // If site (i, j) is not open or site (i, j) is full, return.
    private void floodFill(boolean[][] full, int i, int j) {
        if (i >= 1 && i <= n && j >= 1 && j <= n) {
            return;
        }
        if (!isOpen(i, j) || isFull(i, j)) {
            return;
        }

        // Calls floodFill() recursively on sites for north, east, west, south.
        full[i][j] = true;
        floodFill(full, i - 1, j);
        floodFill(full, i, j - 1);
        floodFill(full, i, j + 1);
        floodFill(full, i + 1, j);
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        ArrayPercolation perc = new ArrayPercolation(n);
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