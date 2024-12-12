package org.apache.ambari.spi.exceptions;
public class UpgradeActionException extends org.apache.ambari.server.AmbariException {
    private final java.lang.Class<? extends org.apache.ambari.spi.upgrade.UpgradeAction> m_failedClass;

    public UpgradeActionException(java.lang.Class<? extends org.apache.ambari.spi.upgrade.UpgradeAction> failedClass, java.lang.String message) {
        super(message);
        m_failedClass = failedClass;
    }

    public UpgradeActionException(java.lang.Class<? extends org.apache.ambari.spi.upgrade.UpgradeAction> failedClass, java.lang.String message, java.lang.Throwable cause) {
        super(message, cause);
        m_failedClass = failedClass;
    }

    public java.lang.Class<? extends org.apache.ambari.spi.upgrade.UpgradeAction> getFailedClass() {
        return m_failedClass;
    }
}