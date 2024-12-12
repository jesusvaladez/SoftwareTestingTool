package org.apache.ambari.server.controller.ivory;
public class FeedTest {
    @org.junit.Test
    public void testGetName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "st", "end", "l", "a", "target", "st", "end", "l", "a", props);
        org.junit.Assert.assertEquals("Feed1", feed.getName());
    }

    @org.junit.Test
    public void testGetDescription() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "desc", "s", "sch", "source", "st", "end", "l", "a", "target", "st", "end", "l", "a", props);
        org.junit.Assert.assertEquals("desc", feed.getDescription());
    }

    @org.junit.Test
    public void testGetStatus() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "WAITING", "sch", "source", "st", "end", "l", "a", "target", "st", "end", "l", "a", props);
        org.junit.Assert.assertEquals("WAITING", feed.getStatus());
    }

    @org.junit.Test
    public void testGetSchedule() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "WAITING", "frequency", "source", "st", "end", "l", "a", "target", "st", "end", "l", "a", props);
        org.junit.Assert.assertEquals("frequency", feed.getSchedule());
    }

    @org.junit.Test
    public void testGetSourceClusterName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "st", "end", "l", "a", "target", "st", "end", "l", "a", props);
        org.junit.Assert.assertEquals("source", feed.getSourceClusterName());
    }

    @org.junit.Test
    public void testGetSourceClusterStart() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "sst", "end", "l", "a", "target", "st", "end", "l", "a", props);
        org.junit.Assert.assertEquals("sst", feed.getSourceClusterStart());
    }

    @org.junit.Test
    public void testGetSourceClusterEnd() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "st", "send", "l", "a", "target", "st", "end", "l", "a", props);
        org.junit.Assert.assertEquals("send", feed.getSourceClusterEnd());
    }

    @org.junit.Test
    public void testGetSourceClusterLimit() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "st", "end", "sl", "a", "target", "st", "end", "l", "a", props);
        org.junit.Assert.assertEquals("sl", feed.getSourceClusterLimit());
    }

    @org.junit.Test
    public void testGetSourceClusterAction() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "st", "end", "l", "sa", "target", "st", "end", "l", "a", props);
        org.junit.Assert.assertEquals("sa", feed.getSourceClusterAction());
    }

    @org.junit.Test
    public void testGetTargetClusterName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "st", "end", "l", "a", "target", "st", "end", "l", "a", props);
        org.junit.Assert.assertEquals("target", feed.getTargetClusterName());
    }

    @org.junit.Test
    public void testGetTargetClusterStart() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "sst", "end", "l", "a", "target", "tst", "end", "l", "a", props);
        org.junit.Assert.assertEquals("tst", feed.getTargetClusterStart());
    }

    @org.junit.Test
    public void testGetTargetClusterEnd() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "st", "send", "l", "a", "target", "st", "tend", "l", "a", props);
        org.junit.Assert.assertEquals("tend", feed.getTargetClusterEnd());
    }

    @org.junit.Test
    public void testGetTargetClusterLimit() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "st", "end", "sl", "a", "target", "st", "end", "tl", "a", props);
        org.junit.Assert.assertEquals("tl", feed.getTargetClusterLimit());
    }

    @org.junit.Test
    public void testGetTargetClusterAction() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "st", "end", "l", "sa", "target", "st", "end", "l", "ta", props);
        org.junit.Assert.assertEquals("ta", feed.getTargetClusterAction());
    }

    @org.junit.Test
    public void testGetProperties() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>();
        props.put("p1", "v1");
        org.apache.ambari.server.controller.ivory.Feed feed = new org.apache.ambari.server.controller.ivory.Feed("Feed1", "d", "s", "sch", "source", "st", "end", "l", "sa", "target", "st", "end", "l", "ta", props);
        org.junit.Assert.assertEquals(props, feed.getProperties());
    }
}