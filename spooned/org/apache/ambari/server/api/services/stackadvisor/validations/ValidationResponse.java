package org.apache.ambari.server.api.services.stackadvisor.validations;
import org.codehaus.jackson.annotate.JsonProperty;
public class ValidationResponse extends org.apache.ambari.server.api.services.stackadvisor.StackAdvisorResponse {
    @org.codehaus.jackson.annotate.JsonProperty
    private java.util.Set<org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse.ValidationItem> items;

    public java.util.Set<org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse.ValidationItem> getItems() {
        return items;
    }

    public void setItems(java.util.Set<org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse.ValidationItem> items) {
        this.items = items;
    }

    public static class ValidationItem {
        @org.codehaus.jackson.annotate.JsonProperty
        private java.lang.String type;

        @org.codehaus.jackson.annotate.JsonProperty
        private java.lang.String level;

        @org.codehaus.jackson.annotate.JsonProperty
        private java.lang.String message;

        @org.codehaus.jackson.annotate.JsonProperty("component-name")
        private java.lang.String componentName;

        @org.codehaus.jackson.annotate.JsonProperty
        private java.lang.String host;

        @org.codehaus.jackson.annotate.JsonProperty("config-type")
        private java.lang.String configType;

        @org.codehaus.jackson.annotate.JsonProperty("config-name")
        private java.lang.String configName;

        public java.lang.String getType() {
            return type;
        }

        public void setType(java.lang.String type) {
            this.type = type;
        }

        public java.lang.String getLevel() {
            return level;
        }

        public void setLevel(java.lang.String level) {
            this.level = level;
        }

        public java.lang.String getMessage() {
            return message;
        }

        public void setMessage(java.lang.String message) {
            this.message = message;
        }

        public java.lang.String getComponentName() {
            return componentName;
        }

        public void setComponentName(java.lang.String componentName) {
            this.componentName = componentName;
        }

        public java.lang.String getHost() {
            return host;
        }

        public void setHost(java.lang.String host) {
            this.host = host;
        }

        public java.lang.String getConfigType() {
            return configType;
        }

        public void setConfigType(java.lang.String configType) {
            this.configType = configType;
        }

        public java.lang.String getConfigName() {
            return configName;
        }

        public void setConfigName(java.lang.String configName) {
            this.configName = configName;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return com.google.common.base.MoreObjects.toStringHelper(this).add("type", type).add("level", level).add("message", message).add("componentName", componentName).add("host", host).add("configType", configType).add("configName", configName).toString();
        }
    }
}