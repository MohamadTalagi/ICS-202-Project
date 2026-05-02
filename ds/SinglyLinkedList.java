package kfupm.clinic.ds;

import java.util.ArrayList;
import java.util.List;

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
        if (item == null) {
            throw new IllegalArgumentException("item is null");
        }

        Node<T> newNode = new Node<>(item);

        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    public void addFirst(T item) {
        if (item == null) {
            throw new IllegalArgumentException("item is null");
        }

        Node<T> newNode = new Node<>(item);

        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }

        size++;
    }

    public T removeFirst() {
        if (head == null) {
            return null;
        }

        T data = head.data;
        head = head.next;

        if (head == null) {
            tail = null;
        }

        size--;
        return data;
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

    public int size() {
        return size;
    }

    //Additional method to help in undo()
    public T removeLast() {
        if (head == null) {
            return null;
        }
        T data = tail.data;
        if (head == tail) {
            head = tail = null;
        } else {
            Node<T> current = head;
            while (current.next != tail) {
                current = current.next;
            }
            current.next = null;
            tail = current;
        }
        size--;
        return data;
    }
}