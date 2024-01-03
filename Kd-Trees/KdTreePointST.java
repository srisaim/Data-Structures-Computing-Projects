import dsa.LinkedQueue;
import dsa.MaxPQ;
import dsa.Point2D;
import dsa.RectHV;
import stdlib.StdIn;
import stdlib.StdOut;

public class KdTreePointST<Value> implements PointST<Value> {
    private Node root; // Create Node root.
    private int n; // Create int n, number of nodes in tree.

    // Constructs an empty symbol table.
    public KdTreePointST() {
        this.root = null; // Initialize root to this.root = null.
        this.n = 0; // Initialize int n to this.n = 0.
    }

    // Returns true if this symbol table is empty, and false otherwise.
    public boolean isEmpty() {
        return this.root == null;
    }

    // Returns the number of key-value pairs in this symbol table.
    public int size() {
        return this.n;
    }

    // If p is null, then a NullPointerException() with a message is thrown.
    // If value is null, then a NullPointerException() with a message is thrown.
    // Inserts the given point and value into this symbol table.
    // Call the private put() method with appropriate arguments.
    public void put(Point2D p, Value value) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        RectHV r = new RectHV(0, 0, 1, 1);
        root = put(root, p, value, r, true);
    }

    // If p is null, then a NullPointerException() with a message is thrown.
    // Returns the value associated with the given point in this symbol table, or null.
    // Call the private get() method with appropriate arguments.
    public Value get(Point2D p) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        return get(root, p, true);
    }

    // If p is null, then a NullPointerException() with a message is thrown.
    // Returns true if this symbol table contains the given point, and false otherwise.
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        return get(p) != null;
    }

    // Create LinkedQueue queue1 and queue2 (2 Queues).
    // Using a while-loop, the loop will run until queue1 is empty.
    // At the end it returns all the points in this symbol table.
    public Iterable<Point2D> points() {
        LinkedQueue<Node> queue1 = new LinkedQueue<Node>();
        LinkedQueue<Point2D> queue2 = new LinkedQueue<Point2D>();
        queue1.enqueue(root);
        while (!queue1.isEmpty()) {
            Node n = queue1.dequeue();
            if (n.lb != null) {
                queue1.enqueue(n.lb);
            }
            if (n.rt != null) {
                queue1.enqueue(n.rt);
            }
            queue2.enqueue(n.p);
        }
        return queue2;
    }

    // If rect is null, then a NullPointerException() with a message is thrown.
    // Create a LinkedQueue<Point2D> queue, to use with private method range().
    // Returns all the points in this symbol table that are inside the given rectangle.
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException("rect is null");
        }
        LinkedQueue<Point2D> queue = new LinkedQueue<Point2D>();
        range(root, rect, queue);
        return queue;
    }

    // If p is null, then a NullPointerException() with a message is thrown.
    // Use the private method nearest() with appropriate arguments.
    // Returns the point in this symbol table that is different from and closest to the given point,
    // or null.
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException("p is null");
        }
        return nearest(root, p, root.p, true);
    }

    // Create MaxPQ<Point2D> maxpq to be passed in private method nearest().
    // Returns up to k points from this symbol table that are different from and closest to the
    // given point.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        MaxPQ<Point2D> maxpq = new MaxPQ<Point2D>(p.distanceToOrder());
        nearest(root, p, k, maxpq, true);
        return maxpq;
    }

    // Note: In the helper methods that have lr as a parameter, its value specifies how to
    // compare the point p with the point x.p. If true, the points are compared by their
    // x-coordinates; otherwise, the points are compared by their y-coordinates. If the
    // comparison of the coordinates (x or y) is true, the recursive call is made on x.lb;
    // otherwise, the call is made on x.rt.

    // If x is null, return new Node object.
    // If the point in x is the same as the given point, update the value in x.
    // Make recursive call to put() with appropriate arguments to insert given point
    // and value into left (x.lb) and right (x.rt) subtree. (Depends on how x.p and p compare)
    // Inserts the given point and value into the KdTree x having rect as its axis-aligned
    // rectangle, and returns a reference to the modified tree.
    private Node put(Node x, Point2D p, Value value, RectHV rect, boolean lr) {
        if (x == null) {
            return new Node(p, value, rect);
        }
        if (x.p.equals(p)) {
            return x;
        }

        boolean m;
        if (lr) {
            m = p.x() < x.p.x();
        } else {
            m = p.y() < x.p.y();
        }

        RectHV nr;
        if (m) {
            if (x.lb == null) {
                double x1 = rect.xMax(), y1 = rect.yMin(), x2, y2;
                if (lr) {
                    x2 = x.p.x();
                    y2 = rect.yMax();
                } else {
                    x2 = rect.xMax();
                    y2 = x.p.y();
                }
                nr = new RectHV(x1, y1, x2, y2);
            } else {
                nr = x.lb.rect;
            }
            x.lb = put(x.lb, p, value, nr, !lr);
        } else {
            if (x.rt == null) {
                double x1, y1;
                if (lr) {
                    x1 = x.p.x();
                    y1 = rect.yMin();
                } else {
                    x1 = rect.xMin();
                    y1 = x.p.y();
                }
                double x2 = rect.xMax(), y2 = rect.yMax();
                nr = new RectHV(x1, y1, x2, y2);
            } else {
                nr = x.rt.rect;
            }
            x.rt = put(x.rt, p, value, nr, !lr);
        }
        return x;
    }

    // If x is null, then return null.
    // If the point in x is same as given point, return value of x.
    // Make a call to get() with appropriate arguments to find value.
    // Returns the value associated with the given point in the KdTree x, or null.
    private Value get(Node x, Point2D p, boolean lr) {
        if (x == null) {
            return null;
        }
        if (x.p.equals(p)) {
            return x.value;
        } else if (lr && p.x() < x.p.x() || !lr && p.y() < x.p.y()) {
            return get(x.lb, p, !lr);
        }
        return get(x.rt, p, !lr);
    }

    // If x is null, then return null.
    // If rect contains point in x, enqueue the point into q.
    // Make calls to range() on left (x.lb) and right (x.rt) subtree.
    // Collects in the given queue all the points in the KdTree x that are inside rect.
    private void range(Node x, RectHV rect, LinkedQueue<Point2D> q) {
        if (x == null) {
            return;
        }
        if (rect.contains(x.p)) {
            q.enqueue(x.p);
        }
        if (x.lb != null && rect.intersects(x.lb.rect)) {
            range(x.lb, rect, q);
        }
        if (x.rt != null && rect.intersects(x.rt.rect)) {
            range(x.rt, rect, q);
        }
    }

    // If x is null, then return nearest.
    // Point2D near will equal nearest and near will then equal x.p.
    // Make calls to nearest() on left (x.lb) and right (x.rt) subtree.
    // Returns the point in the KdTree x that is closest to p, or null; nearest is the closest
    // point discovered so far.
    private Point2D nearest(Node x, Point2D p, Point2D nearest, boolean lr) {
        if (x == null) {
            return nearest;
        }
        Point2D near = nearest;
        near = x.p;
        if (lr) {
            if (x.p.x() < p.x()) {
                near = nearest(x.rt, p, near, !lr);
            }
            if (x.lb != null && near.distanceSquaredTo(p) > x.lb.rect.distanceSquaredTo(p)) {
                near = nearest(x.lb, p, near, !lr);
            } else {
                near = nearest(x.lb, p, near, !lr);
            }
            if (x.rt != null && near.distanceSquaredTo(p) > x.rt.rect.distanceSquaredTo(p)) {
                near = nearest(x.rt, p, near, !lr);
            } else {
                near = nearest(x.rt, p, near, !lr);
            }
        }
        return near;
    }

    // If x is null or the size of pq is greater than k, it will simply return.
    // Insert it into pq if the point x is different from given point.
    // If the size of pq exceeds k, remove the maximum point from the pq.
    // Call nearest() on the left and right subtree.
    // Collects in the given max-PQ up to k points from the KdTree x that are different from and
    // closest to p.
    private void nearest(Node x, Point2D p, int k, MaxPQ<Point2D> pq, boolean lr) {
        if (x == null || pq.size() > k) {
            return;
        }
        if (!x.p.equals(p)) {
            pq.insert(x.p);
        }
        if (pq.size() > k) {
            pq.delMax();
        }
        boolean lb;
        if (lr && p.x() < x.p.x() || !lr && p.y() < x.p.y()) {
            nearest(x.lb, p, k, pq, !lr);
            lb = true;
        } else {
            nearest(x.rt, p, k, pq, !lr);
            lb = false;
        }
        nearest(lb ? x.rt : x.lb, p, k, pq, !lr);
    }

    // A representation of node in a KdTree in two dimensions (ie, a 2dTree). Each node stores a
    // 2d point (the key), a value, an axis-aligned rectangle, and references to the left/bottom
    // and right/top subtrees.
    private class Node {
        private Point2D p;   // the point (key)
        private Value value; // the value
        private RectHV rect; // the axis-aligned rectangle
        private Node lb;     // the left/bottom subtree
        private Node rt;     // the right/top subtree

        // Constructs a node given the point (key), the associated value, and the
        // corresponding axis-aligned rectangle.
        Node(Point2D p, Value value, RectHV rect) {
            this.p = p;
            this.value = value;
            this.rect = rect;
        }
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        KdTreePointST<Integer> st = new KdTreePointST<>();
        double qx = Double.parseDouble(args[0]);
        double qy = Double.parseDouble(args[1]);
        int k = Integer.parseInt(args[2]);
        Point2D query = new Point2D(qx, qy);
        RectHV rect = new RectHV(-1, -1, 1, 1);
        int i = 0;
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            st.put(p, i++);
        }
        StdOut.println("st.empty()? " + st.isEmpty());
        StdOut.println("st.size() = " + st.size());
        StdOut.printf("st.contains(%s)? %s\n", query, st.contains(query));
        StdOut.printf("st.range(%s):\n", rect);
        for (Point2D p : st.range(rect)) {
            StdOut.println("  " + p);
        }
        StdOut.printf("st.nearest(%s) = %s\n", query, st.nearest(query));
        StdOut.printf("st.nearest(%s, %d):\n", query, k);
        for (Point2D p : st.nearest(query, k)) {
            StdOut.println("  " + p);
        }
    }
}
