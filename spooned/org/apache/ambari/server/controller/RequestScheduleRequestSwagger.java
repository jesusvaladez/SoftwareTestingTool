package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface RequestScheduleRequestSwagger {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE)
    org.apache.ambari.server.controller.RequestScheduleRequestSwagger.RequestScheduleRequestElement getRequestScheduleRequest();

    interface RequestScheduleRequestElement {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_PROPERTY_ID)
        java.util.List<org.apache.ambari.server.controller.RequestScheduleRequestSwagger.RequestScheduleRequestElement.BatchRequest> getBatch();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.SCHEDULE_PROPERTY_ID)
        org.apache.ambari.server.controller.RequestScheduleRequestSwagger.RequestScheduleRequestElement.ScheduleRequest getSchedule();

        interface BatchRequest {
            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUESTS_PROPERTY_ID)
            java.util.List<org.apache.ambari.server.controller.RequestScheduleRequestSwagger.RequestScheduleRequestElement.BatchRequest.BatchRequestRequest> getBatchRequests();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_SETTINGS)
            org.apache.ambari.server.controller.RequestScheduleRequestSwagger.RequestScheduleRequestElement.BatchRequest.BatchSettings getBatchSettings();

            interface BatchRequestRequest {
                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ORDER_ID_PROPERTY_ID)
                java.lang.Long getOrderId();

                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TYPE_PROPERTY_ID)
                java.lang.String getType();

                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.URI_PROPERTY_ID)
                java.lang.String getUri();

                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.api.services.parsers.RequestBodyParser.REQUEST_BLOB_TITLE)
                org.apache.ambari.server.controller.RequestScheduleRequestSwagger.RequestScheduleRequestElement.BatchRequest.BatchRequestRequest.RequestBodyInfo getRequestBodyInfo();

                interface RequestBodyInfo {
                    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_INFO)
                    org.apache.ambari.server.controller.RequestPostRequest.RequestInfo getRequestInfo();

                    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_RESOURCE_FILTER_ID)
                    java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> getRequestResourceFilters();
                }
            }

            interface BatchSettings {
                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_SEPARATION_IN_SECONDS_PROPERTY_ID)
                java.lang.Integer getBatchSeparationInSeconds();

                @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TASK_FAILURE_TOLERANCE_LIMIT_PROPERTY_ID)
                java.lang.Integer getTaskFailureToleranceLimit();
            }
        }

        interface ScheduleRequest {
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

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.START_TIME_PROPERTY_ID)
            java.lang.String getStartTime();

            @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.END_TIME_PROPERTY_ID)
            java.lang.String getEndTime();
        }
    }
}