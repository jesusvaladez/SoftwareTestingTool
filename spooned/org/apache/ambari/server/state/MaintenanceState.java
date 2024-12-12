package org.apache.ambari.server.state;
public enum MaintenanceState {

    OFF,
    ON,
    IMPLIED_FROM_SERVICE,
    IMPLIED_FROM_HOST,
    IMPLIED_FROM_SERVICE_AND_HOST;}