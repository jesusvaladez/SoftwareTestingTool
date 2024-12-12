package org.apache.ambari.server.security.encryption;
public class CertificateUtils {
    public static java.security.interfaces.RSAPublicKey getPublicKeyFromString(java.lang.String certificateString) throws java.security.cert.CertificateException, java.io.UnsupportedEncodingException {
        java.security.cert.CertificateFactory fact = java.security.cert.CertificateFactory.getInstance("X.509");
        java.io.ByteArrayInputStream is = new java.io.ByteArrayInputStream(certificateString.getBytes("UTF8"));
        java.security.cert.X509Certificate cer = ((java.security.cert.X509Certificate) (fact.generateCertificate(is)));
        return ((java.security.interfaces.RSAPublicKey) (cer.getPublicKey()));
    }
}