package org.apache.ambari.view.commons.hdfs;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.hadoop.fs.FSDataOutputStream;
public class UploadService extends org.apache.ambari.view.commons.hdfs.HdfsService {
    public UploadService(org.apache.ambari.view.ViewContext context) {
        super(context);
    }

    public UploadService(org.apache.ambari.view.ViewContext context, java.util.Map<java.lang.String, java.lang.String> customProperties) {
        super(context, customProperties);
    }

    private void uploadFile(final java.lang.String filePath, java.io.InputStream uploadedInputStream) throws java.io.IOException, java.lang.InterruptedException {
        int read;
        byte[] chunk = new byte[1024];
        org.apache.hadoop.fs.FSDataOutputStream out = null;
        try {
            out = getApi().create(filePath, false);
            while ((read = uploadedInputStream.read(chunk)) != (-1)) {
                out.write(chunk, 0, read);
            } 
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA)
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response uploadFile(@com.sun.jersey.multipart.FormDataParam("file")
    java.io.InputStream uploadedInputStream, @com.sun.jersey.multipart.FormDataParam("file")
    com.sun.jersey.core.header.FormDataContentDisposition contentDisposition, @com.sun.jersey.multipart.FormDataParam("path")
    java.lang.String path) {
        try {
            if (!path.endsWith("/"))
                path = path + "/";

            java.lang.String filePath = path + new java.lang.String(contentDisposition.getFileName().getBytes("ISO8859-1"), "UTF-8");
            uploadFile(filePath, uploadedInputStream);
            return javax.ws.rs.core.Response.ok(getApi().fileStatusToJSON(getApi().getFileStatus(filePath))).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("/zip")
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA)
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response uploadZip(@com.sun.jersey.multipart.FormDataParam("file")
    java.io.InputStream uploadedInputStream, @com.sun.jersey.multipart.FormDataParam("file")
    com.sun.jersey.core.header.FormDataContentDisposition contentDisposition, @com.sun.jersey.multipart.FormDataParam("path")
    java.lang.String path) {
        try {
            if (!path.endsWith("/"))
                path = path + "/";

            java.util.zip.ZipInputStream zip = new java.util.zip.ZipInputStream(uploadedInputStream);
            java.util.zip.ZipEntry ze = zip.getNextEntry();
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            while (ze != null) {
                java.lang.String filePath = path + ze.getName();
                if (ze.isDirectory()) {
                    api.mkdir(filePath);
                } else {
                    uploadFile(filePath, zip);
                }
                ze = zip.getNextEntry();
            } 
            return javax.ws.rs.core.Response.ok(getApi().fileStatusToJSON(api.listdir(path))).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }
}