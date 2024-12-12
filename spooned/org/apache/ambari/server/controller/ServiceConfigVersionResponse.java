package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.apache.ambari.server.StaticallyInject
public class ServiceConfigVersionResponse {
    public static final java.lang.String DEFAULT_CONFIG_GROUP_NAME = "Default";

    public static final java.lang.String DELETED_CONFIG_GROUP_NAME = "Deleted";

    @org.codehaus.jackson.annotate.JsonProperty("cluster_name")
    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private final java.lang.String clusterName;

    @org.codehaus.jackson.annotate.JsonProperty("service_name")
    private final java.lang.String serviceName;

    @org.codehaus.jackson.annotate.JsonProperty("service_config_version")
    private final java.lang.Long version;

    @org.codehaus.jackson.annotate.JsonProperty("createtime")
    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private final java.lang.Long createTime;

    @org.codehaus.jackson.annotate.JsonProperty("group_id")
    private final java.lang.Long groupId;

    @org.codehaus.jackson.annotate.JsonProperty("group_name")
    private final java.lang.String groupName;

    @org.codehaus.jackson.annotate.JsonProperty("user")
    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private final java.lang.String userName;

    @org.codehaus.jackson.annotate.JsonProperty("service_config_version_note")
    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private final java.lang.String note;

    @org.codehaus.jackson.annotate.JsonProperty("stack_id")
    private java.lang.String stackId;

    @org.codehaus.jackson.annotate.JsonProperty("is_current")
    private java.lang.Boolean isCurrent = java.lang.Boolean.FALSE;

    @org.codehaus.jackson.annotate.JsonProperty("is_cluster_compatible")
    private final java.lang.Boolean isCompatibleWithCurrentStack;

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private java.util.List<org.apache.ambari.server.controller.ConfigurationResponse> configurations;

    @org.codehaus.jackson.annotate.JsonProperty("hosts")
    private final java.util.List<java.lang.String> hosts;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    public ServiceConfigVersionResponse(org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity, java.lang.String configGroupName) {
        super();
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = serviceConfigEntity.getClusterEntity();
        clusterName = clusterEntity.getClusterName();
        serviceName = serviceConfigEntity.getServiceName();
        version = serviceConfigEntity.getVersion();
        userName = serviceConfigEntity.getUser();
        createTime = serviceConfigEntity.getCreateTimestamp();
        note = serviceConfigEntity.getNote();
        groupId = (null != serviceConfigEntity.getGroupId()) ? serviceConfigEntity.getGroupId() : -1L;
        groupName = configGroupName;
        hosts = org.apache.ambari.server.controller.ServiceConfigVersionResponse.hostDAO.getHostNamesByHostIds(serviceConfigEntity.getHostIds());
        org.apache.ambari.server.orm.entities.StackEntity serviceConfigStackEntity = serviceConfigEntity.getStack();
        org.apache.ambari.server.orm.entities.StackEntity clusterStackEntity = clusterEntity.getClusterStateEntity().getCurrentStack();
        isCompatibleWithCurrentStack = clusterStackEntity.equals(serviceConfigStackEntity);
        stackId = new org.apache.ambari.server.state.StackId(serviceConfigStackEntity).getStackId();
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_NAME_PROPERTY_ID)
    public java.lang.String getServiceName() {
        return serviceName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_CONFIG_VERSION_PROPERTY_ID)
    public java.lang.Long getVersion() {
        return version;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CREATE_TIME_PROPERTY_ID)
    public java.lang.Long getCreateTime() {
        return createTime;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.USER_PROPERTY_ID)
    public java.lang.String getUserName() {
        return userName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CLUSTER_NAME_PROPERTY_ID)
    public java.lang.String getClusterName() {
        return clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CONFIGURATIONS_PROPERTY_ID)
    public java.util.List<org.apache.ambari.server.controller.ConfigurationResponse> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(java.util.List<org.apache.ambari.server.controller.ConfigurationResponse> configurations) {
        this.configurations = configurations;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_CONFIG_VERSION_NOTE_PROPERTY_ID)
    public java.lang.String getNote() {
        return note;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.HOSTS_PROPERTY_ID)
    public java.util.List<java.lang.String> getHosts() {
        return hosts;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.GROUP_NAME_PROPERTY_ID)
    public java.lang.String getGroupName() {
        return groupName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.GROUP_ID_PROPERTY_ID)
    public java.lang.Long getGroupId() {
        return groupId;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.STACK_ID_PROPERTY_ID)
    public java.lang.String getStackId() {
        return stackId;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.IS_CURRENT_PROPERTY_ID)
    public java.lang.Boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(java.lang.Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.IS_COMPATIBLE_PROPERTY_ID)
    public java.lang.Boolean isCompatibleWithCurrentStack() {
        return isCompatibleWithCurrentStack;
    }

    @java.lang.Override
    public final boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if (!(o instanceof org.apache.ambari.server.controller.ServiceConfigVersionResponse))
            return false;

        org.apache.ambari.server.controller.ServiceConfigVersionResponse that = ((org.apache.ambari.server.controller.ServiceConfigVersionResponse) (o));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(clusterName, that.clusterName).append(serviceName, that.serviceName).append(version, that.version).append(createTime, that.createTime).append(groupId, that.groupId).append(groupName, that.groupName).append(userName, that.userName).append(note, that.note).append(stackId, that.stackId).append(isCurrent, that.isCurrent).append(isCompatibleWithCurrentStack, that.isCompatibleWithCurrentStack).append(configurations, that.configurations).append(hosts, that.hosts).isEquals();
    }

    @java.lang.Override
    public final int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(clusterName).append(serviceName).append(version).append(createTime).append(groupId).append(groupName).append(userName).append(note).append(stackId).append(isCurrent).append(isCompatibleWithCurrentStack).append(configurations).append(hosts).toHashCode();
    }
}