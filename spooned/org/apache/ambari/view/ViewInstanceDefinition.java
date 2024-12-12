package org.apache.ambari.view;
public interface ViewInstanceDefinition {
    public java.lang.String getInstanceName();

    public java.lang.String getViewName();

    public java.lang.String getLabel();

    public java.lang.String getDescription();

    public java.lang.Long getClusterHandle();

    public org.apache.ambari.view.ClusterType getClusterType();

    public boolean isVisible();

    public java.util.Map<java.lang.String, java.lang.String> getPropertyMap();

    public java.util.Map<java.lang.String, java.lang.String> getInstanceDataMap();

    public org.apache.ambari.view.ViewDefinition getViewDefinition();
}