package org.apache.ambari.view.property;
public class MyValidator implements org.apache.ambari.view.validation.Validator {
    private static final java.lang.String PARAMETER_NAME_FREEFORM = "freeform";

    private static final java.lang.String PARAMETER_NAME_INTEGER = "integer";

    private static final java.lang.String PARAMETER_NAME_FIRST_VALUE = "first.value";

    private static final java.lang.String PARAMETER_NAME_SECOND_VALUE = "second.value";

    private static final java.lang.String PARAMETER_NAME_URL = "url";

    private static final int SECOND_VALUE_MAX = 100;

    @java.lang.Override
    public org.apache.ambari.view.validation.ValidationResult validateInstance(org.apache.ambari.view.ViewInstanceDefinition definition, org.apache.ambari.view.validation.Validator.ValidationContext mode) {
        java.util.Map<java.lang.String, java.lang.String> props = definition.getPropertyMap();
        return validateParameterFirstSecondValues(props);
    }

    @java.lang.Override
    public org.apache.ambari.view.validation.ValidationResult validateProperty(java.lang.String property, org.apache.ambari.view.ViewInstanceDefinition definition, org.apache.ambari.view.validation.Validator.ValidationContext mode) {
        if (property.equals(org.apache.ambari.view.property.MyValidator.PARAMETER_NAME_FREEFORM)) {
        } else if (property.equals(org.apache.ambari.view.property.MyValidator.PARAMETER_NAME_URL)) {
            return validateParameterURL(definition.getPropertyMap());
        } else if (property.equals(org.apache.ambari.view.property.MyValidator.PARAMETER_NAME_INTEGER)) {
            return validateParameterInteger(definition.getPropertyMap());
        } else if (property.equals(org.apache.ambari.view.property.MyValidator.PARAMETER_NAME_FIRST_VALUE)) {
            return validateParameterFirst(definition.getPropertyMap());
        } else if (property.equals(org.apache.ambari.view.property.MyValidator.PARAMETER_NAME_SECOND_VALUE)) {
            return validateParameterSecond(definition.getPropertyMap());
        }
        return org.apache.ambari.view.validation.ValidationResult.SUCCESS;
    }

    private org.apache.ambari.view.validation.ValidationResult validateParameterURL(java.util.Map<java.lang.String, java.lang.String> properties) {
        java.lang.String urlProp = properties.get(org.apache.ambari.view.property.MyValidator.PARAMETER_NAME_URL);
        java.net.URL u = null;
        try {
            u = new java.net.URL(urlProp);
        } catch (java.net.MalformedURLException e) {
            return new org.apache.ambari.view.property.MyValidator.MyValidationResult(false, "Must be valid URL");
        }
        try {
            u.toURI();
        } catch (java.net.URISyntaxException e) {
            return new org.apache.ambari.view.property.MyValidator.MyValidationResult(false, "Must be valid URL");
        }
        return org.apache.ambari.view.validation.ValidationResult.SUCCESS;
    }

    private org.apache.ambari.view.validation.ValidationResult validateParameterInteger(java.util.Map<java.lang.String, java.lang.String> properties) {
        java.lang.String val = properties.get(org.apache.ambari.view.property.MyValidator.PARAMETER_NAME_INTEGER);
        int intValue = -1;
        try {
            org.apache.ambari.view.property.MyValidator.checkInteger(val);
        } catch (java.lang.NumberFormatException nfe) {
            return new org.apache.ambari.view.property.MyValidator.MyValidationResult(false, "Must be an integer");
        }
        return org.apache.ambari.view.validation.ValidationResult.SUCCESS;
    }

    private org.apache.ambari.view.validation.ValidationResult validateParameterFirst(java.util.Map<java.lang.String, java.lang.String> properties) {
        java.lang.String val = properties.get(org.apache.ambari.view.property.MyValidator.PARAMETER_NAME_FIRST_VALUE);
        int intValue = -1;
        try {
            org.apache.ambari.view.property.MyValidator.checkInteger(val);
        } catch (java.lang.NumberFormatException nfe) {
            return new org.apache.ambari.view.property.MyValidator.MyValidationResult(false, "Must be an integer");
        }
        return org.apache.ambari.view.validation.ValidationResult.SUCCESS;
    }

    private org.apache.ambari.view.validation.ValidationResult validateParameterSecond(java.util.Map<java.lang.String, java.lang.String> properties) {
        java.lang.String val = properties.get(org.apache.ambari.view.property.MyValidator.PARAMETER_NAME_SECOND_VALUE);
        int intValue = -1;
        try {
            intValue = org.apache.ambari.view.property.MyValidator.checkInteger(val);
        } catch (java.lang.NumberFormatException nfe) {
            return new org.apache.ambari.view.property.MyValidator.MyValidationResult(false, "Must be an integer");
        }
        if (intValue > org.apache.ambari.view.property.MyValidator.SECOND_VALUE_MAX)
            return new org.apache.ambari.view.property.MyValidator.MyValidationResult(false, "Must be less than " + org.apache.ambari.view.property.MyValidator.SECOND_VALUE_MAX);

        return org.apache.ambari.view.validation.ValidationResult.SUCCESS;
    }

    private org.apache.ambari.view.validation.ValidationResult validateParameterFirstSecondValues(java.util.Map<java.lang.String, java.lang.String> properties) {
        java.lang.String firstValue = properties.get(org.apache.ambari.view.property.MyValidator.PARAMETER_NAME_FIRST_VALUE);
        int firstIntValue = -1;
        try {
            firstIntValue = org.apache.ambari.view.property.MyValidator.checkInteger(firstValue);
        } catch (java.lang.NumberFormatException nfe) {
            return new org.apache.ambari.view.property.MyValidator.MyValidationResult(false, "Must be an integer");
        }
        java.lang.String secondValue = properties.get(org.apache.ambari.view.property.MyValidator.PARAMETER_NAME_SECOND_VALUE);
        int secondIntValue = -1;
        try {
            secondIntValue = org.apache.ambari.view.property.MyValidator.checkInteger(secondValue);
        } catch (java.lang.NumberFormatException nfe) {
            return new org.apache.ambari.view.property.MyValidator.MyValidationResult(false, "Must be an integer");
        }
        if (secondIntValue < firstIntValue)
            return new org.apache.ambari.view.property.MyValidator.MyValidationResult(false, "Second value must be greater or equal to first");

        return org.apache.ambari.view.validation.ValidationResult.SUCCESS;
    }

    private static int checkInteger(java.lang.String value) {
        return java.lang.Integer.parseInt(value);
    }

    private static class MyValidationResult implements org.apache.ambari.view.validation.ValidationResult {
        private boolean valid;

        private java.lang.String details;

        public MyValidationResult(boolean valid, java.lang.String details) {
            this.valid = valid;
            this.details = details;
        }

        @java.lang.Override
        public boolean isValid() {
            return this.valid;
        }

        @java.lang.Override
        public java.lang.String getDetail() {
            return this.details;
        }
    }
}