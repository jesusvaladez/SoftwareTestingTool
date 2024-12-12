package org.apache.ambari.server.security.authorization;
public class AuthorizationException extends org.apache.ambari.server.controller.spi.SystemException {
    public AuthorizationException() {
        this("The authenticated user is not authorized to perform the requested operation");
    }

    public AuthorizationException(java.lang.String msg) {
        super(msg);
    }

    public AuthorizationException(java.lang.String msg, java.lang.Throwable throwable) {
        super(msg, throwable);
    }
}