import dsa.LinkedQueue;
import stdlib.StdOut;
import stdlib.StdRandom;

public class Sample {
    // Entry point.
    // Accept user-inputs as command-line arguments.
    public static void main(String[] args) {
        int lo = Integer.parseInt(args[0]); // Accept int lo.
        int hi = Integer.parseInt(args[1]); // Accept int hi.
        int k = Integer.parseInt(args[2]); // Accept int k.
        String mode = args[3]; // Accept mode String.

        // Create a queue q.
        LinkedQueue<Integer> q = new LinkedQueue<Integer>();

        // Queue q will contain random integers from the interval [lo, hi].
        // If mode is "+" sample and write k integers from q to standard output.
        // If mode is "-" dequeue and write k integers from q to standard output.
        // If mode is neither "+" or "-" then throw an IllegalArgumentException().
        if (mode.equals("+")) {
            for (int i = 0; i < k; i++) {
                int rand = StdRandom.uniform(lo, hi + 1);
                q.enqueue(rand);
            }
            for (int i: q) {
                StdOut.println(i);
            }
        } else if (mode.equals("-")) {
            while (q.size() != k) {
                int rand = StdRandom.uniform(lo, hi + 1);
                q.enqueue(rand);
            }
            while (q.size() != 0) {
                StdOut.println(q.dequeue());
            }
        } else {
            throw new IllegalArgumentException("Illegal mode");
        }
    }
}
