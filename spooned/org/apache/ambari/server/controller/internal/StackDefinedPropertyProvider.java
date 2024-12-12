package org.apache.ambari.server.controller.internal;
public class StackDefinedPropertyProvider implements org.apache.ambari.server.controller.spi.PropertyProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.class);

    @com.google.inject.Inject
    private static org.apache.ambari.server.state.Clusters clusters = null;

    @com.google.inject.Inject
    private static org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = null;

    @com.google.inject.Inject
    private static com.google.inject.Injector injector = null;

    @com.google.inject.Inject
    private static org.apache.ambari.server.controller.metrics.MetricPropertyProviderFactory metricPropertyProviderFactory;

    private org.apache.ambari.server.controller.spi.Resource.Type type = null;

    private java.lang.String clusterNamePropertyId = null;

    private java.lang.String hostNamePropertyId = null;

    private java.lang.String componentNamePropertyId = null;

    private java.lang.String resourceStatePropertyId = null;

    private org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfig = null;

    private org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = null;

    private org.apache.ambari.server.controller.jmx.JMXHostProvider jmxHostProvider;

    private org.apache.ambari.server.controller.spi.PropertyProvider defaultJmx = null;

    private org.apache.ambari.server.controller.spi.PropertyProvider defaultGanglia = null;

    private final org.apache.ambari.server.controller.metrics.MetricHostProvider metricHostProvider;

    private final org.apache.ambari.server.controller.metrics.MetricsServiceProvider metricsServiceProvider;

    private org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider;

    public static final java.lang.String WRAPPED_METRICS_KEY = "WRAPPED_METRICS_KEY";

    @com.google.inject.Inject
    public static void init(com.google.inject.Injector injector) {
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.metaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.metricPropertyProviderFactory = injector.getInstance(org.apache.ambari.server.controller.metrics.MetricPropertyProviderFactory.class);
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.injector = injector;
    }

    public StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.jmx.JMXHostProvider jmxHostProvider, org.apache.ambari.server.controller.metrics.MetricHostProvider metricHostProvider, org.apache.ambari.server.controller.metrics.MetricsServiceProvider serviceProvider, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, java.lang.String clusterPropertyId, java.lang.String hostPropertyId, java.lang.String componentPropertyId, java.lang.String resourceStatePropertyId, org.apache.ambari.server.controller.spi.PropertyProvider defaultJmxPropertyProvider, org.apache.ambari.server.controller.spi.PropertyProvider defaultGangliaPropertyProvider) {
        this.metricHostProvider = metricHostProvider;
        metricsServiceProvider = serviceProvider;
        if (null == clusterPropertyId) {
            throw new java.lang.NullPointerException("Cluster name property id cannot be null");
        }
        if (null == componentPropertyId) {
            throw new java.lang.NullPointerException("Component name property id cannot be null");
        }
        this.type = type;
        clusterNamePropertyId = clusterPropertyId;
        hostNamePropertyId = hostPropertyId;
        componentNamePropertyId = componentPropertyId;
        this.resourceStatePropertyId = resourceStatePropertyId;
        this.jmxHostProvider = jmxHostProvider;
        sslConfig = org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance();
        this.streamProvider = streamProvider;
        defaultJmx = defaultJmxPropertyProvider;
        defaultGanglia = defaultGangliaPropertyProvider;
        cacheProvider = org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.injector.getInstance(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gangliaMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> jmxMap = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> additional = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> overriddenHosts = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.UriInfo> overriddenJmxUris = new java.util.HashMap<>();
        try {
            for (org.apache.ambari.server.controller.spi.Resource r : resources) {
                java.lang.String clusterName = r.getPropertyValue(clusterNamePropertyId).toString();
                java.lang.String componentName = r.getPropertyValue(componentNamePropertyId).toString();
                org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.clusters.getCluster(clusterName);
                org.apache.ambari.server.state.Service service = null;
                try {
                    service = cluster.getServiceByComponentName(componentName);
                } catch (org.apache.ambari.server.ServiceNotFoundException e) {
                    org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.LOG.debug("Could not load component {}", componentName);
                    continue;
                }
                org.apache.ambari.server.state.StackId stack = service.getDesiredStackId();
                java.util.List<org.apache.ambari.server.state.stack.MetricDefinition> defs = org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.metaInfo.getMetrics(stack.getStackName(), stack.getStackVersion(), service.getName(), componentName, type.name());
                if ((null == defs) || (0 == defs.size())) {
                    continue;
                }
                for (org.apache.ambari.server.state.stack.MetricDefinition m : defs) {
                    if (m.getType().equals("ganglia")) {
                        gangliaMap.put(componentName, org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.getPropertyInfo(m));
                        m.getOverriddenHosts().ifPresent(host -> overriddenHosts.put(componentName, host));
                    } else if (m.getType().equals("jmx")) {
                        jmxMap.put(componentName, org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.getPropertyInfo(m));
                        m.getJmxSourceUri().ifPresent(uri -> overriddenJmxUris.put(componentName, uri));
                    } else {
                        org.apache.ambari.server.controller.spi.PropertyProvider pp = getDelegate(m, streamProvider, metricHostProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId, resourceStatePropertyId, componentName);
                        if (pp == null) {
                            pp = getDelegate(m);
                        }
                        if (pp != null) {
                            additional.add(pp);
                        }
                    }
                }
            }
            if (gangliaMap.size() > 0) {
                org.apache.ambari.server.controller.spi.PropertyProvider propertyProvider = org.apache.ambari.server.controller.metrics.MetricsPropertyProvider.createInstance(type, gangliaMap, streamProvider, sslConfig, cacheProvider, metricHostProvider(overriddenHosts), metricsServiceProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId);
                propertyProvider.populateResources(resources, request, predicate);
            } else {
                defaultGanglia.populateResources(resources, request, predicate);
            }
            if (jmxMap.size() > 0) {
                org.apache.ambari.server.controller.jmx.JMXPropertyProvider jpp = org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.metricPropertyProviderFactory.createJMXPropertyProvider(jmxMap, streamProvider, jmxHostProvider(overriddenJmxUris, jmxHostProvider, org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class)), metricHostProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId, resourceStatePropertyId);
                jpp.populateResources(resources, request, predicate);
            } else {
                defaultJmx.populateResources(resources, request, predicate);
            }
            for (org.apache.ambari.server.controller.spi.PropertyProvider pp : additional) {
                pp.populateResources(resources, request, predicate);
            }
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException e) {
            throw e;
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.LOG.error("Error loading deferred resources", e);
            throw new org.apache.ambari.server.controller.spi.SystemException("Error loading deferred resources", e);
        }
        return resources;
    }

    private org.apache.ambari.server.controller.jmx.JMXHostProvider jmxHostProvider(java.util.Map<java.lang.String, org.apache.ambari.server.state.UriInfo> overriddenJmxUris, org.apache.ambari.server.controller.jmx.JMXHostProvider defaultProvider, org.apache.ambari.server.state.ConfigHelper configHelper) {
        return overriddenJmxUris.isEmpty() ? defaultProvider : new org.apache.ambari.server.controller.internal.ConfigBasedJmxHostProvider(overriddenJmxUris, defaultProvider, configHelper);
    }

    private org.apache.ambari.server.controller.metrics.MetricHostProvider metricHostProvider(java.util.Map<java.lang.String, java.lang.String> overriddenHosts) {
        return new org.apache.ambari.server.controller.internal.OverriddenMetricsHostProvider(overriddenHosts, metricHostProvider, org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class));
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        return java.util.Collections.emptySet();
    }

    public static java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> getPropertyInfo(org.apache.ambari.server.state.stack.MetricDefinition def) {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> defs = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.stack.Metric> entry : def.getMetrics().entrySet()) {
            org.apache.ambari.server.state.stack.Metric metric = entry.getValue();
            if (metric.getName() != null) {
                org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = new org.apache.ambari.server.controller.internal.PropertyInfo(metric.getName(), metric.isTemporal(), metric.isPointInTime());
                propertyInfo.setAmsHostMetric(metric.isAmsHostMetric());
                propertyInfo.setUnit(metric.getUnit());
                defs.put(entry.getKey(), propertyInfo);
            }
        }
        return defs;
    }

    private org.apache.ambari.server.controller.spi.PropertyProvider getDelegate(org.apache.ambari.server.state.stack.MetricDefinition definition) {
        try {
            java.lang.Class<?> clz = java.lang.Class.forName(definition.getType());
            try {
                java.lang.reflect.Method m = clz.getMethod("getInstance", java.util.Map.class, java.util.Map.class);
                java.lang.Object o = m.invoke(null, definition.getProperties(), definition.getMetrics());
                return org.apache.ambari.server.controller.spi.PropertyProvider.class.cast(o);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.LOG.info("Could not load singleton or factory method for type '" + definition.getType());
            }
            try {
                java.lang.reflect.Constructor<?> ct = clz.getConstructor(java.util.Map.class, java.util.Map.class);
                java.lang.Object o = ct.newInstance(definition.getProperties(), definition.getMetrics());
                return org.apache.ambari.server.controller.spi.PropertyProvider.class.cast(o);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.LOG.info("Could not find contructor for type '" + definition.getType());
            }
            return org.apache.ambari.server.controller.spi.PropertyProvider.class.cast(clz.newInstance());
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.LOG.error("Could not load class " + definition.getType());
            return null;
        }
    }

    private org.apache.ambari.server.controller.spi.PropertyProvider getDelegate(org.apache.ambari.server.state.stack.MetricDefinition definition, org.apache.ambari.server.controller.utilities.StreamProvider streamProvider, org.apache.ambari.server.controller.metrics.MetricHostProvider metricsHostProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId, java.lang.String componentNamePropertyId, java.lang.String statePropertyId, java.lang.String componentName) {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metrics = org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.getPropertyInfo(definition);
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics = new java.util.HashMap<>();
        componentMetrics.put(org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.WRAPPED_METRICS_KEY, metrics);
        try {
            java.lang.Class<?> clz = java.lang.Class.forName(definition.getType());
            if (clz.equals(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.class)) {
                return org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.metricPropertyProviderFactory.createRESTMetricsPropertyProvider(definition.getProperties(), componentMetrics, streamProvider, metricsHostProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId, statePropertyId, componentName);
            }
            try {
                java.lang.reflect.Constructor<?> ct = clz.getConstructor(java.util.Map.class, java.util.Map.class, org.apache.ambari.server.controller.utilities.StreamProvider.class, org.apache.ambari.server.controller.metrics.MetricHostProvider.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class);
                java.lang.Object o = ct.newInstance(org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.injector, definition.getProperties(), componentMetrics, streamProvider, metricsHostProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId, statePropertyId, componentName);
                return org.apache.ambari.server.controller.spi.PropertyProvider.class.cast(o);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.LOG.info("Could not find contructor for type '" + definition.getType());
            }
            return org.apache.ambari.server.controller.spi.PropertyProvider.class.cast(clz.newInstance());
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.LOG.error("Could not load class " + definition.getType());
            return null;
        }
    }
}