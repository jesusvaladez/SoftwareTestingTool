package org.apache.ambari.server.security;
public class InternalSSLSocketFactoryNonTrusting extends org.apache.ambari.server.security.InternalSSLSocketFactory {
    public InternalSSLSocketFactoryNonTrusting() {
        super("TLSv1.2", false);
    }

    public static javax.net.SocketFactory getDefault() {
        return new org.apache.ambari.server.security.InternalSSLSocketFactoryNonTrusting();
    }
}