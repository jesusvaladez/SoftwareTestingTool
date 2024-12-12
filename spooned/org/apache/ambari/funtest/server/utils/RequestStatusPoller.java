package org.apache.ambari.funtest.server.utils;
class RequestStatusPoller implements java.lang.Runnable {
    private org.apache.ambari.server.actionmanager.HostRoleStatus hostRoleStatus;

    private org.apache.ambari.funtest.server.ConnectionParams serverParams;

    private java.lang.String clusterName;

    private int requestId;

    public RequestStatusPoller(org.apache.ambari.funtest.server.ConnectionParams serverParams, java.lang.String clusterName, int requestId) {
        this.hostRoleStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS;
        this.serverParams = serverParams;
        this.clusterName = clusterName;
        this.requestId = requestId;
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getHostRoleStatus() {
        return this.hostRoleStatus;
    }

    public static boolean poll(org.apache.ambari.funtest.server.ConnectionParams serverParams, java.lang.String clusterName, int requestId) throws java.lang.Exception {
        org.apache.ambari.funtest.server.utils.RequestStatusPoller poller = new org.apache.ambari.funtest.server.utils.RequestStatusPoller(serverParams, clusterName, requestId);
        java.lang.Thread pollerThread = new java.lang.Thread(poller);
        pollerThread.start();
        pollerThread.join();
        if (poller.getHostRoleStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)
            return true;

        return false;
    }

    @java.lang.Override
    public void run() {
        int retryCount = 5;
        while (true) {
            com.google.gson.JsonElement jsonResponse;
            try {
                org.apache.ambari.funtest.server.WebRequest webRequest = new org.apache.ambari.funtest.server.api.cluster.GetRequestStatusWebRequest(serverParams, clusterName, requestId);
                jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(webRequest);
            } catch (java.lang.Exception ex) {
                throw new java.lang.RuntimeException(ex);
            }
            if (!jsonResponse.isJsonNull()) {
                com.google.gson.JsonObject jsonObj = jsonResponse.getAsJsonObject();
                com.google.gson.JsonObject jsonRequestsObj = jsonObj.getAsJsonObject("Requests");
                java.lang.String requestStatus = jsonRequestsObj.get("request_status").getAsString();
                hostRoleStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(requestStatus);
                if ((((hostRoleStatus == org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED) || (hostRoleStatus == org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED)) || (hostRoleStatus == org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT)) || (retryCount == 0))
                    break;

            }
            try {
                java.lang.Thread.sleep(5000);
            } catch (java.lang.InterruptedException ex) {
                break;
            }
            retryCount--;
        } 
    }
}