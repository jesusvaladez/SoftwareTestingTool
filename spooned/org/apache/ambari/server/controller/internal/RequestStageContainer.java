package org.apache.ambari.server.controller.internal;
public class RequestStageContainer {
    private java.lang.Long id;

    private java.util.List<org.apache.ambari.server.actionmanager.Stage> stages;

    private org.apache.ambari.server.actionmanager.RequestFactory requestFactory;

    private org.apache.ambari.server.actionmanager.ActionManager actionManager;

    private java.lang.String requestContext = null;

    private org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = null;

    private java.lang.String clusterHostInfo = null;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.RequestStageContainer.class);

    public RequestStageContainer(java.lang.Long id, java.util.List<org.apache.ambari.server.actionmanager.Stage> stages, org.apache.ambari.server.actionmanager.RequestFactory factory, org.apache.ambari.server.actionmanager.ActionManager manager) {
        this(id, stages, factory, manager, null);
    }

    public RequestStageContainer(java.lang.Long id, java.util.List<org.apache.ambari.server.actionmanager.Stage> stages, org.apache.ambari.server.actionmanager.RequestFactory factory, org.apache.ambari.server.actionmanager.ActionManager manager, org.apache.ambari.server.controller.ExecuteActionRequest actionRequest) {
        this.id = id;
        this.stages = (stages == null) ? new java.util.ArrayList<>() : stages;
        this.requestFactory = factory;
        this.actionManager = manager;
        this.actionRequest = actionRequest;
        this.clusterHostInfo = "{}";
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setClusterHostInfo(java.lang.String clusterHostInfo) {
        this.clusterHostInfo = clusterHostInfo;
    }

    public void addStages(java.util.List<org.apache.ambari.server.actionmanager.Stage> stages) {
        if (stages != null) {
            this.stages.addAll(stages);
        }
    }

    public java.util.List<org.apache.ambari.server.actionmanager.Stage> getStages() {
        return stages;
    }

    public long getLastStageId() {
        return stages.isEmpty() ? -1 : stages.get(stages.size() - 1).getStageId();
    }

    public void setRequestContext(java.lang.String context) {
        requestContext = context;
    }

    public org.apache.ambari.server.state.State getProjectedState(java.lang.String host, java.lang.String component) {
        org.apache.ambari.server.RoleCommand lastCommand = null;
        java.util.ListIterator<org.apache.ambari.server.actionmanager.Stage> iterator = stages.listIterator(stages.size());
        while ((lastCommand == null) && iterator.hasPrevious()) {
            org.apache.ambari.server.actionmanager.Stage stage = iterator.previous();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> stageCommands = stage.getHostRoleCommands();
            if (stageCommands != null) {
                java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> hostCommands = stageCommands.get(host);
                if (hostCommands != null) {
                    org.apache.ambari.server.actionmanager.HostRoleCommand roleCommand = hostCommands.get(component);
                    if ((roleCommand != null) && (roleCommand.getRoleCommand() != org.apache.ambari.server.RoleCommand.SERVICE_CHECK)) {
                        lastCommand = roleCommand.getRoleCommand();
                    }
                }
            }
        } 
        org.apache.ambari.server.state.State resultingState = null;
        if (lastCommand != null) {
            switch (lastCommand) {
                case INSTALL :
                case STOP :
                    resultingState = org.apache.ambari.server.state.State.INSTALLED;
                    break;
                case START :
                    resultingState = org.apache.ambari.server.state.State.STARTED;
                    break;
                case UNINSTALL :
                    resultingState = org.apache.ambari.server.state.State.INIT;
                    break;
                default :
                    resultingState = org.apache.ambari.server.state.State.UNKNOWN;
            }
        }
        return resultingState;
    }

    public void persist() throws org.apache.ambari.server.AmbariException {
        if (!stages.isEmpty()) {
            org.apache.ambari.server.actionmanager.Request request = (null == actionRequest) ? requestFactory.createNewFromStages(stages, clusterHostInfo) : requestFactory.createNewFromStages(stages, clusterHostInfo, actionRequest);
            if (null != requestContext) {
                request.setRequestContext(requestContext);
            }
            if (request != null) {
                request.setUserName(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName());
            }
            if (((request != null) && (request.getStages() != null)) && (!request.getStages().isEmpty())) {
                if (org.apache.ambari.server.controller.internal.RequestStageContainer.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.controller.internal.RequestStageContainer.LOG.debug("Triggering Action Manager, request={}", request);
                }
                actionManager.sendActions(request, actionRequest);
            }
        }
    }

    public org.apache.ambari.server.controller.RequestStatusResponse getRequestStatusResponse() {
        org.apache.ambari.server.controller.RequestStatusResponse response = null;
        if (!stages.isEmpty()) {
            response = new org.apache.ambari.server.controller.RequestStatusResponse(id);
            java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands = actionManager.getRequestTasks(id);
            response.setRequestContext(actionManager.getRequestContext(id));
            java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> tasks = new java.util.ArrayList<>();
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : hostRoleCommands) {
                tasks.add(new org.apache.ambari.server.controller.ShortTaskStatus(hostRoleCommand));
            }
            response.setTasks(tasks);
        }
        return response;
    }
}