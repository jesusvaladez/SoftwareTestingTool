package org.apache.ambari.server.metadata;
public class RoleCommandOrder implements java.lang.Cloneable {
    @com.google.inject.Inject
    org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.metadata.RoleCommandOrder.class);

    private java.util.LinkedHashSet<java.lang.String> sectionKeys;

    private static final java.lang.String GENERAL_DEPS_KEY = "general_deps";

    public static final java.lang.String GLUSTERFS_DEPS_KEY = "optional_glusterfs";

    public static final java.lang.String NO_GLUSTERFS_DEPS_KEY = "optional_no_glusterfs";

    public static final java.lang.String NAMENODE_HA_DEPS_KEY = "namenode_optional_ha";

    public static final java.lang.String RESOURCEMANAGER_HA_DEPS_KEY = "resourcemanager_optional_ha";

    public static final java.lang.String COMMENT_STR = "_comment";

    private static final java.util.Set<org.apache.ambari.server.RoleCommand> independentCommands = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.RoleCommand.EXECUTE, org.apache.ambari.server.RoleCommand.SERVICE_CHECK);

    private java.util.Map<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> dependencies = new java.util.HashMap<>();

    private void addDependency(org.apache.ambari.server.Role blockedRole, org.apache.ambari.server.RoleCommand blockedCommand, org.apache.ambari.server.Role blockerRole, org.apache.ambari.server.RoleCommand blockerCommand, boolean overrideExisting) {
        org.apache.ambari.server.metadata.RoleCommandPair rcp1 = new org.apache.ambari.server.metadata.RoleCommandPair(blockedRole, blockedCommand);
        org.apache.ambari.server.metadata.RoleCommandPair rcp2 = new org.apache.ambari.server.metadata.RoleCommandPair(blockerRole, blockerCommand);
        if ((dependencies.get(rcp1) == null) || overrideExisting) {
            dependencies.put(rcp1, new java.util.HashSet<>());
        }
        dependencies.get(rcp1).add(rcp2);
    }

    void addDependencies(java.util.Map<java.lang.String, java.lang.Object> jsonSection) {
        if (jsonSection == null) {
            return;
        }
        for (java.lang.Object blockedObj : jsonSection.keySet()) {
            java.lang.String blocked = ((java.lang.String) (blockedObj));
            if (org.apache.ambari.server.metadata.RoleCommandOrder.COMMENT_STR.equals(blocked)) {
                continue;
            }
            java.util.ArrayList<java.lang.String> blockers = ((java.util.ArrayList<java.lang.String>) (jsonSection.get(blocked)));
            for (java.lang.String blocker : blockers) {
                java.lang.String[] blockedTuple = blocked.split("-");
                java.lang.String blockedRole = blockedTuple[0];
                java.lang.String blockedCommand = blockedTuple[1];
                boolean overrideExisting = blockedTuple.length == 3;
                java.lang.String[] blockerTuple = blocker.split("-");
                java.lang.String blockerRole = blockerTuple[0];
                java.lang.String blockerCommand = blockerTuple[1];
                addDependency(org.apache.ambari.server.Role.valueOf(blockedRole), org.apache.ambari.server.RoleCommand.valueOf(blockedCommand), org.apache.ambari.server.Role.valueOf(blockerRole), org.apache.ambari.server.RoleCommand.valueOf(blockerCommand), overrideExisting);
            }
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    public void initialize(org.apache.ambari.server.state.Cluster cluster, java.util.LinkedHashSet<java.lang.String> sectionKeys) {
        this.sectionKeys = sectionKeys;
        dependencies.clear();
        java.util.Set<org.apache.ambari.server.state.StackId> stackIds = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            stackIds.add(service.getDesiredStackId());
        }
        for (org.apache.ambari.server.state.StackId stackId : stackIds) {
            org.apache.ambari.server.state.StackInfo stack;
            try {
                stack = ambariMetaInfo.getStack(stackId.getStackName(), stackId.getStackVersion());
            } catch (org.apache.ambari.server.AmbariException ignored) {
                throw new java.lang.NullPointerException("Stack not found: " + stackId);
            }
            java.util.Map<java.lang.String, java.lang.Object> userData = stack.getRoleCommandOrder().getContent();
            java.util.Map<java.lang.String, java.lang.Object> generalSection = ((java.util.Map<java.lang.String, java.lang.Object>) (userData.get(org.apache.ambari.server.metadata.RoleCommandOrder.GENERAL_DEPS_KEY)));
            addDependencies(generalSection);
            for (java.lang.String sectionKey : sectionKeys) {
                java.util.Map<java.lang.String, java.lang.Object> section = ((java.util.Map<java.lang.String, java.lang.Object>) (userData.get(sectionKey)));
                addDependencies(section);
            }
        }
        extendTransitiveDependency();
        addMissingRestartDependencies();
    }

    public int order(org.apache.ambari.server.stageplanner.RoleGraphNode rgn1, org.apache.ambari.server.stageplanner.RoleGraphNode rgn2) {
        org.apache.ambari.server.metadata.RoleCommandPair rcp1 = new org.apache.ambari.server.metadata.RoleCommandPair(rgn1.getRole(), rgn1.getCommand());
        org.apache.ambari.server.metadata.RoleCommandPair rcp2 = new org.apache.ambari.server.metadata.RoleCommandPair(rgn2.getRole(), rgn2.getCommand());
        if ((dependencies.get(rcp1) != null) && dependencies.get(rcp1).contains(rcp2)) {
            return 1;
        } else if ((dependencies.get(rcp2) != null) && dependencies.get(rcp2).contains(rcp1)) {
            return -1;
        } else if (!rgn2.getCommand().equals(rgn1.getCommand())) {
            return compareCommands(rgn1, rgn2);
        }
        return 0;
    }

    public java.util.Set<org.apache.ambari.server.state.Service> getTransitiveServices(org.apache.ambari.server.state.Service service, org.apache.ambari.server.RoleCommand cmd) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.state.Service> transitiveServices = new java.util.HashSet<>();
        org.apache.ambari.server.state.Cluster cluster = service.getCluster();
        java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> allDeps = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.ServiceComponent sc : service.getServiceComponents().values()) {
            org.apache.ambari.server.metadata.RoleCommandPair rcp = new org.apache.ambari.server.metadata.RoleCommandPair(org.apache.ambari.server.Role.valueOf(sc.getName()), cmd);
            java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> deps = dependencies.get(rcp);
            if (deps != null) {
                allDeps.addAll(deps);
            }
        }
        for (org.apache.ambari.server.state.Service s : cluster.getServices().values()) {
            for (org.apache.ambari.server.metadata.RoleCommandPair rcp : allDeps) {
                org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponents().get(rcp.getRole().toString());
                if (sc != null) {
                    transitiveServices.add(s);
                    break;
                }
            }
        }
        return transitiveServices;
    }

    private void extendTransitiveDependency() {
        for (java.util.Map.Entry<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> roleCommandPairSetEntry : dependencies.entrySet()) {
            java.util.HashSet<org.apache.ambari.server.metadata.RoleCommandPair> visited = new java.util.HashSet<>();
            java.util.HashSet<org.apache.ambari.server.metadata.RoleCommandPair> transitiveDependencies = new java.util.HashSet<>();
            for (org.apache.ambari.server.metadata.RoleCommandPair directlyBlockedOn : dependencies.get(roleCommandPairSetEntry.getKey())) {
                visited.add(directlyBlockedOn);
                identifyTransitiveDependencies(directlyBlockedOn, visited, transitiveDependencies);
            }
            if (transitiveDependencies.size() > 0) {
                dependencies.get(roleCommandPairSetEntry.getKey()).addAll(transitiveDependencies);
            }
        }
    }

    private void identifyTransitiveDependencies(org.apache.ambari.server.metadata.RoleCommandPair rcp, java.util.HashSet<org.apache.ambari.server.metadata.RoleCommandPair> visited, java.util.HashSet<org.apache.ambari.server.metadata.RoleCommandPair> transitiveDependencies) {
        if (dependencies.get(rcp) != null) {
            for (org.apache.ambari.server.metadata.RoleCommandPair blockedOn : dependencies.get(rcp)) {
                if (!visited.contains(blockedOn)) {
                    visited.add(blockedOn);
                    transitiveDependencies.add(blockedOn);
                    identifyTransitiveDependencies(blockedOn, visited, transitiveDependencies);
                }
            }
        }
    }

    private void addMissingRestartDependencies() {
        java.util.Map<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> missingDependencies = new java.util.HashMap<>();
        for (java.util.Map.Entry<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> roleCommandPairSetEntry : dependencies.entrySet()) {
            org.apache.ambari.server.metadata.RoleCommandPair roleCommandPair = roleCommandPairSetEntry.getKey();
            if (roleCommandPair.getCmd().equals(org.apache.ambari.server.RoleCommand.START)) {
                org.apache.ambari.server.metadata.RoleCommandPair restartPair = new org.apache.ambari.server.metadata.RoleCommandPair(roleCommandPair.getRole(), org.apache.ambari.server.RoleCommand.RESTART);
                if (!dependencies.containsKey(restartPair)) {
                    java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> roleCommandDeps = new java.util.HashSet<>();
                    for (org.apache.ambari.server.metadata.RoleCommandPair rco : roleCommandPairSetEntry.getValue()) {
                        roleCommandDeps.add(new org.apache.ambari.server.metadata.RoleCommandPair(rco.getRole(), org.apache.ambari.server.RoleCommand.RESTART));
                    }
                    if (org.apache.ambari.server.metadata.RoleCommandOrder.LOG.isDebugEnabled()) {
                        org.apache.ambari.server.metadata.RoleCommandOrder.LOG.debug("Adding dependency for {}, dependencies => {}", restartPair, roleCommandDeps);
                    }
                    missingDependencies.put(restartPair, roleCommandDeps);
                }
            }
        }
        if (!missingDependencies.isEmpty()) {
            dependencies.putAll(missingDependencies);
        }
    }

    private int compareCommands(org.apache.ambari.server.stageplanner.RoleGraphNode rgn1, org.apache.ambari.server.stageplanner.RoleGraphNode rgn2) {
        org.apache.ambari.server.RoleCommand rc1 = rgn1.getCommand();
        org.apache.ambari.server.RoleCommand rc2 = rgn2.getCommand();
        if (rc1.equals(rc2)) {
            return 0;
        }
        if (org.apache.ambari.server.metadata.RoleCommandOrder.independentCommands.contains(rc1) && org.apache.ambari.server.metadata.RoleCommandOrder.independentCommands.contains(rc2)) {
            return 0;
        }
        if (rc1.equals(org.apache.ambari.server.RoleCommand.INSTALL)) {
            return -1;
        } else if (rc2.equals(org.apache.ambari.server.RoleCommand.INSTALL)) {
            return 1;
        } else if ((rc1.equals(org.apache.ambari.server.RoleCommand.START) || rc1.equals(org.apache.ambari.server.RoleCommand.EXECUTE)) || rc1.equals(org.apache.ambari.server.RoleCommand.SERVICE_CHECK)) {
            return -1;
        } else if ((rc2.equals(org.apache.ambari.server.RoleCommand.START) || rc2.equals(org.apache.ambari.server.RoleCommand.EXECUTE)) || rc2.equals(org.apache.ambari.server.RoleCommand.SERVICE_CHECK)) {
            return 1;
        } else if (rc1.equals(org.apache.ambari.server.RoleCommand.STOP)) {
            return -1;
        } else if (rc2.equals(org.apache.ambari.server.RoleCommand.STOP)) {
            return 1;
        }
        return 0;
    }

    public int compareDeps(org.apache.ambari.server.metadata.RoleCommandOrder rco) {
        java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> v1;
        java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> v2;
        if (this == rco) {
            return 0;
        }
        if (!dependencies.keySet().equals(rco.dependencies.keySet())) {
            org.apache.ambari.server.metadata.RoleCommandOrder.LOG.debug("dependency keysets differ");
            return 1;
        }
        org.apache.ambari.server.metadata.RoleCommandOrder.LOG.debug("dependency keysets match");
        for (java.util.Map.Entry<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> roleCommandPairSetEntry : dependencies.entrySet()) {
            v1 = dependencies.get(roleCommandPairSetEntry.getKey());
            v2 = rco.dependencies.get(roleCommandPairSetEntry.getKey());
            if (!v1.equals(v2)) {
                org.apache.ambari.server.metadata.RoleCommandOrder.LOG.debug("different entry found for key ({}, {})", roleCommandPairSetEntry.getKey().getRole(), roleCommandPairSetEntry.getKey().getCmd());
                return 1;
            }
        }
        org.apache.ambari.server.metadata.RoleCommandOrder.LOG.debug("dependency entries match");
        return 0;
    }

    public java.util.LinkedHashSet<java.lang.String> getSectionKeys() {
        return sectionKeys;
    }

    public java.util.Map<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> getDependencies() {
        return dependencies;
    }

    @java.lang.Override
    public java.lang.Object clone() throws java.lang.CloneNotSupportedException {
        org.apache.ambari.server.metadata.RoleCommandOrder clone = ((org.apache.ambari.server.metadata.RoleCommandOrder) (super.clone()));
        clone.sectionKeys = new java.util.LinkedHashSet<>(sectionKeys);
        clone.dependencies = new java.util.HashMap<>(dependencies);
        return clone;
    }
}