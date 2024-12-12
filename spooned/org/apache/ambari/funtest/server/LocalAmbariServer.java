package org.apache.ambari.funtest.server;
public class LocalAmbariServer implements java.lang.Runnable {
    private static org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(org.apache.ambari.server.controller.AmbariServer.class);

    private org.apache.ambari.server.controller.AmbariServer ambariServer = null;

    @com.google.inject.Inject
    private com.google.inject.Injector injector;

    public LocalAmbariServer() {
    }

    @java.lang.Override
    public void run() {
        try {
            startServer();
        } catch (java.lang.Exception ex) {
            org.apache.ambari.funtest.server.LocalAmbariServer.LOG.info("Exception received ", ex);
            throw new java.lang.RuntimeException(ex);
        }
    }

    private void startServer() throws java.lang.Exception {
        try {
            org.apache.ambari.funtest.server.LocalAmbariServer.LOG.info("Attempting to start ambari server...");
            org.apache.ambari.server.controller.AmbariServer.setupProxyAuth();
            ambariServer = injector.getInstance(org.apache.ambari.server.controller.AmbariServer.class);
            ambariServer.initViewRegistry();
            ambariServer.run();
        } catch (java.lang.InterruptedException ex) {
            org.apache.ambari.funtest.server.LocalAmbariServer.LOG.info(ex);
        } catch (java.lang.Throwable t) {
            org.apache.ambari.funtest.server.LocalAmbariServer.LOG.error("Failed to run the Ambari Server", t);
            stopServer();
            throw t;
        }
    }

    public void stopServer() throws java.lang.Exception {
        org.apache.ambari.funtest.server.LocalAmbariServer.LOG.info("Stopping ambari server...");
        if (ambariServer != null) {
            ambariServer.stop();
        }
    }
}