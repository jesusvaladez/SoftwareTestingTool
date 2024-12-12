package org.apache.ambari.spi.upgrade;
public class UpgradeCheckDescription {
    public static final java.lang.String DEFAULT = "default";

    private static final java.util.Set<org.apache.ambari.spi.upgrade.UpgradeCheckDescription> s_values = new java.util.LinkedHashSet<>();

    private final java.lang.String m_name;

    private final org.apache.ambari.spi.upgrade.UpgradeCheckType m_type;

    private final java.lang.String m_description;

    private java.util.Map<java.lang.String, java.lang.String> m_fails;

    public UpgradeCheckDescription(java.lang.String name, org.apache.ambari.spi.upgrade.UpgradeCheckType type, java.lang.String description, java.lang.String failureReason) {
        this(name, type, description, new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, failureReason).build());
    }

    public UpgradeCheckDescription(java.lang.String name, org.apache.ambari.spi.upgrade.UpgradeCheckType type, java.lang.String description, java.util.Map<java.lang.String, java.lang.String> fails) {
        m_name = name;
        m_type = type;
        m_description = description;
        m_fails = fails;
        if (org.apache.ambari.spi.upgrade.UpgradeCheckDescription.s_values.contains(this)) {
            throw new java.lang.RuntimeException(("Unable to add the upgrade check description named " + m_name) + " because it already is registered");
        }
        org.apache.ambari.spi.upgrade.UpgradeCheckDescription.s_values.add(this);
    }

    public java.lang.String name() {
        return m_name;
    }

    public java.util.Set<org.apache.ambari.spi.upgrade.UpgradeCheckDescription> values() {
        return org.apache.ambari.spi.upgrade.UpgradeCheckDescription.s_values;
    }

    public org.apache.ambari.spi.upgrade.UpgradeCheckType getType() {
        return m_type;
    }

    public java.lang.String getText() {
        return m_description;
    }

    public java.lang.String getFailureReason(java.lang.String key) {
        return m_fails.containsKey(key) ? m_fails.get(key) : "";
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(m_name);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (null == object) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (object.getClass() != getClass()) {
            return false;
        }
        org.apache.ambari.spi.upgrade.UpgradeCheckDescription that = ((org.apache.ambari.spi.upgrade.UpgradeCheckDescription) (object));
        return java.util.Objects.equals(m_name, that.m_name);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("name", m_name).toString();
    }

    public java.lang.String getDefaultFailureReason() {
        if (null == m_fails) {
            return null;
        }
        if (m_fails.size() == 1) {
            return m_fails.values().stream().findFirst().get();
        }
        return m_fails.get(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT);
    }
}