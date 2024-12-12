package org.apache.ambari.server.utils;
public class HostUtilsTest {
    @org.junit.Test
    public void testIsValidHostname() throws java.lang.Exception {
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.HostUtils.isValidHostname("localhost"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.HostUtils.isValidHostname("localhost.localdomain"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.HostUtils.isValidHostname("host1.example.com"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.HostUtils.isValidHostname("Host1.eXample.coM"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.HostUtils.isValidHostname("host-name.example.com"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.HostUtils.isValidHostname("123.456.789"));
        junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.HostUtils.isValidHostname("host-123-name.ex4mpl3.c0m"));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.HostUtils.isValidHostname("host_name.example.com"));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.HostUtils.isValidHostname("host;name.example.com"));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.HostUtils.isValidHostname("host?name.example.com"));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.HostUtils.isValidHostname("host@name.example.com"));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.HostUtils.isValidHostname("host=name.example.com"));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.HostUtils.isValidHostname("host+name.example.com"));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.utils.HostUtils.isValidHostname("host)name).example.com"));
    }
}