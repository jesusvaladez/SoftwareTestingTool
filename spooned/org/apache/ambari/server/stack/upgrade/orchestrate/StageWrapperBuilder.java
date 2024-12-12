package org.apache.ambari.server.stack.upgrade.orchestrate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
public abstract class StageWrapperBuilder {
    private static final java.lang.String AUTO_SKIPPED_TASK_SUMMARY = "Pauses the upgrade if there were failed steps that were automatically skipped.";

    private static final java.lang.String AUTO_SKIPPED_MESSAGE = "There are failures that were automatically skipped.  Review the failures before continuing.";

    protected final org.apache.ambari.server.stack.upgrade.Grouping m_grouping;

    protected StageWrapperBuilder(org.apache.ambari.server.stack.upgrade.Grouping grouping) {
        m_grouping = grouping;
    }

    public abstract void add(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, org.apache.ambari.server.stack.HostsType hostsType, java.lang.String service, boolean clientOnly, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc, java.util.Map<java.lang.String, java.lang.String> params);

    public final java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) {
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers = beforeBuild(upgradeContext);
        stageWrappers = build(upgradeContext, stageWrappers);
        stageWrappers = afterBuild(upgradeContext, stageWrappers);
        return stageWrappers;
    }

    protected java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> beforeBuild(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) {
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers = new java.util.ArrayList<>(100);
        return stageWrappers;
    }

    public abstract java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers);

    protected java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> afterBuild(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(stageWrappers)) {
            return stageWrappers;
        }
        final boolean autoSkipFailures;
        if (m_grouping instanceof org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping) {
            autoSkipFailures = upgradeContext.isServiceCheckFailureAutoSkipped();
        } else {
            autoSkipFailures = upgradeContext.isComponentFailureAutoSkipped();
        }
        if ((m_grouping.supportsAutoSkipOnFailure && m_grouping.skippable) && autoSkipFailures) {
            org.apache.ambari.server.stack.upgrade.ServerActionTask skippedFailedCheck = new org.apache.ambari.server.stack.upgrade.ServerActionTask();
            skippedFailedCheck.implClass = org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.class.getName();
            skippedFailedCheck.summary = org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder.AUTO_SKIPPED_TASK_SUMMARY;
            skippedFailedCheck.messages.add(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder.AUTO_SKIPPED_MESSAGE);
            org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper skippedFailedTaskWrapper = new org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper(null, null, java.util.Collections.emptySet(), skippedFailedCheck);
            org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper skippedFailedStageWrapper = new org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, "Verifying Skipped Failures", skippedFailedTaskWrapper);
            stageWrappers.add(skippedFailedStageWrapper);
        }
        return stageWrappers;
    }

    protected java.lang.String getStageText(java.lang.String prefix, java.lang.String component, java.util.Set<java.lang.String> hosts, int batchNum, int totalBatches) {
        java.lang.String stageText = getStageText(prefix, component, hosts);
        java.lang.String batchText = (1 == totalBatches) ? "" : java.lang.String.format(" (Batch %s of %s)", batchNum, totalBatches);
        return stageText + batchText;
    }

    protected java.lang.String getStageText(java.lang.String prefix, java.lang.String component, java.util.Set<java.lang.String> hosts) {
        return java.lang.String.format("%s %s on %s%s", prefix, component, 1 == hosts.size() ? hosts.iterator().next() : java.lang.Integer.valueOf(hosts.size()), 1 == hosts.size() ? "" : " hosts");
    }

    protected java.util.List<org.apache.ambari.server.stack.upgrade.Task> resolveTasks(final org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context, boolean preTasks, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc) {
        if (null == pc) {
            return java.util.Collections.emptyList();
        }
        boolean forUpgrade = context.getDirection().isUpgrade();
        final java.util.List<org.apache.ambari.server.stack.upgrade.Task> interim;
        if (forUpgrade) {
            interim = (preTasks) ? pc.preTasks : pc.postTasks;
        } else {
            interim = (preTasks) ? pc.preDowngradeTasks : pc.postDowngradeTasks;
        }
        if (org.apache.commons.collections.CollectionUtils.isEmpty(interim)) {
            return java.util.Collections.emptyList();
        }
        java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.Task t : interim) {
            boolean taskPassesScoping = context.isScoped(t.scope);
            boolean taskPassesCondition = true;
            if ((null != t.condition) && (!t.condition.isSatisfied(context))) {
                taskPassesCondition = false;
            }
            if (taskPassesScoping && taskPassesCondition) {
                tasks.add(t);
            }
        }
        return tasks;
    }

    protected org.apache.ambari.server.stack.upgrade.Task resolveTask(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc) {
        if ((null != pc.tasks) && (1 == pc.tasks.size())) {
            if (context.isScoped(pc.tasks.get(0).scope)) {
                return pc.tasks.get(0);
            }
        }
        return null;
    }

    protected int getParallelHostCount(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext ctx, int defaultValue) {
        if (m_grouping.parallelScheduler != null) {
            int taskParallelism = m_grouping.parallelScheduler.maxDegreeOfParallelism;
            java.lang.String maxDegreeFromClusterEnv = ctx.getResolver().getValueFromDesiredConfigurations(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, "max_degree_parallelism");
            if (org.apache.commons.lang.StringUtils.isNotEmpty(maxDegreeFromClusterEnv) && org.apache.commons.lang.StringUtils.isNumeric(maxDegreeFromClusterEnv)) {
                taskParallelism = java.lang.Integer.parseInt(maxDegreeFromClusterEnv);
            }
            if (taskParallelism == org.apache.ambari.server.stack.upgrade.ParallelScheduler.DEFAULT_MAX_DEGREE_OF_PARALLELISM) {
                taskParallelism = ctx.getDefaultMaxDegreeOfParallelism();
            }
            return taskParallelism;
        }
        return defaultValue;
    }
}