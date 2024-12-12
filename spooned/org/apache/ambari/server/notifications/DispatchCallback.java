package org.apache.ambari.server.notifications;
public interface DispatchCallback {
    void onSuccess(java.util.List<java.lang.String> callbackIds);

    void onFailure(java.util.List<java.lang.String> callbackIds);
}