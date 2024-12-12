package org.apache.ambari.scom.logging;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class JpaLoggerTest {
    @org.junit.Test
    public void testLog() throws java.lang.Exception {
        org.slf4j.Logger logger = EasyMock.createNiceMock(org.slf4j.Logger.class);
        org.eclipse.persistence.logging.SessionLogEntry severeEntry = EasyMock.createNiceMock(org.eclipse.persistence.logging.SessionLogEntry.class);
        org.eclipse.persistence.logging.SessionLogEntry configEntry = EasyMock.createNiceMock(org.eclipse.persistence.logging.SessionLogEntry.class);
        org.eclipse.persistence.logging.SessionLogEntry finestEntry = EasyMock.createNiceMock(org.eclipse.persistence.logging.SessionLogEntry.class);
        java.util.logging.Formatter formatter = EasyMock.createNiceMock(java.util.logging.Formatter.class);
        org.apache.ambari.scom.logging.JpaLogger jpaLogger = new org.apache.ambari.scom.logging.JpaLoggerTest.TestJpaLogger(logger, formatter);
        EasyMock.expect(severeEntry.getLevel()).andReturn(SessionLog.SEVERE);
        EasyMock.expect(formatter.format(((java.util.logging.LogRecord) (EasyMock.anyObject())))).andReturn("severe log message");
        logger.error("severe log message");
        EasyMock.expect(configEntry.getLevel()).andReturn(SessionLog.CONFIG);
        EasyMock.expect(formatter.format(((java.util.logging.LogRecord) (EasyMock.anyObject())))).andReturn("config log message");
        logger.info("config log message");
        EasyMock.expect(finestEntry.getLevel()).andReturn(SessionLog.FINEST);
        EasyMock.expect(formatter.format(((java.util.logging.LogRecord) (EasyMock.anyObject())))).andReturn("finest log message");
        logger.debug("finest log message");
        EasyMock.replay(logger, severeEntry, configEntry, finestEntry, formatter);
        jpaLogger.log(severeEntry);
        jpaLogger.log(configEntry);
        jpaLogger.log(finestEntry);
        EasyMock.verify(logger, severeEntry, configEntry, finestEntry, formatter);
    }

    @org.junit.Test
    public void testThrowing() throws java.lang.Exception {
        org.slf4j.Logger logger = EasyMock.createNiceMock(org.slf4j.Logger.class);
        java.util.logging.Formatter formatter = EasyMock.createNiceMock(java.util.logging.Formatter.class);
        java.lang.Exception exception = new java.lang.IllegalStateException("Something went wrong!");
        org.apache.ambari.scom.logging.JpaLogger jpaLogger = new org.apache.ambari.scom.logging.JpaLoggerTest.TestJpaLogger(logger, formatter);
        logger.error(null, exception);
        EasyMock.replay(logger, formatter);
        jpaLogger.throwing(exception);
        EasyMock.verify(logger, formatter);
    }

    @org.junit.Test
    public void testShouldLog() throws java.lang.Exception {
        org.apache.ambari.scom.logging.JpaLogger logger = new org.apache.ambari.scom.logging.JpaLogger();
        org.junit.Assert.assertTrue(logger.shouldLog(SessionLog.SEVERE, ""));
        org.junit.Assert.assertTrue(logger.shouldLog(SessionLog.WARNING, ""));
        org.junit.Assert.assertFalse(logger.shouldLog(SessionLog.CONFIG, ""));
        org.junit.Assert.assertFalse(logger.shouldLog(SessionLog.FINER, ""));
        org.junit.Assert.assertFalse(logger.shouldLog(SessionLog.ALL, ""));
        logger.setLogLevel(java.util.logging.Level.FINER);
        org.junit.Assert.assertTrue(logger.shouldLog(SessionLog.SEVERE, ""));
        org.junit.Assert.assertTrue(logger.shouldLog(SessionLog.WARNING, ""));
        org.junit.Assert.assertTrue(logger.shouldLog(SessionLog.CONFIG, ""));
        org.junit.Assert.assertTrue(logger.shouldLog(SessionLog.FINER, ""));
        org.junit.Assert.assertFalse(logger.shouldLog(SessionLog.ALL, ""));
        logger.setLogLevel(java.util.logging.Level.SEVERE);
        org.junit.Assert.assertTrue(logger.shouldLog(SessionLog.SEVERE, ""));
        org.junit.Assert.assertFalse(logger.shouldLog(SessionLog.WARNING, ""));
        org.junit.Assert.assertFalse(logger.shouldLog(SessionLog.CONFIG, ""));
        org.junit.Assert.assertFalse(logger.shouldLog(SessionLog.FINER, ""));
        org.junit.Assert.assertFalse(logger.shouldLog(SessionLog.ALL, ""));
    }

    @org.junit.Test
    public void testGetSetLogLevel() throws java.lang.Exception {
        org.apache.ambari.scom.logging.JpaLogger logger = new org.apache.ambari.scom.logging.JpaLogger();
        org.junit.Assert.assertEquals(java.util.logging.Level.WARNING, logger.getLogLevel());
        logger.setLogLevel(java.util.logging.Level.ALL);
        org.junit.Assert.assertEquals(java.util.logging.Level.ALL, logger.getLogLevel());
        logger.setLogLevel(java.util.logging.Level.FINER);
        org.junit.Assert.assertEquals(java.util.logging.Level.FINER, logger.getLogLevel());
        logger.setLogLevel(java.util.logging.Level.OFF);
        org.junit.Assert.assertEquals(java.util.logging.Level.OFF, logger.getLogLevel());
    }

    private static class TestJpaLogger extends org.apache.ambari.scom.logging.JpaLogger {
        private final org.slf4j.Logger logger;

        private final java.util.logging.Formatter formatter;

        private TestJpaLogger(org.slf4j.Logger logger, java.util.logging.Formatter formatter) {
            this.logger = logger;
            this.formatter = formatter;
        }

        @java.lang.Override
        protected org.slf4j.Logger getLogger() {
            return logger;
        }

        @java.lang.Override
        protected java.util.logging.Formatter getFormatter() {
            return formatter;
        }

        @java.lang.Override
        protected java.lang.String formatMessage(org.eclipse.persistence.logging.SessionLogEntry entry) {
            return "message";
        }
    }
}