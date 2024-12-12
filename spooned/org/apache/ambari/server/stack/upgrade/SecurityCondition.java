package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlType(name = "security")
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public final class SecurityCondition extends org.apache.ambari.server.stack.upgrade.Condition {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.SecurityCondition.class);

    @javax.xml.bind.annotation.XmlAttribute(name = "type")
    public org.apache.ambari.server.state.SecurityType securityType;

    @javax.xml.bind.annotation.XmlAttribute(name = "kdc-type", required = false)
    public org.apache.ambari.server.serveraction.kerberos.KDCType kdctype;

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("type", securityType).add("kdcType", kdctype).omitNullValues().toString();
    }

    @java.lang.Override
    public boolean isSatisfied(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) {
        org.apache.ambari.server.state.Cluster cluster = upgradeContext.getCluster();
        if (cluster.getSecurityType() != securityType) {
            return false;
        }
        switch (securityType) {
            case KERBEROS :
                if (null != kdctype) {
                    try {
                        org.apache.ambari.server.controller.KerberosDetails kerberosDetails = upgradeContext.getKerberosDetails();
                        return kerberosDetails.getKdcType() == kdctype;
                    } catch (org.apache.ambari.server.AmbariException | org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException kerberosException) {
                        org.apache.ambari.server.stack.upgrade.SecurityCondition.LOG.error("Unable to determine if this upgrade condition is met because there was a problem parsing the Kerberos configruations for the KDC Type", kerberosException);
                        return false;
                    }
                }
                return true;
            case NONE :
                return true;
            default :
                return true;
        }
    }
}