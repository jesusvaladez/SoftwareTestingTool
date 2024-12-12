package org.apache.ambari.server.topology.validators;
public class HiveServiceValidator implements org.apache.ambari.server.topology.TopologyValidator {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.validators.HiveServiceValidator.class);

    private static final java.lang.String HIVE_ENV = "hive-env";

    private static final java.lang.String HIVE_DB_DEFAULT = "New MySQL Database";

    private static final java.lang.String HIVE_DB_PROPERTY = "hive_database";

    private static final java.lang.String MYSQL_SERVER_COMPONENT = "MYSQL_SERVER";

    public static final java.lang.String HIVE_SERVICE = "HIVE";

    @java.lang.Override
    public void validate(org.apache.ambari.server.topology.ClusterTopology topology) throws org.apache.ambari.server.topology.InvalidTopologyException {
        if (!topology.getBlueprint().getServices().contains(org.apache.ambari.server.topology.validators.HiveServiceValidator.HIVE_SERVICE)) {
            org.apache.ambari.server.topology.validators.HiveServiceValidator.LOGGER.info(" [{}] service is not listed in the blueprint, skipping hive service validation.", org.apache.ambari.server.topology.validators.HiveServiceValidator.HIVE_SERVICE);
            return;
        }
        org.apache.ambari.server.topology.Configuration clusterConfiguration = topology.getConfiguration();
        if (!clusterConfiguration.getAllConfigTypes().contains(org.apache.ambari.server.topology.validators.HiveServiceValidator.HIVE_ENV)) {
            java.lang.String errorMessage = java.lang.String.format(" [ %s ] config type is missing from the service [ %s ]. HIVE service validation failed.", org.apache.ambari.server.topology.validators.HiveServiceValidator.HIVE_ENV, org.apache.ambari.server.topology.validators.HiveServiceValidator.HIVE_SERVICE);
            org.apache.ambari.server.topology.validators.HiveServiceValidator.LOGGER.error(errorMessage);
            throw new org.apache.ambari.server.topology.InvalidTopologyException(errorMessage);
        }
        if (!org.apache.ambari.server.topology.validators.HiveServiceValidator.HIVE_DB_DEFAULT.equals(clusterConfiguration.getPropertyValue(org.apache.ambari.server.topology.validators.HiveServiceValidator.HIVE_ENV, org.apache.ambari.server.topology.validators.HiveServiceValidator.HIVE_DB_PROPERTY))) {
            org.apache.ambari.server.topology.validators.HiveServiceValidator.LOGGER.info("Custom hive database settings detected. HIVE service validation succeeded.");
            return;
        }
        if (!topology.getBlueprint().getComponents(org.apache.ambari.server.topology.validators.HiveServiceValidator.HIVE_SERVICE).contains(org.apache.ambari.server.topology.validators.HiveServiceValidator.MYSQL_SERVER_COMPONENT)) {
            java.lang.String errorMessage = java.lang.String.format("Component [%s] must explicitly be set in the blueprint when hive database " + "is configured with the current settings. HIVE service validation failed.", org.apache.ambari.server.topology.validators.HiveServiceValidator.MYSQL_SERVER_COMPONENT);
            org.apache.ambari.server.topology.validators.HiveServiceValidator.LOGGER.error(errorMessage);
            throw new org.apache.ambari.server.topology.InvalidTopologyException(errorMessage);
        }
    }
}