package org.apache.ambari.view.filebrowser;
import javax.ws.rs.Path;
public class FileBrowserService {
    public static final java.lang.String VIEW_CONF_KEYVALUES = "view.conf.keyvalues";

    @com.google.inject.Inject
    org.apache.ambari.view.ViewContext context;

    @javax.ws.rs.Path("/download")
    public org.apache.ambari.view.filebrowser.DownloadService download() {
        return new org.apache.ambari.view.filebrowser.DownloadService(context, getViewConfigs());
    }

    private java.util.Map<java.lang.String, java.lang.String> getViewConfigs() {
        com.google.common.base.Optional<java.util.Map<java.lang.String, java.lang.String>> props = org.apache.ambari.view.commons.hdfs.ViewPropertyHelper.getViewConfigs(context, org.apache.ambari.view.filebrowser.FileBrowserService.VIEW_CONF_KEYVALUES);
        return props.isPresent() ? props.get() : new java.util.HashMap<java.lang.String, java.lang.String>();
    }

    @javax.ws.rs.Path("/upload")
    public org.apache.ambari.view.commons.hdfs.UploadService upload() {
        return new org.apache.ambari.view.commons.hdfs.UploadService(context, getViewConfigs());
    }

    @javax.ws.rs.Path("/fileops")
    public org.apache.ambari.view.commons.hdfs.FileOperationService fileOps() {
        return new org.apache.ambari.view.commons.hdfs.FileOperationService(context, getViewConfigs());
    }

    @javax.ws.rs.Path("/help")
    public org.apache.ambari.view.filebrowser.HelpService help() {
        return new org.apache.ambari.view.filebrowser.HelpService(context, getViewConfigs());
    }

    @javax.ws.rs.Path("/user")
    public org.apache.ambari.view.commons.hdfs.UserService userService() {
        return new org.apache.ambari.view.commons.hdfs.UserService(context, getViewConfigs());
    }

    @javax.ws.rs.Path("/preview")
    public org.apache.ambari.view.filebrowser.FilePreviewService preview() {
        return new org.apache.ambari.view.filebrowser.FilePreviewService(context, getViewConfigs());
    }
}