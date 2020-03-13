package queue;

public class ArrayQueueModule {
    private static final int SIZE = 8;

    private static Object[] elements = new Object[SIZE];
    private static int head;
    private static int tail;
    private static int size;

    // post: size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    private static void ensureCapacity(int capacity) {
        if (capacity == elements.length) {
            elements = toArray(2 * elements.length);
            tail = size;
            head = 0;
        }
    }

    // post: size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    private static int increase(int pos) {
        return (pos + 1) % elements.length;
    }

    // pre: x != null
    // post: elements[size'] = x
    //       size = size' + 1
    //       forall i = 1..size': elements[i] = elements'[i]
    public static void enqueue(Object x) {
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
    public static Object element() {
        assert !isEmpty();

        return elements[head];
    }

    // pre: size > 0
    // post: R = elements[1]
    //       size = size' - 1
    //       forall i = 1..size: elements[i] = elements'[i + 1]
    public static Object dequeue() {
        assert !isEmpty();

        Object x = element();
        elements[head] = null;
        head = increase(head);
        --size;
        return x;
    }

    // post: R = size
    //       size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    public static int size() {
        return size;
    }

    // post: R = elements
    //       size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    private static Object[] toArray(int sz) {
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
    public static Object[] toArray() {
        return toArray(size);
    }

    // post: R = true,  size == 0
    //       R = false, size != 0
    //       size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    public static boolean isEmpty() {
        return size == 0;
    }

    // post: size = 0
    public static void clear() {
        size = head = tail = 0;
        elements = new Object[SIZE];
    }
}
