package org.apache.ambari.server.configuration;
import org.apache.log4j.helpers.FileWatchdog;
public class SingleFileWatch {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.configuration.SingleFileWatch.class);

    private final java.io.File file;

    private final org.apache.log4j.helpers.FileWatchdog watchdog;

    private final java.util.function.Consumer<java.io.File> changeListener;

    private volatile boolean started = false;

    public SingleFileWatch(java.io.File file, java.util.function.Consumer<java.io.File> changeListener) {
        this.file = file;
        this.changeListener = changeListener;
        this.watchdog = createWatchDog();
    }

    private org.apache.log4j.helpers.FileWatchdog createWatchDog() {
        org.apache.log4j.helpers.FileWatchdog fileWatch = new org.apache.log4j.helpers.FileWatchdog(file.getAbsolutePath()) {
            @java.lang.Override
            protected void doOnChange() {
                if (started) {
                    notifyListener();
                }
            }
        };
        fileWatch.setDelay(1000);
        fileWatch.setDaemon(true);
        fileWatch.setName("FileWatchdog:" + file.getName());
        return fileWatch;
    }

    private void notifyListener() {
        org.apache.ambari.server.configuration.SingleFileWatch.LOG.info("{} changed. Sending notification.", file);
        try {
            changeListener.accept(file);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.configuration.SingleFileWatch.LOG.warn(("Error while notifying " + this) + " listener", e);
        }
    }

    public void start() {
        org.apache.ambari.server.configuration.SingleFileWatch.LOG.info("Starting " + this);
        started = true;
        watchdog.start();
    }

    public void stop() {
        org.apache.ambari.server.configuration.SingleFileWatch.LOG.info("Stopping " + this);
        started = false;
        watchdog.interrupt();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "SingleFileWatcher:" + file.getName();
    }
}