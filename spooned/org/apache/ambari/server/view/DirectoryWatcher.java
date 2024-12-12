package org.apache.ambari.server.view;
public interface DirectoryWatcher {
    void start();

    boolean isRunning();

    void stop();
}