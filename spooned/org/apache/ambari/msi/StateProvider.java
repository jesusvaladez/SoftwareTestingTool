package org.apache.ambari.msi;
public interface StateProvider {
    public org.apache.ambari.msi.StateProvider.State getRunningState(java.lang.String hostName, java.lang.String componentName);

    public org.apache.ambari.msi.StateProvider.Process setRunningState(java.lang.String hostName, java.lang.String componentName, org.apache.ambari.msi.StateProvider.State state);

    public enum State {

        Stopped,
        Running,
        Paused,
        Unknown;}

    public interface Process {
        public boolean isRunning();

        public int getExitCode();

        public java.lang.String getOutput();

        public java.lang.String getError();
    }
}