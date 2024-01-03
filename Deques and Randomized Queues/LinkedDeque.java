import java.util.Iterator;
import java.util.NoSuchElementException;

import stdlib.StdOut;
import stdlib.StdRandom;

// A data type to represent a double-ended queue (aka deque), implemented using a doubly-linked.
// list as the underlying data structure.
// Use a doubly-linked list Node to implement following API.
public class LinkedDeque<Item> implements Iterable<Item> {
    private int n; // Create size of deque int n.
    private Node first; // Create front of deque Node first.
    private Node last; // Create back of deque Node last.

    // Constructs an empty deque.
    // Initialize the variables to appropriate values.
    public LinkedDeque() {
        this.n = 0;
        this.first = null;
        this.last = null;
    }

    // Returns true if this deque is empty, and false otherwise.
    public boolean isEmpty() {
        return n == 0;
    }

    // Returns the number of items in this deque.
    public int size() {
        return n;
    }

    // Adds item to the front of this deque.
    // If item equals null, then throw a new NullPointerException().
    // Increments n by one after adding item to front of deque.
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        Node newFront = first;
        first = new Node();
        first.prev = null;
        first.item = item;
        if (isEmpty()) {
            first.next = null;
            last = first;
        } else {
            first.next = newFront;
            newFront.prev = first;
        }
        n++;
    }

    // Adds item to the back of this deque.
    // If item is null, then throw a new NullPointerException().
    // Increments n by one after adding item to back of deque.
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        Node newEnd = last;
        last = new Node();
        last.next = null;
        last.item = item;
        if (isEmpty()) {
            last.prev = null;
            first = last;
        } else {
            newEnd.next = last;
            last.prev = newEnd;
        }
        n++;
    }

    // Returns the item at the front of this deque.
    // If deque is empty, then throw a new NoSuchElementException().
    public Item peekFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        } else {
            return first.item;
        }
    }

    // Removes and returns the item at the front of this deque.
    // If deque is empty, then throw a new NoSuchElementException().
    // Decrements n by one at the end.
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        Node temp = first;
        if (first.next == null) {
            last = null;
        } else {
            first.next.prev = null;
        }
        first = first.next;
        n--;
        return temp.item;
    }

    // Returns the item at the back of this deque.
    // If deque is empty, then throw a new NoSuchElementException().
    public Item peekLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        } else {
            return last.item;
        }
    }

    // Removes and returns the item at the back of this deque.
    // If deque is empty, then throw a new NoSuchElementException().
    // Decrements n by one at the end.
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        } else {
            Node temp = last;
            if (last.prev == null) {
                first = null;
            } else {
                last.prev.next = null;
            }
            last = last.prev;
            n--;
            return temp.item;
        }
    }

    // Returns an iterator to iterate over the items in this deque from front to back.
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // Returns a string representation of this deque.
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item item : this) {
            sb.append(item);
            sb.append(", ");
        }
        return n > 0 ? "[" + sb.substring(0, sb.length() - 2) + "]" : "[]";
    }

    // A deque iterator.
    private class DequeIterator implements Iterator<Item> {
        private Node current; // Node current is reference to current node.

        // Constructs an iterator.
        // Initialize variables.
        public DequeIterator() {
            this.current = first;
        }

        // Returns true if there are more items to iterate, and false otherwise.
        public boolean hasNext() {
            return current != null;
        }

        // Returns the next item.
        // Next item is item in current, and advance current to next node.
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException("Iterator is empty");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        // Unsupported method.
        public void remove() {
            throw new UnsupportedOperationException("remove() is not supported");
        }
    }

    // A data type to represent a doubly-linked list. Each node in the list stores a generic item
    // and references to the next and previous nodes in the list.
    private class Node {
        private Item item;  // the item
        private Node next;  // the next node
        private Node prev;  // the previous node
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        LinkedDeque<Character> deque = new LinkedDeque<Character>();
        String quote = "There is grandeur in this view of life, with its several powers, having " +
                "been originally breathed into a few forms or into one; and that, whilst this " +
                "planet has gone cycling on according to the fixed law of gravity, from so simple" +
                " a beginning endless forms most beautiful and most wonderful have been, and are " +
                "being, evolved. ~ Charles Darwin, The Origin of Species";
        int r = StdRandom.uniform(0, quote.length());
        StdOut.println("Filling the deque...");
        for (int i = quote.substring(0, r).length() - 1; i >= 0; i--) {
            deque.addFirst(quote.charAt(i));
        }
        for (int i = 0; i < quote.substring(r).length(); i++) {
            deque.addLast(quote.charAt(r + i));
        }
        StdOut.printf("The deque (%d characters): ", deque.size());
        for (char c : deque) {
            StdOut.print(c);
        }
        StdOut.println();
        StdOut.println("Emptying the deque...");
        double s = StdRandom.uniform();
        for (int i = 0; i < quote.length(); i++) {
            if (StdRandom.bernoulli(s)) {
                deque.removeFirst();
            } else {
                deque.removeLast();
            }
        }
        StdOut.println("deque.isEmpty()? " + deque.isEmpty());
    }
}
