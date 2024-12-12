package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface RequestScheduleResponseSwagger {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE)
    org.apache.ambari.server.controller.RequestScheduleResponseSwagger.RequestScheduleResponseElement getRequestScheduleResponse();

    interface RequestScheduleResponseElement {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ID_PROPERTY_ID)
        java.lang.Long getId();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME_PROPERTY_ID)
        java.lang.String getClusterName();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DESCRIPTION_PROPERTY_ID)
        java.lang.String getDescription();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.STATUS_PROPERTY_ID)
        java.lang.String getStatus();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_PROPERTY_ID)
        org.apache.ambari.server.controller.RequestScheduleResponseSwagger.RequestScheduleResponseElement.BatchResponse getBatch();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.SCHEDULE_PROPERTY_ID)
        org.apache.ambari.server.controller.RequestScheduleResponseSwagger.RequestScheduleResponseElement.ScheduleResponse getSchedule();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CREATE_USER_PROPERTY_ID)
        java.lang.String getCreateUser();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CREATE_TIME_PROPERTY_ID)
        java.lang.String getCreateTime();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.UPDATE_USER_PROPERTY_ID)
        java.lang.String getUpdateUser();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.UPDATE_TIME_PROPERTY_ID)
        java.lang.String getUpdateTime();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.LAST_EXECUTION_STATUS_PROPERTY_ID)
        java.lang.String getLastExecutionStatus();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.AUTHENTICATED_USER_PROPERTY_ID)
        java.lang.Integer getAuthenticatedUserId();

        interface BatchResponse {
            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_REQUESTS)
            java.util.List<org.apache.ambari.server.controller.RequestScheduleResponseSwagger.RequestScheduleResponseElement.BatchResponse.BatchRequestResponse> getBatchRequests();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_SETTINGS)
            org.apache.ambari.server.controller.RequestScheduleResponseSwagger.RequestScheduleResponseElement.BatchResponse.BatchSettings getBatchSettings();

            interface BatchRequestResponse {
                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ORDER_ID_PROPERTY_ID)
                java.lang.Long getOrderId();

                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_TYPE_PROPERTY_ID)
                java.lang.String getType();

                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_URI_PROPERTY_ID)
                java.lang.String getUri();

                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_BODY_PROPERTY_ID)
                java.lang.String getBody();

                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_STATUS_PROPERTY_ID)
                java.lang.String getStatus();

                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.RETURN_CODE_PROPERTY_ID)
                java.lang.Integer getReturnCode();

                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.RESPONSE_MESSAGE_PROPERTY_ID)
                java.lang.String getResponseMsg();
            }

            interface BatchSettings {
                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_SEPARATION_IN_SECONDS_PROPERTY_ID)
                java.lang.Integer getBatchSeparationInSeconds();

                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TASK_FAILURE_TOLERANCE_LIMIT_PROPERTY_ID)
                java.lang.Integer getTaskFailureToleranceLimit();
            }
        }

        interface ScheduleResponse {
            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.MINUTES_PROPERTY_ID)
            java.lang.String getMinutes();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.HOURS_PROPERTY_ID)
            java.lang.String getHours();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DAYS_OF_MONTH_PROPERTY_ID)
            java.lang.String getDaysOfMonth();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.MONTH_PROPERTY_ID)
            java.lang.String getMonth();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DAY_OF_WEEK_PROPERTY_ID)
            java.lang.String getDayOfWeek();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.YEAR_PROPERTY_ID)
            java.lang.String getYear();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.START_TIME_SNAKE_CASE_PROPERTY_ID)
            java.lang.String getStartTime();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.END_TIME_SNAKE_CASE_PROPERTY_ID)
            java.lang.String getEndTime();
        }
    }
}