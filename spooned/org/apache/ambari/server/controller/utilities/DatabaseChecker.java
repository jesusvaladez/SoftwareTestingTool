package org.apache.ambari.server.controller.utilities;
public class DatabaseChecker {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.utilities.DatabaseChecker.class);

    @com.google.inject.Inject
    static com.google.inject.Injector injector;

    static org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    static org.apache.ambari.server.orm.dao.MetainfoDAO metainfoDAO;

    public static void checkDBConsistency() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.info("Checking DB consistency");
        boolean checkPassed = true;
        if (org.apache.ambari.server.controller.utilities.DatabaseChecker.ambariMetaInfo == null) {
            org.apache.ambari.server.controller.utilities.DatabaseChecker.ambariMetaInfo = org.apache.ambari.server.controller.utilities.DatabaseChecker.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        }
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = org.apache.ambari.server.controller.utilities.DatabaseChecker.injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterEntity> clusters = clusterDAO.findAll();
        for (org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity : clusters) {
            org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(clusterEntity.getDesiredStack());
            java.util.Collection<org.apache.ambari.server.orm.entities.ClusterServiceEntity> serviceEntities = clusterEntity.getClusterServiceEntities();
            for (org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity : serviceEntities) {
                org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity = clusterServiceEntity.getServiceDesiredStateEntity();
                if (serviceDesiredStateEntity == null) {
                    checkPassed = false;
                    org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.error(java.lang.String.format("ServiceDesiredStateEntity is null for " + "ServiceComponentDesiredStateEntity, clusterName=%s, serviceName=%s ", clusterEntity.getClusterName(), clusterServiceEntity.getServiceName()));
                }
                java.util.Collection<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> scDesiredStateEntities = clusterServiceEntity.getServiceComponentDesiredStateEntities();
                if ((scDesiredStateEntities == null) || scDesiredStateEntities.isEmpty()) {
                    checkPassed = false;
                    org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.error(java.lang.String.format("serviceComponentDesiredStateEntities is null or empty for " + "ServiceComponentDesiredStateEntity, clusterName=%s, serviceName=%s ", clusterEntity.getClusterName(), clusterServiceEntity.getServiceName()));
                } else {
                    for (org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity scDesiredStateEnity : scDesiredStateEntities) {
                        java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> schDesiredStateEntities = scDesiredStateEnity.getHostComponentDesiredStateEntities();
                        java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> schStateEntities = scDesiredStateEnity.getHostComponentStateEntities();
                        org.apache.ambari.server.state.ComponentInfo componentInfo = org.apache.ambari.server.controller.utilities.DatabaseChecker.ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), scDesiredStateEnity.getServiceName(), scDesiredStateEnity.getComponentName());
                        boolean zeroCardinality = ((componentInfo.getCardinality() == null) || componentInfo.getCardinality().startsWith("0")) || scDesiredStateEnity.getComponentName().equals("SECONDARY_NAMENODE");
                        boolean componentCheckFailed = false;
                        if (schDesiredStateEntities == null) {
                            componentCheckFailed = true;
                            org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.error(java.lang.String.format("hostComponentDesiredStateEntities is null for " + "ServiceComponentDesiredStateEntity, clusterName=%s, serviceName=%s, componentName=%s ", clusterEntity.getClusterName(), scDesiredStateEnity.getServiceName(), scDesiredStateEnity.getComponentName()));
                        } else if ((!zeroCardinality) && schDesiredStateEntities.isEmpty()) {
                            componentCheckFailed = true;
                            org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.error(java.lang.String.format("hostComponentDesiredStateEntities is empty for " + "ServiceComponentDesiredStateEntity, clusterName=%s, serviceName=%s, componentName=%s ", clusterEntity.getClusterName(), scDesiredStateEnity.getServiceName(), scDesiredStateEnity.getComponentName()));
                        }
                        if (schStateEntities == null) {
                            componentCheckFailed = true;
                            org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.error(java.lang.String.format("hostComponentStateEntities is null for " + "ServiceComponentDesiredStateEntity, clusterName=%s, serviceName=%s, componentName=%s ", clusterEntity.getClusterName(), scDesiredStateEnity.getServiceName(), scDesiredStateEnity.getComponentName()));
                        } else if ((!zeroCardinality) && schStateEntities.isEmpty()) {
                            componentCheckFailed = true;
                            org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.error(java.lang.String.format("hostComponentStateEntities is empty for " + "ServiceComponentDesiredStateEntity, clusterName=%s, serviceName=%s, componentName=%s ", clusterEntity.getClusterName(), scDesiredStateEnity.getServiceName(), scDesiredStateEnity.getComponentName()));
                        }
                        if ((!componentCheckFailed) && (schDesiredStateEntities.size() != schStateEntities.size())) {
                            checkPassed = false;
                            org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.error(java.lang.String.format("HostComponentStateEntities and HostComponentDesiredStateEntities " + ("tables must contain equal number of rows mapped to ServiceComponentDesiredStateEntity, " + "(clusterName=%s, serviceName=%s, componentName=%s) "), clusterEntity.getClusterName(), scDesiredStateEnity.getServiceName(), scDesiredStateEnity.getComponentName()));
                        }
                        checkPassed = checkPassed && (!componentCheckFailed);
                    }
                }
            }
        }
        if (checkPassed) {
            org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.info("DB consistency check passed.");
        } else {
            java.lang.String errorMessage = "DB consistency check failed. Run \"ambari-server start --skip-database-validation\" to skip validation.";
            org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.error(errorMessage);
            throw new org.apache.ambari.server.AmbariException(errorMessage);
        }
    }

    private static boolean clusterConfigsContainTypeAndTag(java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities, java.lang.String typeName, java.lang.String tag) {
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : clusterConfigEntities) {
            if (typeName.equals(clusterConfigEntity.getType()) && tag.equals(clusterConfigEntity.getTag())) {
                return true;
            }
        }
        return false;
    }

    public static void checkDBConfigsConsistency() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.info("Checking DB configs consistency");
        boolean checkPassed = true;
        if (org.apache.ambari.server.controller.utilities.DatabaseChecker.ambariMetaInfo == null) {
            org.apache.ambari.server.controller.utilities.DatabaseChecker.ambariMetaInfo = org.apache.ambari.server.controller.utilities.DatabaseChecker.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        }
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = org.apache.ambari.server.controller.utilities.DatabaseChecker.injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterEntity> clusters = clusterDAO.findAll();
        if (clusters != null) {
            for (org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity : clusters) {
                java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities = clusterEntity.getClusterConfigEntities();
                if (null == clusterConfigEntities) {
                    return;
                }
                java.util.Map<java.lang.String, java.lang.Integer> selectedCountForType = new java.util.HashMap<>();
                for (org.apache.ambari.server.orm.entities.ClusterConfigEntity configEntity : clusterConfigEntities) {
                    java.lang.String typeName = configEntity.getType();
                    if (configEntity.isSelected()) {
                        int selectedCount = (selectedCountForType.get(typeName) != null) ? selectedCountForType.get(typeName) : 0;
                        selectedCountForType.put(typeName, selectedCount + 1);
                    } else if (!selectedCountForType.containsKey(typeName)) {
                        selectedCountForType.put(typeName, 0);
                    }
                }
                java.util.Collection<org.apache.ambari.server.orm.entities.ClusterServiceEntity> clusterServiceEntities = clusterEntity.getClusterServiceEntities();
                org.apache.ambari.server.orm.entities.ClusterStateEntity clusterStateEntity = clusterEntity.getClusterStateEntity();
                if (clusterStateEntity != null) {
                    org.apache.ambari.server.orm.entities.StackEntity currentStack = clusterStateEntity.getCurrentStack();
                    org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.controller.utilities.DatabaseChecker.ambariMetaInfo.getStack(currentStack.getStackName(), currentStack.getStackVersion());
                    for (org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity : clusterServiceEntities) {
                        if (!org.apache.ambari.server.state.State.INIT.equals(clusterServiceEntity.getServiceDesiredStateEntity().getDesiredState())) {
                            java.lang.String serviceName = clusterServiceEntity.getServiceName();
                            org.apache.ambari.server.state.ServiceInfo serviceInfo = org.apache.ambari.server.controller.utilities.DatabaseChecker.ambariMetaInfo.getService(stack.getName(), stack.getVersion(), serviceName);
                            for (java.lang.String configTypeName : serviceInfo.getConfigTypeAttributes().keySet()) {
                                if (selectedCountForType.get(configTypeName) == null) {
                                    checkPassed = false;
                                    org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.error("Configuration {} is missing for service {}", configTypeName, serviceName);
                                } else if (selectedCountForType.get(configTypeName) == 0) {
                                    checkPassed = false;
                                    org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.error("Configuration {} has no enabled entries for service {}", configTypeName, serviceName);
                                } else if (selectedCountForType.get(configTypeName) > 1) {
                                    checkPassed = false;
                                    org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.error("Configuration {} has more than 1 enabled entry for service {}", configTypeName, serviceName);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (checkPassed) {
            org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.info("DB configs consistency check passed.");
        } else {
            java.lang.String errorMessage = "DB configs consistency check failed. Run \"ambari-server start --skip-database-validation\" to skip validation.";
            org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.error(errorMessage);
            throw new org.apache.ambari.server.AmbariException(errorMessage);
        }
    }

    public static void checkDBVersion() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.info("Checking DB store version");
        if (org.apache.ambari.server.controller.utilities.DatabaseChecker.metainfoDAO == null) {
            org.apache.ambari.server.controller.utilities.DatabaseChecker.metainfoDAO = org.apache.ambari.server.controller.utilities.DatabaseChecker.injector.getInstance(org.apache.ambari.server.orm.dao.MetainfoDAO.class);
        }
        org.apache.ambari.server.orm.entities.MetainfoEntity schemaVersionEntity = org.apache.ambari.server.controller.utilities.DatabaseChecker.metainfoDAO.findByKey(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_KEY);
        java.lang.String schemaVersion = null;
        if (schemaVersionEntity != null) {
            schemaVersion = schemaVersionEntity.getMetainfoValue();
        }
        org.apache.ambari.server.configuration.Configuration conf = org.apache.ambari.server.controller.utilities.DatabaseChecker.injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        java.io.File versionFile = new java.io.File(conf.getServerVersionFilePath());
        if (!versionFile.exists()) {
            throw new org.apache.ambari.server.AmbariException("Server version file does not exist.");
        }
        java.lang.String serverVersion = null;
        try (java.util.Scanner scanner = new java.util.Scanner(versionFile)) {
            serverVersion = scanner.useDelimiter("\\Z").next();
        } catch (java.io.IOException ioe) {
            throw new org.apache.ambari.server.AmbariException("Unable to read server version file.");
        }
        if ((schemaVersionEntity == null) || (org.apache.ambari.server.utils.VersionUtils.compareVersions(schemaVersion, serverVersion, 3) != 0)) {
            java.lang.String error = ((("Current database store version is not compatible with " + ("current server version" + ", serverVersion=")) + serverVersion) + ", schemaVersion=") + schemaVersion;
            org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.warn(error);
            throw new org.apache.ambari.server.AmbariException(error);
        }
        org.apache.ambari.server.controller.utilities.DatabaseChecker.LOG.info("DB store version is compatible");
    }
}