package org.apache.ambari.view;
public interface URLStreamProvider {
    public java.io.InputStream readFrom(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException;

    public java.io.InputStream readFrom(java.lang.String spec, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException;

    public java.io.InputStream readAs(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers, java.lang.String userName) throws java.io.IOException;

    public java.io.InputStream readAs(java.lang.String spec, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers, java.lang.String userName) throws java.io.IOException;

    public java.io.InputStream readAsCurrent(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException;

    public java.io.InputStream readAsCurrent(java.lang.String spec, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException;
}