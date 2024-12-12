package org.apache.ambari.server.controller.utilities;
public interface StreamProvider {
    java.io.InputStream readFrom(java.lang.String spec) throws java.io.IOException;

    java.io.InputStream readFrom(java.lang.String spec, java.lang.String requestMethod, java.lang.String params) throws java.io.IOException;
}