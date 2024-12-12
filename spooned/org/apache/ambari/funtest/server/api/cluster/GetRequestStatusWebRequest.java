package org.apache.ambari.funtest.server.api.cluster;
public class GetRequestStatusWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private java.lang.String clusterName;

    private int requestId;

    private int taskId;

    private static java.lang.String pathFormat = "/api/v1/clusters/%s/requests/%d";

    private static java.lang.String pathFormatWithTask = "/api/v1/clusters/%s/requests/%d/tasks/%d";

    public GetRequestStatusWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, int requestId) {
        this(params, clusterName, requestId, -1);
    }

    public GetRequestStatusWebRequest(org.apache.ambari.funtest.server.ConnectionParams params, java.lang.String clusterName, int requestId, int taskId) {
        super(params);
        this.clusterName = clusterName;
        this.requestId = requestId;
        this.taskId = taskId;
    }

    public java.lang.String getClusterName() {
        return this.clusterName;
    }

    public int getRequestId() {
        return this.requestId;
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "GET";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        if (taskId != (-1))
            return java.lang.String.format(org.apache.ambari.funtest.server.api.cluster.GetRequestStatusWebRequest.pathFormatWithTask, clusterName, requestId, taskId);

        return java.lang.String.format(org.apache.ambari.funtest.server.api.cluster.GetRequestStatusWebRequest.pathFormat, clusterName, requestId);
    }
}