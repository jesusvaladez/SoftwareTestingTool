package org.apache.ambari.server.alerts;
import static org.apache.ambari.server.state.AlertState.CRITICAL;
import static org.apache.ambari.server.state.AlertState.OK;
import static org.apache.ambari.server.state.AlertState.UNKNOWN;
import static org.apache.ambari.server.state.AlertState.WARNING;
public class ThresholdTest {
    @org.junit.Test
    public void testBetweenOkAndWarnIsOk_dirUp() throws java.lang.Exception {
        org.apache.ambari.server.alerts.Threshold threshold = new org.apache.ambari.server.alerts.Threshold(10.0, 20.0, 30.0);
        assertState(threshold, org.apache.ambari.server.state.AlertState.OK, 10, 15, 19);
    }

    @org.junit.Test
    public void testBetweenWarnAndCritIsWarn_dirUp() throws java.lang.Exception {
        org.apache.ambari.server.alerts.Threshold threshold = new org.apache.ambari.server.alerts.Threshold(10.0, 20.0, 30.0);
        assertState(threshold, org.apache.ambari.server.state.AlertState.WARNING, 20, 25, 29);
    }

    @org.junit.Test
    public void testAboveCritIsCrit_dirUp() throws java.lang.Exception {
        org.apache.ambari.server.alerts.Threshold threshold = new org.apache.ambari.server.alerts.Threshold(10.0, 20.0, 30.0);
        assertState(threshold, org.apache.ambari.server.state.AlertState.CRITICAL, 30, 40, 99999);
    }

    @org.junit.Test
    public void testBelowOkIsUnknown_dirUp() throws java.lang.Exception {
        org.apache.ambari.server.alerts.Threshold threshold = new org.apache.ambari.server.alerts.Threshold(10.0, 20, 30);
        assertState(threshold, org.apache.ambari.server.state.AlertState.UNKNOWN, 9, 2, -99999);
    }

    @org.junit.Test
    public void testBelowCritIsCrit_dirDown() throws java.lang.Exception {
        org.apache.ambari.server.alerts.Threshold threshold = new org.apache.ambari.server.alerts.Threshold(40.0, 30.0, 20.0);
        assertState(threshold, org.apache.ambari.server.state.AlertState.CRITICAL, 20, 15, 2, -99999);
    }

    @org.junit.Test
    public void testBetweenWarnAndCritIsWarn_dirDown() throws java.lang.Exception {
        org.apache.ambari.server.alerts.Threshold threshold = new org.apache.ambari.server.alerts.Threshold(40.0, 30.0, 20.0);
        assertState(threshold, org.apache.ambari.server.state.AlertState.WARNING, 30, 25, 21);
    }

    @org.junit.Test
    public void testBetweenOkAndWarnIsOk_dirDown() throws java.lang.Exception {
        org.apache.ambari.server.alerts.Threshold threshold = new org.apache.ambari.server.alerts.Threshold(40.0, 30.0, 20.0);
        assertState(threshold, org.apache.ambari.server.state.AlertState.OK, 40, 35, 31);
    }

    @org.junit.Test
    public void testAboveOkIsUnknown_dirDown() throws java.lang.Exception {
        org.apache.ambari.server.alerts.Threshold threshold = new org.apache.ambari.server.alerts.Threshold(40.0, 30.0, 20.0);
        assertState(threshold, org.apache.ambari.server.state.AlertState.UNKNOWN, 41, 50, 9999);
    }

    @org.junit.Test
    public void testOkIsOptional() throws java.lang.Exception {
        org.apache.ambari.server.alerts.Threshold threshold = new org.apache.ambari.server.alerts.Threshold(null, 20.0, 30.0);
        assertState(threshold, org.apache.ambari.server.state.AlertState.OK, 10, 15, 19);
    }

    private void assertState(org.apache.ambari.server.alerts.Threshold threshold, org.apache.ambari.server.state.AlertState expectedState, int... values) {
        for (int value : values) {
            org.junit.Assert.assertThat(expectedState, org.hamcrest.core.Is.is(threshold.state(value)));
        }
    }
}