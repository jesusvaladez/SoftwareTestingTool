package org.apache.ambari.server.view;
public class RemoteAmbariStreamProvider implements org.apache.ambari.view.AmbariStreamProvider {
    private java.lang.String baseUrl;

    private java.lang.String username;

    private java.lang.String password;

    private org.apache.ambari.server.controller.internal.URLStreamProvider urlStreamProvider;

    public RemoteAmbariStreamProvider(java.lang.String baseUrl, java.lang.String username, java.lang.String password, int connectTimeout, int readTimeout) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance();
        this.urlStreamProvider = new org.apache.ambari.server.controller.internal.URLStreamProvider(connectTimeout, readTimeout, sslConfiguration.getTruststorePath(), sslConfiguration.getTruststorePassword(), sslConfiguration.getTruststoreType());
    }

    @java.lang.Override
    public java.io.InputStream readFrom(java.lang.String path, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException, org.apache.ambari.view.AmbariHttpException {
        return getInputStream(urlStreamProvider.processURL(getUrl(path), requestMethod, body, addHeaders(headers)));
    }

    @java.lang.Override
    public java.io.InputStream readFrom(java.lang.String path, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException, org.apache.ambari.view.AmbariHttpException {
        return getInputStream(urlStreamProvider.processURL(getUrl(path), requestMethod, body, addHeaders(headers)));
    }

    private java.io.InputStream getInputStream(java.net.HttpURLConnection connection) throws java.io.IOException, org.apache.ambari.view.AmbariHttpException {
        int responseCode = connection.getResponseCode();
        if (responseCode >= org.apache.ambari.server.proxy.ProxyService.HTTP_ERROR_RANGE_START) {
            throw new org.apache.ambari.view.AmbariHttpException(org.apache.commons.io.IOUtils.toString(connection.getErrorStream()), responseCode);
        }
        return connection.getInputStream();
    }

    private java.lang.String getUrl(java.lang.String path) {
        java.lang.String basePath = baseUrl;
        return path.startsWith("/") ? basePath + path : (basePath + "/") + path;
    }

    private void addRequestedByHeaders(java.util.HashMap<java.lang.String, java.lang.String> newHeaders) {
        newHeaders.put("X-Requested-By", "AMBARI");
    }

    private java.util.Map<java.lang.String, java.util.List<java.lang.String>> modifyHeaders(java.util.Map<java.lang.String, java.lang.String> headers) {
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : headers.entrySet()) {
            headerMap.put(entry.getKey(), java.util.Collections.singletonList(entry.getValue()));
        }
        return headerMap;
    }

    private java.util.Map<java.lang.String, java.util.List<java.lang.String>> addHeaders(java.util.Map<java.lang.String, java.lang.String> customHeaders) {
        java.util.HashMap<java.lang.String, java.lang.String> newHeaders = new java.util.HashMap<>();
        if (customHeaders != null)
            newHeaders.putAll(customHeaders);

        addBasicAuthHeaders(newHeaders);
        addRequestedByHeaders(newHeaders);
        return modifyHeaders(newHeaders);
    }

    private void addBasicAuthHeaders(java.util.HashMap<java.lang.String, java.lang.String> headers) {
        java.lang.String authString = (username + ":") + password;
        byte[] authEncBytes = org.apache.commons.codec.binary.Base64.encodeBase64(authString.getBytes());
        java.lang.String authStringEnc = new java.lang.String(authEncBytes);
        headers.put("Authorization", "Basic " + authStringEnc);
    }
}