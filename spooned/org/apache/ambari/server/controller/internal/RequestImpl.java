package org.apache.ambari.server.controller.internal;
public class RequestImpl implements org.apache.ambari.server.controller.spi.Request {
    private final java.util.Set<java.lang.String> propertyIds;

    private final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties;

    private final java.util.Map<java.lang.String, java.lang.String> requestInfoProperties;

    private final java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> m_mapTemporalInfo;

    private final org.apache.ambari.server.controller.spi.PageRequest m_pageRequest;

    private final org.apache.ambari.server.controller.spi.SortRequest m_sortRequest;

    private final boolean dryRun;

    public RequestImpl(java.util.Set<java.lang.String> propertyIds, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties, java.util.Map<java.lang.String, java.lang.String> requestInfoProperties, java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> mapTemporalInfo) {
        this(propertyIds, properties, requestInfoProperties, mapTemporalInfo, null, null);
    }

    public RequestImpl(java.util.Set<java.lang.String> propertyIds, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties, java.util.Map<java.lang.String, java.lang.String> requestInfoProperties, java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> mapTemporalInfo, org.apache.ambari.server.controller.spi.SortRequest sortRequest, org.apache.ambari.server.controller.spi.PageRequest pageRequest) {
        this.propertyIds = (propertyIds == null) ? com.google.common.collect.ImmutableSet.of() : com.google.common.collect.ImmutableSet.copyOf(propertyIds);
        this.properties = (properties == null) ? com.google.common.collect.ImmutableSet.of() : com.google.common.collect.ImmutableSet.copyOf(properties);
        this.requestInfoProperties = (requestInfoProperties == null) ? com.google.common.collect.ImmutableMap.of() : com.google.common.collect.ImmutableMap.copyOf(requestInfoProperties);
        m_mapTemporalInfo = mapTemporalInfo;
        m_sortRequest = sortRequest;
        m_pageRequest = pageRequest;
        this.dryRun = this.requestInfoProperties.containsKey(org.apache.ambari.server.controller.spi.Request.DIRECTIVE_DRY_RUN) && java.lang.Boolean.parseBoolean(this.requestInfoProperties.get(org.apache.ambari.server.controller.spi.Request.DIRECTIVE_DRY_RUN));
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getPropertyIds() {
        return propertyIds;
    }

    @java.lang.Override
    public java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> getProperties() {
        return properties;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getRequestInfoProperties() {
        return requestInfoProperties;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.TemporalInfo getTemporalInfo(java.lang.String id) {
        return m_mapTemporalInfo == null ? null : m_mapTemporalInfo.get(id);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.PageRequest getPageRequest() {
        return m_pageRequest;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.SortRequest getSortRequest() {
        return m_sortRequest;
    }

    @java.lang.Override
    public boolean isDryRunRequest() {
        return dryRun;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.controller.internal.RequestImpl request = ((org.apache.ambari.server.controller.internal.RequestImpl) (o));
        return (!(properties == null ? request.properties != null : !properties.equals(request.properties))) && (!(propertyIds == null ? request.propertyIds != null : !propertyIds.equals(request.propertyIds)));
    }

    @java.lang.Override
    public int hashCode() {
        int result = (propertyIds != null) ? propertyIds.hashCode() : 0;
        result = (31 * result) + (properties != null ? properties.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("Request:" + ", propertyIds=[");
        for (java.lang.String pId : propertyIds) {
            sb.append(" { propertyName=").append(pId).append(" }, ");
        }
        sb.append(" ], properties=[ ");
        for (java.util.Map<java.lang.String, java.lang.Object> map : properties) {
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : map.entrySet()) {
                sb.append(" { propertyName=").append(entry.getKey()).append(", propertyValue=").append(entry.getValue() == null ? "NULL" : entry.getValue().toString()).append(" }, ");
            }
        }
        sb.append(" ], temporalInfo=[");
        if (m_mapTemporalInfo == null) {
            sb.append("null");
        } else {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> entry : m_mapTemporalInfo.entrySet()) {
                sb.append(" { propertyName=").append(entry.getKey()).append(", temporalInfo=").append(entry.getValue() == null ? "NULL" : entry.getValue().toString());
            }
        }
        sb.append(" ]");
        return sb.toString();
    }
}