package org.apache.ambari.server.view;
import org.apache.http.client.utils.URIBuilder;
public class HttpImpersonatorImpl implements org.apache.ambari.view.HttpImpersonator {
    private org.apache.ambari.view.ViewContext context;

    private final org.apache.ambari.server.controller.internal.URLStreamProvider urlStreamProvider;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.view.HttpImpersonatorImpl.class);

    public HttpImpersonatorImpl(org.apache.ambari.view.ViewContext c) {
        this.context = c;
        org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration = org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance();
        this.urlStreamProvider = new org.apache.ambari.server.controller.internal.URLStreamProvider(org.apache.ambari.server.proxy.ProxyService.URL_CONNECT_TIMEOUT, org.apache.ambari.server.proxy.ProxyService.URL_READ_TIMEOUT, configuration.getTruststorePath(), configuration.getTruststorePassword(), configuration.getTruststoreType());
    }

    public HttpImpersonatorImpl(org.apache.ambari.view.ViewContext c, org.apache.ambari.server.controller.internal.URLStreamProvider urlStreamProvider) {
        this.context = c;
        this.urlStreamProvider = urlStreamProvider;
    }

    public org.apache.ambari.view.ViewContext getContext() {
        return this.context;
    }

    public java.lang.String getUsername() {
        return getContext().getUsername();
    }

    @java.lang.Override
    public java.net.HttpURLConnection doAs(java.net.HttpURLConnection conn, java.lang.String type) {
        java.lang.String username = getUsername();
        return doAs(conn, type, username, org.apache.ambari.server.view.ImpersonatorSettingImpl.DEFAULT_DO_AS_PARAM);
    }

    @java.lang.Override
    public java.net.HttpURLConnection doAs(java.net.HttpURLConnection conn, java.lang.String type, java.lang.String username, java.lang.String doAsParamName) {
        java.lang.String url = conn.getURL().toString();
        if (url.toLowerCase().contains(doAsParamName.toLowerCase())) {
            throw new java.lang.IllegalArgumentException(("URL cannot contain \"" + doAsParamName) + "\" parameter");
        }
        try {
            conn.setRequestMethod(type);
        } catch (java.io.IOException e) {
            return null;
        }
        conn.setRequestProperty(doAsParamName, username);
        return conn;
    }

    @java.lang.Override
    public java.lang.String requestURL(java.lang.String url, java.lang.String requestType, final org.apache.ambari.view.ImpersonatorSetting impersonatorSetting) {
        java.lang.String result = "";
        java.io.BufferedReader rd;
        java.lang.String line;
        if (url.toLowerCase().contains(impersonatorSetting.getDoAsParamName().toLowerCase())) {
            throw new java.lang.IllegalArgumentException(("URL cannot contain \"" + impersonatorSetting.getDoAsParamName()) + "\" parameter");
        }
        try {
            java.lang.String username = impersonatorSetting.getUsername();
            if (username != null) {
                org.apache.http.client.utils.URIBuilder builder = new org.apache.http.client.utils.URIBuilder(url);
                builder.addParameter(impersonatorSetting.getDoAsParamName(), username);
                url = builder.build().toString();
            }
            java.net.HttpURLConnection connection = urlStreamProvider.processURL(url, requestType, ((java.lang.String) (null)), null);
            int responseCode = connection.getResponseCode();
            java.io.InputStream resultInputStream;
            if (responseCode >= org.apache.ambari.server.proxy.ProxyService.HTTP_ERROR_RANGE_START) {
                resultInputStream = connection.getErrorStream();
            } else {
                resultInputStream = connection.getInputStream();
            }
            rd = new java.io.BufferedReader(new java.io.InputStreamReader(resultInputStream));
            line = rd.readLine();
            while (line != null) {
                result += line;
                line = rd.readLine();
            } 
            rd.close();
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.view.HttpImpersonatorImpl.LOG.error("Exception caught processing impersonator request.", e);
        }
        return result;
    }
}