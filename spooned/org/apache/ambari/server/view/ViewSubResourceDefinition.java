package org.apache.ambari.server.view;
public class ViewSubResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    private final org.apache.ambari.server.orm.entities.ViewEntity viewDefinition;

    private final org.apache.ambari.server.view.configuration.ResourceConfig resourceConfiguration;

    java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> definitions;

    public ViewSubResourceDefinition(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition, org.apache.ambari.server.view.configuration.ResourceConfig resourceConfiguration) {
        super(new org.apache.ambari.server.controller.spi.Resource.Type(viewDefinition.getQualifiedResourceTypeName(resourceConfiguration.getName())));
        this.viewDefinition = viewDefinition;
        this.resourceConfiguration = resourceConfiguration;
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return resourceConfiguration.getPluralName();
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return resourceConfiguration.getName();
    }

    @java.lang.Override
    public synchronized java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        if (definitions == null) {
            definitions = new java.util.HashSet<>();
            java.util.List<java.lang.String> subResourceNames = resourceConfiguration.getSubResourceNames();
            if (subResourceNames != null) {
                for (java.lang.String subType : subResourceNames) {
                    org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.valueOf(viewDefinition.getQualifiedResourceTypeName(subType));
                    definitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(type));
                }
            }
        }
        return definitions;
    }

    public org.apache.ambari.server.view.configuration.ResourceConfig getResourceConfiguration() {
        return resourceConfiguration;
    }
}