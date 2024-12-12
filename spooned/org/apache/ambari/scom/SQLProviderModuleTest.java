package org.apache.ambari.scom;
public class SQLProviderModuleTest {
    @org.junit.Test
    public void testCreatePropertyProviders() {
        org.apache.ambari.scom.SQLProviderModule providerModule = new org.apache.ambari.scom.SQLProviderModule();
        providerModule.createPropertyProviders(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> providers = providerModule.getPropertyProviders(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        org.junit.Assert.assertTrue(providers.get(0) instanceof org.apache.ambari.server.controller.jmx.JMXPropertyProvider);
        org.junit.Assert.assertTrue(providers.get(1) instanceof org.apache.ambari.scom.SQLPropertyProvider);
        providerModule.createPropertyProviders(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        providers = providerModule.getPropertyProviders(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        org.junit.Assert.assertTrue(providers.get(0) instanceof org.apache.ambari.server.controller.jmx.JMXPropertyProvider);
        org.junit.Assert.assertTrue(providers.get(1) instanceof org.apache.ambari.scom.SQLPropertyProvider);
    }
}