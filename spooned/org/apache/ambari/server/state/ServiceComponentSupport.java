package org.apache.ambari.server.state;
import static org.apache.commons.collections.CollectionUtils.union;
@com.google.inject.Singleton
public class ServiceComponentSupport {
    private final com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> metaInfo;

    @com.google.inject.Inject
    public ServiceComponentSupport(com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> metaInfo) {
        this.metaInfo = metaInfo;
    }

    public java.util.Set<java.lang.String> unsupportedServices(org.apache.ambari.server.state.Cluster cluster, java.lang.String stackName, java.lang.String stackVersion) {
        return cluster.getServices().keySet().stream().filter(serviceName -> !isServiceSupported(serviceName, stackName, stackVersion)).collect(java.util.stream.Collectors.toSet());
    }

    public boolean isServiceSupported(java.lang.String serviceName, java.lang.String stackName, java.lang.String stackVersion) {
        try {
            org.apache.ambari.server.state.ServiceInfo service = metaInfo.get().getServices(stackName, stackVersion).get(serviceName);
            return (service != null) && (!service.isDeleted());
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public java.util.Set<org.apache.ambari.server.state.ServiceComponent> unsupportedComponents(org.apache.ambari.server.state.Cluster cluster, java.lang.String stackName, java.lang.String stackVersion) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.state.ServiceComponent> unsupportedComponents = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            for (org.apache.ambari.server.state.ServiceComponent component : service.getServiceComponents().values()) {
                if (!component.isVersionAdvertised())
                    continue;

                if (!isComponentSupported(service.getName(), component.getName(), stackName, stackVersion)) {
                    unsupportedComponents.add(component);
                }
            }
        }
        return unsupportedComponents;
    }

    private boolean isComponentSupported(java.lang.String serviceName, java.lang.String componentName, java.lang.String stackName, java.lang.String stackVersion) throws org.apache.ambari.server.AmbariException {
        try {
            org.apache.ambari.server.state.ComponentInfo component = metaInfo.get().getComponent(stackName, stackVersion, serviceName, componentName);
            return !component.isDeleted();
        } catch (org.apache.ambari.server.StackAccessException e) {
            return false;
        }
    }

    public java.util.Collection<java.lang.String> allUnsupported(org.apache.ambari.server.state.Cluster cluster, java.lang.String stackName, java.lang.String stackVersion) throws org.apache.ambari.server.AmbariException {
        return org.apache.commons.collections.CollectionUtils.union(unsupportedServices(cluster, stackName, stackVersion), names(unsupportedComponents(cluster, stackName, stackVersion)));
    }

    private java.util.Set<java.lang.String> names(java.util.Set<org.apache.ambari.server.state.ServiceComponent> serviceComponents) {
        return serviceComponents.stream().map(each -> each.getName()).collect(java.util.stream.Collectors.toSet());
    }
}