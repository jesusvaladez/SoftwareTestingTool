package org.apache.ambari.server.api.query.render;
public class AlertSummaryRenderer extends org.apache.ambari.server.api.query.render.BaseRenderer implements org.apache.ambari.server.api.query.render.Renderer {
    @java.lang.Override
    public org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> finalizeProperties(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree, boolean isCollection) {
        org.apache.ambari.server.api.query.QueryInfo queryInfo = queryTree.getObject();
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, queryInfo.getProperties(), queryTree.getName());
        copyPropertiesToResult(queryTree, resultTree);
        boolean addKeysToEmptyResource = true;
        if ((!isCollection) && isRequestWithNoProperties(queryTree)) {
            addSubResources(queryTree, resultTree);
            addKeysToEmptyResource = false;
        }
        ensureRequiredProperties(resultTree, addKeysToEmptyResource);
        java.util.Set<java.lang.String> properties = resultTree.getObject();
        addRequiredAlertProperties(properties);
        return resultTree;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.ResultPostProcessor getResultPostProcessor(org.apache.ambari.server.api.services.Request request) {
        return new org.apache.ambari.server.api.services.ResultPostProcessorImpl(request);
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.Result finalizeResult(org.apache.ambari.server.api.services.Result queryResult) {
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = queryResult.getResultTree();
        org.apache.ambari.server.api.query.render.AlertStateSummary alertSummary = new org.apache.ambari.server.api.query.render.AlertStateSummary();
        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node : resultTree.getChildren()) {
            org.apache.ambari.server.controller.spi.Resource resource = node.getObject();
            org.apache.ambari.server.state.AlertState state = ((org.apache.ambari.server.state.AlertState) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_STATE)));
            java.lang.Long originalTimestampObject = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ORIGINAL_TIMESTAMP)));
            org.apache.ambari.server.state.MaintenanceState maintenanceState = ((org.apache.ambari.server.state.MaintenanceState) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_MAINTENANCE_STATE)));
            if (null == state) {
                state = org.apache.ambari.server.state.AlertState.UNKNOWN;
            }
            long originalTimestamp = 0;
            if (null != originalTimestampObject) {
                originalTimestamp = originalTimestampObject.longValue();
            }
            boolean isMaintenanceModeEnabled = false;
            if ((null != maintenanceState) && (maintenanceState != org.apache.ambari.server.state.MaintenanceState.OFF)) {
                isMaintenanceModeEnabled = true;
            }
            final org.apache.ambari.server.api.query.render.AlertStateValues alertStateValues;
            switch (state) {
                case CRITICAL :
                    {
                        alertStateValues = alertSummary.Critical;
                        break;
                    }
                case OK :
                    {
                        alertStateValues = alertSummary.Ok;
                        break;
                    }
                case WARNING :
                    {
                        alertStateValues = alertSummary.Warning;
                        break;
                    }
                default :
                case UNKNOWN :
                    {
                        alertStateValues = alertSummary.Unknown;
                        break;
                    }
            }
            if (isMaintenanceModeEnabled) {
                alertStateValues.MaintenanceCount++;
            } else {
                alertStateValues.Count++;
            }
            if (originalTimestamp > alertStateValues.Timestamp) {
                alertStateValues.Timestamp = originalTimestamp;
            }
        }
        org.apache.ambari.server.api.services.Result summary = new org.apache.ambari.server.api.services.ResultImpl(true);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Alert);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> summaryTree = summary.getResultTree();
        summaryTree.addChild(resource, "alerts_summary");
        resource.setProperty("alerts_summary", alertSummary);
        return summary;
    }

    protected void addRequiredAlertProperties(java.util.Set<java.lang.String> properties) {
        properties.add(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_STATE);
        properties.add(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ORIGINAL_TIMESTAMP);
        properties.add(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_MAINTENANCE_STATE);
    }
}