package org.apache.ambari.server.controller.jmx;
import javax.annotation.Nullable;
public final class JMXMetricHolder {
    private static final java.lang.String NAME_KEY = "name";

    private java.util.List<java.util.Map<java.lang.String, java.lang.Object>> beans;

    public java.util.List<java.util.Map<java.lang.String, java.lang.Object>> getBeans() {
        return beans;
    }

    public void setBeans(java.util.List<java.util.Map<java.lang.String, java.lang.Object>> beans) {
        this.beans = beans;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
        for (java.util.Map<java.lang.String, java.lang.Object> map : beans) {
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : map.entrySet()) {
                stringBuilder.append("    ").append(entry).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public java.util.List<java.lang.Object> findAll(java.util.List<java.lang.String> properties) {
        return properties.stream().map(this::find).filter(java.util.Optional::isPresent).map(java.util.Optional::get).collect(java.util.stream.Collectors.toList());
    }

    public java.util.Optional<java.lang.Object> find(java.lang.String pattern) {
        org.apache.ambari.server.controller.jmx.JMXMetricHolder.JmxPattern jmxPattern = org.apache.ambari.server.controller.jmx.JMXMetricHolder.JmxPattern.parse(pattern);
        return beans.stream().map(jmxPattern::extract).filter(java.util.Optional::isPresent).map(java.util.Optional::get).findFirst();
    }

    private static class JmxPattern {
        private static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile("(.*?)\\[(\\S+?)\\]");

        private final java.lang.String beanName;

        private final java.lang.String propertyName;

        @javax.annotation.Nullable
        private final java.lang.String key;

        public static org.apache.ambari.server.controller.jmx.JMXMetricHolder.JmxPattern parse(java.lang.String property) {
            java.lang.String beanName = property.split("/")[0];
            java.lang.String propertyName = property.split("/")[1];
            java.lang.String key = null;
            java.util.regex.Matcher matcher = org.apache.ambari.server.controller.jmx.JMXMetricHolder.JmxPattern.PATTERN.matcher(propertyName);
            if (matcher.matches()) {
                propertyName = matcher.group(1);
                key = matcher.group(2);
            }
            return new org.apache.ambari.server.controller.jmx.JMXMetricHolder.JmxPattern(beanName, propertyName, key);
        }

        private JmxPattern(java.lang.String beanName, java.lang.String propertyName, java.lang.String key) {
            this.beanName = beanName;
            this.propertyName = propertyName;
            this.key = key;
        }

        public java.util.Optional<java.lang.Object> extract(java.util.Map<java.lang.String, java.lang.Object> bean) {
            return beanName.equals(name(bean)) ? java.util.Optional.ofNullable(lookupByKey(bean.get(propertyName))) : java.util.Optional.empty();
        }

        public java.lang.Object lookupByKey(java.lang.Object bean) {
            return (key != null) && (bean instanceof java.util.Map) ? ((java.util.Map) (bean)).get(key) : bean;
        }

        private java.lang.String name(java.util.Map<java.lang.String, java.lang.Object> bean) {
            return bean.containsKey(org.apache.ambari.server.controller.jmx.JMXMetricHolder.NAME_KEY) ? ((java.lang.String) (bean.get(org.apache.ambari.server.controller.jmx.JMXMetricHolder.NAME_KEY))) : null;
        }
    }
}