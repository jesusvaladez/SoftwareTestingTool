package org.apache.ambari.view.migration;
public interface ViewDataMigrationContext {
    int getCurrentDataVersion();

    org.apache.ambari.view.ViewInstanceDefinition getCurrentInstanceDefinition();

    java.util.Map<java.lang.String, java.lang.Class> getCurrentEntityClasses();

    org.apache.ambari.view.DataStore getCurrentDataStore();

    void putCurrentInstanceData(java.lang.String user, java.lang.String key, java.lang.String value);

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getCurrentInstanceDataByUser();

    int getOriginDataVersion();

    org.apache.ambari.view.ViewInstanceDefinition getOriginInstanceDefinition();

    java.util.Map<java.lang.String, java.lang.Class> getOriginEntityClasses();

    org.apache.ambari.view.DataStore getOriginDataStore();

    void putOriginInstanceData(java.lang.String user, java.lang.String key, java.lang.String value);

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getOriginInstanceDataByUser();

    void copyAllObjects(java.lang.Class originEntityClass, java.lang.Class currentEntityClass) throws org.apache.ambari.view.migration.ViewDataMigrationException;

    void copyAllObjects(java.lang.Class originEntityClass, java.lang.Class currentEntityClass, org.apache.ambari.view.migration.EntityConverter entityConverter) throws org.apache.ambari.view.migration.ViewDataMigrationException;

    void copyAllInstanceData();
}