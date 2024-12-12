package org.apache.ambari.server.stageplanner;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
public class RoleGraph {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stageplanner.RoleGraph.class);

    java.util.Map<java.lang.String, org.apache.ambari.server.stageplanner.RoleGraphNode> graph = null;

    private org.apache.ambari.server.metadata.RoleCommandOrder roleDependencies;

    private org.apache.ambari.server.actionmanager.Stage initialStage = null;

    private boolean sameHostOptimization = true;

    private org.apache.ambari.server.actionmanager.CommandExecutionType commandExecutionType = org.apache.ambari.server.actionmanager.CommandExecutionType.STAGE;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.HostRoleCommandFactory hrcFactory;

    @com.google.inject.assistedinject.AssistedInject
    public RoleGraph() {
    }

    @com.google.inject.assistedinject.AssistedInject
    public RoleGraph(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.metadata.RoleCommandOrder rd) {
        roleDependencies = rd;
    }

    public org.apache.ambari.server.actionmanager.CommandExecutionType getCommandExecutionType() {
        return commandExecutionType;
    }

    public void setCommandExecutionType(org.apache.ambari.server.actionmanager.CommandExecutionType commandExecutionType) {
        this.commandExecutionType = commandExecutionType;
    }

    public void build(org.apache.ambari.server.actionmanager.Stage stage) {
        if (stage == null) {
            throw new java.lang.IllegalArgumentException("Null stage");
        }
        if (commandExecutionType == org.apache.ambari.server.actionmanager.CommandExecutionType.DEPENDENCY_ORDERED) {
            org.apache.ambari.server.stageplanner.RoleGraph.LOG.info("Build stage with DEPENDENCY_ORDERED commandExecutionType: {} ", stage.getRequestContext());
        }
        initialStage = stage;
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> hostRoleCommands = stage.getHostRoleCommands();
        build(hostRoleCommands);
    }

    private void build(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> hostRoleCommands) {
        graph = new java.util.TreeMap<>();
        for (java.lang.String host : hostRoleCommands.keySet()) {
            for (java.lang.String role : hostRoleCommands.get(host).keySet()) {
                org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommands.get(host).get(role);
                org.apache.ambari.server.stageplanner.RoleGraphNode rgn;
                if (graph.get(role) == null) {
                    rgn = new org.apache.ambari.server.stageplanner.RoleGraphNode(hostRoleCommand.getRole(), getRoleCommand(hostRoleCommand));
                    graph.put(role, rgn);
                }
                rgn = graph.get(role);
                rgn.addHost(host);
            }
        }
        if (commandExecutionType == org.apache.ambari.server.actionmanager.CommandExecutionType.STAGE) {
            if (null != roleDependencies) {
                for (java.lang.String roleI : graph.keySet()) {
                    for (java.lang.String roleJ : graph.keySet()) {
                        if (!roleI.equals(roleJ)) {
                            org.apache.ambari.server.stageplanner.RoleGraphNode rgnI = graph.get(roleI);
                            org.apache.ambari.server.stageplanner.RoleGraphNode rgnJ = graph.get(roleJ);
                            int order = roleDependencies.order(rgnI, rgnJ);
                            if (order == (-1)) {
                                rgnI.addEdge(rgnJ);
                            } else if (order == 1) {
                                rgnJ.addEdge(rgnI);
                            }
                        }
                    }
                }
            }
        }
    }

    private org.apache.ambari.server.RoleCommand getRoleCommand(org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand) {
        if (hostRoleCommand.getRoleCommand().equals(org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND)) {
            return hostRoleCommand.getCustomCommandName().equals("RESTART") ? org.apache.ambari.server.RoleCommand.RESTART : org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND;
        }
        return hostRoleCommand.getRoleCommand();
    }

    public java.util.List<org.apache.ambari.server.actionmanager.Stage> getStages() throws org.apache.ambari.server.AmbariException {
        long initialStageId = initialStage.getStageId();
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stageList = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.stageplanner.RoleGraphNode> firstStageNodes = new java.util.ArrayList<>();
        if (!graph.isEmpty()) {
            org.apache.ambari.server.stageplanner.RoleGraph.LOG.info("Detecting cycle graphs");
            org.apache.ambari.server.stageplanner.RoleGraph.LOG.info(stringifyGraph());
            breakCycleGraph();
        }
        while (!graph.isEmpty()) {
            if (org.apache.ambari.server.stageplanner.RoleGraph.LOG.isDebugEnabled()) {
                org.apache.ambari.server.stageplanner.RoleGraph.LOG.debug(stringifyGraph());
            }
            for (java.lang.String role : graph.keySet()) {
                org.apache.ambari.server.stageplanner.RoleGraphNode rgn = graph.get(role);
                if (rgn.getInDegree() == 0) {
                    firstStageNodes.add(rgn);
                }
            }
            org.apache.ambari.server.actionmanager.Stage aStage = getStageFromGraphNodes(initialStage, firstStageNodes);
            aStage.setStageId(++initialStageId);
            stageList.add(aStage);
            for (org.apache.ambari.server.stageplanner.RoleGraphNode rgn : firstStageNodes) {
                if (sameHostOptimization) {
                }
                removeZeroInDegreeNode(rgn.getRole().toString());
            }
            firstStageNodes.clear();
        } 
        return stageList;
    }

    public java.util.List<java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand>>> getOrderedHostRoleCommands(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> unorderedCommands) {
        build(unorderedCommands);
        java.util.List<java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand>>> orderedCommands = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.stageplanner.RoleGraphNode> firstStageNodes = new java.util.ArrayList<>();
        while (!graph.isEmpty()) {
            for (java.lang.String role : graph.keySet()) {
                org.apache.ambari.server.stageplanner.RoleGraphNode rgn = graph.get(role);
                if (rgn.getInDegree() == 0) {
                    firstStageNodes.add(rgn);
                }
            }
            java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand>> commandsPerHost = new java.util.HashMap<>();
            for (org.apache.ambari.server.stageplanner.RoleGraphNode rgn : firstStageNodes) {
                for (java.lang.String host : rgn.getHosts()) {
                    java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = commandsPerHost.get(host);
                    if (null == commands) {
                        commands = new java.util.ArrayList<>();
                        commandsPerHost.put(host, commands);
                    }
                    org.apache.ambari.server.actionmanager.HostRoleCommand hrc = hrcFactory.create(host, rgn.getRole(), null, rgn.getCommand());
                    commands.add(hrc);
                }
            }
            orderedCommands.add(commandsPerHost);
            for (org.apache.ambari.server.stageplanner.RoleGraphNode rgn : firstStageNodes) {
                removeZeroInDegreeNode(rgn.getRole().toString());
            }
            firstStageNodes.clear();
        } 
        return orderedCommands;
    }

    private synchronized void removeZeroInDegreeNode(java.lang.String role) {
        org.apache.ambari.server.stageplanner.RoleGraphNode nodeToRemove = graph.remove(role);
        for (org.apache.ambari.server.stageplanner.RoleGraphNode edgeNode : nodeToRemove.getEdges()) {
            edgeNode.decrementInDegree();
        }
    }

    private org.apache.ambari.server.actionmanager.Stage getStageFromGraphNodes(org.apache.ambari.server.actionmanager.Stage origStage, java.util.List<org.apache.ambari.server.stageplanner.RoleGraphNode> stageGraphNodes) {
        org.apache.ambari.server.actionmanager.Stage newStage = stageFactory.createNew(origStage.getRequestId(), origStage.getLogDir(), origStage.getClusterName(), origStage.getClusterId(), origStage.getRequestContext(), origStage.getCommandParamsStage(), origStage.getHostParamsStage());
        newStage.setSuccessFactors(origStage.getSuccessFactors());
        newStage.setSkippable(origStage.isSkippable());
        newStage.setAutoSkipFailureSupported(origStage.isAutoSkipOnFailureSupported());
        if (commandExecutionType != null) {
            newStage.setCommandExecutionType(commandExecutionType);
        }
        for (org.apache.ambari.server.stageplanner.RoleGraphNode rgn : stageGraphNodes) {
            for (java.lang.String host : rgn.getHosts()) {
                newStage.addExecutionCommandWrapper(origStage, host, rgn.getRole());
            }
        }
        return newStage;
    }

    public java.lang.String stringifyGraph() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("Graph:\n");
        for (java.lang.String role : graph.keySet()) {
            builder.append(graph.get(role));
            for (org.apache.ambari.server.stageplanner.RoleGraphNode rgn : graph.get(role).getEdges()) {
                builder.append(" --> ");
                builder.append(rgn);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public void breakCycleGraph() throws org.apache.ambari.server.AmbariException {
        java.util.List<java.lang.String> edges = new java.util.ArrayList<>();
        for (java.lang.String role : graph.keySet()) {
            org.apache.ambari.server.stageplanner.RoleGraphNode fromNode = graph.get(role);
            java.lang.String fnRole = fromNode.getRole().name();
            java.lang.String fnCommand = fromNode.getCommand().name();
            java.util.Iterator<org.apache.ambari.server.stageplanner.RoleGraphNode> it = fromNode.getEdges().iterator();
            while (it.hasNext()) {
                org.apache.ambari.server.stageplanner.RoleGraphNode toNode = it.next();
                java.lang.String tnRole = toNode.getRole().name();
                java.lang.String tnCommand = toNode.getCommand().name();
                java.lang.String format = "%s:%s --> %s:%s";
                java.lang.String edge = java.lang.String.format(format, fnRole, fnCommand, tnRole, tnCommand);
                java.lang.String reversedEdge = java.lang.String.format(format, tnRole, tnCommand, fnRole, fnCommand);
                if (edges.contains(reversedEdge)) {
                    java.lang.String msg = java.lang.String.format("Circular dependencies detected between %s and %s for %s. " + "%s already exists in the role command order.", fnRole, tnRole, edge, reversedEdge);
                    org.apache.ambari.server.stageplanner.RoleGraph.LOG.error(msg);
                    throw new org.apache.ambari.server.AmbariException(msg);
                } else {
                    edges.add(edge);
                }
            } 
        }
    }
}