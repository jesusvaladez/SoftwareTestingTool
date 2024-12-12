package org.apache.ambari.server.notifications.dispatchers;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.DefaultPDUFactory;
@com.google.inject.Singleton
public class AmbariSNMPDispatcher extends org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.class);

    public static final java.lang.String BASE_AMBARI_OID = "1.3.6.1.4.1.18060.16";

    public static final java.lang.String APACHE_AMBARI_TRAPS_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.BASE_AMBARI_OID + ".0";

    public static final java.lang.String AMBARI_ALERT_TRAP_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.APACHE_AMBARI_TRAPS_OID + ".1";

    public static final java.lang.String AMBARI_ALERTS_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.BASE_AMBARI_OID + ".1";

    public static final java.lang.String AMBARI_ALERT_TABLE_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERTS_OID + ".1";

    public static final java.lang.String AMBARI_ALERT_ENTRY_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_TABLE_OID + ".1";

    public static final java.lang.String AMBARI_ALERT_DEFINITION_ID_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_ENTRY_OID + ".1";

    public static final java.lang.String AMBARI_ALERT_DEFINITION_NAME_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_ENTRY_OID + ".2";

    public static final java.lang.String AMBARI_ALERT_DEFINITION_HASH_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_ENTRY_OID + ".3";

    public static final java.lang.String AMBARI_ALERT_NAME_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_ENTRY_OID + ".4";

    public static final java.lang.String AMBARI_ALERT_TEXT_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_ENTRY_OID + ".5";

    public static final java.lang.String AMBARI_ALERT_STATE_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_ENTRY_OID + ".6";

    public static final java.lang.String AMBARI_ALERT_HOST_NAME_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_ENTRY_OID + ".7";

    public static final java.lang.String AMBARI_ALERT_SERVICE_NAME_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_ENTRY_OID + ".8";

    public static final java.lang.String AMBARI_ALERT_COMPONENT_NAME_OID = org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_ENTRY_OID + ".9";

    protected AmbariSNMPDispatcher(org.snmp4j.Snmp snmp) {
        super(snmp);
    }

    public AmbariSNMPDispatcher(java.lang.Integer port) throws java.io.IOException {
        super(port);
    }

    @java.lang.Override
    public java.lang.String getType() {
        return org.apache.ambari.server.state.alert.TargetType.AMBARI_SNMP.name();
    }

    @java.lang.Override
    protected org.snmp4j.PDU prepareTrap(org.apache.ambari.server.notifications.Notification notification, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion) throws org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException {
        org.apache.ambari.server.state.alert.AlertNotification alertNotification;
        org.snmp4j.PDU pdu = org.snmp4j.util.DefaultPDUFactory.createPDU(snmpVersion.getTargetVersion());
        if (org.apache.ambari.server.notifications.Notification.Type.ALERT.equals(notification.getType())) {
            try {
                alertNotification = ((org.apache.ambari.server.state.alert.AlertNotification) (notification));
            } catch (java.lang.ClassCastException e) {
                org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.LOG.error("Notification wasn't casted to AlertNotification. Returning empty Protocol data unit", e);
                return pdu;
            }
        } else {
            org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.LOG.error("Notification for AmbariSNMPDispatcher should be of type AlertNotification, but it wasn't. Returning empty Protocol data unit");
            return pdu;
        }
        pdu.setType(snmpVersion.getTrapType());
        java.lang.management.RuntimeMXBean runtimeMXBean = java.lang.management.ManagementFactory.getRuntimeMXBean();
        long uptimeInHundredthsOfSecond = runtimeMXBean.getUptime() / 10;
        pdu.add(new org.snmp4j.smi.VariableBinding(org.snmp4j.mp.SnmpConstants.sysUpTime, new org.snmp4j.smi.TimeTicks(uptimeInHundredthsOfSecond)));
        pdu.add(new org.snmp4j.smi.VariableBinding(org.snmp4j.mp.SnmpConstants.snmpTrapOID, new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_TRAP_OID)));
        org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo alertInfo = alertNotification.getAlertInfo();
        addIntVariableBindingCheckForNull(pdu, org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_DEFINITION_ID_OID, new java.math.BigDecimal(alertInfo.getAlertDefinitionId()).intValueExact());
        addStringVariableBindingCheckForNull(pdu, org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_DEFINITION_NAME_OID, alertInfo.getAlertDefinition().getDefinitionName());
        addStringVariableBindingCheckForNull(pdu, org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_DEFINITION_HASH_OID, alertInfo.getAlertDefinitionHash());
        addStringVariableBindingCheckForNull(pdu, org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_NAME_OID, alertInfo.getAlertName());
        addStringVariableBindingCheckForNull(pdu, org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_TEXT_OID, alertInfo.getAlertText());
        addIntVariableBindingCheckForNull(pdu, org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_STATE_OID, alertInfo.getAlertState().getIntValue());
        addStringVariableBindingCheckForNull(pdu, org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_HOST_NAME_OID, alertInfo.getHostName());
        addStringVariableBindingCheckForNull(pdu, org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_SERVICE_NAME_OID, alertInfo.getServiceName());
        addStringVariableBindingCheckForNull(pdu, org.apache.ambari.server.notifications.dispatchers.AmbariSNMPDispatcher.AMBARI_ALERT_COMPONENT_NAME_OID, alertInfo.getComponentName());
        return pdu;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getSetOfDefaultNeededPropertyNames() {
        return new java.util.HashSet<>(java.util.Collections.singletonList(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY));
    }

    private void addStringVariableBindingCheckForNull(org.snmp4j.PDU pdu, java.lang.String oid, java.lang.Object val) {
        if (val == null) {
            pdu.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(oid), new org.snmp4j.smi.OctetString("null")));
        } else {
            pdu.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(oid), new org.snmp4j.smi.OctetString(java.lang.String.valueOf(val))));
        }
    }

    private void addIntVariableBindingCheckForNull(org.snmp4j.PDU pdu, java.lang.String oid, java.lang.Integer val) {
        if (val == null) {
            pdu.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(oid), new org.snmp4j.smi.OctetString("null")));
        } else {
            pdu.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(oid), new org.snmp4j.smi.Integer32(val)));
        }
    }
}