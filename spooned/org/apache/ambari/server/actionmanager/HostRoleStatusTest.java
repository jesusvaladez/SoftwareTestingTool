package org.apache.ambari.server.actionmanager;
public class HostRoleStatusTest {
    @org.junit.Test
    public void testIsFailedState() throws java.lang.Exception {
        org.junit.Assert.assertTrue(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED.isFailedState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.isFailedState());
        org.junit.Assert.assertTrue(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.isFailedState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.isFailedState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING.isFailedState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED.isFailedState());
        org.junit.Assert.assertTrue(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT.isFailedState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING.isFailedState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED.isFailedState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT.isFailedState());
    }

    @org.junit.Test
    public void testIsCompletedState() throws java.lang.Exception {
        org.junit.Assert.assertTrue(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED.isCompletedState());
        org.junit.Assert.assertTrue(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.isCompletedState());
        org.junit.Assert.assertTrue(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.isCompletedState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.isCompletedState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING.isCompletedState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED.isCompletedState());
        org.junit.Assert.assertTrue(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT.isCompletedState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING.isCompletedState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED.isCompletedState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT.isCompletedState());
    }

    @org.junit.Test
    public void testIsHoldingState() throws java.lang.Exception {
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED.isHoldingState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.isHoldingState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.isHoldingState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.isHoldingState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING.isHoldingState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED.isHoldingState());
        org.junit.Assert.assertFalse(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT.isHoldingState());
        org.junit.Assert.assertTrue(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING.isHoldingState());
        org.junit.Assert.assertTrue(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED.isHoldingState());
        org.junit.Assert.assertTrue(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT.isHoldingState());
    }
}