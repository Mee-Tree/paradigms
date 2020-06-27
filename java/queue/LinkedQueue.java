package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head;
    private Node tail;

    protected void enqueueImpl(Object element) {
        Node tmp = tail;
        tail = new Node(element, null);
        if (isEmpty()) {
            head = tail;
        } else {
            tmp.next = tail;
        }
    }

    protected void dequeueImpl() {
        head = head.next;
    }

    protected Object elementImpl() {
        return head.value;
    }

    protected void clearImpl() {
        head = tail = null;
    }

    protected Object[] toArrayImpl() {
    	Object[] arr = new Object[size()];
        for (int i = 0; i < size(); ++i) {
            arr[i] = dequeue();
            enqueue(arr[i]);
        }
        return arr;
    }

    private static class Node {
        private Object value;
        private Node next;

        public Node(Object value, Node next) {
            assert value != null;

            this.value = value;
            this.next = next;
        }
    }
}
