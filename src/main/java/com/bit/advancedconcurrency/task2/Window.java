package com.bit.advancedconcurrency.task2;

public class Window {
    private final Node gprev;
    private final Node prev;
    private final Node curr;

    public Window(Node gprev, Node prev, Node curr) {
        this.gprev = gprev;
        this.prev = prev;
        this.curr = curr;
    }

    public Node getGprev() {
        return gprev;
    }

    public Node getPrev() {
        return prev;
    }

    public Node getCurr() {
        return curr;
    }

    @Override
    public String toString() {
        return "Window{" +
                "\ngprev=" + gprev +
                ",\nprev=" + prev +
                ",\ncurr=" + curr +
                '}';
    }
}
