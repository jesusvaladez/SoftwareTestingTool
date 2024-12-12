package org.apache.ambari.log4j.common;
import org.apache.log4j.spi.LoggingEvent;
public class LoggingThreadRunnable implements java.lang.Runnable {
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(org.apache.ambari.log4j.common.LoggingThreadRunnable.class);

    private static long WAIT_EMPTY_QUEUE = 60000;

    private final java.util.Queue<org.apache.log4j.spi.LoggingEvent> events;

    private final org.apache.ambari.log4j.common.LogParser parser;

    private final org.apache.ambari.log4j.common.LogStore store;

    private final java.util.concurrent.atomic.AtomicBoolean done = new java.util.concurrent.atomic.AtomicBoolean(false);

    public LoggingThreadRunnable(java.util.Queue<org.apache.log4j.spi.LoggingEvent> events, org.apache.ambari.log4j.common.LogParser parser, org.apache.ambari.log4j.common.LogStore provider) {
        this.events = events;
        this.store = provider;
        this.parser = parser;
    }

    @java.lang.Override
    public void run() {
        while (!done.get()) {
            org.apache.log4j.spi.LoggingEvent event = null;
            while ((event = events.poll()) != null) {
                java.lang.Object result = null;
                try {
                    parser.addEventToParse(event);
                    while ((result = parser.getParseResult()) != null) {
                        try {
                            store.persist(event, result);
                        } catch (java.io.IOException e) {
                            org.apache.ambari.log4j.common.LoggingThreadRunnable.LOG.warn("Failed to persist " + result);
                        }
                    } 
                } catch (java.io.IOException ioe) {
                    org.apache.ambari.log4j.common.LoggingThreadRunnable.LOG.warn("Failed to parse log-event: " + event);
                }
            } 
            try {
                java.lang.Thread.sleep(org.apache.ambari.log4j.common.LoggingThreadRunnable.WAIT_EMPTY_QUEUE);
            } catch (java.lang.InterruptedException ie) {
            }
        } 
        try {
            store.close();
        } catch (java.io.IOException ioe) {
            org.apache.ambari.log4j.common.LoggingThreadRunnable.LOG.info("Failed to close logStore", ioe);
        }
    }

    public void close() throws java.io.IOException {
        done.set(true);
    }
}