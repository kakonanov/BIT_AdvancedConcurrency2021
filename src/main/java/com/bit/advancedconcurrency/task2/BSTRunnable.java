package com.bit.advancedconcurrency.task2;

import java.util.List;
import java.util.Random;

public class BSTRunnable implements Runnable{
    private final BST bst;
    private final int size;
    private final int prepopulate;
    private final int x;
    private final long startTime;
    private final Random random = new Random();

    private int counter = 0;

    public BSTRunnable(BST bst, int size, int prepopulate, int x) {
        this.bst = bst;
        this.size = size;
        this.prepopulate = prepopulate;
        this.x = x;
        this.startTime = System.nanoTime();
    }

    @Override
    public void run() {
        while (System.nanoTime() - startTime < 5_000_000_000L) {
            int tryPrepopulate = random.nextInt(100);
            if (tryPrepopulate < prepopulate) {
                continue;
            }
            ++counter;
            int key = random.nextInt(size);
            int tryX = random.nextInt(100);
            if (tryX < x) {
                bst.insert(key);
            } else if (x <= tryX && tryX < 2 * x) {
                bst.remove(key);
            } else {
                bst.constains(key);
            }

        }
        System.out.println(Thread.currentThread().getId() + ": " + counter);
    }
}
