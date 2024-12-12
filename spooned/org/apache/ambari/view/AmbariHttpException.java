package org.apache.ambari.view;
public class AmbariHttpException extends java.lang.Exception {
    private int responseCode;

    public AmbariHttpException(java.lang.String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}