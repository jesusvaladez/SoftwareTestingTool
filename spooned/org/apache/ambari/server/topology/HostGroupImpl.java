package org.apache.ambari.server.topology;
public class HostGroupImpl implements org.apache.ambari.server.topology.HostGroup {
    private java.lang.String name;

    private java.lang.String blueprintName;

    private java.util.Map<java.lang.String, org.apache.ambari.server.topology.Component> components = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentsForService = new java.util.HashMap<>();

    private org.apache.ambari.server.topology.Configuration configuration = null;

    private boolean containsMasterComponent = false;

    private org.apache.ambari.server.controller.internal.Stack stack;

    private java.lang.String cardinality = "NOT SPECIFIED";

    public HostGroupImpl(org.apache.ambari.server.orm.entities.HostGroupEntity entity, java.lang.String blueprintName, org.apache.ambari.server.controller.internal.Stack stack) {
        this.name = entity.getName();
        this.cardinality = entity.getCardinality();
        this.blueprintName = blueprintName;
        this.stack = stack;
        parseComponents(entity);
        parseConfigurations(entity);
    }

    public HostGroupImpl(java.lang.String name, java.lang.String bpName, org.apache.ambari.server.controller.internal.Stack stack, java.util.Collection<org.apache.ambari.server.topology.Component> components, org.apache.ambari.server.topology.Configuration configuration, java.lang.String cardinality) {
        this.name = name;
        this.blueprintName = bpName;
        this.stack = stack;
        for (org.apache.ambari.server.topology.Component component : components) {
            addComponent(component.getName(), component.getProvisionAction());
        }
        this.configuration = configuration;
        if ((cardinality != null) && (!cardinality.equals("null"))) {
            this.cardinality = cardinality;
        }
    }

    @java.lang.Override
    public java.lang.String getName() {
        return name;
    }

    @java.lang.Override
    public java.lang.String getFullyQualifiedName() {
        return java.lang.String.format("%s:%s", blueprintName, getName());
    }

    public static java.lang.String formatAbsoluteName(java.lang.String bpName, java.lang.String hgName) {
        return java.lang.String.format("%s:%s", bpName, hgName);
    }

    @java.lang.Override
    public java.util.Collection<org.apache.ambari.server.topology.Component> getComponents() {
        return components.values();
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getComponentNames() {
        return components.keySet();
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getComponentNames(org.apache.ambari.server.controller.internal.ProvisionAction provisionAction) {
        java.util.Set<java.lang.String> setOfComponentNames = new java.util.HashSet<>();
        for (java.lang.String componentName : components.keySet()) {
            org.apache.ambari.server.topology.Component component = components.get(componentName);
            if ((component.getProvisionAction() != null) && (component.getProvisionAction() == provisionAction)) {
                setOfComponentNames.add(componentName);
            }
        }
        return setOfComponentNames;
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getServices() {
        return componentsForService.keySet();
    }

    @java.lang.Override
    public boolean addComponent(java.lang.String component) {
        return this.addComponent(component, null);
    }

    @java.lang.Override
    public boolean addComponent(java.lang.String component, org.apache.ambari.server.controller.internal.ProvisionAction provisionAction) {
        boolean added;
        if (!components.containsKey(component)) {
            components.put(component, new org.apache.ambari.server.topology.Component(component, provisionAction));
            added = true;
        } else {
            added = false;
        }
        if (stack.isMasterComponent(component)) {
            containsMasterComponent = true;
        }
        if (added) {
            java.lang.String service = stack.getServiceForComponent(component);
            if (service != null) {
                java.util.Set<java.lang.String> serviceComponents = componentsForService.get(service);
                if (serviceComponents == null) {
                    serviceComponents = new java.util.HashSet<>();
                    componentsForService.put(service, serviceComponents);
                }
                serviceComponents.add(component);
            }
        }
        return added;
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getComponents(java.lang.String service) {
        return componentsForService.containsKey(service) ? new java.util.HashSet<>(componentsForService.get(service)) : java.util.Collections.emptySet();
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.Configuration getConfiguration() {
        return configuration;
    }

    @java.lang.Override
    public java.lang.String getBlueprintName() {
        return blueprintName;
    }

    @java.lang.Override
    public boolean containsMasterComponent() {
        return containsMasterComponent;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.Stack getStack() {
        return stack;
    }

    @java.lang.Override
    public java.lang.String getCardinality() {
        return cardinality;
    }

    private void parseComponents(org.apache.ambari.server.orm.entities.HostGroupEntity entity) {
        for (org.apache.ambari.server.orm.entities.HostGroupComponentEntity componentEntity : entity.getComponents()) {
            if (componentEntity.getProvisionAction() != null) {
                addComponent(componentEntity.getName(), org.apache.ambari.server.controller.internal.ProvisionAction.valueOf(componentEntity.getProvisionAction()));
            } else {
                addComponent(componentEntity.getName());
            }
        }
    }

    private void parseConfigurations(org.apache.ambari.server.orm.entities.HostGroupEntity entity) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> config = new java.util.HashMap<>();
        com.google.gson.Gson jsonSerializer = new com.google.gson.Gson();
        for (org.apache.ambari.server.orm.entities.HostGroupConfigEntity configEntity : entity.getConfigurations()) {
            java.lang.String type = configEntity.getType();
            java.util.Map<java.lang.String, java.lang.String> typeProperties = config.get(type);
            if (typeProperties == null) {
                typeProperties = new java.util.HashMap<>();
                config.put(type, typeProperties);
            }
            java.util.Map<java.lang.String, java.lang.String> propertyMap = jsonSerializer.<java.util.Map<java.lang.String, java.lang.String>>fromJson(configEntity.getConfigData(), java.util.Map.class);
            if (propertyMap != null) {
                typeProperties.putAll(propertyMap);
            }
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = new java.util.HashMap<>();
        configuration = new org.apache.ambari.server.topology.Configuration(config, attributes);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return name;
    }
}