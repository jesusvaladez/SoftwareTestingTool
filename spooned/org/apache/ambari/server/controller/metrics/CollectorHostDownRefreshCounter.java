package org.apache.ambari.server.controller.metrics;
public class CollectorHostDownRefreshCounter {
    private int collectorDownRefreshCounterLimit = 5;

    private java.util.concurrent.atomic.AtomicInteger collectorDownRefreshCounter = new java.util.concurrent.atomic.AtomicInteger(0);

    CollectorHostDownRefreshCounter(int counter) {
        this.collectorDownRefreshCounterLimit = counter;
    }

    public boolean testRefreshCounter() {
        collectorDownRefreshCounter.incrementAndGet();
        if (collectorDownRefreshCounter.get() == collectorDownRefreshCounterLimit) {
            collectorDownRefreshCounter = new java.util.concurrent.atomic.AtomicInteger(0);
            return true;
        }
        return false;
    }
}