package org.apache.ambari.server.view;
public class ViewAmbariStreamProvider implements org.apache.ambari.view.AmbariStreamProvider {
    private final org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider;

    private final org.apache.ambari.server.controller.AmbariSessionManager ambariSessionManager;

    private final org.apache.ambari.server.controller.AmbariManagementController controller;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.view.ViewAmbariStreamProvider.class);

    protected ViewAmbariStreamProvider(org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.controller.AmbariSessionManager ambariSessionManager, org.apache.ambari.server.controller.AmbariManagementController controller) {
        this.streamProvider = streamProvider;
        this.ambariSessionManager = ambariSessionManager;
        this.controller = controller;
    }

    @java.lang.Override
    public java.io.InputStream readFrom(java.lang.String path, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException, org.apache.ambari.view.AmbariHttpException {
        return getInputStream(path, requestMethod, headers, body == null ? null : body.getBytes());
    }

    @java.lang.Override
    public java.io.InputStream readFrom(java.lang.String path, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException, org.apache.ambari.view.AmbariHttpException {
        return getInputStream(path, requestMethod, headers, body == null ? null : org.apache.commons.io.IOUtils.toByteArray(body));
    }

    private java.io.InputStream getInputStream(java.lang.String path, java.lang.String requestMethod, java.util.Map<java.lang.String, java.lang.String> headers, byte[] body) throws java.io.IOException, org.apache.ambari.view.AmbariHttpException {
        java.lang.String sessionId = ambariSessionManager.getCurrentSessionId();
        if (sessionId != null) {
            java.lang.String ambariSessionCookie = (ambariSessionManager.getSessionCookie() + "=") + sessionId;
            if ((headers == null) || headers.isEmpty()) {
                headers = java.util.Collections.singletonMap(org.apache.ambari.server.controller.internal.URLStreamProvider.COOKIE, ambariSessionCookie);
            } else {
                headers = new java.util.HashMap<>(headers);
                java.lang.String cookies = headers.get(org.apache.ambari.server.controller.internal.URLStreamProvider.COOKIE);
                headers.put(org.apache.ambari.server.controller.internal.URLStreamProvider.COOKIE, org.apache.ambari.server.controller.internal.URLStreamProvider.appendCookie(cookies, ambariSessionCookie));
            }
        }
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : headers.entrySet()) {
            headerMap.put(entry.getKey(), java.util.Collections.singletonList(entry.getValue()));
        }
        return getInputStream(streamProvider.processURL(controller.getAmbariServerURI(path.startsWith("/") ? path : "/" + path), requestMethod, body, headerMap));
    }

    private java.io.InputStream getInputStream(java.net.HttpURLConnection connection) throws java.io.IOException, org.apache.ambari.view.AmbariHttpException {
        int responseCode = connection.getResponseCode();
        if (responseCode >= org.apache.ambari.server.proxy.ProxyService.HTTP_ERROR_RANGE_START) {
            java.lang.String message = connection.getResponseMessage();
            if (connection.getErrorStream() != null) {
                message = org.apache.commons.io.IOUtils.toString(connection.getErrorStream());
            }
            org.apache.ambari.server.view.ViewAmbariStreamProvider.LOG.error("Got error response for url {}. Response code:{}. {}", connection.getURL(), responseCode, message);
            throw new org.apache.ambari.view.AmbariHttpException(message, responseCode);
        }
        return connection.getInputStream();
    }
}