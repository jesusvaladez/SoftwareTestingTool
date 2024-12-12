package org.apache.ambari.server.proxy;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
@javax.ws.rs.Path("/")
public class ProxyService {
    public static final int URL_CONNECT_TIMEOUT = 20000;

    public static final int URL_READ_TIMEOUT = 15000;

    public static final int HTTP_ERROR_RANGE_START = Response.Status.BAD_REQUEST.getStatusCode();

    private static final java.lang.String REQUEST_TYPE_GET = "GET";

    private static final java.lang.String REQUEST_TYPE_POST = "POST";

    private static final java.lang.String REQUEST_TYPE_PUT = "PUT";

    private static final java.lang.String REQUEST_TYPE_DELETE = "DELETE";

    private static final java.lang.String QUERY_PARAMETER_URL = "url=";

    private static final java.lang.String AMBARI_PROXY_PREFIX = "AmbariProxy-";

    private static final java.lang.String ERROR_PROCESSING_URL = "Error occurred during processing URL ";

    private static final java.lang.String INVALID_PARAM_IN_URL = "Invalid query params found in URL ";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.proxy.ProxyService.class);

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    public javax.ws.rs.core.Response processGetRequestForwarding(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(org.apache.ambari.server.proxy.ProxyService.REQUEST_TYPE_GET, ui, null, headers);
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Consumes({ javax.ws.rs.core.MediaType.WILDCARD, javax.ws.rs.core.MediaType.TEXT_PLAIN, javax.ws.rs.core.MediaType.TEXT_XML, javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED, javax.ws.rs.core.MediaType.APPLICATION_JSON })
    public javax.ws.rs.core.Response processPostRequestForwarding(java.io.InputStream body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(org.apache.ambari.server.proxy.ProxyService.REQUEST_TYPE_POST, ui, body, headers);
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Consumes({ javax.ws.rs.core.MediaType.WILDCARD, javax.ws.rs.core.MediaType.TEXT_PLAIN, javax.ws.rs.core.MediaType.TEXT_XML, javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED, javax.ws.rs.core.MediaType.APPLICATION_JSON })
    public javax.ws.rs.core.Response processPutRequestForwarding(java.io.InputStream body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(org.apache.ambari.server.proxy.ProxyService.REQUEST_TYPE_PUT, ui, body, headers);
    }

    @javax.ws.rs.DELETE
    @org.apache.ambari.annotations.ApiIgnore
    public javax.ws.rs.core.Response processDeleteRequestForwarding(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(org.apache.ambari.server.proxy.ProxyService.REQUEST_TYPE_DELETE, ui, null, headers);
    }

    private javax.ws.rs.core.Response handleRequest(java.lang.String requestType, javax.ws.rs.core.UriInfo ui, java.io.InputStream body, javax.ws.rs.core.HttpHeaders headers) {
        org.apache.ambari.server.controller.internal.URLStreamProvider urlStreamProvider = new org.apache.ambari.server.controller.internal.URLStreamProvider(org.apache.ambari.server.proxy.ProxyService.URL_CONNECT_TIMEOUT, org.apache.ambari.server.proxy.ProxyService.URL_READ_TIMEOUT, null, null, null);
        java.lang.String query = ui.getRequestUri().getQuery();
        if ((query != null) && (query.indexOf(org.apache.ambari.server.proxy.ProxyService.QUERY_PARAMETER_URL) != (-1))) {
            java.lang.String url = query.replaceFirst(org.apache.ambari.server.proxy.ProxyService.QUERY_PARAMETER_URL, "");
            javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> m = ui.getQueryParameters();
            if (m.containsKey(org.apache.ambari.server.view.ImpersonatorSettingImpl.DEFAULT_DO_AS_PARAM)) {
                org.apache.ambari.server.proxy.ProxyService.LOG.error(org.apache.ambari.server.proxy.ProxyService.INVALID_PARAM_IN_URL + url);
                return javax.ws.rs.core.Response.status(Response.Status.BAD_REQUEST.getStatusCode()).type(MediaType.TEXT_PLAIN).entity(org.apache.ambari.server.proxy.ProxyService.INVALID_PARAM_IN_URL).build();
            }
            try {
                java.net.HttpURLConnection connection = urlStreamProvider.processURL(url, requestType, body, getHeaderParamsToForward(headers));
                int responseCode = connection.getResponseCode();
                java.io.InputStream resultInputStream = null;
                if (responseCode >= org.apache.ambari.server.proxy.ProxyService.HTTP_ERROR_RANGE_START) {
                    resultInputStream = connection.getErrorStream();
                } else {
                    resultInputStream = connection.getInputStream();
                }
                java.lang.String contentType = connection.getContentType();
                javax.ws.rs.core.Response.ResponseBuilder rb = javax.ws.rs.core.Response.status(responseCode);
                if (contentType.indexOf(javax.ws.rs.core.MediaType.APPLICATION_JSON) != (-1)) {
                    rb.entity(new com.google.gson.Gson().fromJson(new java.io.InputStreamReader(resultInputStream), java.util.Map.class));
                } else {
                    rb.entity(resultInputStream);
                }
                return rb.type(contentType).build();
            } catch (java.io.IOException e) {
                org.apache.ambari.server.proxy.ProxyService.LOG.error(org.apache.ambari.server.proxy.ProxyService.ERROR_PROCESSING_URL + url, e);
                return javax.ws.rs.core.Response.status(Response.Status.BAD_REQUEST.getStatusCode()).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
            }
        }
        return null;
    }

    private java.util.Map<java.lang.String, java.util.List<java.lang.String>> getHeaderParamsToForward(javax.ws.rs.core.HttpHeaders headers) {
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> headerParamsToForward = new java.util.HashMap<>();
        for (java.lang.String paramName : headers.getRequestHeaders().keySet()) {
            if (paramName.startsWith(org.apache.ambari.server.proxy.ProxyService.AMBARI_PROXY_PREFIX)) {
                headerParamsToForward.put(paramName.replaceAll(org.apache.ambari.server.proxy.ProxyService.AMBARI_PROXY_PREFIX, ""), headers.getRequestHeader(paramName));
            }
        }
        return headerParamsToForward;
    }
}