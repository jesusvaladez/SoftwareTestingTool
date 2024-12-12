package org.apache.ambari.server.topology;
public class DefaultStackFactory implements org.apache.ambari.server.topology.StackFactory {
    @java.lang.Override
    public org.apache.ambari.server.controller.internal.Stack createStack(java.lang.String stackName, java.lang.String stackVersion, org.apache.ambari.server.controller.AmbariManagementController managementController) throws org.apache.ambari.server.AmbariException {
        return new org.apache.ambari.server.controller.internal.Stack(stackName, stackVersion, managementController);
    }
}