package org.apache.ambari.view.pig;
public class PropertyValidator implements org.apache.ambari.view.validation.Validator {
    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.PropertyValidator.class);

    public static final java.lang.String WEBHDFS_URL = "webhdfs.url";

    public static final java.lang.String WEBHCAT_PORT = "webhcat.port";

    @java.lang.Override
    public org.apache.ambari.view.validation.ValidationResult validateInstance(org.apache.ambari.view.ViewInstanceDefinition viewInstanceDefinition, org.apache.ambari.view.validation.Validator.ValidationContext validationContext) {
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.view.validation.ValidationResult validateProperty(java.lang.String property, org.apache.ambari.view.ViewInstanceDefinition viewInstanceDefinition, org.apache.ambari.view.validation.Validator.ValidationContext validationContext) {
        org.apache.ambari.view.ClusterType clusterType = viewInstanceDefinition.getClusterType();
        if ((clusterType == org.apache.ambari.view.ClusterType.LOCAL_AMBARI) || (clusterType == org.apache.ambari.view.ClusterType.REMOTE_AMBARI)) {
            return org.apache.ambari.view.validation.ValidationResult.SUCCESS;
        }
        if (property.equals(org.apache.ambari.view.pig.PropertyValidator.WEBHDFS_URL)) {
            java.lang.String webhdfsUrl = viewInstanceDefinition.getPropertyMap().get(org.apache.ambari.view.pig.PropertyValidator.WEBHDFS_URL);
            if (!org.apache.ambari.view.utils.ambari.ValidatorUtils.validateHdfsURL(webhdfsUrl)) {
                org.apache.ambari.view.pig.PropertyValidator.LOG.error("Illegal webhdfsUrl : {}", webhdfsUrl);
                return new org.apache.ambari.view.pig.PropertyValidator.InvalidPropertyValidationResult(false, "Must be valid URL");
            }
        }
        if (property.equals(org.apache.ambari.view.pig.PropertyValidator.WEBHCAT_PORT)) {
            java.lang.String webhcatPort = viewInstanceDefinition.getPropertyMap().get(org.apache.ambari.view.pig.PropertyValidator.WEBHCAT_PORT);
            if (webhcatPort != null) {
                try {
                    int port = java.lang.Integer.valueOf(webhcatPort);
                    if ((port < 1) || (port > 65535)) {
                        org.apache.ambari.view.pig.PropertyValidator.LOG.error("Illegal port : {} ", port);
                        return new org.apache.ambari.view.pig.PropertyValidator.InvalidPropertyValidationResult(false, "Must be from 1 to 65535");
                    }
                } catch (java.lang.NumberFormatException e) {
                    org.apache.ambari.view.pig.PropertyValidator.LOG.error("Port not numeric. webhcatPort = {}", webhcatPort);
                    return new org.apache.ambari.view.pig.PropertyValidator.InvalidPropertyValidationResult(false, "Must be integer");
                }
            }
        }
        return org.apache.ambari.view.validation.ValidationResult.SUCCESS;
    }

    public static class InvalidPropertyValidationResult implements org.apache.ambari.view.validation.ValidationResult {
        private boolean valid;

        private java.lang.String detail;

        public InvalidPropertyValidationResult(boolean valid, java.lang.String detail) {
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
    }
}