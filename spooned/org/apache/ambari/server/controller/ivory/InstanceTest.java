package org.apache.ambari.server.controller.ivory;
public class InstanceTest {
    @org.junit.Test
    public void testGetFeedName() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.Instance instance = new org.apache.ambari.server.controller.ivory.Instance("Feed1", "Instance1", "SUBMITTED", "start", "end", "details", "log");
        org.junit.Assert.assertEquals("Feed1", instance.getFeedName());
    }

    @org.junit.Test
    public void testGetId() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.Instance instance = new org.apache.ambari.server.controller.ivory.Instance("Feed1", "Instance1", "SUBMITTED", "start", "end", "details", "log");
        org.junit.Assert.assertEquals("Instance1", instance.getId());
    }

    @org.junit.Test
    public void testGetStatus() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.Instance instance = new org.apache.ambari.server.controller.ivory.Instance("Feed1", "Instance1", "SUBMITTED", "start", "end", "details", "log");
        org.junit.Assert.assertEquals("SUBMITTED", instance.getStatus());
    }

    @org.junit.Test
    public void testGetStartTime() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.Instance instance = new org.apache.ambari.server.controller.ivory.Instance("Feed1", "Instance1", "SUBMITTED", "start", "end", "details", "log");
        org.junit.Assert.assertEquals("start", instance.getStartTime());
    }

    @org.junit.Test
    public void testGetEndTime() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.Instance instance = new org.apache.ambari.server.controller.ivory.Instance("Feed1", "Instance1", "SUBMITTED", "start", "end", "details", "log");
        org.junit.Assert.assertEquals("end", instance.getEndTime());
    }

    @org.junit.Test
    public void testGetDetails() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.Instance instance = new org.apache.ambari.server.controller.ivory.Instance("Feed1", "Instance1", "SUBMITTED", "start", "end", "details", "log");
        org.junit.Assert.assertEquals("details", instance.getDetails());
    }

    @org.junit.Test
    public void testGetLog() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.Instance instance = new org.apache.ambari.server.controller.ivory.Instance("Feed1", "Instance1", "SUBMITTED", "start", "end", "details", "log");
        org.junit.Assert.assertEquals("log", instance.getLog());
    }
}