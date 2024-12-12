package org.apache.ambari.view;
public interface AmbariClusterStreamProvider {
    public java.io.InputStream readFromCluster(java.lang.String path, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException, org.apache.ambari.view.AmbariHttpException;

    public java.io.InputStream readFromCluster(java.lang.String path, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException, org.apache.ambari.view.AmbariHttpException;

    public java.io.InputStream readFromAmbari(java.lang.String path, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException, org.apache.ambari.view.AmbariHttpException;

    public java.io.InputStream readFromAmbari(java.lang.String path, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException, org.apache.ambari.view.AmbariHttpException;
}