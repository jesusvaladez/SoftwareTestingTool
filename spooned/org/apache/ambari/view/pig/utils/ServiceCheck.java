package org.apache.ambari.view.pig.utils;
import org.apache.hadoop.conf.Configuration;
public class ServiceCheck {
    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.utils.ServiceCheck.class);

    private final org.apache.ambari.view.ViewContext viewContext;

    public ServiceCheck(org.apache.ambari.view.ViewContext viewContext) {
        this.viewContext = viewContext;
    }

    public static class Policy {
        private boolean checkHdfs = true;

        private boolean checkHomeDirectory = true;

        private boolean checkWebhcat = true;

        private boolean checkStorage = true;

        public Policy() {
        }

        public Policy(boolean checkHdfs, boolean checkHomeDirectory, boolean checkWebhcat, boolean checkStorage) {
            this.checkHdfs = checkHdfs;
            this.checkHomeDirectory = checkHomeDirectory;
            this.checkWebhcat = checkWebhcat;
            this.checkStorage = checkStorage;
        }

        public boolean isCheckHdfs() {
            return checkHdfs;
        }

        public void setCheckHdfs(boolean checkHdfs) {
            this.checkHdfs = checkHdfs;
        }

        public boolean isCheckHomeDirectory() {
            return checkHomeDirectory;
        }

        public void setCheckHomeDirectory(boolean checkHomeDirectory) {
            this.checkHomeDirectory = checkHomeDirectory;
        }

        public boolean isCheckWebhcat() {
            return checkWebhcat;
        }

        public void setCheckWebhcat(boolean checkWebhcat) {
            this.checkWebhcat = checkWebhcat;
        }

        public boolean isCheckStorage() {
            return checkStorage;
        }

        public void setCheckStorage(boolean checkStorage) {
            this.checkStorage = checkStorage;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((("Policy{" + "checkHdfs=") + checkHdfs) + ", checkHomeDirectory=") + checkHomeDirectory) + ", checkWebhcat=") + checkWebhcat) + ", checkStorage=") + checkStorage) + '}';
        }
    }

    public org.apache.ambari.view.pig.utils.ServiceCheck.Policy getServiceCheckPolicy() throws org.apache.ambari.view.utils.hdfs.HdfsApiException {
        org.apache.ambari.view.pig.utils.ServiceCheck.Policy policy = new org.apache.ambari.view.pig.utils.ServiceCheck.Policy();
        com.google.common.base.Optional<java.util.Map<java.lang.String, java.lang.String>> viewConfigs = org.apache.ambari.view.commons.hdfs.ViewPropertyHelper.getViewConfigs(viewContext, org.apache.ambari.view.pig.utils.Constants.VIEW_CONF_KEYVALUES);
        org.apache.ambari.view.utils.hdfs.ConfigurationBuilder configBuilder;
        if (viewConfigs.isPresent()) {
            configBuilder = new org.apache.ambari.view.utils.hdfs.ConfigurationBuilder(this.viewContext, viewConfigs.get());
        } else {
            configBuilder = new org.apache.ambari.view.utils.hdfs.ConfigurationBuilder(this.viewContext);
        }
        org.apache.hadoop.conf.Configuration configurations = configBuilder.buildConfig();
        java.lang.String defaultFS = configurations.get(org.apache.ambari.view.pig.utils.Constants.DEFAULT_FS);
        java.net.URI fsUri = null;
        try {
            fsUri = new java.net.URI(defaultFS);
            java.lang.String protocol = fsUri.getScheme();
            java.lang.String ambariSkipCheckValues = viewContext.getAmbariProperty(org.apache.ambari.view.pig.utils.Constants.AMBARI_SKIP_HOME_DIRECTORY_CHECK_PROTOCOL_LIST);
            java.util.List<java.lang.String> protocolSkipList = (ambariSkipCheckValues == null) ? new java.util.LinkedList<java.lang.String>() : java.util.Arrays.asList(ambariSkipCheckValues.split(","));
            if ((null != protocol) && protocolSkipList.contains(protocol)) {
                policy.setCheckHomeDirectory(false);
                return policy;
            }
        } catch (java.net.URISyntaxException e) {
            org.apache.ambari.view.pig.utils.ServiceCheck.LOG.error("Error occurred while parsing the defaultFS URI.", e);
            return policy;
        }
        return policy;
    }
}