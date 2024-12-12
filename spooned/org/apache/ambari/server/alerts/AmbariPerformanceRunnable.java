package org.apache.ambari.server.alerts;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
public class AmbariPerformanceRunnable extends org.apache.ambari.server.alerts.AlertRunnable {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.alerts.AmbariPerformanceRunnable.class);

    private static final java.lang.String PERFORMANCE_OVERVIEW_TEMPLATE = ("Performance Overview:" + java.lang.System.lineSeparator()) + "{0}";

    private static final java.lang.String PERFORMANCE_AREA_TEMPLATE = "  {0}: {1}ms ({2})";

    private static final java.lang.String PERFORMANCE_AREA_FAILURE_TEMPLATE = "  Unable to execute performance alert area {0}: ({1})";

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertDefinitionFactory m_definitionFactory;

    enum PerformanceArea {

        REQUEST_BY_STATUS("Database Access (Request By Status)", "request.by.status.warning.threshold", 3000, "request.by.status.critical.threshold", 5000) {
            @java.lang.Override
            void execute(org.apache.ambari.server.alerts.AmbariPerformanceRunnable runnable, org.apache.ambari.server.state.Cluster cluster) throws java.lang.Exception {
                runnable.m_actionManager.getRequestsByStatus(org.apache.ambari.server.actionmanager.RequestStatus.IN_PROGRESS, org.apache.ambari.server.api.services.BaseRequest.DEFAULT_PAGE_SIZE, false);
            }
        },
        HRC_SUMMARY_STATUS("Database Access (Task Status Aggregation)", "task.status.aggregation.warning.threshold", 3000, "task.status.aggregation.critical.threshold", 5000) {
            @java.lang.Override
            void execute(org.apache.ambari.server.alerts.AmbariPerformanceRunnable runnable, org.apache.ambari.server.state.Cluster cluster) throws java.lang.Exception {
                java.util.List<java.lang.Long> requestIds = runnable.m_requestDAO.findAllRequestIds(org.apache.ambari.server.api.services.BaseRequest.DEFAULT_PAGE_SIZE, false);
                for (long requestId : requestIds) {
                    runnable.m_hostRoleCommandDAO.findAggregateCounts(requestId);
                }
            }
        },
        REST_API_GET_CLUSTER("REST API (Cluster)", "rest.api.cluster.warning.threshold", 5000, "rest.api.cluster.critical.threshold", 7000) {
            @java.lang.Override
            void execute(org.apache.ambari.server.alerts.AmbariPerformanceRunnable runnable, org.apache.ambari.server.state.Cluster cluster) throws java.lang.Exception {
                org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken authenticationToken = new org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken("admin");
                authenticationToken.setAuthenticated(true);
                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
                mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, cluster.getClusterName());
                org.apache.ambari.server.controller.spi.ClusterController clusterController = org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController();
                org.apache.ambari.server.api.query.Query query = new org.apache.ambari.server.api.query.QueryImpl(mapIds, new org.apache.ambari.server.api.resources.ClusterResourceDefinition(), clusterController);
                query.setRenderer(new org.apache.ambari.server.api.query.render.MinimalRenderer());
                query.addProperty("Clusters/desired_configs", null);
                query.addProperty("Clusters/credential_store_properties", null);
                query.addProperty("Clusters/desired_service_config_versions", null);
                query.addProperty("Clusters/health_report", null);
                query.addProperty("Clusters/total_hosts", null);
                query.addProperty("alerts_summary", null);
                query.addProperty("alerts_summary_hosts", null);
                query.execute();
            }
        };
        private final java.lang.String m_label;

        private final java.lang.String m_warningParameter;

        private final int m_defaultWarningThreshold;

        private final java.lang.String m_criticalParameter;

        private final int m_defaultCriticalThreshold;

        PerformanceArea(java.lang.String label, java.lang.String warningParameter, int defaultWarningThreshold, java.lang.String criticalParameter, int defaultCriticalThreshold) {
            m_label = label;
            m_warningParameter = warningParameter;
            m_defaultWarningThreshold = defaultWarningThreshold;
            m_criticalParameter = criticalParameter;
            m_defaultCriticalThreshold = defaultCriticalThreshold;
        }

        abstract void execute(org.apache.ambari.server.alerts.AmbariPerformanceRunnable runnable, org.apache.ambari.server.state.Cluster cluster) throws java.lang.Exception;
    }

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RequestDAO m_requestDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO m_hostRoleCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.ActionManager m_actionManager;

    public AmbariPerformanceRunnable(java.lang.String definitionName) {
        super(definitionName);
    }

    @java.lang.Override
    java.util.List<org.apache.ambari.server.state.Alert> execute(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.alert.AlertDefinition definition = m_definitionFactory.coerce(entity);
        org.apache.ambari.server.state.alert.ServerSource serverSource = ((org.apache.ambari.server.state.alert.ServerSource) (definition.getSource()));
        java.util.List<org.apache.ambari.server.state.alert.ParameterizedSource.AlertParameter> parameters = serverSource.getParameters();
        java.util.List<java.lang.String> results = new java.util.ArrayList<>();
        org.apache.ambari.server.state.AlertState alertState = org.apache.ambari.server.state.AlertState.OK;
        for (org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PerformanceArea performanceArea : org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PerformanceArea.values()) {
            org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PerformanceResult performanceResult;
            try {
                long startTime = java.lang.System.currentTimeMillis();
                performanceArea.execute(this, cluster);
                long totalTime = java.lang.System.currentTimeMillis() - startTime;
                performanceResult = calculatePerformanceResult(performanceArea, totalTime, parameters);
            } catch (java.lang.Exception exception) {
                java.lang.String result = java.text.MessageFormat.format(org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PERFORMANCE_AREA_FAILURE_TEMPLATE, performanceArea, org.apache.ambari.server.state.AlertState.UNKNOWN);
                org.apache.ambari.server.alerts.AmbariPerformanceRunnable.LOG.error(result, exception);
                performanceResult = new org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PerformanceResult(result, org.apache.ambari.server.state.AlertState.UNKNOWN);
            }
            java.lang.String result = performanceResult.getResult();
            org.apache.ambari.server.state.AlertState resultAlertState = performanceResult.getAlertState();
            results.add(result);
            switch (resultAlertState) {
                case CRITICAL :
                    alertState = org.apache.ambari.server.state.AlertState.CRITICAL;
                    break;
                case OK :
                    break;
                case SKIPPED :
                    break;
                case UNKNOWN :
                    if (alertState == org.apache.ambari.server.state.AlertState.OK) {
                        alertState = org.apache.ambari.server.state.AlertState.UNKNOWN;
                    }
                    break;
                case WARNING :
                    if (alertState != org.apache.ambari.server.state.AlertState.CRITICAL) {
                        alertState = org.apache.ambari.server.state.AlertState.WARNING;
                    }
                    break;
                default :
                    break;
            }
        }
        java.lang.String allResults = org.apache.commons.lang.StringUtils.join(results, java.lang.System.lineSeparator());
        java.lang.String overview = java.text.MessageFormat.format(org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PERFORMANCE_OVERVIEW_TEMPLATE, allResults);
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(entity.getDefinitionName(), null, entity.getServiceName(), entity.getComponentName(), null, alertState);
        alert.setLabel(entity.getLabel());
        alert.setText(overview);
        alert.setTimestamp(java.lang.System.currentTimeMillis());
        alert.setClusterId(cluster.getClusterId());
        return java.util.Collections.singletonList(alert);
    }

    org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PerformanceResult calculatePerformanceResult(org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PerformanceArea area, long time, java.util.List<org.apache.ambari.server.state.alert.ParameterizedSource.AlertParameter> parameters) {
        org.apache.ambari.server.state.AlertState alertState = org.apache.ambari.server.state.AlertState.OK;
        int warningThreshold = area.m_defaultWarningThreshold;
        int criticalThreshold = area.m_defaultCriticalThreshold;
        for (org.apache.ambari.server.state.alert.ParameterizedSource.AlertParameter parameter : parameters) {
            java.lang.Object value = parameter.getValue();
            if (org.apache.commons.lang.StringUtils.equals(parameter.getName(), area.m_warningParameter)) {
                warningThreshold = alertHelper.getThresholdValue(value, warningThreshold);
            }
            if (org.apache.commons.lang.StringUtils.equals(parameter.getName(), area.m_criticalParameter)) {
                criticalThreshold = alertHelper.getThresholdValue(value, criticalThreshold);
            }
        }
        if ((time >= warningThreshold) && (time < criticalThreshold)) {
            alertState = org.apache.ambari.server.state.AlertState.WARNING;
        }
        if (time >= criticalThreshold) {
            alertState = org.apache.ambari.server.state.AlertState.CRITICAL;
        }
        java.lang.String resultLabel = java.text.MessageFormat.format(org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PERFORMANCE_AREA_TEMPLATE, area.m_label, time, alertState);
        return new org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PerformanceResult(resultLabel, alertState);
    }

    private static final class PerformanceResult {
        private final java.lang.String m_result;

        private final org.apache.ambari.server.state.AlertState m_alertState;

        private PerformanceResult(java.lang.String result, org.apache.ambari.server.state.AlertState alertState) {
            m_result = result;
            m_alertState = alertState;
        }

        public java.lang.String getResult() {
            return m_result;
        }

        public org.apache.ambari.server.state.AlertState getAlertState() {
            return m_alertState;
        }
    }
}