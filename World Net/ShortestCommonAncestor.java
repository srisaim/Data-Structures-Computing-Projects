import dsa.DiGraph;
import dsa.LinkedQueue;
import dsa.SeparateChainingHashST;
import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

public class ShortestCommonAncestor {
    private DiGraph G; // Construct a rooted DAG, DiGraph G.

    // If G is equal to null, throw a new NullPointerException() with a message.
    // Constructs a ShortestCommonAncestor object given a rooted DAG.
    public ShortestCommonAncestor(DiGraph G) {
        if (G == null) {
            throw new NullPointerException("G is null");
        }

        this.G = G; // Initialize instance variable.
    }

    // If v is < 0 or v >= the number of vertices in G, throw a new
    // IndexOutOfBoundsException() with a message.
    // If w is < 0 or w >= the number of vertices in G, throw a new
    // IndexOutOfBoundsException() with a message.
    // Returns length of the shortest ancestral path between vertices v and w.
    // Use ancestor(v, w) and distFrom() methods.
    public int length(int v, int w) {
        if (v < 0 || v > G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        if (w < 0 || w > G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }

        int anc = ancestor(v, w);
        return distFrom(v).get(anc) + distFrom(w).get(anc);
    }

    // If v is < 0 or v >= the number of vertices in G, throw a new
    // IndexOutOfBoundsException() with a message.
    // If w is < 0 or w >= the number of vertices in G, throw a new
    // IndexOutOfBoundsException() with a message.
    // Returns a shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) {
        if (v < 0 || v > G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        if (w < 0 || w > G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }

        SeparateChainingHashST<Integer, Integer> vDist = distFrom(v);
        SeparateChainingHashST<Integer, Integer> wDist = distFrom(w);

        int shortDist = Integer.MAX_VALUE; // Create int shortDist (shortest distance).
        int shortAnc = 0; // Create int shortAnc (shortest ancestor).

        for (int x : wDist.keys()) {
            if (vDist.contains(x)) {
                int d = vDist.get(x) + wDist.get(x); // Create int d (distance).

                if (d < shortDist) {
                    shortDist = d;
                    shortAnc = x;
                }
            }
        }

        return shortAnc;
    }

    // If A is equal to null, throw a new NullPointerException() with a message.
    // If B is equal to null, throw a new NullPointerException() with a message.
    // Use triad(A, B) and distFrom() methods to get length.
    // Returns length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null) {
            throw new NullPointerException("A is null");
        }
        if (B == null) {
            throw new NullPointerException("B is null");
        }

        int[] q = triad(A, B); // Create int[] q.
        int v = q[1]; // Create int v.
        int w = q[2]; // Create int w.

        SeparateChainingHashST<Integer, Integer> dist1 = distFrom(v);
        SeparateChainingHashST<Integer, Integer> dist2 = distFrom(w);

        // Length of the shortest ancestral path of A and B.
        int length = (dist1.get(q[0]) + dist2.get(q[0]));
        return length;
    }

    // If A is equal to null, throw a new NullPointerException() with a message.
    // If B is equal to null, throw a new NullPointerException() with a message.
    // Use triad(A, B) method to get shortest common ancestor of A and B.
    // Returns a shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null) {
            throw new NullPointerException("A is null");
        }
        if (B == null) {
            throw new NullPointerException("B is null");
        }

        int[] q = triad(A, B);
        return q[0];
    }

    // Create SeparateChainingHashST<Integer, Integer> map, for map of vertices.
    // Create LinkedQueue<Integer> q = new LinkedQueue<Integer>().
    // Using loops, computed using Breadth-First Search starting at v.
    // Returns a map of vertices reachable from v and their respective shortest distances from v.
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        SeparateChainingHashST<Integer, Integer> map =
                new SeparateChainingHashST<Integer, Integer>();
        LinkedQueue<Integer> q = new LinkedQueue<Integer>();

        map.put(v, 0);
        q.enqueue(v);

        while (!q.isEmpty()) {
            int a = q.dequeue();

            for (int b : G.adj(a)) {
                if (!map.contains(b)) {
                    map.put(b, map.get(a) + 1);
                    q.enqueue(b);
                }
            }
        }

        return map;
    }

    // Create and Initialize variables.
    // Returns an array consisting of a shortest common ancestor a of vertex subsets A and B,
    // vertex v from A, and vertex w from B such that the path v-a-w is the shortest ancestral
    // path of A and B.
    // Use length(int v, int w) and ancestor(int v, int w) methods.
    private int[] triad(Iterable<Integer> A, Iterable<Integer> B) {
        int shortDist = Integer.MAX_VALUE; // Create int shortDist (shortest distance).
        int shortAnc = 0; // Create int shortAnc (shortest ancestor).
        int v = 0; // Create int v.
        int w = 0; // Create int w.

        for (int a : A) {
            for (int b : B) {
                int d = length(a, b); // Create int d (distance).

                if (d < shortDist) {
                    shortDist = d;
                    shortAnc = ancestor(a, b);
                    v = a;
                    w = b;
                }
            }
        }

        // Return a 3-element array with shortest common ancestor, A(v), and B(w).
        return new int[]{shortAnc, v, w};
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        DiGraph G = new DiGraph(in);
        in.close();
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
