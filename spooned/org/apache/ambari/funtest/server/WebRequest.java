package org.apache.ambari.funtest.server;
public class WebRequest {
    private java.lang.String httpMethod;

    private java.lang.String url;

    private java.lang.String queryString;

    private java.lang.String content;

    private java.lang.String contentType;

    private java.lang.String contentEncoding;

    private java.lang.String userName;

    private java.lang.String password;

    private java.util.Map<java.lang.String, java.lang.String> headers = new java.util.HashMap<>();

    public WebRequest() {
    }

    public org.apache.ambari.funtest.server.WebResponse getResponse() throws java.lang.Exception {
        return null;
    }

    public java.lang.String getHttpMethod() {
        return this.httpMethod;
    }

    public void setHttpMethod(java.lang.String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public java.lang.String getUrl() {
        return this.url;
    }

    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    public java.lang.String getQueryString() {
        return this.queryString;
    }

    public void setQueryString(java.lang.String queryString) {
        this.queryString = queryString;
    }

    public java.lang.String getContent() {
        return this.content;
    }

    public void setContent(java.lang.String content) {
        this.content = content;
    }

    public java.lang.String getContentType() {
        return this.contentType;
    }

    public void setContentType(java.lang.String contentType) {
        this.contentType = contentType;
    }

    public java.lang.String getContentEncoding() {
        return this.contentEncoding;
    }

    public void setContentEncoding(java.lang.String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public java.util.Map<java.lang.String, java.lang.String> getHeaders() {
        return java.util.Collections.unmodifiableMap(this.headers);
    }

    public void clearHeaders() {
        this.headers.clear();
    }

    public void addHeaders(java.util.Map<java.lang.String, java.lang.String> headers) {
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : headers.entrySet()) {
            addHeader(entry.getKey(), entry.getValue());
        }
    }

    public void addHeader(java.lang.String name, java.lang.String value) {
        this.headers.put(name, value);
    }

    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    public java.lang.String getUserName() {
        return this.userName;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    public java.lang.String getPassword() {
        return this.password;
    }
}