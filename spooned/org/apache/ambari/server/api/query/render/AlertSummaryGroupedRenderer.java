package org.apache.ambari.server.api.query.render;
import org.codehaus.jackson.annotate.JsonProperty;
public class AlertSummaryGroupedRenderer extends org.apache.ambari.server.api.query.render.AlertSummaryRenderer {
    private static final java.lang.String ALERTS_SUMMARY_GROUP = "alerts_summary_grouped";

    @java.lang.Override
    public org.apache.ambari.server.api.services.Result finalizeResult(org.apache.ambari.server.api.services.Result queryResult) {
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = queryResult.getResultTree();
        java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary> summaries = new java.util.HashMap<>();
        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node : resultTree.getChildren()) {
            org.apache.ambari.server.controller.spi.Resource resource = node.getObject();
            java.lang.Long definitionId = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_ID)));
            java.lang.String definitionName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME)));
            org.apache.ambari.server.state.AlertState state = ((org.apache.ambari.server.state.AlertState) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_STATE)));
            java.lang.Long originalTimestampObject = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ORIGINAL_TIMESTAMP)));
            org.apache.ambari.server.state.MaintenanceState maintenanceState = ((org.apache.ambari.server.state.MaintenanceState) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_MAINTENANCE_STATE)));
            java.lang.String alertText = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_TEXT)));
            org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.updateSummary(summaries, definitionId, definitionName, state, originalTimestampObject, maintenanceState, alertText);
        }
        java.util.Set<java.util.Map.Entry<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary>> entrySet = summaries.entrySet();
        java.util.List<org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary> groupedResources = new java.util.ArrayList<>(entrySet.size());
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary> entry : entrySet) {
            groupedResources.add(entry.getValue());
        }
        org.apache.ambari.server.api.services.Result groupedSummary = new org.apache.ambari.server.api.services.ResultImpl(true);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> summaryTree = groupedSummary.getResultTree();
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Alert);
        summaryTree.addChild(resource, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.ALERTS_SUMMARY_GROUP);
        resource.setProperty(org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.ALERTS_SUMMARY_GROUP, groupedResources);
        return groupedSummary;
    }

    public static void updateSummary(java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary> summaries, java.lang.Long definitionId, java.lang.String definitionName, org.apache.ambari.server.state.AlertState state, java.lang.Long originalTimestampObject, org.apache.ambari.server.state.MaintenanceState maintenanceState, java.lang.String alertText) {
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
        org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary groupSummaryInfo = summaries.get(definitionName);
        if (null == groupSummaryInfo) {
            groupSummaryInfo = new org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary();
            groupSummaryInfo.Id = definitionId;
            groupSummaryInfo.Name = definitionName;
            summaries.put(definitionName, groupSummaryInfo);
        }
        final org.apache.ambari.server.api.query.render.AlertStateValues alertStateValues;
        switch (state) {
            case CRITICAL :
                {
                    alertStateValues = groupSummaryInfo.State.Critical;
                    break;
                }
            case OK :
                {
                    alertStateValues = groupSummaryInfo.State.Ok;
                    break;
                }
            case WARNING :
                {
                    alertStateValues = groupSummaryInfo.State.Warning;
                    break;
                }
            default :
            case UNKNOWN :
                {
                    alertStateValues = groupSummaryInfo.State.Unknown;
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
            alertStateValues.AlertText = alertText;
        }
    }

    public static java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary> generateEmptySummary(java.lang.Long definitionId, java.lang.String definitionName) {
        java.util.Map<java.lang.String, org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary> summaries = new java.util.HashMap<>();
        org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary groupSummaryInfo = new org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary();
        groupSummaryInfo.Id = definitionId;
        groupSummaryInfo.Name = definitionName;
        summaries.put(definitionName, groupSummaryInfo);
        return summaries;
    }

    @java.lang.Override
    protected void addRequiredAlertProperties(java.util.Set<java.lang.String> properties) {
        super.addRequiredAlertProperties(properties);
        properties.add(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID);
        properties.add(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_NAME);
        properties.add(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_MAINTENANCE_STATE);
        properties.add(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_TEXT);
    }

    public static final class AlertDefinitionSummary {
        @org.codehaus.jackson.annotate.JsonProperty("definition_id")
        @com.fasterxml.jackson.annotation.JsonProperty("definition_id")
        public long Id;

        @org.codehaus.jackson.annotate.JsonProperty("definition_name")
        @com.fasterxml.jackson.annotation.JsonProperty("definition_name")
        public java.lang.String Name;

        @org.codehaus.jackson.annotate.JsonProperty("summary")
        @com.fasterxml.jackson.annotation.JsonProperty("summary")
        public final org.apache.ambari.server.api.query.render.AlertStateSummary State = new org.apache.ambari.server.api.query.render.AlertStateSummary();

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary that = ((org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer.AlertDefinitionSummary) (o));
            if (Id != that.Id)
                return false;

            if (Name != null ? !Name.equals(that.Name) : that.Name != null)
                return false;

            return State != null ? State.equals(that.State) : that.State == null;
        }

        @java.lang.Override
        public int hashCode() {
            int result = ((int) (Id ^ (Id >>> 32)));
            result = (31 * result) + (Name != null ? Name.hashCode() : 0);
            result = (31 * result) + (State != null ? State.hashCode() : 0);
            return result;
        }
    }
}