package org.apache.ambari.server.stack.upgrade.orchestrate;
public class UpgradeGroupHolder {
    boolean processingGroup;

    public java.lang.String name;

    public java.lang.String title;

    public java.lang.Class<? extends org.apache.ambari.server.stack.upgrade.Grouping> groupClass;

    public boolean allowRetry = true;

    public boolean skippable = false;

    public boolean supportsAutoSkipOnFailure = true;

    public java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> items = new java.util.ArrayList<>();

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("UpgradeGroupHolder{");
        buffer.append("name=").append(name);
        buffer.append(", title=").append(title);
        buffer.append(", allowRetry=").append(allowRetry);
        buffer.append(", skippable=").append(skippable);
        buffer.append("}");
        return buffer.toString();
    }
}