package org.apache.ambari.server.agent.stomp.dto;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class AlertGroupUpdate {
    @com.fasterxml.jackson.annotation.JsonProperty("cluster_id")
    private java.lang.Long clusterId;

    @com.fasterxml.jackson.annotation.JsonProperty("default")
    private java.lang.Boolean defaultGroup;

    @com.fasterxml.jackson.annotation.JsonProperty("definitions")
    private java.util.List<java.lang.Long> definitions;

    @com.fasterxml.jackson.annotation.JsonProperty("id")
    private java.lang.Long id;

    @com.fasterxml.jackson.annotation.JsonProperty("name")
    private java.lang.String name;

    @com.fasterxml.jackson.annotation.JsonProperty("service_name")
    private java.lang.String serviceName;

    @com.fasterxml.jackson.annotation.JsonProperty("targets")
    private java.util.List<java.lang.Long> targets;

    public AlertGroupUpdate(org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroupEntity) {
        this.clusterId = alertGroupEntity.getClusterId();
        this.defaultGroup = alertGroupEntity.isDefault();
        this.definitions = alertGroupEntity.getAlertDefinitions().stream().map(al -> al.getDefinitionId()).collect(java.util.stream.Collectors.toList());
        this.id = alertGroupEntity.getGroupId();
        this.name = alertGroupEntity.getGroupName();
        this.serviceName = alertGroupEntity.getServiceName();
        this.targets = alertGroupEntity.getAlertTargets().stream().map(at -> at.getTargetId()).collect(java.util.stream.Collectors.toList());
    }

    public AlertGroupUpdate(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.Boolean getDefaultGroup() {
        return defaultGroup;
    }

    public void setDefaultGroup(java.lang.Boolean defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.util.List<java.lang.Long> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(java.util.List<java.lang.Long> definitions) {
        this.definitions = definitions;
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.util.List<java.lang.Long> getTargets() {
        return targets;
    }

    public void setTargets(java.util.List<java.lang.Long> targets) {
        this.targets = targets;
    }
}