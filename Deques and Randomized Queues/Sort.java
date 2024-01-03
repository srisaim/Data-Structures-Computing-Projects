import dsa.LinkedStack;

import stdlib.StdIn;
import stdlib.StdOut;

public class Sort {
    // Entry Point
    // Create a deque d.
    public static void main(String[] args) {
        LinkedDeque<String> d = new LinkedDeque<String>();

        // readAll() will read user input and store in String s.
        // String s will split and be stored in String[] w.
        String s = StdIn.readAll();
        String[] w = s.split("\\w+");
        int n = w.length; // Int n is the length of String[] w.

        // Construct a for-loop where i = 0, i < n for each word w.
        // Add w to the front of d if it is less than last word d.
        // Add w to the back of d if it is greater than the last word in d.
        for (int i = 0; i < n; i++) {
            if (d.size() == 0) {
                d.addFirst(w[i]);
            } else if (less(w[i], d.peekFirst())) {
                d.addFirst(w[i]);
            } else {
                // Otherwise remove words less than w from front and store
                // in temporary stack t. (Some reason "s" did not work)
                // Add w to front of d and also add from s to front of d.
                LinkedStack<String> t = new LinkedStack<String>();
                while (!less(w[i], d.peekLast())) {
                    t.push(d.pop());
                    if (d.size() == 0) {
                        break;
                    }
                }
                d.addFirst(w[i]);
                while (!t.isEmpty()) {
                    d.addFirst(t.pop());
                }
            }
        }
        // Write the words from d to standard output.
        StdOut.println(d);
    }

    // Returns true if v is less than w according to their lexicographic order, and false otherwise.
    private static boolean less(String v, String w) {
        return v.compareTo(w) < 0;
    }
}
