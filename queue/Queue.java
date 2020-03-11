package queue;

// inv: elements.length >= 0
//      forall i = 1..elements.length: elements[i] != null
public interface Queue {
    // pre: x != null
    // post: elements[size'] = x
    //       size' = size + 1
    //       forall i = 1..size: elements'[i] = elements[i]
    void enqueue(Object element);

    // pre: size > 0
    // post: R = elements[1]
    //       size' = size - 1
    //       forall i = 1..size': elements'[i] = elements[i + 1]
    Object dequeue();

    // pre: size > 0
    // post: R = elements[1]
    //       size' = size
    //       forall i = 1..size: elements'[i] = elements[i]
    Object element();

    // pre: size > 0
    // post: R = elements[1..size]
    Object[] toArray();

    // post: R = size
    //       size' = size
    //       forall i = 1..size: elements'[i] = elements[i]
    int size();

    // post: R = true,  size == 0
    //       R = false, size != 0
    //       size' = size
    //       forall i = 1..size: elements'[i] = elements[i]
    boolean isEmpty();

    // post: size' = 0
    void clear();
}
