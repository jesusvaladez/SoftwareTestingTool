package org.apache.ambari.server.controller.internal;
public class HttpPropertyProvider extends org.apache.ambari.server.controller.internal.BaseProvider implements org.apache.ambari.server.controller.spi.PropertyProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.HttpPropertyProvider.class);

    private final org.apache.ambari.server.controller.utilities.StreamProvider streamProvider;

    private final java.lang.String clusterNamePropertyId;

    private final java.lang.String hostNamePropertyId;

    private final java.lang.String publicHostNamePropertyId;

    private final java.lang.String componentNamePropertyId;

    private final org.apache.ambari.server.state.Clusters clusters;

    private final java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.controller.internal.HttpPropertyProvider.HttpPropertyRequest>> httpPropertyRequests;

    public HttpPropertyProvider(org.apache.ambari.server.controller.utilities.StreamProvider stream, org.apache.ambari.server.state.Clusters clusters, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId, java.lang.String publicHostNamePropertyId, java.lang.String componentNamePropertyId, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.controller.internal.HttpPropertyProvider.HttpPropertyRequest>> httpPropertyRequests) {
        super(org.apache.ambari.server.controller.internal.HttpPropertyProvider.getSupportedProperties(httpPropertyRequests));
        this.streamProvider = stream;
        this.clusterNamePropertyId = clusterNamePropertyId;
        this.hostNamePropertyId = hostNamePropertyId;
        this.publicHostNamePropertyId = publicHostNamePropertyId;
        this.componentNamePropertyId = componentNamePropertyId;
        this.clusters = clusters;
        this.httpPropertyRequests = httpPropertyRequests;
    }

    private static java.util.Set<java.lang.String> getSupportedProperties(java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.controller.internal.HttpPropertyProvider.HttpPropertyRequest>> httpPropertyRequests) {
        java.util.Set<java.lang.String> supportedProperties = new java.util.HashSet<>();
        for (java.util.List<org.apache.ambari.server.controller.internal.HttpPropertyProvider.HttpPropertyRequest> httpPropertyRequestList : httpPropertyRequests.values()) {
            for (org.apache.ambari.server.controller.internal.HttpPropertyProvider.HttpPropertyRequest httpPropertyRequest : httpPropertyRequestList) {
                supportedProperties.addAll(httpPropertyRequest.getSupportedProperties());
            }
        }
        return java.util.Collections.unmodifiableSet(supportedProperties);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<java.lang.String> ids = getRequestPropertyIds(request, predicate);
        if (ids.size() == 0) {
            return resources;
        }
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(clusterNamePropertyId)));
            java.lang.String hostName = ((java.lang.String) (resource.getPropertyValue(hostNamePropertyId)));
            java.lang.String publicHostName = ((java.lang.String) (resource.getPropertyValue(publicHostNamePropertyId)));
            java.lang.String componentName = ((java.lang.String) (resource.getPropertyValue(componentNamePropertyId)));
            if ((((clusterName != null) && (hostName != null)) && (componentName != null)) && httpPropertyRequests.containsKey(componentName)) {
                try {
                    org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
                    java.util.List<org.apache.ambari.server.controller.internal.HttpPropertyProvider.HttpPropertyRequest> httpPropertyRequestList = httpPropertyRequests.get(componentName);
                    for (org.apache.ambari.server.controller.internal.HttpPropertyProvider.HttpPropertyRequest httpPropertyRequest : httpPropertyRequestList) {
                        populateResource(httpPropertyRequest, resource, cluster, hostName, publicHostName);
                    }
                } catch (org.apache.ambari.server.AmbariException e) {
                    java.lang.String msg = java.lang.String.format("Could not load cluster with name %s.", clusterName);
                    org.apache.ambari.server.controller.internal.HttpPropertyProvider.LOG.debug(msg, e);
                    throw new org.apache.ambari.server.controller.spi.SystemException(msg, e);
                }
            }
        }
        return resources;
    }

    private void populateResource(org.apache.ambari.server.controller.internal.HttpPropertyProvider.HttpPropertyRequest httpPropertyRequest, org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.state.Cluster cluster, java.lang.String hostName, java.lang.String publicHostName) throws org.apache.ambari.server.controller.spi.SystemException {
        java.lang.String url = httpPropertyRequest.getUrl(cluster, hostName);
        try {
            java.io.InputStream inputStream = streamProvider.readFrom(url);
            try {
                httpPropertyRequest.populateResource(resource, inputStream);
            } finally {
                try {
                    inputStream.close();
                } catch (java.io.IOException ioe) {
                    org.apache.ambari.server.controller.internal.HttpPropertyProvider.LOG.error(java.lang.String.format("Error closing HTTP response stream %s", url), ioe);
                }
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.internal.HttpPropertyProvider.LOG.debug(java.lang.String.format("Error reading HTTP response from %s", url), e);
            if ((publicHostName != null) && (!publicHostName.equalsIgnoreCase(hostName))) {
                java.lang.String publicUrl = httpPropertyRequest.getUrl(cluster, publicHostName);
                org.apache.ambari.server.controller.internal.HttpPropertyProvider.LOG.debug(java.lang.String.format("Retry using public host name url %s", publicUrl));
                try {
                    java.io.InputStream inputStream = streamProvider.readFrom(publicUrl);
                    try {
                        httpPropertyRequest.populateResource(resource, inputStream);
                    } finally {
                        try {
                            inputStream.close();
                        } catch (java.io.IOException ioe) {
                            org.apache.ambari.server.controller.internal.HttpPropertyProvider.LOG.error(java.lang.String.format("Error closing HTTP response stream %s", url), ioe);
                        }
                    }
                } catch (java.lang.Exception ex) {
                    org.apache.ambari.server.controller.internal.HttpPropertyProvider.LOG.debug(java.lang.String.format("Error reading HTTP response from public host name url %s", url), ex);
                }
            }
        }
    }

    public static abstract class HttpPropertyRequest {
        private final java.util.Map<java.lang.String, java.lang.String> propertyMappings;

        protected HttpPropertyRequest(java.util.Map<java.lang.String, java.lang.String> propertyMappings) {
            this.propertyMappings = propertyMappings;
        }

        public java.util.Collection<java.lang.String> getSupportedProperties() {
            return propertyMappings.values();
        }

        protected java.util.Map<java.lang.String, java.lang.String> getPropertyMappings() {
            return propertyMappings;
        }

        public abstract java.lang.String getUrl(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostName) throws org.apache.ambari.server.controller.spi.SystemException;

        public abstract void populateResource(org.apache.ambari.server.controller.spi.Resource resource, java.io.InputStream inputStream) throws org.apache.ambari.server.controller.spi.SystemException;
    }
}