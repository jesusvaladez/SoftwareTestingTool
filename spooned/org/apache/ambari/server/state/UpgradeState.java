package org.apache.ambari.server.state;
public enum UpgradeState {

    NONE,
    COMPLETE,
    IN_PROGRESS,
    FAILED,
    VERSION_MISMATCH;
    public static final java.util.EnumSet<org.apache.ambari.server.state.UpgradeState> VERSION_NON_ADVERTISED_STATES = java.util.EnumSet.of(org.apache.ambari.server.state.UpgradeState.IN_PROGRESS, org.apache.ambari.server.state.UpgradeState.FAILED, org.apache.ambari.server.state.UpgradeState.VERSION_MISMATCH);
}