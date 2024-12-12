package org.apache.ambari.server.controller.internal;
public class OperationStatusMetaData implements org.apache.ambari.server.controller.spi.RequestStatusMetaData , org.apache.ambari.server.api.services.ResultMetadata {
    private final java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.OperationStatusMetaData.Result> results = new java.util.HashMap<>();

    public void addResult(java.lang.String id, boolean success, java.lang.String message, java.lang.Object response) {
        results.put(id, new org.apache.ambari.server.controller.internal.OperationStatusMetaData.Result(id, success, message, response));
    }

    public java.util.Set<java.lang.String> getResultIds() {
        return results.keySet();
    }

    public org.apache.ambari.server.controller.internal.OperationStatusMetaData.Result getResult(java.lang.String id) {
        if (results.containsKey(id)) {
            return results.get(id);
        }
        throw new java.util.NoSuchElementException();
    }

    public java.util.List<org.apache.ambari.server.controller.internal.OperationStatusMetaData.Result> getResults() {
        return new java.util.ArrayList<>(results.values());
    }

    public class Result {
        private final java.lang.String id;

        private final boolean success;

        private final java.lang.String message;

        private final java.lang.Object response;

        Result(java.lang.String id, boolean success, java.lang.String message, java.lang.Object response) {
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