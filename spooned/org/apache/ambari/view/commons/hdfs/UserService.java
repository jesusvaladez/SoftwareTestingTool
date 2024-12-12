package org.apache.ambari.view.commons.hdfs;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
public class UserService extends org.apache.ambari.view.commons.hdfs.HdfsService {
    public UserService(org.apache.ambari.view.ViewContext context) {
        super(context);
    }

    public UserService(org.apache.ambari.view.ViewContext context, java.util.Map<java.lang.String, java.lang.String> customProperties) {
        super(context, customProperties);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/home")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response homeDir() {
        try {
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            return javax.ws.rs.core.Response.ok(getApi().fileStatusToJSON(api.getFileStatus(api.getHomeDir().toString()))).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/trash/enabled")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response trashEnabled() {
        try {
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            return javax.ws.rs.core.Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(api.trashEnabled())).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/trashDir")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response trashdir() {
        try {
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            return javax.ws.rs.core.Response.ok(getApi().fileStatusToJSON(api.getFileStatus(api.getTrashDir().toString()))).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            throw ex;
        } catch (java.io.FileNotFoundException ex) {
            throw new org.apache.ambari.view.commons.exceptions.NotFoundFormattedException(ex.getMessage(), ex);
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }
}