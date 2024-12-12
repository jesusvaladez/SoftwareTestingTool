package org.apache.ambari.server.agent.stomp.dto;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class ClusterConfigs {
    private java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> configurations;

    private java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>>> configurationAttributes;

    public ClusterConfigs(java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> configurations, java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>>> configurationAttributes) {
        this.configurations = configurations;
        this.configurationAttributes = configurationAttributes;
    }

    public java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> getConfigurations() {
        return configurations;
    }

    public java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>>> getConfigurationAttributes() {
        return configurationAttributes;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.agent.stomp.dto.ClusterConfigs that = ((org.apache.ambari.server.agent.stomp.dto.ClusterConfigs) (o));
        if (configurations != null ? !configurations.equals(that.configurations) : that.configurations != null)
            return false;

        return configurationAttributes != null ? configurationAttributes.equals(that.configurationAttributes) : that.configurationAttributes == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (configurations != null) ? configurations.hashCode() : 0;
        result = (31 * result) + (configurationAttributes != null ? configurationAttributes.hashCode() : 0);
        return result;
    }
}