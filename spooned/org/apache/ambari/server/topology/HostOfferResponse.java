package org.apache.ambari.server.topology;
final class HostOfferResponse {
    public enum Answer {

        ACCEPTED,
        DECLINED_PREDICATE,
        DECLINED_DONE;}

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.HostOfferResponse.class);

    static final org.apache.ambari.server.topology.HostOfferResponse DECLINED_DUE_TO_PREDICATE = new org.apache.ambari.server.topology.HostOfferResponse(org.apache.ambari.server.topology.HostOfferResponse.Answer.DECLINED_PREDICATE);

    static final org.apache.ambari.server.topology.HostOfferResponse DECLINED_DUE_TO_DONE = new org.apache.ambari.server.topology.HostOfferResponse(org.apache.ambari.server.topology.HostOfferResponse.Answer.DECLINED_DONE);

    private final org.apache.ambari.server.topology.HostOfferResponse.Answer answer;

    private final java.lang.String hostGroupName;

    private final long hostRequestId;

    private final java.util.List<org.apache.ambari.server.topology.tasks.TopologyHostTask> tasks;

    static org.apache.ambari.server.topology.HostOfferResponse createAcceptedResponse(long hostRequestId, java.lang.String hostGroupName, java.util.List<org.apache.ambari.server.topology.tasks.TopologyHostTask> tasks) {
        return new org.apache.ambari.server.topology.HostOfferResponse(org.apache.ambari.server.topology.HostOfferResponse.Answer.ACCEPTED, hostRequestId, hostGroupName, tasks);
    }

    private HostOfferResponse(org.apache.ambari.server.topology.HostOfferResponse.Answer answer) {
        this(answer, -1, null, null);
    }

    private HostOfferResponse(org.apache.ambari.server.topology.HostOfferResponse.Answer answer, long hostRequestId, java.lang.String hostGroupName, java.util.List<org.apache.ambari.server.topology.tasks.TopologyHostTask> tasks) {
        this.answer = answer;
        this.hostRequestId = hostRequestId;
        this.hostGroupName = hostGroupName;
        this.tasks = tasks;
    }

    public org.apache.ambari.server.topology.HostOfferResponse.Answer getAnswer() {
        return answer;
    }

    public long getHostRequestId() {
        return hostRequestId;
    }

    public java.lang.String getHostGroupName() {
        return hostGroupName;
    }

    void executeTasks(java.util.concurrent.Executor executor, final java.lang.String hostName, final org.apache.ambari.server.topology.ClusterTopology topology, final org.apache.ambari.server.topology.AmbariContext ambariContext) {
        if (answer != org.apache.ambari.server.topology.HostOfferResponse.Answer.ACCEPTED) {
            org.apache.ambari.server.topology.HostOfferResponse.LOG.warn("Attempted to execute tasks for declined host offer", answer);
        } else {
            executor.execute(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    for (org.apache.ambari.server.topology.tasks.TopologyHostTask task : tasks) {
                        try {
                            org.apache.ambari.server.topology.HostOfferResponse.LOG.info("Running task for accepted host offer for hostname = {}, task = {}", hostName, task.getType());
                            task.run();
                        } catch (java.lang.Exception e) {
                            org.apache.ambari.server.topology.HostRequest hostRequest = task.getHostRequest();
                            org.apache.ambari.server.topology.HostOfferResponse.LOG.error("{} task for host {} failed due to", task.getType(), hostRequest.getHostName(), e);
                            hostRequest.markHostRequestFailed(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, e, ambariContext.getPersistedTopologyState());
                            break;
                        }
                    }
                }
            });
        }
    }
}