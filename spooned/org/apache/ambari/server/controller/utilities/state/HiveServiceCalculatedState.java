package org.apache.ambari.server.controller.utilities.state;
@org.apache.ambari.server.StaticallyInject
public final class HiveServiceCalculatedState extends org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState implements org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.utilities.state.HiveServiceCalculatedState.class);

    @java.lang.Override
    public org.apache.ambari.server.state.State getState(java.lang.String clusterName, java.lang.String serviceName) {
        try {
            org.apache.ambari.server.state.Cluster cluster = getCluster(clusterName);
            if ((cluster != null) && (org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.managementControllerProvider != null)) {
                org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.managementControllerProvider.get().getAmbariMetaInfo();
                org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
                org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
                org.apache.ambari.server.controller.ServiceComponentHostRequest request = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, serviceName, null, null, null);
                java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> hostComponentResponses = org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.managementControllerProvider.get().getHostComponents(java.util.Collections.singleton(request), true);
                int activeHiveMetastoreComponentCount = 0;
                org.apache.ambari.server.state.State nonStartedState = null;
                boolean embeddedMysqlComponentExists = false;
                boolean hiveServerComponentStarted = false;
                boolean webHcatComponentStarted = false;
                boolean mysqlComponentStarted = false;
                for (org.apache.ambari.server.controller.ServiceComponentHostResponse hostComponentResponse : hostComponentResponses) {
                    try {
                        org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), hostComponentResponse.getServiceName(), hostComponentResponse.getComponentName());
                        if (componentInfo.isMaster()) {
                            org.apache.ambari.server.state.State state = getHostComponentState(hostComponentResponse);
                            java.lang.String componentName = hostComponentResponse.getComponentName();
                            if (componentName.equals("MYSQL_SERVER")) {
                                embeddedMysqlComponentExists = true;
                            }
                            switch (state) {
                                case STARTED :
                                case DISABLED :
                                    if (componentName.equals("HIVE_METASTORE")) {
                                        ++activeHiveMetastoreComponentCount;
                                    } else if (componentName.equals("HIVE_SERVER")) {
                                        hiveServerComponentStarted = true;
                                    } else if (componentName.equals("MYSQL_SERVER")) {
                                        mysqlComponentStarted = true;
                                    } else if (componentName.equals("WEBHCAT_SERVER")) {
                                        webHcatComponentStarted = true;
                                    }
                                    break;
                                default :
                                    nonStartedState = state;
                            }
                        }
                    } catch (org.apache.ambari.server.ObjectNotFoundException e) {
                    }
                }
                if ((nonStartedState == null) || (((hiveServerComponentStarted && webHcatComponentStarted) && (activeHiveMetastoreComponentCount > 0)) && ((!embeddedMysqlComponentExists) || mysqlComponentStarted))) {
                    return org.apache.ambari.server.state.State.STARTED;
                }
                return nonStartedState;
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.controller.utilities.state.HiveServiceCalculatedState.LOG.error("Can't determine service state.", e);
        }
        return org.apache.ambari.server.state.State.UNKNOWN;
    }
}