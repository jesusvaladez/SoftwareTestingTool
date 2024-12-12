package org.apache.ambari.server.state.alert;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
@com.google.inject.Singleton
public class AlertHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.alert.AlertHelper.class);

    private static final int INTERVAL_WAIT_FACTOR_DEFAULT = 2;

    private static final java.lang.String STALE_INTERVAL_MULTIPLIER_PARAM_KEY = "stale.interval.multiplier";

    private java.util.concurrent.ConcurrentMap<java.lang.Long, java.util.concurrent.ConcurrentMap<java.lang.Long, java.lang.Long>> staleAlerts = new java.util.concurrent.ConcurrentHashMap<>();

    public int getWaitFactorMultiplier(org.apache.ambari.server.state.alert.AlertDefinition definition) {
        int waitFactor = org.apache.ambari.server.state.alert.AlertHelper.INTERVAL_WAIT_FACTOR_DEFAULT;
        try {
            org.apache.ambari.server.state.alert.ServerSource serverSource = ((org.apache.ambari.server.state.alert.ServerSource) (definition.getSource()));
            java.util.List<org.apache.ambari.server.state.alert.ParameterizedSource.AlertParameter> parameters = serverSource.getParameters();
            for (org.apache.ambari.server.state.alert.ParameterizedSource.AlertParameter parameter : parameters) {
                java.lang.Object value = parameter.getValue();
                if (org.apache.commons.lang.StringUtils.equals(parameter.getName(), org.apache.ambari.server.state.alert.AlertHelper.STALE_INTERVAL_MULTIPLIER_PARAM_KEY)) {
                    waitFactor = getThresholdValue(value, org.apache.ambari.server.state.alert.AlertHelper.INTERVAL_WAIT_FACTOR_DEFAULT);
                }
            }
            if ((waitFactor < 2) || (waitFactor > 10)) {
                org.apache.ambari.server.state.alert.AlertHelper.LOG.warn("The interval multipler of {} is outside the valid range for {} and will be set to 2", waitFactor, definition.getLabel());
                waitFactor = 2;
            }
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.state.alert.AlertHelper.LOG.error("Unable to read the {} parameter for {}", org.apache.ambari.server.state.alert.AlertHelper.STALE_INTERVAL_MULTIPLIER_PARAM_KEY, org.apache.ambari.server.alerts.StaleAlertRunnable.class.getSimpleName(), exception);
        }
        return waitFactor;
    }

    public int getThresholdValue(java.lang.Object value, int defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        if (value instanceof java.lang.Number) {
            return ((java.lang.Number) (value)).intValue();
        }
        if (!(value instanceof java.lang.String)) {
            value = value.toString();
        }
        if (!org.apache.commons.lang.math.NumberUtils.isNumber(((java.lang.String) (value)))) {
            return defaultValue;
        }
        java.lang.Number number = org.apache.commons.lang.math.NumberUtils.createNumber(((java.lang.String) (value)));
        return number.intValue();
    }

    public void addStaleAlerts(java.lang.Long hostId, java.util.List<org.apache.ambari.server.agent.StaleAlert> staleAlertsDefinitionId) {
        staleAlerts.put(hostId, new java.util.concurrent.ConcurrentHashMap<>());
        java.util.concurrent.ConcurrentMap<java.lang.Long, java.lang.Long> hostStaleAlerts = staleAlerts.get(hostId);
        staleAlertsDefinitionId.forEach(s -> hostStaleAlerts.put(s.getId(), s.getTimestamp()));
    }

    public java.util.Map<java.lang.Long, java.lang.Long> getStaleAlerts(java.lang.Long hostId) {
        return staleAlerts.containsKey(hostId) ? new java.util.HashMap<>(staleAlerts.get(hostId)) : java.util.Collections.emptyMap();
    }

    public void clearStaleAlerts(java.lang.Long hostId) {
        staleAlerts.remove(hostId);
    }

    public void clearStaleAlert(java.lang.Long hostId, java.lang.Long definitionId) {
        if (staleAlerts.containsKey(hostId)) {
            staleAlerts.get(hostId).remove(definitionId);
        }
    }

    public void clearStaleAlert(java.lang.Long definitionId) {
        staleAlerts.forEach((k, v) -> v.remove(definitionId));
    }

    public java.util.List<java.lang.Long> getHostIdsByDefinitionId(java.lang.Long definitionId) {
        return staleAlerts.entrySet().stream().filter(e -> e.getValue().containsKey(definitionId)).map(e -> e.getKey()).collect(java.util.stream.Collectors.toList());
    }
}