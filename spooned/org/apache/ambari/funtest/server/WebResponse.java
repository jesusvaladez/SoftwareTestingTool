package org.apache.ambari.funtest.server;
public class WebResponse {
    private java.lang.String content;

    private int statusCode;

    public WebResponse() {
    }

    public java.lang.String getContent() {
        return this.content;
    }

    public void setContent(java.lang.String content) {
        this.content = content;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}