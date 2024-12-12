package org.apache.ambari.view.validation;
public interface Validator {
    public org.apache.ambari.view.validation.ValidationResult validateInstance(org.apache.ambari.view.ViewInstanceDefinition definition, org.apache.ambari.view.validation.Validator.ValidationContext mode);

    public org.apache.ambari.view.validation.ValidationResult validateProperty(java.lang.String property, org.apache.ambari.view.ViewInstanceDefinition definition, org.apache.ambari.view.validation.Validator.ValidationContext mode);

    public enum ValidationContext {

        PRE_CREATE,
        PRE_UPDATE,
        EXISTING;}
}