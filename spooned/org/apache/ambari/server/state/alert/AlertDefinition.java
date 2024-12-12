package org.apache.ambari.server.state.alert;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.controller.RootComponent.AMBARI_AGENT;
import static org.apache.ambari.server.controller.RootService.AMBARI;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class AlertDefinition {
    private long clusterId;

    private long definitionId;

    private java.lang.String serviceName = null;

    private java.lang.String componentName = null;

    private java.lang.String name = null;

    private org.apache.ambari.server.state.alert.Scope scope = org.apache.ambari.server.state.alert.Scope.ANY;

    private int interval = 1;

    private boolean enabled = true;

    private org.apache.ambari.server.state.alert.Source source = null;

    private java.lang.String label = null;

    private java.lang.String description = null;

    private java.lang.String uuid = null;

    @com.google.gson.annotations.SerializedName("ignore_host")
    private boolean ignoreHost = false;

    @com.google.gson.annotations.SerializedName("help_url")
    private java.lang.String helpURL = null;

    @com.fasterxml.jackson.annotation.JsonProperty("repeat_tolerance")
    private int repeatTolerance;

    @com.fasterxml.jackson.annotation.JsonProperty("repeat_tolerance_enabled")
    private java.lang.Boolean repeatToleranceEnabled;

    public long getClusterId() {
        return clusterId;
    }

    public void setClusterId(long clusterId) {
        this.clusterId = clusterId;
    }

    public long getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(long definitionId) {
        this.definitionId = definitionId;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String name) {
        serviceName = name;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String name) {
        componentName = name;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String definitionName) {
        name = definitionName;
    }

    public org.apache.ambari.server.state.alert.Scope getScope() {
        return scope;
    }

    public void setScope(org.apache.ambari.server.state.alert.Scope definitionScope) {
        if (null == definitionScope) {
            definitionScope = org.apache.ambari.server.state.alert.Scope.ANY;
        }
        scope = definitionScope;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int definitionInterval) {
        interval = definitionInterval;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean definitionEnabled) {
        enabled = definitionEnabled;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("ignore_host")
    public boolean isHostIgnored() {
        return ignoreHost;
    }

    public void setHostIgnored(boolean definitionHostIgnored) {
        ignoreHost = definitionHostIgnored;
    }

    public org.apache.ambari.server.state.alert.Source getSource() {
        return source;
    }

    public void setSource(org.apache.ambari.server.state.alert.Source definitionSource) {
        source = definitionSource;
    }

    public java.lang.String getLabel() {
        return label;
    }

    public void setLabel(java.lang.String definitionLabel) {
        label = definitionLabel;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("help_url")
    public java.lang.String getHelpURL() {
        return helpURL;
    }

    public void setHelpURL(java.lang.String helpURL) {
        this.helpURL = helpURL;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public void setUuid(java.lang.String definitionUuid) {
        uuid = definitionUuid;
    }

    public java.lang.String getUuid() {
        return uuid;
    }

    public int getRepeatTolerance() {
        return repeatTolerance;
    }

    public void setRepeatTolerance(int repeatTolerance) {
        this.repeatTolerance = repeatTolerance;
    }

    public java.lang.Boolean getRepeatToleranceEnabled() {
        return repeatToleranceEnabled;
    }

    public void setRepeatToleranceEnabled(java.lang.Boolean repeatToleranceEnabled) {
        this.repeatToleranceEnabled = repeatToleranceEnabled;
    }

    public boolean deeplyEquals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        org.apache.ambari.server.state.alert.AlertDefinition other = ((org.apache.ambari.server.state.alert.AlertDefinition) (obj));
        if (componentName == null) {
            if (other.componentName != null) {
                return false;
            }
        } else if (!componentName.equals(other.componentName)) {
            return false;
        }
        if (enabled != other.enabled) {
            return false;
        }
        if (ignoreHost != other.ignoreHost) {
            return false;
        }
        if (interval != other.interval) {
            return false;
        }
        if (label == null) {
            if (other.label != null) {
                return false;
            }
        } else if (!label.equals(other.label)) {
            return false;
        }
        if (!org.apache.commons.lang.StringUtils.equals(helpURL, other.helpURL)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (null == scope) {
            scope = org.apache.ambari.server.state.alert.Scope.ANY;
        }
        if (scope != other.scope) {
            return false;
        }
        if (serviceName == null) {
            if (other.serviceName != null) {
                return false;
            }
        } else if (!serviceName.equals(other.serviceName)) {
            return false;
        }
        if (source == null) {
            if (other.source != null) {
                return false;
            }
        } else if (!source.equals(other.source)) {
            return false;
        }
        return true;
    }

    public org.apache.ambari.server.state.Alert buildAlert(double value, java.util.List<java.lang.Object> args) {
        org.apache.ambari.server.state.alert.Reporting reporting = source.getReporting();
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(name, null, serviceName, componentName, null, reporting.state(value));
        alert.setText(reporting.formatMessage(value, args));
        return alert;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if ((null == obj) || (!obj.getClass().equals(org.apache.ambari.server.state.alert.AlertDefinition.class))) {
            return false;
        }
        return name.equals(((org.apache.ambari.server.state.alert.AlertDefinition) (obj)).name);
    }

    @java.lang.Override
    public int hashCode() {
        return name.hashCode();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return name;
    }

    public java.util.Set<java.lang.String> matchingHosts(org.apache.ambari.server.state.Clusters clusters) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterId);
        if (source.getType() == org.apache.ambari.server.state.alert.SourceType.AGGREGATE) {
            return java.util.Collections.emptySet();
        }
        if (org.apache.ambari.server.controller.RootService.AMBARI.name().equals(serviceName)) {
            return org.apache.ambari.server.controller.RootComponent.AMBARI_AGENT.name().equals(componentName) ? cluster.getHostNames() : java.util.Collections.emptySet();
        }
        java.util.Set<java.lang.String> matchingHosts = new java.util.HashSet<>();
        for (java.lang.String host : cluster.getHostNames()) {
            for (org.apache.ambari.server.state.ServiceComponentHost component : cluster.getServiceComponentHosts(host)) {
                if (belongsTo(component)) {
                    matchingHosts.add(host);
                }
            }
        }
        return matchingHosts;
    }

    private boolean belongsTo(org.apache.ambari.server.state.ServiceComponentHost component) {
        return component.getServiceName().equals(serviceName) && component.getServiceComponentName().equals(componentName);
    }
}