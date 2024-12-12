package org.apache.ambari.server.view;
public class ViewClassLoader extends org.eclipse.jetty.webapp.WebAppClassLoader {
    public ViewClassLoader(org.apache.ambari.server.view.configuration.ViewConfig viewConfig, java.net.URL[] urls) throws java.io.IOException {
        this(viewConfig, null, urls);
    }

    public ViewClassLoader(org.apache.ambari.server.view.configuration.ViewConfig viewConfig, java.lang.ClassLoader parent, java.net.URL[] urls) throws java.io.IOException {
        super(parent, org.apache.ambari.server.view.ViewClassLoader.getInitContext(viewConfig));
        for (java.net.URL url : urls) {
            addURL(url);
        }
    }

    private static org.eclipse.jetty.webapp.WebAppContext getInitContext(org.apache.ambari.server.view.configuration.ViewConfig viewConfig) {
        org.eclipse.jetty.webapp.WebAppContext webAppContext = new org.eclipse.jetty.webapp.WebAppContext();
        webAppContext.addSystemClass("org.apache.ambari.server.");
        webAppContext.addSystemClass("org.apache.ambari.view.");
        webAppContext.addSystemClass("com.google.inject.");
        webAppContext.addSystemClass("org.slf4j.");
        webAppContext.addSystemClass("com.sun.jersey.");
        webAppContext.addSystemClass("org.apache.velocity.");
        if (viewConfig != null) {
            java.lang.String extraClasspath = viewConfig.getExtraClasspath();
            if (extraClasspath != null) {
                webAppContext.setExtraClasspath(extraClasspath);
            }
        }
        return webAppContext;
    }
}