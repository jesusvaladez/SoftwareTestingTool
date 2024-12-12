package org.apache.ambari.server.utils;
public class HTTPUtils {
    public static java.lang.String requestURL(java.lang.String urlToRead) {
        java.lang.String result = "";
        java.io.BufferedReader rd;
        java.lang.String line = null;
        java.lang.String url = urlToRead;
        try {
            org.apache.ambari.server.controller.internal.URLStreamProvider urlStreamProvider = new org.apache.ambari.server.controller.internal.URLStreamProvider(org.apache.ambari.server.proxy.ProxyService.URL_CONNECT_TIMEOUT, org.apache.ambari.server.proxy.ProxyService.URL_READ_TIMEOUT, org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance());
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> headers = new java.util.HashMap<>();
            java.net.HttpURLConnection connection = urlStreamProvider.processURL(url, "GET", ((java.lang.String) (null)), headers);
            int responseCode = connection.getResponseCode();
            java.io.InputStream resultInputStream = null;
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
            e.printStackTrace();
        }
        return result;
    }

    public static org.apache.ambari.server.utils.HostAndPort getHostAndPortFromProperty(java.lang.String value) {
        if ((value == null) || value.isEmpty())
            return null;

        value = value.trim();
        int colonIndex = value.indexOf(":");
        if ((colonIndex > 0) && (colonIndex < (value.length() - 1))) {
            java.lang.String host = value.substring(0, colonIndex);
            int port = java.lang.Integer.parseInt(value.substring(colonIndex + 1, value.length()));
            return new org.apache.ambari.server.utils.HostAndPort(host, port);
        }
        return null;
    }
}