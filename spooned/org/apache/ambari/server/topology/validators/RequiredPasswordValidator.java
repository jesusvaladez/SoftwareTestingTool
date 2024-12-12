package org.apache.ambari.server.topology.validators;
public class RequiredPasswordValidator implements org.apache.ambari.server.topology.TopologyValidator {
    private java.lang.String defaultPassword;

    public RequiredPasswordValidator() {
    }

    @java.lang.Override
    public void validate(org.apache.ambari.server.topology.ClusterTopology topology) throws org.apache.ambari.server.topology.InvalidTopologyException {
        defaultPassword = topology.getDefaultPassword();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>>> missingPasswords = validateRequiredPasswords(topology);
        if (!missingPasswords.isEmpty()) {
            throw new org.apache.ambari.server.topology.InvalidTopologyException(("Missing required password properties.  Specify a value for these " + "properties in the cluster or host group configurations or include 'default_password' field in request. ") + missingPasswords);
        }
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>>> validateRequiredPasswords(org.apache.ambari.server.topology.ClusterTopology topology) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>>> missingProperties = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> groupEntry : topology.getHostGroupInfo().entrySet()) {
            java.lang.String hostGroupName = groupEntry.getKey();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> groupProperties = groupEntry.getValue().getConfiguration().getFullProperties(3);
            java.util.Collection<java.lang.String> processedServices = new java.util.HashSet<>();
            org.apache.ambari.server.topology.Blueprint blueprint = topology.getBlueprint();
            org.apache.ambari.server.controller.internal.Stack stack = blueprint.getStack();
            org.apache.ambari.server.topology.HostGroup hostGroup = blueprint.getHostGroup(hostGroupName);
            for (java.lang.String component : hostGroup.getComponentNames()) {
                if (component.equals(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name())) {
                    continue;
                }
                java.lang.String serviceName = stack.getServiceForComponent(component);
                if (processedServices.add(serviceName)) {
                    java.util.Collection<org.apache.ambari.server.controller.internal.Stack.ConfigProperty> requiredProperties = stack.getRequiredConfigurationProperties(serviceName, org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD);
                    for (org.apache.ambari.server.controller.internal.Stack.ConfigProperty property : requiredProperties) {
                        java.lang.String category = property.getType();
                        java.lang.String name = property.getName();
                        if (!propertyExists(topology, groupProperties, category, name)) {
                            java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> missingHostGroupPropsMap = missingProperties.get(hostGroupName);
                            if (missingHostGroupPropsMap == null) {
                                missingHostGroupPropsMap = new java.util.HashMap<>();
                                missingProperties.put(hostGroupName, missingHostGroupPropsMap);
                            }
                            java.util.Collection<java.lang.String> missingHostGroupTypeProps = missingHostGroupPropsMap.get(category);
                            if (missingHostGroupTypeProps == null) {
                                missingHostGroupTypeProps = new java.util.HashSet<>();
                                missingHostGroupPropsMap.put(category, missingHostGroupTypeProps);
                            }
                            missingHostGroupTypeProps.add(name);
                        }
                    }
                }
            }
        }
        return missingProperties;
    }

    private boolean propertyExists(org.apache.ambari.server.topology.ClusterTopology topology, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> props, java.lang.String type, java.lang.String property) {
        java.util.Map<java.lang.String, java.lang.String> typeProps = props.get(type);
        return ((typeProps != null) && typeProps.containsKey(property)) || setDefaultPassword(topology, type, property);
    }

    private boolean setDefaultPassword(org.apache.ambari.server.topology.ClusterTopology topology, java.lang.String configType, java.lang.String property) {
        boolean setDefaultPassword = false;
        if ((defaultPassword != null) && (!defaultPassword.trim().isEmpty())) {
            topology.getConfiguration().setProperty(configType, property, defaultPassword);
            setDefaultPassword = true;
        }
        return setDefaultPassword;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.topology.validators.RequiredPasswordValidator that = ((org.apache.ambari.server.topology.validators.RequiredPasswordValidator) (o));
        return defaultPassword == null ? that.defaultPassword == null : defaultPassword.equals(that.defaultPassword);
    }

    @java.lang.Override
    public int hashCode() {
        return defaultPassword != null ? defaultPassword.hashCode() : 0;
    }
}