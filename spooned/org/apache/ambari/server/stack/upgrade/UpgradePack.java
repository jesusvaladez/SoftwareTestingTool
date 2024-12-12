package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
@javax.xml.bind.annotation.XmlRootElement(name = "upgrade")
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class UpgradePack {
    private static final java.lang.String ALL_VERSIONS = "*";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.UpgradePack.class);

    private java.lang.String name;

    @javax.xml.bind.annotation.XmlElement(name = "source")
    private java.lang.String source;

    @javax.xml.bind.annotation.XmlElement(name = "source-stack")
    private java.lang.String sourceStack;

    @javax.xml.bind.annotation.XmlElement(name = "target")
    private java.lang.String target;

    @javax.xml.bind.annotation.XmlElement(name = "target-stack")
    private java.lang.String targetStack;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "order")
    @javax.xml.bind.annotation.XmlElement(name = "group")
    private java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groups = new java.util.ArrayList<>();

    @javax.xml.bind.annotation.XmlElement(name = "prerequisite-checks")
    private org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteChecks prerequisiteChecks;

    @javax.xml.bind.annotation.XmlElement(name = "orchestration-options-class")
    private java.lang.String orchestrationOptionsClass;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "processing")
    @javax.xml.bind.annotation.XmlElement(name = "service")
    private java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingService> processing;

    @javax.xml.bind.annotation.XmlElement(name = "skip-failures")
    private boolean skipFailures = false;

    @javax.xml.bind.annotation.XmlElement(name = "downgrade-allowed", required = false, defaultValue = "true")
    private boolean downgradeAllowed = true;

    @javax.xml.bind.annotation.XmlElement(name = "skip-service-check-failures")
    private boolean skipServiceCheckFailures = false;

    @javax.xml.bind.annotation.XmlTransient
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent>> m_process = null;

    @javax.xml.bind.annotation.XmlTransient
    private final java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.AddComponentTask> m_addComponentTasks = new java.util.LinkedHashMap<>();

    @javax.xml.bind.annotation.XmlTransient
    private boolean m_resolvedGroups = false;

    @javax.xml.bind.annotation.XmlElement(name = "type", defaultValue = "rolling")
    private org.apache.ambari.spi.upgrade.UpgradeType type;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "upgrade-path")
    @javax.xml.bind.annotation.XmlElement(name = "intermediate-stack")
    private java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.IntermediateStack> intermediateStacks;

    @javax.xml.bind.annotation.XmlTransient
    private org.apache.ambari.server.state.StackId ownerStackId;

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(type, source, sourceStack, target, targetStack, ownerStackId);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof org.apache.ambari.server.stack.upgrade.UpgradePack)) {
            return false;
        }
        org.apache.ambari.server.stack.upgrade.UpgradePack that = ((org.apache.ambari.server.stack.upgrade.UpgradePack) (object));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(type, that.type).append(source, that.source).append(sourceStack, that.sourceStack).append(target, that.targetStack).append(ownerStackId, that.ownerStackId).isEquals();
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getSource() {
        return source;
    }

    public java.lang.String getSourceStack() {
        return sourceStack;
    }

    public java.lang.String getTarget() {
        return target;
    }

    public java.lang.String getTargetStack() {
        return targetStack;
    }

    public org.apache.ambari.spi.upgrade.UpgradeType getType() {
        return type;
    }

    public java.util.List<java.lang.String> getPrerequisiteChecks() {
        if (prerequisiteChecks == null) {
            return new java.util.ArrayList<>();
        }
        return new java.util.ArrayList<>(prerequisiteChecks.checks);
    }

    public org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteCheckConfig getPrerequisiteCheckConfig() {
        if (prerequisiteChecks == null) {
            return new org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteCheckConfig();
        }
        return prerequisiteChecks.configuration;
    }

    public void mergePrerequisiteChecks(org.apache.ambari.server.stack.upgrade.UpgradePack pack) {
        org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteChecks newPrereqChecks = pack.prerequisiteChecks;
        if (prerequisiteChecks == null) {
            prerequisiteChecks = newPrereqChecks;
            return;
        }
        if (newPrereqChecks == null) {
            return;
        }
        if (prerequisiteChecks.checks == null) {
            prerequisiteChecks.checks = new java.util.ArrayList<>();
        }
        if (newPrereqChecks.checks != null) {
            prerequisiteChecks.checks.addAll(newPrereqChecks.checks);
        }
        if (newPrereqChecks.configuration == null) {
            return;
        }
        if (prerequisiteChecks.configuration == null) {
            prerequisiteChecks.configuration = newPrereqChecks.configuration;
            return;
        }
        if (prerequisiteChecks.configuration.globalProperties == null) {
            prerequisiteChecks.configuration.globalProperties = new java.util.ArrayList<>();
        }
        if (prerequisiteChecks.configuration.prerequisiteCheckProperties == null) {
            prerequisiteChecks.configuration.prerequisiteCheckProperties = new java.util.ArrayList<>();
        }
        if (newPrereqChecks.configuration.globalProperties != null) {
            prerequisiteChecks.configuration.globalProperties.addAll(newPrereqChecks.configuration.globalProperties);
        }
        if (newPrereqChecks.configuration.prerequisiteCheckProperties != null) {
            prerequisiteChecks.configuration.prerequisiteCheckProperties.addAll(newPrereqChecks.configuration.prerequisiteCheckProperties);
        }
    }

    public void mergeProcessing(org.apache.ambari.server.stack.upgrade.UpgradePack pack) {
        java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingService> list = pack.processing;
        if (list == null) {
            return;
        }
        if (processing == null) {
            processing = list;
            return;
        }
        processing.addAll(list);
        initializeProcessingComponentMappings();
    }

    public java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.IntermediateStack> getIntermediateStacks() {
        return intermediateStacks;
    }

    public boolean isComponentFailureAutoSkipped() {
        return skipFailures;
    }

    public boolean isServiceCheckFailureAutoSkipped() {
        return skipServiceCheckFailures;
    }

    public java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> getAllGroups() {
        return groups;
    }

    public java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> getGroups(org.apache.ambari.server.stack.upgrade.Direction direction) {
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> list;
        if (direction.isUpgrade()) {
            list = groups;
        } else {
            switch (type) {
                case NON_ROLLING :
                    list = getDowngradeGroupsForNonrolling();
                    break;
                case HOST_ORDERED :
                case ROLLING :
                default :
                    list = getDowngradeGroupsForRolling();
                    break;
            }
        }
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> checked = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.Grouping group : list) {
            if ((null == group.intendedDirection) || (direction == group.intendedDirection)) {
                checked.add(group);
            }
        }
        return checked;
    }

    public boolean isDowngradeAllowed() {
        return downgradeAllowed;
    }

    public boolean canBeApplied(java.lang.String targetVersion) {
        java.lang.String regexPattern = getTarget().replaceAll("\\.", "\\\\.");
        regexPattern = regexPattern.replaceAll("\\\\\\.\\*", "(\\\\\\.\\\\d+)?");
        regexPattern = regexPattern.concat("(-\\d+)?");
        return java.util.regex.Pattern.matches(regexPattern, targetVersion);
    }

    private java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> getDowngradeGroupsForRolling() {
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> reverse = new java.util.ArrayList<>();
        if (groups.size() == 1) {
            return groups;
        }
        int idx = 0;
        int iter = 0;
        java.util.Iterator<org.apache.ambari.server.stack.upgrade.Grouping> it = groups.iterator();
        while (it.hasNext()) {
            org.apache.ambari.server.stack.upgrade.Grouping g = it.next();
            if (org.apache.ambari.server.stack.upgrade.ClusterGrouping.class.isInstance(g)) {
                reverse.add(g);
                idx++;
            } else if ((iter + 1) < groups.size()) {
                org.apache.ambari.server.stack.upgrade.Grouping peek = groups.get(iter + 1);
                if (org.apache.ambari.server.stack.upgrade.ServiceCheckGrouping.class.isInstance(peek)) {
                    reverse.add(idx, it.next());
                    reverse.add(idx, g);
                    iter++;
                } else {
                    reverse.add(idx, g);
                }
            }
            iter++;
        } 
        return reverse;
    }

    private java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> getDowngradeGroupsForNonrolling() {
        return new java.util.ArrayList<>(groups);
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent>> getTasks() {
        return m_process;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.AddComponentTask> getAddComponentTasks() {
        return m_addComponentTasks;
    }

    void afterUnmarshal(javax.xml.bind.Unmarshaller unmarshaller, java.lang.Object parent) {
        initializeProcessingComponentMappings();
        initializeAddComponentTasks();
    }

    private void initializeProcessingComponentMappings() {
        m_process = new java.util.LinkedHashMap<>();
        if (org.apache.commons.collections.CollectionUtils.isEmpty(processing)) {
            return;
        }
        for (org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingService svc : processing) {
            java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent> componentMap = m_process.get(svc.name);
            if (null == componentMap) {
                componentMap = new java.util.LinkedHashMap<>();
                m_process.put(svc.name, componentMap);
            }
            for (org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc : svc.components) {
                if (pc != null) {
                    componentMap.put(pc.name, pc);
                } else {
                    org.apache.ambari.server.stack.upgrade.UpgradePack.LOG.warn("ProcessingService {} has null amongst it's values (total {} components)", svc.name, svc.components.size());
                }
            }
        }
    }

    private void initializeAddComponentTasks() {
        for (org.apache.ambari.server.stack.upgrade.Grouping group : groups) {
            if (org.apache.ambari.server.stack.upgrade.ClusterGrouping.class.isInstance(group)) {
                java.util.List<org.apache.ambari.server.stack.upgrade.ExecuteStage> executeStages = ((org.apache.ambari.server.stack.upgrade.ClusterGrouping) (group)).executionStages;
                for (org.apache.ambari.server.stack.upgrade.ExecuteStage executeStage : executeStages) {
                    org.apache.ambari.server.stack.upgrade.Task task = executeStage.task;
                    if (task.getType() == org.apache.ambari.server.stack.upgrade.Task.Type.ADD_COMPONENT) {
                        org.apache.ambari.server.stack.upgrade.AddComponentTask addComponentTask = ((org.apache.ambari.server.stack.upgrade.AddComponentTask) (task));
                        m_addComponentTasks.put(addComponentTask.getServiceAndComponentAsString(), addComponentTask);
                    }
                }
            }
        }
    }

    public boolean isAllTarget() {
        return org.apache.ambari.server.stack.upgrade.UpgradePack.ALL_VERSIONS.equals(target) && org.apache.ambari.server.stack.upgrade.UpgradePack.ALL_VERSIONS.equals(targetStack);
    }

    public static class OrderService {
        @javax.xml.bind.annotation.XmlAttribute(name = "name")
        public java.lang.String serviceName;

        @javax.xml.bind.annotation.XmlElement(name = "component")
        public java.util.List<java.lang.String> components;
    }

    public static class ProcessingService {
        @javax.xml.bind.annotation.XmlAttribute
        public java.lang.String name;

        @javax.xml.bind.annotation.XmlElement(name = "component")
        public java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent> components;
    }

    public static class ProcessingComponent {
        @javax.xml.bind.annotation.XmlAttribute
        public java.lang.String name;

        @javax.xml.bind.annotation.XmlElementWrapper(name = "pre-upgrade")
        @javax.xml.bind.annotation.XmlElement(name = "task")
        public java.util.List<org.apache.ambari.server.stack.upgrade.Task> preTasks;

        @javax.xml.bind.annotation.XmlElement(name = "pre-downgrade")
        private org.apache.ambari.server.stack.upgrade.UpgradePack.DowngradeTasks preDowngradeXml;

        @javax.xml.bind.annotation.XmlTransient
        public java.util.List<org.apache.ambari.server.stack.upgrade.Task> preDowngradeTasks;

        @javax.xml.bind.annotation.XmlElementWrapper(name = "upgrade")
        @javax.xml.bind.annotation.XmlElement(name = "task")
        public java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks;

        @javax.xml.bind.annotation.XmlElementWrapper(name = "post-upgrade")
        @javax.xml.bind.annotation.XmlElement(name = "task")
        public java.util.List<org.apache.ambari.server.stack.upgrade.Task> postTasks;

        @javax.xml.bind.annotation.XmlElement(name = "post-downgrade")
        private org.apache.ambari.server.stack.upgrade.UpgradePack.DowngradeTasks postDowngradeXml;

        @javax.xml.bind.annotation.XmlTransient
        public java.util.List<org.apache.ambari.server.stack.upgrade.Task> postDowngradeTasks;

        void afterUnmarshal(javax.xml.bind.Unmarshaller unmarshaller, java.lang.Object parent) {
            if (null != preDowngradeXml) {
                preDowngradeTasks = (preDowngradeXml.copyUpgrade) ? preTasks : preDowngradeXml.tasks;
            }
            if (null != postDowngradeXml) {
                postDowngradeTasks = (postDowngradeXml.copyUpgrade) ? postTasks : postDowngradeXml.tasks;
            }
            org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingService service = ((org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingService) (parent));
            if (org.apache.ambari.server.stack.upgrade.UpgradePack.LOG.isDebugEnabled()) {
                org.apache.ambari.server.stack.upgrade.UpgradePack.LOG.debug("Processing component {}/{} preUpgrade={} postUpgrade={} preDowngrade={} postDowngrade={}", preTasks, preDowngradeTasks, postTasks, postDowngradeTasks);
            }
            if ((null != preTasks) && (null == preDowngradeTasks)) {
                java.lang.String error = java.lang.String.format("Upgrade pack must contain pre-downgrade elements if " + "pre-upgrade exists for processing component %s/%s", service.name, name);
                throw new java.lang.RuntimeException(error);
            }
            if ((null != postTasks) && (null == postDowngradeTasks)) {
                java.lang.String error = java.lang.String.format("Upgrade pack must contain post-downgrade elements if " + "post-upgrade exists for processing component %s/%s", service.name, name);
                throw new java.lang.RuntimeException(error);
            }
            initializeTasks(service.name, preTasks);
            initializeTasks(service.name, postTasks);
            initializeTasks(service.name, tasks);
            initializeTasks(service.name, preDowngradeTasks);
            initializeTasks(service.name, postDowngradeTasks);
        }

        private void initializeTasks(java.lang.String service, java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks) {
            if (org.apache.commons.collections.CollectionUtils.isEmpty(tasks)) {
                return;
            }
            for (org.apache.ambari.server.stack.upgrade.Task task : tasks) {
                if (org.apache.ambari.server.stack.upgrade.Task.Type.CONFIGURE == task.getType()) {
                    ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (task)).associatedService = service;
                } else if (org.apache.ambari.server.stack.upgrade.Task.Type.CREATE_AND_CONFIGURE == task.getType()) {
                    ((org.apache.ambari.server.stack.upgrade.CreateAndConfigureTask) (task)).associatedService = service;
                }
            }
        }
    }

    public static class IntermediateStack {
        @javax.xml.bind.annotation.XmlAttribute
        public java.lang.String version;
    }

    public static class PrerequisiteChecks {
        @javax.xml.bind.annotation.XmlElement(name = "check", type = java.lang.String.class)
        public java.util.List<java.lang.String> checks = new java.util.ArrayList<>();

        @javax.xml.bind.annotation.XmlElement(name = "configuration")
        public org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteCheckConfig configuration;
    }

    public static class PrerequisiteCheckConfig {
        @javax.xml.bind.annotation.XmlElement(name = "property")
        public java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteProperty> globalProperties;

        @javax.xml.bind.annotation.XmlElement(name = "check-properties")
        public java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteCheckProperties> prerequisiteCheckProperties;

        public java.util.Map<java.lang.String, java.lang.String> getGlobalProperties() {
            if (globalProperties == null) {
                return null;
            }
            java.util.Map<java.lang.String, java.lang.String> result = new java.util.HashMap<>();
            for (org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteProperty property : globalProperties) {
                result.put(property.name, property.value);
            }
            return result;
        }

        public java.util.Map<java.lang.String, java.lang.String> getAllProperties() {
            java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
            if (null != globalProperties) {
                for (org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteProperty property : globalProperties) {
                    properties.put(property.name, property.value);
                }
            }
            if (null != prerequisiteCheckProperties) {
                for (org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteCheckProperties checkProperties : prerequisiteCheckProperties) {
                    properties.putAll(checkProperties.getProperties());
                }
            }
            return properties;
        }

        public java.util.Map<java.lang.String, java.lang.String> getCheckProperties(java.lang.String checkName) {
            if (prerequisiteCheckProperties == null) {
                return null;
            }
            for (org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteCheckProperties checkProperties : prerequisiteCheckProperties) {
                if (checkProperties.name.equalsIgnoreCase(checkName)) {
                    return checkProperties.getProperties();
                }
            }
            return null;
        }
    }

    public static class PrerequisiteCheckProperties {
        @javax.xml.bind.annotation.XmlAttribute
        public java.lang.String name;

        @javax.xml.bind.annotation.XmlElement(name = "property")
        public java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteProperty> properties;

        public java.util.Map<java.lang.String, java.lang.String> getProperties() {
            if (properties == null) {
                return null;
            }
            java.util.Map<java.lang.String, java.lang.String> result = new java.util.HashMap<>();
            for (org.apache.ambari.server.stack.upgrade.UpgradePack.PrerequisiteProperty property : properties) {
                result.put(property.name, property.value);
            }
            return result;
        }
    }

    public static class PrerequisiteProperty {
        @javax.xml.bind.annotation.XmlAttribute
        public java.lang.String name;

        @javax.xml.bind.annotation.XmlValue
        public java.lang.String value;
    }

    private static class DowngradeTasks {
        @javax.xml.bind.annotation.XmlAttribute(name = "copy-upgrade")
        private boolean copyUpgrade = false;

        @javax.xml.bind.annotation.XmlElement(name = "task")
        private java.util.List<org.apache.ambari.server.stack.upgrade.Task> tasks = new java.util.ArrayList<>();
    }

    public boolean anyGroupTaskMatch(java.util.function.Predicate<org.apache.ambari.server.stack.upgrade.Task> taskPredicate) {
        return getAllGroups().stream().filter(org.apache.ambari.server.stack.upgrade.ClusterGrouping.class::isInstance).flatMap(group -> ((org.apache.ambari.server.stack.upgrade.ClusterGrouping) (group)).executionStages.stream()).map(executeStage -> executeStage.task).anyMatch(taskPredicate);
    }

    public void setOwnerStackId(org.apache.ambari.server.state.StackId stackId) {
        ownerStackId = (null == ownerStackId) ? stackId : ownerStackId;
    }

    public org.apache.ambari.server.state.StackId getOwnerStackId() {
        return ownerStackId;
    }

    public java.lang.String getOrchestrationOptions() {
        return orchestrationOptionsClass;
    }
}