package org.apache.ambari.view.migration;
public interface ViewDataMigrator {
    boolean beforeMigration() throws org.apache.ambari.view.migration.ViewDataMigrationException;

    void afterMigration() throws org.apache.ambari.view.migration.ViewDataMigrationException;

    void migrateEntity(java.lang.Class originEntityClass, java.lang.Class currentEntityClass) throws org.apache.ambari.view.migration.ViewDataMigrationException;

    void migrateInstanceData() throws org.apache.ambari.view.migration.ViewDataMigrationException;
}