package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlType(name = "service-check")
public class ServiceCheckGrouping extends org.apache.ambari.server.stack.upgrade.Grouping {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.class);

    @javax.xml.bind.annotation.XmlElementWrapper(name = "priority")
    @javax.xml.bind.annotation.XmlElement(name = "service")
    private java.util.Set<java.lang.String> priorityServices = new java.util.LinkedHashSet<>();

    @javax.xml.bind.annotation.XmlElementWrapper(name = "exclude")
    @javax.xml.bind.annotation.XmlElement(name = "service")
    private java.util.Set<java.lang.String> excludeServices = new java.util.HashSet<>();

    @java.lang.Override
    protected boolean serviceCheckAfterProcessing() {
        return false;
    }

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.ServiceCheckBuilder getBuilder() {
        return new org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.ServiceCheckBuilder(this);
    }

    public java.util.Set<java.lang.String> getPriorities() {
        return priorityServices;
    }

    public java.util.Set<java.lang.String> getExcluded() {
        return excludeServices;
    }

    public class ServiceCheckBuilder extends org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder {
        private org.apache.ambari.server.state.Cluster m_cluster;

        private org.apache.ambari.server.api.services.AmbariMetaInfo m_metaInfo;

        protected ServiceCheckBuilder(org.apache.ambari.server.stack.upgrade.Grouping grouping) {
            super(grouping);
        }

        @java.lang.Override
        public void add(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext ctx, org.apache.ambari.server.stack.HostsType hostsType, java.lang.String service, boolean clientOnly, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc, java.util.Map<java.lang.String, java.lang.String> params) {
        }

        @java.lang.Override
        public java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers) {
            m_cluster = upgradeContext.getCluster();
            m_metaInfo = upgradeContext.getAmbariMetaInfo();
            java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> result = new java.util.ArrayList<>(stageWrappers);
            if (upgradeContext.getDirection().isDowngrade()) {
                return result;
            }
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> serviceMap = m_cluster.getServices();
            java.util.Set<java.lang.String> clusterServices = new java.util.LinkedHashSet<>(serviceMap.keySet());
            for (java.lang.String service : priorityServices) {
                if (checkServiceValidity(upgradeContext, service, serviceMap)) {
                    org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper = new org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.ServiceCheckStageWrapper(service, upgradeContext.getServiceDisplay(service), true);
                    result.add(wrapper);
                    clusterServices.remove(service);
                }
            }
            if (upgradeContext.getType() == org.apache.ambari.spi.upgrade.UpgradeType.ROLLING) {
                for (java.lang.String service : clusterServices) {
                    if (excludeServices.contains(service)) {
                        continue;
                    }
                    if (checkServiceValidity(upgradeContext, service, serviceMap)) {
                        org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper = new org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.ServiceCheckStageWrapper(service, upgradeContext.getServiceDisplay(service), false);
                        result.add(wrapper);
                    }
                }
            }
            return result;
        }

        private boolean checkServiceValidity(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext ctx, java.lang.String service, java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> clusterServices) {
            if (clusterServices.containsKey(service)) {
                org.apache.ambari.server.state.Service svc = clusterServices.get(service);
                if (null != svc) {
                    org.apache.ambari.server.state.StackId stackId = svc.getDesiredStackId();
                    try {
                        org.apache.ambari.server.state.ServiceInfo si = m_metaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), service);
                        org.apache.ambari.server.state.CommandScriptDefinition script = si.getCommandScript();
                        if (((null != script) && (null != script.getScript())) && (!script.getScript().isEmpty())) {
                            ctx.setServiceDisplay(service, si.getDisplayName());
                            return true;
                        }
                    } catch (org.apache.ambari.server.AmbariException e) {
                        org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.LOG.error((("Could not determine if service " + service) + " can run a service check. Exception: ") + e.getMessage());
                    }
                }
            }
            return false;
        }
    }

    @java.lang.Override
    public void merge(java.util.Iterator<org.apache.ambari.server.stack.upgrade.Grouping> iterator) throws org.apache.ambari.server.AmbariException {
        java.util.List<java.lang.String> priorities = new java.util.ArrayList<>();
        priorities.addAll(getPriorities());
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> skippedPriorities = new java.util.HashMap<>();
        while (iterator.hasNext()) {
            org.apache.ambari.server.stack.upgrade.Grouping next = iterator.next();
            if (!(next instanceof org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping)) {
                throw new org.apache.ambari.server.AmbariException(("Invalid group type " + next.getClass().getSimpleName()) + " expected service check group");
            }
            org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping checkGroup = ((org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping) (next));
            getExcluded().addAll(checkGroup.getExcluded());
            boolean added = addPriorities(priorities, checkGroup.getPriorities(), checkGroup.addAfterGroupEntry);
            if (added) {
                addSkippedPriorities(priorities, skippedPriorities, checkGroup.getPriorities());
            } else if (skippedPriorities.containsKey(checkGroup.addAfterGroupEntry)) {
                java.util.Set<java.lang.String> tmp = skippedPriorities.get(checkGroup.addAfterGroupEntry);
                tmp.addAll(checkGroup.getPriorities());
            } else {
                skippedPriorities.put(checkGroup.addAfterGroupEntry, checkGroup.getPriorities());
            }
        } 
        getPriorities().clear();
        getPriorities().addAll(priorities);
    }

    private boolean addPriorities(java.util.List<java.lang.String> priorities, java.util.Set<java.lang.String> childPriorities, java.lang.String after) {
        if (after == null) {
            priorities.addAll(childPriorities);
            return true;
        } else {
            for (int index = priorities.size() - 1; index >= 0; index--) {
                java.lang.String priority = priorities.get(index);
                if (after.equals(priority)) {
                    priorities.addAll(index + 1, childPriorities);
                    return true;
                }
            }
        }
        return false;
    }

    private void addSkippedPriorities(java.util.List<java.lang.String> priorities, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> skippedPriorites, java.util.Set<java.lang.String> prioritiesJustAdded) {
        for (java.lang.String priority : prioritiesJustAdded) {
            if (skippedPriorites.containsKey(priority)) {
                java.util.Set<java.lang.String> prioritiesToAdd = skippedPriorites.remove(priority);
                addPriorities(priorities, prioritiesToAdd, priority);
                addSkippedPriorities(priorities, skippedPriorites, prioritiesToAdd);
            }
        }
    }

    public static class ServiceCheckStageWrapper extends org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper {
        public java.lang.String service;

        public boolean priority;

        ServiceCheckStageWrapper(java.lang.String service, java.lang.String serviceDisplay, boolean priority) {
            this(service, serviceDisplay, priority, null);
        }

        public ServiceCheckStageWrapper(java.lang.String service, java.lang.String serviceDisplay, boolean priority, java.lang.String host) {
            super(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVICE_CHECK, java.lang.String.format("Service Check %s", serviceDisplay), new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(service, "", null == host ? java.util.Collections.emptySet() : java.util.Collections.singleton(host), new org.apache.ambari.server.stack.upgrade.ServiceCheckTask()));
            this.service = service;
            this.priority = priority;
        }

        @java.lang.Override
        public int hashCode() {
            return service.hashCode();
        }

        @java.lang.Override
        public boolean equals(java.lang.Object other) {
            if (!other.getClass().equals(getClass())) {
                return false;
            }
            if (other == this) {
                return true;
            }
            return ((org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.ServiceCheckStageWrapper) (other)).service.equals(service);
        }
    }
}