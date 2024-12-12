package org.apache.ambari.server.logging;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
public class EclipseLinkLogger extends org.eclipse.persistence.logging.AbstractSessionLog implements org.eclipse.persistence.logging.SessionLog {
    private static final org.slf4j.Logger JPA_LOG = org.slf4j.LoggerFactory.getLogger("eclipselink");

    private static final java.lang.String LOG_TEMPLATE = "[EL {}]: {} {}";

    @java.lang.Override
    public void log(org.eclipse.persistence.logging.SessionLogEntry sessionLogEntry) {
        int level = sessionLogEntry.getLevel();
        if (!shouldLog(level, sessionLogEntry.getNameSpace())) {
            return;
        }
        switch (level) {
            case org.eclipse.persistence.logging.SessionLog.ALL :
            case org.eclipse.persistence.logging.SessionLog.FINER :
            case org.eclipse.persistence.logging.SessionLog.FINEST :
                org.apache.ambari.server.logging.EclipseLinkLogger.JPA_LOG.trace(org.apache.ambari.server.logging.EclipseLinkLogger.LOG_TEMPLATE, "Trace", getSupplementDetailString(sessionLogEntry), formatMessage(sessionLogEntry));
                return;
            case org.eclipse.persistence.logging.SessionLog.INFO :
            case org.eclipse.persistence.logging.SessionLog.CONFIG :
                org.apache.ambari.server.logging.EclipseLinkLogger.JPA_LOG.info(org.apache.ambari.server.logging.EclipseLinkLogger.LOG_TEMPLATE, "Info", getSupplementDetailString(sessionLogEntry), formatMessage(sessionLogEntry));
                return;
            case org.eclipse.persistence.logging.SessionLog.FINE :
                org.apache.ambari.server.logging.EclipseLinkLogger.JPA_LOG.debug(org.apache.ambari.server.logging.EclipseLinkLogger.LOG_TEMPLATE, "Debug", getSupplementDetailString(sessionLogEntry), formatMessage(sessionLogEntry));
                return;
            case org.eclipse.persistence.logging.SessionLog.SEVERE :
                org.apache.ambari.server.logging.EclipseLinkLogger.JPA_LOG.error(org.apache.ambari.server.logging.EclipseLinkLogger.LOG_TEMPLATE, "Error", getSupplementDetailString(sessionLogEntry), formatMessage(sessionLogEntry));
                return;
            case org.eclipse.persistence.logging.SessionLog.WARNING :
                org.apache.ambari.server.logging.EclipseLinkLogger.JPA_LOG.warn(org.apache.ambari.server.logging.EclipseLinkLogger.LOG_TEMPLATE, "Warning", getSupplementDetailString(sessionLogEntry), formatMessage(sessionLogEntry));
                return;
            case org.eclipse.persistence.logging.SessionLog.OFF :
                return;
            default :
                org.apache.ambari.server.logging.EclipseLinkLogger.JPA_LOG.debug(org.apache.ambari.server.logging.EclipseLinkLogger.LOG_TEMPLATE, "Unknown", getSupplementDetailString(sessionLogEntry), formatMessage(sessionLogEntry));
                return;
        }
    }
}