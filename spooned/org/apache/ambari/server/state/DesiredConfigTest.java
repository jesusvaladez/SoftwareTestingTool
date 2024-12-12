package org.apache.ambari.server.state;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
public class DesiredConfigTest {
    @org.junit.Test
    public void testDesiredConfig() throws java.lang.Exception {
        org.apache.ambari.server.state.DesiredConfig dc = new org.apache.ambari.server.state.DesiredConfig();
        dc.setServiceName("service");
        dc.setTag("global");
        org.junit.Assert.assertEquals("Expected service 'service'", "service", dc.getServiceName());
        org.junit.Assert.assertEquals("Expected version 'global'", "global", dc.getTag());
        org.junit.Assert.assertEquals("Expected no host overrides", 0, dc.getHostOverrides().size());
        java.util.List<org.apache.ambari.server.state.DesiredConfig.HostOverride> hosts = java.util.Arrays.asList(new org.apache.ambari.server.state.DesiredConfig.HostOverride("h1", "v2"), new org.apache.ambari.server.state.DesiredConfig.HostOverride("h2", "v3"));
        dc.setHostOverrides(hosts);
        org.junit.Assert.assertNotNull("Expected host overrides to be set", dc.getHostOverrides());
        org.junit.Assert.assertEquals("Expected host override equality", hosts, dc.getHostOverrides());
    }

    @org.junit.Test
    public void testHostOverride() throws java.lang.Exception {
        org.apache.ambari.server.state.DesiredConfig.HostOverride override = new org.apache.ambari.server.state.DesiredConfig.HostOverride("h1", "v1");
        org.junit.Assert.assertNotNull(override.getName());
        org.junit.Assert.assertNotNull(override.getVersionTag());
        org.junit.Assert.assertEquals("Expected override host 'h1'", "h1", override.getName());
        org.junit.Assert.assertEquals("Expected override version 'v1'", "v1", override.getVersionTag());
    }

    @org.junit.Test
    public void testEquals() throws java.lang.Exception {
        nl.jqno.equalsverifier.EqualsVerifier.forClass(org.apache.ambari.server.state.DesiredConfig.class).usingGetClass().suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @org.junit.Test
    public void testHostOverride_Equals() throws java.lang.Exception {
        nl.jqno.equalsverifier.EqualsVerifier.forClass(org.apache.ambari.server.state.DesiredConfig.HostOverride.class).usingGetClass().verify();
    }
}