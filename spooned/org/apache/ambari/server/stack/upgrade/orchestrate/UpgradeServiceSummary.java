package org.apache.ambari.server.stack.upgrade.orchestrate;
public class UpgradeServiceSummary {
    @com.google.gson.annotations.SerializedName("sourceRepositoryId")
    public long sourceRepositoryId;

    @com.google.gson.annotations.SerializedName("targetRepositoryId")
    public long targetRepositoryId;

    @com.google.gson.annotations.SerializedName("sourceStackId")
    public java.lang.String sourceStackId;

    @com.google.gson.annotations.SerializedName("targetStackId")
    public java.lang.String targetStackId;

    @com.google.gson.annotations.SerializedName("sourceVersion")
    public java.lang.String sourceVersion;

    @com.google.gson.annotations.SerializedName("targetVersion")
    public java.lang.String targetVersion;
}