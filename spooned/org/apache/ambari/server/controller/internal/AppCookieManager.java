package org.apache.ambari.server.controller.internal;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.DefaultHttpClient;
public class AppCookieManager {
    static final java.lang.String HADOOP_AUTH = "hadoop.auth";

    private static final java.lang.String HADOOP_AUTH_EQ = "hadoop.auth=";

    private static final java.lang.String SET_COOKIE = "Set-Cookie";

    private static final org.apache.ambari.server.controller.internal.AppCookieManager.EmptyJaasCredentials EMPTY_JAAS_CREDENTIALS = new org.apache.ambari.server.controller.internal.AppCookieManager.EmptyJaasCredentials();

    private java.util.Map<java.lang.String, java.lang.String> endpointCookieMap = new java.util.concurrent.ConcurrentHashMap<>();

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.AppCookieManager.class);

    public static void main(java.lang.String[] args) throws java.io.IOException {
        new org.apache.ambari.server.controller.internal.AppCookieManager().getAppCookie(args[0], false);
    }

    public AppCookieManager() {
    }

    public java.lang.String getAppCookie(java.lang.String endpoint, boolean refresh) throws java.io.IOException {
        org.apache.http.client.methods.HttpUriRequest outboundRequest = new org.apache.http.client.methods.HttpGet(endpoint);
        java.net.URI uri = outboundRequest.getURI();
        java.lang.String scheme = uri.getScheme();
        java.lang.String host = uri.getHost();
        int port = uri.getPort();
        java.lang.String path = uri.getPath();
        if (!refresh) {
            java.lang.String appCookie = endpointCookieMap.get(endpoint);
            if (appCookie != null) {
                return appCookie;
            }
        }
        clearAppCookie(endpoint);
        org.apache.http.impl.client.DefaultHttpClient client = new org.apache.http.impl.client.DefaultHttpClient();
        org.apache.http.impl.auth.SPNegoSchemeFactory spNegoSF = new org.apache.http.impl.auth.SPNegoSchemeFactory(true);
        client.getAuthSchemes().register(AuthPolicy.SPNEGO, spNegoSF);
        client.getCredentialsProvider().setCredentials(new org.apache.http.auth.AuthScope(null, -1, null), org.apache.ambari.server.controller.internal.AppCookieManager.EMPTY_JAAS_CREDENTIALS);
        java.lang.String hadoopAuthCookie = null;
        org.apache.http.HttpResponse httpResponse = null;
        try {
            org.apache.http.HttpHost httpHost = new org.apache.http.HttpHost(host, port, scheme);
            org.apache.http.HttpRequest httpRequest = new org.apache.http.client.methods.HttpOptions(path);
            httpResponse = client.execute(httpHost, httpRequest);
            org.apache.http.Header[] headers = httpResponse.getHeaders(org.apache.ambari.server.controller.internal.AppCookieManager.SET_COOKIE);
            hadoopAuthCookie = org.apache.ambari.server.controller.internal.AppCookieManager.getHadoopAuthCookieValue(headers);
            if (hadoopAuthCookie == null) {
                org.apache.ambari.server.controller.internal.AppCookieManager.LOG.error("SPNego authentication failed, can not get hadoop.auth cookie for URL: " + endpoint);
                throw new java.io.IOException("SPNego authentication failed, can not get hadoop.auth cookie");
            }
        } finally {
            if (httpResponse != null) {
                org.apache.http.HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    entity.getContent().close();
                }
            }
        }
        hadoopAuthCookie = org.apache.ambari.server.controller.internal.AppCookieManager.HADOOP_AUTH_EQ + org.apache.ambari.server.controller.internal.AppCookieManager.quote(hadoopAuthCookie);
        setAppCookie(endpoint, hadoopAuthCookie);
        if (org.apache.ambari.server.controller.internal.AppCookieManager.LOG.isInfoEnabled()) {
            org.apache.ambari.server.controller.internal.AppCookieManager.LOG.info("Successful SPNego authentication to URL:" + uri);
        }
        return hadoopAuthCookie;
    }

    public java.lang.String getCachedAppCookie(java.lang.String endpoint) {
        return endpointCookieMap.get(endpoint);
    }

    private void setAppCookie(java.lang.String endpoint, java.lang.String appCookie) {
        endpointCookieMap.put(endpoint, appCookie);
    }

    private void clearAppCookie(java.lang.String endpoint) {
        endpointCookieMap.remove(endpoint);
    }

    static java.lang.String quote(java.lang.String s) {
        return s == null ? s : ("\"" + s) + "\"";
    }

    static java.lang.String getHadoopAuthCookieValue(org.apache.http.Header[] headers) {
        if (headers == null) {
            return null;
        }
        for (org.apache.http.Header header : headers) {
            org.apache.http.HeaderElement[] elements = header.getElements();
            for (org.apache.http.HeaderElement element : elements) {
                java.lang.String cookieName = element.getName();
                if (cookieName.equals(org.apache.ambari.server.controller.internal.AppCookieManager.HADOOP_AUTH)) {
                    if (element.getValue() != null) {
                        java.lang.String trimmedVal = element.getValue().trim();
                        if (!trimmedVal.isEmpty()) {
                            return trimmedVal;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static class EmptyJaasCredentials implements org.apache.http.auth.Credentials {
        @java.lang.Override
        public java.lang.String getPassword() {
            return null;
        }

        @java.lang.Override
        public java.security.Principal getUserPrincipal() {
            return null;
        }
    }
}