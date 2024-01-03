import stdlib.StdArrayIO;

public class Transpose {
    // Entry point. [DO NOT EDIT]
    public static void main(String[] args) {
        double[][] x = StdArrayIO.readDouble2D();
        StdArrayIO.print(transpose(x));
    }

    // Returns a new matrix that is the transpose of x.
    private static double[][] transpose(double[][] x) {
        // Create a new 2D matrix t (for transpose) with dimensions n x m, where m x n are the
        // dimensions of x.
        // m is length of row.
        // n = x[0].length which is length of a column.
        int m = x.length;
        int n = x[0].length;
        // Create new matrix t which is n x m.
        double[][] t = new double[n][m];

        // For each 0 <= i < m and 0 <= j < n, set t[j][i] to x[i][j].
        // Loop will create matrix t using matrix x with n x m.
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                t[j][i] = x[i][j];
            }
        }

        // Return t.
        return t;
    }
}
