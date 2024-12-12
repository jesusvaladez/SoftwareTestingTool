package org.apache.ambari.server.notifications.dispatchers;
import org.mockito.ArgumentCaptor;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
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
public class AmbariSNMPDispatcherTest {
    private static final int DEFAULT_SNMP_PORT = 31444;

    public static final java.lang.String DEFINITION_NAME = "definition name";

    public static final java.lang.String ALERT_LABEL = "alert name";

    public static final java.lang.String ALERT_TEXT = "alert text";

    public static final java.lang.String ALERT_HOSTNAME = "hostname";

    public static final java.lang.String ALERT_SERVICE_NAME = "service name";

    public static final java.lang.String ALERT_COMPONENT_NAME = "component name";

    public static final java.lang.Long DEFINITION_ID = 1L;

    public static final org.apache.ambari.server.state.AlertState ALERT_STATE = org.apache.ambari.server.state.AlertState.OK;

    @org.junit.Test
    public void testDispatch_nullProperties() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.state.alert.AlertNotification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatchUdpTransportMappingCrash() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT));
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv1;
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.state.alert.AlertNotification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = Mockito.mock(java.util.List.class);
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        notification.DispatchProperties = properties;
        notification.Recipients = java.util.Arrays.asList(new org.apache.ambari.server.notifications.Recipient());
        Mockito.doThrow(new java.io.IOException()).when(dispatcher).sendTraps(notification, snmpVersion);
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
        org.junit.Assert.assertNull(dispatcher.getTransportMapping());
    }

    @org.junit.Test
    public void testDispatch_notDefinedProperties() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.state.alert.AlertNotification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = Mockito.mock(java.util.List.class);
        notification.DispatchProperties = new java.util.HashMap<>();
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_nullRecipients() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = getAlertNotification(true);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = Mockito.mock(java.util.List.class);
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        notification.DispatchProperties = properties;
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_noRecipients() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = getAlertNotification(true);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = Mockito.mock(java.util.List.class);
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        notification.DispatchProperties = properties;
        notification.Recipients = new java.util.ArrayList<>();
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_sendTrapError() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT));
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.state.alert.AlertNotification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        notification.DispatchProperties = properties;
        notification.Recipients = java.util.Arrays.asList(new org.apache.ambari.server.notifications.Recipient());
        Mockito.doThrow(new java.lang.RuntimeException()).when(dispatcher).sendTraps(Matchers.eq(notification), Matchers.any(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.class));
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_incorrectSnmpVersion() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT));
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.state.alert.AlertNotification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv11");
        notification.DispatchProperties = properties;
        notification.Recipients = java.util.Arrays.asList(new org.apache.ambari.server.notifications.Recipient());
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback, Mockito.never()).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_successful_v1() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT));
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv1;
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.state.alert.AlertNotification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        notification.DispatchProperties = properties;
        notification.Recipients = java.util.Arrays.asList(new org.apache.ambari.server.notifications.Recipient());
        Mockito.doNothing().when(dispatcher).sendTraps(notification, snmpVersion);
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback, Mockito.never()).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_successful_v2() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT));
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv2c;
        org.apache.ambari.server.notifications.Notification notification = Mockito.mock(org.apache.ambari.server.state.alert.AlertNotification.class);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = Mockito.mock(java.util.List.class);
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv2c");
        notification.DispatchProperties = properties;
        notification.Recipients = java.util.Arrays.asList(new org.apache.ambari.server.notifications.Recipient());
        Mockito.doNothing().when(dispatcher).sendTraps(notification, snmpVersion);
        dispatcher.dispatch(notification);
        Mockito.verify(notification.Callback, Mockito.never()).onFailure(notification.CallbackIds);
        Mockito.verify(notification.Callback).onSuccess(notification.CallbackIds);
    }

    @org.junit.Test
    public void testDispatch_successful_v3() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = getAlertNotification(true);
        notification.Callback = Mockito.mock(org.apache.ambari.server.notifications.DispatchCallback.class);
        notification.CallbackIds = Mockito.mock(java.util.List.class);
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "public");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_PRIV_PASSPHRASE_PROPERTY, "PASSPHRASE2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_NOPRIV");
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
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = getAlertNotification(true);
        org.snmp4j.PDU pdu = dispatcher.prepareTrap(notification, snmpVersion);
        org.junit.Assert.assertEquals(PDU.V1TRAP, pdu.getType());
        java.util.Map<java.lang.String, org.snmp4j.smi.VariableBinding> variableBindings = new java.util.HashMap<>();
        for (org.snmp4j.smi.VariableBinding variableBinding : pdu.toArray()) {
            variableBindings.put(variableBinding.getOid().toString(), variableBinding);
        }
        org.junit.Assert.assertEquals(11, variableBindings.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_TRAP_OID, variableBindings.get(SnmpConstants.snmpTrapOID.toString()).toValueString());
        org.junit.Assert.assertTrue(variableBindings.get(SnmpConstants.snmpTrapOID.toString()).getVariable() instanceof org.snmp4j.smi.OID);
        org.junit.Assert.assertEquals(java.lang.String.valueOf(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFINITION_ID), variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_DEFINITION_ID_OID).toValueString());
        org.junit.Assert.assertTrue(variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_DEFINITION_ID_OID).getVariable() instanceof org.snmp4j.smi.Integer32);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFINITION_NAME, variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_DEFINITION_NAME_OID).toValueString());
        org.junit.Assert.assertTrue(variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_DEFINITION_NAME_OID).getVariable() instanceof org.snmp4j.smi.OctetString);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_LABEL, variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_NAME_OID).toValueString());
        org.junit.Assert.assertTrue(variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_NAME_OID).getVariable() instanceof org.snmp4j.smi.OctetString);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_TEXT, variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_TEXT_OID).toValueString());
        org.junit.Assert.assertTrue(variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_TEXT_OID).getVariable() instanceof org.snmp4j.smi.OctetString);
        org.junit.Assert.assertEquals(java.lang.String.valueOf(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_STATE.getIntValue()), variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_STATE_OID).toValueString());
        org.junit.Assert.assertTrue(variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_STATE_OID).getVariable() instanceof org.snmp4j.smi.Integer32);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_HOSTNAME, variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_HOST_NAME_OID).toValueString());
        org.junit.Assert.assertTrue(variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_HOST_NAME_OID).getVariable() instanceof org.snmp4j.smi.OctetString);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_SERVICE_NAME, variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_SERVICE_NAME_OID).toValueString());
        org.junit.Assert.assertTrue(variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_SERVICE_NAME_OID).getVariable() instanceof org.snmp4j.smi.OctetString);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_COMPONENT_NAME, variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_COMPONENT_NAME_OID).toValueString());
        org.junit.Assert.assertTrue(variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_COMPONENT_NAME_OID).getVariable() instanceof org.snmp4j.smi.OctetString);
    }

    @org.junit.Test
    public void testPrepareTrapNull() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv1;
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.state.alert.AlertNotification notification = ((org.apache.ambari.server.state.alert.AlertNotification) (getAlertNotification(false)));
        org.snmp4j.PDU pdu = dispatcher.prepareTrap(notification, snmpVersion);
        org.junit.Assert.assertEquals(PDU.V1TRAP, pdu.getType());
        java.util.Map<java.lang.String, org.snmp4j.smi.VariableBinding> variableBindings = new java.util.HashMap<>();
        for (org.snmp4j.smi.VariableBinding variableBinding : pdu.toArray()) {
            variableBindings.put(variableBinding.getOid().toString(), variableBinding);
        }
        org.junit.Assert.assertEquals(11, variableBindings.size());
        org.junit.Assert.assertEquals("null", variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_COMPONENT_NAME_OID).toValueString());
    }

    @org.junit.Test
    public void testPrepareTrap_v2c() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv2c;
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.Notification notification = getAlertNotification(true);
        org.snmp4j.PDU pdu = dispatcher.prepareTrap(notification, snmpVersion);
        org.junit.Assert.assertEquals(PDU.TRAP, pdu.getType());
        java.util.Map<java.lang.String, org.snmp4j.smi.VariableBinding> variableBindings = new java.util.HashMap<>();
        for (org.snmp4j.smi.VariableBinding variableBinding : pdu.toArray()) {
            variableBindings.put(variableBinding.getOid().toString(), variableBinding);
        }
        org.junit.Assert.assertEquals(11, variableBindings.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_TRAP_OID, variableBindings.get(SnmpConstants.snmpTrapOID.toString()).toValueString());
        org.junit.Assert.assertEquals(java.lang.String.valueOf(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFINITION_ID), variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_DEFINITION_ID_OID).toValueString());
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFINITION_NAME, variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_DEFINITION_NAME_OID).toValueString());
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_LABEL, variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_NAME_OID).toValueString());
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_TEXT, variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_TEXT_OID).toValueString());
        org.junit.Assert.assertEquals(java.lang.String.valueOf(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_STATE.getIntValue()), variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_STATE_OID).toValueString());
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_HOSTNAME, variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_HOST_NAME_OID).toValueString());
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_SERVICE_NAME, variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_SERVICE_NAME_OID).toValueString());
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_COMPONENT_NAME, variableBindings.get(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_COMPONENT_NAME_OID).toValueString());
    }

    @org.junit.Test
    public void testSendTraps_v1() throws java.lang.Exception {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.SNMPv1;
        org.snmp4j.Snmp snmp = Mockito.mock(org.snmp4j.Snmp.class);
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(snmp));
        org.snmp4j.PDU trap = Mockito.mock(org.snmp4j.PDU.class);
        org.apache.ambari.server.notifications.Notification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "public");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
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
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(snmp));
        org.snmp4j.PDU trap = Mockito.mock(org.snmp4j.PDU.class);
        org.apache.ambari.server.notifications.Notification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "public");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
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
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(snmp));
        org.snmp4j.PDU trap = Mockito.mock(org.snmp4j.PDU.class);
        org.apache.ambari.server.notifications.Notification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_PRIV_PASSPHRASE_PROPERTY, "PASSPHRASE2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_NOPRIV");
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
        org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher dispatcher = Mockito.spy(new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(snmp));
        org.snmp4j.PDU trap = Mockito.mock(org.snmp4j.PDU.class);
        org.apache.ambari.server.notifications.Notification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_PRIV_PASSPHRASE_PROPERTY, "PASSPHRASE2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_LEVEL_PROPERTY, "INCORRECT");
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
        properties.put(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "public");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.VALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_incorrectSNMPversion() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv4");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "public");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv1_invalid_noPort() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "public");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv2c() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv2c");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.COMMUNITY_PROPERTY, "public");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.VALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv2c_invalid() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv2c");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_incorrectSecurityLevel() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_PRIV_PASSPHRASE_PROPERTY, "PASSPHRASE2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_LEVEL_PROPERTY, "INCORRECT");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_noAuthNoPriv() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_LEVEL_PROPERTY, "NOAUTH_NOPRIV");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.VALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_AuthNoPriv_valid() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_NOPRIV");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.VALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_AuthNoPriv_invalid() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_NOPRIV");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_AuthPriv_valid() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_PRIV_PASSPHRASE_PROPERTY, "PASSPHRASE2");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_PRIV");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.VALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_AuthPriv_noPassphrases() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_PRIV");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    @org.junit.Test
    public void testValidateAlertValidation_SNMPv3_AuthPriv_onlyAuthPassphrase() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.PORT_PROPERTY, "162");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SNMP_VERSION_PROPERTY, "SNMPv3");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_USERNAME_PROPERTY, "USER");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY, "PASSPHRASE1");
        properties.put(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.SECURITY_LEVEL_PROPERTY, "AUTH_PRIV");
        org.apache.ambari.server.notifications.NotificationDispatcher dispatcher = new org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFAULT_SNMP_PORT);
        org.apache.ambari.server.notifications.TargetConfigurationResult configValidationResult = dispatcher.validateTargetConfig(properties);
        org.junit.Assert.assertEquals(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, configValidationResult.getStatus());
    }

    private org.apache.ambari.server.notifications.Notification getAlertNotification(boolean hasComponent) {
        org.apache.ambari.server.state.alert.AlertNotification notification = new org.apache.ambari.server.state.alert.AlertNotification();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinitionEntity = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        alertDefinitionEntity.setDefinitionName(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFINITION_NAME);
        alertDefinitionEntity.setLabel(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_LABEL);
        alertDefinitionEntity.setDefinitionId(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.DEFINITION_ID);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity alertHistoryEntity = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        alertHistoryEntity.setAlertDefinition(alertDefinitionEntity);
        alertHistoryEntity.setAlertLabel(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_LABEL);
        alertHistoryEntity.setAlertState(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_STATE);
        alertHistoryEntity.setAlertText(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_TEXT);
        alertHistoryEntity.setHostName(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_HOSTNAME);
        alertHistoryEntity.setServiceName(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_SERVICE_NAME);
        if (hasComponent) {
            alertHistoryEntity.setComponentName(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcherTest.ALERT_COMPONENT_NAME);
        }
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo alertInfo = new org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo(alertHistoryEntity);
        notification.setAlertInfo(alertInfo);
        return notification;
    }
}