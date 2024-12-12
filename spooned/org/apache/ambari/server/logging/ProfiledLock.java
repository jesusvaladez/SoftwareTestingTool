package org.apache.ambari.server.logging;
interface ProfiledLock extends java.util.concurrent.locks.Lock {
    boolean isHeldByCurrentThread();

    java.util.Map<java.lang.String, java.lang.Long> getTimeSpentWaitingForLock();

    java.util.Map<java.lang.String, java.lang.Long> getTimeSpentLocked();

    java.util.Map<java.lang.String, java.lang.Integer> getLockCount();

    java.lang.String getLabel();
}