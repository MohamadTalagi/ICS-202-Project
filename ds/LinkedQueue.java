package kfupm.clinic.ds;

import java.util.List;
import java.util.ArrayList;

/** Students implement. */
public class LinkedQueue<T> {
    // we'll create our own implementation of Node DS
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> front;
    private Node<T> rear;


    public LinkedQueue() {
        front = null;
        rear = null;
    }

    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);

        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }

    }

    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }

        T value = front.data;
        front = front.next;

        if (front == null) {
            rear = null;
        }

        return value;
    }
    public boolean isEmpty() {
        return front == null;
    }

    /** Non-destructive view for printing. */
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node<T> current = front;

        while (current != null) {
            list.add(current.data);
            current = current.next;
        }

        return list;
    }
}
