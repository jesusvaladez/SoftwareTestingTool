package org.apache.ambari.view.validation;
public interface ValidationResult {
    public boolean isValid();

    public java.lang.String getDetail();

    public static final org.apache.ambari.view.validation.ValidationResult SUCCESS = new org.apache.ambari.view.validation.ValidationResult() {
        @java.lang.Override
        public boolean isValid() {
            return true;
        }

        @java.lang.Override
        public java.lang.String getDetail() {
            return "OK";
        }
    };
}