package queue.test;

import queue.ArrayQueueADT;

public class ArrayQueueADTTest {
    public static void fill(ArrayQueueADT queue) {
        for (int i = 0; i < 10; i++) {
            ArrayQueueADT.enqueue(queue, i);
        }
    }

    public static void dump(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println(
                    ArrayQueueADT.size(queue) + " " +
                    ArrayQueueADT.element(queue) + " " +
                    ArrayQueueADT.dequeue(queue)
            );
        }
    }

    public static void clear(ArrayQueueADT queue) {
        ArrayQueueADT.clear(queue);
        System.out.println(ArrayQueueADT.isEmpty(queue));
    }

    public static void main(String[] args) {
        ArrayQueueADT queue = new ArrayQueueADT();
        fill(queue);
        dump(queue);
        fill(queue);
        clear(queue);
        fill(queue);
        dump(queue);
    }
}
