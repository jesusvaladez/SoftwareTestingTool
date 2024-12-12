package org.apache.ambari.server.stack.upgrade.orchestrate;
import com.google.inject.persist.Transactional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class UpgradeHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);

    private static final java.util.regex.Pattern PLACEHOLDER_REGEX = java.util.regex.Pattern.compile("(\\{\\{.*?\\}\\})");

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.state.ConfigHelper> m_configHelperProvider;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> m_ambariMetaInfoProvider;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clusters;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.controller.AmbariManagementControllerImpl> m_controllerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.ServiceConfigDAO m_serviceConfigDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    public org.apache.ambari.server.stack.upgrade.UpgradePack suggestUpgradePack(java.lang.String clusterName, org.apache.ambari.server.state.StackId sourceStackId, org.apache.ambari.server.state.StackId targetStackId, org.apache.ambari.server.stack.upgrade.Direction direction, org.apache.ambari.spi.upgrade.UpgradeType upgradeType, java.lang.String preferredUpgradePackName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = m_clusters.get().getCluster(clusterName);
        org.apache.ambari.server.state.StackId currentStack = cluster.getCurrentStackVersion();
        org.apache.ambari.server.stack.upgrade.UpgradePack pack = findPreferred(preferredUpgradePackName, currentStack, targetStackId);
        if (null != pack) {
            return pack;
        }
        pack = findUpgradePack(targetStackId, currentStack, upgradeType, true);
        if (null != pack) {
            return pack;
        }
        pack = findUpgradePack(currentStack, targetStackId, upgradeType, false);
        if (null == pack) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unable to perform %s. Could not locate %s upgrade pack for stack %s", direction.getText(false), upgradeType.toString(), targetStackId));
        }
        return pack;
    }

    private org.apache.ambari.server.stack.upgrade.UpgradePack findPreferred(java.lang.String preferred, org.apache.ambari.server.state.StackId... stackIds) {
        if (org.apache.commons.lang.StringUtils.isEmpty(preferred)) {
            return null;
        }
        for (org.apache.ambari.server.state.StackId stackId : stackIds) {
            java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> packs = m_ambariMetaInfoProvider.get().getUpgradePacks(stackId.getStackName(), stackId.getStackVersion());
            if (!packs.containsKey(preferred)) {
                org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.warn("Upgrade pack '{}' not found for stack {}", preferred, stackId);
            } else {
                return packs.get(preferred);
            }
        }
        return null;
    }

    private org.apache.ambari.server.stack.upgrade.UpgradePack findUpgradePack(org.apache.ambari.server.state.StackId sourceStack, org.apache.ambari.server.state.StackId stackToFind, org.apache.ambari.spi.upgrade.UpgradeType upgradeType, boolean compareToSource) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> packs = m_ambariMetaInfoProvider.get().getUpgradePacks(sourceStack.getStackName(), sourceStack.getStackVersion());
        org.apache.ambari.server.stack.upgrade.UpgradePack result = null;
        for (org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack : packs.values()) {
            org.apache.ambari.server.state.StackId upgradeStack = (compareToSource) ? new org.apache.ambari.server.state.StackId(upgradePack.getSourceStack()) : new org.apache.ambari.server.state.StackId(upgradePack.getTargetStack());
            if (upgradeStack.equals(stackToFind) && (upgradePack.getType() == upgradeType)) {
                if (null == result) {
                    result = upgradePack;
                } else {
                    throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unable to resolve upgrade pack. Found multiple upgrade packs for type %s and stack %s", upgradeType.toString(), stackToFind));
                }
            }
        }
        return result;
    }

    public java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> createSequence(org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = context.getCluster();
        org.apache.ambari.server.stack.MasterHostResolver mhr = context.getResolver();
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.AddComponentTask> addedComponentsDuringUpgrade = upgradePack.getAddComponentTasks();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent>> allTasks = upgradePack.getTasks();
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder previousGroupHolder = null;
        for (org.apache.ambari.server.stack.upgrade.Grouping group : upgradePack.getGroups(context.getDirection())) {
            if (!context.isScoped(group.scope)) {
                continue;
            }
            if ((null != group.condition) && (!group.condition.isSatisfied(context))) {
                org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.info("Skipping {} while building upgrade orchestration due to {}", group, group.condition);
                continue;
            }
            org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder groupHolder = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder();
            groupHolder.name = group.name;
            groupHolder.title = group.title;
            groupHolder.groupClass = group.getClass();
            groupHolder.skippable = group.skippable;
            groupHolder.supportsAutoSkipOnFailure = group.supportsAutoSkipOnFailure;
            groupHolder.allowRetry = group.allowRetry;
            groupHolder.processingGroup = group.isProcessingGroup();
            if (context.getDirection().isDowngrade()) {
                groupHolder.skippable = true;
            }
            org.apache.ambari.server.stack.upgrade.Task.Type functionName = null;
            if (group instanceof org.apache.ambari.server.stack.upgrade.UpgradeFunction) {
                functionName = ((org.apache.ambari.server.stack.upgrade.UpgradeFunction) (group)).getFunction();
            }
            if (upgradePack.getType() == org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING) {
                group.performServiceCheck = false;
            }
            org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder builder = group.getBuilder();
            java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService> services = group.services;
            if (upgradePack.getType() == org.apache.ambari.spi.upgrade.UpgradeType.ROLLING) {
                if (context.getDirection().isDowngrade() && (!services.isEmpty())) {
                    java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService> reverse = new java.util.ArrayList<>(services);
                    java.util.Collections.reverse(reverse);
                    services = reverse;
                }
            }
            for (org.apache.ambari.server.stack.upgrade.UpgradePack.OrderService service : services) {
                if (!context.isServiceSupported(service.serviceName)) {
                    continue;
                }
                if ((upgradePack.getType() == org.apache.ambari.spi.upgrade.UpgradeType.ROLLING) && (!allTasks.containsKey(service.serviceName))) {
                    continue;
                }
                for (java.lang.String component : service.components) {
                    if ((upgradePack.getType() == org.apache.ambari.spi.upgrade.UpgradeType.ROLLING) && (!allTasks.get(service.serviceName).containsKey(component))) {
                        continue;
                    }
                    org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc = null;
                    if (null == functionName) {
                        pc = allTasks.get(service.serviceName).get(component);
                    } else if (functionName == org.apache.ambari.server.stack.upgrade.Task.Type.STOP) {
                        pc = new org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent();
                        pc.name = component;
                        pc.tasks = new java.util.ArrayList<>();
                        pc.tasks.add(new org.apache.ambari.server.stack.upgrade.StopTask());
                    } else if (allTasks.containsKey(service.serviceName) && allTasks.get(service.serviceName).containsKey(component)) {
                        pc = allTasks.get(service.serviceName).get(component);
                    } else {
                        pc = new org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent();
                        pc.name = component;
                        pc.tasks = new java.util.ArrayList<>();
                        if (functionName == org.apache.ambari.server.stack.upgrade.Task.Type.START) {
                            pc.tasks.add(new org.apache.ambari.server.stack.upgrade.StartTask());
                        }
                        if (functionName == org.apache.ambari.server.stack.upgrade.Task.Type.RESTART) {
                            pc.tasks.add(new org.apache.ambari.server.stack.upgrade.RestartTask());
                        }
                    }
                    if (pc == null) {
                        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.error(java.text.MessageFormat.format("Couldn't create a processing component for service {0} and component {1}.", service.serviceName, component));
                        continue;
                    }
                    org.apache.ambari.server.stack.HostsType hostsType = mhr.getMasterAndHosts(service.serviceName, component);
                    boolean taskIsRestartOrStart = ((functionName == null) || (functionName == org.apache.ambari.server.stack.upgrade.Task.Type.START)) || (functionName == org.apache.ambari.server.stack.upgrade.Task.Type.RESTART);
                    java.lang.String serviceAndComponentHash = (service.serviceName + "/") + component;
                    if (taskIsRestartOrStart && addedComponentsDuringUpgrade.containsKey(serviceAndComponentHash)) {
                        org.apache.ambari.server.stack.upgrade.AddComponentTask task = addedComponentsDuringUpgrade.get(serviceAndComponentHash);
                        java.util.Collection<org.apache.ambari.server.state.Host> candidateHosts = org.apache.ambari.server.stack.MasterHostResolver.getCandidateHosts(cluster, task.hosts, task.hostService, task.hostComponent);
                        if (!candidateHosts.isEmpty()) {
                            if (null == hostsType) {
                                hostsType = org.apache.ambari.server.stack.HostsType.normal(candidateHosts.stream().map(host -> host.getHostName()).collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new)));
                            } else {
                                java.util.Set<java.lang.String> hostsForTask = hostsType.getHosts();
                                for (org.apache.ambari.server.state.Host host : candidateHosts) {
                                    hostsForTask.add(host.getHostName());
                                }
                            }
                        }
                    }
                    if (null == hostsType) {
                        continue;
                    }
                    if (!hostsType.unhealthy.isEmpty()) {
                        context.addUnhealthy(hostsType.unhealthy);
                    }
                    org.apache.ambari.server.state.Service svc = cluster.getService(service.serviceName);
                    setDisplayNames(context, service.serviceName, component);
                    if (service.serviceName.equalsIgnoreCase("HDFS") && component.equalsIgnoreCase("NAMENODE")) {
                        switch (upgradePack.getType()) {
                            case ROLLING :
                                if ((!hostsType.getHosts().isEmpty()) && hostsType.hasMastersAndSecondaries()) {
                                    hostsType.arrangeHostSecondariesFirst();
                                    builder.add(context, hostsType, service.serviceName, svc.isClientOnlyService(), pc, null);
                                } else {
                                    org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.warn("Could not orchestrate NameNode.  Hosts could not be resolved: hosts={}, active={}, standby={}", org.apache.commons.lang.StringUtils.join(hostsType.getHosts(), ','), hostsType.getMasters(), hostsType.getSecondaries());
                                }
                                break;
                            case NON_ROLLING :
                                boolean isNameNodeHA = mhr.isNameNodeHA();
                                if (isNameNodeHA && hostsType.hasMastersAndSecondaries()) {
                                    builder.add(context, org.apache.ambari.server.stack.HostsType.normal(hostsType.getMasters()), service.serviceName, svc.isClientOnlyService(), pc, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.nameNodeRole("active"));
                                    builder.add(context, org.apache.ambari.server.stack.HostsType.normal(hostsType.getSecondaries()), service.serviceName, svc.isClientOnlyService(), pc, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.nameNodeRole("standby"));
                                } else {
                                    builder.add(context, hostsType, service.serviceName, svc.isClientOnlyService(), pc, null);
                                }
                                break;
                        }
                    } else {
                        builder.add(context, hostsType, service.serviceName, svc.isClientOnlyService(), pc, null);
                    }
                }
            }
            java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> proxies = builder.build(context);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(proxies)) {
                groupHolder.items = proxies;
                postProcess(context, groupHolder);
                if (org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.class.isInstance(group)) {
                    if ((null != previousGroupHolder) && org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.class.equals(previousGroupHolder.groupClass)) {
                        mergeServiceChecks(groupHolder, previousGroupHolder);
                    } else {
                        groups.add(groupHolder);
                    }
                } else {
                    groups.add(groupHolder);
                }
                previousGroupHolder = groupHolder;
            }
        }
        if (org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.isDebugEnabled()) {
            for (org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group : groups) {
                org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.debug(group.name);
                int i = 0;
                for (org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper proxy : group.items) {
                    org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.debug("  Stage {}", java.lang.Integer.valueOf(i++));
                    int j = 0;
                    for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper task : proxy.getTasks()) {
                        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.debug("    Task {} {}", java.lang.Integer.valueOf(j++), task);
                    }
                }
            }
        }
        java.util.Iterator<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> iterator = groups.iterator();
        boolean canServiceCheck = false;
        while (iterator.hasNext()) {
            org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder holder = iterator.next();
            if (org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.class.equals(holder.groupClass) && (!canServiceCheck)) {
                iterator.remove();
            }
            canServiceCheck |= holder.processingGroup;
        } 
        return groups;
    }

    private static java.util.Map<java.lang.String, java.lang.String> nameNodeRole(java.lang.String value) {
        java.util.Map<java.lang.String, java.lang.String> params = new java.util.HashMap<>();
        params.put("desired_namenode_role", value);
        return params;
    }

    @java.lang.SuppressWarnings("unchecked")
    private void mergeServiceChecks(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder newHolder, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder oldHolder) {
        java.util.LinkedHashSet<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> priority = new java.util.LinkedHashSet<>();
        java.util.LinkedHashSet<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> others = new java.util.LinkedHashSet<>();
        java.util.Set<java.lang.String> extraKeys = new java.util.HashSet<>();
        java.util.LinkedHashSet<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> extras = new java.util.LinkedHashSet<>();
        for (java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> holderItems : new java.util.List[]{ oldHolder.items, newHolder.items }) {
            for (org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper stageWrapper : holderItems) {
                if (stageWrapper instanceof org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.ServiceCheckStageWrapper) {
                    org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.ServiceCheckStageWrapper wrapper = ((org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.ServiceCheckStageWrapper) (stageWrapper));
                    if (wrapper.priority) {
                        priority.add(stageWrapper);
                    } else {
                        others.add(stageWrapper);
                    }
                } else {
                    java.lang.String key = stageWrapper.toString();
                    if (!extraKeys.contains(key)) {
                        extras.add(stageWrapper);
                        extraKeys.add(key);
                    }
                }
            }
        }
        others = new java.util.LinkedHashSet<>(org.apache.commons.collections.CollectionUtils.subtract(others, priority));
        oldHolder.items = com.google.common.collect.Lists.newLinkedList(priority);
        oldHolder.items.addAll(others);
        oldHolder.items.addAll(extras);
    }

    private void postProcess(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext ctx, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder holder) {
        holder.title = tokenReplace(ctx, holder.title, null, null);
        for (org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper stageWrapper : holder.items) {
            if (null != stageWrapper.getText()) {
                stageWrapper.setText(tokenReplace(ctx, stageWrapper.getText(), null, null));
            }
            for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper taskWrapper : stageWrapper.getTasks()) {
                for (org.apache.ambari.server.stack.upgrade.Task task : taskWrapper.getTasks()) {
                    if (null != task.summary) {
                        task.summary = tokenReplace(ctx, task.summary, null, null);
                    }
                    if (task.getType() == org.apache.ambari.server.stack.upgrade.Task.Type.MANUAL) {
                        org.apache.ambari.server.stack.upgrade.ManualTask mt = ((org.apache.ambari.server.stack.upgrade.ManualTask) (task));
                        if ((null != mt.messages) && (!mt.messages.isEmpty())) {
                            for (int i = 0; i < mt.messages.size(); i++) {
                                java.lang.String message = mt.messages.get(i);
                                message = tokenReplace(ctx, message, taskWrapper.getService(), taskWrapper.getComponent());
                                mt.messages.set(i, message);
                            }
                        }
                    }
                }
            }
        }
    }

    private java.lang.String tokenReplace(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext ctx, java.lang.String source, java.lang.String service, java.lang.String component) {
        org.apache.ambari.server.state.Cluster cluster = ctx.getCluster();
        org.apache.ambari.server.stack.MasterHostResolver mhr = ctx.getResolver();
        java.lang.String result = source;
        java.util.List<java.lang.String> tokens = new java.util.ArrayList<>(5);
        java.util.regex.Matcher matcher = org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.PLACEHOLDER_REGEX.matcher(source);
        while (matcher.find()) {
            tokens.add(matcher.group(1));
        } 
        for (java.lang.String token : tokens) {
            java.lang.String value = null;
            org.apache.ambari.server.stack.upgrade.orchestrate.Placeholder p = org.apache.ambari.server.stack.upgrade.orchestrate.Placeholder.find(token);
            switch (p) {
                case HOST_ALL :
                    {
                        if ((null != service) && (null != component)) {
                            org.apache.ambari.server.stack.HostsType hostsType = mhr.getMasterAndHosts(service, component);
                            if (null != hostsType) {
                                value = org.apache.commons.lang.StringUtils.join(hostsType.getHosts(), ", ");
                            }
                        }
                        break;
                    }
                case HOST_MASTER :
                    {
                        if ((null != service) && (null != component)) {
                            org.apache.ambari.server.stack.HostsType hostsType = mhr.getMasterAndHosts(service, component);
                            if (null != hostsType) {
                                value = org.apache.commons.lang.StringUtils.join(hostsType.getMasters(), ", ");
                            }
                        }
                        break;
                    }
                case VERSION :
                    value = ctx.getRepositoryVersion().getVersion();
                    break;
                case DIRECTION_VERB :
                case DIRECTION_VERB_PROPER :
                    value = ctx.getDirection().getVerb(p == org.apache.ambari.server.stack.upgrade.orchestrate.Placeholder.DIRECTION_VERB_PROPER);
                    break;
                case DIRECTION_PAST :
                case DIRECTION_PAST_PROPER :
                    value = ctx.getDirection().getPast(p == org.apache.ambari.server.stack.upgrade.orchestrate.Placeholder.DIRECTION_PAST_PROPER);
                    break;
                case DIRECTION_PLURAL :
                case DIRECTION_PLURAL_PROPER :
                    value = ctx.getDirection().getPlural(p == org.apache.ambari.server.stack.upgrade.orchestrate.Placeholder.DIRECTION_PLURAL_PROPER);
                    break;
                case DIRECTION_TEXT :
                case DIRECTION_TEXT_PROPER :
                    value = ctx.getDirection().getText(p == org.apache.ambari.server.stack.upgrade.orchestrate.Placeholder.DIRECTION_TEXT_PROPER);
                    break;
                default :
                    value = m_configHelperProvider.get().getPlaceholderValueFromDesiredConfigurations(cluster, token);
                    break;
            }
            if (null != value) {
                result = result.replace(token, value);
            }
        }
        return result;
    }

    public org.apache.ambari.server.controller.spi.Resource getTaskResource(java.lang.String clusterName, java.lang.Long requestId, java.lang.Long stageId, java.lang.Long taskId) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.SystemException {
        org.apache.ambari.server.controller.spi.ClusterController clusterController = org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest();
        org.apache.ambari.server.controller.spi.Predicate p1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_CLUSTER_NAME_PROPERTY_ID).equals(clusterName).toPredicate();
        org.apache.ambari.server.controller.spi.Predicate p2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID).equals(requestId.toString()).toPredicate();
        org.apache.ambari.server.controller.spi.Predicate p3 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_STAGE_ID_PROPERTY_ID).equals(stageId.toString()).toPredicate();
        org.apache.ambari.server.controller.spi.Predicate p4 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID).equals(taskId.toString()).toPredicate();
        org.apache.ambari.server.controller.spi.QueryResponse response = clusterController.getResources(org.apache.ambari.server.controller.spi.Resource.Type.Task, request, new org.apache.ambari.server.controller.predicate.AndPredicate(p1, p2, p3, p4));
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> task = response.getResources();
        return task.size() == 1 ? task.iterator().next() : null;
    }

    private void setDisplayNames(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context, java.lang.String service, java.lang.String component) {
        org.apache.ambari.server.state.StackId currentStackId = context.getCluster().getCurrentStackVersion();
        org.apache.ambari.server.state.StackId stackId = context.getRepositoryVersion().getStackId();
        try {
            org.apache.ambari.server.state.ServiceInfo serviceInfo = m_ambariMetaInfoProvider.get().getService(stackId.getStackName(), stackId.getStackVersion(), service);
            if (null == serviceInfo) {
                serviceInfo = m_ambariMetaInfoProvider.get().getService(currentStackId.getStackName(), currentStackId.getStackVersion(), service);
            }
            if (null == serviceInfo) {
                org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.debug("Unable to lookup service display name information for {}", service);
                return;
            }
            context.setServiceDisplay(service, serviceInfo.getDisplayName());
            org.apache.ambari.server.state.ComponentInfo compInfo = serviceInfo.getComponentByName(component);
            if (null == compInfo) {
                org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.debug("Unable to lookup component display name information for {}", component);
                return;
            }
            context.setComponentDisplay(service, component, compInfo.getDisplayName());
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.debug("Could not get service detail", e);
        }
    }

    @com.google.inject.persist.Transactional
    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
    public void updateDesiredRepositoriesAndConfigs(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) throws org.apache.ambari.server.AmbariException {
        setDesiredRepositories(upgradeContext);
        processConfigurationsIfRequired(upgradeContext);
    }

    public void publishDesiredRepositoriesUpdates(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = upgradeContext.getCluster();
        ambariEventPublisher.publish(new org.apache.ambari.server.events.ClusterComponentsRepoChangedEvent(cluster.getClusterId()));
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
    private void setDesiredRepositories(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = upgradeContext.getCluster();
        java.util.Set<java.lang.String> services = upgradeContext.getSupportedServices();
        for (java.lang.String serviceName : services) {
            org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersion = upgradeContext.getTargetRepositoryVersion(serviceName);
            org.apache.ambari.server.state.StackId targetStack = targetRepositoryVersion.getStackId();
            service.setDesiredRepositoryVersion(targetRepositoryVersion);
            java.util.Collection<org.apache.ambari.server.state.ServiceComponent> components = service.getServiceComponents().values();
            for (org.apache.ambari.server.state.ServiceComponent serviceComponent : components) {
                boolean versionAdvertised = false;
                try {
                    org.apache.ambari.server.state.ComponentInfo ci = m_ambariMetaInfoProvider.get().getComponent(targetStack.getStackName(), targetStack.getStackVersion(), serviceComponent.getServiceName(), serviceComponent.getName());
                    versionAdvertised = ci.isVersionAdvertised();
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.warn("Component {}/{} doesn't exist for stack {}.  Setting version to {}", serviceComponent.getServiceName(), serviceComponent.getName(), targetStack, org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.UNKNOWN_VERSION);
                }
                org.apache.ambari.server.state.UpgradeState upgradeStateToSet = org.apache.ambari.server.state.UpgradeState.IN_PROGRESS;
                if (!versionAdvertised) {
                    upgradeStateToSet = org.apache.ambari.server.state.UpgradeState.NONE;
                }
                for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : serviceComponent.getServiceComponentHosts().values()) {
                    if (serviceComponentHost.getUpgradeState() != upgradeStateToSet) {
                        serviceComponentHost.setUpgradeState(upgradeStateToSet);
                    }
                    if ((!versionAdvertised) && (!org.apache.commons.lang.StringUtils.equals(org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.UNKNOWN_VERSION, serviceComponentHost.getVersion()))) {
                        serviceComponentHost.setVersion(org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.UNKNOWN_VERSION);
                    }
                }
                serviceComponent.setDesiredRepositoryVersion(targetRepositoryVersion);
            }
        }
    }

    private void processConfigurationsIfRequired(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController controller = m_controllerProvider.get();
        org.apache.ambari.server.state.Cluster cluster = upgradeContext.getCluster();
        org.apache.ambari.server.stack.upgrade.Direction direction = upgradeContext.getDirection();
        java.lang.String userName = controller.getAuthName();
        java.util.Set<java.lang.String> servicesInUpgrade = upgradeContext.getSupportedServices();
        java.util.Set<java.lang.String> clusterConfigTypes = new java.util.HashSet<>();
        java.util.Set<java.lang.String> processedClusterConfigTypes = new java.util.HashSet<>();
        boolean configsChanged = false;
        for (java.lang.String serviceName : servicesInUpgrade) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity sourceRepositoryVersion = upgradeContext.getSourceRepositoryVersion(serviceName);
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersion = upgradeContext.getTargetRepositoryVersion(serviceName);
            org.apache.ambari.server.state.StackId sourceStackId = sourceRepositoryVersion.getStackId();
            org.apache.ambari.server.state.StackId targetStackId = targetRepositoryVersion.getStackId();
            if (sourceStackId.equals(targetStackId)) {
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity associatedRepositoryVersion = upgradeContext.getRepositoryVersion();
                org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.info("The {} {} {} will not change stack configurations for {} since the source and target are both {}", direction.getText(false), direction.getPreposition(), associatedRepositoryVersion.getVersion(), serviceName, targetStackId);
                continue;
            }
            org.apache.ambari.server.state.ConfigHelper configHelper = m_configHelperProvider.get();
            if (direction == org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE) {
                cluster.applyLatestConfigurations(targetStackId, serviceName);
                configsChanged = true;
                continue;
            }
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> readOnlyProperties = getReadOnlyProperties(sourceStackId, serviceName);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> oldServiceDefaultConfigsByType = configHelper.getDefaultProperties(sourceStackId, serviceName);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> newServiceDefaultConfigsByType = configHelper.getDefaultProperties(targetStackId, serviceName);
            if ((null == oldServiceDefaultConfigsByType) || (null == newServiceDefaultConfigsByType)) {
                continue;
            }
            java.util.Set<java.lang.String> foundConfigTypes = new java.util.HashSet<>();
            java.util.List<org.apache.ambari.server.state.Config> existingServiceConfigs = new java.util.ArrayList<>();
            java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> latestServiceConfigs = m_serviceConfigDAO.getLastServiceConfigsForService(cluster.getClusterId(), serviceName);
            for (org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfig : latestServiceConfigs) {
                java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> existingConfigurations = serviceConfig.getClusterConfigEntities();
                for (org.apache.ambari.server.orm.entities.ClusterConfigEntity currentServiceConfig : existingConfigurations) {
                    java.lang.String configurationType = currentServiceConfig.getType();
                    org.apache.ambari.server.state.Config currentClusterConfigForService = cluster.getDesiredConfigByType(configurationType);
                    if (currentClusterConfigForService == null) {
                        throw new org.apache.ambari.server.AmbariException(java.lang.String.format("configuration type %s did not have a selected version", configurationType));
                    }
                    existingServiceConfigs.add(currentClusterConfigForService);
                    foundConfigTypes.add(configurationType);
                }
            }
            @java.lang.SuppressWarnings("unchecked")
            java.util.Set<java.lang.String> missingConfigTypes = new java.util.HashSet<>(org.apache.commons.collections.CollectionUtils.subtract(oldServiceDefaultConfigsByType.keySet(), foundConfigTypes));
            for (java.lang.String missingConfigType : missingConfigTypes) {
                org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(missingConfigType);
                if (null != config) {
                    existingServiceConfigs.add(config);
                    clusterConfigTypes.add(missingConfigType);
                }
            }
            for (org.apache.ambari.server.state.Config existingServiceConfig : existingServiceConfigs) {
                java.lang.String configurationType = existingServiceConfig.getType();
                java.util.Map<java.lang.String, java.lang.String> oldServiceDefaultConfigs = oldServiceDefaultConfigsByType.get(configurationType);
                if (null == oldServiceDefaultConfigs) {
                    oldServiceDefaultConfigs = java.util.Collections.emptyMap();
                }
                java.util.Map<java.lang.String, java.lang.String> existingConfigurations = existingServiceConfig.getProperties();
                java.util.Map<java.lang.String, java.lang.String> newDefaultConfigurations = newServiceDefaultConfigsByType.get(configurationType);
                if (null == newDefaultConfigurations) {
                    newServiceDefaultConfigsByType.put(configurationType, existingConfigurations);
                    continue;
                } else {
                    java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.String>> iter = newDefaultConfigurations.entrySet().iterator();
                    while (iter.hasNext()) {
                        java.util.Map.Entry<java.lang.String, java.lang.String> entry = iter.next();
                        if (entry.getValue() == null) {
                            iter.remove();
                        }
                    } 
                }
                for (java.util.Map.Entry<java.lang.String, java.lang.String> existingConfigurationEntry : existingConfigurations.entrySet()) {
                    java.lang.String existingConfigurationKey = existingConfigurationEntry.getKey();
                    java.lang.String existingConfigurationValue = existingConfigurationEntry.getValue();
                    if (newDefaultConfigurations.containsKey(existingConfigurationKey)) {
                        java.lang.String newDefaultConfigurationValue = newDefaultConfigurations.get(existingConfigurationKey);
                        if (!org.apache.commons.lang.StringUtils.equals(existingConfigurationValue, newDefaultConfigurationValue)) {
                            java.lang.String oldDefaultValue = oldServiceDefaultConfigs.get(existingConfigurationKey);
                            java.util.Set<java.lang.String> readOnlyPropertiesForType = readOnlyProperties.get(configurationType);
                            boolean readOnly = (null != readOnlyPropertiesForType) && readOnlyPropertiesForType.contains(existingConfigurationKey);
                            if ((!readOnly) && (!org.apache.commons.lang.StringUtils.equals(existingConfigurationValue, oldDefaultValue))) {
                                newDefaultConfigurations.put(existingConfigurationKey, existingConfigurationValue);
                            }
                        }
                    } else {
                        newDefaultConfigurations.put(existingConfigurationKey, existingConfigurationValue);
                    }
                }
                java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.String>> newDefaultConfigurationsIterator = newDefaultConfigurations.entrySet().iterator();
                while (newDefaultConfigurationsIterator.hasNext()) {
                    java.util.Map.Entry<java.lang.String, java.lang.String> newConfigurationEntry = newDefaultConfigurationsIterator.next();
                    java.lang.String newConfigurationPropertyName = newConfigurationEntry.getKey();
                    if (oldServiceDefaultConfigs.containsKey(newConfigurationPropertyName) && (!existingConfigurations.containsKey(newConfigurationPropertyName))) {
                        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.info("The property {}/{} exists in both {} and {} but is not part of the current set of configurations and will therefore not be included in the configuration merge", configurationType, newConfigurationPropertyName, sourceStackId, targetStackId);
                        newDefaultConfigurationsIterator.remove();
                    }
                } 
            }
            for (java.lang.String clusterConfigType : clusterConfigTypes) {
                if (processedClusterConfigTypes.contains(clusterConfigType)) {
                    newServiceDefaultConfigsByType.remove(clusterConfigType);
                } else {
                    processedClusterConfigTypes.add(clusterConfigType);
                }
            }
            java.util.Set<java.lang.String> configTypes = newServiceDefaultConfigsByType.keySet();
            org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.LOG.warn("The upgrade will create the following configurations for stack {}: {}", targetStackId, org.apache.commons.lang.StringUtils.join(configTypes, ','));
            java.lang.String serviceVersionNote = java.lang.String.format("%s %s %s", direction.getText(true), direction.getPreposition(), upgradeContext.getRepositoryVersion().getVersion());
            configHelper.createConfigTypes(cluster, targetStackId, controller, newServiceDefaultConfigsByType, userName, serviceVersionNote);
            configsChanged = true;
        }
        if (configsChanged) {
            m_configHelperProvider.get().updateAgentConfigs(java.util.Collections.singleton(cluster.getClusterName()));
        }
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getReadOnlyProperties(org.apache.ambari.server.state.StackId stackId, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> readOnlyProperties = new java.util.HashMap<>();
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> properties = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties = m_ambariMetaInfoProvider.get().getStackProperties(stackId.getStackName(), stackId.getStackVersion());
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> serviceProperties = m_ambariMetaInfoProvider.get().getServiceProperties(stackId.getStackName(), stackId.getStackVersion(), serviceName);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(stackProperties)) {
            properties.addAll(stackProperties);
        }
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(serviceProperties)) {
            properties.addAll(serviceProperties);
        }
        for (org.apache.ambari.server.state.PropertyInfo property : properties) {
            org.apache.ambari.server.state.ValueAttributesInfo valueAttributes = property.getPropertyValueAttributes();
            if ((null != valueAttributes) && (valueAttributes.getReadOnly() == java.lang.Boolean.TRUE)) {
                java.lang.String type = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(property.getFilename());
                java.util.Set<java.lang.String> readOnlyPropertiesForType = readOnlyProperties.get(type);
                if (null == readOnlyPropertiesForType) {
                    readOnlyPropertiesForType = new java.util.HashSet<>();
                    readOnlyProperties.put(type, readOnlyPropertiesForType);
                }
                readOnlyPropertiesForType.add(property.getName());
            }
        }
        return readOnlyProperties;
    }
}