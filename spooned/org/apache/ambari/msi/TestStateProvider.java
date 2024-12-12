package org.apache.ambari.msi;
public class TestStateProvider implements org.apache.ambari.msi.StateProvider {
    private org.apache.ambari.msi.StateProvider.State state = org.apache.ambari.msi.StateProvider.State.Running;

    public void setHealthy(boolean healthy) {
        state = (healthy) ? org.apache.ambari.msi.StateProvider.State.Running : org.apache.ambari.msi.StateProvider.State.Stopped;
    }

    public void setState(org.apache.ambari.msi.StateProvider.State state) {
        this.state = state;
    }

    public org.apache.ambari.msi.StateProvider.State getState() {
        return state;
    }

    @java.lang.Override
    public org.apache.ambari.msi.StateProvider.State getRunningState(java.lang.String hostName, java.lang.String componentName) {
        return state;
    }

    @java.lang.Override
    public org.apache.ambari.msi.StateProvider.Process setRunningState(java.lang.String hostName, java.lang.String componentName, org.apache.ambari.msi.StateProvider.State state) {
        this.state = state;
        return new org.apache.ambari.msi.TestStateProvider.TestProcess();
    }

    private class TestProcess implements org.apache.ambari.msi.StateProvider.Process {
        @java.lang.Override
        public boolean isRunning() {
            return false;
        }

        @java.lang.Override
        public int getExitCode() {
            return 0;
        }

        @java.lang.Override
        public java.lang.String getOutput() {
            return "output";
        }

        @java.lang.Override
        public java.lang.String getError() {
            return "error";
        }
    }
}