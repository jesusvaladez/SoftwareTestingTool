package org.apache.ambari.server.controller.utilities.state;
@org.apache.ambari.server.StaticallyInject
public final class OozieServiceCalculatedState extends org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState implements org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.class);

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
                int oozieServerActiveCount = 0;
                org.apache.ambari.server.state.State nonStartedState = null;
                for (org.apache.ambari.server.controller.ServiceComponentHostResponse hostComponentResponse : hostComponentResponses) {
                    try {
                        org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), hostComponentResponse.getServiceName(), hostComponentResponse.getComponentName());
                        if (componentInfo.isMaster()) {
                            org.apache.ambari.server.state.State state = getHostComponentState(hostComponentResponse);
                            switch (state) {
                                case STARTED :
                                case DISABLED :
                                    java.lang.String componentName = hostComponentResponse.getComponentName();
                                    if (componentName.equals("OOZIE_SERVER")) {
                                        ++oozieServerActiveCount;
                                    }
                                    break;
                                default :
                                    nonStartedState = state;
                            }
                        }
                    } catch (org.apache.ambari.server.ObjectNotFoundException e) {
                    }
                }
                if (oozieServerActiveCount > 0) {
                    return org.apache.ambari.server.state.State.STARTED;
                }
                return nonStartedState == null ? org.apache.ambari.server.state.State.INSTALLED : nonStartedState;
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.controller.utilities.state.OozieServiceCalculatedState.LOG.error("Can't determine service state.", e);
        }
        return org.apache.ambari.server.state.State.UNKNOWN;
    }
}