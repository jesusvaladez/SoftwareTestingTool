package org.apache.ambari.server.view.validation;
public class InstanceValidationResultImpl extends org.apache.ambari.server.view.validation.ValidationResultImpl {
    private static final com.google.gson.Gson GSON = new com.google.gson.Gson();

    private final java.util.Map<java.lang.String, org.apache.ambari.view.validation.ValidationResult> propertyResults;

    public InstanceValidationResultImpl(org.apache.ambari.view.validation.ValidationResult instanceResult, java.util.Map<java.lang.String, org.apache.ambari.view.validation.ValidationResult> propertyResults) {
        super(org.apache.ambari.server.view.validation.InstanceValidationResultImpl.isValid(instanceResult, propertyResults), org.apache.ambari.server.view.validation.InstanceValidationResultImpl.getDetail(instanceResult, propertyResults));
        this.propertyResults = propertyResults;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.view.validation.ValidationResult> getPropertyResults() {
        return propertyResults;
    }

    public java.lang.String toJson() {
        return org.apache.ambari.server.view.validation.InstanceValidationResultImpl.GSON.toJson(this);
    }

    private static boolean isValid(org.apache.ambari.view.validation.ValidationResult instanceResult, java.util.Map<java.lang.String, org.apache.ambari.view.validation.ValidationResult> propertyResults) {
        boolean instanceValid = instanceResult.isValid();
        if (instanceValid) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.view.validation.ValidationResult> entry : propertyResults.entrySet()) {
                org.apache.ambari.view.validation.ValidationResult propertyResult = entry.getValue();
                if ((propertyResult != null) && (!propertyResult.isValid())) {
                    return false;
                }
            }
        }
        return instanceValid;
    }

    private static java.lang.String getDetail(org.apache.ambari.view.validation.ValidationResult instanceResult, java.util.Map<java.lang.String, org.apache.ambari.view.validation.ValidationResult> propertyResults) {
        if (instanceResult.isValid()) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.view.validation.ValidationResult> entry : propertyResults.entrySet()) {
                org.apache.ambari.view.validation.ValidationResult propertyResult = entry.getValue();
                if ((propertyResult != null) && (!propertyResult.isValid())) {
                    return "The instance has invalid properties.";
                }
            }
        }
        return instanceResult.getDetail();
    }
}