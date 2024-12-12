package org.apache.ambari.server.topology;
import io.swagger.annotations.ApiModelProperty;
public interface Configurable {
    java.lang.String CONFIGURATIONS = "configurations";

    @com.fasterxml.jackson.annotation.JsonIgnore
    @org.apache.ambari.annotations.ApiIgnore
    void setConfiguration(org.apache.ambari.server.topology.Configuration configuration);

    @com.fasterxml.jackson.annotation.JsonIgnore
    @org.apache.ambari.annotations.ApiIgnore
    org.apache.ambari.server.topology.Configuration getConfiguration();

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.Configurable.CONFIGURATIONS)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.Configurable.CONFIGURATIONS)
    default void setConfigs(java.util.Collection<? extends java.util.Map<java.lang.String, ?>> configs) {
        setConfiguration(org.apache.ambari.server.topology.ConfigurableHelper.parseConfigs(configs));
    }

    @com.fasterxml.jackson.annotation.JsonProperty(org.apache.ambari.server.topology.Configurable.CONFIGURATIONS)
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.topology.Configurable.CONFIGURATIONS)
    default java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, ?>>> getConfigs() {
        return org.apache.ambari.server.topology.ConfigurableHelper.convertConfigToMap(getConfiguration());
    }
}