package org.apache.ambari.server.agent;
public class LocalAgentSimulator implements java.lang.Runnable {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.HeartBeatHandler.class);

    private java.lang.Thread agentThread = null;

    private volatile boolean shouldRun = true;

    private final org.apache.ambari.server.agent.HeartBeatHandler handler;

    private long sleepTime = 500;

    private long responseId = 1;

    private java.lang.String hostname = "localhost";

    private java.lang.String agentVersion = "1.3.0";

    public LocalAgentSimulator(org.apache.ambari.server.agent.HeartBeatHandler hbh) {
        this.handler = hbh;
    }

    public LocalAgentSimulator(org.apache.ambari.server.agent.HeartBeatHandler hbh, java.lang.String hostname, long sleepTime) {
        this(hbh);
        this.sleepTime = sleepTime;
        this.hostname = hostname;
    }

    private volatile int numberOfHeartbeats = -1;

    private int currentHeartbeatCount = 0;

    private volatile boolean shouldSendRegistration = true;

    private volatile org.apache.ambari.server.agent.Register nextRegistration = null;

    private volatile org.apache.ambari.server.agent.HeartBeat nextHeartbeat = null;

    private volatile org.apache.ambari.server.agent.RegistrationResponse lastRegistrationResponse = null;

    private volatile org.apache.ambari.server.agent.HeartBeatResponse lastHeartBeatResponse = null;

    public void start() {
        agentThread = new java.lang.Thread(this);
        agentThread.start();
    }

    public void shutdown() {
        shouldRun = false;
        agentThread.interrupt();
    }

    @java.lang.Override
    public void run() {
        while (shouldRun) {
            try {
                if (shouldSendRegistration) {
                    sendRegistration();
                } else if ((numberOfHeartbeats > 0) && (currentHeartbeatCount < numberOfHeartbeats)) {
                    sendHeartBeat();
                }
                java.lang.Thread.sleep(sleepTime);
            } catch (java.lang.InterruptedException e) {
            } catch (java.lang.Exception ex) {
                org.apache.ambari.server.agent.LocalAgentSimulator.LOG.info("Exception received ", ex);
                throw new java.lang.RuntimeException(ex);
            }
        } 
    }

    private void sendRegistration() {
        org.apache.ambari.server.agent.Register reg;
        if (nextRegistration != null) {
            reg = nextRegistration;
        } else {
            reg = new org.apache.ambari.server.agent.Register();
            reg.setTimestamp(java.lang.System.currentTimeMillis());
            reg.setHostname(this.hostname);
            reg.setAgentVersion(this.agentVersion);
            reg.setPrefix(org.apache.ambari.server.configuration.Configuration.PREFIX_DIR);
        }
        org.apache.ambari.server.agent.RegistrationResponse response;
        try {
            response = handler.handleRegistration(reg);
        } catch (org.apache.ambari.server.AmbariException | org.apache.ambari.server.state.fsm.InvalidStateTransitionException e) {
            org.apache.ambari.server.agent.LocalAgentSimulator.LOG.info("Registration failed", e);
            return;
        }
        this.responseId = response.getResponseId();
        this.lastRegistrationResponse = response;
        this.shouldSendRegistration = false;
        this.nextRegistration = null;
    }

    private void sendHeartBeat() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.agent.HeartBeat hb;
        if (nextHeartbeat != null) {
            hb = nextHeartbeat;
        } else {
            hb = new org.apache.ambari.server.agent.HeartBeat();
            hb.setResponseId(responseId);
            hb.setHostname(hostname);
            hb.setTimestamp(java.lang.System.currentTimeMillis());
        }
        org.apache.ambari.server.agent.HeartBeatResponse response = handler.handleHeartBeat(hb);
        this.responseId = response.getResponseId();
        this.lastHeartBeatResponse = response;
        this.nextHeartbeat = null;
    }

    public void setNumberOfHeartbeats(int numberOfHeartbeats) {
        this.numberOfHeartbeats = numberOfHeartbeats;
        currentHeartbeatCount = 0;
    }

    public void setShouldSendRegistration(boolean shouldSendRegistration) {
        this.shouldSendRegistration = shouldSendRegistration;
    }

    public org.apache.ambari.server.agent.RegistrationResponse getLastRegistrationResponse() {
        return lastRegistrationResponse;
    }

    public org.apache.ambari.server.agent.HeartBeatResponse getLastHeartBeatResponse() {
        return lastHeartBeatResponse;
    }

    public void setNextRegistration(org.apache.ambari.server.agent.Register nextRegistration) {
        this.nextRegistration = nextRegistration;
    }

    public void setNextHeartbeat(org.apache.ambari.server.agent.HeartBeat nextHeartbeat) {
        this.nextHeartbeat = nextHeartbeat;
    }
}