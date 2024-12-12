package org.apache.ambari.view.commons.hdfs;
import javax.ws.rs.WebApplicationException;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.hadoop.security.UserGroupInformation;
public abstract class HdfsService {
    protected static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.commons.hdfs.HdfsService.class);

    protected final org.apache.ambari.view.ViewContext context;

    private java.util.Map<java.lang.String, java.lang.String> customProperties;

    public HdfsService(org.apache.ambari.view.ViewContext context) {
        this.context = context;
    }

    public HdfsService(org.apache.ambari.view.ViewContext context, java.util.Map<java.lang.String, java.lang.String> customProperties) {
        this.context = context;
        this.customProperties = customProperties;
    }

    @javax.xml.bind.annotation.XmlRootElement
    public static class FileOperationResult {
        public boolean success;

        public java.lang.String message;

        public java.util.List<java.lang.String> succeeded;

        public java.util.List<java.lang.String> failed;

        public java.util.List<java.lang.String> unprocessed;

        public FileOperationResult(boolean success) {
            this.success = success;
        }

        public FileOperationResult(boolean success, java.lang.String message) {
            this(success);
            this.message = message;
        }

        public FileOperationResult(boolean success, java.lang.String message, java.util.List<java.lang.String> succeeded, java.util.List<java.lang.String> failed, java.util.List<java.lang.String> unprocessed) {
            this(success, message);
            this.succeeded = succeeded;
            this.failed = failed;
            this.unprocessed = unprocessed;
        }
    }

    private org.apache.ambari.view.utils.hdfs.HdfsApi _api = null;

    public org.apache.ambari.view.utils.hdfs.HdfsApi getApi() {
        if (_api == null) {
            try {
                if (this.customProperties != null) {
                    _api = org.apache.ambari.view.utils.hdfs.HdfsUtil.connectToHDFSApi(context, customProperties);
                } else {
                    _api = org.apache.ambari.view.utils.hdfs.HdfsUtil.connectToHDFSApi(context);
                }
            } catch (java.lang.Exception ex) {
                org.apache.ambari.view.commons.hdfs.HdfsService.logger.error("Exception while connecting to hdfs : {}", ex.getMessage(), ex);
                throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException("HdfsApi connection failed. Check \"webhdfs.url\" property", ex);
            }
        }
        return _api;
    }

    private static java.util.Map<java.lang.String, java.lang.String> getHdfsAuthParams(org.apache.ambari.view.ViewContext context) {
        java.lang.String auth = context.getProperties().get("webhdfs.auth");
        java.util.Map<java.lang.String, java.lang.String> params = new java.util.HashMap<java.lang.String, java.lang.String>();
        if ((auth == null) || auth.isEmpty()) {
            auth = "auth=SIMPLE";
        }
        for (java.lang.String param : auth.split(";")) {
            java.lang.String[] keyvalue = param.split("=");
            if (keyvalue.length != 2) {
                org.apache.ambari.view.commons.hdfs.HdfsService.logger.error((("Can not parse authentication param " + param) + " in ") + auth);
                continue;
            }
            params.put(keyvalue[0], keyvalue[1]);
        }
        return params;
    }

    public java.lang.String getDoAsUsername(org.apache.ambari.view.ViewContext context) {
        java.lang.String username = context.getProperties().get("webhdfs.username");
        if ((username == null) || username.isEmpty())
            username = context.getUsername();

        return username;
    }

    public static void hdfsSmokeTest(org.apache.ambari.view.ViewContext context) {
        try {
            org.apache.ambari.view.utils.hdfs.HdfsApi api = org.apache.ambari.view.utils.hdfs.HdfsUtil.connectToHDFSApi(context);
            api.getStatus();
        } catch (javax.ws.rs.WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    public java.lang.String getRealUsername(org.apache.ambari.view.ViewContext context) {
        java.lang.String username = context.getProperties().get("webhdfs.proxyuser");
        if ((username == null) || username.isEmpty())
            try {
                username = org.apache.hadoop.security.UserGroupInformation.getCurrentUser().getShortUserName();
            } catch (java.io.IOException e) {
                throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException("HdfsApi connection failed. Can't get current user", e);
            }

        return username;
    }
}