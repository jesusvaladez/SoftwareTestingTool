package org.apache.ambari.server.controller.metrics.ganglia;
public class TestHttpUrlConnection extends java.net.HttpURLConnection {
    java.io.InputStream inputStream;

    public TestHttpUrlConnection(java.io.InputStream inputStream) {
        super(null);
        this.inputStream = inputStream;
    }

    @java.lang.Override
    public boolean usingProxy() {
        return false;
    }

    public void disconnect() {
        throw new java.lang.UnsupportedOperationException();
    }

    public void connect() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.InputStream getInputStream() throws java.io.IOException {
        return inputStream;
    }
}