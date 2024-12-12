package org.apache.ambari.server.upgrade;
public class UpgradeCatalog275 extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.UpgradeCatalog275.class);

    static final com.google.gson.Gson GSON = new com.google.gson.Gson();

    @com.google.inject.Inject
    public UpgradeCatalog275(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    public java.lang.String getSourceVersion() {
        return "2.7.4";
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "2.7.5";
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.upgrade.UpgradeCatalog275.LOG.debug("UpgradeCatalog275 executing Pre-DML Updates.");
        removeDfsHAInitial();
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.upgrade.UpgradeCatalog275.LOG.debug("UpgradeCatalog275 executing DML Updates.");
        addNewConfigurationsFromXml();
    }

    protected void removeDfsHAInitial() {
        org.apache.ambari.server.orm.dao.BlueprintDAO blueprintDAO = injector.getInstance(org.apache.ambari.server.orm.dao.BlueprintDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.BlueprintEntity> blueprintEntityList = blueprintDAO.findAll();
        java.util.List<org.apache.ambari.server.orm.entities.BlueprintEntity> changedBlueprints = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.entities.BlueprintEntity blueprintEntity : blueprintEntityList) {
            boolean changed = false;
            java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintConfigEntity> blueprintConfigurations = blueprintEntity.getConfigurations();
            for (org.apache.ambari.server.orm.entities.BlueprintConfigEntity blueprintConfigEntity : blueprintConfigurations) {
                if (blueprintConfigEntity.getType().equals("hadoop-env")) {
                    java.lang.String configData = blueprintConfigEntity.getConfigData();
                    java.util.Map<java.lang.String, java.lang.String> typeProperties = org.apache.ambari.server.upgrade.UpgradeCatalog275.GSON.<java.util.Map<java.lang.String, java.lang.String>>fromJson(configData, java.util.Map.class);
                    typeProperties.remove("dfs_ha_initial_namenode_standby");
                    typeProperties.remove("dfs_ha_initial_namenode_active");
                    blueprintConfigEntity.setConfigData(org.apache.ambari.server.upgrade.UpgradeCatalog275.GSON.toJson(typeProperties));
                    changed = true;
                }
            }
            if (changed) {
                changedBlueprints.add(blueprintEntity);
            }
        }
        for (org.apache.ambari.server.orm.entities.BlueprintEntity blueprintEntity : changedBlueprints) {
            blueprintDAO.merge(blueprintEntity);
        }
    }
}