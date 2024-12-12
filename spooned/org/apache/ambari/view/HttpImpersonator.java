package org.apache.ambari.view;
@java.lang.Deprecated
public interface HttpImpersonator {
    public java.net.HttpURLConnection doAs(java.net.HttpURLConnection conn, java.lang.String type);

    public java.net.HttpURLConnection doAs(java.net.HttpURLConnection conn, java.lang.String type, java.lang.String username, java.lang.String doAsParamName);

    public java.lang.String requestURL(java.lang.String urlToRead, java.lang.String requestType, org.apache.ambari.view.ImpersonatorSetting impersonatorSetting);
}