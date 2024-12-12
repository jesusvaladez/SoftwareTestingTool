package org.apache.ambari.server.controller.internal;
public class Extension {
    private java.lang.String name;

    private java.lang.String version;

    private java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceComponents = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.lang.String> componentService = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.DependencyInfo>> dependencies = new java.util.HashMap<>();

    private java.util.Map<org.apache.ambari.server.state.DependencyInfo, java.lang.String> dependencyConditionalServiceMap = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.lang.String> dbDependencyInfo = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.lang.String> cardinalityRequirements = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.AutoDeployInfo> componentAutoDeployInfo = new java.util.HashMap<>();

    private final org.apache.ambari.server.controller.AmbariManagementController controller;

    public Extension(org.apache.ambari.server.orm.entities.ExtensionEntity extension, org.apache.ambari.server.controller.AmbariManagementController ambariManagementController) throws org.apache.ambari.server.AmbariException {
        this(extension.getExtensionName(), extension.getExtensionVersion(), ambariManagementController);
    }

    public Extension(java.lang.String name, java.lang.String version, org.apache.ambari.server.controller.AmbariManagementController controller) throws org.apache.ambari.server.AmbariException {
        this.name = name;
        this.version = version;
        this.controller = controller;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getVersion() {
        return version;
    }

    java.util.Map<org.apache.ambari.server.state.DependencyInfo, java.lang.String> getDependencyConditionalServiceMap() {
        return dependencyConditionalServiceMap;
    }

    public java.util.Collection<java.lang.String> getServices() {
        return serviceComponents.keySet();
    }

    public java.util.Collection<java.lang.String> getComponents(java.lang.String service) {
        return serviceComponents.get(service);
    }

    public java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> getComponents() {
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceComponents = new java.util.HashMap<>();
        for (java.lang.String service : getServices()) {
            java.util.Collection<java.lang.String> components = new java.util.HashSet<>();
            components.addAll(getComponents(service));
            serviceComponents.put(service, components);
        }
        return serviceComponents;
    }

    public org.apache.ambari.server.state.ComponentInfo getComponentInfo(java.lang.String component) {
        org.apache.ambari.server.state.ComponentInfo componentInfo = null;
        java.lang.String service = getServiceForComponent(component);
        if (service != null) {
            try {
                componentInfo = controller.getAmbariMetaInfo().getComponent(getName(), getVersion(), service, component);
            } catch (org.apache.ambari.server.AmbariException e) {
            }
        }
        return componentInfo;
    }

    public java.lang.String getServiceForComponent(java.lang.String component) {
        return componentService.get(component);
    }

    public java.util.Collection<java.lang.String> getServicesForComponents(java.util.Collection<java.lang.String> components) {
        java.util.Set<java.lang.String> services = new java.util.HashSet<>();
        for (java.lang.String component : components) {
            services.add(getServiceForComponent(component));
        }
        return services;
    }

    public java.util.Collection<org.apache.ambari.server.state.DependencyInfo> getDependenciesForComponent(java.lang.String component) {
        return dependencies.containsKey(component) ? dependencies.get(component) : java.util.Collections.emptySet();
    }

    public java.lang.String getConditionalServiceForDependency(org.apache.ambari.server.state.DependencyInfo dependency) {
        return dependencyConditionalServiceMap.get(dependency);
    }

    public java.lang.String getExternalComponentConfig(java.lang.String component) {
        return dbDependencyInfo.get(component);
    }

    public org.apache.ambari.server.topology.Cardinality getCardinality(java.lang.String component) {
        return new org.apache.ambari.server.topology.Cardinality(cardinalityRequirements.get(component));
    }

    public org.apache.ambari.server.state.AutoDeployInfo getAutoDeployInfo(java.lang.String component) {
        return componentAutoDeployInfo.get(component);
    }
}