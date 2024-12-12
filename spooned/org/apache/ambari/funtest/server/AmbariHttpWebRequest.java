package org.apache.ambari.funtest.server;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.RequestLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
public class AmbariHttpWebRequest extends org.apache.ambari.funtest.server.WebRequest {
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(org.apache.ambari.funtest.server.AmbariHttpWebRequest.class);

    private static java.lang.String SERVER_URL_FORMAT = "http://%s:%d";

    private static java.lang.String SERVER_SSL_URL_FORMAT = "https://%s:%d";

    private java.lang.String content;

    private java.lang.String serverName;

    private int serverApiPort;

    private int serverAgentPort;

    private org.apache.ambari.funtest.server.WebResponse response;

    private java.lang.String curlApi;

    public AmbariHttpWebRequest(org.apache.ambari.funtest.server.ConnectionParams params) {
        setServerName(params.getServerName());
        setServerApiPort(params.getServerApiPort());
        setServerAgentPort(params.getServerAgentPort());
        setUserName(params.getUserName());
        setPassword(params.getPassword());
        addHeader("X-Requested-By", "ambari");
        if (getUserName() != null) {
            addHeader("Authorization", getBasicAuthentication());
        }
    }

    @java.lang.Override
    public org.apache.ambari.funtest.server.WebResponse getResponse() throws java.io.IOException {
        if (response == null) {
            org.apache.ambari.funtest.server.AmbariHttpWebRequest.LOG.info(getCurlApi());
            response = executeRequest();
        }
        return response;
    }

    @java.lang.Override
    public java.lang.String getUrl() {
        return getServerApiUrl() + getApiPath();
    }

    @java.lang.Override
    public java.lang.String getContent() {
        if (content == null) {
            content = getRequestData();
        }
        return content;
    }

    @java.lang.Override
    public java.lang.String getContentEncoding() {
        return "UTF-8";
    }

    @java.lang.Override
    public java.lang.String getContentType() {
        return "application/json";
    }

