package queue;

import java.util.Arrays;

public class ArrayQueueADT {
    private static final int SIZE = 8;

    private /*static*/ Object[] elements = new Object[SIZE];
    private /*static*/ int head;
    private /*static*/ int tail;
    private /*static*/ int size;

    // post: size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity == queue.elements.length) {
        	queue.elements = Arrays.copyOf(toArray(queue), 2 * queue.elements.length);
            queue.tail = queue.size;
            queue.head = 0;
        }
    }

    // post: size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    private static int increase(ArrayQueueADT queue, int pos) {
        return (pos + 1) % queue.elements.length;
    }

    // pre: x != null
    // post: queue.elements[queue.size] = x
    //       queue.size = queue.size' + 1
    //       forall i = 1..queue.size': queue.elements[i] = queue.elements'[i]
    public static void enqueue(ArrayQueueADT queue, Object x) {
        assert x != null;

        ensureCapacity(queue, queue.size + 1);
        queue.elements[queue.tail] = x;
        queue.tail = increase(queue, queue.tail);
        ++queue.size;
    }

    // pre: queue.size > 0
    // post: R = queue.elements[1]
    //       queue.size = queue.size'
    //       forall i = 1..queue.size: queue.elements[i] = queue.elements'[i]
    public static Object element(ArrayQueueADT queue) {
        assert !isEmpty(queue);

        return queue.elements[queue.head];
    }

    // pre: queue.size > 0
    // post: R = queue.elements[1]
    //       queue.size = queue.size' - 1
    //       forall i = 1..queue.size: queue.elements[i] = queue.elements'[i + 1]
    public static Object dequeue(ArrayQueueADT queue) {
        assert !isEmpty(queue);

        Object x = element(queue);
        queue.elements[queue.head] = null;
        queue.head = increase(queue, queue.head);
        --queue.size;
        return x;
    }

    // post: R = elements
    //       size = size'
    //       forall i = 1..size: elements[i] = elements'[i]
    public static Object[] toArray(ArrayQueueADT queue) {
        Object[] arr = new Object[queue.size];
        int tmp = queue.head > queue.tail ? queue.elements.length - queue.head : queue.size;
        System.arraycopy(queue.elements, queue.head, arr, 0, tmp);
        if (queue.head > queue.tail) {
            System.arraycopy(queue.elements, 0, arr, tmp, queue.tail);
        }

        return arr;
    }

    // post: R = queue.size
    //       queue.size = queue.size'
    //       forall i = 1..queue.size: queue.elements[i] = queue.elements'[i]
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // post: R = true,  queue.size == 0
    //       R = false, queue.size != 0
    //       queue.size = queue.size'
    //       forall i = 1..queue.size: queue.elements[i] = queue.elements'[i]
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // post: queue.size = 0
    public static void clear(ArrayQueueADT queue) {
        queue.head = 0;
        queue.tail = 0;
        queue.size = 0;
        queue.elements = new Object[SIZE];
    }
}
