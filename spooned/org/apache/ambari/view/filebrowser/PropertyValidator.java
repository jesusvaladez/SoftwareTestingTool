package org.apache.ambari.view.filebrowser;
public class PropertyValidator implements org.apache.ambari.view.validation.Validator {
    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.filebrowser.PropertyValidator.class);

    public static final java.lang.String WEBHDFS_URL = "webhdfs.url";

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
        if (property.equals(org.apache.ambari.view.filebrowser.PropertyValidator.WEBHDFS_URL)) {
            java.lang.String webhdfsUrl = viewInstanceDefinition.getPropertyMap().get(org.apache.ambari.view.filebrowser.PropertyValidator.WEBHDFS_URL);
            if (!org.apache.ambari.view.utils.ambari.ValidatorUtils.validateHdfsURL(webhdfsUrl)) {
                org.apache.ambari.view.filebrowser.PropertyValidator.LOG.error("Invalid webhdfs.url = {}", webhdfsUrl);
                return new org.apache.ambari.view.filebrowser.PropertyValidator.InvalidPropertyValidationResult(false, "Must be valid URL");
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