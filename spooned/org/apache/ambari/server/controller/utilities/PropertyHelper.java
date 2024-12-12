package org.apache.ambari.server.controller.utilities;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
public class PropertyHelper {
    private static final java.lang.String GANGLIA_PROPERTIES_FILE = "ganglia_properties.json";

    private static final java.lang.String SQLSERVER_PROPERTIES_FILE = "sqlserver_properties.json";

    private static final java.lang.String JMX_PROPERTIES_FILE = "jmx_properties.json";

    public static final char EXTERNAL_PATH_SEP = '/';

    public static final java.util.List<java.lang.String> AGGREGATE_FUNCTION_IDENTIFIERS = com.google.common.collect.ImmutableList.of("._sum", "._max", "._min", "._avg", "._rate");

    private static final java.util.List<org.apache.ambari.server.controller.spi.Resource.InternalType> REPORT_METRIC_RESOURCES = com.google.common.collect.ImmutableList.of(org.apache.ambari.server.controller.spi.Resource.InternalType.Cluster, org.apache.ambari.server.controller.spi.Resource.InternalType.Host);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Set<java.lang.String>> PROPERTY_IDS = new java.util.HashMap<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>>> JMX_PROPERTY_IDS = org.apache.ambari.server.controller.utilities.PropertyHelper.readPropertyProviderIds(org.apache.ambari.server.controller.utilities.PropertyHelper.JMX_PROPERTIES_FILE);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>>> GANGLIA_PROPERTY_IDS = org.apache.ambari.server.controller.utilities.PropertyHelper.readPropertyProviderIds(org.apache.ambari.server.controller.utilities.PropertyHelper.GANGLIA_PROPERTIES_FILE);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>>> SQLSERVER_PROPERTY_IDS = org.apache.ambari.server.controller.utilities.PropertyHelper.readPropertyProviderIds(org.apache.ambari.server.controller.utilities.PropertyHelper.SQLSERVER_PROPERTIES_FILE);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, java.util.List<java.lang.String>> RPC_METRIC_SUFFIXES = new java.util.HashMap<>();

    private static final java.util.regex.Pattern CHECK_FOR_METRIC_ARGUMENTS_REGEX = java.util.regex.Pattern.compile(".*\\$\\d+.*");

    private static final java.util.regex.Pattern METRIC_CATEGORY_TOKENIZE_REGEX = java.util.regex.Pattern.compile("/+(?=([^\"\\\\\\\\]*(\\\\\\\\.|\"([^\"\\\\\\\\]*\\\\\\\\.)*[^\"\\\\\\\\]*\"))*[^\"]*$)");

    static {
        RPC_METRIC_SUFFIXES.put("metrics/rpc/", com.google.common.collect.ImmutableList.of("client", "datanode", "healthcheck"));
        RPC_METRIC_SUFFIXES.put("metrics/rpcdetailed/", com.google.common.collect.ImmutableList.of("client", "datanode", "healthcheck"));
    }

    public static java.lang.String getPropertyId(java.lang.String category, java.lang.String name) {
        java.lang.String propertyId = ((category == null) || category.isEmpty()) ? name : (name == null) || name.isEmpty() ? category : (category + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + name;
        if (propertyId.endsWith("/")) {
            propertyId = propertyId.substring(0, propertyId.length() - 1);
        }
        return propertyId;
    }

    public static java.util.Set<java.lang.String> getPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        java.util.Set<java.lang.String> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.PROPERTY_IDS.get(resourceType.getInternalType());
        return propertyIds == null ? java.util.Collections.emptySet() : propertyIds;
    }

