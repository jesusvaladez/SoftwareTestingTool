package org.apache.ambari.view.utils.ambari;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
public class AmbariApi {
    private org.apache.ambari.view.ViewContext context;

    private org.apache.ambari.view.utils.ambari.Services services;

    private java.lang.String requestedBy = "views";

    public static java.lang.String API_PREFIX = "/api/v1/clusters/";

    public AmbariApi(org.apache.ambari.view.ViewContext context) {
        this.context = context;
    }

    public void setRequestedBy(java.lang.String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public java.lang.String requestClusterAPI(java.lang.String path) throws org.apache.ambari.view.utils.ambari.AmbariApiException, org.apache.ambari.view.AmbariHttpException {
        return requestClusterAPI(path, "GET", null, null);
    }

    public java.lang.String requestClusterAPI(java.lang.String path, java.lang.String method, java.lang.String data, java.util.Map<java.lang.String, java.lang.String> headers) throws org.apache.ambari.view.utils.ambari.AmbariApiException, org.apache.ambari.view.AmbariHttpException {
        java.lang.String response;
        try {
            if ((context.getAmbariClusterStreamProvider() == null) || (context.getCluster() == null)) {
                throw new org.apache.ambari.view.utils.ambari.NoClusterAssociatedException("RA030 View is not associated with any cluster. No way to request Ambari.");
            }
            if (!path.startsWith("/"))
                path = "/" + path;

            path = (org.apache.ambari.view.utils.ambari.AmbariApi.API_PREFIX + context.getCluster().getName()) + path;
            java.io.InputStream inputStream = context.getAmbariClusterStreamProvider().readFrom(path, method, data, addRequestedByHeader(headers));
            response = org.apache.commons.io.IOUtils.toString(inputStream);
        } catch (java.io.IOException e) {
            throw new org.apache.ambari.view.utils.ambari.AmbariApiException("RA040 I/O error while requesting Ambari", e);
        }
        return response;
    }

    public java.lang.String readFromAmbari(java.lang.String path, java.lang.String method, java.lang.String data, java.util.Map<java.lang.String, java.lang.String> headers) throws org.apache.ambari.view.utils.ambari.AmbariApiException, org.apache.ambari.view.AmbariHttpException {
        java.lang.String response;
        try {
            if (context.getAmbariClusterStreamProvider() == null) {
                throw new org.apache.ambari.view.utils.ambari.NoClusterAssociatedException("RA060 View is not associated with any cluster. No way to request Ambari.");
            }
            java.io.InputStream inputStream = context.getAmbariClusterStreamProvider().readFrom(path, method, data, addRequestedByHeader(headers));
            response = org.apache.commons.io.IOUtils.toString(inputStream);
        } catch (java.io.IOException e) {
            throw new org.apache.ambari.view.utils.ambari.AmbariApiException("RA050 I/O error while requesting Ambari", e);
        }
        return response;
    }

    public org.apache.ambari.view.utils.ambari.Services getServices() {
        if (services == null) {
            services = new org.apache.ambari.view.utils.ambari.Services(this, context);
        }
        return services;
    }

    private java.util.Map<java.lang.String, java.lang.String> addRequestedByHeader(java.util.Map<java.lang.String, java.lang.String> headers) {
        if (headers == null) {
            headers = new java.util.HashMap<java.lang.String, java.lang.String>();
        }
        headers.put("X-Requested-By", this.requestedBy);
        return headers;
    }

    public boolean isClusterAssociated() {
        return context.getCluster() != null;
    }
}