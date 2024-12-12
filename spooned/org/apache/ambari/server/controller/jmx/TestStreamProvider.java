package org.apache.ambari.server.controller.jmx;
public class TestStreamProvider extends org.apache.ambari.server.controller.internal.URLStreamProvider {
    protected static java.util.Map<java.lang.String, java.lang.String> FILE_MAPPING = new java.util.HashMap<>();

    static {
        FILE_MAPPING.put("50070", "hdfs_namenode_jmx.json");
        FILE_MAPPING.put("50075", "hdfs_datanode_jmx.json");
        FILE_MAPPING.put("50030", "mapreduce_jobtracker_jmx.json");
        FILE_MAPPING.put("50060", "mapreduce_tasktracker_jmx.json");
        FILE_MAPPING.put("60010", "hbase_hbasemaster_jmx.json");
        FILE_MAPPING.put("60011", "hbase_hbasemaster_jmx_2.json");
        FILE_MAPPING.put("8088", "resourcemanager_jmx.json");
        FILE_MAPPING.put("8480", "hdfs_journalnode_jmx.json");
        FILE_MAPPING.put("8745", "storm_rest_api_jmx.json");
    }

    private static java.lang.String NN_HASTATE_ONLY_JMX = "hdfs_namenode_jmx_ha_only.json";

    protected final long delay;

    private java.lang.String lastSpec;

    private java.util.List<java.lang.String> specs = new java.util.ArrayList<>();

    private boolean isLastSpecUpdated;

    public TestStreamProvider() {
        super(1000, 1000, org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance());
        delay = 0;
    }

    public TestStreamProvider(long delay) {
        super(1000, 1000, org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance());
        this.delay = delay;
    }

    @java.lang.Override
    public java.io.InputStream readFrom(java.lang.String spec) throws java.io.IOException {
        specs.add(spec);
        if (!isLastSpecUpdated)
            lastSpec = spec;

        isLastSpecUpdated = false;
        java.lang.String filename = null;
        if (spec.endsWith(":50070/jmx?get=Hadoop:service=NameNode,name=FSNamesystem::tag.HAState")) {
            filename = org.apache.ambari.server.controller.jmx.TestStreamProvider.NN_HASTATE_ONLY_JMX;
        } else {
            filename = org.apache.ambari.server.controller.jmx.TestStreamProvider.FILE_MAPPING.get(getPort(spec));
        }
        if (filename == null) {
            throw new java.io.IOException("Can't find JMX source for " + spec);
        }
        if (delay > 0) {
            try {
                java.lang.Thread.sleep(delay);
            } catch (java.lang.InterruptedException e) {
            }
        }
        return java.lang.ClassLoader.getSystemResourceAsStream(filename);
    }

    public java.lang.String getLastSpec() {
        return lastSpec;
    }

    public java.util.List<java.lang.String> getSpecs() {
        return specs;
    }

    private java.lang.String getPort(java.lang.String spec) {
        int colonIndex = spec.indexOf(":", 5);
        int slashIndex = spec.indexOf("/", colonIndex);
        return spec.substring(colonIndex + 1, slashIndex);
    }

    @java.lang.Override
    public java.io.InputStream readFrom(java.lang.String spec, java.lang.String requestMethod, java.lang.String params) throws java.io.IOException {
        lastSpec = (spec + "?") + params;
        isLastSpecUpdated = true;
        return readFrom(spec);
    }
}