    public java.lang.String getCurlApi() {
        if (curlApi == null) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("curl");
            if (getUserName() != null) {
                sb.append(java.lang.String.format(" -u %s", getUserName()));
                if (getPassword() != null) {
                    sb.append(java.lang.String.format(":%s", getPassword()));
                }
            }
            sb.append(java.lang.String.format(" -H \"%s\"", "X-Requested-By: ambari"));
            sb.append(java.lang.String.format(" -X %s", getHttpMethod()));
            if (getHttpMethod().equals("PUT") || getHttpMethod().equals("POST")) {
                if (getContent() != null) {
                    sb.append(java.lang.String.format(" -d '%s'", getContent()));
                }
            }
            sb.append(java.lang.String.format(" %s", getUrl()));
            curlApi = sb.toString();
        }
        return curlApi;
    }

    public void setServerName(java.lang.String serverName) {
        this.serverName = serverName;
    }

    public java.lang.String getServerName() {
        return this.serverName;
    }

    public int getServerApiPort() {
        return this.serverApiPort;
    }

    public void setServerApiPort(int serverApiPort) {
        this.serverApiPort = serverApiPort;
    }

    public int getServerAgentPort() {
        return this.serverAgentPort;
    }

    public void setServerAgentPort(int serverAgentPort) {
        this.serverAgentPort = serverAgentPort;
    }

    protected java.lang.String getApiPath() {
        return "";
    }

    protected java.lang.String getRequestData() {
        return "";
    }

    protected java.lang.String getBasicAuthentication() {
        java.lang.String authString = (getUserName() + ":") + getPassword();
        byte[] authEncBytes = org.apache.commons.codec.binary.Base64.encodeBase64(authString.getBytes());
        java.lang.String authStringEnc = new java.lang.String(authEncBytes);
        return "Basic " + authStringEnc;
    }

    protected java.lang.String getServerApiUrl() {
        return java.lang.String.format(org.apache.ambari.funtest.server.AmbariHttpWebRequest.SERVER_URL_FORMAT, getServerName(), getServerApiPort());
    }

    protected java.lang.String getServerAgentUrl() {
        return java.lang.String.format(org.apache.ambari.funtest.server.AmbariHttpWebRequest.SERVER_URL_FORMAT, getServerName(), getServerAgentPort());
    }

    protected static com.google.gson.JsonObject createJsonObject(java.lang.String name, java.lang.String value) {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.addProperty(name, value);
        return jsonObject;
    }

    protected static com.google.gson.JsonObject createJsonObject(java.lang.String name, com.google.gson.JsonElement jsonElement) {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        jsonObject.add(name, jsonElement);
        return jsonObject;
    }

    private org.apache.ambari.funtest.server.WebResponse executeRequest() throws java.io.IOException {
        final org.apache.ambari.funtest.server.WebResponse response = new org.apache.ambari.funtest.server.WebResponse();
        org.apache.http.impl.client.CloseableHttpClient httpClient = org.apache.http.impl.client.HttpClients.createDefault();
        try {
            org.apache.http.client.methods.HttpRequestBase requestBase = null;
            java.lang.String httpMethod = getHttpMethod();
            if (httpMethod.equals("GET")) {
                requestBase = new org.apache.http.client.methods.HttpGet(getRequestUrl());
            } else if (httpMethod.equals("POST")) {
                org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(getRequestUrl());
                if (org.apache.commons.lang.StringUtils.isNotEmpty(getContent())) {
                    httpPost.setHeader("Content-Type", getContentType());
                    httpPost.setEntity(new org.apache.http.entity.StringEntity(getContent(), getContentEncoding()));
                }
                requestBase = httpPost;
            } else if (httpMethod.equals("PUT")) {
                org.apache.http.client.methods.HttpPut httpPut = new org.apache.http.client.methods.HttpPut(getRequestUrl());
                if (org.apache.commons.lang.StringUtils.isNotEmpty(getContent())) {
                    httpPut.setHeader("Content-Type", getContentType());
                    httpPut.setEntity(new org.apache.http.entity.StringEntity(getContent(), getContentEncoding()));
                }
                requestBase = httpPut;
            } else if (httpMethod.equals("DELETE")) {
                requestBase = new org.apache.http.client.methods.HttpDelete(getRequestUrl());
            } else {
                throw new java.lang.RuntimeException(java.lang.String.format("Unsupported HTTP method: %s", httpMethod));
            }
            java.util.Map<java.lang.String, java.lang.String> headers = getHeaders();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> header : headers.entrySet()) {
                requestBase.addHeader(header.getKey(), header.getValue());
            }
            org.apache.http.RequestLine requestLine = requestBase.getRequestLine();
            org.apache.ambari.funtest.server.AmbariHttpWebRequest.LOG.debug(requestLine);
            org.apache.http.client.ResponseHandler<java.lang.String> responseHandler = new org.apache.http.client.ResponseHandler<java.lang.String>() {
                @java.lang.Override
                public java.lang.String handleResponse(final org.apache.http.HttpResponse httpResponse) throws org.apache.http.client.ClientProtocolException, java.io.IOException {
                    int statusCode = httpResponse.getStatusLine().getStatusCode();
                    response.setStatusCode(statusCode);
                    org.apache.http.HttpEntity entity = httpResponse.getEntity();
                    return entity != null ? org.apache.http.util.EntityUtils.toString(entity) : null;
                }
            };
            java.lang.String responseBody = httpClient.execute(requestBase, responseHandler);
            response.setContent(responseBody);
        } finally {
            httpClient.close();
        }
        return response;
    }

    private java.lang.String getRequestUrl() {
        java.lang.StringBuilder requestUrl = new java.lang.StringBuilder(getUrl());
        if (org.apache.commons.lang.StringUtils.isNotEmpty(getQueryString())) {
            requestUrl.append("?");
            requestUrl.append(getQueryString());
        }
        return requestUrl.toString();
    }
}