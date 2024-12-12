package org.apache.ambari.view.utils.hdfs;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
public class HdfsUtil {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.utils.hdfs.HdfsUtil.class);

    public static void putStringToFile(final org.apache.ambari.view.utils.hdfs.HdfsApi hdfs, final java.lang.String filePath, final java.lang.String content) throws org.apache.ambari.view.utils.hdfs.HdfsApiException {
        try {
            synchronized(hdfs) {
                hdfs.execute(new java.security.PrivilegedExceptionAction<java.lang.Void>() {
                    @java.lang.Override
                    public java.lang.Void run() throws java.lang.Exception {
                        final org.apache.hadoop.fs.FSDataOutputStream stream = hdfs.create(filePath, true);
                        stream.write(content.getBytes());
                        stream.close();
                        return null;
                    }
                }, true);
            }
        } catch (java.io.IOException e) {
            throw new org.apache.ambari.view.utils.hdfs.HdfsApiException("HDFS020 Could not write file " + filePath, e);
        } catch (java.lang.InterruptedException e) {
            throw new org.apache.ambari.view.utils.hdfs.HdfsApiException("HDFS021 Could not write file " + filePath, e);
        }
    }

    public static java.lang.String readFile(org.apache.ambari.view.utils.hdfs.HdfsApi hdfs, java.lang.String filePath) throws org.apache.ambari.view.utils.hdfs.HdfsApiException {
        org.apache.hadoop.fs.FSDataInputStream stream;
        try {
            stream = hdfs.open(filePath);
            return org.apache.commons.io.IOUtils.toString(stream);
        } catch (java.io.IOException e) {
            throw new org.apache.ambari.view.utils.hdfs.HdfsApiException("HDFS060 Could not read file " + filePath, e);
        } catch (java.lang.InterruptedException e) {
            throw new org.apache.ambari.view.utils.hdfs.HdfsApiException("HDFS061 Could not read file " + filePath, e);
        }
    }

    public static java.lang.String findUnallocatedFileName(org.apache.ambari.view.utils.hdfs.HdfsApi hdfs, java.lang.String fullPathAndFilename, java.lang.String extension) throws org.apache.ambari.view.utils.hdfs.HdfsApiException {
        int triesCount = 0;
        java.lang.String newFilePath;
        boolean isUnallocatedFilenameFound;
        try {
            do {
                newFilePath = java.lang.String.format((fullPathAndFilename + "%s") + extension, triesCount == 0 ? "" : "_" + triesCount);
                org.apache.ambari.view.utils.hdfs.HdfsUtil.LOG.debug("Trying to find free filename " + newFilePath);
                isUnallocatedFilenameFound = !hdfs.exists(newFilePath);
                if (isUnallocatedFilenameFound) {
                    org.apache.ambari.view.utils.hdfs.HdfsUtil.LOG.debug("File created successfully!");
                }
                triesCount += 1;
                if (triesCount > 1000) {
                    throw new org.apache.ambari.view.utils.hdfs.HdfsApiException(("HDFS100 Can't find unallocated file name " + fullPathAndFilename) + "...");
                }
            } while (!isUnallocatedFilenameFound );
        } catch (java.io.IOException e) {
            throw new org.apache.ambari.view.utils.hdfs.HdfsApiException(("HDFS030 Error in creation " + fullPathAndFilename) + "...", e);
        } catch (java.lang.InterruptedException e) {
            throw new org.apache.ambari.view.utils.hdfs.HdfsApiException(("HDFS031 Error in creation " + fullPathAndFilename) + "...", e);
        }
        return newFilePath;
    }

    public static synchronized org.apache.ambari.view.utils.hdfs.HdfsApi connectToHDFSApi(org.apache.ambari.view.ViewContext context, java.util.Map<java.lang.String, java.lang.String> customViewProperties) throws org.apache.ambari.view.utils.hdfs.HdfsApiException {
        java.lang.ClassLoader currentClassLoader = java.lang.Thread.currentThread().getContextClassLoader();
        java.lang.Thread.currentThread().setContextClassLoader(org.apache.ambari.view.utils.hdfs.HdfsUtil.class.getClassLoader());
        try {
            org.apache.ambari.view.utils.hdfs.ConfigurationBuilder configurationBuilder = new org.apache.ambari.view.utils.hdfs.ConfigurationBuilder(context, customViewProperties);
            return org.apache.ambari.view.utils.hdfs.HdfsUtil.getHdfsApi(context, configurationBuilder);
        } finally {
            java.lang.Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
    }

    public static synchronized org.apache.ambari.view.utils.hdfs.HdfsApi connectToHDFSApi(org.apache.ambari.view.ViewContext context) throws org.apache.ambari.view.utils.hdfs.HdfsApiException {
        java.lang.ClassLoader currentClassLoader = java.lang.Thread.currentThread().getContextClassLoader();
        java.lang.Thread.currentThread().setContextClassLoader(org.apache.ambari.view.utils.hdfs.HdfsUtil.class.getClassLoader());
        try {
            org.apache.ambari.view.utils.hdfs.ConfigurationBuilder configurationBuilder = new org.apache.ambari.view.utils.hdfs.ConfigurationBuilder(context);
            return org.apache.ambari.view.utils.hdfs.HdfsUtil.getHdfsApi(context, configurationBuilder);
        } finally {
            java.lang.Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
    }

    private static org.apache.ambari.view.utils.hdfs.HdfsApi getHdfsApi(org.apache.ambari.view.ViewContext context, org.apache.ambari.view.utils.hdfs.ConfigurationBuilder configurationBuilder) throws org.apache.ambari.view.utils.hdfs.HdfsApiException {
        org.apache.ambari.view.utils.hdfs.HdfsApi api = null;
        org.apache.ambari.view.utils.hdfs.AuthConfigurationBuilder authConfigurationBuilder = new org.apache.ambari.view.utils.hdfs.AuthConfigurationBuilder(context);
        java.util.Map<java.lang.String, java.lang.String> authParams = authConfigurationBuilder.build();
        configurationBuilder.setAuthParams(authParams);
        try {
            api = new org.apache.ambari.view.utils.hdfs.HdfsApi(configurationBuilder, org.apache.ambari.view.utils.hdfs.HdfsUtil.getHdfsUsername(context));
            org.apache.ambari.view.utils.hdfs.HdfsUtil.LOG.info("HdfsApi connected OK");
        } catch (java.io.IOException e) {
            org.apache.ambari.view.utils.hdfs.HdfsUtil.LOG.error("exception occurred while creating hdfsApi objcet : {}", e.getMessage(), e);
            java.lang.String message = "HDFS040 Couldn't open connection to HDFS";
            org.apache.ambari.view.utils.hdfs.HdfsUtil.LOG.error(message);
            throw new org.apache.ambari.view.utils.hdfs.HdfsApiException(message, e);
        } catch (java.lang.InterruptedException e) {
            org.apache.ambari.view.utils.hdfs.HdfsUtil.LOG.error("exception occurred while creating hdfsApi objcet : {}", e.getMessage(), e);
            java.lang.String message = "HDFS041 Couldn't open connection to HDFS";
            org.apache.ambari.view.utils.hdfs.HdfsUtil.LOG.error(message);
            throw new org.apache.ambari.view.utils.hdfs.HdfsApiException(message, e);
        }
        return api;
    }

    public static java.lang.String getHdfsUsername(org.apache.ambari.view.ViewContext context) {
        java.lang.String userName = context.getProperties().get("webhdfs.username");
        if (((userName == null) || (userName.compareTo("null") == 0)) || (userName.compareTo("") == 0)) {
            userName = context.getUsername();
        }
        return userName;
    }
}