package org.apache.ambari.view.pig.templeton.client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
public class JSONRequest<RESPONSE> {
    protected final java.lang.Class<RESPONSE> responseClass;

    protected final org.apache.ambari.view.ViewContext context;

    protected final com.sun.jersey.api.client.WebResource resource;

    private java.lang.String username;

    private java.lang.String doAs;

    protected final com.google.gson.Gson gson = new com.google.gson.Gson();

    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.templeton.client.JSONRequest.class);

    public JSONRequest(com.sun.jersey.api.client.WebResource resource, java.lang.Class<RESPONSE> responseClass, java.lang.String username, java.lang.String doAs, org.apache.ambari.view.ViewContext context) {
        this.resource = resource;
        this.responseClass = responseClass;
        this.username = username;
        this.context = context;
        this.doAs = doAs;
    }

    public RESPONSE get(com.sun.jersey.api.client.WebResource resource) throws java.io.IOException {
        org.apache.ambari.view.pig.templeton.client.JSONRequest.LOG.info("GET {}", resource);
        java.io.InputStream inputStream = readFrom(resource, "GET", null, new java.util.HashMap<java.lang.String, java.lang.String>());
        org.apache.ambari.view.pig.templeton.client.JSONRequest.recordLastCurlCommand(java.lang.String.format(("curl \"" + resource.toString()) + "\""));
        java.lang.String responseJson = org.apache.commons.io.IOUtils.toString(inputStream);
        org.apache.ambari.view.pig.templeton.client.JSONRequest.LOG.debug("RESPONSE {}", responseJson);
        return gson.fromJson(responseJson, responseClass);
    }

    public RESPONSE get() throws java.io.IOException {
        return get(this.resource);
    }

    public RESPONSE get(com.sun.jersey.core.util.MultivaluedMapImpl params) throws java.io.IOException {
        return get(this.resource.queryParams(params));
    }

    public RESPONSE post(com.sun.jersey.api.client.WebResource resource, com.sun.jersey.core.util.MultivaluedMapImpl data) throws java.io.IOException {
        org.apache.ambari.view.pig.templeton.client.JSONRequest.LOG.info("POST: {}", resource);
        org.apache.ambari.view.pig.templeton.client.JSONRequest.LOG.debug("data: {}", data);
        java.lang.StringBuilder curlBuilder = new java.lang.StringBuilder();
        javax.ws.rs.core.UriBuilder builder = getUriBuilder(data, curlBuilder);
        java.util.Map<java.lang.String, java.lang.String> headers = new java.util.HashMap<java.lang.String, java.lang.String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        org.apache.ambari.view.pig.templeton.client.JSONRequest.recordLastCurlCommand(java.lang.String.format(((("curl " + curlBuilder.toString()) + " \"") + resource.toString()) + "\""));
        java.io.InputStream inputStream = readFrom(resource, "POST", builder.build().getRawQuery(), headers);
        java.lang.String responseJson = org.apache.commons.io.IOUtils.toString(inputStream);
        org.apache.ambari.view.pig.templeton.client.JSONRequest.LOG.debug("RESPONSE => {}", responseJson);
        return gson.fromJson(responseJson, responseClass);
    }

    public RESPONSE post(com.sun.jersey.core.util.MultivaluedMapImpl data) throws java.io.IOException {
        return post(resource, data);
    }

    public RESPONSE post() throws java.io.IOException {
        return post(resource, new com.sun.jersey.core.util.MultivaluedMapImpl());
    }

    public RESPONSE post(com.sun.jersey.core.util.MultivaluedMapImpl params, com.sun.jersey.core.util.MultivaluedMapImpl data) throws java.io.IOException {
        return post(resource.queryParams(params), data);
    }

    public RESPONSE put(com.sun.jersey.api.client.WebResource resource, com.sun.jersey.core.util.MultivaluedMapImpl data) throws java.io.IOException {
        org.apache.ambari.view.pig.templeton.client.JSONRequest.LOG.info("PUT {}", resource);
        java.lang.StringBuilder curlBuilder = new java.lang.StringBuilder();
        javax.ws.rs.core.UriBuilder builder = getUriBuilder(data, curlBuilder);
        java.util.Map<java.lang.String, java.lang.String> headers = new java.util.HashMap<java.lang.String, java.lang.String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        org.apache.ambari.view.pig.templeton.client.JSONRequest.recordLastCurlCommand(java.lang.String.format(((("curl -X PUT " + curlBuilder.toString()) + " \"") + resource.toString()) + "\""));
        java.io.InputStream inputStream = readFrom(resource, "PUT", builder.build().getRawQuery(), headers);
        java.lang.String responseJson = org.apache.commons.io.IOUtils.toString(inputStream);
        org.apache.ambari.view.pig.templeton.client.JSONRequest.LOG.debug("RESPONSE => {}", responseJson);
        return gson.fromJson(responseJson, responseClass);
    }

    public javax.ws.rs.core.UriBuilder getUriBuilder(com.sun.jersey.core.util.MultivaluedMapImpl data, java.lang.StringBuilder curlBuilder) {
        com.sun.jersey.core.util.MultivaluedMapImpl effectiveData;
        if (data == null)
            effectiveData = new com.sun.jersey.core.util.MultivaluedMapImpl();
        else
            effectiveData = new com.sun.jersey.core.util.MultivaluedMapImpl(data);

        effectiveData.putSingle("user.name", username);
        effectiveData.putSingle("doAs", doAs);
        javax.ws.rs.core.UriBuilder builder = javax.ws.rs.core.UriBuilder.fromPath("host/");
        for (java.lang.String key : effectiveData.keySet()) {
            for (java.lang.String value : effectiveData.get(key)) {
                builder.queryParam(key, value);
                curlBuilder.append(java.lang.String.format("-d %s=\"%s\" ", key, value.replace("\"", "\\\"")));
            }
        }
        if (data != null)
            org.apache.ambari.view.pig.templeton.client.JSONRequest.LOG.debug("data: {}", builder.build().getRawQuery());

        return builder;
    }

    public RESPONSE put(com.sun.jersey.core.util.MultivaluedMapImpl data) throws java.io.IOException {
        return put(resource, data);
    }

    public RESPONSE put() throws java.io.IOException {
        return put(resource, new com.sun.jersey.core.util.MultivaluedMapImpl());
    }

    public RESPONSE put(com.sun.jersey.core.util.MultivaluedMapImpl params, com.sun.jersey.core.util.MultivaluedMapImpl data) throws java.io.IOException {
        return put(resource.queryParams(params), data);
    }

    public RESPONSE delete(com.sun.jersey.api.client.WebResource resource, com.sun.jersey.core.util.MultivaluedMapImpl data) throws java.io.IOException {
        org.apache.ambari.view.pig.templeton.client.JSONRequest.LOG.info("DELETE {}", resource.toString());
        java.lang.StringBuilder curlBuilder = new java.lang.StringBuilder();
        javax.ws.rs.core.UriBuilder builder = getUriBuilder(data, curlBuilder);
        java.util.Map<java.lang.String, java.lang.String> headers = new java.util.HashMap<java.lang.String, java.lang.String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        org.apache.ambari.view.pig.templeton.client.JSONRequest.recordLastCurlCommand(java.lang.String.format(((("curl -X DELETE " + curlBuilder.toString()) + " \"") + resource.toString()) + "\""));
        java.io.InputStream inputStream = readFrom(resource, "DELETE", builder.build().getRawQuery(), headers);
        java.lang.String responseJson = org.apache.commons.io.IOUtils.toString(inputStream);
        org.apache.ambari.view.pig.templeton.client.JSONRequest.LOG.debug("RESPONSE => {}", responseJson);
        return gson.fromJson(responseJson, responseClass);
    }

    public RESPONSE delete(com.sun.jersey.core.util.MultivaluedMapImpl data) throws java.io.IOException {
        return delete(resource, data);
    }

    public RESPONSE delete() throws java.io.IOException {
        return delete(resource, new com.sun.jersey.core.util.MultivaluedMapImpl());
    }

    public RESPONSE delete(com.sun.jersey.core.util.MultivaluedMapImpl params, com.sun.jersey.core.util.MultivaluedMapImpl data) throws java.io.IOException {
        return delete(resource.queryParams(params), data);
    }

    private static void recordLastCurlCommand(java.lang.String curl) {
        org.apache.ambari.view.pig.templeton.client.JSONRequest.LOG.info(curl);
    }

    public java.io.InputStream readFrom(com.sun.jersey.api.client.WebResource resource, java.lang.String method, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException {
        java.net.HttpURLConnection connection;
        resource = resource.queryParam("user.name", username).queryParam("doAs", doAs);
        java.lang.String path = resource.toString();
        if (doAs == null) {
            connection = context.getURLConnectionProvider().getConnection(path, method, body, headers);
        } else {
            connection = context.getURLConnectionProvider().getConnectionAs(path, method, body, headers, doAs);
        }
        return getInputStream(connection);
    }

    private java.io.InputStream getInputStream(java.net.HttpURLConnection connection) throws java.io.IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode >= Response.Status.BAD_REQUEST.getStatusCode()) {
            java.lang.String message = connection.getResponseMessage();
            if (connection.getErrorStream() != null) {
                message = org.apache.commons.io.IOUtils.toString(connection.getErrorStream());
            }
            org.apache.ambari.view.pig.templeton.client.JSONRequest.LOG.error("Got error response for url {}. Response code:{}. {}", connection.getURL(), responseCode, message);
            throw new org.apache.ambari.view.utils.ambari.AmbariApiException(message);
        }
        return connection.getInputStream();
    }
}