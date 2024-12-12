package org.apache.ambari.view.pig.utils;
public class BadRequestFormattedException extends org.apache.ambari.view.pig.utils.ServiceFormattedException {
    private static final int STATUS = 400;

    public BadRequestFormattedException(java.lang.String message, java.lang.Throwable exception) {
        super(message, exception, org.apache.ambari.view.pig.utils.BadRequestFormattedException.STATUS);
    }
}