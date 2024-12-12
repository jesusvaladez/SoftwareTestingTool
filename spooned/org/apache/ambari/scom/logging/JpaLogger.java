package org.apache.ambari.scom.logging;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.EclipseLinkLogRecord;
import org.eclipse.persistence.logging.SessionLogEntry;
public class JpaLogger extends org.eclipse.persistence.logging.AbstractSessionLog {
    private final java.util.logging.Formatter formatter = new java.util.logging.SimpleFormatter();

    public java.util.logging.Level logLevel = java.util.logging.Level.WARNING;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.scom.logging.JpaLogger.class);

    public static final java.util.logging.Level[] JAVA_LOG_LEVELS = new java.util.logging.Level[]{ java.util.logging.Level.ALL, java.util.logging.Level.FINEST, java.util.logging.Level.FINER, java.util.logging.Level.FINE, java.util.logging.Level.CONFIG, java.util.logging.Level.INFO, java.util.logging.Level.WARNING, java.util.logging.Level.SEVERE, java.util.logging.Level.OFF };

    @java.lang.Override
    public void log(org.eclipse.persistence.logging.SessionLogEntry sessionLogEntry) {
        org.slf4j.Logger logger = getLogger();
        switch (sessionLogEntry.getLevel()) {
            case SEVERE :
                logger.error(getLogMessage(sessionLogEntry));
                break;
            case WARNING :
                logger.warn(getLogMessage(sessionLogEntry));
                break;
            case INFO :
            case CONFIG :
                logger.info(getLogMessage(sessionLogEntry));
                break;
            case FINE :
            case FINER :
            case FINEST :
                logger.debug(getLogMessage(sessionLogEntry));
                break;
            case ALL :
                logger.trace(getLogMessage(sessionLogEntry));
                break;
        }
    }

    @java.lang.Override
    public void throwing(java.lang.Throwable throwable) {
        getLogger().error(null, throwable);
    }

    @java.lang.Override
    public boolean shouldLog(int level, java.lang.String category) {
        return org.apache.ambari.scom.logging.JpaLogger.getJavaLogLevel(level).intValue() >= logLevel.intValue();
    }

    public java.util.logging.Level getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(java.util.logging.Level logLevel) {
        this.logLevel = logLevel;
    }

    protected org.slf4j.Logger getLogger() {
        return org.apache.ambari.scom.logging.JpaLogger.LOG;
    }

    protected java.util.logging.Formatter getFormatter() {
        return formatter;
    }

    private java.lang.String getLogMessage(org.eclipse.persistence.logging.SessionLogEntry sessionLogEntry) {
        return getFormatter().format(getLogRecord(sessionLogEntry, org.apache.ambari.scom.logging.JpaLogger.getJavaLogLevel(sessionLogEntry.getLevel())));
    }

    private org.eclipse.persistence.logging.EclipseLinkLogRecord getLogRecord(org.eclipse.persistence.logging.SessionLogEntry sessionLogEntry, java.util.logging.Level level) {
        org.eclipse.persistence.logging.EclipseLinkLogRecord logRecord = new org.eclipse.persistence.logging.EclipseLinkLogRecord(level, formatMessage(sessionLogEntry));
        logRecord.setLoggerName(sessionLogEntry.getNameSpace());
        logRecord.setShouldPrintDate(shouldPrintDate());
        logRecord.setShouldPrintThread(shouldPrintThread());
        java.lang.Throwable exception = sessionLogEntry.getException();
        if (exception != null) {
            logRecord.setThrown(exception);
            logRecord.setShouldLogExceptionStackTrace(shouldLogExceptionStackTrace());
        }
        if (shouldPrintConnection()) {
            logRecord.setConnection(sessionLogEntry.getConnection());
        }
        if (shouldPrintSession()) {
            logRecord.setSessionString(getSessionString(sessionLogEntry.getSession()));
        }
        return logRecord;
    }

    private static java.util.logging.Level getJavaLogLevel(int level) {
        return (level >= ALL) && (level <= OFF) ? org.apache.ambari.scom.logging.JpaLogger.JAVA_LOG_LEVELS[level] : java.util.logging.Level.OFF;
    }
}