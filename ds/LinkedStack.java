package kfupm.clinic.ds;

public class LinkedStack<T> {

    private static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
        }
    }

    private Node<T> top;
    private int size;

    public void push(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        Node<T> newNode = new Node<>(item);
        newNode.next = top;
        top = newNode;
        size = size + 1;
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        }

        T data = top.data;
        top = top.next;
        size = size - 1;
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return top.data;
    }

    public boolean isEmpty() {
        if (top == null) {
            return true;
        } else {
            return false;
        }
    }

    public int size() {
        return size;
    }
}