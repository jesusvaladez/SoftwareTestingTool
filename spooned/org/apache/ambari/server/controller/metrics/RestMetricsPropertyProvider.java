package org.apache.ambari.server.controller.metrics;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javax.annotation.Nullable;
public class RestMetricsPropertyProvider extends org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController amc;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private com.google.gson.Gson gson;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.services.MetricsRetrievalService metricsRetrievalService;

    private final java.util.Map<java.lang.String, java.lang.String> metricsProperties;

    private final org.apache.ambari.server.controller.utilities.StreamProvider streamProvider;

    private final java.lang.String clusterNamePropertyId;

    private final java.lang.String componentNamePropertyId;

    private final java.lang.String statePropertyId;

    private final java.lang.String componentName;

    private static final java.lang.String DEFAULT_PORT_PROPERTY = "default_port";

    private static final java.lang.String PORT_CONFIG_TYPE_PROPERTY = "port_config_type";

    private static final java.lang.String PORT_PROPERTY_NAME_PROPERTY = "port_property_name";

    private static final java.lang.String HTTPS_PORT_PROPERTY_NAME_PROPERTY = "https_port_property_name";

    private static final java.lang.String PROTOCOL_OVERRIDE_PROPERTY = "protocol";

    private static final java.lang.String HTTPS_PROTOCOL_PROPERTY = "https_property_name";

    private static final java.lang.String HTTP_PROTOCOL = "http";

    private static final java.lang.String HTTPS_PROTOCOL = "https";

    private static final java.lang.String DEFAULT_PROTOCOL = org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.HTTP_PROTOCOL;

    public static final java.lang.String URL_PATH_SEPARATOR = "##";

    public static final java.lang.String DOCUMENT_PATH_SEPARATOR = "#";

    @com.google.inject.assistedinject.AssistedInject
    RestMetricsPropertyProvider(@com.google.inject.assistedinject.Assisted("metricsProperties")
    java.util.Map<java.lang.String, java.lang.String> metricsProperties, @com.google.inject.assistedinject.Assisted("componentMetrics")
    java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics, @com.google.inject.assistedinject.Assisted("streamProvider")
    org.apache.ambari.server.controller.utilities.StreamProvider streamProvider, @com.google.inject.assistedinject.Assisted("metricHostProvider")
    org.apache.ambari.server.controller.metrics.MetricHostProvider metricHostProvider, @com.google.inject.assistedinject.Assisted("clusterNamePropertyId")
    java.lang.String clusterNamePropertyId, @com.google.inject.assistedinject.Assisted("hostNamePropertyId")
    @javax.annotation.Nullable
    java.lang.String hostNamePropertyId, @com.google.inject.assistedinject.Assisted("componentNamePropertyId")
    java.lang.String componentNamePropertyId, @com.google.inject.assistedinject.Assisted("statePropertyId")
    @javax.annotation.Nullable
    java.lang.String statePropertyId, @com.google.inject.assistedinject.Assisted("componentName")
    @javax.annotation.Nullable
    java.lang.String componentName) {
        super(componentMetrics, hostNamePropertyId, metricHostProvider, clusterNamePropertyId);
        this.metricsProperties = metricsProperties;
        this.streamProvider = streamProvider;
        this.clusterNamePropertyId = clusterNamePropertyId;
        this.componentNamePropertyId = componentNamePropertyId;
        this.statePropertyId = statePropertyId;
        this.componentName = componentName;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.Resource populateResource(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.Ticket ticket) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<java.lang.String> ids = getRequestPropertyIds(request, predicate);
        java.util.Set<java.lang.String> temporalIds = new java.util.HashSet<>();
        java.lang.String resourceComponentName = ((java.lang.String) (resource.getPropertyValue(componentNamePropertyId)));
        if (!componentName.equals(resourceComponentName)) {
            return resource;
        }
        for (java.lang.String id : ids) {
            if (request.getTemporalInfo(id) != null) {
                temporalIds.add(id);
            }
        }
        ids.removeAll(temporalIds);
        if (ids.isEmpty()) {
            return resource;
        }
        if (statePropertyId != null) {
            java.lang.String state = ((java.lang.String) (resource.getPropertyValue(statePropertyId)));
            if ((state != null) && (!org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.healthyStates.contains(state))) {
                return resource;
            }
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> propertyInfos = getComponentMetrics().get(org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.WRAPPED_METRICS_KEY);
        if (propertyInfos == null) {
            return resource;
        }
        java.lang.String protocol = null;
        java.lang.String port = "-1";
        java.lang.String hostname = null;
        try {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(clusterNamePropertyId)));
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
            hostname = getHost(resource, clusterName, resourceComponentName);
            if (hostname == null) {
                java.lang.String msg = java.lang.String.format("Unable to get component REST metrics. " + "No host name for %s.", resourceComponentName);
                org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.LOG.warn(msg);
                return resource;
            }
            protocol = resolveProtocol(cluster, hostname);
            port = resolvePort(cluster, hostname, resourceComponentName, metricsProperties, protocol);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.rethrowSystemException(e);
        }
        java.util.Set<java.lang.String> resultIds = new java.util.HashSet<>();
        for (java.lang.String id : ids) {
            for (java.lang.String metricId : propertyInfos.keySet()) {
                if (metricId.startsWith(id)) {
                    resultIds.add(metricId);
                }
            }
        }
        java.util.HashMap<java.lang.String, java.util.Set<java.lang.String>> urls = extractPropertyURLs(resultIds, propertyInfos);
        for (java.lang.String url : urls.keySet()) {
            java.lang.String spec = getSpec(protocol, hostname, port, url);
            metricsRetrievalService.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.REST, streamProvider, spec);
            java.util.Map<java.lang.String, java.lang.String> jsonMap = metricsRetrievalService.getCachedRESTMetric(spec);
            if (null == jsonMap) {
                return resource;
            }
            if (!ticket.isValid()) {
                return resource;
            }
            try {
                extractValuesFromJSON(jsonMap, urls.get(url), resource, propertyInfos);
            } catch (org.apache.ambari.server.AmbariException ambariException) {
                org.apache.ambari.server.AmbariException detailedException = new org.apache.ambari.server.AmbariException(java.lang.String.format("Unable to get REST metrics from the for %s at %s", resourceComponentName, spec), ambariException);
                org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.logException(detailedException);
            }
        }
        return resource;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        java.util.Set<java.lang.String> unsupported = new java.util.HashSet<>();
        for (java.lang.String propertyId : propertyIds) {
            if (!getComponentMetrics().get(org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.WRAPPED_METRICS_KEY).containsKey(propertyId)) {
                unsupported.add(propertyId);
            }
        }
        return unsupported;
    }

    protected java.lang.String resolvePort(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostname, java.lang.String componentName, java.util.Map<java.lang.String, java.lang.String> metricsProperties, java.lang.String protocol) throws org.apache.ambari.server.AmbariException {
        java.lang.String portConfigType = null;
        java.lang.String portPropertyNameInMetricsProperties = (protocol.equalsIgnoreCase(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.HTTPS_PROTOCOL)) ? org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.HTTPS_PORT_PROPERTY_NAME_PROPERTY : org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.PORT_PROPERTY_NAME_PROPERTY;
        java.lang.String portPropertyName = null;
        if (metricsProperties.containsKey(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.PORT_CONFIG_TYPE_PROPERTY) && metricsProperties.containsKey(portPropertyNameInMetricsProperties)) {
            portConfigType = metricsProperties.get(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.PORT_CONFIG_TYPE_PROPERTY);
            portPropertyName = metricsProperties.get(portPropertyNameInMetricsProperties);
        }
        java.lang.String portStr = getPropertyValueByNameAndConfigType(portPropertyName, portConfigType, cluster, hostname);
        if (portStr == null) {
            if (metricsProperties.containsKey(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.DEFAULT_PORT_PROPERTY)) {
                portStr = metricsProperties.get(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.DEFAULT_PORT_PROPERTY);
            } else {
                java.lang.String message = java.lang.String.format("Can not determine REST port for " + ((("component %s. " + "Default REST port property %s is not defined at metrics.json ") + "file for service, and there is no any other available ways ") + "to determine port information."), componentName, org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.DEFAULT_PORT_PROPERTY);
                throw new org.apache.ambari.server.AmbariException(message);
            }
        }
        return portStr;
    }

    private java.lang.String getPropertyValueByNameAndConfigType(java.lang.String propertyName, java.lang.String configType, org.apache.ambari.server.state.Cluster cluster, java.lang.String hostname) {
        java.lang.String result = null;
        if ((configType != null) && (propertyName != null)) {
            try {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configTags = amc.findConfigurationTagsWithOverrides(cluster, hostname, null);
                if (configTags.containsKey(configType)) {
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = amc.getConfigHelper().getEffectiveConfigProperties(cluster, java.util.Collections.singletonMap(configType, configTags.get(configType)));
                    java.util.Map<java.lang.String, java.lang.String> config = properties.get(configType);
                    if ((config != null) && config.containsKey(propertyName)) {
                        result = config.get(propertyName);
                    }
                }
            } catch (org.apache.ambari.server.AmbariException e) {
                java.lang.String message = java.lang.String.format("Can not extract configs for " + "component = %s, hostname = %s, config type = %s, property name = %s", componentName, hostname, configType, propertyName);
                org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.LOG.warn(message, e);
            }
            if (result == null) {
                java.lang.String message = java.lang.String.format("Can not extract property for " + ((("component %s from configurations. " + "Config tag = %s, config key name = %s, ") + "hostname = %s. Probably metrics.json file for ") + "service is misspelled."), componentName, configType, propertyName, hostname);
                org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.LOG.debug(message);
            }
        }
        return result;
    }

    private java.lang.String resolveProtocol(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostname) {
        java.lang.String protocol = org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.DEFAULT_PROTOCOL;
        if (metricsProperties.containsKey(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.PORT_CONFIG_TYPE_PROPERTY) && metricsProperties.containsKey(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.HTTPS_PROTOCOL_PROPERTY)) {
            java.lang.String configType = metricsProperties.get(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.PORT_CONFIG_TYPE_PROPERTY);
            java.lang.String propertyName = metricsProperties.get(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.HTTPS_PROTOCOL_PROPERTY);
            java.lang.String value = getPropertyValueByNameAndConfigType(propertyName, configType, cluster, hostname);
            if (value != null) {
                return org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.HTTPS_PROTOCOL;
            }
        }
        if (metricsProperties.containsKey(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.PROTOCOL_OVERRIDE_PROPERTY)) {
            protocol = metricsProperties.get(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.PROTOCOL_OVERRIDE_PROPERTY).toLowerCase();
            if ((!protocol.equals(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.HTTP_PROTOCOL)) && (!protocol.equals(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.HTTPS_PROTOCOL))) {
                java.lang.String message = java.lang.String.format("Unsupported protocol type %s, falling back to %s", protocol, org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.DEFAULT_PROTOCOL);
                org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.LOG.warn(message);
                protocol = org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.DEFAULT_PROTOCOL;
            }
        } else {
            protocol = org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.DEFAULT_PROTOCOL;
        }
        return protocol;
    }

    private java.lang.String extractMetricsURL(java.lang.String metricsPath) throws java.lang.IllegalArgumentException {
        return validateAndExtractPathParts(metricsPath)[0];
    }

    private java.lang.String extractDocumentPath(java.lang.String metricsPath) throws java.lang.IllegalArgumentException {
        return validateAndExtractPathParts(metricsPath)[1];
    }

    private java.lang.String[] validateAndExtractPathParts(java.lang.String metricsPath) throws java.lang.IllegalArgumentException {
        java.lang.String[] pathParts = metricsPath.split(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.URL_PATH_SEPARATOR);
        if (pathParts.length == 2) {
            return pathParts;
        } else {
            java.lang.String message = java.lang.String.format("Metrics path %s does not contain or contains" + (("more than one %s sequence. That probably " + "means that the mentioned metrics path is misspelled. ") + "Please check the relevant metrics.json file"), metricsPath, org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.URL_PATH_SEPARATOR);
            throw new java.lang.IllegalArgumentException(message);
        }
    }

    private java.util.HashMap<java.lang.String, java.util.Set<java.lang.String>> extractPropertyURLs(java.util.Set<java.lang.String> ids, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> propertyInfos) {
        java.util.HashMap<java.lang.String, java.util.Set<java.lang.String>> result = new java.util.HashMap<>();
        for (java.lang.String requestedPropertyId : ids) {
            org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = propertyInfos.get(requestedPropertyId);
            java.lang.String metricsPath = propertyInfo.getPropertyId();
            java.lang.String url = extractMetricsURL(metricsPath);
            java.util.Set<java.lang.String> set;
            if (!result.containsKey(url)) {
                set = new java.util.HashSet<>();
                result.put(url, set);
            } else {
                set = result.get(url);
            }
            set.add(requestedPropertyId);
        }
        return result;
    }

    private void extractValuesFromJSON(java.util.Map<java.lang.String, java.lang.String> jsonMap, java.util.Set<java.lang.String> requestedPropertyIds, org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> propertyInfos) throws org.apache.ambari.server.AmbariException {
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.Object, java.lang.Object>>() {}.getType();
        for (java.lang.String requestedPropertyId : requestedPropertyIds) {
            org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = propertyInfos.get(requestedPropertyId);
            java.lang.String metricsPath = propertyInfo.getPropertyId();
            java.lang.String documentPath = extractDocumentPath(metricsPath);
            java.lang.String[] docPath = documentPath.split(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.DOCUMENT_PATH_SEPARATOR);
            java.util.Map<java.lang.String, java.lang.String> subMap = jsonMap;
            for (int i = 0; i < docPath.length; i++) {
                java.lang.String pathElement = docPath[i];
                if (!subMap.containsKey(pathElement)) {
                    java.lang.String message = java.lang.String.format("Can not fetch %dth element of document path (%s) " + "from json. Wrong metrics path: %s", i, pathElement, metricsPath);
                    throw new org.apache.ambari.server.AmbariException(message);
                }
                java.lang.Object jsonSubElement = jsonMap.get(pathElement);
                if (i == (docPath.length - 1)) {
                    resource.setProperty(requestedPropertyId, jsonSubElement);
                } else {
                    subMap = gson.fromJson(((com.google.gson.JsonElement) (jsonSubElement)), type);
                }
            }
        }
    }
}