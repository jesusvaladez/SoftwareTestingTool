package org.apache.ambari.server.notifications.dispatchers;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.transport.UdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
@com.google.inject.Singleton
public class SNMPDispatcher implements org.apache.ambari.server.notifications.NotificationDispatcher {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.class);

    public static final java.lang.String BODY_OID_PROPERTY = "ambari.dispatch.snmp.oids.body";

    public static final java.lang.String SUBJECT_OID_PROPERTY = "ambari.dispatch.snmp.oids.subject";

    public static final java.lang.String TRAP_OID_PROPERTY = "ambari.dispatch.snmp.oids.trap";

    public static final java.lang.String PORT_PROPERTY = "ambari.dispatch.snmp.port";

    public static final java.lang.String SNMP_VERSION_PROPERTY = "ambari.dispatch.snmp.version";

    public static final java.lang.String COMMUNITY_PROPERTY = "ambari.dispatch.snmp.community";

    public static final java.lang.String SECURITY_USERNAME_PROPERTY = "ambari.dispatch.snmp.security.username";

    public static final java.lang.String SECURITY_AUTH_PASSPHRASE_PROPERTY = "ambari.dispatch.snmp.security.auth.passphrase";

    public static final java.lang.String SECURITY_PRIV_PASSPHRASE_PROPERTY = "ambari.dispatch.snmp.security.priv.passphrase";

    public static final java.lang.String SECURITY_LEVEL_PROPERTY = "ambari.dispatch.snmp.security.level";

    private org.snmp4j.Snmp snmp;

    private final java.lang.Integer port;

    private volatile org.snmp4j.transport.UdpTransportMapping transportMapping;

    public SNMPDispatcher(org.snmp4j.Snmp snmp) {
        this.port = null;
        this.snmp = snmp;
    }

    public SNMPDispatcher() throws java.io.IOException {
        this(((java.lang.Integer) (null)));
    }

    public SNMPDispatcher(java.lang.Integer port) throws java.io.IOException {
        if (((port != null) && (port >= 0)) && (port <= '￿')) {
            this.port = port;
        } else {
            this.port = null;
        }
    }

    private void createTransportMapping() throws java.io.IOException {
        if (transportMapping == null) {
            synchronized(this) {
                if (transportMapping == null) {
                    if (port != null) {
                        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.LOG.info("Setting SNMP dispatch port: " + port);
                        transportMapping = new org.snmp4j.transport.DefaultUdpTransportMapping(new org.snmp4j.smi.UdpAddress(port), true);
                    } else {
                        transportMapping = new org.snmp4j.transport.DefaultUdpTransportMapping();
                    }
                }
            }
        }
    }

    @java.lang.Override
    public java.lang.String getType() {
        return org.apache.ambari.server.state.alert.TargetType.SNMP.name();
    }

    @java.lang.Override
    public boolean isNotificationContentGenerationRequired() {
        return true;
    }

    @java.lang.Override
    public void dispatch(org.apache.ambari.server.notifications.Notification notification) {
        org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.LOG.info("Sending SNMP trap: {}", notification.Subject);
        try {
            createTransportMapping();
            snmp = new org.snmp4j.Snmp(transportMapping);
            org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = getSnmpVersion(notification.DispatchProperties);
            sendTraps(notification, snmpVersion);
            successCallback(notification);
        } catch (org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException ex) {
            org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.LOG.error("Unable to dispatch SNMP trap with invalid configuration. " + ex.getMessage());
            failureCallback(notification);
        } catch (java.lang.Exception ex) {
            org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.LOG.error("Error occurred during SNMP trap dispatching.", ex);
            failureCallback(notification);
            transportMapping = null;
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.notifications.TargetConfigurationResult validateTargetConfig(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.util.Map<java.lang.String, java.lang.String> stringValuesConfig = new java.util.HashMap<>(properties.size());
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> propertyEntry : properties.entrySet()) {
            stringValuesConfig.put(propertyEntry.getKey(), propertyEntry.getValue().toString());
        }
        try {
            for (java.lang.String property : getSetOfDefaultNeededPropertyNames()) {
                org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(stringValuesConfig, property);
            }
            org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion = getSnmpVersion(stringValuesConfig);
            switch (snmpVersion) {
                case SNMPv3 :
                    org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(stringValuesConfig, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_USERNAME_PROPERTY);
                    org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TrapSecurity securityLevel = getSecurityLevel(stringValuesConfig);
                    switch (securityLevel) {
                        case AUTH_PRIV :
                            org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(stringValuesConfig, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_PRIV_PASSPHRASE_PROPERTY);
                            org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(stringValuesConfig, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY);
                            break;
                        case AUTH_NOPRIV :
                            org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(stringValuesConfig, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY);
                            break;
                    }
                    break;
                case SNMPv2c :
                case SNMPv1 :
                    org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(stringValuesConfig, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY);
                    break;
            }
        } catch (org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException ex) {
            return org.apache.ambari.server.notifications.TargetConfigurationResult.invalid(ex.getMessage());
        }
        return org.apache.ambari.server.notifications.TargetConfigurationResult.valid();
    }

    protected java.util.Set<java.lang.String> getSetOfDefaultNeededPropertyNames() {
        return new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY));
    }

    protected org.snmp4j.PDU prepareTrap(org.apache.ambari.server.notifications.Notification notification, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion) throws org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException {
        org.snmp4j.PDU pdu = org.snmp4j.util.DefaultPDUFactory.createPDU(snmpVersion.getTargetVersion());
        pdu.setType(snmpVersion.getTrapType());
        pdu.add(new org.snmp4j.smi.VariableBinding(org.snmp4j.mp.SnmpConstants.snmpTrapOID, new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(notification.DispatchProperties, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TRAP_OID_PROPERTY))));
        pdu.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(notification.DispatchProperties, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.BODY_OID_PROPERTY)), new org.snmp4j.smi.OctetString(notification.Body)));
        pdu.add(new org.snmp4j.smi.VariableBinding(new org.snmp4j.smi.OID(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(notification.DispatchProperties, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SUBJECT_OID_PROPERTY)), new org.snmp4j.smi.OctetString(notification.Subject)));
        return pdu;
    }

    protected void sendTraps(org.apache.ambari.server.notifications.Notification notification, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion) throws org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException, java.io.IOException {
        org.snmp4j.PDU trap = prepareTrap(notification, snmpVersion);
        java.lang.String udpPort = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(notification.DispatchProperties, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.PORT_PROPERTY);
        for (org.apache.ambari.server.notifications.Recipient recipient : getNotificationRecipients(notification)) {
            java.lang.String address = recipient.Identifier;
            org.snmp4j.Target target = createTrapTarget(notification, snmpVersion);
            target.setAddress(new org.snmp4j.smi.UdpAddress((address + "/") + udpPort));
            snmp.send(trap, target);
        }
    }

    protected org.snmp4j.Target createTrapTarget(org.apache.ambari.server.notifications.Notification notification, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion snmpVersion) throws org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException {
        if (snmpVersion.isCommunityTargetRequired()) {
            org.snmp4j.smi.OctetString community = new org.snmp4j.smi.OctetString(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(notification.DispatchProperties, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.COMMUNITY_PROPERTY));
            org.snmp4j.CommunityTarget communityTarget = new org.snmp4j.CommunityTarget();
            communityTarget.setCommunity(community);
            communityTarget.setVersion(snmpVersion.getTargetVersion());
            return communityTarget;
        } else {
            org.snmp4j.smi.OctetString userName = new org.snmp4j.smi.OctetString(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(notification.DispatchProperties, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_USERNAME_PROPERTY));
            if (snmp.getUSM() == null) {
                org.snmp4j.security.USM usm = new org.snmp4j.security.USM(org.snmp4j.security.SecurityProtocols.getInstance(), new org.snmp4j.smi.OctetString(org.snmp4j.mp.MPv3.createLocalEngineID()), 0);
                java.lang.String authPassphraseProperty = notification.DispatchProperties.get(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_AUTH_PASSPHRASE_PROPERTY);
                java.lang.String privPassphraseProperty = notification.DispatchProperties.get(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_PRIV_PASSPHRASE_PROPERTY);
                org.snmp4j.smi.OctetString authPassphrase = (authPassphraseProperty != null) ? new org.snmp4j.smi.OctetString(authPassphraseProperty) : null;
                org.snmp4j.smi.OctetString privPassphrase = (privPassphraseProperty != null) ? new org.snmp4j.smi.OctetString(privPassphraseProperty) : null;
                org.snmp4j.security.UsmUser usmUser = new org.snmp4j.security.UsmUser(userName, org.snmp4j.security.AuthMD5.ID, authPassphrase, org.snmp4j.security.PrivDES.ID, privPassphrase);
                usm.addUser(userName, usmUser);
                org.snmp4j.security.SecurityModels.getInstance().addSecurityModel(usm);
            }
            org.snmp4j.UserTarget userTarget = new org.snmp4j.UserTarget();
            userTarget.setSecurityName(userName);
            userTarget.setSecurityLevel(getSecurityLevel(notification.DispatchProperties).getSecurityLevel());
            userTarget.setSecurityModel(SecurityModel.SECURITY_MODEL_USM);
            userTarget.setVersion(snmpVersion.getTargetVersion());
            return userTarget;
        }
    }

    @java.lang.Override
    public boolean isDigestSupported() {
        return false;
    }

    protected enum TrapSecurity {

        NOAUTH_NOPRIV(org.snmp4j.security.SecurityLevel.NOAUTH_NOPRIV),
        AUTH_NOPRIV(org.snmp4j.security.SecurityLevel.AUTH_NOPRIV),
        AUTH_PRIV(org.snmp4j.security.SecurityLevel.AUTH_PRIV);
        int securityLevel;

        TrapSecurity(int securityLevel) {
            this.securityLevel = securityLevel;
        }

        public int getSecurityLevel() {
            return securityLevel;
        }
    }

    protected enum SnmpVersion {

        SNMPv1(org.snmp4j.PDU.V1TRAP, org.snmp4j.mp.SnmpConstants.version1, true),
        SNMPv2c(org.snmp4j.PDU.TRAP, org.snmp4j.mp.SnmpConstants.version2c, true),
        SNMPv3(org.snmp4j.PDU.TRAP, org.snmp4j.mp.SnmpConstants.version3, false);
        private int trapType;

        private int targetVersion;

        private boolean communityTargetRequired;

        SnmpVersion(int trapType, int targetVersion, boolean communityTargetRequired) {
            this.trapType = trapType;
            this.targetVersion = targetVersion;
            this.communityTargetRequired = communityTargetRequired;
        }

        public int getTrapType() {
            return trapType;
        }

        public int getTargetVersion() {
            return targetVersion;
        }

        public boolean isCommunityTargetRequired() {
            return communityTargetRequired;
        }
    }

    protected static class InvalidSnmpConfigurationException extends java.lang.Exception {
        public InvalidSnmpConfigurationException(java.lang.String message) {
            super(message);
        }
    }

    private java.util.List<org.apache.ambari.server.notifications.Recipient> getNotificationRecipients(org.apache.ambari.server.notifications.Notification notification) throws org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException {
        if ((notification.Recipients == null) || notification.Recipients.isEmpty()) {
            throw new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException("Destination addresses should be set.");
        }
        return notification.Recipients;
    }

    protected static java.lang.String getDispatchProperty(java.util.Map<java.lang.String, java.lang.String> dispatchProperties, java.lang.String key) throws org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException {
        if ((dispatchProperties == null) || (!dispatchProperties.containsKey(key))) {
            throw new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException(java.lang.String.format("Property \"%s\" should be set.", key));
        }
        return dispatchProperties.get(key);
    }

    protected org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion getSnmpVersion(java.util.Map<java.lang.String, java.lang.String> dispatchProperties) throws org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException {
        java.lang.String snmpVersion = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(dispatchProperties, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY);
        try {
            return org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.valueOf(snmpVersion);
        } catch (java.lang.IllegalArgumentException ex) {
            java.lang.String errorMessage = java.lang.String.format("Incorrect SNMP version - \"%s\". Possible values for \"%s\": %s", snmpVersion, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SNMP_VERSION_PROPERTY, java.util.Arrays.toString(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SnmpVersion.values()));
            throw new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException(errorMessage);
        }
    }

    protected org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TrapSecurity getSecurityLevel(java.util.Map<java.lang.String, java.lang.String> dispatchProperties) throws org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException {
        java.lang.String securityLevel = org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.getDispatchProperty(dispatchProperties, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_LEVEL_PROPERTY);
        try {
            return org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TrapSecurity.valueOf(securityLevel);
        } catch (java.lang.IllegalArgumentException ex) {
            java.lang.String errorMessage = java.lang.String.format("Incorrect security level for trap - \"%s\". Possible values for \"%s\": %s", securityLevel, org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.SECURITY_LEVEL_PROPERTY, java.util.Arrays.toString(org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.TrapSecurity.values()));
            throw new org.apache.ambari.server.notifications.dispatchers.SNMPDispatcher.InvalidSnmpConfigurationException(errorMessage);
        }
    }

    private void failureCallback(org.apache.ambari.server.notifications.Notification notification) {
        if (notification.Callback != null) {
            notification.Callback.onFailure(notification.CallbackIds);
        }
    }

    private void successCallback(org.apache.ambari.server.notifications.Notification notification) {
        if (notification.Callback != null) {
            notification.Callback.onSuccess(notification.CallbackIds);
        }
    }

    public java.lang.Integer getPort() {
        return port;
    }

    protected org.snmp4j.transport.UdpTransportMapping getTransportMapping() {
        return transportMapping;
    }
}