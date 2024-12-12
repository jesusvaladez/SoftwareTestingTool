package org.apache.ambari.server.state.configgroup;
import com.google.inject.assistedinject.Assisted;
import javax.annotation.Nullable;
public interface ConfigGroupFactory {
    org.apache.ambari.server.state.configgroup.ConfigGroup createNew(@com.google.inject.assistedinject.Assisted("cluster")
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted("serviceName")
    @javax.annotation.Nullable
    java.lang.String serviceName, @com.google.inject.assistedinject.Assisted("name")
    java.lang.String name, @com.google.inject.assistedinject.Assisted("tag")
    java.lang.String tag, @com.google.inject.assistedinject.Assisted("description")
    java.lang.String description, @com.google.inject.assistedinject.Assisted("configs")
    java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configs, @com.google.inject.assistedinject.Assisted("hosts")
    java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hosts);

    org.apache.ambari.server.state.configgroup.ConfigGroup createExisting(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.ConfigGroupEntity entity);
}