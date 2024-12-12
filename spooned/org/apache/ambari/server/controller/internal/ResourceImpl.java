package org.apache.ambari.server.controller.internal;
public class ResourceImpl implements org.apache.ambari.server.controller.spi.Resource {
    private final org.apache.ambari.server.controller.spi.Resource.Type type;

    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> propertiesMap = java.util.Collections.synchronizedMap(new java.util.TreeMap<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>());

    public ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type type) {
        this.type = type;
    }

    public ResourceImpl(org.apache.ambari.server.controller.spi.Resource resource) {
        this(resource, null);
    }

    public ResourceImpl(org.apache.ambari.server.controller.spi.Resource resource, java.util.Set<java.lang.String> propertyIds) {
        this.type = resource.getType();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> categoryEntry : resource.getPropertiesMap().entrySet()) {
            java.lang.String category = categoryEntry.getKey();
            java.util.Map<java.lang.String, java.lang.Object> propertyMap = categoryEntry.getValue();
            if (propertyMap != null) {
                for (java.util.Map.Entry<java.lang.String, java.lang.Object> propertyEntry : propertyMap.entrySet()) {
                    java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(category, propertyEntry.getKey());
                    if (((propertyIds == null) || propertyIds.isEmpty()) || org.apache.ambari.server.controller.utilities.PropertyHelper.containsProperty(propertyIds, propertyId)) {
                        java.lang.Object propertyValue = propertyEntry.getValue();
                        setProperty(propertyId, propertyValue);
                    }
                }
            }
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Resource.Type getType() {
        return type;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> getPropertiesMap() {
        return propertiesMap;
    }

    @java.lang.Override
    public void setProperty(java.lang.String id, java.lang.Object value) {
        java.lang.String categoryKey = getCategoryKey(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(id));
        java.util.Map<java.lang.String, java.lang.Object> properties = propertiesMap.get(categoryKey);
        if (properties == null) {
            properties = java.util.Collections.synchronizedMap(new java.util.TreeMap<java.lang.String, java.lang.Object>());
            propertiesMap.put(categoryKey, properties);
        }
        properties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(id), value);
    }

    @java.lang.Override
    public void addCategory(java.lang.String id) {
        java.lang.String categoryKey = getCategoryKey(id);
        if (!propertiesMap.containsKey(categoryKey)) {
            propertiesMap.put(categoryKey, new java.util.HashMap<>());
        }
    }

    @java.lang.Override
    public java.lang.Object getPropertyValue(java.lang.String id) {
        java.lang.String categoryKey = getCategoryKey(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(id));
        java.util.Map<java.lang.String, java.lang.Object> properties = propertiesMap.get(categoryKey);
        return properties == null ? null : properties.get(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(id));
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("Resource : ").append(type).append("\n");
        sb.append("Properties:\n");
        sb.append(propertiesMap);
        return sb.toString();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.internal.ResourceImpl resource = ((org.apache.ambari.server.controller.internal.ResourceImpl) (o));
        return (type == resource.type) && (!(propertiesMap != null ? !propertiesMap.equals(resource.propertiesMap) : resource.propertiesMap != null));
    }

    @java.lang.Override
    public int hashCode() {
        return (31 * type.hashCode()) + (propertiesMap != null ? propertiesMap.hashCode() : 0);
    }

    private java.lang.String getCategoryKey(java.lang.String category) {
        return category == null ? "" : category;
    }
}