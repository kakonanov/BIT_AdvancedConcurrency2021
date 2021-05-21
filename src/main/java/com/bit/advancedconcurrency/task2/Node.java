package com.bit.advancedconcurrency.task2;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Node {
    private int key;
    private Node left;
    private Node right;
    private boolean isRouting;
    private boolean isDeleted = false;

    public Node(int key, Node left, Node right, boolean isRouting) {
        this.key = key;
        this.left = left;
        this.right = right;
        this.isRouting = isRouting;
    }

    public Node(int key, boolean isRouting) {
        this.key = key;
        this.left = null;
        this.right = null;
        this.isRouting = isRouting;
    }

    @Override
    public String toString() {
        return "Node{" +
                "key=" + key +
                ",isRouting=" + isRouting +
                '}';
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public boolean isRouting() {
        return isRouting;
    }

    public void setRouting(boolean routing) {
        isRouting = routing;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return key == node.key && isRouting == node.isRouting && isDeleted == node.isDeleted && Objects.equals(left, node.left) && Objects.equals(right, node.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, left, right, isRouting, isDeleted);
    }
}
