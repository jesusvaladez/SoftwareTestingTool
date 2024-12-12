package org.apache.ambari.view.pig.resources.files;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileAlreadyExistsException;
import org.apache.hadoop.fs.FileStatus;
import org.json.simple.JSONObject;
public class FileService extends org.apache.ambari.view.pig.services.BaseService {
    public static final java.lang.String VIEW_CONF_KEYVALUES = "view.conf.keyvalues";

    @com.google.inject.Inject
    org.apache.ambari.view.ViewResourceHandler handler;

    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.resources.files.FileService.class);

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{filePath:.*}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response getFile(@javax.ws.rs.PathParam("filePath")
    java.lang.String filePath, @javax.ws.rs.QueryParam("page")
    java.lang.Long page, @javax.ws.rs.QueryParam("action")
    java.lang.String action) throws java.io.IOException, java.lang.InterruptedException {
        try {
            filePath = sanitizeFilePath(filePath);
            if ((action != null) && action.equals("ls")) {
                org.apache.ambari.view.pig.resources.files.FileService.LOG.debug("List directory {}", filePath);
                java.util.List<java.lang.String> ls = new java.util.LinkedList<java.lang.String>();
                for (org.apache.hadoop.fs.FileStatus fs : getHdfsApi().listdir(filePath)) {
                    ls.add(fs.getPath().toString());
                }
                org.json.simple.JSONObject object = new org.json.simple.JSONObject();
                object.put("ls", ls);
                return javax.ws.rs.core.Response.ok(object).status(200).build();
            }
            org.apache.ambari.view.pig.resources.files.FileService.LOG.debug("Reading file {}", filePath);
            org.apache.ambari.view.pig.utils.FilePaginator paginator = new org.apache.ambari.view.pig.utils.FilePaginator(filePath, context);
            if (page == null)
                page = 0L;

            org.apache.ambari.view.pig.resources.files.FileResource file = new org.apache.ambari.view.pig.resources.files.FileResource();
            file.setFilePath(filePath);
            file.setFileContent(paginator.readPage(page));
            file.setHasNext(paginator.pageCount() > (page + 1));
            file.setPage(page);
            file.setPageCount(paginator.pageCount());
            org.json.simple.JSONObject object = new org.json.simple.JSONObject();
            object.put("file", file);
            return javax.ws.rs.core.Response.ok(object).status(200).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.files.FileService.LOG.error("Error occurred : ", ex);
            throw ex;
        } catch (java.io.FileNotFoundException ex) {
            org.apache.ambari.view.pig.resources.files.FileService.LOG.error("Error occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException(ex.getMessage(), ex);
        } catch (java.lang.IllegalArgumentException ex) {
            org.apache.ambari.view.pig.resources.files.FileService.LOG.error("Error occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.BadRequestFormattedException(ex.getMessage(), ex);
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.files.FileService.LOG.error("Error occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("{filePath:.*}")
    public javax.ws.rs.core.Response deleteFile(@javax.ws.rs.PathParam("filePath")
    java.lang.String filePath) throws java.io.IOException, java.lang.InterruptedException {
        try {
            filePath = sanitizeFilePath(filePath);
            org.apache.ambari.view.pig.resources.files.FileService.LOG.info("Deleting file {}", filePath);
            if (getHdfsApi().delete(filePath, false)) {
                return javax.ws.rs.core.Response.status(204).build();
            }
            throw new org.apache.ambari.view.pig.utils.NotFoundFormattedException("FileSystem.delete returned false", null);
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.files.FileService.LOG.error("Error occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.files.FileService.LOG.error("Error occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("{filePath:.*}")
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response updateFile(org.apache.ambari.view.pig.resources.files.FileService.FileResourceRequest request, @javax.ws.rs.PathParam("filePath")
    java.lang.String filePath) throws java.io.IOException, java.lang.InterruptedException {
        try {
            filePath = sanitizeFilePath(filePath);
            org.apache.ambari.view.pig.resources.files.FileService.LOG.info("Rewriting file {}", filePath);
            org.apache.hadoop.fs.FSDataOutputStream output = getHdfsApi().create(filePath, true);
            output.write(request.file.getFileContent().getBytes("UTF-8"));
            output.close();
            return javax.ws.rs.core.Response.status(204).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.files.FileService.LOG.error("Error occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.files.FileService.LOG.error("Error occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response createFile(org.apache.ambari.view.pig.resources.files.FileService.FileResourceRequest request, @javax.ws.rs.core.Context
    javax.servlet.http.HttpServletResponse response, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) throws java.io.IOException, java.lang.InterruptedException {
        try {
            org.apache.ambari.view.pig.resources.files.FileService.LOG.info("Creating file {}", request.file.getFilePath());
            try {
                org.apache.hadoop.fs.FSDataOutputStream output = getHdfsApi().create(request.file.getFilePath(), false);
                if (request.file.getFileContent() != null) {
                    output.write(request.file.getFileContent().getBytes("UTF-8"));
                }
                output.close();
            } catch (org.apache.hadoop.fs.FileAlreadyExistsException ex) {
                throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex, 400);
            }
            response.setHeader("Location", java.lang.String.format("%s/%s", ui.getAbsolutePath().toString(), request.file.getFilePath()));
            return javax.ws.rs.core.Response.status(204).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.files.FileService.LOG.error("Error occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.files.FileService.LOG.error("Error occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    public static void hdfsSmokeTest(org.apache.ambari.view.ViewContext context) {
        try {
            com.google.common.base.Optional<java.util.Map<java.lang.String, java.lang.String>> props = org.apache.ambari.view.commons.hdfs.ViewPropertyHelper.getViewConfigs(context, org.apache.ambari.view.pig.resources.files.FileService.VIEW_CONF_KEYVALUES);
            org.apache.ambari.view.utils.hdfs.HdfsApi api;
            if (props.isPresent()) {
                api = org.apache.ambari.view.utils.hdfs.HdfsUtil.connectToHDFSApi(context, props.get());
            } else {
                api = org.apache.ambari.view.utils.hdfs.HdfsUtil.connectToHDFSApi(context);
            }
            api.getStatus();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.pig.resources.files.FileService.LOG.error("Error occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.pig.resources.files.FileService.LOG.error("Error occurred : ", ex);
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    public static void userhomeSmokeTest(org.apache.ambari.view.ViewContext context) {
        try {
            org.apache.ambari.view.commons.hdfs.UserService userservice = new org.apache.ambari.view.commons.hdfs.UserService(context, org.apache.ambari.view.pig.resources.files.FileService.getViewConfigs(context));
            userservice.homeDir();
        } catch (javax.ws.rs.WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.pig.utils.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    public static class FileResourceRequest {
        public org.apache.ambari.view.pig.resources.files.FileResource file;
    }

    private java.lang.String sanitizeFilePath(java.lang.String filePath) {
        if ((!filePath.startsWith("/")) && (!filePath.startsWith("."))) {
            filePath = "/" + filePath;
        }
        return filePath;
    }

    private static java.util.Map<java.lang.String, java.lang.String> getViewConfigs(org.apache.ambari.view.ViewContext context) {
        com.google.common.base.Optional<java.util.Map<java.lang.String, java.lang.String>> props = org.apache.ambari.view.commons.hdfs.ViewPropertyHelper.getViewConfigs(context, org.apache.ambari.view.pig.resources.files.FileService.VIEW_CONF_KEYVALUES);
        return props.isPresent() ? props.get() : new java.util.HashMap<java.lang.String, java.lang.String>();
    }
}