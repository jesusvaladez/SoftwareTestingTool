package org.apache.ambari.server.controller.metrics.ganglia;
public class GangliaMetricTest {
    @org.junit.Test
    public void testSetDatapointsOfPercentValue() {
        java.lang.System.out.println("setDatapoints");
        java.util.List<org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric> listTemporalMetrics = new java.util.ArrayList<>();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric instance = new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric();
        instance.setDs_name("dsName");
        instance.setCluster_name("c1");
        instance.setHost_name("localhost");
        instance.setMetric_name("cpu_wio");
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("111.0", new java.lang.Long(1362440880)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("11.0", new java.lang.Long(1362440881)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("100.0", new java.lang.Long(1362440882)));
        instance.setDatapointsFromList(listTemporalMetrics);
        org.junit.Assert.assertTrue(instance.getDatapoints().length == 2);
    }

    public void testSetDatapointsOfgcTimeMillisValue() {
        java.lang.System.out.println("setDatapoints");
        java.util.List<org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric> listTemporalMetrics = new java.util.ArrayList<>();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric instance = new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric();
        instance.setDs_name("dsName");
        instance.setCluster_name("c1");
        instance.setHost_name("localhost");
        instance.setMetric_name("jvm.metrics.gcTimeMillis");
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("0.0", new java.lang.Long(1)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("0.0", new java.lang.Long(2)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("0.0", new java.lang.Long(3)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("111.0", new java.lang.Long(4)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("11.0", new java.lang.Long(5)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("100.0", new java.lang.Long(6)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("0.0", new java.lang.Long(7)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("11.0", new java.lang.Long(8)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("0.0", new java.lang.Long(9)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("0.0", new java.lang.Long(10)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("0.0", new java.lang.Long(11)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("0.0", new java.lang.Long(12)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("11.0", new java.lang.Long(13)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("100.0", new java.lang.Long(14)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("0.0", new java.lang.Long(15)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("0.0", new java.lang.Long(16)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("0.0", new java.lang.Long(17)));
        listTemporalMetrics.add(new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("0.0", new java.lang.Long(18)));
        instance.setDatapointsFromList(listTemporalMetrics);
        java.lang.System.out.println(instance);
        org.junit.Assert.assertTrue(instance.getDatapoints().length == 11);
    }

    @org.junit.Test
    public void testTemporalMetricFineValue() {
        java.lang.System.out.println("GangliaMetric.TemporalMetric");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric tm;
        tm = new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("100", new java.lang.Long(1362440880));
        org.junit.Assert.assertTrue("GangliaMetric.TemporalMetric is valid", tm.isValid());
    }

    @org.junit.Test
    public void testTemporalMetricIsNaNValue() {
        java.lang.System.out.println("GangliaMetric.TemporalMetric");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric tm;
        tm = new org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric("any string", new java.lang.Long(1362440880));
        org.junit.Assert.assertFalse("GangliaMetric.TemporalMetric is invalid", tm.isValid());
    }
}