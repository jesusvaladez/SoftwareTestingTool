package org.apache.ambari.server.agent;
public abstract class AgentReport<R> {
    private final java.lang.String hostName;

    private final R report;

    public AgentReport(java.lang.String hostName, R report) {
        this.hostName = hostName;
        this.report = report;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public final void process() throws org.apache.ambari.server.AmbariException {
        process(report, hostName);
    }

    protected abstract void process(R report, java.lang.String hostName) throws org.apache.ambari.server.AmbariException;
}