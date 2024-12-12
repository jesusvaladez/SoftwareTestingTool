package org.apache.ambari.server.controller.ivory;
public interface IvoryService {
    void submitFeed(org.apache.ambari.server.controller.ivory.Feed feed);

    org.apache.ambari.server.controller.ivory.Feed getFeed(java.lang.String feedName);

    java.util.List<java.lang.String> getFeedNames();

    void updateFeed(org.apache.ambari.server.controller.ivory.Feed feed);

    void suspendFeed(java.lang.String feedName);

    void resumeFeed(java.lang.String feedName);

    void scheduleFeed(java.lang.String feedName);

    void deleteFeed(java.lang.String feedName);

    void submitCluster(org.apache.ambari.server.controller.ivory.Cluster cluster);

    org.apache.ambari.server.controller.ivory.Cluster getCluster(java.lang.String clusterName);

    java.util.List<java.lang.String> getClusterNames();

    void updateCluster(org.apache.ambari.server.controller.ivory.Cluster cluster);

    void deleteCluster(java.lang.String clusterName);

    java.util.List<org.apache.ambari.server.controller.ivory.Instance> getInstances(java.lang.String feedName);

    void suspendInstance(java.lang.String feedName, java.lang.String instanceId);

    void resumeInstance(java.lang.String feedName, java.lang.String instanceId);

    void killInstance(java.lang.String feedName, java.lang.String instanceId);
}