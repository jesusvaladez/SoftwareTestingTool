package org.apache.ambari.server.topology;
public class RepositorySetting {
    public static final java.lang.String OVERRIDE_STRATEGY = "override_strategy";

    public static final java.lang.String OVERRIDE_STRATEGY_ALWAYS_APPLY = "ALWAYS_APPLY";

    public static final java.lang.String OVERRIDE_STRATEGY_APPLY_WHEN_MISSING = "APPLY_WHEN_MISSING";

    public static final java.lang.String OPERATING_SYSTEM = "operating_system";

    public static final java.lang.String REPO_ID = "repo_id";

    public static final java.lang.String BASE_URL = "base_url";

    private java.lang.String overrideStrategy;

    private java.lang.String operatingSystem;

    private java.lang.String repoId;

    private java.lang.String baseUrl;

    public java.lang.String getOverrideStrategy() {
        return overrideStrategy;
    }

    public void setOverrideStrategy(java.lang.String overrideStrategy) {
        this.overrideStrategy = overrideStrategy;
    }

    public java.lang.String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(java.lang.String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public java.lang.String getRepoId() {
        return repoId;
    }

    public void setRepoId(java.lang.String repoId) {
        this.repoId = repoId;
    }

    public java.lang.String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(java.lang.String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder strBldr = new java.lang.StringBuilder();
        strBldr.append(org.apache.ambari.server.topology.RepositorySetting.OVERRIDE_STRATEGY);
        strBldr.append(": ");
        strBldr.append(overrideStrategy);
        strBldr.append(org.apache.ambari.server.topology.RepositorySetting.OPERATING_SYSTEM);
        strBldr.append(": ");
        strBldr.append(operatingSystem);
        strBldr.append(org.apache.ambari.server.topology.RepositorySetting.REPO_ID);
        strBldr.append(": ");
        strBldr.append(repoId);
        strBldr.append(org.apache.ambari.server.topology.RepositorySetting.BASE_URL);
        strBldr.append(": ");
        strBldr.append(baseUrl);
        return strBldr.toString();
    }
}