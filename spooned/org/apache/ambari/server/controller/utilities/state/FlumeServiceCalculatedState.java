package org.apache.ambari.server.controller.utilities.state;
@org.apache.ambari.server.StaticallyInject
public final class FlumeServiceCalculatedState extends org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState implements org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.utilities.state.FlumeServiceCalculatedState.class);

    @java.lang.Override
    public org.apache.ambari.server.state.State getState(java.lang.String clusterName, java.lang.String serviceName) {
        try {
            org.apache.ambari.server.state.Cluster cluster = getCluster(clusterName);
            if ((cluster != null) && (org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.managementControllerProvider != null)) {
                org.apache.ambari.server.controller.ServiceComponentHostRequest request = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, serviceName, null, null, null);
                java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> hostComponentResponses = org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.managementControllerProvider.get().getHostComponents(java.util.Collections.singleton(request), true);
                org.apache.ambari.server.state.State state = org.apache.ambari.server.state.State.UNKNOWN;
                for (org.apache.ambari.server.controller.ServiceComponentHostResponse schr : hostComponentResponses) {
                    org.apache.ambari.server.state.State schState = getHostComponentState(schr);
                    if (schState.ordinal() < state.ordinal()) {
                        state = schState;
                    }
                }
                return state;
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.controller.utilities.state.FlumeServiceCalculatedState.LOG.error("Can't determine service state.", e);
        }
        return org.apache.ambari.server.state.State.UNKNOWN;
    }
}