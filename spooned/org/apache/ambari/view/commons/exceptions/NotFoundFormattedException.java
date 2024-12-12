package org.apache.ambari.view.commons.exceptions;
public class NotFoundFormattedException extends org.apache.ambari.view.commons.exceptions.ServiceFormattedException {
    private static final int STATUS = 404;

    public NotFoundFormattedException(java.lang.String message, java.lang.Throwable exception) {
        super(message, exception, org.apache.ambari.view.commons.exceptions.NotFoundFormattedException.STATUS);
    }
}