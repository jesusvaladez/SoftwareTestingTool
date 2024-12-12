package org.apache.ambari.server.stack.upgrade.orchestrate;
public class UpgradeSummary {
    @com.google.gson.annotations.SerializedName("direction")
    public org.apache.ambari.server.stack.upgrade.Direction direction;

    @com.google.gson.annotations.SerializedName("type")
    public org.apache.ambari.spi.upgrade.UpgradeType type;

    @com.google.gson.annotations.SerializedName("orchestration")
    public org.apache.ambari.spi.RepositoryType orchestration;

    @com.google.gson.annotations.SerializedName("isRevert")
    public boolean isRevert = false;

    @com.google.gson.annotations.SerializedName("downgradeAllowed")
    public boolean isDowngradeAllowed = true;

    @com.google.gson.annotations.SerializedName("services")
    public java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeServiceSummary> services;

    @com.google.gson.annotations.SerializedName("associatedRepositoryId")
    public long associatedRepositoryId;

    @com.google.gson.annotations.SerializedName("associatedStackId")
    public java.lang.String associatedStackId;

    @com.google.gson.annotations.SerializedName("associatedVersion")
    public java.lang.String associatedVersion;

    @com.google.gson.annotations.SerializedName("isSwitchBits")
    public boolean isSwitchBits = false;
}