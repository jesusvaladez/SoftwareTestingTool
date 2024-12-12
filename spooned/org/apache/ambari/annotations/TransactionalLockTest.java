package org.apache.ambari.annotations;
public class TransactionalLockTest {
    @org.junit.Test
    public void testLockAreaEnabled() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.put(org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_ENABLED.getKey(), "true");
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        org.apache.ambari.annotations.TransactionalLock.LockArea lockArea = org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE;
        lockArea.clearEnabled();
        junit.framework.Assert.assertTrue(lockArea.isEnabled(configuration));
    }

    @org.junit.Test
    public void testLockAreaEnabledDisabled() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.put(org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_ENABLED.getKey(), "false");
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        org.apache.ambari.annotations.TransactionalLock.LockArea lockArea = org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE;
        lockArea.clearEnabled();
        junit.framework.Assert.assertFalse(lockArea.isEnabled(configuration));
    }

    @org.junit.Test
    public void testAnnotationEquality() {
        java.util.HashSet<org.apache.ambari.annotations.TransactionalLock> annotations = new java.util.HashSet<>();
        int annotationsFound = 0;
        java.lang.reflect.Method[] methods = getClass().getDeclaredMethods();
        for (java.lang.reflect.Method method : methods) {
            org.apache.ambari.annotations.TransactionalLock annotation = method.getAnnotation(org.apache.ambari.annotations.TransactionalLock.class);
            if (null != annotation) {
                annotations.add(annotation);
                annotationsFound++;
            }
        }
        junit.framework.Assert.assertEquals(2, annotations.size());
        junit.framework.Assert.assertEquals(3, annotationsFound);
    }

    @org.apache.ambari.annotations.TransactionalLock(lockArea = org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE, lockType = org.apache.ambari.annotations.TransactionalLock.LockType.READ)
    private void transactionalHRCRead() {
    }

    @org.apache.ambari.annotations.TransactionalLock(lockArea = org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE, lockType = org.apache.ambari.annotations.TransactionalLock.LockType.READ)
    private void transactionalHRCRead2() {
    }

    @org.apache.ambari.annotations.TransactionalLock(lockArea = org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE, lockType = org.apache.ambari.annotations.TransactionalLock.LockType.WRITE)
    private void transactionalHRCWrite() {
    }
}