package org.apache.ambari.server.view;
public class ViewDataMigrationUtility {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.view.ViewDataMigrationUtility.class);

    private org.apache.ambari.server.view.ViewRegistry viewRegistry;

    public ViewDataMigrationUtility(org.apache.ambari.server.view.ViewRegistry viewRegistry) {
        this.viewRegistry = viewRegistry;
    }

    public void migrateData(org.apache.ambari.server.orm.entities.ViewInstanceEntity targetInstanceDefinition, org.apache.ambari.server.orm.entities.ViewInstanceEntity sourceInstanceDefinition, boolean migrateOnce) throws org.apache.ambari.view.migration.ViewDataMigrationException {
        org.apache.ambari.server.view.ViewDataMigrationContextImpl migrationContext = getViewDataMigrationContext(targetInstanceDefinition, sourceInstanceDefinition);
        if (migrateOnce) {
            if (!isTargetEmpty(migrationContext)) {
                org.apache.ambari.server.view.ViewDataMigrationUtility.LOG.error("Migration canceled because target instance is not empty");
                return;
            }
        }
        org.apache.ambari.view.migration.ViewDataMigrator dataMigrator = getViewDataMigrator(targetInstanceDefinition, migrationContext);
        org.apache.ambari.server.view.ViewDataMigrationUtility.LOG.debug("Running before-migration hook");
        if (!dataMigrator.beforeMigration()) {
            java.lang.String msg = ("View " + targetInstanceDefinition.getInstanceName()) + " canceled the migration process";
            org.apache.ambari.server.view.ViewDataMigrationUtility.LOG.error(msg);
            throw new org.apache.ambari.view.migration.ViewDataMigrationException(msg);
        }
        java.util.Map<java.lang.String, java.lang.Class> originClasses = migrationContext.getOriginEntityClasses();
        java.util.Map<java.lang.String, java.lang.Class> currentClasses = migrationContext.getCurrentEntityClasses();
        for (java.util.Map.Entry<java.lang.String, java.lang.Class> originEntity : originClasses.entrySet()) {
            org.apache.ambari.server.view.ViewDataMigrationUtility.LOG.debug("Migrating persistence entity {}", originEntity.getKey());
            if (currentClasses.containsKey(originEntity.getKey())) {
                java.lang.Class entity = currentClasses.get(originEntity.getKey());
                dataMigrator.migrateEntity(originEntity.getValue(), entity);
            } else {
                org.apache.ambari.server.view.ViewDataMigrationUtility.LOG.debug("Entity {} not found in target view", originEntity.getKey());
                dataMigrator.migrateEntity(originEntity.getValue(), null);
            }
        }
        org.apache.ambari.server.view.ViewDataMigrationUtility.LOG.debug("Migrating instance data");
        dataMigrator.migrateInstanceData();
        org.apache.ambari.server.view.ViewDataMigrationUtility.LOG.debug("Running after-migration hook");
        dataMigrator.afterMigration();
        org.apache.ambari.server.view.ViewDataMigrationUtility.LOG.debug("Copying user permissions");
        viewRegistry.copyPrivileges(sourceInstanceDefinition, targetInstanceDefinition);
        migrationContext.putCurrentInstanceData("upgrade", "upgradedFrom", sourceInstanceDefinition.getViewEntity().getVersion());
        migrationContext.closeMigration();
    }

    private boolean isTargetEmpty(org.apache.ambari.view.migration.ViewDataMigrationContext migrationContext) {
        if (migrationContext.getCurrentInstanceDataByUser().size() > 0) {
            return false;
        }
        try {
            for (java.lang.Class entity : migrationContext.getCurrentEntityClasses().values()) {
                if (migrationContext.getCurrentDataStore().findAll(entity, null).size() > 0) {
                    return false;
                }
            }
        } catch (org.apache.ambari.view.PersistenceException e) {
            org.apache.ambari.view.ViewInstanceDefinition current = migrationContext.getCurrentInstanceDefinition();
            org.apache.ambari.server.view.ViewDataMigrationUtility.LOG.error((((("Persistence exception while check if instance is empty: " + current.getViewDefinition().getViewName()) + "{") + current.getViewDefinition().getVersion()) + "}/") + current.getInstanceName(), e);
        }
        return true;
    }

    protected org.apache.ambari.server.view.ViewDataMigrationContextImpl getViewDataMigrationContext(org.apache.ambari.server.orm.entities.ViewInstanceEntity targetInstanceDefinition, org.apache.ambari.server.orm.entities.ViewInstanceEntity sourceInstanceDefinition) {
        return new org.apache.ambari.server.view.ViewDataMigrationContextImpl(sourceInstanceDefinition, targetInstanceDefinition);
    }

    protected org.apache.ambari.view.migration.ViewDataMigrator getViewDataMigrator(org.apache.ambari.server.orm.entities.ViewInstanceEntity currentInstanceDefinition, org.apache.ambari.server.view.ViewDataMigrationContextImpl migrationContext) throws org.apache.ambari.view.migration.ViewDataMigrationException {
        org.apache.ambari.view.migration.ViewDataMigrator dataMigrator;
        org.apache.ambari.server.view.ViewDataMigrationUtility.LOG.info(((((("Migrating " + currentInstanceDefinition.getInstanceName()) + " data from ") + migrationContext.getOriginDataVersion()) + " to ") + migrationContext.getCurrentDataVersion()) + " data version");
        if (migrationContext.getOriginDataVersion() == migrationContext.getCurrentDataVersion()) {
            org.apache.ambari.server.view.ViewDataMigrationUtility.LOG.info("Instances of same version, copying all data.");
            dataMigrator = new org.apache.ambari.server.view.ViewDataMigrationUtility.CopyAllDataMigrator(migrationContext);
        } else {
            try {
                dataMigrator = currentInstanceDefinition.getDataMigrator(migrationContext);
                if (dataMigrator == null) {
                    throw new org.apache.ambari.view.migration.ViewDataMigrationException(("A view instance " + currentInstanceDefinition.getInstanceName()) + " does not support migration.");
                }
                org.apache.ambari.server.view.ViewDataMigrationUtility.LOG.debug("Data migrator loaded");
            } catch (java.lang.ClassNotFoundException e) {
                java.lang.String msg = "Caught exception loading data migrator of " + currentInstanceDefinition.getInstanceName();
                org.apache.ambari.server.view.ViewDataMigrationUtility.LOG.error(msg, e);
                throw new java.lang.RuntimeException(msg);
            }
        }
        return dataMigrator;
    }

    public static class CopyAllDataMigrator implements org.apache.ambari.view.migration.ViewDataMigrator {
        private org.apache.ambari.view.migration.ViewDataMigrationContext migrationContext;

        public CopyAllDataMigrator(org.apache.ambari.view.migration.ViewDataMigrationContext migrationContext) {
            this.migrationContext = migrationContext;
        }

        @java.lang.Override
        public boolean beforeMigration() {
            return true;
        }

        @java.lang.Override
        public void afterMigration() {
        }

        @java.lang.Override
        public void migrateEntity(java.lang.Class originEntityClass, java.lang.Class currentEntityClass) throws org.apache.ambari.view.migration.ViewDataMigrationException {
            if (currentEntityClass == null) {
                return;
            }
            migrationContext.copyAllObjects(originEntityClass, currentEntityClass);
        }

        @java.lang.Override
        public void migrateInstanceData() {
            migrationContext.copyAllInstanceData();
        }
    }
}