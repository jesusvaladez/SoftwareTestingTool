package org.apache.ambari.funtest.server.api.cluster;
public class GetAllClustersWebRequest extends org.apache.ambari.funtest.server.AmbariHttpWebRequest {
    private static java.lang.String pathFormat = "/api/v1/clusters";

    public GetAllClustersWebRequest(org.apache.ambari.funtest.server.ConnectionParams params) {
        super(params);
    }

    @java.lang.Override
    public java.lang.String getHttpMethod() {
        return "GET";
    }

    @java.lang.Override
    protected java.lang.String getApiPath() {
        return org.apache.ambari.funtest.server.api.cluster.GetAllClustersWebRequest.pathFormat;
    }
}