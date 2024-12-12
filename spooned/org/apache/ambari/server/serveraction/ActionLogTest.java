package org.apache.ambari.server.serveraction;
public class ActionLogTest {
    @org.junit.Test
    public void testWriteStdErr() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.ActionLog actionLog = new org.apache.ambari.server.serveraction.ActionLog();
        actionLog.writeStdErr(null);
        junit.framework.Assert.assertEquals("", actionLog.getStdErr());
        junit.framework.Assert.assertEquals("", actionLog.getStdOut());
        actionLog.writeStdErr("This is a test message");
        junit.framework.Assert.assertNotNull(actionLog.getStdErr());
        junit.framework.Assert.assertTrue(actionLog.getStdErr().contains("This is a test message"));
        junit.framework.Assert.assertEquals("", actionLog.getStdOut());
    }

    @org.junit.Test
    public void testWriteStdOut() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.ActionLog actionLog = new org.apache.ambari.server.serveraction.ActionLog();
        actionLog.writeStdOut(null);
        junit.framework.Assert.assertEquals("", actionLog.getStdOut());
        junit.framework.Assert.assertEquals("", actionLog.getStdErr());
        actionLog.writeStdOut("This is a test message");
        junit.framework.Assert.assertNotNull(actionLog.getStdErr());
        junit.framework.Assert.assertTrue(actionLog.getStdOut().contains("This is a test message"));
        junit.framework.Assert.assertEquals("", actionLog.getStdErr());
    }
}