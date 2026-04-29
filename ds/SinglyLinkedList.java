package kfupm.clinic.ds;
import java.util.ArrayList;

import java.util.List;

/** Students implement. */
public class SinglyLinkedList<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void addLast(T item) {
        if (item == null) throw new IllegalArgumentException("item is null");

        Node<T> newNode = new Node<>(item);

        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    public List<T> toList() {
        List<T> list = new ArrayList<>();

        Node<T> current = head;

        while (current != null) {
            list.add(current.data);
            current = current.next;
        }

        return list;
    }
}