package org.apache.ambari.server.controller.logging;
public interface LoggingRequestHelperFactory {
    org.apache.ambari.server.controller.logging.LoggingRequestHelper getHelper(org.apache.ambari.server.controller.AmbariManagementController ambariManagementController, java.lang.String clusterName);
}