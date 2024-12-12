package org.apache.ambari.server.controller.utilities.state;
@org.apache.ambari.server.StaticallyInject
public final class HDFSServiceCalculatedState extends org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState implements org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.utilities.state.HDFSServiceCalculatedState.class);

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
                java.util.Set<java.lang.String> startedOrDisabledNNHosts = new java.util.HashSet<>();
                int nameNodeCount = 0;
                int nameNodeStartedOrDisabledCount = 0;
                boolean hasSecondary = false;
                boolean hasJournal = false;
                org.apache.ambari.server.state.State nonStartedState = null;
                for (org.apache.ambari.server.controller.ServiceComponentHostResponse hostComponentResponse : hostComponentResponses) {
                    try {
                        org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), hostComponentResponse.getServiceName(), hostComponentResponse.getComponentName());
                        if (componentInfo.isMaster()) {
                            java.lang.String componentName = hostComponentResponse.getComponentName();
                            boolean isNameNode = false;
                            switch (componentName) {
                                case "NAMENODE" :
                                    ++nameNodeCount;
                                    isNameNode = true;
                                    break;
                                case "SECONDARY_NAMENODE" :
                                    hasSecondary = true;
                                    break;
                                case "JOURNALNODE" :
                                    hasJournal = true;
                                    break;
                            }
                            org.apache.ambari.server.state.State state = getHostComponentState(hostComponentResponse);
                            switch (state) {
                                case STARTED :
                                case DISABLED :
                                    if (isNameNode) {
                                        ++nameNodeStartedOrDisabledCount;
                                        startedOrDisabledNNHosts.add(hostComponentResponse.getHostname());
                                    }
                                    break;
                                default :
                                    nonStartedState = state;
                            }
                        }
                    } catch (org.apache.ambari.server.ObjectNotFoundException e) {
                    }
                }
                boolean multipleNameServices = nameNodeCount > 2;
                int nameServiceWithStartedOrDisabledNNCount = 0;
                java.util.List<org.apache.ambari.server.stack.NameService> nameServices = new java.util.ArrayList<>();
                if (multipleNameServices) {
                    org.apache.ambari.server.state.ConfigHelper configHelper = org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.managementControllerProvider.get().getConfigHelper();
                    nameServices = org.apache.ambari.server.stack.NameService.fromConfig(configHelper, cluster);
                    for (org.apache.ambari.server.stack.NameService nameService : nameServices) {
                        boolean hasStartedOrDisabledNN = false;
                        for (org.apache.ambari.server.stack.NameService.NameNode nameNode : nameService.getNameNodes()) {
                            if (startedOrDisabledNNHosts.contains(nameNode.getHost())) {
                                hasStartedOrDisabledNN = true;
                                break;
                            }
                        }
                        if (hasStartedOrDisabledNN) {
                            nameServiceWithStartedOrDisabledNNCount++;
                        }
                    }
                }
                if ((nonStartedState == null) || (((((nameNodeCount > 0) && (!hasSecondary)) || hasJournal) && (nameNodeStartedOrDisabledCount > 0)) && ((!multipleNameServices) || (nameServiceWithStartedOrDisabledNNCount == nameServices.size())))) {
                    return org.apache.ambari.server.state.State.STARTED;
                }
                return nonStartedState;
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.controller.utilities.state.HDFSServiceCalculatedState.LOG.error("Can't determine service state.", e);
        }
        return org.apache.ambari.server.state.State.UNKNOWN;
    }
}