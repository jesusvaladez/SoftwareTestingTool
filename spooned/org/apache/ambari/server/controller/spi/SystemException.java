package org.apache.ambari.server.controller.spi;
public class SystemException extends java.lang.Exception {
    public SystemException(java.lang.String msg) {
        super(msg);
    }

    public SystemException(java.lang.String msg, java.lang.Throwable throwable) {
        super(msg, throwable);
    }
}