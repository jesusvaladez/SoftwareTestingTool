package org.apache.ambari.server.topology.validators;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
public class UnitValidatedProperty {
    public static final java.util.Set<org.apache.ambari.server.topology.validators.UnitValidatedProperty> ALL = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.topology.validators.UnitValidatedProperty>builder().add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("HDFS", "hadoop-env", "namenode_heapsize")).add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("HDFS", "hadoop-env", "namenode_opt_newsize")).add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("HDFS", "hadoop-env", "namenode_opt_maxnewsize")).add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("HDFS", "hadoop-env", "namenode_opt_permsize")).add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("HDFS", "hadoop-env", "namenode_opt_maxpermsize")).add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("HDFS", "hadoop-env", "dtnode_heapsize")).add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("MAPREDUCE2", "mapred-env", "jtnode_opt_newsize")).add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("MAPREDUCE2", "mapred-env", "jtnode_opt_maxnewsize")).add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("MAPREDUCE2", "mapred-env", "jtnode_heapsize")).add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("HBASE", "hbase-env", "hbase_master_heapsize")).add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("HBASE", "hbase-env", "hbase_regionserver_heapsize")).add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("OOZIE", "oozie-env", "oozie_heapsize")).add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("OOZIE", "oozie-env", "oozie_permsize")).add(new org.apache.ambari.server.topology.validators.UnitValidatedProperty("ZOOKEEPER", "zookeeper-env", "zk_server_heapsize")).build();

    private final java.lang.String configType;

    private final java.lang.String serviceName;

    private final java.lang.String propertyName;

    public UnitValidatedProperty(java.lang.String serviceName, java.lang.String configType, java.lang.String propertyName) {
        this.configType = configType;
        this.serviceName = serviceName;
        this.propertyName = propertyName;
    }

    public boolean hasTypeAndName(java.lang.String configType, java.lang.String propertyName) {
        return configType.equals(this.getConfigType()) && propertyName.equals(this.getPropertyName());
    }

    public java.lang.String getConfigType() {
        return configType;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public java.lang.String getPropertyName() {
        return propertyName;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.topology.validators.UnitValidatedProperty that = ((org.apache.ambari.server.topology.validators.UnitValidatedProperty) (o));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(configType, that.configType).append(serviceName, that.serviceName).append(propertyName, that.propertyName).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(configType).append(serviceName).append(propertyName).toHashCode();
    }
}