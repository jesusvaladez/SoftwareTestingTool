package org.apache.ambari.spi.upgrade;
import org.apache.commons.lang.StringUtils;
public class UpgradeActionOperations {
    private java.util.List<org.apache.ambari.spi.upgrade.UpgradeActionOperations.ConfigurationChanges> m_configurationChanges;

    private java.util.Set<java.lang.String> m_configurationTypeRemovals;

    private java.lang.String m_standardOutput;

    public org.apache.ambari.spi.upgrade.UpgradeActionOperations setConfigurationChanges(org.apache.ambari.spi.upgrade.UpgradeActionOperations.ConfigurationChanges configurationChanges) {
        setConfigurationChanges(java.util.Collections.singletonList(configurationChanges));
        return this;
    }

    public org.apache.ambari.spi.upgrade.UpgradeActionOperations setConfigurationChanges(java.util.List<org.apache.ambari.spi.upgrade.UpgradeActionOperations.ConfigurationChanges> configurationChanges) {
        m_configurationChanges = configurationChanges;
        return this;
    }

    public org.apache.ambari.spi.upgrade.UpgradeActionOperations addConfigurationChange(org.apache.ambari.spi.upgrade.UpgradeActionOperations.ConfigurationChanges configurationChanges) {
        if (null == m_configurationChanges) {
            m_configurationChanges = new java.util.ArrayList<>();
        }
        m_configurationChanges.add(configurationChanges);
        return this;
    }

    public org.apache.ambari.spi.upgrade.UpgradeActionOperations setConfigurationTypeRemoval(java.util.Set<java.lang.String> configurationTypeRemovals) {
        m_configurationTypeRemovals = configurationTypeRemovals;
        return this;
    }

    public org.apache.ambari.spi.upgrade.UpgradeActionOperations setStandardOutput(java.lang.String standardOutput) {
        m_standardOutput = standardOutput;
        return this;
    }

    public java.util.List<org.apache.ambari.spi.upgrade.UpgradeActionOperations.ConfigurationChanges> getConfigurationChanges() {
        return m_configurationChanges;
    }

    public java.util.Set<java.lang.String> getConfigurationTypeRemovals() {
        return m_configurationTypeRemovals;
    }

    public java.lang.String getStandardOutput() {
        return m_standardOutput;
    }

    public enum ChangeType {

        SET,
        REMOVE;}

    public static class ConfigurationChanges {
        private final java.lang.String m_configType;

        private final java.util.List<org.apache.ambari.spi.upgrade.UpgradeActionOperations.PropertyChange> m_changes = new java.util.ArrayList<>();

        private boolean m_onlyRemovals = true;

        public ConfigurationChanges(java.lang.String configType) {
            m_configType = configType;
        }

        public org.apache.ambari.spi.upgrade.UpgradeActionOperations.ConfigurationChanges set(java.lang.String propertyName, java.lang.String propertyValue) {
            m_onlyRemovals = false;
            org.apache.ambari.spi.upgrade.UpgradeActionOperations.PropertyChange propertyChange = new org.apache.ambari.spi.upgrade.UpgradeActionOperations.PropertyChange(org.apache.ambari.spi.upgrade.UpgradeActionOperations.ChangeType.SET, propertyName, propertyValue);
            m_changes.add(propertyChange);
            return this;
        }

        public org.apache.ambari.spi.upgrade.UpgradeActionOperations.ConfigurationChanges remove(java.lang.String propertyName) {
            org.apache.ambari.spi.upgrade.UpgradeActionOperations.PropertyChange propertyChange = new org.apache.ambari.spi.upgrade.UpgradeActionOperations.PropertyChange(org.apache.ambari.spi.upgrade.UpgradeActionOperations.ChangeType.REMOVE, propertyName, null);
            m_changes.add(propertyChange);
            return this;
        }

        public java.lang.String getConfigType() {
            return m_configType;
        }

        public java.util.List<org.apache.ambari.spi.upgrade.UpgradeActionOperations.PropertyChange> getPropertyChanges() {
            return m_changes;
        }

        public boolean isOnlyRemovals() {
            return m_onlyRemovals;
        }

        public boolean isEmpty() {
            return (null == m_changes) || m_changes.isEmpty();
        }

        @java.lang.Override
        public java.lang.String toString() {
            if (m_changes.isEmpty()) {
                return "There are no configuration changes";
            }
            java.lang.StringBuilder buffer = new java.lang.StringBuilder(m_configType);
            buffer.append(java.lang.System.lineSeparator());
            for (org.apache.ambari.spi.upgrade.UpgradeActionOperations.PropertyChange propertyChange : m_changes) {
                switch (propertyChange.getChangeType()) {
                    case REMOVE :
                        buffer.append("  Removed ").append(propertyChange.getPropertyName());
                        break;
                    case SET :
                        buffer.append("  Set ").append(propertyChange.getPropertyName()).append(" to ").append(org.apache.commons.lang.StringUtils.abbreviateMiddle(propertyChange.getPropertyValue(), "…", 30));
                        break;
                    default :
                        break;
                }
            }
            return buffer.toString();
        }
    }

    public static class PropertyChange {
        private final org.apache.ambari.spi.upgrade.UpgradeActionOperations.ChangeType m_changeType;

        private final java.lang.String m_propertyName;

        private final java.lang.String m_propertyValue;

        public PropertyChange(org.apache.ambari.spi.upgrade.UpgradeActionOperations.ChangeType changeType, java.lang.String propertyName, java.lang.String propertyValue) {
            m_changeType = changeType;
            m_propertyName = propertyName;
            m_propertyValue = propertyValue;
        }

        public org.apache.ambari.spi.upgrade.UpgradeActionOperations.ChangeType getChangeType() {
            return m_changeType;
        }

        public java.lang.String getPropertyName() {
            return m_propertyName;
        }

        public java.lang.String getPropertyValue() {
            return m_propertyValue;
        }
    }
}