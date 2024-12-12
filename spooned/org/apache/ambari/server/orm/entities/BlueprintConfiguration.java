package org.apache.ambari.server.orm.entities;
public interface BlueprintConfiguration {
    void setType(java.lang.String type);

    java.lang.String getType();

    void setBlueprintName(java.lang.String blueprintName);

    java.lang.String getBlueprintName();

    void setConfigData(java.lang.String configData);

    java.lang.String getConfigData();

    java.lang.String getConfigAttributes();

    void setConfigAttributes(java.lang.String configAttributes);
}