package org.apache.ambari.server.state;
public interface Config {
    java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> getPropertiesTypes();

    void setPropertiesTypes(java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertiesTypes);

    java.lang.String getType();

    java.lang.String getTag();

    org.apache.ambari.server.state.StackId getStackId();

    java.lang.Long getVersion();

    java.util.Map<java.lang.String, java.lang.String> getProperties();

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getPropertiesAttributes();

    void setProperties(java.util.Map<java.lang.String, java.lang.String> properties);

    void setPropertiesAttributes(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes);

    void updateProperties(java.util.Map<java.lang.String, java.lang.String> properties);

    java.util.List<java.lang.Long> getServiceConfigVersions();

    void deleteProperties(java.util.List<java.lang.String> properties);

    void save();

    org.apache.ambari.server.state.Cluster getCluster();
}