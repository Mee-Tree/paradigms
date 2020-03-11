package queue;

public abstract class AbstractQueue implements Queue {
    protected int size;

    public void enqueue(Object element) {
        assert element != null;

        enqueueImpl(element);
        size++;
    }

    protected abstract void enqueueImpl(Object element);

    public Object dequeue() {
        assert !isEmpty();

        Object result = element();
        dequeueImpl();
        size--;
        return result;
    }

    protected abstract void dequeueImpl();

    public Object element() {
        assert !isEmpty();

        return elementImpl();
    }

    protected abstract Object elementImpl();

    public void clear() {
        clearImpl();
        size = 0;
    }

    protected abstract void clearImpl();

    public Object[] toArray() {
        assert !isEmpty();

        Object[] arr = new Object[size()];
        for (int i = 0; i < size(); ++i) {
            arr[i] = dequeue();
            enqueue(arr[i]);
        }
        return arr;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
