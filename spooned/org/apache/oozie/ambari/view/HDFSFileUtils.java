package org.apache.oozie.ambari.view;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
public class HDFSFileUtils {
    public static final java.lang.String VIEW_CONF_KEYVALUES = "view.conf.keyvalues";

    private static final java.lang.String DEFAULT_FS = "fs.defaultFS";

    private static final java.lang.String AMBARI_SKIP_HOME_DIRECTORY_CHECK_PROTOCOL_LIST = "views.skip.home-directory-check.file-system.list";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.oozie.ambari.view.HDFSFileUtils.class);

    private org.apache.ambari.view.ViewContext viewContext;

    public HDFSFileUtils(org.apache.ambari.view.ViewContext viewContext) {
        super();
        this.viewContext = viewContext;
    }

    public boolean fileExists(java.lang.String path) {
        try {
            return getHdfsgetApi().exists(path);
        } catch (java.io.IOException e) {
            org.apache.oozie.ambari.view.HDFSFileUtils.LOGGER.error(e.getMessage(), e);
            throw new java.lang.RuntimeException(e);
        } catch (java.lang.InterruptedException e) {
            org.apache.oozie.ambari.view.HDFSFileUtils.LOGGER.error(e.getMessage(), e);
            throw new java.lang.RuntimeException(e);
        }
    }

    public org.apache.hadoop.fs.FSDataInputStream read(java.lang.String filePath) throws java.io.IOException {
        org.apache.hadoop.fs.FSDataInputStream is;
        try {
            is = getHdfsgetApi().open(filePath);
        } catch (java.lang.InterruptedException e) {
            throw new java.lang.RuntimeException(e);
        }
        return is;
    }

    public java.lang.String writeToFile(java.lang.String filePath, java.lang.String content, boolean overwrite) throws java.io.IOException {
        org.apache.hadoop.fs.FSDataOutputStream fsOut;
        try {
            fsOut = getHdfsgetApi().create(filePath, overwrite);
        } catch (java.lang.InterruptedException e) {
            throw new java.lang.RuntimeException(e);
        }
        fsOut.write(content.getBytes());
        fsOut.close();
        return filePath;
    }

    public void deleteFile(java.lang.String filePath) throws java.io.IOException {
        try {
            getHdfsgetApi().delete(filePath, false);
        } catch (java.lang.InterruptedException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private org.apache.ambari.view.utils.hdfs.HdfsApi getHdfsgetApi() {
        try {
            com.google.common.base.Optional<java.util.Map<java.lang.String, java.lang.String>> props = org.apache.ambari.view.commons.hdfs.ViewPropertyHelper.getViewConfigs(viewContext, org.apache.oozie.ambari.view.HDFSFileUtils.VIEW_CONF_KEYVALUES);
            org.apache.ambari.view.utils.hdfs.HdfsApi api;
            if (props.isPresent()) {
                api = org.apache.ambari.view.utils.hdfs.HdfsUtil.connectToHDFSApi(viewContext, props.get());
            } else {
                api = org.apache.ambari.view.utils.hdfs.HdfsUtil.connectToHDFSApi(viewContext);
            }
            return api;
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.HDFSFileUtils.LOGGER.error("Error in getting HDFS Api", ex);
            throw new java.lang.RuntimeException("HdfsApi connection failed. Check \"webhdfs.url\" property", ex);
        }
    }

    public java.lang.Boolean shouldCheckForHomeDir() {
        com.google.common.base.Optional<java.util.Map<java.lang.String, java.lang.String>> viewConfigs = org.apache.ambari.view.commons.hdfs.ViewPropertyHelper.getViewConfigs(viewContext, org.apache.oozie.ambari.view.HDFSFileUtils.VIEW_CONF_KEYVALUES);
        org.apache.ambari.view.utils.hdfs.ConfigurationBuilder configBuilder;
        if (viewConfigs.isPresent()) {
            configBuilder = new org.apache.ambari.view.utils.hdfs.ConfigurationBuilder(this.viewContext, viewConfigs.get());
        } else {
            configBuilder = new org.apache.ambari.view.utils.hdfs.ConfigurationBuilder(this.viewContext);
        }
        org.apache.hadoop.conf.Configuration configurations = null;
        try {
            configurations = configBuilder.buildConfig();
        } catch (org.apache.ambari.view.utils.hdfs.HdfsApiException e) {
            throw new java.lang.RuntimeException(e);
        }
        java.lang.String defaultFS = configurations.get(org.apache.oozie.ambari.view.HDFSFileUtils.DEFAULT_FS);
        try {
            java.net.URI fsUri = new java.net.URI(defaultFS);
            java.lang.String protocol = fsUri.getScheme();
            java.lang.String ambariSkipCheckValues = viewContext.getAmbariProperty(org.apache.oozie.ambari.view.HDFSFileUtils.AMBARI_SKIP_HOME_DIRECTORY_CHECK_PROTOCOL_LIST);
            java.util.List<java.lang.String> protocolSkipList = (ambariSkipCheckValues == null) ? new java.util.LinkedList<java.lang.String>() : java.util.Arrays.asList(ambariSkipCheckValues.split(","));
            if ((null != protocol) && protocolSkipList.contains(protocol)) {
                return java.lang.Boolean.FALSE;
            }
        } catch (java.net.URISyntaxException e) {
            org.apache.oozie.ambari.view.HDFSFileUtils.LOGGER.error("Error occurred while parsing the defaultFS URI.", e);
            return java.lang.Boolean.TRUE;
        }
        return java.lang.Boolean.TRUE;
    }

    public org.apache.hadoop.fs.FileStatus getFileStatus(java.lang.String filePath) {
        try {
            return getHdfsgetApi().getFileStatus(filePath);
        } catch (java.io.FileNotFoundException e) {
            throw new java.lang.RuntimeException(e);
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException(e);
        } catch (java.lang.InterruptedException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public boolean hdfsCheck() {
        try {
            getHdfsgetApi().getStatus();
            return true;
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public boolean homeDirCheck() {
        org.apache.ambari.view.commons.hdfs.UserService userservice = new org.apache.ambari.view.commons.hdfs.UserService(viewContext, getViewConfigs(viewContext));
        userservice.homeDir();
        return true;
    }

    private java.util.Map<java.lang.String, java.lang.String> getViewConfigs(org.apache.ambari.view.ViewContext context) {
        com.google.common.base.Optional<java.util.Map<java.lang.String, java.lang.String>> props = org.apache.ambari.view.commons.hdfs.ViewPropertyHelper.getViewConfigs(context, org.apache.oozie.ambari.view.HDFSFileUtils.VIEW_CONF_KEYVALUES);
        return props.isPresent() ? props.get() : new java.util.HashMap<java.lang.String, java.lang.String>();
    }
}