package org.apache.ambari.server.controller.metrics.ganglia;
public class TestStreamProvider extends org.apache.ambari.server.controller.internal.URLStreamProvider {
    protected java.lang.String fileName;

    private java.lang.String lastSpec;

    protected java.util.Set<java.lang.String> specs = new java.util.HashSet<>();

    private boolean isLastSpecUpdated;

    public TestStreamProvider(java.lang.String fileName) {
        super(1000, 1000, org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance());
        this.fileName = fileName;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.metrics.ganglia.TestHttpUrlConnection processURL(java.lang.String spec, java.lang.String requestMethod, java.lang.String body, java.util.Map<java.lang.String, java.util.List<java.lang.String>> headers) throws java.io.IOException {
        return new org.apache.ambari.server.controller.metrics.ganglia.TestHttpUrlConnection(readFrom(spec));
    }

    @java.lang.Override
    public java.io.InputStream readFrom(java.lang.String spec) throws java.io.IOException {
        if (!isLastSpecUpdated) {
            lastSpec = spec;
        }
        isLastSpecUpdated = false;
        specs.add(spec);
        return java.lang.ClassLoader.getSystemResourceAsStream(fileName);
    }

    public java.lang.String getLastSpec() {
        return lastSpec;
    }

    public java.util.Set<java.lang.String> getAllSpecs() {
        return specs;
    }

    @java.lang.Override
    public java.io.InputStream readFrom(java.lang.String spec, java.lang.String requestMethod, java.lang.String params) throws java.io.IOException {
        lastSpec = (spec + "?") + params;
        isLastSpecUpdated = true;
        return readFrom(spec);
    }
}