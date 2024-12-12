package org.apache.oozie.ambari.view;
import javax.ws.rs.Path;
public class FileServices {
    public static final java.lang.String VIEW_CONF_KEYVALUES = "view.conf.keyvalues";

    private org.apache.ambari.view.ViewContext context;

    public FileServices(org.apache.ambari.view.ViewContext viewContext) {
        this.context = viewContext;
    }

    @javax.ws.rs.Path("/upload")
    public org.apache.ambari.view.commons.hdfs.UploadService upload() {
        return new org.apache.ambari.view.commons.hdfs.UploadService(context, getViewConfigs());
    }

    @javax.ws.rs.Path("/fileops")
    public org.apache.ambari.view.commons.hdfs.FileOperationService fileOps() {
        return new org.apache.ambari.view.commons.hdfs.FileOperationService(context, getViewConfigs());
    }

    @javax.ws.rs.Path("/user")
    public org.apache.ambari.view.commons.hdfs.UserService userService() {
        return new org.apache.ambari.view.commons.hdfs.UserService(context, getViewConfigs());
    }

    private java.util.Map<java.lang.String, java.lang.String> getViewConfigs() {
        com.google.common.base.Optional<java.util.Map<java.lang.String, java.lang.String>> props = org.apache.ambari.view.commons.hdfs.ViewPropertyHelper.getViewConfigs(context, org.apache.oozie.ambari.view.FileServices.VIEW_CONF_KEYVALUES);
        return props.isPresent() ? props.get() : new java.util.HashMap<java.lang.String, java.lang.String>();
    }
}