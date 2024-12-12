package org.apache.ambari.server.state;
import com.google.inject.assistedinject.Assisted;
public interface ConfigFactory {
    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.MULTI_SERVICE, comment = "This constructor is only used for test compatibility and should be removed")
    org.apache.ambari.server.state.Config createNew(org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted("type")
    java.lang.String type, @com.google.inject.assistedinject.Assisted("tag")
    java.lang.String tag, java.util.Map<java.lang.String, java.lang.String> map, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> mapAttributes);

    org.apache.ambari.server.state.Config createNew(org.apache.ambari.server.state.StackId stackId, @com.google.inject.assistedinject.Assisted("type")
    java.lang.String type, org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted("tag")
    java.lang.String tag, java.util.Map<java.lang.String, java.lang.String> map, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> mapAttributes, boolean refreshCluster);

    org.apache.ambari.server.state.Config createNew(org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted("type")
    java.lang.String type, @com.google.inject.assistedinject.Assisted("tag")
    java.lang.String tag, java.util.Map<java.lang.String, java.lang.String> map, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> mapAttributes);

    org.apache.ambari.server.state.Config createExisting(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.ClusterConfigEntity entity);

    org.apache.ambari.server.state.Config createReadOnly(@com.google.inject.assistedinject.Assisted("type")
    java.lang.String type, @com.google.inject.assistedinject.Assisted("tag")
    java.lang.String tag, java.util.Map<java.lang.String, java.lang.String> map, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> mapAttributes);
}