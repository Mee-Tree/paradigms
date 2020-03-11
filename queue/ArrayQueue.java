package queue;

import java.util.Arrays;

public class ArrayQueue extends AbstractQueue {
    private static final int SIZE = 8;

    private Object[] elements = new Object[SIZE];
    private int head;

    private void ensureCapacity(int capacity) {
        if (capacity == elements.length) {
            elements = Arrays.copyOf(toArray(), 2 * elements.length);
            head = 0;
        }
    }

    private int increase(int pos, int delta) {
        return (pos + delta) % elements.length;
    }

    protected void enqueueImpl(Object x) {
        ensureCapacity(size + 1);
        elements[increase(head, size)] = x;
    }

    protected void dequeueImpl() {
        elements[head] = null;
        head = increase(head, 1);
    }

    protected Object elementImpl() {
        return elements[head];
    }

    protected void clearImpl() {
        head = 0;
        elements = new Object[SIZE];
    }
}
