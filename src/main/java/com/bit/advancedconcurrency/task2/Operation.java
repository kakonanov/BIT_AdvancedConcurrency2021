package com.bit.advancedconcurrency.task2;


public class Operation {
    private final int operationId;
    private final long time;

    public Operation(int operationId, long time) {
        this.operationId = operationId;
        this.time = time;
    }

    public int getOperationId() {
        return operationId;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "operationId=" + operationId +
                ", time=" + time +
                '}';
    }
}
