package org.apache.ambari.server.controller.internal;
import org.apache.http.HttpStatus;
public class URLStreamProvider implements org.apache.ambari.server.controller.utilities.StreamProvider {
    public static final java.lang.String COOKIE = "Cookie";

    private static final java.lang.String WWW_AUTHENTICATE = "WWW-Authenticate";

    private static final java.lang.String NEGOTIATE = "Negotiate";

    private static final java.lang.String AUTHORIZATION = "Authorization";

    private static final java.lang.String BASIC_AUTH = "Basic %s";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.URLStreamProvider.class);

    private boolean setupTruststoreForHttps;

    private final int connTimeout;

    private final int readTimeout;

    private final java.lang.String trustStorePath;

    private final java.lang.String trustStorePassword;

    private final java.lang.String trustStoreType;

    private volatile javax.net.ssl.SSLSocketFactory sslSocketFactory = null;

    private org.apache.ambari.server.controller.internal.AppCookieManager appCookieManager = null;

    public URLStreamProvider(int connectionTimeout, int readTimeout, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration) {
        this(connectionTimeout, readTimeout, configuration.getTruststorePath(), configuration.getTruststorePassword(), configuration.getTruststoreType());
    }

    public URLStreamProvider(int connectionTimeout, int readTimeout, java.lang.String trustStorePath, java.lang.String trustStorePassword, java.lang.String trustStoreType) {
        connTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.trustStorePath = trustStorePath;
        this.trustStorePassword = trustStorePassword;
        this.trustStoreType = trustStoreType;
        setupTruststoreForHttps = true;
    }

    public void setSetupTruststoreForHttps(boolean setupTruststoreForHttps) {
        this.setupTruststoreForHttps = setupTruststoreForHttps;
    }

    public boolean getSetupTruststoreForHttps() {
        return setupTruststoreForHttps;
    }

    @java.lang.Override
    public java.io.InputStream readFrom(java.lang.String spec, java.lang.String requestMethod, java.lang.String params) throws java.io.IOException {
        return processURL(spec, requestMethod, params, null).getInputStream();
    }

    @java.lang.Override
    public java.io.InputStream readFrom(java.lang.String spec) throws java.io.IOException {
        return readFrom(spec, "GET", null);
    }

    public java.net.HttpURLConnection processURL(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.util.List<java.lang.String>> headers) throws java.io.IOException {
        return processURL(spec, requestMethod, body == null ? null : body.getBytes(), headers);
    }

    public java.net.HttpURLConnection processURL(java.lang.String spec, java.lang.String requestMethod, java.io.InputStream body, java.util.Map<java.lang.String, java.util.List<java.lang.String>> headers) throws java.io.IOException {
        return processURL(spec, requestMethod, body == null ? null : org.apache.commons.io.IOUtils.toByteArray(body), headers);
    }

    public java.net.HttpURLConnection processURL(java.lang.String spec, java.lang.String requestMethod, byte[] body, java.util.Map<java.lang.String, java.util.List<java.lang.String>> headers) throws java.io.IOException {
        if (org.apache.ambari.server.controller.internal.URLStreamProvider.LOG.isDebugEnabled()) {
            org.apache.ambari.server.controller.internal.URLStreamProvider.LOG.debug("readFrom spec:{}", org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(spec));
        }
        java.net.URL url = new java.net.URL(spec);
        java.net.HttpURLConnection connection = (spec.startsWith("https") && setupTruststoreForHttps) ? getSSLConnection(url) : getConnection(url);
        org.apache.ambari.server.controller.internal.AppCookieManager appCookieManager = getAppCookieManager();
        java.lang.String appCookie = appCookieManager.getCachedAppCookie(spec);
        if (appCookie != null) {
            org.apache.ambari.server.controller.internal.URLStreamProvider.LOG.debug("Using cached app cookie for URL:{}", org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(spec));
            if ((headers == null) || headers.isEmpty()) {
                headers = java.util.Collections.singletonMap(org.apache.ambari.server.controller.internal.URLStreamProvider.COOKIE, java.util.Collections.singletonList(appCookie));
            } else {
                headers = new java.util.HashMap<>(headers);
                java.util.List<java.lang.String> cookieList = headers.get(org.apache.ambari.server.controller.internal.URLStreamProvider.COOKIE);
                java.lang.String cookies = ((cookieList == null) || cookieList.isEmpty()) ? null : cookieList.get(0);
                headers.put(org.apache.ambari.server.controller.internal.URLStreamProvider.COOKIE, java.util.Collections.singletonList(org.apache.ambari.server.controller.internal.URLStreamProvider.appendCookie(cookies, appCookie)));
            }
        }
        connection.setConnectTimeout(connTimeout);
        connection.setReadTimeout(readTimeout);
        connection.setDoOutput(true);
        connection.setRequestMethod(requestMethod);
        if (headers != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> entry : headers.entrySet()) {
                java.lang.String paramValue = entry.getValue().toString();
                connection.setRequestProperty(entry.getKey(), paramValue.substring(1, paramValue.length() - 1));
            }
        }
        if (body != null) {
            connection.getOutputStream().write(body);
        }
        if (url.getUserInfo() != null) {
            java.lang.String basicAuth = java.lang.String.format(org.apache.ambari.server.controller.internal.URLStreamProvider.BASIC_AUTH, new java.lang.String(new org.apache.commons.codec.binary.Base64().encode(url.getUserInfo().getBytes())));
            connection.setRequestProperty(org.apache.ambari.server.controller.internal.URLStreamProvider.AUTHORIZATION, basicAuth);
        }
        int statusCode = connection.getResponseCode();
        if (statusCode == org.apache.http.HttpStatus.SC_UNAUTHORIZED) {
            java.lang.String wwwAuthHeader = connection.getHeaderField(org.apache.ambari.server.controller.internal.URLStreamProvider.WWW_AUTHENTICATE);
            if (org.apache.ambari.server.controller.internal.URLStreamProvider.LOG.isInfoEnabled()) {
                org.apache.ambari.server.controller.internal.URLStreamProvider.LOG.info((("Received WWW-Authentication header:" + wwwAuthHeader) + ", for URL:") + org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(spec));
            }
            if ((wwwAuthHeader != null) && wwwAuthHeader.trim().startsWith(org.apache.ambari.server.controller.internal.URLStreamProvider.NEGOTIATE)) {
                connection = (spec.startsWith("https")) ? getSSLConnection(url) : getConnection(url);
                appCookie = appCookieManager.getAppCookie(spec, true);
                connection.setRequestProperty(org.apache.ambari.server.controller.internal.URLStreamProvider.COOKIE, appCookie);
                connection.setConnectTimeout(connTimeout);
                connection.setReadTimeout(readTimeout);
                connection.setDoOutput(true);
                return connection;
            } else {
                org.apache.ambari.server.controller.internal.URLStreamProvider.LOG.error((("Unsupported WWW-Authentication header:" + wwwAuthHeader) + ", for URL:") + org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(spec));
                return connection;
            }
        } else {
            if ((statusCode == org.apache.http.HttpStatus.SC_NOT_FOUND) || (statusCode == org.apache.http.HttpStatus.SC_FORBIDDEN)) {
                org.apache.ambari.server.controller.internal.URLStreamProvider.LOG.error(java.lang.String.format("Received HTTP %s response from URL: %s", statusCode, org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(spec)));
            }
            return connection;
        }
    }

    public synchronized org.apache.ambari.server.controller.internal.AppCookieManager getAppCookieManager() {
        if (appCookieManager == null) {
            appCookieManager = new org.apache.ambari.server.controller.internal.AppCookieManager();
        }
        return appCookieManager;
    }

    public static java.lang.String appendCookie(java.lang.String cookies, java.lang.String newCookie) {
        if ((cookies == null) || (cookies.length() == 0)) {
            return newCookie;
        }
        return (cookies + "; ") + newCookie;
    }

    public static class TrustAllHostnameVerifier implements javax.net.ssl.HostnameVerifier {
        public boolean verify(java.lang.String hostname, javax.net.ssl.SSLSession session) {
            return true;
        }
    }

    public static class TrustAllManager implements javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType) {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType) {
        }
    }

    protected java.net.HttpURLConnection getConnection(java.net.URL url) throws java.io.IOException {
        java.net.URLConnection connection = url.openConnection();
        if (!setupTruststoreForHttps) {
            javax.net.ssl.HttpsURLConnection httpsConnection = ((javax.net.ssl.HttpsURLConnection) (connection));
            javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[]{ new org.apache.ambari.server.controller.internal.URLStreamProvider.TrustAllManager() };
            javax.net.ssl.HostnameVerifier hostnameVerifier = new org.apache.ambari.server.controller.internal.URLStreamProvider.TrustAllHostnameVerifier();
            try {
                javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                httpsConnection.setSSLSocketFactory(sc.getSocketFactory());
                httpsConnection.setHostnameVerifier(hostnameVerifier);
            } catch (java.security.NoSuchAlgorithmException | java.security.KeyManagementException e) {
                throw new java.lang.IllegalStateException("Cannot create unverified ssl context.", e);
            }
        }
        return ((java.net.HttpURLConnection) (connection));
    }

    protected javax.net.ssl.HttpsURLConnection getSSLConnection(java.net.URL url) throws java.io.IOException, java.lang.IllegalStateException {
        if (sslSocketFactory == null) {
            synchronized(this) {
                if (sslSocketFactory == null) {
                    if ((trustStorePath == null) || (trustStorePassword == null)) {
                        java.lang.String msg = java.lang.String.format("Can't get secure connection to %s.  Truststore path or password is not set.", org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(url.toString()));
                        org.apache.ambari.server.controller.internal.URLStreamProvider.LOG.error(msg);
                        throw new java.lang.IllegalStateException(msg);
                    }
                    java.io.FileInputStream in = null;
                    try {
                        in = new java.io.FileInputStream(new java.io.File(trustStorePath));
                        java.security.KeyStore store = java.security.KeyStore.getInstance(trustStoreType == null ? java.security.KeyStore.getDefaultType() : trustStoreType);
                        store.load(in, trustStorePassword.toCharArray());
                        javax.net.ssl.TrustManagerFactory tmf = javax.net.ssl.TrustManagerFactory.getInstance(javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
                        tmf.init(store);
                        javax.net.ssl.SSLContext context = javax.net.ssl.SSLContext.getInstance("TLS");
                        context.init(null, tmf.getTrustManagers(), null);
                        sslSocketFactory = context.getSocketFactory();
                    } catch (java.lang.Exception e) {
                        throw new java.io.IOException("Can't get connection.", e);
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                    }
                }
            }
        }
        javax.net.ssl.HttpsURLConnection connection = ((javax.net.ssl.HttpsURLConnection) (url.openConnection()));
        connection.setSSLSocketFactory(sslSocketFactory);
        return connection;
    }

    public static final class AmbariHttpUrlConnectionProvider implements org.apache.ambari.spi.net.HttpURLConnectionProvider {
        private final org.apache.ambari.server.controller.internal.URLStreamProvider m_streamProvider;

        public AmbariHttpUrlConnectionProvider() {
            m_streamProvider = new org.apache.ambari.server.controller.internal.URLStreamProvider(org.apache.ambari.server.proxy.ProxyService.URL_CONNECT_TIMEOUT, org.apache.ambari.server.proxy.ProxyService.URL_READ_TIMEOUT, org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance());
        }

        @java.lang.Override
        public java.net.HttpURLConnection getConnection(java.lang.String url, java.util.Map<java.lang.String, java.util.List<java.lang.String>> headers) throws java.io.IOException {
            return m_streamProvider.processURL(url, "GET", ((java.io.InputStream) (null)), headers);
        }
    }
}