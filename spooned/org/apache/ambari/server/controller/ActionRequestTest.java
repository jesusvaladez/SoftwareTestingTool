package org.apache.ambari.server.controller;
public class ActionRequestTest {
    @org.junit.Test
    public void testBasicGetAndSet() {
        org.apache.ambari.server.controller.ActionRequest adr1 = new org.apache.ambari.server.controller.ActionRequest("a1", "SYSTEM", "fileName", "HDFS", "DATANODE", "Desc1", "Any", "100");
        org.junit.Assert.assertEquals("a1", adr1.getActionName());
        org.junit.Assert.assertEquals("SYSTEM", adr1.getActionType());
        org.junit.Assert.assertEquals("fileName", adr1.getInputs());
        org.junit.Assert.assertEquals("HDFS", adr1.getTargetService());
        org.junit.Assert.assertEquals("DATANODE", adr1.getTargetComponent());
        org.junit.Assert.assertEquals("Desc1", adr1.getDescription());
        org.junit.Assert.assertEquals("Any", adr1.getTargetType());
        org.junit.Assert.assertEquals("100", adr1.getDefaultTimeout());
        adr1.setDescription("Desc2");
        adr1.setActionType("USER");
        org.junit.Assert.assertEquals("Desc2", adr1.getDescription());
        org.junit.Assert.assertEquals("USER", adr1.getActionType());
    }

    @org.junit.Test
    public void testToString() {
        org.apache.ambari.server.controller.ActionRequest r1 = org.apache.ambari.server.controller.ActionRequest.getAllRequest();
        r1.toString();
    }
}