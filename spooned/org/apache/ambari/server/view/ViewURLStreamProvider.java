package org.apache.ambari.server.view;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
public class ViewURLStreamProvider implements org.apache.ambari.view.URLStreamProvider , org.apache.ambari.view.URLConnectionProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.view.ViewContextImpl.class);

    private static final java.lang.String DO_AS_PARAM = "doAs";

    private final org.apache.ambari.view.ViewContext viewContext;

    private final org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider;

    private org.apache.ambari.server.view.ViewURLStreamProvider.HostPortRestrictionHandler hostPortRestrictionHandler;

    protected ViewURLStreamProvider(org.apache.ambari.view.ViewContext viewContext, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider) {
        this.viewContext = viewContext;
        this.streamProvider = streamProvider;
    }

    private org.apache.ambari.server.view.ViewURLStreamProvider.HostPortRestrictionHandler getHostPortRestrictionHandler() {
        if (hostPortRestrictionHandler == null) {
            org.apache.ambari.server.view.ViewURLStreamProvider.HostPortRestrictionHandler hostPortRestrictionHandlerTmp = new org.apache.ambari.server.view.ViewURLStreamProvider.HostPortRestrictionHandler(viewContext.getAmbariProperty(org.apache.ambari.server.configuration.Configuration.PROXY_ALLOWED_HOST_PORTS.getKey()));
            hostPortRestrictionHandler = hostPortRestrictionHandlerTmp;
        }
        return hostPortRestrictionHandler;
    }

    @java.lang.Override
    public java.io.InputStream readFrom(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException {
        return getInputStream(spec, requestMethod, headers, body == null ? null : body.getBytes());
    }

    @java.lang.Override
    public java.io.InputStream readFrom(java.lang.String spec, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException {
        return getInputStream(spec, requestMethod, headers, body == null ? null : org.apache.commons.io.IOUtils.toByteArray(body));
    }

    @java.lang.Override
    public java.io.InputStream readAs(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers, java.lang.String userName) throws java.io.IOException {
        return readFrom(addDoAs(spec, userName), requestMethod, body, headers);
    }

    @java.lang.Override
    public java.io.InputStream readAs(java.lang.String spec, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers, java.lang.String userName) throws java.io.IOException {
        return readFrom(addDoAs(spec, userName), requestMethod, body, headers);
    }

    @java.lang.Override
    public java.io.InputStream readAsCurrent(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException {
        return readAs(spec, requestMethod, body, headers, viewContext.getUsername());
    }

    @java.lang.Override
    public java.io.InputStream readAsCurrent(java.lang.String spec, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException {
        return readAs(spec, requestMethod, body, headers, viewContext.getUsername());
    }

    @java.lang.Override
    public java.net.HttpURLConnection getConnection(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException {
        return getHttpURLConnection(spec, requestMethod, headers, body == null ? null : body.getBytes());
    }

    @java.lang.Override
    public java.net.HttpURLConnection getConnection(java.lang.String spec, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException {
        return getHttpURLConnection(spec, requestMethod, headers, body == null ? null : org.apache.commons.io.IOUtils.toByteArray(body));
    }

    @java.lang.Override
    public java.net.HttpURLConnection getConnectionAs(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers, java.lang.String userName) throws java.io.IOException {
        return getConnection(addDoAs(spec, userName), requestMethod, body, headers);
    }

    @java.lang.Override
    public java.net.HttpURLConnection getConnectionAs(java.lang.String spec, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers, java.lang.String userName) throws java.io.IOException {
        return getConnection(addDoAs(spec, userName), requestMethod, body, headers);
    }

    @java.lang.Override
    public java.net.HttpURLConnection getConnectionAsCurrent(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException {
        return getConnectionAs(spec, requestMethod, body, headers, viewContext.getUsername());
    }

    @java.lang.Override
    public java.net.HttpURLConnection getConnectionAsCurrent(java.lang.String spec, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException {
        return getConnectionAs(spec, requestMethod, body, headers, viewContext.getUsername());
    }

    private java.lang.String addDoAs(java.lang.String spec, java.lang.String userName) throws java.io.IOException {
        if (spec.toLowerCase().contains(org.apache.ambari.server.view.ViewURLStreamProvider.DO_AS_PARAM)) {
            throw new java.lang.IllegalArgumentException(("URL cannot contain \"" + org.apache.ambari.server.view.ViewURLStreamProvider.DO_AS_PARAM) + "\" parameter.");
        }
        try {
            org.apache.http.client.utils.URIBuilder builder = new org.apache.http.client.utils.URIBuilder(spec);
            builder.addParameter(org.apache.ambari.server.view.ViewURLStreamProvider.DO_AS_PARAM, userName);
            return builder.build().toString();
        } catch (java.net.URISyntaxException e) {
            throw new java.io.IOException(e);
        }
    }

    private java.io.InputStream getInputStream(java.lang.String spec, java.lang.String requestMethod, java.util.Map<java.lang.String, java.lang.String> headers, byte[] info) throws java.io.IOException {
        if (!isProxyCallAllowed(spec)) {
            org.apache.ambari.server.view.ViewURLStreamProvider.LOG.warn(("Call to " + spec) + " is not allowed. See ambari.properties proxy.allowed.hostports.");
            throw new java.io.IOException(("Call to " + spec) + " is not allowed. See ambari.properties proxy.allowed.hostports.");
        }
        java.net.HttpURLConnection connection = getHttpURLConnection(spec, requestMethod, headers, info);
        int responseCode = connection.getResponseCode();
        return responseCode >= org.apache.ambari.server.proxy.ProxyService.HTTP_ERROR_RANGE_START ? connection.getErrorStream() : connection.getInputStream();
    }

    private java.net.HttpURLConnection getHttpURLConnection(java.lang.String spec, java.lang.String requestMethod, java.util.Map<java.lang.String, java.lang.String> headers, byte[] info) throws java.io.IOException {
        if (!isProxyCallAllowed(spec)) {
            org.apache.ambari.server.view.ViewURLStreamProvider.LOG.warn(("Call to " + spec) + " is not allowed. See ambari.properties proxy.allowed.hostports.");
            throw new java.io.IOException(("Call to " + spec) + " is not allowed. See ambari.properties proxy.allowed.hostports.");
        }
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : headers.entrySet()) {
            headerMap.put(entry.getKey(), java.util.Collections.singletonList(entry.getValue()));
        }
        return streamProvider.processURL(spec, requestMethod, info, headerMap);
    }

    protected boolean isProxyCallAllowed(java.lang.String spec) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(spec) && getHostPortRestrictionHandler().proxyCallRestricted()) {
            try {
                java.net.URL url = new java.net.URL(spec);
                return getHostPortRestrictionHandler().allowProxy(url.getHost(), java.lang.Integer.toString(url.getPort() == (-1) ? url.getDefaultPort() : url.getPort()));
            } catch (java.net.MalformedURLException ignored) {
            }
        }
        return true;
    }

    class HostPortRestrictionHandler {
        private final java.lang.String allowedHostPortsValue;

        private java.util.Map<java.lang.String, java.util.HashSet<java.lang.String>> allowedHostPorts = null;

        private java.lang.Boolean isProxyCallRestricted = java.lang.Boolean.FALSE;

        public HostPortRestrictionHandler(java.lang.String allowedHostPortsValue) {
            this.allowedHostPortsValue = allowedHostPortsValue;
            org.apache.ambari.server.view.ViewURLStreamProvider.LOG.debug("Proxy restriction will be derived from {}", allowedHostPortsValue);
        }

        public boolean allowProxy(java.lang.String host, java.lang.String port) {
            org.apache.ambari.server.view.ViewURLStreamProvider.LOG.debug("Checking host {} port {} against allowed list.", host, port);
            if (org.apache.commons.lang.StringUtils.isNotBlank(host)) {
                java.lang.String hostToCompare = host.trim().toLowerCase();
                if (allowedHostPorts == null) {
                    initializeAllowedHostPorts();
                }
                if (isProxyCallRestricted) {
                    if (allowedHostPorts.containsKey(hostToCompare)) {
                        if (allowedHostPorts.get(hostToCompare).contains("*")) {
                            return true;
                        }
                        java.lang.String portToCompare = "";
                        if (org.apache.commons.lang.StringUtils.isNotBlank(port)) {
                            portToCompare = port.trim();
                        }
                        if (allowedHostPorts.get(hostToCompare).contains(portToCompare)) {
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
            }
            return true;
        }

        private void initializeAllowedHostPorts() {
            boolean proxyCallRestricted = false;
            java.util.Map<java.lang.String, java.util.HashSet<java.lang.String>> allowed = new java.util.HashMap<>();
            if (org.apache.commons.lang.StringUtils.isNotBlank(allowedHostPortsValue)) {
                java.lang.String allowedStr = allowedHostPortsValue.toLowerCase();
                if (!allowedStr.equals(org.apache.ambari.server.configuration.Configuration.PROXY_ALLOWED_HOST_PORTS.getDefaultValue())) {
                    proxyCallRestricted = true;
                    java.lang.String[] hostPorts = allowedStr.trim().split(",");
                    for (java.lang.String hostPortStr : hostPorts) {
                        java.lang.String[] hostAndPort = hostPortStr.trim().split(":");
                        if (hostAndPort.length == 1) {
                            if (!allowed.containsKey(hostAndPort[0])) {
                                allowed.put(hostAndPort[0], new java.util.HashSet<>());
                            }
                            allowed.get(hostAndPort[0]).add("*");
                            org.apache.ambari.server.view.ViewURLStreamProvider.LOG.debug("Allow proxy to host {} and all ports.", hostAndPort[0]);
                        } else {
                            if (!allowed.containsKey(hostAndPort[0])) {
                                allowed.put(hostAndPort[0], new java.util.HashSet<>());
                            }
                            allowed.get(hostAndPort[0]).add(hostAndPort[1]);
                            org.apache.ambari.server.view.ViewURLStreamProvider.LOG.debug("Allow proxy to host {} and port {}", hostAndPort[0], hostAndPort[1]);
                        }
                    }
                }
            }
            allowedHostPorts = allowed;
            isProxyCallRestricted = proxyCallRestricted;
        }

        public java.lang.Boolean proxyCallRestricted() {
            if (allowedHostPorts == null) {
                initializeAllowedHostPorts();
            }
            return isProxyCallRestricted;
        }
    }
}