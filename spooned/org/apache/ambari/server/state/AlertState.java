package org.apache.ambari.server.state;
public enum AlertState {

    OK(0),
    WARNING(2),
    CRITICAL(3),
    UNKNOWN(1),
    SKIPPED(4);
    public static final java.util.Set<org.apache.ambari.server.state.AlertState> RECALCULATE_AGGREGATE_ALERT_STATES = com.google.common.collect.Sets.immutableEnumSet(org.apache.ambari.server.state.AlertState.CRITICAL, org.apache.ambari.server.state.AlertState.WARNING);

    private final int intValue;

    public int getIntValue() {
        return intValue;
    }

    AlertState(int i) {
        this.intValue = i;
    }
}