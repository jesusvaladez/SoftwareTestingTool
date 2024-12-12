package org.apache.ambari.server.utils;
public class TestHTTPUtils {
    @org.junit.Test
    public void testGetHostAndPortFromProperty() {
        java.lang.String value = null;
        org.apache.ambari.server.utils.HostAndPort hp = org.apache.ambari.server.utils.HTTPUtils.getHostAndPortFromProperty(value);
        junit.framework.Assert.assertNull(hp);
        value = "";
        hp = org.apache.ambari.server.utils.HTTPUtils.getHostAndPortFromProperty(value);
        junit.framework.Assert.assertNull(hp);
        value = "c6401.ambari.apache.org";
        hp = org.apache.ambari.server.utils.HTTPUtils.getHostAndPortFromProperty(value);
        junit.framework.Assert.assertNull(hp);
        value = "c6401.ambari.apache.org:";
        hp = org.apache.ambari.server.utils.HTTPUtils.getHostAndPortFromProperty(value);
        junit.framework.Assert.assertNull(hp);
        value = "c6401.ambari.apache.org:50070";
        hp = org.apache.ambari.server.utils.HTTPUtils.getHostAndPortFromProperty(value);
        junit.framework.Assert.assertEquals(hp.host, "c6401.ambari.apache.org");
        junit.framework.Assert.assertEquals(hp.port, 50070);
        value = "  c6401.ambari.apache.org:50070   ";
        junit.framework.Assert.assertEquals(hp.host, "c6401.ambari.apache.org");
        junit.framework.Assert.assertEquals(hp.port, 50070);
    }
}