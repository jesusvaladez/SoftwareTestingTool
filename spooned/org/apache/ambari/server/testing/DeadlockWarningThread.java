package org.apache.ambari.server.testing;
import org.apache.commons.lang.ArrayUtils;
public class DeadlockWarningThread extends java.lang.Thread {
    private final java.util.List<java.lang.String> errorMessages;

    private int MAX_STACK_DEPTH = 30;

    private int SLEEP_TIME_MS = 3000;

    private java.util.Collection<java.lang.Thread> monitoredThreads = null;

    private boolean deadlocked = false;

    private static final java.lang.management.ThreadMXBean mbean = java.lang.management.ManagementFactory.getThreadMXBean();

    public java.util.List<java.lang.String> getErrorMessages() {
        return errorMessages;
    }

    public boolean isDeadlocked() {
        return deadlocked;
    }

    public DeadlockWarningThread(java.util.Collection<java.lang.Thread> monitoredThreads, int maxStackDepth, int sleepTimeMS) {
        this.errorMessages = new java.util.ArrayList<>();
        this.monitoredThreads = monitoredThreads;
        this.MAX_STACK_DEPTH = maxStackDepth;
        this.SLEEP_TIME_MS = sleepTimeMS;
        start();
    }

    public DeadlockWarningThread(java.util.Collection<java.lang.Thread> monitoredThreads) {
        this(monitoredThreads, 30, 3000);
    }

    public java.lang.String getThreadsStacktraces(java.util.Collection<java.lang.Long> ids) {
        java.lang.StringBuilder errBuilder = new java.lang.StringBuilder();
        for (long id : ids) {
            java.lang.management.ThreadInfo ti = org.apache.ambari.server.testing.DeadlockWarningThread.mbean.getThreadInfo(id, MAX_STACK_DEPTH);
            errBuilder.append("Deadlocked Thread:\n").append("------------------\n").append(ti).append('\n');
            for (java.lang.StackTraceElement ste : ti.getStackTrace()) {
                errBuilder.append('\t').append(ste);
            }
            errBuilder.append('\n');
        }
        return errBuilder.toString();
    }

    @java.lang.Override
    public void run() {
        while (true) {
            try {
                java.lang.Thread.sleep(SLEEP_TIME_MS);
            } catch (java.lang.InterruptedException ex) {
            }
            long[] ids = org.apache.ambari.server.testing.DeadlockWarningThread.mbean.findMonitorDeadlockedThreads();
            java.lang.StringBuilder errBuilder = new java.lang.StringBuilder();
            if ((ids != null) && (ids.length > 0)) {
                errBuilder.append(getThreadsStacktraces(java.util.Arrays.asList(org.apache.commons.lang.ArrayUtils.toObject(ids))));
                errorMessages.add(errBuilder.toString());
                java.lang.System.out.append(errBuilder.toString());
                deadlocked = true;
                break;
            } else {
                boolean hasLive = false;
                boolean hasRunning = false;
                for (java.lang.Thread monTh : monitoredThreads) {
                    java.lang.Thread.State state = monTh.getState();
                    if ((state != java.lang.Thread.State.TERMINATED) && (state != java.lang.Thread.State.NEW)) {
                        hasLive = true;
                    }
                    if ((state == java.lang.Thread.State.RUNNABLE) || (state == java.lang.Thread.State.TIMED_WAITING)) {
                        hasRunning = true;
                        break;
                    }
                }
                if (!hasLive) {
                    deadlocked = false;
                    break;
                } else if (!hasRunning) {
                    java.util.List<java.lang.Long> tIds = new java.util.ArrayList<>();
                    for (java.lang.Thread monitoredThread : monitoredThreads) {
                        java.lang.Thread.State state = monitoredThread.getState();
                        if ((state == java.lang.Thread.State.WAITING) || (state == java.lang.Thread.State.BLOCKED)) {
                            tIds.add(monitoredThread.getId());
                        }
                    }
                    errBuilder.append(getThreadsStacktraces(tIds));
                    errorMessages.add(errBuilder.toString());
                    deadlocked = true;
                    break;
                }
            }
        } 
    }
}