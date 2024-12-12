package org.apache.ambari.server.api.resources;
public class LdapSyncEventResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public LdapSyncEventResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.LdapSyncEvent);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "ldap_sync_events";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "ldap_sync_event";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return java.util.Collections.emptySet();
    }
}