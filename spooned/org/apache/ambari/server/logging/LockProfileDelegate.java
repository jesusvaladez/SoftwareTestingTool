package org.apache.ambari.server.logging;
final class LockProfileDelegate {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.logging.LockProfileDelegate.class);

    private final java.lang.ThreadLocal<java.lang.Long> lockRequestTime = new java.lang.ThreadLocal<>();

    private final java.lang.ThreadLocal<java.lang.Long> lockAcquireTime = new java.lang.ThreadLocal<>();

    private final java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Long> timeSpentWaitingForLock = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Long> timeSpentLocked = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Integer> lockCount = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.lang.String label;

    private final org.apache.ambari.server.logging.ProfiledLock lock;

    private final com.google.common.base.Ticker ticker;

    LockProfileDelegate(com.google.common.base.Ticker ticker, java.lang.String label, org.apache.ambari.server.logging.ProfiledLock lock) {
        this.label = org.apache.ambari.server.logging.LockProfileDelegate.addSpacePostfixIfNeeded(label);
        this.lock = lock;
        this.ticker = ticker;
    }

    java.lang.String getLabel() {
        return label;
    }

    java.util.Map<java.lang.String, java.lang.Long> getTimeSpentWaitingForLock() {
        return new java.util.TreeMap<>(timeSpentWaitingForLock);
    }

    java.util.Map<java.lang.String, java.lang.Long> getTimeSpentLocked() {
        return new java.util.TreeMap<>(timeSpentLocked);
    }

    java.util.Map<java.lang.String, java.lang.Integer> getLockCount() {
        return new java.util.TreeMap<>(lockCount);
    }

    boolean logRequest() {
        boolean alreadyOwned = lock.isHeldByCurrentThread();
        if (!alreadyOwned) {
            if (org.apache.ambari.server.logging.LockProfileDelegate.LOG.isDebugEnabled()) {
                org.apache.ambari.server.logging.LockProfileDelegate.LOG.debug("{}request {} from {}", label, lock, org.apache.ambari.server.logging.LockProfileDelegate.getFilteredStackTrace());
            }
            lockRequestTime.set(ticker.read());
        }
        return alreadyOwned;
    }

    void logRequestCompleted(boolean alreadyOwned, boolean acquired) {
        if (!alreadyOwned) {
            if (acquired) {
                long elapsed = storeElapsedTime(lockRequestTime, timeSpentWaitingForLock);
                org.apache.ambari.server.logging.LockProfileDelegate.LOG.debug("{}acquired {} after {} ms", label, lock, elapsed);
                org.apache.ambari.server.logging.LockProfileDelegate.increment(lockCount);
                lockAcquireTime.set(ticker.read());
            } else {
                org.apache.ambari.server.logging.LockProfileDelegate.LOG.debug("{}failed to acquire {}", label, lock);
            }
        }
    }

    void logUnlock() {
        boolean released = !lock.isHeldByCurrentThread();
        if (released) {
            long elapsed = storeElapsedTime(lockAcquireTime, timeSpentLocked);
            if (org.apache.ambari.server.logging.LockProfileDelegate.LOG.isDebugEnabled()) {
                org.apache.ambari.server.logging.LockProfileDelegate.LOG.debug("{}released {} after {} ms", label, lock, elapsed);
            }
        }
    }

    private long storeElapsedTime(java.lang.ThreadLocal<java.lang.Long> startHolder, java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Long> map) {
        long end = ticker.read();
        long elapsed = java.lang.Long.MIN_VALUE;
        java.lang.Long start = startHolder.get();
        if ((start != null) && (start <= end)) {
            elapsed = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(end - start);
            java.lang.String name = java.lang.Thread.currentThread().getName();
            map.putIfAbsent(name, 0L);
            if (elapsed > 0) {
                map.put(name, map.get(name) + elapsed);
            }
        }
        startHolder.remove();
        return elapsed;
    }

    private static void increment(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Integer> map) {
        java.lang.String name = java.lang.Thread.currentThread().getName();
        map.putIfAbsent(name, 0);
        map.put(name, map.get(name) + 1);
    }

    private static java.lang.String addSpacePostfixIfNeeded(java.lang.String label) {
        if (label == null) {
            return "";
        }
        label = label.trim();
        return label.length() > 0 ? label + " " : label;
    }

    private static java.lang.String getFilteredStackTrace() {
        java.lang.StackTraceElement[] stackTrace = java.lang.Thread.currentThread().getStackTrace();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.StackTraceElement element : stackTrace) {
            java.lang.String className = element.getClassName();
            if (className.startsWith("org.apache.ambari") && (!className.startsWith("org.apache.ambari.server.logging"))) {
                sb.append(className).append("#").append(element.getMethodName()).append("(").append(element.getFileName()).append(":").append(element.getLineNumber()).append(");\t");
            }
        }
        return sb.toString();
    }
}