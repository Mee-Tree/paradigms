package queue;

public class ArrayQueue {
    private static final int SIZE = 8;

    private Object[] elements = new Object[SIZE];
    private int head;
    private int tail;
    private int size;

    // post: size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    private void ensureCapacity(int capacity) {
        if (capacity == elements.length) {
            elements = toArray(2 * elements.length);
            tail = size;
            head = 0;
        }
    }

    // post: size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    private int increase(int pos) {
        return (pos + 1) % elements.length;
    }

    // pre: x != null
    // post: elements[size] = x
    //       size = size' + 1
    //       forall i = 1..size': elements[i] = elements'[i]
    public void enqueue(Object x) {
        assert x != null;

        ensureCapacity(size + 1);
        elements[tail] = x;
        tail = increase(tail);
        ++size;
    }


    // pre: size > 0
    // post: R = elements[1]
    //       size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    public Object element() {
        assert !isEmpty();

        return elements[head];
    }

    // pre: size > 0
    // post: R = elements[1]
    //       size = size' - 1
    //       forall i = 1..size: elements[i] = elements'[i + 1]
    public Object dequeue() {
        assert !isEmpty();

        Object x = element();
        elements[head] = null;
        head = increase(head);
        --size;
        return x;
    }

    // post: R = elements
    //       size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    private Object[] toArray(int sz) {
        Object[] arr = new Object[sz];
        int tmp = head > tail ? elements.length - head : size;
        System.arraycopy(elements, head, arr, 0, tmp);
        if (head > tail) {
            System.arraycopy(elements, 0, arr, tmp, tail);
        }

        return arr;
    }

    // post: R = elements
    //       size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    public Object[] toArray() {
        return toArray(size);
    }

    // post: R = size
    //       size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    public int size() {
        return size;
    }

    // post: R = true,  size == 0
    //       R = false, size != 0
    //       size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    public boolean isEmpty() {
        return size == 0;
    }

    // post: size = 0
    public void clear() {
        size = head = tail = 0;
        elements = new Object[SIZE];
    }
}
