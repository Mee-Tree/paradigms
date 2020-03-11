package queue;

import java.util.List;
import java.util.stream.Stream;

import static queue.ArrayQueueToArrayTest.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ArrayQueueToArrayTest extends ArrayQueueTest<ToArrayQueue> {
    public ArrayQueueToArrayTest() {
        super(ToArrayQueue.class, ReferenceToArrayQueue::new);
    }

    public static void main(final String[] args) {
        new ArrayQueueToArrayTest().test();
    }

    @Override
    protected List<ToArrayQueue> linearTest(final ToArrayQueue queue) {
        queue.toArray();
        return List.of();
    }

    protected interface ToArrayQueue extends Queue {
        Object[] toArray();
    }

    protected static class ReferenceToArrayQueue extends ReferenceQueue implements ToArrayQueue {
        public ReferenceToArrayQueue(final Stream<Object> elements) {
            super(elements);
        }

        @Override
        public Object[] toArray() {
            return deque.toArray();
        }
    }
}