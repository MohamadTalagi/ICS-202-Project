package kfupm.clinic.ds;

import java.util.function.BiConsumer;

public class AVLTree<K extends Comparable<K>, V> {

    private class AVLNode {
        K key;
        V value;
        AVLNode left, right;
        int height;

        AVLNode(K key, V value) {
            this.key = key;
            this.value = value;

            this.height = 1;
        }
    }

    private AVLNode root;


    //Public Methods
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    public V get(K key) {
        AVLNode node = get(root, key);
        return node == null ? null : node.value;
    }

    public void remove(K key) {
        root = remove(root, key);
    }

    public void inOrder(BiConsumer<K, V> visitor) {
        inOrder(root, visitor);
    }

    public Entry<K, V> minEntry() {
        AVLNode minNode = min(root);
        if(minNode == null){
            return null;
        }
        else{
            return(new Entry<>(minNode.key,minNode.value));
        }
    }

    public record Entry<K, V>(K key, V value) {}

    //Private Helper Methods
    private int height(AVLNode currentNode) {
        if(currentNode == null){
            return 0;
        }
        else{
            return  currentNode.height;
        }
    }

    private int getBalance(AVLNode currentNode) {
        if(currentNode == null){
            return 0;
        }
        else{
            int rightHeight = height(currentNode.right);
            int leftHeight = height(currentNode.left);
            return(rightHeight-leftHeight);
        }
    }

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode z = x.right;
        x.right = y;
        y.left = z;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode z = y.left;
        y.left = x;
        x.right = z;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    private AVLNode put(AVLNode node, K key, V value) {
        if (node == null){
            return new AVLNode(key, value);
        }

        else {
            int comparisonResult = key.compareTo(node.key);
            if (comparisonResult < 0){
                node.left = put(node.left, key, value);
            }
            else if(comparisonResult > 0){
                node.right = put(node.right, key, value);
            }
            else{
                node.value = value;
                return(node);
            }
            node.height = 1 + Math.max(height(node.left), height(node.right));
            return balance(node);
        }
    }

    private AVLNode get(AVLNode x, K key) {
        if (x == null){
            return null;
        }
        int comparisonResult = key.compareTo(x.key);
        if(comparisonResult < 0){
            return get(x.left, key);
        }
        else if(comparisonResult > 0){
            return get(x.right, key);
        }
        else return x;
    }

    private AVLNode remove(AVLNode node, K key) {
        if (node == null){
            return null;
        }

        int comparisonResult = key.compareTo(node.key);
        if (comparisonResult < 0){
            node.left = remove(node.left, key);
        }
        else if (comparisonResult > 0){
            node.right = remove(node.right, key);
        }
        else {
            if (node.left == null || node.right == null) {
                if(node.left != null){
                    node = node.left;
                }
                else{
                    node = node.right;
                }
            }
            else {
                AVLNode temp = min(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = remove(node.right, temp.key);
            }
        }

        if (node == null) return null;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    private AVLNode balance(AVLNode n) {
        int balanceFactor = getBalance(n);

        // Right Heavy
        if (balanceFactor > 1) {
            if (getBalance(n.right) < 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }

        // Left Heavy
        if (balanceFactor < -1) {
            if (getBalance(n.left) > 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }

        return n;
    }

    private AVLNode min(AVLNode n) {
        if (n == null) return null;
        while (n.left != null) n = n.left;
        return n;
    }

    private void inOrder(AVLNode n, BiConsumer<K, V> visitor) {
        if (n == null) return;
        inOrder(n.left, visitor);
        visitor.accept(n.key, n.value);
        inOrder(n.right, visitor);
    }
}