package org.apache.ambari.server.topology;
public class BlueprintFactory {
    protected static final java.lang.String BLUEPRINT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Blueprints", "blueprint_name");

    protected static final java.lang.String STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Blueprints", "stack_name");

    protected static final java.lang.String STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Blueprints", "stack_version");

    protected static final java.lang.String HOST_GROUP_PROPERTY_ID = "host_groups";

    protected static final java.lang.String HOST_GROUP_NAME_PROPERTY_ID = "name";

    protected static final java.lang.String HOST_GROUP_CARDINALITY_PROPERTY_ID = "cardinality";

    protected static final java.lang.String COMPONENT_PROPERTY_ID = "components";

    protected static final java.lang.String COMPONENT_NAME_PROPERTY_ID = "name";

    protected static final java.lang.String COMPONENT_PROVISION_ACTION_PROPERTY_ID = "provision_action";

    protected static final java.lang.String CONFIGURATION_PROPERTY_ID = "configurations";

    protected static final java.lang.String PROPERTIES_PROPERTY_ID = "properties";

    protected static final java.lang.String PROPERTIES_ATTRIBUTES_PROPERTY_ID = "properties_attributes";

    protected static final java.lang.String SETTINGS_PROPERTY_ID = "settings";

    private static org.apache.ambari.server.orm.dao.BlueprintDAO blueprintDAO;

    private org.apache.ambari.server.topology.ConfigurationFactory configFactory = new org.apache.ambari.server.topology.ConfigurationFactory();

    private final org.apache.ambari.server.topology.StackFactory stackFactory;

    public BlueprintFactory() {
        this(new org.apache.ambari.server.topology.DefaultStackFactory());
    }

    protected BlueprintFactory(org.apache.ambari.server.topology.StackFactory stackFactory) {
        this.stackFactory = stackFactory;
    }

    public org.apache.ambari.server.topology.Blueprint getBlueprint(java.lang.String blueprintName) throws org.apache.ambari.server.stack.NoSuchStackException {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = org.apache.ambari.server.topology.BlueprintFactory.blueprintDAO.findByName(blueprintName);
        return entity == null ? null : new org.apache.ambari.server.topology.BlueprintImpl(entity);
    }

    @java.lang.SuppressWarnings("unchecked")
    public org.apache.ambari.server.topology.Blueprint createBlueprint(java.util.Map<java.lang.String, java.lang.Object> properties, org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration) throws org.apache.ambari.server.stack.NoSuchStackException {
        java.lang.String name = java.lang.String.valueOf(properties.get(org.apache.ambari.server.topology.BlueprintFactory.BLUEPRINT_NAME_PROPERTY_ID));
        if (name.equals("null") || name.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Blueprint name must be provided");
        }
        org.apache.ambari.server.controller.internal.Stack stack = createStack(properties);
        java.util.Collection<org.apache.ambari.server.topology.HostGroup> hostGroups = processHostGroups(name, stack, properties);
        org.apache.ambari.server.topology.Configuration configuration = configFactory.getConfiguration(((java.util.Collection<java.util.Map<java.lang.String, java.lang.String>>) (properties.get(org.apache.ambari.server.topology.BlueprintFactory.CONFIGURATION_PROPERTY_ID))));
        org.apache.ambari.server.topology.Setting setting = org.apache.ambari.server.topology.SettingFactory.getSetting(((java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>>) (properties.get(org.apache.ambari.server.topology.BlueprintFactory.SETTINGS_PROPERTY_ID))));
        return new org.apache.ambari.server.topology.BlueprintImpl(name, hostGroups, stack, configuration, securityConfiguration, setting);
    }