    public static void setPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type resourceType, java.util.Set<java.lang.String> propertyIds) {
        org.apache.ambari.server.controller.utilities.PropertyHelper.PROPERTY_IDS.put(resourceType.getInternalType(), propertyIds);
    }

    public static java.util.Set<java.lang.String> getPropertyIds(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap) {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> entry : componentPropertyInfoMap.entrySet()) {
            propertyIds.addAll(entry.getValue().keySet());
        }
        return propertyIds;
    }

    public static java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        return org.apache.ambari.server.controller.utilities.PropertyHelper.GANGLIA_PROPERTY_IDS.get(resourceType.getInternalType());
    }

    public static java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> getSQLServerPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        return org.apache.ambari.server.controller.utilities.PropertyHelper.SQLSERVER_PROPERTY_IDS.get(resourceType.getInternalType());
    }

    public static java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> getJMXPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        return org.apache.ambari.server.controller.utilities.PropertyHelper.JMX_PROPERTY_IDS.get(resourceType.getInternalType());
    }

    public static java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        return org.apache.ambari.server.controller.utilities.PropertyHelper.KEY_PROPERTY_IDS.get(resourceType.getInternalType());
    }

    public static void setKeyPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type resourceType, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyKeys) {
        org.apache.ambari.server.controller.utilities.PropertyHelper.KEY_PROPERTY_IDS.put(resourceType.getInternalType(), keyPropertyKeys);
    }

    public static java.lang.String getPropertyName(java.lang.String absProperty) {
        int lastPathSep = absProperty.lastIndexOf(org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP);
        return lastPathSep == (-1) ? absProperty : absProperty.substring(lastPathSep + 1);
    }

    public static java.lang.String getPropertyCategory(java.lang.String property) {
        int lastPathSep = -1;
        if (!org.apache.ambari.server.controller.utilities.PropertyHelper.containsArguments(property)) {
            lastPathSep = property.lastIndexOf(org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP);
            return lastPathSep == (-1) ? null : property.substring(0, lastPathSep);
        }
        java.lang.String[] tokens = org.apache.ambari.server.controller.utilities.PropertyHelper.METRIC_CATEGORY_TOKENIZE_REGEX.split(property);
        if ((null == tokens) || (tokens.length == 0)) {
            return null;
        }
        java.lang.StringBuilder categoryBuilder = new java.lang.StringBuilder();
        for (int i = 0; i < (tokens.length - 1); i++) {
            java.lang.String token = tokens[i];
            if (org.apache.ambari.server.controller.utilities.PropertyHelper.containsArguments(token)) {
                int methodIndex = token.indexOf('.');
                if (methodIndex != (-1)) {
                    int parensIndex = token.lastIndexOf(')');
                    if (parensIndex < (token.length() - 1)) {
                        java.lang.String temp = token.substring(0, methodIndex);
                        temp += token.substring(parensIndex + 1);
                        token = temp;
                    } else {
                        token = token.substring(0, methodIndex);
                    }
                }
            }
            categoryBuilder.append(token);
            if (i < (tokens.length - 2)) {
                categoryBuilder.append('/');
            }
        }
        java.lang.String category = categoryBuilder.toString();
        return category;
    }

    public static java.util.Set<java.lang.String> getCategories(java.util.Set<java.lang.String> propertyIds) {
        java.util.Set<java.lang.String> categories = new java.util.HashSet<>();
        for (java.lang.String property : propertyIds) {
            java.lang.String category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(property);
            while (category != null) {
                categories.add(category);
                category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(category);
            } 
        }
        return categories;
    }

    public static boolean containsProperty(java.util.Set<java.lang.String> propertyIds, java.lang.String propertyId) {
        if (propertyIds.contains(propertyId)) {
            return true;
        }
        java.lang.String category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(propertyId);
        while (category != null) {
            if (propertyIds.contains(category)) {
                return true;
            }
            category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(category);
        } 
        return false;
    }

    public static boolean containsArguments(java.lang.String propertyId) {
        if (!propertyId.contains("$")) {
            return false;
        }
        java.util.regex.Matcher matcher = org.apache.ambari.server.controller.utilities.PropertyHelper.CHECK_FOR_METRIC_ARGUMENTS_REGEX.matcher(propertyId);
        return matcher.find();
    }

    public static java.util.Set<java.lang.String> getAssociatedPropertyIds(org.apache.ambari.server.controller.spi.Request request) {
        java.util.Set<java.lang.String> ids = request.getPropertyIds();
        if (ids != null) {
            ids = new java.util.HashSet<>(ids);
        } else {
            ids = new java.util.HashSet<>();
        }
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = request.getProperties();
        if (properties != null) {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : properties) {
                ids.addAll(propertyMap.keySet());
            }
        }
        return ids;
    }

    public static java.util.Map<java.lang.String, java.lang.Object> getProperties(org.apache.ambari.server.controller.spi.Resource resource) {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> categories = resource.getPropertiesMap();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> categoryEntry : categories.entrySet()) {
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> propertyEntry : categoryEntry.getValue().entrySet()) {
                properties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(categoryEntry.getKey(), propertyEntry.getKey()), propertyEntry.getValue());
            }
        }
        return properties;
    }

    public static org.apache.ambari.server.controller.spi.Request getCreateRequest(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties, java.util.Map<java.lang.String, java.lang.String> requestInfoProperties) {
        return new org.apache.ambari.server.controller.internal.RequestImpl(null, properties, requestInfoProperties, null);
    }

    public static org.apache.ambari.server.controller.spi.Request getReadRequest(java.util.Set<java.lang.String> propertyIds) {
        return org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds, null);
    }

    public static org.apache.ambari.server.controller.spi.Request getReadRequest(java.util.Set<java.lang.String> propertyIds, java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> mapTemporalInfo) {
        return org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds, null, mapTemporalInfo, null, null);
    }

    public static org.apache.ambari.server.controller.spi.Request getReadRequest(java.util.Set<java.lang.String> propertyIds, java.util.Map<java.lang.String, java.lang.String> requestInfoProperties, java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> mapTemporalInfo, org.apache.ambari.server.controller.spi.PageRequest pageRequest, org.apache.ambari.server.controller.spi.SortRequest sortRequest) {
        return new org.apache.ambari.server.controller.internal.RequestImpl(propertyIds, null, requestInfoProperties, mapTemporalInfo, sortRequest, pageRequest);
    }

    public static org.apache.ambari.server.controller.spi.Request getReadRequest(java.lang.String... propertyIds) {
        return org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<>(java.util.Arrays.asList(propertyIds)));
    }

    public static org.apache.ambari.server.controller.spi.Request getUpdateRequest(java.util.Map<java.lang.String, java.lang.Object> properties, java.util.Map<java.lang.String, java.lang.String> requestInfoProperties) {
        return new org.apache.ambari.server.controller.internal.RequestImpl(null, java.util.Collections.singleton(properties), requestInfoProperties, null);
    }

    private static java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>>> readPropertyProviderIds(java.lang.String filename) {
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        try {
            java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.utilities.PropertyHelper.Metric>>> resourceMetricMap = mapper.readValue(java.lang.ClassLoader.getSystemResourceAsStream(filename), new org.codehaus.jackson.type.TypeReference<java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.utilities.PropertyHelper.Metric>>>>() {});
            java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>>> resourceMetrics = new java.util.HashMap<>();
            for (java.util.Map.Entry<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.utilities.PropertyHelper.Metric>>> resourceEntry : resourceMetricMap.entrySet()) {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics = new java.util.HashMap<>();
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.utilities.PropertyHelper.Metric>> componentEntry : resourceEntry.getValue().entrySet()) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metrics = new java.util.HashMap<>();
                    for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.utilities.PropertyHelper.Metric> metricEntry : componentEntry.getValue().entrySet()) {
                        java.lang.String property = metricEntry.getKey();
                        org.apache.ambari.server.controller.utilities.PropertyHelper.Metric metric = metricEntry.getValue();
                        org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = new org.apache.ambari.server.controller.internal.PropertyInfo(metric.getMetric(), metric.isTemporal(), metric.isPointInTime());
                        propertyInfo.setAmsId(metric.getAmsId());
                        propertyInfo.setAmsHostMetric(metric.isAmsHostMetric());
                        propertyInfo.setUnit(metric.getUnit());
                        metrics.put(property, propertyInfo);
                    }
                    componentMetrics.put(componentEntry.getKey(), metrics);
                }
                if (org.apache.ambari.server.controller.utilities.PropertyHelper.REPORT_METRIC_RESOURCES.contains(resourceEntry.getKey())) {
                    org.apache.ambari.server.controller.utilities.PropertyHelper.updateMetricsWithAggregateFunctionSupport(componentMetrics);
                }
                resourceMetrics.put(resourceEntry.getKey(), componentMetrics);
            }
            return resourceMetrics;
        } catch (java.io.IOException e) {
            throw new java.lang.IllegalStateException("Can't read properties file " + filename, e);
        }
    }

    private static java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Set<java.lang.String>> readPropertyIds(java.lang.String filename) {
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        try {
            return mapper.readValue(java.lang.ClassLoader.getSystemResourceAsStream(filename), new org.codehaus.jackson.type.TypeReference<java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Set<java.lang.String>>>() {});
        } catch (java.io.IOException e) {
            throw new java.lang.IllegalStateException("Can't read properties file " + filename, e);
        }
    }

    private static java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>> readKeyPropertyIds(java.lang.String filename) {
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        try {
            java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.lang.String>> map = mapper.readValue(java.lang.ClassLoader.getSystemResourceAsStream(filename), new org.codehaus.jackson.type.TypeReference<java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.lang.String>>>() {});
            java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>> returnMap = new java.util.HashMap<>();
            for (java.util.Map.Entry<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.lang.String>> entry : map.entrySet()) {
                java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> innerMap = new java.util.HashMap<>();
                for (java.util.Map.Entry<org.apache.ambari.server.controller.spi.Resource.InternalType, java.lang.String> entry1 : entry.getValue().entrySet()) {
                    innerMap.put(org.apache.ambari.server.controller.spi.Resource.Type.valueOf(entry1.getKey().name()), entry1.getValue());
                }
                returnMap.put(entry.getKey(), innerMap);
            }
            return returnMap;
        } catch (java.io.IOException e) {
            throw new java.lang.IllegalStateException("Can't read properties file " + filename, e);
        }
    }

    protected static class Metric {
        private java.lang.String metric;

        private boolean pointInTime;

        private boolean temporal;

        private java.lang.String amsId;

        private boolean amsHostMetric;

        private java.lang.String unit = "unitless";

        private Metric() {
        }

        protected Metric(java.lang.String metric, boolean pointInTime, boolean temporal, java.lang.String unit) {
            this.metric = metric;
            this.pointInTime = pointInTime;
            this.temporal = temporal;
            this.unit = unit;
        }

        public java.lang.String getMetric() {
            return metric;
        }

        public void setMetric(java.lang.String metric) {
            this.metric = metric;
        }

        public boolean isPointInTime() {
            return pointInTime;
        }

        public void setPointInTime(boolean pointInTime) {
            this.pointInTime = pointInTime;
        }

        public boolean isTemporal() {
            return temporal;
        }

        public void setTemporal(boolean temporal) {
            this.temporal = temporal;
        }

        public java.lang.String getAmsId() {
            return amsId;
        }

        public void setAmsId(java.lang.String amsId) {
            this.amsId = amsId;
        }

        public boolean isAmsHostMetric() {
            return amsHostMetric;
        }

        public void setAmsHostMetric(boolean amsHostMetric) {
            this.amsHostMetric = amsHostMetric;
        }

        public void setUnit(java.lang.String unit) {
            this.unit = unit;
        }

        public java.lang.String getUnit() {
            return unit;
        }
    }

    public static void updateMetricsWithAggregateFunctionSupport(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> metrics) {
        if ((metrics == null) || metrics.isEmpty()) {
            return;
        }
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> metricInfoEntry : metrics.entrySet()) {
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metricInfo = metricInfoEntry.getValue();
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> aggregateMetrics = new java.util.HashMap<>();
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metricEntry : metricInfo.entrySet()) {
                for (java.lang.String identifierToAdd : org.apache.ambari.server.controller.utilities.PropertyHelper.AGGREGATE_FUNCTION_IDENTIFIERS) {
                    java.lang.String metricInfoKey = metricEntry.getKey() + identifierToAdd;
                    if (metricInfo.containsKey(metricInfoKey)) {
                        continue;
                    }
                    org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = metricEntry.getValue();
                    org.apache.ambari.server.controller.internal.PropertyInfo metricInfoValue = new org.apache.ambari.server.controller.internal.PropertyInfo(propertyInfo.getPropertyId() + identifierToAdd, propertyInfo.isTemporal(), propertyInfo.isPointInTime());
                    metricInfoValue.setAmsHostMetric(propertyInfo.isAmsHostMetric());
                    metricInfoValue.setAmsId(!org.apache.commons.lang.StringUtils.isEmpty(propertyInfo.getAmsId()) ? propertyInfo.getAmsId() + identifierToAdd : null);
                    metricInfoValue.setUnit(propertyInfo.getUnit());
                    aggregateMetrics.put(metricInfoKey, metricInfoValue);
                }
            }
            metricInfo.putAll(aggregateMetrics);
        }
    }

    public static boolean hasAggregateFunctionSuffix(java.lang.String propertyId) {
        for (java.lang.String aggregateFunctionId : org.apache.ambari.server.controller.utilities.PropertyHelper.AGGREGATE_FUNCTION_IDENTIFIERS) {
            if (propertyId.endsWith(aggregateFunctionId)) {
                return true;
            }
        }
        return false;
    }

    public static java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> processRpcMetricDefinition(java.lang.String metricType, java.lang.String componentName, java.lang.String propertyId, org.apache.ambari.server.state.stack.Metric metric) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> replacementMap = null;
        if (componentName.equalsIgnoreCase("NAMENODE")) {
            for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> entry : org.apache.ambari.server.controller.utilities.PropertyHelper.RPC_METRIC_SUFFIXES.entrySet()) {
                java.lang.String prefix = entry.getKey();
                if (propertyId.startsWith(prefix)) {
                    replacementMap = new java.util.HashMap<>();
                    for (java.lang.String suffix : entry.getValue()) {
                        java.lang.String newMetricName;
                        if ("jmx".equals(metricType)) {
                            newMetricName = org.apache.ambari.server.controller.utilities.PropertyHelper.insertTagIntoCategoty(suffix, metric.getName());
                        } else {
                            newMetricName = org.apache.ambari.server.controller.utilities.PropertyHelper.insertTagInToMetricName(suffix, metric.getName(), prefix);
                        }
                        org.apache.ambari.server.state.stack.Metric newMetric = new org.apache.ambari.server.state.stack.Metric(newMetricName, metric.isPointInTime(), metric.isTemporal(), metric.isAmsHostMetric(), metric.getUnit());
                        replacementMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.insertTagInToMetricName(suffix, propertyId, prefix), newMetric);
                    }
                }
            }
        }
        return replacementMap;
    }

    static java.lang.String insertTagInToMetricName(java.lang.String tag, java.lang.String metricName, java.lang.String prefix) {
        java.lang.String sepExpr = "\\.";
        java.lang.String seperator = ".";
        if (metricName.indexOf(org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) != (-1)) {
            sepExpr = java.lang.Character.toString(org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP);
            seperator = sepExpr;
        }
        java.lang.String prefixSep = (prefix.contains(".")) ? "\\." : "" + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP;
        if (prefix.substring(prefix.length() - 1).equals(prefixSep)) {
            prefix = prefix.substring(0, prefix.length() - 1);
        }
        int pos = prefix.split(prefixSep).length - 1;
        java.lang.String[] parts = metricName.split(sepExpr);
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i < (parts.length - 1)) {
                sb.append(seperator);
            }
            if (i == pos) {
                sb.append(tag);
                sb.append(seperator);
            }
        }
        return sb.toString();
    }

    static java.lang.String insertTagIntoCategoty(java.lang.String tag, java.lang.String metricName) {
        int pos = metricName.lastIndexOf('.');
        return ((metricName.substring(0, pos) + ",tag=") + tag) + metricName.substring(pos);
    }
}