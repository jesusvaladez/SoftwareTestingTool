package org.apache.ambari.server.topology;
public interface HostGroup {
    java.util.regex.Pattern HOSTGROUP_REGEX = java.util.regex.Pattern.compile("%HOSTGROUP::(\\S+?)%");

    java.lang.String getName();

    java.lang.String getBlueprintName();

    java.lang.String getFullyQualifiedName();

    java.util.Collection<org.apache.ambari.server.topology.Component> getComponents();

    java.util.Collection<java.lang.String> getComponentNames();

    java.util.Collection<java.lang.String> getComponentNames(org.apache.ambari.server.controller.internal.ProvisionAction provisionAction);

    java.util.Collection<java.lang.String> getComponents(java.lang.String service);

    boolean addComponent(java.lang.String component);

    boolean addComponent(java.lang.String component, org.apache.ambari.server.controller.internal.ProvisionAction provisionAction);

    boolean containsMasterComponent();

    java.util.Collection<java.lang.String> getServices();

    org.apache.ambari.server.topology.Configuration getConfiguration();

    org.apache.ambari.server.controller.internal.Stack getStack();

    java.lang.String getCardinality();
}