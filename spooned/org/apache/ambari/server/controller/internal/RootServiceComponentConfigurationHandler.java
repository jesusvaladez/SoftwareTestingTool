package org.apache.ambari.server.controller.internal;
abstract class RootServiceComponentConfigurationHandler {
    public abstract java.util.Map<java.lang.String, org.apache.ambari.server.api.services.RootServiceComponentConfiguration> getComponentConfigurations(java.lang.String categoryName);

    public abstract void removeComponentConfiguration(java.lang.String categoryName);

    public abstract void updateComponentCategory(java.lang.String categoryName, java.util.Map<java.lang.String, java.lang.String> properties, boolean removePropertiesIfNotSpecified) throws org.apache.ambari.server.AmbariException;

    public abstract org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandler.OperationResult performOperation(java.lang.String categoryName, java.util.Map<java.lang.String, java.lang.String> properties, boolean mergeExistingProperties, java.lang.String operation, java.util.Map<java.lang.String, java.lang.Object> operationParameters) throws org.apache.ambari.server.controller.spi.SystemException;

    class OperationResult {
        private final java.lang.String id;

        private final boolean success;

        private final java.lang.String message;

        private final java.lang.Object response;

        OperationResult(java.lang.String id, boolean success, java.lang.String message, java.lang.Object response) {
            this.id = id;
            this.success = success;
            this.message = message;
            this.response = response;
        }

        public java.lang.String getId() {
            return id;
        }

        public boolean isSuccess() {
            return success;
        }

        public java.lang.String getMessage() {
            return message;
        }

        public java.lang.Object getResponse() {
            return response;
        }
    }
}