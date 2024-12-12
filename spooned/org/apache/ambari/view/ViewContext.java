package org.apache.ambari.view;
public interface ViewContext {
    public static final java.lang.String CONTEXT_ATTRIBUTE = "ambari-view-context";

    public java.lang.String getUsername();

    public java.lang.String getLoggedinUser();

    public void hasPermission(java.lang.String userName, java.lang.String permissionName) throws org.apache.ambari.view.SecurityException;

    public java.lang.String getViewName();

    public org.apache.ambari.view.ViewDefinition getViewDefinition();

    public java.lang.String getInstanceName();

    public org.apache.ambari.view.ViewInstanceDefinition getViewInstanceDefinition();

    public java.util.Map<java.lang.String, java.lang.String> getProperties();

    public void putInstanceData(java.lang.String key, java.lang.String value);

    public java.lang.String getInstanceData(java.lang.String key);

    public java.util.Map<java.lang.String, java.lang.String> getInstanceData();

    public void removeInstanceData(java.lang.String key);

    public java.lang.String getAmbariProperty(java.lang.String key);

    public org.apache.ambari.view.ResourceProvider<?> getResourceProvider(java.lang.String type);

    public org.apache.ambari.view.URLStreamProvider getURLStreamProvider();

    public org.apache.ambari.view.URLConnectionProvider getURLConnectionProvider();

    public org.apache.ambari.view.AmbariStreamProvider getAmbariStreamProvider();

    public org.apache.ambari.view.AmbariStreamProvider getAmbariClusterStreamProvider();

    public org.apache.ambari.view.DataStore getDataStore();

    public java.util.Collection<org.apache.ambari.view.ViewDefinition> getViewDefinitions();

    public java.util.Collection<org.apache.ambari.view.ViewInstanceDefinition> getViewInstanceDefinitions();

    public org.apache.ambari.view.ViewController getController();

    @java.lang.Deprecated
    public org.apache.ambari.view.HttpImpersonator getHttpImpersonator();

    @java.lang.Deprecated
    public org.apache.ambari.view.ImpersonatorSetting getImpersonatorSetting();

    public org.apache.ambari.view.cluster.Cluster getCluster();
}