    protected org.apache.ambari.server.controller.internal.Stack createStack(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.stack.NoSuchStackException {
        java.lang.String stackName = java.lang.String.valueOf(properties.get(org.apache.ambari.server.topology.BlueprintFactory.STACK_NAME_PROPERTY_ID));
        java.lang.String stackVersion = java.lang.String.valueOf(properties.get(org.apache.ambari.server.topology.BlueprintFactory.STACK_VERSION_PROPERTY_ID));
        try {
            return stackFactory.createStack(stackName, stackVersion, org.apache.ambari.server.controller.AmbariServer.getController());
        } catch (org.apache.ambari.server.ObjectNotFoundException e) {
            throw new org.apache.ambari.server.stack.NoSuchStackException(stackName, stackVersion);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new java.lang.RuntimeException("An error occurred parsing the stack information.", e);
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    private java.util.Collection<org.apache.ambari.server.topology.HostGroup> processHostGroups(java.lang.String bpName, org.apache.ambari.server.controller.internal.Stack stack, java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.Object>> hostGroupProps = ((java.util.HashSet<java.util.HashMap<java.lang.String, java.lang.Object>>) (properties.get(org.apache.ambari.server.topology.BlueprintFactory.HOST_GROUP_PROPERTY_ID)));
        if ((hostGroupProps == null) || hostGroupProps.isEmpty()) {
            throw new java.lang.IllegalArgumentException("At least one host group must be specified in a blueprint");
        }
        java.util.Collection<org.apache.ambari.server.topology.HostGroup> hostGroups = new java.util.ArrayList<>();
        for (java.util.HashMap<java.lang.String, java.lang.Object> hostGroupProperties : hostGroupProps) {
            java.lang.String hostGroupName = ((java.lang.String) (hostGroupProperties.get(org.apache.ambari.server.topology.BlueprintFactory.HOST_GROUP_NAME_PROPERTY_ID)));
            if ((hostGroupName == null) || hostGroupName.isEmpty()) {
                throw new java.lang.IllegalArgumentException("Every host group must include a non-null 'name' property");
            }
            java.util.HashSet<java.util.HashMap<java.lang.String, java.lang.String>> componentProps = ((java.util.HashSet<java.util.HashMap<java.lang.String, java.lang.String>>) (hostGroupProperties.get(org.apache.ambari.server.topology.BlueprintFactory.COMPONENT_PROPERTY_ID)));
            java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> configProps = ((java.util.Collection<java.util.Map<java.lang.String, java.lang.String>>) (hostGroupProperties.get(org.apache.ambari.server.topology.BlueprintFactory.CONFIGURATION_PROPERTY_ID)));
            java.util.Collection<org.apache.ambari.server.topology.Component> components = processHostGroupComponents(stack, hostGroupName, componentProps);
            org.apache.ambari.server.topology.Configuration configuration = configFactory.getConfiguration(configProps);
            java.lang.String cardinality = java.lang.String.valueOf(hostGroupProperties.get(org.apache.ambari.server.topology.BlueprintFactory.HOST_GROUP_CARDINALITY_PROPERTY_ID));
            org.apache.ambari.server.topology.HostGroup group = new org.apache.ambari.server.topology.HostGroupImpl(hostGroupName, bpName, stack, components, configuration, cardinality);
            hostGroups.add(group);
        }
        return hostGroups;
    }

    private java.util.Collection<org.apache.ambari.server.topology.Component> processHostGroupComponents(org.apache.ambari.server.controller.internal.Stack stack, java.lang.String groupName, java.util.HashSet<java.util.HashMap<java.lang.String, java.lang.String>> componentProps) {
        if ((componentProps == null) || componentProps.isEmpty()) {
            throw new java.lang.IllegalArgumentException(("Host group '" + groupName) + "' must contain at least one component");
        }
        java.util.Collection<java.lang.String> stackComponentNames = getAllStackComponents(stack);
        java.util.Collection<org.apache.ambari.server.topology.Component> components = new java.util.ArrayList<>();
        for (java.util.HashMap<java.lang.String, java.lang.String> componentProperties : componentProps) {
            java.lang.String componentName = componentProperties.get(org.apache.ambari.server.topology.BlueprintFactory.COMPONENT_NAME_PROPERTY_ID);
            if ((componentName == null) || componentName.isEmpty()) {
                throw new java.lang.IllegalArgumentException(("Host group '" + groupName) + "' contains a component with no 'name' property");
            }
            if (!stackComponentNames.contains(componentName)) {
                throw new java.lang.IllegalArgumentException(((("The component '" + componentName) + "' in host group '") + groupName) + "' is not valid for the specified stack");
            }
            java.lang.String componentProvisionAction = componentProperties.get(org.apache.ambari.server.topology.BlueprintFactory.COMPONENT_PROVISION_ACTION_PROPERTY_ID);
            if (componentProvisionAction != null) {
                components.add(new org.apache.ambari.server.topology.Component(componentName, org.apache.ambari.server.controller.internal.ProvisionAction.valueOf(componentProvisionAction)));
            } else {
                components.add(new org.apache.ambari.server.topology.Component(componentName));
            }
        }
        return components;
    }

    private java.util.Collection<java.lang.String> getAllStackComponents(org.apache.ambari.server.controller.internal.Stack stack) {
        java.util.Collection<java.lang.String> allComponents = new java.util.HashSet<>();
        for (java.util.Collection<java.lang.String> components : stack.getComponents().values()) {
            allComponents.addAll(components);
        }
        allComponents.add(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name());
        return allComponents;
    }

    @com.google.inject.Inject
    public static void init(org.apache.ambari.server.orm.dao.BlueprintDAO dao) {
        org.apache.ambari.server.topology.BlueprintFactory.blueprintDAO = dao;
    }
}