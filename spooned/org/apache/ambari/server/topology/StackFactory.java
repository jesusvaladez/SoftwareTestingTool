package org.apache.ambari.server.topology;
public interface StackFactory {
    org.apache.ambari.server.controller.internal.Stack createStack(java.lang.String stackName, java.lang.String stackVersion, org.apache.ambari.server.controller.AmbariManagementController managementController) throws org.apache.ambari.server.AmbariException;
}