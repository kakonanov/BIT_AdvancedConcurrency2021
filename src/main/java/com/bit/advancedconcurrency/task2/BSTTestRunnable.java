package com.bit.advancedconcurrency.task2;

import java.util.List;
import java.util.Random;

public class BSTTestRunnable implements Runnable{
    private final BSTTestImpl bst;
    private final int size;
    private final int prepopulate;
    private final int x;
    private final long startTime;
    private final Random random = new Random();
    private final List<Operation> operations;

    public BSTTestRunnable(BSTTestImpl bst, int size, int prepopulate, int x, List<Operation> operations) {
        this.bst = bst;
        this.size = size;
        this.prepopulate = prepopulate;
        this.x = x;
        this.operations = operations;
        this.startTime = System.nanoTime();
    }

    @Override
    public void run() {
        while (System.nanoTime() - startTime < 5_000_000_00) {
            int tryPrepopulate = random.nextInt(100);
            if (tryPrepopulate < prepopulate) {
                continue;
            }
            int key = random.nextInt(size);
            int tryX = random.nextInt(100);
            if (tryX < x) {
                operations.add(new Operation(0, bst.insert(key)));
            } else if (x <= tryX && tryX < 2 * x) {
                operations.add(new Operation(1, bst.remove(key)));
            } else {
                operations.add(new Operation(2, bst.constains(key)));
            }

        }
    }
}
