package org.apache.ambari.spi.upgrade;
public enum UpgradeCheckGroup {

    MAINTENANCE_MODE(1.0F),
    REPOSITORY_VERSION(2.0F),
    NAMENODE_HA(3.0F),
    TOPOLOGY(4.0F),
    LIVELINESS(5.0F),
    CLIENT_RETRY_PROPERTY(6.0F),
    MULTIPLE_COMPONENT_WARNING(7.0F),
    CONFIGURATION_WARNING(8.0F),
    COMPONENT_VERSION(9.0F),
    KERBEROS(10.0F),
    INFORMATIONAL_WARNING(100.0F),
    DEFAULT(java.lang.Float.MAX_VALUE);
    private final java.lang.Float m_order;

    UpgradeCheckGroup(java.lang.Float order) {
        m_order = order;
    }

    public java.lang.Float getOrder() {
        return m_order;
    }
}