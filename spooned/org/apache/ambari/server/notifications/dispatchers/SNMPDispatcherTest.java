package org.apache.ambari.server.notifications.dispatchers;
import org.mockito.ArgumentCaptor;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.VariableBinding;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
public class SNMPDispatcherTest {
    private static final int DEFAULT_SNMP_PORT = 31444;

    @org.junit.Test
    public void testDispatch_nullProperties() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.notifications.Notification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatchUdpTransportMappingCrash() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT));
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv1;
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.notifications.Notification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        notification.DispatchProperties = properties;
        notification.Body = "body";
        notification.Subject = "subject";
        notification.Recipients = java.util.Arrays.asList(new org.apache.ambari.server.notifications.Recipient());
        Mockito.doThrow(new java.io.IOException()).when(dispatcher).sendTraps(notification, snmpVersion);
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
        org.junit.Assert.assertNull(dispatcher.getTransportMapping());
    }

    @org.junit.Test
    public void testDispatch_notDefinedProperties() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.notifications.Notification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        notification.DispatchProperties = new java.util.HashMap<>();
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_nullRecipients() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.notifications.Notification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        notification.DispatchProperties = properties;
        notification.Body = "body";
        notification.Subject = "subject";
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_noRecipients() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.notifications.Notification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        notification.DispatchProperties = properties;
        notification.Body = "body";
        notification.Subject = "subject";
        notification.Recipients = new java.util.ArrayList<>();
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_sendTrapError() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT));
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.notifications.Notification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        notification.DispatchProperties = properties;
        notification.Body = "body";
        notification.Subject = "subject";
        notification.Recipients = java.util.Arrays.asList(new org.apache.ambari.server.notifications.Recipient());
        Mockito.doThrow(new java.lang.RuntimeException()).when(dispatcher).sendTraps(Matchers.eq(notification), Matchers.any(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.class));
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_incorrectSnmpVersion() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT));
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.notifications.Notification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv11");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        notification.DispatchProperties = properties;
        notification.Body = "body";
        notification.Subject = "subject";
        notification.Recipients = java.util.Arrays.asList(new org.apache.ambari.server.notifications.Recipient());
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_successful_v1() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT));
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv1;
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.notifications.Notification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        notification.DispatchProperties = properties;
        notification.Body = "body";
        notification.Subject = "subject";
        notification.Recipients = java.util.Arrays.asList(new org.apache.ambari.server.notifications.Recipient());
        Mockito.doNothing().when(dispatcher).sendTraps(notification, snmpVersion);
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback, Mockito.never()).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_successful_v2() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT));
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv2c;
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.notifications.Notification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv2c");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        notification.DispatchProperties = properties;
        notification.Body = "body";
        notification.Subject = "subject";
        notification.Recipients = java.util.Arrays.asList(new org.apache.ambari.server.notifications.Recipient());
        Mockito.doNothing().when(dispatcher).sendTraps(notification, snmpVersion);
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback, Mockito.never()).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_successful_v3() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = new org.apache.ambari.server.notifications.Notification();
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        notification.Body = "body";
        notification.Subject = "subject";
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "public");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_PRIV_PASSPHRASE_PROPERTY, "PASSPHRASE2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_NOPRIV");
        notification.DispatchProperties = properties;
        org.apache.ambari.server.notifications.Recipient recipient = new org.apache.ambari.server.notifications.Recipient();
        recipient.Identifier = "192.168.0.2";
        notification.Recipients = java.util.Arrays.asList(recipient);
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback, Mockito.never()).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testPrepareTrap_v1() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv1;
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = new org.apache.ambari.server.notifications.Notification();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "3");
        notification.DispatchProperties = properties;
        notification.Body = "body";
        notification.Subject = "subject";
        org.snmp4j.PDU pdu = dispatcher.prepareTrap(notification, snmpVersion);
        org.junit.Assert.assertEquals(PDU.V1TRAP, pdu.getType());
        java.util.Map<java.lang.String, org.snmp4j.smi.VariableBinding> variableBindings = new java.util.HashMap<>();
        for (org.snmp4j.smi.VariableBinding variableBinding : pdu.toArray()) {
            variableBindings.put(variableBinding.getOid().toString(), variableBinding);
        }
        org.junit.Assert.assertEquals(3, variableBindings.size());
        org.junit.Assert.assertEquals("subject", variableBindings.get("1").toValueString());
        org.junit.Assert.assertEquals("body", variableBindings.get("2").toValueString());
        org.junit.Assert.assertEquals("3", variableBindings.get(SnmpConstants.snmpTrapOID.toString()).toValueString());
    }

    @org.junit.Test
    public void testPrepareTrap_v2c() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv2c;
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = new org.apache.ambari.server.notifications.Notification();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "4");
        notification.DispatchProperties = properties;
        notification.Body = "body";
        notification.Subject = "subject";
        org.snmp4j.PDU pdu = dispatcher.prepareTrap(notification, snmpVersion);
        org.junit.Assert.assertEquals(PDU.TRAP, pdu.getType());
        java.util.Map<java.lang.String, org.snmp4j.smi.VariableBinding> variableBindings = new java.util.HashMap<>();
        for (org.snmp4j.smi.VariableBinding variableBinding : pdu.toArray()) {
            variableBindings.put(variableBinding.getOid().toString(), variableBinding);
        }
        org.junit.Assert.assertEquals(3, variableBindings.size());
        org.junit.Assert.assertEquals("subject", variableBindings.get("1").toValueString());
        org.junit.Assert.assertEquals("body", variableBindings.get("2").toValueString());
        org.junit.Assert.assertEquals("4", variableBindings.get(SnmpConstants.snmpTrapOID.toString()).toValueString());
    }

    @org.junit.Test
    public void testSendTraps_v1() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv1;
        org.snmp4j.Snmp snmp = Mockito.mock(org.snmp4j.Snmp.class);
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(snmp));
        org.snmp4j.PDU trap = Mockito.mock(org.snmp4j.PDU.class);
        org.apache.ambari.server.notifications.Notification notification = new org.apache.ambari.server.notifications.Notification();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "public");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        notification.DispatchProperties = properties;
        org.apache.ambari.server.notifications.Recipient rec1 = new org.apache.ambari.server.notifications.Recipient();
        rec1.Identifier = "192.168.0.2";
        notification.Recipients = java.util.Arrays.asList(rec1);
        Mockito.doReturn(trap).when(dispatcher).prepareTrap(notification, snmpVersion);
        dispatcher.sendTraps(notification, snmpVersion);
        org.mockito.ArgumentCaptor<org.snmp4j.Target> argument = org.mockito.ArgumentCaptor.forClass(org.snmp4j.Target.class);
        Mockito.verify(snmp, Mockito.times(1)).send(Matchers.eq(trap), argument.capture());
        org.junit.Assert.assertEquals("192.168.0.2/162", argument.getValue().getAddress().toString());
        org.junit.Assert.assertEquals(SnmpConstants.version1, argument.getValue().getVersion());
    }

    @org.junit.Test
    public void testSendTraps_v2() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv2c;
        org.snmp4j.Snmp snmp = Mockito.mock(org.snmp4j.Snmp.class);
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(snmp));
        org.snmp4j.PDU trap = Mockito.mock(org.snmp4j.PDU.class);
        org.apache.ambari.server.notifications.Notification notification = new org.apache.ambari.server.notifications.Notification();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "public");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        notification.DispatchProperties = properties;
        org.apache.ambari.server.notifications.Recipient rec1 = new org.apache.ambari.server.notifications.Recipient();
        rec1.Identifier = "192.168.0.2";
        notification.Recipients = java.util.Arrays.asList(rec1);
        Mockito.doReturn(trap).when(dispatcher).prepareTrap(notification, snmpVersion);
        dispatcher.sendTraps(notification, snmpVersion);
        org.mockito.ArgumentCaptor<org.snmp4j.Target> argument = org.mockito.ArgumentCaptor.forClass(org.snmp4j.Target.class);
        Mockito.verify(snmp, Mockito.times(1)).send(Matchers.eq(trap), argument.capture());
        org.junit.Assert.assertEquals("192.168.0.2/162", argument.getValue().getAddress().toString());
        org.junit.Assert.assertEquals(SnmpConstants.version2c, argument.getValue().getVersion());
    }

    @org.junit.Test
    public void testSendTraps_v3() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv3;
        org.snmp4j.Snmp snmp = Mockito.mock(org.snmp4j.Snmp.class);
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(snmp));
        org.snmp4j.PDU trap = Mockito.mock(org.snmp4j.PDU.class);
        org.apache.ambari.server.notifications.Notification notification = new org.apache.ambari.server.notifications.Notification();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_PRIV_PASSPHRASE_PROPERTY, "PASSPHRASE2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_NOPRIV");
        notification.DispatchProperties = properties;
        org.apache.ambari.server.notifications.Recipient rec1 = new org.apache.ambari.server.notifications.Recipient();
        rec1.Identifier = "192.168.0.2";
        notification.Recipients = java.util.Arrays.asList(rec1);
        Mockito.doReturn(trap).when(dispatcher).prepareTrap(notification, snmpVersion);
        dispatcher.sendTraps(notification, snmpVersion);
        org.mockito.ArgumentCaptor<org.snmp4j.Target> argument = org.mockito.ArgumentCaptor.forClass(org.snmp4j.Target.class);
        Mockito.verify(snmp, Mockito.times(1)).send(Matchers.eq(trap), argument.capture());
        org.junit.Assert.assertEquals("192.168.0.2/162", argument.getValue().getAddress().toString());
        org.junit.Assert.assertEquals(SnmpConstants.version3, argument.getValue().getVersion());
    }

    @org.junit.Test(expected = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException.class)
    public void testSendTraps_v3_incorrectSecurityLevelVersion() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv3;
        org.snmp4j.Snmp snmp = Mockito.mock(org.snmp4j.Snmp.class);
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(snmp));
        org.snmp4j.PDU trap = Mockito.mock(org.snmp4j.PDU.class);
        org.apache.ambari.server.notifications.Notification notification = new org.apache.ambari.server.notifications.Notification();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_PRIV_PASSPHRASE_PROPERTY, "PASSPHRASE2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_LEVEL_PROPERTY, "INCORRECT");
        notification.DispatchProperties = properties;
        org.apache.ambari.server.notifications.Recipient rec1 = new org.apache.ambari.server.notifications.Recipient();
        rec1.Identifier = "192.168.0.2";
        notification.Recipients = java.util.Arrays.asList(rec1);
        Mockito.doReturn(trap).when(dispatcher).prepareTrap(notification, snmpVersion);
        dispatcher.sendTraps(notification, snmpVersion);
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv1() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "public");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.VALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_incorrectSNMPversion() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "public");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv1_invalid() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "public");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv2c() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv2c");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY, "public");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.VALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv2c_invalid() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv2c");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_incorrectSecurityLevel() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_PRIV_PASSPHRASE_PROPERTY, "PASSPHRASE2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_LEVEL_PROPERTY, "INCORRECT");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_noAuthNoPriv() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_LEVEL_PROPERTY, "NOAUTH_NOPRIV");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.VALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_AuthNoPriv_valid() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_NOPRIV");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.VALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_AuthNoPriv_invalid() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_NOPRIV");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_AuthPriv_valid() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_PRIV_PASSPHRASE_PROPERTY, "PASSPHRASE2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_PRIV");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.VALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_AuthPriv_noPassphrases() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_PRIV");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_AuthPriv_onlyAuthPassphrase() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, "1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, "2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, "1.3.6.1.6.3.1.1.5.4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_PRIV");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }
}