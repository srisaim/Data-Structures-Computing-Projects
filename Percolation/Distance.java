import stdlib.StdArrayIO;
import stdlib.StdOut;

public class Distance {
    // Entry point. [DO NOT EDIT]
    public static void main(String[] args) {
        double[] x = StdArrayIO.readDouble1D();
        double[] y = StdArrayIO.readDouble1D();
        StdOut.println(distance(x, y));
    }

    // Returns the Euclidean distance between the position vectors x and y.
    private static double distance(double[] x, double[] y) {
        // Sum up the squares of (x[i] - y[i]), where 0 <= i < x.length, and return the square
        // root of the sum.
        // Range is 0 to x.length or y.length.
        // Return square root of sum using Math.sqrt().
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += (x[i] - y[i]) * (x[i] - y[i]);
        }
        return Math.sqrt(sum);
    }
}
