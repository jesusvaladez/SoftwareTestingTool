package org.apache.ambari.server.controller.internal;
public abstract class BaseClusterRequest implements org.apache.ambari.server.topology.TopologyRequest {
    public static final java.lang.String PROVISION_ACTION_PROPERTY = "provision_action";

    protected final java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfoMap = new java.util.HashMap<>();

    protected org.apache.ambari.server.controller.internal.ProvisionAction provisionAction;

    protected java.lang.Long clusterId;

    protected org.apache.ambari.server.topology.Blueprint blueprint;

    protected org.apache.ambari.server.topology.Configuration configuration;

    protected org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration;

    protected static org.apache.ambari.server.topology.BlueprintFactory blueprintFactory;

    private static final org.apache.ambari.server.api.predicate.QueryLexer queryLexer = new org.apache.ambari.server.api.predicate.QueryLexer();

    private static org.apache.ambari.server.controller.spi.ResourceProvider hostResourceProvider;

    public static void init(org.apache.ambari.server.topology.BlueprintFactory factory) {
        org.apache.ambari.server.controller.internal.BaseClusterRequest.blueprintFactory = factory;
    }

    @java.lang.Override
    public java.lang.Long getClusterId() {
        return clusterId;
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.Blueprint getBlueprint() {
        return blueprint;
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.Configuration getConfiguration() {
        return configuration;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> getHostGroupInfo() {
        return hostGroupInfoMap;
    }

    protected void validateHostPredicateProperties(java.lang.String predicate) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        org.apache.ambari.server.api.predicate.Token[] tokens;
        try {
            tokens = org.apache.ambari.server.controller.internal.BaseClusterRequest.queryLexer.tokens(predicate);
        } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException(java.lang.String.format("The specified host query is invalid: %s", e.getMessage()));
        }
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        for (org.apache.ambari.server.api.predicate.Token token : tokens) {
            if (token.getType() == org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND) {
                propertyIds.add(token.getValue());
            }
        }
        java.util.Set<java.lang.String> invalidProperties = org.apache.ambari.server.controller.internal.BaseClusterRequest.ensureHostProvider().checkPropertyIds(propertyIds);
        if (!invalidProperties.isEmpty()) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException(java.lang.String.format("Invalid Host Predicate.  The following properties are not valid for a host predicate: %s", invalidProperties));
        }
    }

    protected void setBlueprint(org.apache.ambari.server.topology.Blueprint blueprint) {
        this.blueprint = blueprint;
    }

    protected void setConfiguration(org.apache.ambari.server.topology.Configuration configuration) {
        this.configuration = configuration;
    }

    protected org.apache.ambari.server.topology.BlueprintFactory getBlueprintFactory() {
        return org.apache.ambari.server.controller.internal.BaseClusterRequest.blueprintFactory;
    }

    public org.apache.ambari.server.topology.SecurityConfiguration getSecurityConfiguration() {
        return securityConfiguration;
    }

    private static synchronized org.apache.ambari.server.controller.spi.ResourceProvider ensureHostProvider() {
        if (org.apache.ambari.server.controller.internal.BaseClusterRequest.hostResourceProvider == null) {
            org.apache.ambari.server.controller.internal.BaseClusterRequest.hostResourceProvider = org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController().ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        }
        return org.apache.ambari.server.controller.internal.BaseClusterRequest.hostResourceProvider;
    }

    public org.apache.ambari.server.controller.internal.ProvisionAction getProvisionAction() {
        return provisionAction;
    }

    public void setProvisionAction(org.apache.ambari.server.controller.internal.ProvisionAction provisionAction) {
        this.provisionAction = provisionAction;
    }
}