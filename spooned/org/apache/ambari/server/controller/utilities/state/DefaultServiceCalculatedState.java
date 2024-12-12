package org.apache.ambari.server.controller.utilities.state;
@org.apache.ambari.server.StaticallyInject
public class DefaultServiceCalculatedState implements org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.class);

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clustersProvider;

    @com.google.inject.Inject
    static com.google.inject.Provider<org.apache.ambari.server.controller.AmbariManagementController> managementControllerProvider;

    protected org.apache.ambari.server.state.State getHostComponentState(org.apache.ambari.server.controller.ServiceComponentHostResponse hostComponent) {
        return org.apache.ambari.server.state.State.valueOf(hostComponent.getLiveState());
    }

    protected org.apache.ambari.server.state.Cluster getCluster(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        if (((org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.clustersProvider != null) && (clusterName != null)) && (clusterName.length() > 0)) {
            org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.clustersProvider.get();
            if (clusters != null) {
                return clusters.getCluster(clusterName);
            }
        }
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.State getState(java.lang.String clusterName, java.lang.String serviceName) {
        try {
            org.apache.ambari.server.state.Cluster cluster = getCluster(clusterName);
            if ((cluster != null) && (org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.managementControllerProvider != null)) {
                org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
                org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.managementControllerProvider.get().getAmbariMetaInfo();
                org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
                org.apache.ambari.server.controller.ServiceComponentHostRequest request = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, serviceName, null, null, null);
                java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> hostComponentResponses = org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.managementControllerProvider.get().getHostComponents(java.util.Collections.singleton(request), true);
                org.apache.ambari.server.state.State masterState = null;
                org.apache.ambari.server.state.State clientState = null;
                org.apache.ambari.server.state.State otherState = null;
                org.apache.ambari.server.state.State maxMMState = null;
                boolean hasDisabled = false;
                boolean hasMaster = false;
                boolean hasOther = false;
                boolean hasClient = false;
                boolean hasMM = false;
                for (org.apache.ambari.server.controller.ServiceComponentHostResponse hostComponentResponse : hostComponentResponses) {
                    try {
                        org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), hostComponentResponse.getServiceName(), hostComponentResponse.getComponentName());
                        org.apache.ambari.server.state.State state = getHostComponentState(hostComponentResponse);
                        boolean isInMaintenance = !org.apache.ambari.server.state.MaintenanceState.OFF.toString().equals(hostComponentResponse.getMaintenanceState());
                        if (state.equals(org.apache.ambari.server.state.State.DISABLED)) {
                            hasDisabled = true;
                        }
                        if (isInMaintenance && (!componentInfo.isClient())) {
                            hasMM = true;
                            if ((maxMMState == null) || (state.ordinal() > maxMMState.ordinal())) {
                                maxMMState = state;
                            }
                        }
                        if (componentInfo.isMaster()) {
                            if (state.equals(org.apache.ambari.server.state.State.STARTED) || (!isInMaintenance)) {
                                hasMaster = true;
                            }
                            if (((!state.equals(org.apache.ambari.server.state.State.STARTED)) && (!isInMaintenance)) && ((masterState == null) || (state.ordinal() > masterState.ordinal()))) {
                                masterState = state;
                            }
                        } else if (componentInfo.isClient()) {
                            hasClient = true;
                            if ((!state.equals(org.apache.ambari.server.state.State.INSTALLED)) && ((clientState == null) || (state.ordinal() > clientState.ordinal()))) {
                                clientState = state;
                            }
                        } else {
                            if (state.equals(org.apache.ambari.server.state.State.STARTED) || (!isInMaintenance)) {
                                hasOther = true;
                            }
                            if (((!state.equals(org.apache.ambari.server.state.State.STARTED)) && (!isInMaintenance)) && ((otherState == null) || (state.ordinal() > otherState.ordinal()))) {
                                otherState = state;
                            }
                        }
                    } catch (org.apache.ambari.server.ObjectNotFoundException e) {
                    }
                }
                return hasMaster ? masterState == null ? org.apache.ambari.server.state.State.STARTED : masterState : hasOther ? otherState == null ? org.apache.ambari.server.state.State.STARTED : otherState : hasClient ? clientState == null ? org.apache.ambari.server.state.State.INSTALLED : clientState : hasDisabled ? org.apache.ambari.server.state.State.DISABLED : hasMM ? maxMMState : org.apache.ambari.server.state.State.UNKNOWN;
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.LOG.error("Can't determine service state.", e);
        }
        return org.apache.ambari.server.state.State.UNKNOWN;
    }
}