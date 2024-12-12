package org.apache.ambari.server.agent;
import com.google.inject.persist.UnitOfWork;
@com.google.inject.Singleton
public class AgentReportsProcessor {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.AgentReportsProcessor.class);

    private final int poolSize;

    private final java.util.List<java.util.concurrent.ExecutorService> executors;

    public void addAgentReport(org.apache.ambari.server.agent.AgentReport agentReport) {
        int hash = agentReport.getHostName().hashCode();
        hash = (hash == java.lang.Integer.MIN_VALUE) ? 0 : hash;
        int executorNumber = java.lang.Math.abs(hash) % poolSize;
        executors.get(executorNumber).execute(new org.apache.ambari.server.agent.AgentReportsProcessor.AgentReportProcessingTask(agentReport));
    }

    @com.google.inject.Inject
    private com.google.inject.persist.UnitOfWork unitOfWork;

    @com.google.inject.Inject
    public AgentReportsProcessor(org.apache.ambari.server.configuration.Configuration configuration) {
        java.util.concurrent.ThreadFactory threadFactory = new com.google.common.util.concurrent.ThreadFactoryBuilder().setNameFormat("agent-report-processor-%d").build();
        poolSize = configuration.getAgentsReportThreadPoolSize();
        executors = new java.util.ArrayList<>();
        for (int i = 0; i < poolSize; i++) {
            executors.add(java.util.concurrent.Executors.newSingleThreadExecutor(threadFactory));
        }
    }

    private class AgentReportProcessingTask implements java.lang.Runnable {
        private final org.apache.ambari.server.agent.AgentReport agentReport;

        public AgentReportProcessingTask(org.apache.ambari.server.agent.AgentReport agentReport) {
            this.agentReport = agentReport;
        }

        @java.lang.Override
        public void run() {
            try {
                unitOfWork.begin();
                try {
                    agentReport.process();
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.agent.AgentReportsProcessor.LOG.error("Error processing agent reports", e);
                }
            } finally {
                unitOfWork.end();
            }
        }
    }
}