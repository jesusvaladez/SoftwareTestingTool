package org.apache.ambari.log4j.hadoop.mapreduce.jobhistory;
import org.apache.hadoop.tools.rumen.HistoryEvent;
import org.apache.hadoop.util.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
public class JobHistoryAppender extends org.apache.log4j.AppenderSkeleton implements org.apache.log4j.Appender {
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.JobHistoryAppender.class);

    private final java.util.Queue<org.apache.log4j.spi.LoggingEvent> events;

    private org.apache.ambari.log4j.common.LoggingThreadRunnable logThreadRunnable;

    private java.lang.Thread logThread;

    private final org.apache.ambari.log4j.common.LogParser logParser;

    private final org.apache.ambari.log4j.common.LogStore nullStore = new org.apache.ambari.log4j.common.LogStore() {
        @java.lang.Override
        public void persist(org.apache.log4j.spi.LoggingEvent originalEvent, java.lang.Object parsedEvent) throws java.io.IOException {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.JobHistoryAppender.LOG.info(((org.apache.hadoop.tools.rumen.HistoryEvent) (parsedEvent)).toString());
        }

        @java.lang.Override
        public void close() throws java.io.IOException {
        }
    };

    private java.lang.String driver;

    private java.lang.String database;

    private java.lang.String user;

    private java.lang.String password;

    private org.apache.ambari.log4j.common.LogStore logStore;

    public JobHistoryAppender() {
        events = new java.util.concurrent.LinkedBlockingQueue<org.apache.log4j.spi.LoggingEvent>();
        logParser = new org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryParser();
        logStore = nullStore;
    }

    public java.lang.String getDatabase() {
        return database;
    }

    public void setDatabase(java.lang.String database) {
        this.database = database;
    }

    public java.lang.String getDriver() {
        return driver;
    }

    public void setDriver(java.lang.String driver) {
        this.driver = driver;
    }

    public java.lang.String getUser() {
        return user;
    }

    public void setUser(java.lang.String user) {
        this.user = user;
    }

    public java.lang.String getPassword() {
        return password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    @java.lang.Override
    public void activateOptions() {
        synchronized(this) {
            if (database.equals("none")) {
                logStore = nullStore;
                org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.JobHistoryAppender.LOG.info("database set to 'none'");
            } else {
                try {
                    logStore = new org.apache.ambari.log4j.common.store.DatabaseStore(driver, database, user, password, new org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.MapReduceJobHistoryUpdater());
                } catch (java.io.IOException ioe) {
                    org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.JobHistoryAppender.LOG.debug("Failed to connect to db " + database, ioe);
                    java.lang.System.err.println((((((((("Failed to connect to db " + database) + " as user ") + user) + " password ") + password) + " and driver ") + driver) + " with ") + org.apache.hadoop.util.StringUtils.stringifyException(ioe));
                    throw new java.lang.RuntimeException("Failed to create database store for " + database, ioe);
                } catch (java.lang.Exception e) {
                    org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.JobHistoryAppender.LOG.debug("Failed to connect to db " + database, e);
                    java.lang.System.err.println((((((((("Failed to connect to db " + database) + " as user ") + user) + " password ") + password) + " and driver ") + driver) + " with ") + org.apache.hadoop.util.StringUtils.stringifyException(e));
                    throw new java.lang.RuntimeException("Failed to create database store for " + database, e);
                }
            }
            logThreadRunnable = new org.apache.ambari.log4j.common.LoggingThreadRunnable(events, logParser, logStore);
            logThread = new java.lang.Thread(logThreadRunnable);
            logThread.setDaemon(true);
            logThread.start();
            super.activateOptions();
        }
    }

    @java.lang.Override
    public void close() {
        try {
            logThreadRunnable.close();
        } catch (java.io.IOException ioe) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.JobHistoryAppender.LOG.info("Failed to close logThreadRunnable", ioe);
        }
        try {
            logThread.join(1000);
        } catch (java.lang.InterruptedException ie) {
            org.apache.ambari.log4j.hadoop.mapreduce.jobhistory.JobHistoryAppender.LOG.info("logThread interrupted", ie);
        }
    }

    @java.lang.Override
    public boolean requiresLayout() {
        return false;
    }

    @java.lang.Override
    protected void append(org.apache.log4j.spi.LoggingEvent event) {
        events.add(event);
    }
}