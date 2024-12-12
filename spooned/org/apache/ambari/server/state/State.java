package org.apache.ambari.server.state;
public enum State {

    INIT,
    INSTALLING,
    INSTALL_FAILED,
    INSTALLED,
    STARTING,
    STARTED,
    STOPPING,
    UNINSTALLING,
    UNINSTALLED,
    WIPING_OUT,
    UPGRADING,
    DISABLED,
    UNKNOWN;
    public boolean isValidDesiredState() {
        switch (this) {
            case INIT :
            case INSTALLED :
            case STARTED :
            case UNINSTALLED :
            case DISABLED :
                return true;
            default :
                return false;
        }
    }

    public boolean isValidClientComponentState() {
        switch (this) {
            case STARTING :
            case STARTED :
            case STOPPING :
                return false;
            default :
                return true;
        }
    }

    public boolean isRemovableState() {
        switch (this) {
            case INIT :
            case INSTALLING :
            case INSTALLED :
            case INSTALL_FAILED :
            case UNINSTALLED :
            case UNKNOWN :
            case DISABLED :
                return true;
            default :
                return false;
        }
    }

    public static boolean isValidStateTransition(org.apache.ambari.server.state.State startState, org.apache.ambari.server.state.State desiredState) {
        switch (desiredState) {
            case INSTALLED :
                if ((((((((((startState == org.apache.ambari.server.state.State.INIT) || (startState == org.apache.ambari.server.state.State.UNINSTALLED)) || (startState == org.apache.ambari.server.state.State.INSTALLED)) || (startState == org.apache.ambari.server.state.State.INSTALLING)) || (startState == org.apache.ambari.server.state.State.STARTED)) || (startState == org.apache.ambari.server.state.State.INSTALL_FAILED)) || (startState == org.apache.ambari.server.state.State.UPGRADING)) || (startState == org.apache.ambari.server.state.State.STOPPING)) || (startState == org.apache.ambari.server.state.State.UNKNOWN)) || (startState == org.apache.ambari.server.state.State.DISABLED)) {
                    return true;
                }
                break;
            case STARTED :
                if (((startState == org.apache.ambari.server.state.State.INSTALLED) || (startState == org.apache.ambari.server.state.State.STARTING)) || (startState == org.apache.ambari.server.state.State.STARTED)) {
                    return true;
                }
                break;
            case UNINSTALLED :
                if (((startState == org.apache.ambari.server.state.State.INSTALLED) || (startState == org.apache.ambari.server.state.State.UNINSTALLED)) || (startState == org.apache.ambari.server.state.State.UNINSTALLING)) {
                    return true;
                }
                break;
            case INIT :
                if (((startState == org.apache.ambari.server.state.State.UNINSTALLED) || (startState == org.apache.ambari.server.state.State.INIT)) || (startState == org.apache.ambari.server.state.State.WIPING_OUT)) {
                    return true;
                }
                break;
            case DISABLED :
                if (((startState == org.apache.ambari.server.state.State.INSTALLED) || (startState == org.apache.ambari.server.state.State.INSTALL_FAILED)) || (startState == org.apache.ambari.server.state.State.UNKNOWN)) {
                    return true;
                }
                break;
        }
        return false;
    }

    public static boolean isValidDesiredStateTransition(org.apache.ambari.server.state.State startState, org.apache.ambari.server.state.State desiredState) {
        switch (desiredState) {
            case INSTALLED :
                if (((((startState == org.apache.ambari.server.state.State.INIT) || (startState == org.apache.ambari.server.state.State.UNINSTALLED)) || (startState == org.apache.ambari.server.state.State.INSTALLED)) || (startState == org.apache.ambari.server.state.State.STARTED)) || (startState == org.apache.ambari.server.state.State.STOPPING)) {
                    return true;
                }
                break;
            case STARTED :
                if ((startState == org.apache.ambari.server.state.State.INSTALLED) || (startState == org.apache.ambari.server.state.State.STARTED)) {
                    return true;
                }
                break;
        }
        return false;
    }

    public static void checkUpdateConfiguration(org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost, org.apache.ambari.server.state.State currentState, org.apache.ambari.server.state.State desiredState) throws org.apache.ambari.server.AmbariException {
        if (desiredState != null) {
            if (!(((desiredState == org.apache.ambari.server.state.State.INIT) || (desiredState == org.apache.ambari.server.state.State.INSTALLED)) || (desiredState == org.apache.ambari.server.state.State.STARTED))) {
                throw new org.apache.ambari.server.AmbariException(((((((((((("Changing of configs not supported" + (" for this transition" + ", clusterName=")) + serviceComponentHost.getClusterName()) + ", serviceName=") + serviceComponentHost.getServiceName()) + ", componentName=") + serviceComponentHost.getServiceComponentName()) + ", hostname=") + serviceComponentHost.getHostName()) + ", currentState=") + currentState) + ", newDesiredState=") + desiredState);
            }
        }
    }
}