package org.apache.ambari.server.controller.internal;
public class RequestOperationLevel {
    private static final java.lang.String[][] LEVEL_ALIASES = new java.lang.String[][]{ new java.lang.String[]{ "CLUSTER", "Cluster" }, new java.lang.String[]{ "SERVICE", "Service" }, new java.lang.String[]{ "HOST", "Host" }, new java.lang.String[]{ "HOST_COMPONENT", "HostComponent" } };

    private static final int ALIAS_COLUMN = 0;

    private static final int INTERNAL_NAME_COLUMN = 1;

    public static final java.lang.String OPERATION_LEVEL_ID = "operation_level/level";

    public static final java.lang.String OPERATION_CLUSTER_ID = "operation_level/cluster_name";

    public static final java.lang.String OPERATION_SERVICE_ID = "operation_level/service_name";

    public static final java.lang.String OPERATION_HOSTCOMPONENT_ID = "operation_level/hostcomponent_name";

    public static final java.lang.String OPERATION_HOST_NAME = "operation_level/host_name";

    public static java.lang.String getInternalLevelName(java.lang.String external) throws java.lang.IllegalArgumentException {
        java.lang.String refinedAlias = external.trim().toUpperCase();
        for (java.lang.String[] pair : org.apache.ambari.server.controller.internal.RequestOperationLevel.LEVEL_ALIASES) {
            if (pair[org.apache.ambari.server.controller.internal.RequestOperationLevel.ALIAS_COLUMN].equals(refinedAlias)) {
                return pair[org.apache.ambari.server.controller.internal.RequestOperationLevel.INTERNAL_NAME_COLUMN];
            }
        }
        java.lang.String message = java.lang.String.format("Unknown operation level %s", external);
        throw new java.lang.IllegalArgumentException(message);
    }

    public static java.lang.String getExternalLevelName(java.lang.String internal) {
        for (java.lang.String[] pair : org.apache.ambari.server.controller.internal.RequestOperationLevel.LEVEL_ALIASES) {
            if (pair[org.apache.ambari.server.controller.internal.RequestOperationLevel.INTERNAL_NAME_COLUMN].equals(internal)) {
                return pair[org.apache.ambari.server.controller.internal.RequestOperationLevel.ALIAS_COLUMN];
            }
        }
        java.lang.String message = java.lang.String.format("Unknown internal " + "operation level name %s", internal);
        throw new java.lang.IllegalArgumentException(message);
    }

    public RequestOperationLevel(org.apache.ambari.server.controller.spi.Resource.Type level, java.lang.String clusterName, java.lang.String serviceName, java.lang.String hostComponentName, java.lang.String hostName) {
        this.level = level;
        this.clusterName = clusterName;
        this.serviceName = serviceName;
        this.hostComponentName = hostComponentName;
        this.hostName = hostName;
    }

    public RequestOperationLevel(java.util.Map<java.lang.String, java.lang.String> requestInfoProperties) throws java.lang.IllegalArgumentException {
        java.lang.String operationLevelStr = requestInfoProperties.get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID);
        try {
            java.lang.String internalOpLevelNameStr = org.apache.ambari.server.controller.internal.RequestOperationLevel.getInternalLevelName(operationLevelStr);
            this.level = org.apache.ambari.server.controller.spi.Resource.Type.valueOf(internalOpLevelNameStr);
        } catch (java.lang.IllegalArgumentException e) {
            java.lang.String message = java.lang.String.format("Wrong operation level value: %s", operationLevelStr);
            throw new java.lang.IllegalArgumentException(message, e);
        }
        if (!requestInfoProperties.containsKey(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID)) {
            java.lang.String message = java.lang.String.format("Mandatory key %s for operation level is not specified", org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID);
            throw new java.lang.IllegalArgumentException(message);
        }
        this.clusterName = requestInfoProperties.get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID);
        this.serviceName = requestInfoProperties.get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_SERVICE_ID);
        this.hostComponentName = requestInfoProperties.get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_HOSTCOMPONENT_ID);
        this.hostName = requestInfoProperties.get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_HOST_NAME);
    }

    private org.apache.ambari.server.controller.spi.Resource.Type level;

    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private java.lang.String hostComponentName;

    private java.lang.String hostName;

    public org.apache.ambari.server.controller.spi.Resource.Type getLevel() {
        return level;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public java.lang.String getHostComponentName() {
        return hostComponentName;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((("RequestOperationLevel{" + "level=") + level) + ", clusterName='") + clusterName) + '\'') + ", serviceName='") + serviceName) + '\'') + ", hostComponentName='") + hostComponentName) + '\'') + ", hostName='") + hostName) + '\'') + '}';
    }

    public static java.util.Map<java.lang.String, java.lang.String> propertiesFor(org.apache.ambari.server.controller.spi.Resource.Type type, java.lang.String clusterName) {
        return com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID, org.apache.ambari.server.controller.internal.RequestOperationLevel.getExternalLevelName(type.name()), org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID, clusterName);
    }
}