package org.apache.ambari.server.controller.jmx;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javax.annotation.Nullable;
public class JMXPropertyProvider extends org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider {
    private static final java.lang.String NAME_KEY = "name";

    private static final java.lang.String PORT_KEY = "tag.port";

    private static final java.lang.String DOT_REPLACEMENT_CHAR = "#";

    private static final java.util.Map<java.lang.String, java.lang.String> DEFAULT_JMX_PORTS = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> AD_HOC_PROPERTIES = new java.util.HashMap<>();

    static {
        DEFAULT_JMX_PORTS.put("NAMENODE", "50070");
        DEFAULT_JMX_PORTS.put("DATANODE", "50075");
        DEFAULT_JMX_PORTS.put("HBASE_MASTER", "60010");
        DEFAULT_JMX_PORTS.put("HBASE_REGIONSERVER", "60030");
        DEFAULT_JMX_PORTS.put("RESOURCEMANAGER", "8088");
        DEFAULT_JMX_PORTS.put("HISTORYSERVER", "19888");
        DEFAULT_JMX_PORTS.put("NODEMANAGER", "8042");
        DEFAULT_JMX_PORTS.put("JOURNALNODE", "8480");
        DEFAULT_JMX_PORTS.put("STORM_REST_API", "8745");
        DEFAULT_JMX_PORTS.put("OZONE_MANAGER", "9874");
        DEFAULT_JMX_PORTS.put("STORAGE_CONTAINER_MANAGER", "9876");
        AD_HOC_PROPERTIES.put("NAMENODE", java.util.Collections.singletonMap("metrics/dfs/FSNamesystem/HAState", "/jmx?get=Hadoop:service=NameNode,name=FSNamesystem::tag.HAState"));
    }

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.jmx.JMXPropertyProvider.class);

    private static final java.util.regex.Pattern dotReplacementCharPattern = java.util.regex.Pattern.compile(org.apache.ambari.server.controller.jmx.JMXPropertyProvider.DOT_REPLACEMENT_CHAR);

    private final org.apache.ambari.server.controller.utilities.StreamProvider streamProvider;

    private final org.apache.ambari.server.controller.jmx.JMXHostProvider jmxHostProvider;

    private final java.lang.String clusterNamePropertyId;

    private final java.lang.String hostNamePropertyId;

    private final java.lang.String componentNamePropertyId;

    private final java.lang.String statePropertyId;

    private final java.util.Map<java.lang.String, java.lang.String> clusterComponentPortsMap;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.services.MetricsRetrievalService metricsRetrievalService;

    @com.google.inject.assistedinject.AssistedInject
    JMXPropertyProvider(@com.google.inject.assistedinject.Assisted("componentMetrics")
    java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics, @com.google.inject.assistedinject.Assisted("streamProvider")
    org.apache.ambari.server.controller.utilities.StreamProvider streamProvider, @com.google.inject.assistedinject.Assisted("jmxHostProvider")
    org.apache.ambari.server.controller.jmx.JMXHostProvider jmxHostProvider, @com.google.inject.assistedinject.Assisted("metricHostProvider")
    org.apache.ambari.server.controller.metrics.MetricHostProvider metricHostProvider, @com.google.inject.assistedinject.Assisted("clusterNamePropertyId")
    java.lang.String clusterNamePropertyId, @com.google.inject.assistedinject.Assisted("hostNamePropertyId")
    @javax.annotation.Nullable
    java.lang.String hostNamePropertyId, @com.google.inject.assistedinject.Assisted("componentNamePropertyId")
    java.lang.String componentNamePropertyId, @com.google.inject.assistedinject.Assisted("statePropertyId")
    @javax.annotation.Nullable
    java.lang.String statePropertyId) {
        super(componentMetrics, hostNamePropertyId, metricHostProvider, clusterNamePropertyId);
        this.streamProvider = streamProvider;
        this.jmxHostProvider = jmxHostProvider;
        this.clusterNamePropertyId = clusterNamePropertyId;
        this.hostNamePropertyId = hostNamePropertyId;
        this.componentNamePropertyId = componentNamePropertyId;
        this.statePropertyId = statePropertyId;
        clusterComponentPortsMap = new java.util.HashMap<>();
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        clusterComponentPortsMap.clear();
        return super.populateResources(resources, request, predicate);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.Resource populateResource(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.Ticket ticket) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<java.lang.String> ids = getRequestPropertyIds(request, predicate);
        java.util.Set<java.lang.String> unsupportedIds = new java.util.HashSet<>();
        java.lang.String componentName = ((java.lang.String) (resource.getPropertyValue(componentNamePropertyId)));
        if (getComponentMetrics().get(componentName) == null) {
            return resource;
        }
        for (java.lang.String id : ids) {
            if (request.getTemporalInfo(id) != null) {
                unsupportedIds.add(id);
            }
            if (!isSupportedPropertyId(componentName, id)) {
                unsupportedIds.add(id);
            }
        }
        ids.removeAll(unsupportedIds);
        if (ids.isEmpty()) {
            return resource;
        }
        if (statePropertyId != null) {
            java.lang.String state = ((java.lang.String) (resource.getPropertyValue(statePropertyId)));
            if ((state != null) && (!org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.healthyStates.contains(state))) {
                return resource;
            }
        }
        java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(clusterNamePropertyId)));
        java.lang.String protocol = jmxHostProvider.getJMXProtocol(clusterName, componentName);
        boolean httpsEnabled = false;
        if (protocol.equals("https")) {
            httpsEnabled = true;
        }
        java.util.Set<java.lang.String> hostNames = getHosts(resource, clusterName, componentName);
        if ((hostNames == null) || hostNames.isEmpty()) {
            org.apache.ambari.server.controller.jmx.JMXPropertyProvider.LOG.warn("Unable to get JMX metrics.  No host name for " + componentName);
            return resource;
        }
        java.lang.String spec = null;
        for (java.lang.String hostName : hostNames) {
            try {
                java.lang.String port = getPort(clusterName, componentName, hostName, httpsEnabled);
                java.lang.String publicHostName = jmxHostProvider.getPublicHostName(clusterName, hostName);
                if (port == null) {
                    org.apache.ambari.server.controller.jmx.JMXPropertyProvider.LOG.warn("Unable to get JMX metrics.  No port value for " + componentName);
                    return resource;
                }
                java.lang.String jmxUrl = getSpec(protocol, hostName, port, "/jmx");
                metricsRetrievalService.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.JMX, streamProvider, jmxUrl);
                org.apache.ambari.server.controller.jmx.JMXMetricHolder jmxMetricHolder = metricsRetrievalService.getCachedJMXMetric(jmxUrl);
                if ((jmxMetricHolder == null) && (!hostName.equalsIgnoreCase(publicHostName))) {
                    java.lang.String publicJmxUrl = getSpec(protocol, publicHostName, port, "/jmx");
                    metricsRetrievalService.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.JMX, streamProvider, publicJmxUrl);
                    jmxMetricHolder = metricsRetrievalService.getCachedJMXMetric(publicJmxUrl);
                }
                if (!ticket.isValid()) {
                    return resource;
                }
                if (null != jmxMetricHolder) {
                    getHadoopMetricValue(jmxMetricHolder, ids, resource, request, ticket);
                }
                if (org.apache.ambari.server.controller.jmx.JMXPropertyProvider.AD_HOC_PROPERTIES.containsKey(componentName)) {
                    for (java.lang.String propertyId : ids) {
                        for (java.lang.String adHocId : org.apache.ambari.server.controller.jmx.JMXPropertyProvider.AD_HOC_PROPERTIES.get(componentName).keySet()) {
                            java.lang.String queryURL = null;
                            if (adHocId.equals(propertyId) || adHocId.startsWith(propertyId + '/')) {
                                queryURL = org.apache.ambari.server.controller.jmx.JMXPropertyProvider.AD_HOC_PROPERTIES.get(componentName).get(adHocId);
                            }
                            if (queryURL != null) {
                                java.lang.String adHocUrl = getSpec(protocol, hostName, port, queryURL);
                                metricsRetrievalService.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.JMX, streamProvider, adHocUrl);
                                org.apache.ambari.server.controller.jmx.JMXMetricHolder adHocJMXMetricHolder = metricsRetrievalService.getCachedJMXMetric(adHocUrl);
                                if ((adHocJMXMetricHolder == null) && (!hostName.equalsIgnoreCase(publicHostName))) {
                                    java.lang.String publicAdHocUrl = getSpec(protocol, publicHostName, port, queryURL);
                                    metricsRetrievalService.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.JMX, streamProvider, publicAdHocUrl);
                                    adHocJMXMetricHolder = metricsRetrievalService.getCachedJMXMetric(publicAdHocUrl);
                                }
                                if (!ticket.isValid()) {
                                    return resource;
                                }
                                if (null != adHocJMXMetricHolder) {
                                    getHadoopMetricValue(adHocJMXMetricHolder, java.util.Collections.singleton(propertyId), resource, request, ticket);
                                }
                            }
                        }
                    }
                }
            } catch (java.io.IOException e) {
                org.apache.ambari.server.AmbariException detailedException = new org.apache.ambari.server.AmbariException(java.lang.String.format("Unable to get JMX metrics from the host %s for the component %s. Spec: %s", hostName, componentName, spec), e);
                org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.logException(detailedException);
            }
        }
        return resource;
    }

    private void getHadoopMetricValue(org.apache.ambari.server.controller.jmx.JMXMetricHolder metricHolder, java.util.Set<java.lang.String> ids, org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.Ticket ticket) throws java.io.IOException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> categories = new java.util.HashMap<>();
        java.lang.String componentName = ((java.lang.String) (resource.getPropertyValue(componentNamePropertyId)));
        java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(clusterNamePropertyId)));
        for (java.util.Map<java.lang.String, java.lang.Object> bean : metricHolder.getBeans()) {
            java.lang.String category = getCategory(bean, clusterName, componentName);
            if (category != null) {
                categories.put(category, bean);
            }
        }
        for (java.lang.String propertyId : ids) {
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> propertyInfoMap = getPropertyInfoMap(componentName, propertyId);
            java.lang.String requestedPropertyId = propertyId;
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> entry : propertyInfoMap.entrySet()) {
                org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = entry.getValue();
                propertyId = entry.getKey();
                if (propertyInfo.isPointInTime()) {
                    java.lang.String property = propertyInfo.getPropertyId();
                    java.lang.String category = "";
                    java.util.List<java.lang.String> keyList = new java.util.LinkedList<>();
                    int keyStartIndex = property.indexOf('[');
                    if ((-1) != keyStartIndex) {
                        int keyEndIndex = property.indexOf(']', keyStartIndex);
                        if (((-1) != keyEndIndex) && (keyEndIndex > keyStartIndex)) {
                            keyList.add(property.substring(keyStartIndex + 1, keyEndIndex));
                        }
                    }
                    if (!containsArguments(propertyId)) {
                        int dotIndex = property.indexOf('.', property.indexOf('='));
                        if ((-1) != dotIndex) {
                            category = property.substring(0, dotIndex);
                            property = ((-1) == keyStartIndex) ? property.substring(dotIndex + 1) : property.substring(dotIndex + 1, keyStartIndex);
                        }
                    } else {
                        int firstKeyIndex = (keyStartIndex > (-1)) ? keyStartIndex : property.length();
                        int dotIndex = property.lastIndexOf('.', firstKeyIndex);
                        if (dotIndex != (-1)) {
                            category = property.substring(0, dotIndex);
                            property = property.substring(dotIndex + 1, firstKeyIndex);
                        }
                    }
                    if (containsArguments(propertyId)) {
                        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(category);
                        for (java.lang.String jmxCat : categories.keySet()) {
                            java.util.regex.Matcher matcher = pattern.matcher(jmxCat);
                            if (matcher.matches()) {
                                java.lang.String newPropertyId = propertyId;
                                for (int i = 0; i < matcher.groupCount(); i++) {
                                    newPropertyId = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.substituteArgument(newPropertyId, "$" + (i + 1), matcher.group(i + 1));
                                }
                                if (org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.isRequestedPropertyId(newPropertyId, requestedPropertyId, request)) {
                                    if (!ticket.isValid()) {
                                        return;
                                    }
                                    setResourceValue(resource, categories, newPropertyId, jmxCat, property, keyList);
                                }
                            }
                        }
                    } else {
                        if (!ticket.isValid()) {
                            return;
                        }
                        setResourceValue(resource, categories, propertyId, category, property, keyList);
                    }
                }
            }
        }
    }

    private void setResourceValue(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> categories, java.lang.String propertyId, java.lang.String category, java.lang.String property, java.util.List<java.lang.String> keyList) {
        java.util.Map<java.lang.String, java.lang.Object> properties = categories.get(category);
        if (property.contains(org.apache.ambari.server.controller.jmx.JMXPropertyProvider.DOT_REPLACEMENT_CHAR)) {
            property = org.apache.ambari.server.controller.jmx.JMXPropertyProvider.dotReplacementCharPattern.matcher(property).replaceAll(".");
        }
        if ((properties != null) && properties.containsKey(property)) {
            java.lang.Object value = properties.get(property);
            if ((keyList.size() > 0) && (value instanceof java.util.Map)) {
                java.util.Map<?, ?> map = ((java.util.Map<?, ?>) (value));
                for (java.lang.String key : keyList) {
                    value = map.get(key);
                    if (value instanceof java.util.Map) {
                        map = ((java.util.Map<?, ?>) (value));
                    } else {
                        break;
                    }
                }
            }
            resource.setProperty(propertyId, value);
        }
    }

    private java.lang.String getPort(java.lang.String clusterName, java.lang.String componentName, java.lang.String hostName, boolean httpsEnabled) throws org.apache.ambari.server.controller.spi.SystemException {
        java.lang.String portMapKey = java.lang.String.format("%s-%s-%s", clusterName, componentName, httpsEnabled);
        java.lang.String port = clusterComponentPortsMap.get(portMapKey);
        if (port == null) {
            port = jmxHostProvider.getPort(clusterName, componentName, hostName, httpsEnabled);
            port = (port == null) ? org.apache.ambari.server.controller.jmx.JMXPropertyProvider.DEFAULT_JMX_PORTS.get(componentName) : port;
            clusterComponentPortsMap.put(portMapKey, port);
        }
        return port;
    }

    private java.util.Set<java.lang.String> getHosts(org.apache.ambari.server.controller.spi.Resource resource, java.lang.String clusterName, java.lang.String componentName) {
        return hostNamePropertyId == null ? jmxHostProvider.getHostNames(clusterName, componentName) : java.util.Collections.singleton(((java.lang.String) (resource.getPropertyValue(hostNamePropertyId))));
    }

    private java.lang.String getCategory(java.util.Map<java.lang.String, java.lang.Object> bean, java.lang.String clusterName, java.lang.String componentName) {
        if (bean.containsKey(org.apache.ambari.server.controller.jmx.JMXPropertyProvider.NAME_KEY)) {
            java.lang.String name = ((java.lang.String) (bean.get(org.apache.ambari.server.controller.jmx.JMXPropertyProvider.NAME_KEY)));
            if (bean.containsKey(org.apache.ambari.server.controller.jmx.JMXPropertyProvider.PORT_KEY)) {
                java.lang.String port = ((java.lang.String) (bean.get(org.apache.ambari.server.controller.jmx.JMXPropertyProvider.PORT_KEY)));
                java.lang.String tag = jmxHostProvider.getJMXRpcMetricTag(clusterName, componentName, port);
                name = name.replace("ForPort" + port, tag == null ? "" : ",tag=" + tag);
            }
            return name;
        }
        return null;
    }
}