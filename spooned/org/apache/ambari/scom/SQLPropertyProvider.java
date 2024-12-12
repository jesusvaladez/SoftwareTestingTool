package org.apache.ambari.scom;
import org.apache.commons.lang.StringUtils;
public class SQLPropertyProvider extends org.apache.ambari.server.controller.internal.AbstractPropertyProvider {
    private final org.apache.ambari.scom.HostInfoProvider hostProvider;

    private final java.lang.String clusterNamePropertyId;

    private final java.lang.String hostNamePropertyId;

    private final java.lang.String componentNamePropertyId;

    private final java.lang.String serviceNamePropertyId;

    private final org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory;

    private static final java.lang.String GET_METRICS_STATEMENT = "SELECT  s.RecordTypeContext, s.RecordTypeName, s.TagPairs, s.NodeName, s.ServiceName, mn.Name AS MetricName, s.RecordTimeStamp, mp.MetricValue\n" + ((((((((((((((((("FROM HadoopMetrics.dbo.MetricPair mp\n" + "     INNER JOIN (\n") + "         SELECT mr.RecordID AS RecordID, mr.RecordTimeStamp AS RecordTimeStamp, rt.Context AS RecordTypeContext, rt.Name AS RecordTypeName, ts.TagPairs AS TagPairs, nd.Name AS NodeName, sr.Name AS ServiceName\n") + "         FROM HadoopMetrics.dbo.MetricRecord mr\n") + "              INNER JOIN HadoopMetrics.dbo.RecordType rt ON (mr.RecordTypeId = rt.RecordTypeId)\n") + "              INNER JOIN HadoopMetrics.dbo.TagSet ts ON (mr.TagSetID = ts.TagSetID)\n") + "              INNER JOIN HadoopMetrics.dbo.Node nd ON (mr.NodeID = nd.NodeID)\n") + "              INNER JOIN HadoopMetrics.dbo.Service sr ON (mr.ServiceID = sr.ServiceID)\n") + "         WHERE rt.Context in (%s)\n") + "               AND rt.Name in (%s)\n") + "               AND (ts.TagPairs LIKE %s)\n") + "               AND (nd.Name in (%s))\n") + "               AND (sr.Name in (%s))\n") + "               AND mr.RecordTimestamp >= %d\n") + "               AND mr.RecordTimestamp <= %d\n") + "     ) s ON (mp.RecordID = s.RecordID)\n") + "     INNER JOIN HadoopMetrics.dbo.MetricName mn ON (mp.MetricID = mn.MetricID)\n") + "WHERE (mn.Name in (%s))");

    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.scom.SQLPropertyProvider.class);

    public SQLPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.scom.HostInfoProvider hostProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId, java.lang.String componentNamePropertyId, java.lang.String serviceNamePropertyId, org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory) {
        super(componentPropertyInfoMap);
        this.hostProvider = hostProvider;
        this.clusterNamePropertyId = clusterNamePropertyId;
        this.hostNamePropertyId = hostNamePropertyId;
        this.componentNamePropertyId = componentNamePropertyId;
        this.serviceNamePropertyId = serviceNamePropertyId;
        this.connectionFactory = connectionFactory;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> keepers = new java.util.HashSet<org.apache.ambari.server.controller.spi.Resource>();
        try {
            java.sql.Connection connection = connectionFactory.getConnection();
            try {
                java.sql.Statement statement = connection.createStatement();
                try {
                    for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
                        if (populateResource(resource, request, predicate, statement)) {
                            keepers.add(resource);
                        }
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (java.sql.SQLException e) {
            if (org.apache.ambari.scom.SQLPropertyProvider.LOG.isErrorEnabled()) {
                org.apache.ambari.scom.SQLPropertyProvider.LOG.error("Error during populateResources call.");
                org.apache.ambari.scom.SQLPropertyProvider.LOG.debug("Error during populateResources call : caught exception", e);
            }
        }
        return keepers;
    }

    private boolean populateResource(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate, java.sql.Statement statement) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<java.lang.String> ids = getRequestPropertyIds(request, predicate);
        if (ids.isEmpty()) {
            return true;
        }
        java.lang.String componentName = ((java.lang.String) (resource.getPropertyValue(componentNamePropertyId)));
        java.lang.String serviceName = ((java.lang.String) (resource.getPropertyValue(serviceNamePropertyId)));
        if (getComponentMetrics().get(componentName) == null) {
            return true;
        }
        java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(clusterNamePropertyId)));
        java.lang.String hostName = getHost(resource, clusterName, componentName);
        if (hostName == null) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Unable to get metrics.  No host name for " + componentName, null);
        }
        java.util.Set<org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition> metricsDefinitionSet = new java.util.HashSet<org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition>();
        for (java.lang.String id : ids) {
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> propertyInfoMap = getPropertyInfoMap(componentName, id);
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> entry : propertyInfoMap.entrySet()) {
                java.lang.String propertyKey = entry.getKey();
                org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = entry.getValue();
                if (containsArguments(propertyKey)) {
                    propertyInfo = updatePropertyInfo(propertyKey, id, propertyInfo);
                }
                java.lang.String propertyId = propertyInfo.getPropertyId();
                org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = request.getTemporalInfo(id);
                if ((propertyInfo.isPointInTime() && (temporalInfo == null)) || (propertyInfo.isTemporal() && (temporalInfo != null))) {
                    long startTime;
                    long endTime;
                    if (temporalInfo != null) {
                        java.lang.Long endTimeSeconds = temporalInfo.getEndTime();
                        endTime = (endTimeSeconds != (-1)) ? endTimeSeconds * 1000 : java.lang.Long.MAX_VALUE;
                        startTime = temporalInfo.getStartTime() * 1000;
                    } else {
                        startTime = 0L;
                        endTime = java.lang.Long.MAX_VALUE;
                    }
                    java.lang.String category = "";
                    java.lang.String recordTypeContext = "";
                    java.lang.String recordTypeName = "";
                    java.lang.String metricName = "";
                    java.lang.String tagPairsPattern = "";
                    int dotIndex = propertyId.lastIndexOf('.');
                    if (dotIndex != (-1)) {
                        category = propertyId.substring(0, dotIndex);
                        metricName = propertyId.substring(dotIndex + 1);
                    }
                    java.lang.String[] parts = category.split("\\.");
                    if (parts.length >= 2) {
                        recordTypeContext = parts[0];
                        recordTypeName = parts[1];
                        if (containsArguments(propertyKey) && (parts.length > 2)) {
                            tagPairsPattern = org.apache.commons.lang.StringUtils.join(java.util.Arrays.copyOfRange(parts, 2, parts.length), ".");
                        }
                        metricsDefinitionSet.add(new org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition(startTime, endTime, recordTypeContext, recordTypeName, tagPairsPattern, metricName, (serviceName != null) && serviceName.toLowerCase().equals("hbase") ? serviceName.toLowerCase() : componentName.toLowerCase(), hostName, propertyKey, id, temporalInfo));
                    } else if (org.apache.ambari.scom.SQLPropertyProvider.LOG.isWarnEnabled()) {
                        org.apache.ambari.scom.SQLPropertyProvider.LOG.warn((("Can't get metrics for " + id) + " : ") + propertyId);
                    }
                }
            }
        }
        java.util.Map<org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition, java.util.List<org.apache.ambari.scom.SQLPropertyProvider.DataPoint>> results = getMetric(metricsDefinitionSet, statement);
        for (org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition metricDefinition : metricsDefinitionSet) {
            java.util.List<org.apache.ambari.scom.SQLPropertyProvider.DataPoint> dataPoints = (results.containsKey(metricDefinition)) ? results.get(metricDefinition) : new java.util.ArrayList<org.apache.ambari.scom.SQLPropertyProvider.DataPoint>();
            org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = metricDefinition.getTemporalInfo();
            java.lang.String propertyKey = metricDefinition.getPropertyKey();
            java.lang.String requestedPropertyKey = metricDefinition.getRequestedPropertyKey();
            if (dataPoints != null) {
                if (temporalInfo == null) {
                    int length = dataPoints.size();
                    java.io.Serializable value = (length > 0) ? dataPoints.get(length - 1).getValue() : 0;
                    resource.setProperty(propertyKey, value);
                } else {
                    java.lang.Number[][] dp = new java.lang.Number[dataPoints.size()][2];
                    for (int i = 0; i < dp.length; i++) {
                        dp[i][0] = dataPoints.get(i).getValue();
                        dp[i][1] = dataPoints.get(i).getTimestamp() / 1000;
                    }
                    if (containsArguments(propertyKey)) {
                        resource.setProperty(requestedPropertyKey, dp);
                    } else {
                        resource.setProperty(propertyKey, dp);
                    }
                }
            }
        }
        return true;
    }

    private java.util.Map<org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition, java.util.List<org.apache.ambari.scom.SQLPropertyProvider.DataPoint>> getMetric(java.util.Set<org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition> metricDefinitionSet, java.sql.Statement statement) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Map<org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition, java.util.List<org.apache.ambari.scom.SQLPropertyProvider.DataPoint>> results = new java.util.HashMap<org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition, java.util.List<org.apache.ambari.scom.SQLPropertyProvider.DataPoint>>();
        try {
            java.lang.StringBuilder query = new java.lang.StringBuilder();
            java.util.Set<java.lang.String> recordTypeContexts = new java.util.HashSet<java.lang.String>();
            java.util.Set<java.lang.String> recordTypeNamess = new java.util.HashSet<java.lang.String>();
            java.util.Set<java.lang.String> tagPairsPatterns = new java.util.HashSet<java.lang.String>();
            java.util.Set<java.lang.String> nodeNames = new java.util.HashSet<java.lang.String>();
            java.util.Set<java.lang.String> serviceNames = new java.util.HashSet<java.lang.String>();
            java.util.Set<java.lang.String> metricNames = new java.util.HashSet<java.lang.String>();
            long startTime = 0;
            long endTime = 0;
            for (org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition metricDefinition : metricDefinitionSet) {
                if (((metricDefinition.getRecordTypeContext() == null) || (metricDefinition.getRecordTypeName() == null)) || (metricDefinition.getNodeName() == null)) {
                    continue;
                }
                recordTypeContexts.add(metricDefinition.getRecordTypeContext());
                recordTypeNamess.add(metricDefinition.getRecordTypeName());
                tagPairsPatterns.add(metricDefinition.getTagPairsPattern());
                nodeNames.add(metricDefinition.getNodeName());
                serviceNames.add(metricDefinition.getServiceName());
                metricNames.add(metricDefinition.getMetricName());
                startTime = metricDefinition.getStartTime();
                endTime = metricDefinition.getEndTime();
            }
            for (java.lang.String tagPairsPattern : tagPairsPatterns) {
                if (query.length() != 0) {
                    query.append("\nUNION\n");
                }
                query.append(java.lang.String.format(org.apache.ambari.scom.SQLPropertyProvider.GET_METRICS_STATEMENT, ("'" + org.apache.commons.lang.StringUtils.join(recordTypeContexts, "','")) + "'", ("'" + org.apache.commons.lang.StringUtils.join(recordTypeNamess, "','")) + "'", ("'%" + tagPairsPattern) + "%'", ("'" + org.apache.commons.lang.StringUtils.join(nodeNames, "','")) + "'", ("'" + org.apache.commons.lang.StringUtils.join(serviceNames, "','")) + "'", startTime, endTime, ("'" + org.apache.commons.lang.StringUtils.join(metricNames, "','")) + "'"));
            }
            java.sql.ResultSet rs = null;
            if (query.length() != 0) {
                rs = statement.executeQuery(query.toString());
            }
            if (rs != null) {
                while (rs.next()) {
                    org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition metricDefinition = new org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition(rs.getString("RecordTypeContext"), rs.getString("RecordTypeName"), rs.getString("TagPairs"), rs.getString("MetricName"), rs.getString("ServiceName"), rs.getString("NodeName"));
                    java.text.ParsePosition parsePosition = new java.text.ParsePosition(0);
                    java.text.NumberFormat numberFormat = java.text.NumberFormat.getInstance();
                    java.lang.Number parsedNumber = numberFormat.parse(rs.getNString("MetricValue"), parsePosition);
                    if (results.containsKey(metricDefinition)) {
                        results.get(metricDefinition).add(new org.apache.ambari.scom.SQLPropertyProvider.DataPoint(rs.getLong("RecordTimeStamp"), parsedNumber));
                    } else {
                        java.util.List<org.apache.ambari.scom.SQLPropertyProvider.DataPoint> dataPoints = new java.util.ArrayList<org.apache.ambari.scom.SQLPropertyProvider.DataPoint>();
                        dataPoints.add(new org.apache.ambari.scom.SQLPropertyProvider.DataPoint(rs.getLong("RecordTimeStamp"), parsedNumber));
                        results.put(metricDefinition, dataPoints);
                    }
                } 
            }
        } catch (java.sql.SQLException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Error during getMetric call : caught exception - ", e);
        }
        return results;
    }

    private java.lang.String getHost(org.apache.ambari.server.controller.spi.Resource resource, java.lang.String clusterName, java.lang.String componentName) throws org.apache.ambari.server.controller.spi.SystemException {
        return hostNamePropertyId == null ? hostProvider.getHostName(clusterName, componentName) : hostProvider.getHostName(((java.lang.String) (resource.getPropertyValue(hostNamePropertyId))));
    }

    private static class DataPoint {
        private final long timestamp;

        private final java.lang.Number value;

        private DataPoint(long timestamp, java.lang.Number value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public java.lang.Number getValue() {
            return value;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("{" + value) + " : ") + timestamp) + "}";
        }
    }

    private class MetricDefinition {
        long startTime;

        long endTime;

        java.lang.String recordTypeContext;

        java.lang.String recordTypeName;

        java.lang.String tagPairsPattern;

        java.lang.String metricName;

        java.lang.String serviceName;

        java.lang.String nodeName;

        java.lang.String propertyKey;

        java.lang.String requestedPropertyKey;

        org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo;

        private MetricDefinition(long startTime, long endTime, java.lang.String recordTypeContext, java.lang.String recordTypeName, java.lang.String tagPairsPattern, java.lang.String metricName, java.lang.String serviceName, java.lang.String nodeName, java.lang.String propertyKey, java.lang.String requestedPropertyKey, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.recordTypeContext = recordTypeContext;
            this.recordTypeName = recordTypeName;
            this.tagPairsPattern = tagPairsPattern;
            this.metricName = metricName;
            this.serviceName = serviceName;
            this.nodeName = nodeName;
            this.propertyKey = propertyKey;
            this.requestedPropertyKey = requestedPropertyKey;
            this.temporalInfo = temporalInfo;
        }

        private MetricDefinition(java.lang.String recordTypeContext, java.lang.String recordTypeName, java.lang.String tagPairsPattern, java.lang.String metricName, java.lang.String serviceName, java.lang.String nodeName) {
            this.recordTypeContext = recordTypeContext;
            this.recordTypeName = recordTypeName;
            this.tagPairsPattern = tagPairsPattern;
            this.metricName = metricName;
            this.serviceName = serviceName;
            this.nodeName = nodeName;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public java.lang.String getRecordTypeContext() {
            return recordTypeContext;
        }

        public void setRecordTypeContext(java.lang.String recordTypeContext) {
            this.recordTypeContext = recordTypeContext;
        }

        public java.lang.String getRecordTypeName() {
            return recordTypeName;
        }

        public void setRecordTypeName(java.lang.String recordTypeName) {
            this.recordTypeName = recordTypeName;
        }

        public java.lang.String getTagPairsPattern() {
            return tagPairsPattern;
        }

        public void getTagPairsPattern(java.lang.String tagPairsPattern) {
            this.tagPairsPattern = tagPairsPattern;
        }

        public java.lang.String getMetricName() {
            return metricName;
        }

        public void setMetricName(java.lang.String metricName) {
            this.metricName = metricName;
        }

        public java.lang.String getServiceName() {
            return serviceName;
        }

        public void setServiceName(java.lang.String serviceName) {
            this.serviceName = serviceName;
        }

        public java.lang.String getNodeName() {
            return nodeName;
        }

        public void setNodeName(java.lang.String nodeName) {
            this.nodeName = nodeName;
        }

        public java.lang.String getPropertyKey() {
            return propertyKey;
        }

        public void setPropertyKey(java.lang.String propertyKey) {
            this.propertyKey = propertyKey;
        }

        public java.lang.String getRequestedPropertyKey() {
            return requestedPropertyKey;
        }

        public void setRequestedPropertyKey(java.lang.String requestedPropertyKey) {
            this.requestedPropertyKey = requestedPropertyKey;
        }

        public org.apache.ambari.server.controller.spi.TemporalInfo getTemporalInfo() {
            return temporalInfo;
        }

        public void setTemporalInfo(org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
            this.temporalInfo = temporalInfo;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition that = ((org.apache.ambari.scom.SQLPropertyProvider.MetricDefinition) (o));
            if (metricName != null ? !metricName.equals(that.metricName) : that.metricName != null)
                return false;

            if (nodeName != null ? !nodeName.equalsIgnoreCase(that.nodeName) : that.nodeName != null)
                return false;

            if (recordTypeContext != null ? !recordTypeContext.equals(that.recordTypeContext) : that.recordTypeContext != null)
                return false;

            if (recordTypeName != null ? !recordTypeName.equals(that.recordTypeName) : that.recordTypeName != null)
                return false;

            if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null)
                return false;

            if (tagPairsPattern != null ? !(tagPairsPattern.contains(that.tagPairsPattern) || that.tagPairsPattern.contains(tagPairsPattern)) : that.tagPairsPattern != null)
                return false;

            return true;
        }

        @java.lang.Override
        public int hashCode() {
            int result = (recordTypeContext != null) ? recordTypeContext.hashCode() : 0;
            result = (31 * result) + (recordTypeName != null ? recordTypeName.hashCode() : 0);
            result = (31 * result) + (metricName != null ? metricName.hashCode() : 0);
            result = (31 * result) + (serviceName != null ? serviceName.hashCode() : 0);
            result = (31 * result) + (nodeName != null ? nodeName.toLowerCase().hashCode() : 0);
            return result;
        }
    }
}