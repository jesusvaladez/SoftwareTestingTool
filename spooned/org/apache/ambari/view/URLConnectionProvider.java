package org.apache.ambari.view;
public interface URLConnectionProvider {
    public java.net.HttpURLConnection getConnection(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException;

    public java.net.HttpURLConnection getConnection(java.lang.String spec, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException;

    public java.net.HttpURLConnection getConnectionAs(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers, java.lang.String userName) throws java.io.IOException;

    public java.net.HttpURLConnection getConnectionAs(java.lang.String spec, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers, java.lang.String userName) throws java.io.IOException;

    public java.net.HttpURLConnection getConnectionAsCurrent(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException;

    public java.net.HttpURLConnection getConnectionAsCurrent(java.lang.String spec, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException;
}