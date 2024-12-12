package org.apache.ambari.server.upgrade;
public class UpdateAlertScriptPaths extends org.apache.ambari.server.upgrade.AbstractFinalUpgradeCatalog {
    @javax.inject.Inject
    public UpdateAlertScriptPaths(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        ambariMetaInfo.reconcileAlertDefinitions(clusters, true);
    }
}