package org.apache.ambari.server;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
public class RandomPortJerseyTest extends com.sun.jersey.test.framework.JerseyTest {
    private static int testPort;

    public RandomPortJerseyTest(com.sun.jersey.test.framework.AppDescriptor ad) {
        super(ad);
    }

    @java.lang.Override
    protected int getPort(int defaultPort) {
        java.net.ServerSocket server = null;
        int port = -1;
        try {
            server = new java.net.ServerSocket(defaultPort);
            port = server.getLocalPort();
        } catch (java.io.IOException e) {
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (java.io.IOException e) {
                }
            }
        }
        if ((port != (-1)) || (defaultPort == 0)) {
            this.testPort = port;
            return port;
        }
        return getPort(0);
    }

    public int getTestPort() {
        return org.apache.ambari.server.RandomPortJerseyTest.testPort;
    }
}