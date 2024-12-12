package org.apache.ambari.server.api.resources;
public class SimpleResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    private final java.lang.String singularName;

    private final java.lang.String pluralName;

    public SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type resourceType, java.lang.String singularName, java.lang.String pluralName, org.apache.ambari.server.controller.spi.Resource.Type... subTypes) {
        this(resourceType, singularName, pluralName, subTypes == null ? null : java.util.Arrays.asList(subTypes), null);
    }

    public SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type resourceType, java.lang.String singularName, java.lang.String pluralName, java.util.Collection<org.apache.ambari.server.controller.spi.Resource.Type> subTypes, java.util.Map<org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType, ? extends java.util.Collection<java.lang.String>> directives) {
        super(resourceType, subTypes, directives);
        this.singularName = singularName;
        this.pluralName = pluralName;
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return pluralName;
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return singularName;
    }
}