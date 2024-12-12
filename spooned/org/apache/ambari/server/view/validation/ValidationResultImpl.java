package org.apache.ambari.server.view.validation;
public class ValidationResultImpl implements org.apache.ambari.view.validation.ValidationResult {
    private final boolean valid;

    private final java.lang.String detail;

    public ValidationResultImpl(boolean valid, java.lang.String detail) {
        this.valid = valid;
        this.detail = detail;
    }

    @java.lang.Override
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public java.lang.String getDetail() {
        return detail;
    }

    public static org.apache.ambari.view.validation.ValidationResult create(org.apache.ambari.view.validation.ValidationResult result) {
        if (result == null) {
            result = org.apache.ambari.view.validation.ValidationResult.SUCCESS;
        }
        return new org.apache.ambari.server.view.validation.ValidationResultImpl(result.isValid(), result.getDetail());
    }
}