package kfupm.clinic.ds;

import java.util.Objects;

public class HashTable<K, V> {

    private class Node {
        K key;
        V value;
        Node next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private Node[] table;
    private int size;
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    public HashTable() {
        this.table = new HashTable.Node[INITIAL_CAPACITY];
        this.size = 0;
    }

    private int hash(K key, int capacity) {
        if (key == null){
            return 0;
        }
        return((key.hashCode() & 0x7FFFFFFF) % capacity);
    }

    public void put(K key, V value) {
        if(key == null){
            throw new IllegalArgumentException("Key cannot be null");
        }
        if((double) size / table.length >= LOAD_FACTOR_THRESHOLD){
            resize();
        }

        int index = hash(key, table.length);
        Node head = table[index];

        Node current = head;
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node newNode = new Node(key, value);
        newNode.next = head;
        table[index] = newNode;
        size++;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node[] newTable = new HashTable.Node[newCapacity];

        for (int i = 0; i < table.length; i++) {
            Node current = table[i];
            while (current != null) {
                Node nextNode = current.next; // Save reference to the rest of the old chain

                int newIndex = hash(current.key, newCapacity);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = nextNode;
            }
        }
        this.table = newTable;
    }

    public V get(K key) {
        int index = hash(key, table.length);
        Node current = table[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public V remove(K key) {
        int index = hash(key, table.length);
        Node current = table[index];
        Node prev = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    public int size() {
        return size;
    }
}