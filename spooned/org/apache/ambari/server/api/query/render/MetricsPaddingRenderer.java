package org.apache.ambari.server.api.query.render;
public class MetricsPaddingRenderer extends org.apache.ambari.server.api.query.render.DefaultRenderer {
    org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY paddingMethod = org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.ZEROS;

    public MetricsPaddingRenderer(java.lang.String paddingMethod) {
        if (paddingMethod.equalsIgnoreCase("null_padding")) {
            this.paddingMethod = org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.NULLS;
        } else if (paddingMethod.equalsIgnoreCase("no_padding")) {
            this.paddingMethod = org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.NONE;
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> finalizeProperties(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryProperties, boolean isCollection) {
        java.util.Set<java.lang.String> properties = queryProperties.getObject().getProperties();
        if (properties != null) {
            properties.add("params/padding/" + paddingMethod.name());
        }
        return super.finalizeProperties(queryProperties, isCollection);
    }
}