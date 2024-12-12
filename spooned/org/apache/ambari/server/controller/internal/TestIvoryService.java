package org.apache.ambari.server.controller.internal;
public class TestIvoryService implements org.apache.ambari.server.controller.ivory.IvoryService {
    private int instanceCounter = 0;

    private final java.util.Map<java.lang.String, org.apache.ambari.server.controller.ivory.Feed> feeds = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, org.apache.ambari.server.controller.ivory.Cluster> clusters = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.ivory.Instance>> instanceMap = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, java.lang.String> suspendedFeedStatusMap = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, java.lang.String> suspendedInstanceStatusMap = new java.util.HashMap<>();

    public TestIvoryService(java.util.Map<java.lang.String, org.apache.ambari.server.controller.ivory.Feed> feeds, java.util.Map<java.lang.String, org.apache.ambari.server.controller.ivory.Cluster> clusters, java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.ivory.Instance>> instanceMap) {
        if (feeds != null) {
            this.feeds.putAll(feeds);
        }
        if (clusters != null) {
            this.clusters.putAll(clusters);
        }
        if (instanceMap != null) {
            this.instanceMap.putAll(instanceMap);
        }
    }

    @java.lang.Override
    public void submitFeed(org.apache.ambari.server.controller.ivory.Feed feed) {
        feeds.put(feed.getName(), feed);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ivory.Feed getFeed(java.lang.String feedName) {
        return feeds.get(feedName);
    }

    @java.lang.Override
    public java.util.List<java.lang.String> getFeedNames() {
        return new java.util.LinkedList<>(feeds.keySet());
    }

    @java.lang.Override
    public void updateFeed(org.apache.ambari.server.controller.ivory.Feed feed) {
        feeds.put(feed.getName(), feed);
    }

    @java.lang.Override
    public void suspendFeed(java.lang.String feedName) {
        suspendedFeedStatusMap.put(feedName, setFeedStatus(feedName, "SUSPENDED"));
    }

    @java.lang.Override
    public void resumeFeed(java.lang.String feedName) {
        java.lang.String suspendedStatus = suspendedFeedStatusMap.get(feedName);
        if (suspendedStatus != null) {
            setFeedStatus(feedName, suspendedStatus);
            suspendedFeedStatusMap.remove(feedName);
        }
    }

    @java.lang.Override
    public void scheduleFeed(java.lang.String feedName) {
        setFeedStatus(feedName, "SCHEDULED");
        addDummyInstance(feedName);
    }

    @java.lang.Override
    public void deleteFeed(java.lang.String feedName) {
        feeds.remove(feedName);
    }

    @java.lang.Override
    public void submitCluster(org.apache.ambari.server.controller.ivory.Cluster cluster) {
        clusters.put(cluster.getName(), cluster);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ivory.Cluster getCluster(java.lang.String clusterName) {
        return clusters.get(clusterName);
    }

    @java.lang.Override
    public java.util.List<java.lang.String> getClusterNames() {
        return new java.util.LinkedList<>(clusters.keySet());
    }

    @java.lang.Override
    public void updateCluster(org.apache.ambari.server.controller.ivory.Cluster cluster) {
        clusters.put(cluster.getName(), cluster);
    }

    @java.lang.Override
    public void deleteCluster(java.lang.String clusterName) {
        clusters.remove(clusterName);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.controller.ivory.Instance> getInstances(java.lang.String feedName) {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.ivory.Instance> instances = instanceMap.get(feedName);
        if (instances != null) {
            return new java.util.LinkedList<>(instances.values());
        }
        return java.util.Collections.emptyList();
    }

    @java.lang.Override
    public void suspendInstance(java.lang.String feedName, java.lang.String instanceId) {
        java.lang.String instanceKey = (feedName + "/") + instanceId;
        suspendedInstanceStatusMap.put(instanceKey, setInstanceStatus(feedName, instanceId, "SUSPENDED"));
    }

    @java.lang.Override
    public void resumeInstance(java.lang.String feedName, java.lang.String instanceId) {
        java.lang.String instanceKey = (feedName + "/") + instanceId;
        java.lang.String suspendedStatus = suspendedInstanceStatusMap.get(instanceKey);
        if (suspendedStatus != null) {
            setInstanceStatus(feedName, instanceId, suspendedStatus);
            suspendedInstanceStatusMap.remove(instanceKey);
        }
    }

    @java.lang.Override
    public void killInstance(java.lang.String feedName, java.lang.String instanceId) {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.ivory.Instance> instances = instanceMap.get(feedName);
        if (instances != null) {
            instances.remove(instanceId);
        }
    }

    private java.lang.String setFeedStatus(java.lang.String feedName, java.lang.String status) {
        java.lang.String currentStatus = null;
        org.apache.ambari.server.controller.ivory.Feed feed = feeds.get(feedName);
        if (feed != null) {
            currentStatus = feed.getStatus();
            if (!currentStatus.equals(status)) {
                feed = new org.apache.ambari.server.controller.ivory.Feed(feed.getName(), feed.getDescription(), status, feed.getSchedule(), feed.getSourceClusterName(), feed.getSourceClusterStart(), feed.getSourceClusterEnd(), feed.getSourceClusterLimit(), feed.getSourceClusterAction(), feed.getTargetClusterName(), feed.getTargetClusterStart(), feed.getTargetClusterEnd(), feed.getTargetClusterLimit(), feed.getTargetClusterAction(), feed.getProperties());
                feeds.put(feed.getName(), feed);
            }
        }
        return currentStatus;
    }

    private java.lang.String setInstanceStatus(java.lang.String feedName, java.lang.String instanceId, java.lang.String status) {
        java.lang.String currentStatus = null;
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.ivory.Instance> instances = instanceMap.get(feedName);
        if (instances != null) {
            org.apache.ambari.server.controller.ivory.Instance instance = instances.get(instanceId);
            if (instance != null) {
                currentStatus = instance.getStatus();
                if (!currentStatus.equals(status)) {
                    instance = new org.apache.ambari.server.controller.ivory.Instance(instance.getFeedName(), instance.getId(), status, instance.getStartTime(), instance.getEndTime(), instance.getDetails(), instance.getLog());
                    instances.put(instance.getId(), instance);
                }
            }
        }
        return currentStatus;
    }

    private void addDummyInstance(java.lang.String feedName) {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.ivory.Instance> instances = instanceMap.get(feedName);
        if (instances == null) {
            instances = new java.util.HashMap<>();
            instanceMap.put(feedName, instances);
        }
        java.lang.String id = "Instance" + (instanceCounter++);
        org.apache.ambari.server.controller.ivory.Instance instance = new org.apache.ambari.server.controller.ivory.Instance(feedName, id, "RUNNING", "2011-01-01T00:00Z", "2011-01-01T00:10Z", "details", "stdout");
        instances.put(id, instance);
    }
}