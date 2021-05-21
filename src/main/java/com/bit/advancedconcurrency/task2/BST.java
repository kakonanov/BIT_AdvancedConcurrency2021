package com.bit.advancedconcurrency.task2;

import lombok.Getter;

import java.util.*;

@Getter
public class BST {
    private Node root;
    private Node rootPrev;
    private Node rootGprev;

    public BST(Node root) {
        this.root = root;
        rootPrev = new Node(-1, null, root, true);
        rootGprev = new Node(-2, null, rootPrev, true);
    }

    public Window search(int key) {
        Node gprev = rootGprev, prev = rootPrev, curr = root;
        while (curr != null && (curr.getKey() != key || curr.isRouting())) {
            gprev = prev;
            prev = curr;
            curr = key >= curr.getKey() ? curr.getRight() : curr.getLeft();
        }
        return new Window(gprev, prev, curr);
    }

    public boolean constains(int key) {
        Window window = search(key);
        return window.getCurr() != null;
    }

    public boolean insert(int key) {
        Window window;
        while (true) {
            window = search(key);
            synchronized (window.getGprev()) {
                Node gprev = window.getGprev();

                if (gprev.isDeleted())
                    continue;

                synchronized (window.getPrev()) {
                    Node prev = window.getPrev();

                    if (prev.isDeleted() || getSuitableChildren(gprev, prev.getKey()) != prev)
                        continue;

                    // работаем с curr, для вставки он должен быть null

                    Node curr = window.getCurr();
                    if (curr != null) {
                        synchronized (window.getCurr()) {
                            if (curr.isDeleted() || getSuitableChildren(prev, curr.getKey()) != curr)
                                continue;
                            else
                                return false;
                        }
                    }


                    int leftKey = Math.min(prev.getKey(), key);
                    int rightKey = Math.max(prev.getKey(), key);

                    Node newPrev = new Node(
                            rightKey,
                            new Node(leftKey, false),
                            new Node(rightKey, false),
                            true
                    );
                    replaceSuitableChildren(gprev, newPrev);
                    // надо ли?
                    prev.setDeleted(true);
                    return true;
                }
            }
        }
    }

    public boolean remove(int key) {
        Window window;
        while (true) {
            window = search(key);
            synchronized (window.getGprev()) {
                Node gprev = window.getGprev();

                if (gprev.isDeleted())
                    continue;

                synchronized (window.getPrev()) {
                    Node prev = window.getPrev();

                    if (prev.isDeleted() || getSuitableChildren(gprev, prev.getKey()) != prev)
                        continue;

                    // работаем с curr, для удаления он должен быть не null

                    Node curr = window.getCurr();

                    // такого ключа нет в дереве
                    if (curr == null) {
                        return false;
                    }

                    synchronized (window.getCurr()) {
                        if (curr.isDeleted() || getSuitableChildren(prev, curr.getKey()) != curr)
                            continue;

                        boolean currIsLeftChild = curr.getKey() < prev.getKey();
                        boolean prevIsLeftChild = prev.getKey() < gprev.getKey();

                        Node prevChild = currIsLeftChild ? prev.getRight() : prev.getLeft();
                        if (prevIsLeftChild)
                            gprev.setLeft(prevChild);
                        else
                            gprev.setRight(prevChild);

                        curr.setDeleted(true);
                        prev.setDeleted(true);
                        return true;
                    }

                }
            }
        }
    }

    public Set<Integer> traverse() {
        Set<Integer> result = new HashSet<>();
        Stack<Node> s = new Stack<>();
        s.push(root);
        while (!s.isEmpty()) {
            Node node = s.pop();
            if (!node.isRouting())
                result.add(node.getKey());
            if (node.getRight() != null)
                s.push(node.getRight());
            if (node.getLeft() != null)
                s.push(node.getLeft());
        }
        return result;
    }

    private Node getSuitableChildren(Node gprev, int key) {
        if (key >= gprev.getKey())
            return gprev.getRight();
        return gprev.getLeft();
    }

    private void replaceSuitableChildren(Node gprev, Node prev) {
        if (prev.getKey() >= gprev.getKey())
            gprev.setRight(prev);
        else
            gprev.setLeft(prev);
    }

    public void printTree() {
        printTree(root);
    }

    public void printTree(Node tmpRoot) {

        Queue<Node> currentLevel = new LinkedList<>();
        Queue<Node> nextLevel = new LinkedList<>();

        currentLevel.add(tmpRoot);

        while (!currentLevel.isEmpty()) {
            Iterator<Node> iter = currentLevel.iterator();
            while (iter.hasNext()) {
                Node currentNode = iter.next();
                if (currentNode.getLeft() != null) {
                    nextLevel.add(currentNode.getLeft());
                }
                if (currentNode.getRight() != null) {
                    nextLevel.add(currentNode.getRight());
                }
                System.out.print(currentNode.getKey() + " ");
            }
            System.out.println();
            currentLevel = nextLevel;
            nextLevel = new LinkedList<Node>();

        }

    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRootPrev() {
        return rootPrev;
    }

    public void setRootPrev(Node rootPrev) {
        this.rootPrev = rootPrev;
    }

    public Node getRootGprev() {
        return rootGprev;
    }

    public void setRootGprev(Node rootGprev) {
        this.rootGprev = rootGprev;
    }

}
