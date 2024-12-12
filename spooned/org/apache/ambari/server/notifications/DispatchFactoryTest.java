package org.apache.ambari.server.notifications;
public class DispatchFactoryTest {
    @org.junit.Test
    public void testDispatchFactoryRegistration() throws java.lang.Exception {
        java.lang.String sourceResourceDirectory = ((("src" + java.io.File.separator) + "test") + java.io.File.separator) + "resources";
        java.lang.Integer snmpPort = 30111;
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_TYPE.getKey(), "in-memory");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.OS_VERSION.getKey(), "centos6");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR.getKey(), sourceResourceDirectory);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.ALERTS_SNMP_DISPATCH_UDP_PORT.getKey(), snmpPort.toString());
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.audit.AuditLoggerModule(), new org.apache.ambari.server.controller.ControllerModule(properties), new org.apache.ambari.server.ldap.LdapModule());
        org.apache.ambari.server.notifications.DispatchFactory dispatchFactory = injector.getInstance(org.apache.ambari.server.notifications.DispatchFactory.class);
        org.apache.ambari.server.notifications.DispatchFactory dispatchFactory2 = injector.getInstance(org.apache.ambari.server.notifications.DispatchFactory.class);
        org.junit.Assert.assertEquals(dispatchFactory, dispatchFactory2);
        org.apache.ambari.server.notifications.dispatchers.EmailDispatcher emailDispatcher = injector.getInstance(org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.class);
        org.apache.ambari.server.notifications.dispatchers.EmailDispatcher emailDispatcher2 = ((org.apache.ambari.server.notifications.dispatchers.EmailDispatcher) (dispatchFactory.getDispatcher(emailDispatcher.getType())));
        org.junit.Assert.assertNotNull(emailDispatcher);
        org.junit.Assert.assertNotNull(emailDispatcher2);
        org.junit.Assert.assertEquals(emailDispatcher, emailDispatcher2);
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher snmpDispatcher = injector.getInstance(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.class);
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher snmpDispatcher2 = ((org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher) (dispatchFactory.getDispatcher(snmpDispatcher.getType())));
        org.junit.Assert.assertNotNull(snmpDispatcher);
        org.junit.Assert.assertEquals(snmpDispatcher.getPort(), snmpPort);
        org.junit.Assert.assertNotNull(snmpDispatcher2);
        org.junit.Assert.assertEquals(snmpDispatcher2.getPort(), snmpPort);
        org.junit.Assert.assertEquals(snmpDispatcher, snmpDispatcher2);
    }
}