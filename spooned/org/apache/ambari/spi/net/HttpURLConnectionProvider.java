package org.apache.ambari.spi.net;
public interface HttpURLConnectionProvider {
    java.net.HttpURLConnection getConnection(java.lang.String url, java.util.Map<java.lang.String, java.util.List<java.lang.String>> headers) throws java.io.IOException;
}