package org.apache.ambari.view.pig.utils;
public class NotFoundFormattedException extends org.apache.ambari.view.pig.utils.ServiceFormattedException {
    private static final int STATUS = 404;

    public NotFoundFormattedException(java.lang.String message, java.lang.Throwable exception) {
        super(message, exception, org.apache.ambari.view.pig.utils.NotFoundFormattedException.STATUS);
    }
}