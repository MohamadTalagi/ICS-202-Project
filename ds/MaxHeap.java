package kfupm.clinic.ds;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * MaxHeap starter.
 *
 * Day-1 safety rule:
 * - Constructors must NEVER throw, so the program can start.
 * - Operations may throw UnsupportedOperationException until students implement them;
 *   the command dispatcher will catch and print [NOT SUPPORTED] instead of crashing.
 */
public class MaxHeap<T> {

    protected final Comparator<T> comparator;
    protected final T[] heap;
    protected int size;

    public MaxHeap(Comparator<T> comparator) {
        if (comparator == null) throw new IllegalArgumentException("comparator is null");
        this.comparator = comparator;
        this.heap = (T[]) new Object[16]; // initial capacity
        this.size = 0;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public T peek() {
        if (isEmpty()) return null;
        return heap[0];
    }

    public void push(T item) {
        if (item == null) throw new IllegalArgumentException("item is null");
        if (size == heap.length) throw new IllegalStateException("heap is full");

        heap[size] = item;
        percolateUp(size);
        size++;
    }
    public T pop() {
        if (isEmpty()) return null;

        T max = heap[0];
        size--;

        heap[0] = heap[size];
        heap[size] = null;

        if (!isEmpty()) {
            percolateDown(0);
        }

        return max;
    }
    private void percolateUp(int idx) {
        boolean done = false;

        while (idx > 0 && !done) {
            int parent = (idx - 1) / 2;

            // if current child has higher priority than parent, swap
            if (comparator.compare(heap[idx], heap[parent]) > 0) {
                T temp = heap[idx];
                heap[idx] = heap[parent];
                heap[parent] = temp;

                idx = parent;
            } else {
                done = true;
            }
        }
    }
    private void percolateDown(int idx) {
        boolean done = false;

        while ((2 * idx + 1 < size) && !done) {
            int child = 2 * idx + 1; // left child

            // choose right child if it has higher priority
            if ((child + 1 < size) &&
                    comparator.compare(heap[child + 1], heap[child]) > 0) {
                child = child + 1;
            }

            // if child has higher priority than parent, swap
            if (comparator.compare(heap[child], heap[idx]) > 0) {
                T temp = heap[idx];
                heap[idx] = heap[child];
                heap[child] = temp;

                idx = child;
            } else {
                done = true;
            }
        }
    }

    /** Non-destructive view for printing. */
    public List<T> toListSnapshot() {
        List<T> snapshot = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            snapshot.add(heap[i]);
        }

        return snapshot;
    }

    //Extra Method Added to Help with the undo operation
    public void remove(T item) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(item)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            size = size - 1;
            heap[index] = heap[size];
            heap[size] = null;

            if (index < size) {
                percolateDown(index);
                percolateUp(index);
            }
        }
    }
}
