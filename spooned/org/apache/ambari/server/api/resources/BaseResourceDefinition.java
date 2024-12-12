package org.apache.ambari.server.api.resources;
public abstract class BaseResourceDefinition implements org.apache.ambari.server.api.resources.ResourceDefinition {
    private org.apache.ambari.server.controller.spi.Resource.Type m_type;

    private final java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = new java.util.HashSet<>();

    private final java.util.Map<org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType, java.util.Collection<java.lang.String>> directives = new java.util.HashMap<>();

    public BaseResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        this(resourceType, null, null);
    }

    public BaseResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type resourceType, org.apache.ambari.server.controller.spi.Resource.Type... subTypes) {
        this(resourceType, subTypes == null ? null : java.util.Arrays.asList(subTypes), null);
    }

    public BaseResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type resourceType, java.util.Collection<org.apache.ambari.server.controller.spi.Resource.Type> subTypes, java.util.Map<org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType, ? extends java.util.Collection<java.lang.String>> directives) {
        m_type = resourceType;
        if (subTypes != null) {
            for (org.apache.ambari.server.controller.spi.Resource.Type subType : subTypes) {
                subResourceDefinitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(subType));
            }
        }
        initializeDirectives(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.READ, directives);
        initializeDirectives(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.CREATE, directives);
        initializeDirectives(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.UPDATE, directives);
        initializeDirectives(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.DELETE, directives);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Resource.Type getType() {
        return m_type;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return subResourceDefinitions;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
        java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> listProcessors = new java.util.ArrayList<>();
        listProcessors.add(new org.apache.ambari.server.api.resources.BaseResourceDefinition.BaseHrefPostProcessor());
        return listProcessors;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.query.render.Renderer getRenderer(java.lang.String name) {
        if ((name == null) || name.equals("default")) {
            return new org.apache.ambari.server.api.query.render.DefaultRenderer();
        } else if (name.equals("minimal")) {
            return new org.apache.ambari.server.api.query.render.MinimalRenderer();
        } else if ((name.contains("null_padding") || name.contains("no_padding")) || name.contains("zero_padding")) {
            return new org.apache.ambari.server.api.query.render.MetricsPaddingRenderer(name);
        } else {
            throw new java.lang.IllegalArgumentException("Invalid renderer name for resource of type " + m_type);
        }
    }

    org.apache.ambari.server.controller.spi.ClusterController getClusterController() {
        return org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController();
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getReadDirectives() {
        return directives.get(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.READ);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getCreateDirectives() {
        return directives.get(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.CREATE);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getUpdateDirectives() {
        return directives.get(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.UPDATE);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getDeleteDirectives() {
        return directives.get(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.DELETE);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        boolean result = false;
        if (this == o)
            result = true;

        if (o instanceof org.apache.ambari.server.api.resources.BaseResourceDefinition) {
            org.apache.ambari.server.api.resources.BaseResourceDefinition other = ((org.apache.ambari.server.api.resources.BaseResourceDefinition) (o));
            if (m_type == other.m_type)
                result = true;

        }
        return result;
    }

    @java.lang.Override
    public int hashCode() {
        return m_type.hashCode();
    }

    @java.lang.Override
    public boolean isCreatable() {
        return true;
    }

    class BaseHrefPostProcessor implements org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            org.apache.ambari.server.controller.spi.Resource r = resultNode.getObject();
            org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> parent = resultNode.getParent();
            if (parent.getName() != null) {
                int i = href.indexOf("?");
                if (i != (-1)) {
                    href = href.substring(0, i);
                }
                if (!href.endsWith("/")) {
                    href = href + '/';
                }
                org.apache.ambari.server.controller.spi.Schema schema = getClusterController().getSchema(r.getType());
                java.lang.Object id = r.getPropertyValue(schema.getKeyPropertyId(r.getType()));
                java.lang.String hrefIdPart = urlencode(id);
                href = (parent.getStringProperty("isCollection").equals("true")) ? href + hrefIdPart : ((href + parent.getName()) + '/') + hrefIdPart;
            }
            resultNode.setProperty("href", href);
        }

        protected java.lang.String urlencode(java.lang.Object id) {
            if (id == null)
                return "";
            else {
                try {
                    return new org.apache.commons.codec.net.URLCodec().encode(id.toString());
                } catch (org.apache.commons.codec.EncoderException e) {
                    return id.toString();
                }
            }
        }
    }

    private void initializeDirectives(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType type, java.util.Map<org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType, ? extends java.util.Collection<java.lang.String>> directives) {
        java.util.HashSet<java.lang.String> requestDirectives = new java.util.HashSet<>();
        if ((directives != null) && (directives.get(type) != null)) {
            requestDirectives.addAll(directives.get(type));
        }
        this.directives.put(type, requestDirectives);
    }

    public enum DirectiveType {

        CREATE,
        READ,
        UPDATE,
        DELETE;}
}