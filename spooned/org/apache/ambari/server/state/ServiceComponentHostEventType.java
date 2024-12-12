package org.apache.ambari.server.state;
public enum ServiceComponentHostEventType {

    HOST_SVCCOMP_OP_IN_PROGRESS,
    HOST_SVCCOMP_OP_SUCCEEDED,
    HOST_SVCCOMP_OP_FAILED,
    HOST_SVCCOMP_OP_RESTART,
    HOST_SVCCOMP_INSTALL,
    HOST_SVCCOMP_START,
    HOST_SVCCOMP_STOP,
    HOST_SVCCOMP_STARTED,
    HOST_SVCCOMP_STOPPED,
    HOST_SVCCOMP_UNINSTALL,
    HOST_SVCCOMP_WIPEOUT,
    HOST_SVCCOMP_UPGRADE,
    HOST_SVCCOMP_DISABLE,
    HOST_SVCCOMP_RESTORE,
    HOST_SVCCOMP_SERVER_ACTION;}