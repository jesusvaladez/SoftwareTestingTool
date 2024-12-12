package org.apache.ambari.server.security;
public class InternalSSLSocketFactory extends javax.net.ssl.SSLSocketFactory {
    private javax.net.ssl.SSLSocketFactory socketFactory;

    InternalSSLSocketFactory(java.lang.String protocol, boolean trusting) {
        try {
            javax.net.ssl.SSLContext ctx = javax.net.ssl.SSLContext.getInstance(protocol);
            javax.net.ssl.TrustManager[] trustManagers = (trusting) ? new javax.net.ssl.TrustManager[]{ new org.apache.ambari.server.security.InternalSSLSocketFactory.LenientTrustManager() } : null;
            ctx.init(null, trustManagers, new java.security.SecureRandom());
            socketFactory = ctx.getSocketFactory();
        } catch (java.lang.Exception ex) {
            ex.printStackTrace(java.lang.System.err);
        }
    }

    @java.lang.Override
    public java.lang.String[] getDefaultCipherSuites() {
        return socketFactory.getDefaultCipherSuites();
    }

    @java.lang.Override
    public java.lang.String[] getSupportedCipherSuites() {
        return socketFactory.getSupportedCipherSuites();
    }

    @java.lang.Override
    public java.net.Socket createSocket(java.net.Socket socket, java.lang.String string, int i, boolean bln) throws java.io.IOException {
        return socketFactory.createSocket(socket, string, i, bln);
    }

    @java.lang.Override
    public java.net.Socket createSocket(java.lang.String string, int i) throws java.io.IOException {
        return socketFactory.createSocket(string, i);
    }

    @java.lang.Override
    public java.net.Socket createSocket(java.lang.String string, int i, java.net.InetAddress ia, int i1) throws java.io.IOException {
        return socketFactory.createSocket(string, i, ia, i1);
    }

    @java.lang.Override
    public java.net.Socket createSocket(java.net.InetAddress ia, int i) throws java.io.IOException {
        return socketFactory.createSocket(ia, i);
    }

    @java.lang.Override
    public java.net.Socket createSocket(java.net.InetAddress ia, int i, java.net.InetAddress ia1, int i1) throws java.io.IOException {
        return socketFactory.createSocket(ia, i, ia1, i1);
    }

    public static class LenientTrustManager extends javax.net.ssl.X509ExtendedTrustManager implements javax.net.ssl.X509TrustManager {
        @java.lang.Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, java.lang.String string) throws java.security.cert.CertificateException {
        }

        @java.lang.Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, java.lang.String string) throws java.security.cert.CertificateException {
        }

        @java.lang.Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }

        @java.lang.Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, java.lang.String s, java.net.Socket socket) throws java.security.cert.CertificateException {
        }

        @java.lang.Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, java.lang.String s, java.net.Socket socket) throws java.security.cert.CertificateException {
        }

        @java.lang.Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, java.lang.String s, javax.net.ssl.SSLEngine sslEngine) throws java.security.cert.CertificateException {
        }

        @java.lang.Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, java.lang.String s, javax.net.ssl.SSLEngine sslEngine) throws java.security.cert.CertificateException {
        }
    }
}