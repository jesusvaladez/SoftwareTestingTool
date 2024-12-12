package org.apache.ambari.server.controller.internal;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
public class URLRedirectProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.URLRedirectProvider.class);

    private final int connTimeout;

    private final int readTimeout;

    private final boolean skipSslCertificateCheck;

    public URLRedirectProvider(int connectionTimeout, int readTimeout, boolean skipSslCertificateCheck) {
        this.connTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.skipSslCertificateCheck = skipSslCertificateCheck;
    }

    public org.apache.ambari.server.controller.internal.URLRedirectProvider.RequestResult executeGet(java.lang.String spec) throws java.io.IOException {
        try (org.apache.http.impl.client.CloseableHttpClient httpClient = buildHttpClient()) {
            org.apache.http.client.methods.HttpGet httpGet = new org.apache.http.client.methods.HttpGet(spec);
            org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom().setConnectionRequestTimeout(connTimeout).setSocketTimeout(readTimeout).build();
            httpGet.setConfig(requestConfig);
            try (org.apache.http.client.methods.CloseableHttpResponse response = httpClient.execute(httpGet)) {
                final org.apache.http.HttpEntity entity = response.getEntity();
                final java.io.InputStream is = entity.getContent();
                final int statusCode = response.getStatusLine().getStatusCode();
                final org.apache.ambari.server.controller.internal.URLRedirectProvider.RequestResult result = new org.apache.ambari.server.controller.internal.URLRedirectProvider.RequestResult(org.apache.commons.io.IOUtils.toString(is, java.nio.charset.StandardCharsets.UTF_8), statusCode);
                if (((statusCode == org.apache.http.HttpStatus.SC_UNAUTHORIZED) || (statusCode == org.apache.http.HttpStatus.SC_NOT_FOUND)) || (statusCode == org.apache.http.HttpStatus.SC_FORBIDDEN)) {
                    org.apache.ambari.server.controller.internal.URLRedirectProvider.LOG.error(java.lang.String.format("Received HTTP '%s' response from URL: '%s'", statusCode, org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(spec)));
                }
                return result;
            }
        }
    }

    private org.apache.http.impl.client.CloseableHttpClient buildHttpClient() throws org.apache.ambari.server.AmbariException {
        org.apache.http.impl.client.HttpClientBuilder httpClientBuilder = org.apache.http.impl.client.HttpClientBuilder.create();
        if (skipSslCertificateCheck) {
            final javax.net.ssl.SSLContext sslContext;
            try {
                sslContext = new org.apache.http.ssl.SSLContextBuilder().loadTrustMaterial(null, (x509CertChain, authType) -> true).build();
            } catch (java.security.NoSuchAlgorithmException | java.security.KeyManagementException | java.security.KeyStoreException e) {
                throw new org.apache.ambari.server.AmbariException("Cannot build null truststore.", e);
            }
            httpClientBuilder.setSSLContext(sslContext).setConnectionManager(new org.apache.http.impl.conn.PoolingHttpClientConnectionManager(org.apache.http.config.RegistryBuilder.<org.apache.http.conn.socket.ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new org.apache.http.conn.ssl.SSLConnectionSocketFactory(sslContext, org.apache.http.conn.ssl.NoopHostnameVerifier.INSTANCE)).build()));
        }
        return httpClientBuilder.build();
    }

    public static class RequestResult {
        private final java.lang.String content;

        private final int code;

        public RequestResult(java.lang.String content, int code) {
            this.content = content;
            this.code = code;
        }

        public java.lang.String getContent() {
            return content;
        }

        public int getCode() {
            return code;
        }
    }
}