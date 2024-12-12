package org.apache.ambari.server.state.alert;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class AmsSource extends org.apache.ambari.server.state.alert.Source {
    @com.google.gson.annotations.SerializedName("uri")
    private org.apache.ambari.server.state.UriInfo uri = null;

    @com.google.gson.annotations.SerializedName("ams")
    private org.apache.ambari.server.state.alert.AmsSource.AmsInfo amsInfo = null;

    @com.fasterxml.jackson.annotation.JsonProperty("ams")
    public org.apache.ambari.server.state.alert.AmsSource.AmsInfo getAmsInfo() {
        return amsInfo;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("uri")
    public org.apache.ambari.server.state.UriInfo getUri() {
        return uri;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), uri, amsInfo);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        org.apache.ambari.server.state.alert.AmsSource other = ((org.apache.ambari.server.state.alert.AmsSource) (obj));
        return java.util.Objects.equals(uri, other.uri) && java.util.Objects.equals(amsInfo, other.amsInfo);
    }

    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    public static class AmsInfo {
        @com.google.gson.annotations.SerializedName("metric_list")
        private java.util.List<java.lang.String> metricList;

        private java.lang.String value;

        private int interval = 60;

        private java.lang.String compute;

        @com.google.gson.annotations.SerializedName("app_id")
        private java.lang.String appId;

        @com.google.gson.annotations.SerializedName("minimum_value")
        private int minimumValue;

        @com.fasterxml.jackson.annotation.JsonProperty("app_id")
        public java.lang.String getAppId() {
            return appId;
        }

        public int getInterval() {
            return interval;
        }

        public java.lang.String getCompute() {
            return compute;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("metric_list")
        public java.util.List<java.lang.String> getMetricList() {
            return metricList;
        }

        public java.lang.String getValue() {
            return value;
        }

        @com.fasterxml.jackson.annotation.JsonProperty("minimum_value")
        public int getMinimumValue() {
            return minimumValue;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.state.alert.AmsSource.AmsInfo amsInfo = ((org.apache.ambari.server.state.alert.AmsSource.AmsInfo) (o));
            if (interval != amsInfo.interval)
                return false;

            if (minimumValue != amsInfo.minimumValue)
                return false;

            if (appId != null ? !appId.equals(amsInfo.appId) : amsInfo.appId != null)
                return false;

            if (compute != null ? !compute.equals(amsInfo.compute) : amsInfo.compute != null)
                return false;

            if (metricList != null ? !metricList.equals(amsInfo.metricList) : amsInfo.metricList != null)
                return false;

            if (value != null ? !value.equals(amsInfo.value) : amsInfo.value != null)
                return false;

            return true;
        }

        @java.lang.Override
        public int hashCode() {
            int result = (metricList != null) ? metricList.hashCode() : 0;
            result = (31 * result) + (value != null ? value.hashCode() : 0);
            result = (31 * result) + interval;
            result = (31 * result) + (compute != null ? compute.hashCode() : 0);
            result = (31 * result) + (appId != null ? appId.hashCode() : 0);
            result = (31 * result) + minimumValue;
            return result;
        }
    }
}