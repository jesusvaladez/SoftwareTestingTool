package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface RequestResponse extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS)
    org.apache.ambari.server.controller.RequestResponse.RequestStatusInfo getRequestStatusInfo();

    interface RequestStatusInfo {
        @io.swagger.annotations.ApiModelProperty(name = "id")
        java.lang.String getRequestId();

        @io.swagger.annotations.ApiModelProperty(name = "request_status")
        java.lang.String getStatus();

        @io.swagger.annotations.ApiModelProperty(name = "aborted_task_count")
        int getAbortedTaskCount();

        @io.swagger.annotations.ApiModelProperty(name = "cluster_name")
        java.lang.String getClusterName();

        @io.swagger.annotations.ApiModelProperty(name = "completed_task_count")
        java.lang.String getCompletedTaskCount();

        @io.swagger.annotations.ApiModelProperty(name = "create_time")
        long getCreateTime();

        @io.swagger.annotations.ApiModelProperty(name = "start_time")
        java.lang.String getStartTime();

        @io.swagger.annotations.ApiModelProperty(name = "end_time")
        java.lang.String getEndTime();

        @io.swagger.annotations.ApiModelProperty(name = "exclusive")
        boolean isExclusive();

        @io.swagger.annotations.ApiModelProperty(name = "failed_task_count")
        int getFailedTaskCount();

        @io.swagger.annotations.ApiModelProperty(name = "inputs")
        java.lang.String getInputs();

        @io.swagger.annotations.ApiModelProperty(name = "operation_level")
        java.lang.String getOperationLevel();

        @io.swagger.annotations.ApiModelProperty(name = "progress_percent")
        double getProgressPercent();

        @io.swagger.annotations.ApiModelProperty(name = "queued_task_count")
        int getQueuedTaskCount();

        @io.swagger.annotations.ApiModelProperty(name = "request_context")
        java.lang.String getRequestContext();

        @io.swagger.annotations.ApiModelProperty(name = "request_schedule")
        java.lang.String getRequestSchedule();

        @io.swagger.annotations.ApiModelProperty(name = "request_schedule_id")
        long getRequestScheduleId();

        @io.swagger.annotations.ApiModelProperty(name = "resource_filters")
        java.util.List<org.apache.ambari.server.controller.RequestPostRequest.RequestResourceFilter> getResourceFilters();

        @io.swagger.annotations.ApiModelProperty(name = "task_count")
        int getTaskCount();

        @io.swagger.annotations.ApiModelProperty(name = "type")
        java.lang.String getType();
    }
}