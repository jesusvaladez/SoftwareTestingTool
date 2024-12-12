package org.apache.ambari.server.alerts;
import javax.annotation.Nullable;
public class Threshold {
    private final java.lang.Double okValue;

    private final double warnValue;

    private final double critValue;

    public Threshold(@javax.annotation.Nullable
    java.lang.Double okValue, double warnValue, double critValue) {
        this.okValue = okValue;
        this.warnValue = warnValue;
        this.critValue = critValue;
    }

    public org.apache.ambari.server.state.AlertState state(double value) {
        return directionUp() ? stateWhenDirectionUp(value) : stateWhenDirectionDown(value);
    }

    private boolean directionUp() {
        return critValue >= warnValue;
    }

    private org.apache.ambari.server.state.AlertState stateWhenDirectionUp(double value) {
        if (value >= critValue) {
            return org.apache.ambari.server.state.AlertState.CRITICAL;
        }
        if (value >= warnValue) {
            return org.apache.ambari.server.state.AlertState.WARNING;
        }
        if ((okValue == null) || (value >= okValue)) {
            return org.apache.ambari.server.state.AlertState.OK;
        }
        return org.apache.ambari.server.state.AlertState.UNKNOWN;
    }

    private org.apache.ambari.server.state.AlertState stateWhenDirectionDown(double value) {
        if (value <= critValue) {
            return org.apache.ambari.server.state.AlertState.CRITICAL;
        }
        if (value <= warnValue) {
            return org.apache.ambari.server.state.AlertState.WARNING;
        }
        if ((okValue == null) || (value <= okValue)) {
            return org.apache.ambari.server.state.AlertState.OK;
        }
        return org.apache.ambari.server.state.AlertState.UNKNOWN;
    }
}