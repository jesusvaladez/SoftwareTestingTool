package org.apache.ambari.server.state.alert;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class MetricSource extends org.apache.ambari.server.state.alert.Source {
    @com.google.gson.annotations.SerializedName("uri")
    private org.apache.ambari.server.state.UriInfo uri = null;

    @com.google.gson.annotations.SerializedName("jmx")
    private org.apache.ambari.server.state.alert.MetricSource.JmxInfo jmxInfo = null;

    @com.google.gson.annotations.SerializedName("ganglia")
    private java.lang.String gangliaInfo = null;

    @com.fasterxml.jackson.annotation.JsonProperty("jmx")
    public org.apache.ambari.server.state.alert.MetricSource.JmxInfo getJmxInfo() {
        return jmxInfo;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("ganglia")
    public java.lang.String getGangliaInfo() {
        return gangliaInfo;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("uri")
    public org.apache.ambari.server.state.UriInfo getUri() {
        return uri;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), gangliaInfo, uri, jmxInfo);
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
        org.apache.ambari.server.state.alert.MetricSource other = ((org.apache.ambari.server.state.alert.MetricSource) (obj));
        return (java.util.Objects.equals(gangliaInfo, other.gangliaInfo) && java.util.Objects.equals(uri, other.uri)) && java.util.Objects.equals(jmxInfo, other.jmxInfo);
    }

    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    @com.fasterxml.jackson.annotation.JsonAutoDetect(fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY, getterVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE, setterVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE)
    public static class JmxInfo {
        @com.fasterxml.jackson.annotation.JsonProperty("property_list")
        @com.google.gson.annotations.SerializedName("property_list")
        private java.util.List<java.lang.String> propertyList;

        @com.google.gson.annotations.SerializedName("value")
        private java.lang.String value = "{0}";

        @com.fasterxml.jackson.annotation.JsonProperty("url_suffix")
        @com.google.gson.annotations.SerializedName("url_suffix")
        private java.lang.String urlSuffix = "/jmx";

        public java.util.List<java.lang.String> getPropertyList() {
            return propertyList;
        }

        public void setPropertyList(java.util.List<java.lang.String> propertyList) {
            this.propertyList = propertyList;
        }

        public void setValue(java.lang.String value) {
            this.value = value;
        }

        public org.apache.ambari.server.state.alert.MetricSource.Value getValue() {
            return new org.apache.ambari.server.state.alert.MetricSource.Value(value);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object object) {
            if (!org.apache.ambari.server.state.alert.MetricSource.JmxInfo.class.isInstance(object)) {
                return false;
            }
            org.apache.ambari.server.state.alert.MetricSource.JmxInfo other = ((org.apache.ambari.server.state.alert.MetricSource.JmxInfo) (object));
            java.util.List<java.lang.String> list1 = new java.util.ArrayList<>(propertyList);
            java.util.List<java.lang.String> list2 = new java.util.ArrayList<>(other.propertyList);
            return list1.equals(list2);
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hashCode(propertyList);
        }

        public java.lang.String getUrlSuffix() {
            return urlSuffix;
        }

        public java.util.Optional<java.lang.Number> eval(org.apache.ambari.server.controller.jmx.JMXMetricHolder jmxMetricHolder) {
            java.util.List<java.lang.Object> metrics = jmxMetricHolder.findAll(propertyList);
            if (metrics.isEmpty()) {
                return java.util.Optional.empty();
            } else {
                java.lang.Object value = getValue().eval(metrics);
                return value instanceof java.lang.Number ? java.util.Optional.of(((java.lang.Number) (value))) : java.util.Optional.empty();
            }
        }
    }

    public static class Value {
        private final java.lang.String value;

        public Value(java.lang.String value) {
            this.value = value;
        }

        public java.lang.Object eval(java.util.List<java.lang.Object> metrics) {
            org.springframework.expression.spel.support.SimpleEvaluationContext context = org.springframework.expression.spel.support.SimpleEvaluationContext.forReadWriteDataBinding().build();
            for (int i = 0; i < metrics.size(); i++) {
                context.setVariable("var" + i, metrics.get(i));
            }
            return new org.springframework.expression.spel.standard.SpelExpressionParser().parseExpression(value.replaceAll("(\\{(\\d+)\\})", "#var$2")).getValue(context);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return value;
        }
    }
}