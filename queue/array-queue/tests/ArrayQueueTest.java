package queue.test;

import queue.ArrayQueue;

public class ArrayQueueTest {
    public static void fill(ArrayQueue queue) {
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
    }

    public static void dump(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println(
                    queue.size() + " " +
                    queue.element() + " " +
                    queue.dequeue()
            );
        }
    }

    public static void clear(ArrayQueue queue) {
        queue.clear();
        System.out.println(queue.isEmpty());
    }

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        fill(queue);
        dump(queue);
        fill(queue);
        clear(queue);
        fill(queue);
        dump(queue);
    }
}
