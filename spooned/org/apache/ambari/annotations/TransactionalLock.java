package org.apache.ambari.annotations;
import com.google.inject.persist.Transactional;
@java.lang.annotation.Inherited
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ java.lang.annotation.ElementType.METHOD })
public @interface TransactionalLock {
    org.apache.ambari.annotations.TransactionalLock.LockArea lockArea();

    org.apache.ambari.annotations.TransactionalLock.LockType lockType();

    enum LockArea {

        HRC_STATUS_CACHE(org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_ENABLED.getKey());
        private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.annotations.TransactionalLock.LockArea.class);

        private java.lang.String m_configurationProperty;

        private java.lang.Boolean m_enabled = null;

        LockArea(java.lang.String configurationProperty) {
            m_configurationProperty = configurationProperty;
        }

        public boolean isEnabled(org.apache.ambari.server.configuration.Configuration configuration) {
            if (null != m_enabled) {
                return m_enabled.booleanValue();
            }
            m_enabled = java.lang.Boolean.TRUE;
            java.lang.String property = configuration.getProperty(m_configurationProperty);
            if (null != property) {
                try {
                    m_enabled = java.lang.Boolean.valueOf(property);
                } catch (java.lang.Exception exception) {
                    org.apache.ambari.annotations.TransactionalLock.LockArea.LOG.error("Unable to determine if the lock area {} is enabled, defaulting to TRUE", m_configurationProperty, exception);
                }
            }
            org.apache.ambari.annotations.TransactionalLock.LockArea.LOG.info("LockArea {} is {}", name(), m_enabled ? "enabled" : "disabled");
            return m_enabled.booleanValue();
        }

        void clearEnabled() {
            m_enabled = null;
        }
    }

    enum LockType {

        READ,
        WRITE;}
}