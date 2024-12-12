package org.apache.ambari.server.controller;
public class ActionResponseTest {
    @org.junit.Test
    public void testBasicGetAndSet() {
        org.apache.ambari.server.controller.ActionResponse r1 = new org.apache.ambari.server.controller.ActionResponse("a1", "SYSTEM", "fileName", "HDFS", "DATANODE", "Desc1", "Any", "100");
        org.junit.Assert.assertEquals("a1", r1.getActionName());
        org.junit.Assert.assertEquals("SYSTEM", r1.getActionType());
        org.junit.Assert.assertEquals("fileName", r1.getInputs());
        org.junit.Assert.assertEquals("HDFS", r1.getTargetService());
        org.junit.Assert.assertEquals("DATANODE", r1.getTargetComponent());
        org.junit.Assert.assertEquals("Desc1", r1.getDescription());
        org.junit.Assert.assertEquals("Any", r1.getTargetType());
        org.junit.Assert.assertEquals("100", r1.getDefaultTimeout());
    }

    @org.junit.Test
    public void testToString() {
        org.apache.ambari.server.controller.ActionResponse r = new org.apache.ambari.server.controller.ActionResponse(null, null, null, null, null, null, null, null);
        r.toString();
    }
}