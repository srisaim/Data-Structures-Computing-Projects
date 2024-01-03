import java.util.Arrays;
import java.util.Comparator;

import stdlib.In;
import stdlib.StdOut;

public class Term implements Comparable<Term> {
    private String query; // Create Query string, String query.
    private long weight; // Create Query weight, long weight.

    // Constructs a term given the associated query string, having weight 0.
    // If query = null, then throw new NullPointerException.
    // Initialize instance variables.
    public Term(String query) {
        if (query == null) {
            throw new NullPointerException("Null query");
        }
        this.query = query;
        weight = 0;
    }

    // Constructs a term given the associated query string and weight.
    public Term(String query, long weight) {
        if (query == null) {
            throw new NullPointerException("Null query");
        } else if (weight < 0) {
            throw new IllegalArgumentException("Illegal weight");
        }
        this.query = query;
        this.weight = weight;
    }

    // Returns a string representation of this term.
    // Returns a string with weight and query separated by a tab.
    public String toString() {
        return weight + "\t" + query;
    }

    // Returns a comparison of this term and other by query.
    // Use compareTo() to compare this.query to other.query.
    public int compareTo(Term other) {
        return query.compareTo(other.query);
    }

    // Returns a comparator for comparing two terms in reverse order of their weights.
    public static Comparator<Term> byReverseWeightOrder() {
        return new ReverseWeightOrder();
    }

    // If r < 0, then throw a new IllegalArgumentException().
    // Returns a comparator for comparing two terms by their prefixes of length r.
    public static Comparator<Term> byPrefixOrder(int r) {
        if (r < 0) {
            throw new IllegalArgumentException("Illegal r");
        }
        return new PrefixOrder(r);
    }

    // Reverse-weight comparator.
    private static class ReverseWeightOrder implements Comparator<Term> {
        // Returns a comparison of terms v and w by their weights in reverse order.
        // Returns -, 0, or + if v.weight is <, >, or = w.weight.
        public int compare(Term v, Term w) {
            if (v.weight > w.weight) {
                return -1;
            } else if (v.weight < w.weight) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    // Prefix-order comparator.
    private static class PrefixOrder implements Comparator<Term> {
        private int r; // Create int r.

        // Constructs a new prefix order given the prefix length.
        // Initialize instance variables.
        PrefixOrder(int r) {
            this.r = r;
        }

        // Returns a comparison of terms v and w by their prefixes of length r.
        // Returns -, 0, or + based on a being <, >, or = to b.
        public int compare(Term v, Term w) {
            String a = v.query.substring(0, r);
            String b = w.query.substring(0, r);
            return a.compareTo(b);
        }
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
        StdOut.printf("Top %d by lexicographic order:\n", k);
        Arrays.sort(terms);
        for (int i = 0; i < k; i++) {
            StdOut.println(terms[i]);
        }
        StdOut.printf("Top %d by reverse-weight order:\n", k);
        Arrays.sort(terms, Term.byReverseWeightOrder());
        for (int i = 0; i < k; i++) {
            StdOut.println(terms[i]);
        }
    }
}
