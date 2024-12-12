package org.apache.ambari.server.controller.utilities;
public class ScalingThreadPoolExecutor extends java.util.concurrent.ThreadPoolExecutor {
    public ScalingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, java.util.concurrent.TimeUnit unit, int workerQueueSize) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new java.util.concurrent.LinkedBlockingQueue<>(workerQueueSize));
    }
}