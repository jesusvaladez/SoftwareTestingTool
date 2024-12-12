package org.apache.ambari.server.notifications;
public class TargetConfigurationResult {
    public enum Status {

        VALID,
        INVALID;}

    private java.lang.String message;

    private org.apache.ambari.server.notifications.TargetConfigurationResult.Status status;

    private TargetConfigurationResult(org.apache.ambari.server.notifications.TargetConfigurationResult.Status status, java.lang.String message) {
        this.message = message;
        this.status = status;
    }

    public java.lang.String getMessage() {
        return message;
    }

    public org.apache.ambari.server.notifications.TargetConfigurationResult.Status getStatus() {
        return status;
    }

    public static org.apache.ambari.server.notifications.TargetConfigurationResult valid() {
        return new org.apache.ambari.server.notifications.TargetConfigurationResult(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.VALID, "Configuration is valid");
    }

    public static org.apache.ambari.server.notifications.TargetConfigurationResult invalid(java.lang.String message) {
        return new org.apache.ambari.server.notifications.TargetConfigurationResult(org.apache.ambari.server.notifications.TargetConfigurationResult.Status.INVALID, message);
    }
}