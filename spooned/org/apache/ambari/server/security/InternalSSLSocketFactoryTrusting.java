package org.apache.ambari.server.security;
public class InternalSSLSocketFactoryTrusting extends org.apache.ambari.server.security.InternalSSLSocketFactory {
    public InternalSSLSocketFactoryTrusting() {
        super("TLSv1.2", true);
    }

    public static javax.net.SocketFactory getDefault() {
        return new org.apache.ambari.server.security.InternalSSLSocketFactoryTrusting();
    }
}