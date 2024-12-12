package org.apache.ambari.msi;
public class TaskProvider extends org.apache.ambari.msi.AbstractResourceProvider {
    protected static final java.lang.String TASK_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "cluster_name");

    protected static final java.lang.String TASK_REQUEST_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "request_id");

    protected static final java.lang.String TASK_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "id");

    protected static final java.lang.String TASK_STATUS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "status");

    protected static final java.lang.String TASK_EXIT_CODE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "exit_code");

    protected static final java.lang.String TASK_STDERR_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "stderr");

    protected static final java.lang.String TASK_STOUT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "stdout");

    public TaskProvider(org.apache.ambari.msi.ClusterDefinition clusterDefinition) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Task, clusterDefinition);
    }

    @java.lang.Override
    protected java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources() {
        return getClusterDefinition().getTaskResources();
    }

    @java.lang.Override
    public void updateProperties(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        java.lang.Integer taskId = ((java.lang.Integer) (resource.getPropertyValue(org.apache.ambari.msi.TaskProvider.TASK_ID_PROPERTY_ID)));
        org.apache.ambari.msi.StateProvider.Process process = getClusterDefinition().getProcess(taskId);
        if (process != null) {
            resource.setProperty(org.apache.ambari.msi.TaskProvider.TASK_STATUS_PROPERTY_ID, process.isRunning() ? "IN_PROGRESS" : "COMPLETED");
            java.util.Set<java.lang.String> propertyIds = getRequestPropertyIds(request, predicate);
            if (org.apache.ambari.msi.AbstractResourceProvider.contains(propertyIds, org.apache.ambari.msi.TaskProvider.TASK_EXIT_CODE_PROPERTY_ID)) {
                resource.setProperty(org.apache.ambari.msi.TaskProvider.TASK_EXIT_CODE_PROPERTY_ID, process.getExitCode());
            }
            if (org.apache.ambari.msi.AbstractResourceProvider.contains(propertyIds, org.apache.ambari.msi.TaskProvider.TASK_STDERR_PROPERTY_ID)) {
                resource.setProperty(org.apache.ambari.msi.TaskProvider.TASK_STDERR_PROPERTY_ID, process.getError());
            }
            if (org.apache.ambari.msi.AbstractResourceProvider.contains(propertyIds, org.apache.ambari.msi.TaskProvider.TASK_STOUT_PROPERTY_ID)) {
                resource.setProperty(org.apache.ambari.msi.TaskProvider.TASK_STOUT_PROPERTY_ID, process.getOutput());
            }
        }
    }

    @java.lang.Override
    public int updateProperties(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, java.lang.Object> properties) {
        return -1;
    }
}