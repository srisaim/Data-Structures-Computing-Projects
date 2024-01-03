import java.util.Arrays;
import java.util.Comparator;

import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

public class Autocomplete {
    private Term[] terms; // Create Array of terms, Term[] terms.

    // Constructs an autocomplete data structure from an array of terms.
    // If terms = null, then throw new IllegalArgumentException().
    // Initialize to a defensive copy of terms and then sort this.terms.
    public Autocomplete(Term[] terms) {
        if (terms == null) {
            throw new IllegalArgumentException("terms is null");
        }
        this.terms = new Term[terms.length];

        for (int i = 0; i < terms.length; i++) {
            if (terms[i] == null) {
                throw new IllegalArgumentException("terms is null");
            }
            this.terms[i] = terms[i];
        }
        Arrays.sort(this.terms);
    }

    // Returns all terms that start with prefix, in descending order of their weights.
    // If prefix = null, then throw new IllegalArgumentException().
    // Set Term p = new Term(prefix, 0).
    // Find index i, first term, that starts with prefix.
    // Find index j, last term, that starts with prefix.
    // Construct int n = (j - i + 1), number of terms in terms.
    // Construct an array matches with n elements from terms at index 1.
    // Sort matches in reverse order of weight and return.
    public Term[] allMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("prefix is null");
        }

        Term p = new Term(prefix, 0); // Term p is new Term(prefix, 0).
        int i = BinarySearchDeluxe.firstIndexOf(terms, p,
                Term.byPrefixOrder(prefix.length()));
        if (i < 0) {
            return new Term[0];
        }
        int j = BinarySearchDeluxe.lastIndexOf(terms, p,
                Term.byPrefixOrder(prefix.length()));

        int n = (j - i + 1); // Number of terms in terms.
        Term[] matches = new Term[n]; // Array matches with n elements.

        for (int x = 0; x < matches.length; x++) {
            matches[x] = terms[i++];
        }
        Arrays.sort(matches, Term.byReverseWeightOrder());
        return matches;
    }

    // Returns the number of terms that start with prefix.
    // If prefix = null, then throw new IllegalArgumentException().
    // Set Term p = new Term(prefix, 0).
    // Find index i and j, first and last terms, with prefix.
    // With i and j, compute number of terms with prefix, int n, and return n.
    public int numberOfMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("prefix is null");
        }
        if (prefix.length() == 0) {
            return terms.length;
        }

        Term p = new Term(prefix, 0); // Term p is new Term(prefix, 0).
        int i = BinarySearchDeluxe.firstIndexOf(terms, p,
                Term.byPrefixOrder(prefix.length()));
        if (i < 0) {
            return 0;
        }
        int j = BinarySearchDeluxe.lastIndexOf(terms, p,
                Term.byPrefixOrder(prefix.length()));

        int n = (j - i + 1); // Number of terms in terms.
        return n;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        int k = Integer.parseInt(args[1]);
        In in = new In(filename);
        int N = in.readInt();
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) {
            long weight = in.readLong();
            in.readChar();
            String query = in.readLine();
            terms[i] = new Term(query.trim(), weight);
        }
        Autocomplete autocomplete = new Autocomplete(terms);
        StdOut.print("Enter a prefix (or ctrl-d to quit): ");
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            String msg = " matches for \"" + prefix + "\", in descending order by weight:";
            if (results.length == 0) {
                msg = "No matches";
            } else if (results.length > k) {
                msg = "First " + k + msg;
            } else {
                msg = "All" + msg;
            }
            StdOut.printf("%s\n", msg);
            for (int i = 0; i < Math.min(k, results.length); i++) {
                StdOut.println("  " + results[i]);
            }
            StdOut.print("Enter a prefix (or ctrl-d to quit): ");
        }
    }
}
