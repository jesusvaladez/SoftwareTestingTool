package org.apache.ambari.server.controller.internal;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetric;
public abstract class AbstractPropertyProvider extends org.apache.ambari.server.controller.internal.BaseProvider implements org.apache.ambari.server.controller.spi.PropertyProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.AbstractPropertyProvider.class);

    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics;

    private static final java.util.regex.Pattern CHECK_FOR_METRIC_ARGUMENT_METHODS_REGEX = java.util.regex.Pattern.compile("(\\$\\d\\.[^\\$]+\\))+");

    private static final java.util.regex.Pattern FIND_ARGUMENT_METHOD_REGEX = java.util.regex.Pattern.compile(".\\w+\\(.*?\\)");

    private static final java.util.regex.Pattern FIND_ARGUMENT_METHOD_ARGUMENTS_REGEX = java.util.regex.Pattern.compile("\".*?\"|[0-9]+");

    private static final java.lang.String FIND_REGEX_IN_METRIC_REGEX = "\\([^)]+\\)";

    private static final java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("#.00");

    public AbstractPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics) {
        super(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(componentMetrics));
        this.componentMetrics = componentMetrics;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> getComponentMetrics() {
        return componentMetrics;
    }

    protected java.lang.String getResourceTypeFromResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources) {
        java.lang.String resType = null;
        if (resources != null) {
            java.util.Iterator<org.apache.ambari.server.controller.spi.Resource> itr = resources.iterator();
            if (itr.hasNext()) {
                org.apache.ambari.server.controller.spi.Resource res = itr.next();
                if (res != null) {
                    resType = res.getType().toString();
                }
            }
        }
        return resType;
    }

    protected java.util.Set<java.lang.String> getClustersNameFromResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, java.lang.String clusterNamePropertyId) {
        java.util.Set<java.lang.String> clusNames = new java.util.HashSet<>();
        if (resources != null) {
            java.util.Iterator<org.apache.ambari.server.controller.spi.Resource> itr = resources.iterator();
            while (itr.hasNext()) {
                org.apache.ambari.server.controller.spi.Resource res = itr.next();
                if (res != null) {
                    clusNames.add(((java.lang.String) (res.getPropertyValue(clusterNamePropertyId))));
                }
            } 
        }
        return clusNames;
    }

    protected java.util.Set<java.lang.Long> getClustersResourceId(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, java.lang.String clusterNamePropertyId) {
        java.util.Set<java.lang.Long> clusterResId = new java.util.HashSet<>();
        if (clusterNamePropertyId != null) {
            try {
                org.apache.ambari.server.controller.AmbariManagementController amc = org.apache.ambari.server.controller.AmbariServer.getController();
                java.util.Set<java.lang.String> clusterNames = getClustersNameFromResources(resources, clusterNamePropertyId);
                java.util.Iterator<java.lang.String> clusNameItr = clusterNames.iterator();
                while (clusNameItr.hasNext()) {
                    clusterResId.add(amc.getClusters().getCluster(clusNameItr.next()).getResourceId());
                } 
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.controller.internal.AbstractPropertyProvider.LOG.error("Cluster Id couldn't be retrieved.");
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.controller.internal.AbstractPropertyProvider.LOG.error("Cluster Id couldn't be retrieved");
            }
        }
        if (org.apache.ambari.server.controller.internal.AbstractPropertyProvider.LOG.isDebugEnabled()) {
            org.apache.ambari.server.controller.internal.AbstractPropertyProvider.LOG.debug("Retrieved Cluster Ids = {}", clusterResId);
        }
        return clusterResId;
    }

    protected boolean checkAuthorizationForMetrics(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, java.lang.String clusterNamePropertyId) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String resType = null;
        resType = getResourceTypeFromResources(resources);
        if (resType == null) {
            return false;
        }
        java.util.Set<java.lang.Long> clusterResIds = getClustersResourceId(resources, clusterNamePropertyId);
        if (clusterResIds.size() == 0) {
            return false;
        }
        if (org.apache.ambari.server.controller.internal.AbstractPropertyProvider.LOG.isDebugEnabled()) {
            org.apache.ambari.server.controller.internal.AbstractPropertyProvider.LOG.debug("Retrieved cluster's Resource Id = {}, Resource Type = {}", clusterResIds, resType);
        }
        java.util.Iterator<java.lang.Long> clusResIdsItr = clusterResIds.iterator();
        while (clusResIdsItr.hasNext()) {
            java.lang.Long clusResId = clusResIdsItr.next();
            org.apache.ambari.server.controller.spi.Resource.InternalType resTypeVal = org.apache.ambari.server.controller.spi.Resource.InternalType.valueOf(resType);
            switch (resTypeVal) {
                case Cluster :
                    if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, clusResId, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS))) {
                        throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization to view cluster metrics");
                    }
                    break;
                case Host :
                    if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, clusResId, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_METRICS))) {
                        throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization to view Host metrics");
                    }
                    break;
                case Component :
                    if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, clusResId, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_METRICS))) {
                        throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization to view Service metrics");
                    }
                    break;
                case HostComponent :
                    if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, clusResId, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_METRICS))) {
                        throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization to view Service metrics");
                    }
                    break;
                default :
                    org.apache.ambari.server.controller.internal.AbstractPropertyProvider.LOG.error("Unsuported Resource Type for Metrics");
                    return false;
            }
        } 
        return true;
    }

    protected java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> getPropertyInfoMap(java.lang.String componentName, java.lang.String propertyId) {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> propertyInfoMap = new java.util.HashMap<>();
        updatePropertyInfoMap(componentName, propertyId, propertyInfoMap);
        return propertyInfoMap;
    }

    protected void updatePropertyInfoMap(java.lang.String componentName, java.lang.String propertyId, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> propertyInfoMap) {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> componentMetricMap = getComponentMetrics().get(componentName);
        propertyInfoMap.clear();
        if (componentMetricMap == null) {
            return;
        }
        org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = componentMetricMap.get(propertyId);
        if (propertyInfo != null) {
            propertyInfoMap.put(propertyId, propertyInfo);
            return;
        }
        java.util.Map.Entry<java.lang.String, java.util.regex.Pattern> regexEntry = getRegexEntry(propertyId);
        if (regexEntry != null) {
            java.lang.String regexKey = regexEntry.getKey();
            propertyInfo = componentMetricMap.get(regexKey);
            if (propertyInfo != null) {
                propertyInfoMap.put(regexKey, propertyInfo);
                return;
            }
        }
        if (!propertyId.endsWith("/")) {
            propertyId += "/";
        }
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> entry : componentMetricMap.entrySet()) {
            if (entry.getKey().startsWith(propertyId)) {
                java.lang.String key = entry.getKey();
                propertyInfoMap.put(key, entry.getValue());
            }
        }
        if (regexEntry != null) {
            java.lang.String regexPattern = regexEntry.getValue().pattern();
            regexPattern += "(\\S*)";
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> entry : componentMetricMap.entrySet()) {
                if (entry.getKey().matches(regexPattern)) {
                    propertyInfoMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return;
    }

    protected static java.lang.String substituteArgument(java.lang.String propertyId, java.lang.String argName, java.lang.String val) {
        int argStart = propertyId.indexOf(argName);
        java.lang.String value = (val == null) ? "" : val;
        if (argStart > (-1)) {
            java.lang.String argSegment = propertyId.substring(argStart);
            java.util.regex.Matcher matcher = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.CHECK_FOR_METRIC_ARGUMENT_METHODS_REGEX.matcher(argSegment);
            if (matcher.find()) {
                argName = argSegment.substring(matcher.start(), matcher.end());
                matcher = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.FIND_ARGUMENT_METHOD_REGEX.matcher(argName);
                while (matcher.find()) {
                    int openParenIndex = argName.indexOf('(', matcher.start());
                    int closeParenIndex = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.indexOfClosingParenthesis(argName, openParenIndex);
                    java.lang.String methodName = argName.substring(matcher.start() + 1, openParenIndex);
                    java.lang.String args = argName.substring(openParenIndex + 1, closeParenIndex);
                    java.util.List<java.lang.Object> argList = new java.util.LinkedList<>();
                    java.util.List<java.lang.Class<?>> paramTypes = new java.util.LinkedList<>();
                    java.util.regex.Matcher argMatcher = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.FIND_ARGUMENT_METHOD_ARGUMENTS_REGEX.matcher(args);
                    while (argMatcher.find()) {
                        org.apache.ambari.server.controller.internal.AbstractPropertyProvider.addArgument(args, argMatcher.start(), argMatcher.end(), argList, paramTypes);
                    } 
                    try {
                        value = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.invokeArgumentMethod(value, methodName, argList, paramTypes);
                    } catch (java.lang.Exception e) {
                        throw new java.lang.IllegalArgumentException((((("Can't apply method " + methodName) + " for argument ") + argName) + " in ") + propertyId, e);
                    }
                } 
                if (value.equals(val)) {
                    return propertyId;
                }
            }
            return propertyId.replace(argName, value);
        }
        throw new java.lang.IllegalArgumentException((((("Can't substitute " + val) + "  for argument ") + argName) + " in ") + propertyId);
    }

    private static int indexOfClosingParenthesis(java.lang.String s, int index) {
        int depth = 0;
        int length = s.length();
        while (index < length) {
            char c = s.charAt(index++);
            if (c == '(') {
                ++depth;
            } else if (c == ')') {
                if ((--depth) == 0) {
                    return index;
                }
            }
        } 
        return -1;
    }

    private static void addArgument(java.lang.String args, int start, int end, java.util.List<java.lang.Object> argList, java.util.List<java.lang.Class<?>> paramTypes) {
        java.lang.String arg = args.substring(start, end);
        if (arg.contains("\"")) {
            argList.add(arg.substring(1, arg.length() - 1));
            paramTypes.add(java.lang.String.class);
        } else {
            java.lang.Integer number = java.lang.Integer.parseInt(arg);
            argList.add(number);
            paramTypes.add(java.lang.Integer.TYPE);
        }
    }

    private static java.lang.String invokeArgumentMethod(java.lang.String argValue, java.lang.String methodName, java.util.List<java.lang.Object> argList, java.util.List<java.lang.Class<?>> paramTypes) throws java.lang.NoSuchMethodException, java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException {
        java.lang.reflect.Method method = java.lang.String.class.getMethod(methodName, paramTypes.toArray(new java.lang.Class<?>[paramTypes.size()]));
        return ((java.lang.String) (method.invoke(argValue, argList.toArray(new java.lang.Object[argList.size()]))));
    }

    protected void updateComponentMetricMap(java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> componentMetricMap, java.lang.String propertyId) {
        java.lang.String regexKey = null;
        java.util.Map.Entry<java.lang.String, java.util.regex.Pattern> regexEntry = getRegexEntry(propertyId);
        if (null != regexEntry) {
            regexKey = regexEntry.getKey();
        }
        if (((!componentMetricMap.containsKey(propertyId)) && (regexKey != null)) && (!regexKey.equals(propertyId))) {
            org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = componentMetricMap.get(regexKey);
            if (propertyInfo != null) {
                java.util.List<java.lang.String> regexGroups = getRegexGroups(regexKey, propertyId);
                java.lang.String key = propertyInfo.getPropertyId();
                for (java.lang.String regexGroup : regexGroups) {
                    regexGroup = regexGroup.replace("/", ".");
                    key = key.replaceFirst(org.apache.ambari.server.controller.internal.AbstractPropertyProvider.FIND_REGEX_IN_METRIC_REGEX, regexGroup);
                }
                org.apache.ambari.server.controller.internal.PropertyInfo compPropertyInfo = new org.apache.ambari.server.controller.internal.PropertyInfo(key, propertyInfo.isTemporal(), propertyInfo.isPointInTime());
                compPropertyInfo.setAmsHostMetric(propertyInfo.isAmsHostMetric());
                compPropertyInfo.setAmsId(propertyInfo.getAmsId());
                compPropertyInfo.setUnit(propertyInfo.getUnit());
                componentMetricMap.put(propertyId, compPropertyInfo);
            }
        }
    }

    protected org.apache.ambari.server.controller.internal.PropertyInfo updatePropertyInfo(java.lang.String propertyKey, java.lang.String id, org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo) {
        java.util.List<java.lang.String> regexGroups = getRegexGroups(propertyKey, id);
        java.lang.String propertyId = propertyInfo.getPropertyId();
        if (propertyId != null) {
            for (java.lang.String regexGroup : regexGroups) {
                regexGroup = regexGroup.replace("/", ".");
                propertyId = propertyId.replaceFirst(org.apache.ambari.server.controller.internal.AbstractPropertyProvider.FIND_REGEX_IN_METRIC_REGEX, regexGroup);
            }
        }
        return new org.apache.ambari.server.controller.internal.PropertyInfo(propertyId, propertyInfo.isTemporal(), propertyInfo.isPointInTime());
    }

    protected boolean isSupportedPropertyId(java.lang.String componentName, java.lang.String propertyId) {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> componentMetricMap = componentMetrics.get(componentName);
        return (componentMetricMap != null) && (componentMetricMap.containsKey(propertyId) || checkPropertyCategory(propertyId));
    }

    protected boolean checkPropertyCategory(java.lang.String propertyId) {
        java.util.Set<java.lang.String> categoryIds = getCategoryIds();
        if (categoryIds.contains(propertyId)) {
            return true;
        }
        java.lang.String category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(propertyId);
        while (category != null) {
            if (categoryIds.contains(category)) {
                return true;
            }
            category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(category);
        } 
        return false;
    }

    private static java.lang.Number[][] getGangliaLikeDatapoints(org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
        org.apache.ambari.server.controller.metrics.MetricReportingAdapter rpt = new org.apache.ambari.server.controller.metrics.MetricReportingAdapter(metric);
        return rpt.reportMetricData(metric, temporalInfo);
    }

    protected static java.lang.Object getValue(org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
        java.lang.Number[][] dataPoints = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.getGangliaLikeDatapoints(metric, temporalInfo);
        int length = dataPoints.length;
        if (temporalInfo != null) {
            return length > 0 ? dataPoints : null;
        } else {
            return length > 0 ? dataPoints[length - 1][0] : 0;
        }
    }
}