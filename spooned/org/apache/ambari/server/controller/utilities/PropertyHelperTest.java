package org.apache.ambari.server.controller.utilities;
public class PropertyHelperTest {
    @org.junit.Test
    public void testGetPropertyId() {
        org.junit.Assert.assertEquals("foo", org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("", "foo"));
        org.junit.Assert.assertEquals("foo", org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, "foo"));
        org.junit.Assert.assertEquals("foo", org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, "foo/"));
        org.junit.Assert.assertEquals("cat", org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat", ""));
        org.junit.Assert.assertEquals("cat", org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat", null));
        org.junit.Assert.assertEquals("cat", org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat/", null));
        org.junit.Assert.assertEquals("cat/foo", org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat", "foo"));
        org.junit.Assert.assertEquals("cat/sub/foo", org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat/sub", "foo"));
        org.junit.Assert.assertEquals("cat/sub/foo", org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("cat/sub", "foo/"));
    }

    @org.junit.Test
    public void testGetJMXPropertyIds() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> metrics = org.apache.ambari.server.controller.utilities.PropertyHelper.getJMXPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> componentMetrics = metrics.get("HISTORYSERVER");
        org.junit.Assert.assertNull(componentMetrics);
        componentMetrics = metrics.get("NAMENODE");
        org.junit.Assert.assertNotNull(componentMetrics);
        org.apache.ambari.server.controller.internal.PropertyInfo info = componentMetrics.get("metrics/jvm/memHeapUsedM");
        org.junit.Assert.assertNotNull(info);
        org.junit.Assert.assertEquals("Hadoop:service=NameNode,name=jvm.memHeapUsedM", info.getPropertyId());
    }

    @org.junit.Test
    public void testGetPropertyCategory() {
        java.lang.String propertyId = "metrics/yarn/Queue/$1.replaceAll(\",q(\\d+)=\",\"/\").substring(1)/AppsRunning";
        java.lang.String category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(propertyId);
        org.junit.Assert.assertEquals("metrics/yarn/Queue/$1", category);
        category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(category);
        org.junit.Assert.assertEquals("metrics/yarn/Queue", category);
        category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(category);
        org.junit.Assert.assertEquals("metrics/yarn", category);
        category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(category);
        org.junit.Assert.assertEquals("metrics", category);
        category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(category);
        org.junit.Assert.assertNull(category);
    }

    @org.junit.Test
    public void testGetCategories() {
        java.lang.String propertyId = "metrics/yarn/Queue/$1.replaceAll(\",q(\\d+)=\",\"/\").substring(1)/AppsRunning";
        java.util.Set<java.lang.String> categories = org.apache.ambari.server.controller.utilities.PropertyHelper.getCategories(java.util.Collections.singleton(propertyId));
        org.junit.Assert.assertTrue(categories.contains("metrics/yarn/Queue/$1"));
        org.junit.Assert.assertTrue(categories.contains("metrics/yarn/Queue"));
        org.junit.Assert.assertTrue(categories.contains("metrics/yarn"));
        org.junit.Assert.assertTrue(categories.contains("metrics"));
        java.lang.String propertyId2 = "foo/bar/baz";
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(propertyId);
        propertyIds.add(propertyId2);
        categories = org.apache.ambari.server.controller.utilities.PropertyHelper.getCategories(propertyIds);
        org.junit.Assert.assertTrue(categories.contains("metrics/yarn/Queue/$1"));
        org.junit.Assert.assertTrue(categories.contains("metrics/yarn/Queue"));
        org.junit.Assert.assertTrue(categories.contains("metrics/yarn"));
        org.junit.Assert.assertTrue(categories.contains("metrics"));
        org.junit.Assert.assertTrue(categories.contains("foo/bar"));
        org.junit.Assert.assertTrue(categories.contains("foo"));
    }

    @org.junit.Test
    public void testContainsArguments() {
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.utilities.PropertyHelper.containsArguments("foo"));
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.utilities.PropertyHelper.containsArguments("foo/bar"));
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.utilities.PropertyHelper.containsArguments("foo/bar/baz"));
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.utilities.PropertyHelper.containsArguments("foo/bar/$1/baz"));
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.utilities.PropertyHelper.containsArguments("foo/bar/$1/baz/$2"));
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.utilities.PropertyHelper.containsArguments("$1/foo/bar/$2/baz"));
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.utilities.PropertyHelper.containsArguments("$1/foo/bar/$2/baz/$3"));
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.utilities.PropertyHelper.containsArguments("metrics/yarn/Queue/$1.replaceAll(\",q(\\d+)=\",\"/\").substring(1)"));
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.utilities.PropertyHelper.containsArguments("$X/foo/bar/$Y/baz/$Z"));
    }

    @org.junit.Test
    public void testDuplicatePointInTimeMetrics() {
        java.util.TreeSet<java.lang.String> set = new java.util.TreeSet<>();
        for (org.apache.ambari.server.controller.spi.Resource.Type type : org.apache.ambari.server.controller.spi.Resource.Type.values()) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gids = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(type);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> jids = org.apache.ambari.server.controller.utilities.PropertyHelper.getJMXPropertyIds(type);
            if ((gids != null) && (jids != null)) {
                gids = org.apache.ambari.server.controller.utilities.PropertyHelperTest.normalizeMetricNames(gids);
                jids = org.apache.ambari.server.controller.utilities.PropertyHelperTest.normalizeMetricNames(jids);
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gComponentEntry : gids.entrySet()) {
                    java.lang.String gComponent = gComponentEntry.getKey();
                    java.util.Set<java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gComponentEntries = gComponentEntry.getValue().entrySet();
                    for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> gMetricEntry : gComponentEntries) {
                        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> jMetrics = jids.get(gComponent);
                        if (jMetrics != null) {
                            java.lang.String gMetric = gMetricEntry.getKey();
                            org.apache.ambari.server.controller.internal.PropertyInfo jProperty = jMetrics.get(gMetric);
                            if (jProperty != null) {
                                org.apache.ambari.server.controller.internal.PropertyInfo gProperty = gMetricEntry.getValue();
                                if (gProperty.isPointInTime()) {
                                    java.lang.String s = (((((type + " : ") + gComponent) + " : ") + gMetric) + " : ") + gProperty.getPropertyId();
                                    set.add(s);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (set.size() > 0) {
            java.lang.System.out.println("The following point in time metrics are defined for both JMX and Ganglia.");
            java.lang.System.out.println("The preference is to get point in time metrics from JMX only if possible.");
            java.lang.System.out.println("If the metric can be obtained from JMX then set \"pointInTime\" : false for ");
            java.lang.System.out.println("the metric in the Ganglia properties definition, otherwise remove the metric ");
            java.lang.System.out.println("from the JMX properties definition.\n");
            for (java.lang.String s : set) {
                java.lang.System.out.println(s);
            }
            org.junit.Assert.fail("Found duplicate point in time metrics.");
        }
    }

    @org.junit.Test
    public void testTemporalOnlyMetrics() {
        java.util.TreeSet<java.lang.String> set = new java.util.TreeSet<>();
        for (org.apache.ambari.server.controller.spi.Resource.Type type : org.apache.ambari.server.controller.spi.Resource.Type.values()) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gids = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(type);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> jids = org.apache.ambari.server.controller.utilities.PropertyHelper.getJMXPropertyIds(type);
            if ((gids != null) && (jids != null)) {
                gids = org.apache.ambari.server.controller.utilities.PropertyHelperTest.normalizeMetricNames(gids);
                jids = org.apache.ambari.server.controller.utilities.PropertyHelperTest.normalizeMetricNames(jids);
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gComponentEntry : gids.entrySet()) {
                    java.lang.String gComponent = gComponentEntry.getKey();
                    java.util.Set<java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gComponentEntries = gComponentEntry.getValue().entrySet();
                    for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> gMetricEntry : gComponentEntries) {
                        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> jMetrics = jids.get(gComponent);
                        if (jMetrics != null) {
                            java.lang.String gMetric = gMetricEntry.getKey();
                            org.apache.ambari.server.controller.internal.PropertyInfo gProperty = gMetricEntry.getValue();
                            if (!gProperty.isPointInTime()) {
                                org.apache.ambari.server.controller.internal.PropertyInfo jProperty = jMetrics.get(gMetric);
                                if ((jProperty == null) || (!jProperty.isPointInTime())) {
                                    java.lang.String s = (((((type + " : ") + gComponent) + " : ") + gMetric) + " : ") + gProperty.getPropertyId();
                                    set.add(s);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (set.size() > 0) {
            java.lang.System.out.println("The following metrics are marked as temporal only for Ganglia ");
            java.lang.System.out.println("but are not defined for JMX.");
            java.lang.System.out.println("The preference is to get point in time metrics from JMX if possible.");
            java.lang.System.out.println("If the metric can be obtained from JMX then add it to the JMX properties");
            java.lang.System.out.println("definition, otherwise set set \"pointInTime\" : true for the metric in ");
            java.lang.System.out.println("the Ganglia properties definition.\n");
            for (java.lang.String s : set) {
                java.lang.System.out.println(s);
            }
            org.junit.Assert.fail("Found temporal only metrics.");
        }
    }

    @org.junit.Test
    public void testJMXTemporal() {
        java.util.TreeSet<java.lang.String> set = new java.util.TreeSet<>();
        for (org.apache.ambari.server.controller.spi.Resource.Type type : org.apache.ambari.server.controller.spi.Resource.Type.values()) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> jids = org.apache.ambari.server.controller.utilities.PropertyHelper.getJMXPropertyIds(type);
            if (jids != null) {
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> jComponentEntry : jids.entrySet()) {
                    java.lang.String jComponent = jComponentEntry.getKey();
                    java.util.Set<java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> jComponentEntries = jComponentEntry.getValue().entrySet();
                    for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> jMetricEntry : jComponentEntries) {
                        java.lang.String jMetric = jMetricEntry.getKey();
                        org.apache.ambari.server.controller.internal.PropertyInfo jProperty = jMetricEntry.getValue();
                        if (jProperty.isTemporal()) {
                            java.lang.String s = (((((type + " : ") + jComponent) + " : ") + jMetric) + " : ") + jProperty.getPropertyId();
                            set.add(s);
                        }
                    }
                }
            }
        }
        if (set.size() > 0) {
            java.lang.System.out.println("The following metrics are marked as temporal JMX.");
            java.lang.System.out.println("JMX can provide point in time metrics only.\n");
            for (java.lang.String s : set) {
                java.lang.System.out.println(s);
            }
            org.junit.Assert.fail("Found temporal JMX metrics.");
        }
    }

    @org.junit.Test
    public void testInsertTagIntoMetricName() {
        org.junit.Assert.assertEquals("rpc.rpc.client.CallQueueLength", org.apache.ambari.server.controller.utilities.PropertyHelper.insertTagInToMetricName("client", "rpc.rpc.CallQueueLength", "metrics/rpc/"));
        org.junit.Assert.assertEquals("rpc.rpc.client.CallQueueLength", org.apache.ambari.server.controller.utilities.PropertyHelper.insertTagInToMetricName("client", "rpc.rpc.CallQueueLength", "metrics/rpc/"));
        org.junit.Assert.assertEquals("metrics/rpc/client/CallQueueLen", org.apache.ambari.server.controller.utilities.PropertyHelper.insertTagInToMetricName("client", "metrics/rpc/CallQueueLen", "metrics/rpc/"));
        org.junit.Assert.assertEquals("metrics/rpc/client/CallQueueLen", org.apache.ambari.server.controller.utilities.PropertyHelper.insertTagInToMetricName("client", "metrics/rpc/CallQueueLen", "metrics/rpc/"));
        org.junit.Assert.assertEquals("rpcdetailed.rpcdetailed.client.FsyncAvgTime", org.apache.ambari.server.controller.utilities.PropertyHelper.insertTagInToMetricName("client", "rpcdetailed.rpcdetailed.FsyncAvgTime", "metrics/rpc/"));
        org.junit.Assert.assertEquals("metrics/rpcdetailed/client/fsync_avg_time", org.apache.ambari.server.controller.utilities.PropertyHelper.insertTagInToMetricName("client", "metrics/rpcdetailed/fsync_avg_time", "metrics/rpc/"));
    }

    @org.junit.Test
    public void testProcessRpcMetricDefinition() {
        org.apache.ambari.server.state.stack.Metric metric = new org.apache.ambari.server.state.stack.Metric("rpcdetailed.rpcdetailed.FsyncAvgTime", false, true, false, "unitless");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> replacementMap = org.apache.ambari.server.controller.utilities.PropertyHelper.processRpcMetricDefinition("ganglia", "NAMENODE", "metrics/rpcdetailed/fsync_avg_time", metric);
        org.junit.Assert.assertNotNull(replacementMap);
        org.junit.Assert.assertEquals(3, replacementMap.size());
        org.junit.Assert.assertTrue(replacementMap.containsKey("metrics/rpcdetailed/client/fsync_avg_time"));
        org.junit.Assert.assertTrue(replacementMap.containsKey("metrics/rpcdetailed/datanode/fsync_avg_time"));
        org.junit.Assert.assertTrue(replacementMap.containsKey("metrics/rpcdetailed/healthcheck/fsync_avg_time"));
        org.junit.Assert.assertEquals("rpcdetailed.rpcdetailed.client.FsyncAvgTime", replacementMap.get("metrics/rpcdetailed/client/fsync_avg_time").getName());
        org.junit.Assert.assertEquals("rpcdetailed.rpcdetailed.datanode.FsyncAvgTime", replacementMap.get("metrics/rpcdetailed/datanode/fsync_avg_time").getName());
        org.junit.Assert.assertEquals("rpcdetailed.rpcdetailed.healthcheck.FsyncAvgTime", replacementMap.get("metrics/rpcdetailed/healthcheck/fsync_avg_time").getName());
        metric = new org.apache.ambari.server.state.stack.Metric("Hadoop:service=NameNode,name=RpcDetailedActivity.FsyncAvgTime", true, false, false, "unitless");
        replacementMap = org.apache.ambari.server.controller.utilities.PropertyHelper.processRpcMetricDefinition("jmx", "NAMENODE", "metrics/rpcdetailed/fsync_avg_time", metric);
        org.junit.Assert.assertNotNull(replacementMap);
        org.junit.Assert.assertEquals(3, replacementMap.size());
        org.junit.Assert.assertTrue(replacementMap.containsKey("metrics/rpcdetailed/client/fsync_avg_time"));
        org.junit.Assert.assertTrue(replacementMap.containsKey("metrics/rpcdetailed/datanode/fsync_avg_time"));
        org.junit.Assert.assertTrue(replacementMap.containsKey("metrics/rpcdetailed/healthcheck/fsync_avg_time"));
        org.junit.Assert.assertEquals("Hadoop:service=NameNode,name=RpcDetailedActivity,tag=client.FsyncAvgTime", replacementMap.get("metrics/rpcdetailed/client/fsync_avg_time").getName());
        org.junit.Assert.assertEquals("Hadoop:service=NameNode,name=RpcDetailedActivity,tag=datanode.FsyncAvgTime", replacementMap.get("metrics/rpcdetailed/datanode/fsync_avg_time").getName());
        org.junit.Assert.assertEquals("Hadoop:service=NameNode,name=RpcDetailedActivity,tag=healthcheck.FsyncAvgTime", replacementMap.get("metrics/rpcdetailed/healthcheck/fsync_avg_time").getName());
        metric = new org.apache.ambari.server.state.stack.Metric("Hadoop:service=NameNode,name=RpcActivity.RpcQueueTime_avg_time", true, false, false, "unitless");
        replacementMap = org.apache.ambari.server.controller.utilities.PropertyHelper.processRpcMetricDefinition("jmx", "NAMENODE", "metrics/rpc/RpcQueueTime_avg_time", metric);
        org.junit.Assert.assertNotNull(replacementMap);
        org.junit.Assert.assertEquals(3, replacementMap.size());
        org.junit.Assert.assertTrue(replacementMap.containsKey("metrics/rpc/client/RpcQueueTime_avg_time"));
        org.junit.Assert.assertTrue(replacementMap.containsKey("metrics/rpc/datanode/RpcQueueTime_avg_time"));
        org.junit.Assert.assertTrue(replacementMap.containsKey("metrics/rpc/healthcheck/RpcQueueTime_avg_time"));
        org.junit.Assert.assertEquals("Hadoop:service=NameNode,name=RpcActivity,tag=client.RpcQueueTime_avg_time", replacementMap.get("metrics/rpc/client/RpcQueueTime_avg_time").getName());
        org.junit.Assert.assertEquals("Hadoop:service=NameNode,name=RpcActivity,tag=datanode.RpcQueueTime_avg_time", replacementMap.get("metrics/rpc/datanode/RpcQueueTime_avg_time").getName());
        org.junit.Assert.assertEquals("Hadoop:service=NameNode,name=RpcActivity,tag=healthcheck.RpcQueueTime_avg_time", replacementMap.get("metrics/rpc/healthcheck/RpcQueueTime_avg_time").getName());
    }

    private static java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> normalizeMetricNames(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gids) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> returnMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gComponentEntry : gids.entrySet()) {
            java.lang.String gComponent = gComponentEntry.getKey();
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> newMap = new java.util.HashMap<>();
            java.util.Set<java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gComponentEntries = gComponentEntry.getValue().entrySet();
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> gMetricEntry : gComponentEntries) {
                java.lang.String gMetric = gMetricEntry.getKey();
                org.apache.ambari.server.controller.internal.PropertyInfo propertyInfo = gMetricEntry.getValue();
                newMap.put(gMetric.replaceAll("\\$\\d+(\\.\\S+\\(\\S+\\))*", "*"), propertyInfo);
            }
            returnMap.put(gComponent, newMap);
        }
        return returnMap;
    }
}