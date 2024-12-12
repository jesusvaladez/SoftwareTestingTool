package org.apache.ambari.server.notifications;
import org.apache.commons.lang.StringUtils;
public class Notification {
    public enum Type {

        GENERIC,
        ALERT;}

    public java.lang.String Subject;

    public java.lang.String Body;

    public java.util.List<org.apache.ambari.server.notifications.Recipient> Recipients;

    public java.util.Map<java.lang.String, java.lang.String> DispatchProperties;

    public org.apache.ambari.server.notifications.DispatchCredentials Credentials;

    public org.apache.ambari.server.notifications.DispatchCallback Callback;

    public java.util.List<java.lang.String> CallbackIds;

    public org.apache.ambari.server.notifications.Notification.Type getType() {
        return org.apache.ambari.server.notifications.Notification.Type.GENERIC;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("Notification{ ");
        buffer.append("type=").append(getType());
        buffer.append(", subject=").append(org.apache.commons.lang.StringUtils.strip(Subject));
        buffer.append("}");
        return buffer.toString();
    }
}