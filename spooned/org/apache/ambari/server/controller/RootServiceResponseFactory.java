package org.apache.ambari.server.controller;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_LOCATION;
public class RootServiceResponseFactory extends org.apache.ambari.server.controller.AbstractRootServiceResponseFactory {
    private static final java.lang.String RUNNING_STATE = "RUNNING";

    public static final java.lang.String NOT_APPLICABLE = "NOT_APPLICABLE";

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configs;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController managementController;

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> getRootServices(org.apache.ambari.server.controller.RootServiceRequest request) throws org.apache.ambari.server.ObjectNotFoundException {
        java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> response;
        java.lang.String serviceName = null;
        if (request != null)
            serviceName = request.getServiceName();

        if (serviceName != null) {
            org.apache.ambari.server.controller.RootService service;
            try {
                service = org.apache.ambari.server.controller.RootService.valueOf(serviceName);
            } catch (java.lang.IllegalArgumentException ex) {
                throw new org.apache.ambari.server.ObjectNotFoundException("Root service name: " + serviceName);
            }
            response = java.util.Collections.singleton(new org.apache.ambari.server.controller.RootServiceResponse(service.toString()));
        } else {
            response = new java.util.HashSet<>();
            for (org.apache.ambari.server.controller.RootService service : org.apache.ambari.server.controller.RootService.values())
                response.add(new org.apache.ambari.server.controller.RootServiceResponse(service.toString()));

        }
        return response;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> getRootServiceComponents(org.apache.ambari.server.controller.RootServiceComponentRequest request) throws org.apache.ambari.server.ObjectNotFoundException {
        java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> response = new java.util.HashSet<>();
        java.lang.String serviceName = request.getServiceName();
        java.lang.String componentName = request.getComponentName();
        org.apache.ambari.server.controller.RootService service;
        try {
            service = org.apache.ambari.server.controller.RootService.valueOf(serviceName);
        } catch (java.lang.IllegalArgumentException ex) {
            throw new org.apache.ambari.server.ObjectNotFoundException("Root service name: " + serviceName);
        } catch (java.lang.NullPointerException np) {
            throw new org.apache.ambari.server.ObjectNotFoundException("Root service name: null");
        }
        if (componentName != null) {
            org.apache.ambari.server.controller.RootComponent component;
            try {
                component = org.apache.ambari.server.controller.RootComponent.valueOf(componentName);
                if (!org.apache.commons.lang.ArrayUtils.contains(service.getComponents(), component))
                    throw new org.apache.ambari.server.ObjectNotFoundException((("No component name: " + componentName) + "in service: ") + serviceName);

            } catch (java.lang.IllegalArgumentException ex) {
                throw new org.apache.ambari.server.ObjectNotFoundException("Component name: " + componentName);
            }
            response = java.util.Collections.singleton(new org.apache.ambari.server.controller.RootServiceComponentResponse(serviceName, component.toString(), getComponentVersion(componentName, null), getComponentProperties(componentName)));
        } else {
            for (org.apache.ambari.server.controller.RootComponent component : service.getComponents())
                response.add(new org.apache.ambari.server.controller.RootServiceComponentResponse(serviceName, component.toString(), getComponentVersion(component.name(), null), getComponentProperties(component.name())));

        }
        return response;
    }

    private java.lang.String getComponentVersion(java.lang.String componentName, org.apache.ambari.server.controller.HostResponse host) {
        org.apache.ambari.server.controller.RootComponent component = org.apache.ambari.server.controller.RootComponent.valueOf(componentName);
        java.lang.String componentVersion;
        switch (component) {
            case AMBARI_SERVER :
                componentVersion = ambariMetaInfo.getServerVersion();
                break;
            case AMBARI_AGENT :
                if (host == null)
                    componentVersion = org.apache.ambari.server.controller.RootServiceResponseFactory.NOT_APPLICABLE;
                else
                    componentVersion = host.getAgentVersion().getVersion();

                break;
            default :
                componentVersion = null;
        }
        return componentVersion;
    }

    private java.util.Map<java.lang.String, java.lang.String> getComponentProperties(java.lang.String componentName) {
        java.util.Map<java.lang.String, java.lang.String> response;
        java.util.Set<java.lang.String> propertiesToHideInResponse;
        org.apache.ambari.server.controller.RootComponent component = null;
        if (componentName != null) {
            component = org.apache.ambari.server.controller.RootComponent.valueOf(componentName);
            switch (component) {
                case AMBARI_SERVER :
                    response = configs.getAmbariProperties();
                    response.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_LOCATION, managementController.getJdkResourceUrl());
                    response.put("java.version", java.lang.System.getProperty("java.specification.version"));
                    propertiesToHideInResponse = configs.getPropertiesToBlackList();
                    for (java.lang.String key : propertiesToHideInResponse) {
                        response.remove(key);
                    }
                    break;
                default :
                    response = java.util.Collections.emptyMap();
            }
        } else
            response = java.util.Collections.emptyMap();

        return response;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.RootServiceHostComponentResponse> getRootServiceHostComponent(org.apache.ambari.server.controller.RootServiceHostComponentRequest request, java.util.Set<org.apache.ambari.server.controller.HostResponse> hosts) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.RootServiceHostComponentResponse> response = new java.util.HashSet<>();
        java.lang.String serviceName = request.getServiceName();
        java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> rootServiceComponents = getRootServiceComponents(new org.apache.ambari.server.controller.RootServiceComponentRequest(serviceName, request.getComponentName()));
        for (org.apache.ambari.server.controller.RootServiceComponentResponse component : rootServiceComponents) {
            java.util.Set<org.apache.ambari.server.controller.HostResponse> filteredHosts = new java.util.HashSet<>(hosts);
            if (component.getComponentName().equals(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name())) {
                org.apache.commons.collections.CollectionUtils.filter(filteredHosts, new org.apache.commons.collections.Predicate() {
                    @java.lang.Override
                    public boolean evaluate(java.lang.Object arg0) {
                        org.apache.ambari.server.controller.HostResponse hostResponse = ((org.apache.ambari.server.controller.HostResponse) (arg0));
                        return hostResponse.getHostname().equals(org.apache.ambari.server.utils.StageUtils.getHostName());
                    }
                });
            }
            for (org.apache.ambari.server.controller.HostResponse host : filteredHosts) {
                java.lang.String state;
                if (component.getComponentName().equals(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name())) {
                    state = org.apache.ambari.server.controller.RootServiceResponseFactory.RUNNING_STATE;
                } else {
                    state = host.getHostState().toString();
                }
                java.lang.String componentVersion = getComponentVersion(component.getComponentName(), host);
                response.add(new org.apache.ambari.server.controller.RootServiceHostComponentResponse(serviceName, host.getHostname(), component.getComponentName(), state, componentVersion, component.getProperties()));
            }
        }
        return response;
    }
}