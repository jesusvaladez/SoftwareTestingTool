package org.apache.oozie.ambari.view;
public class AmbariIOUtil {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.oozie.ambari.view.AmbariIOUtil.class);

    private org.apache.ambari.view.ViewContext viewContext;

    public AmbariIOUtil(org.apache.ambari.view.ViewContext viewContext) {
        super();
        this.viewContext = viewContext;
    }

    public java.io.InputStream readFromUrl(java.lang.String urlToRead, java.lang.String method, java.lang.String body, java.util.Map<java.lang.String, java.lang.String> newHeaders) {
        org.apache.ambari.view.URLStreamProvider streamProvider = viewContext.getURLStreamProvider();
        java.io.InputStream stream = null;
        try {
            if (isSecurityEnabled()) {
                stream = streamProvider.readAsCurrent(urlToRead, method, body, newHeaders);
            } else {
                stream = streamProvider.readFrom(urlToRead, method, body, newHeaders);
            }
        } catch (java.io.IOException e) {
            org.apache.oozie.ambari.view.AmbariIOUtil.LOGGER.error("error talking to oozie", e);
            throw new java.lang.RuntimeException(e);
        }
        return stream;
    }

    private boolean isSecurityEnabled() {
        java.lang.String securityEnbaled = viewContext.getProperties().get("hadoop.security.authentication");
        return !"simple".equalsIgnoreCase(securityEnbaled);
    }
}