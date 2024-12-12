package org.apache.ambari.server.upgrade;
public class UpgradeCatalog262 extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    private static final java.lang.String HOST_REQUEST_TABLE = "topology_host_request";

    private static final java.lang.String STATUS_COLUMN = "status";

    private static final java.lang.String STATUS_MESSAGE_COLUMN = "status_message";

    @com.google.inject.Inject
    public UpgradeCatalog262(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    public java.lang.String getSourceVersion() {
        return "2.6.1";
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "2.6.2";
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        addHostRequestStatusColumn();
    }

    private void addHostRequestStatusColumn() throws java.sql.SQLException {
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog262.HOST_REQUEST_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog262.STATUS_COLUMN, java.lang.String.class, 255, null, true));
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog262.HOST_REQUEST_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog262.STATUS_MESSAGE_COLUMN, java.lang.String.class, 1024, null, true));
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        fixDesiredStack();
    }

    private void fixDesiredStack() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = getCheckedClusterMap(clusters);
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    org.apache.ambari.server.state.StackId desiredStack = cluster.getDesiredStackVersion();
                    org.apache.ambari.server.state.StackId currentStack = cluster.getCurrentStackVersion();
                    if (!desiredStack.equals(currentStack)) {
                        cluster.setDesiredStackVersion(currentStack);
                    }
                }
            }
        }
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }
}