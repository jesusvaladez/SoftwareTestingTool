package org.apache.ambari.server.state;
public enum HostState {

    INIT,
    WAITING_FOR_HOST_STATUS_UPDATES,
    HEALTHY,
    HEARTBEAT_LOST,
    UNHEALTHY;}