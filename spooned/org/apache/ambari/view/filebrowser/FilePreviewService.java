package org.apache.ambari.view.filebrowser;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.json.simple.JSONObject;
public class FilePreviewService extends org.apache.ambari.view.commons.hdfs.HdfsService {
    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.filebrowser.FilePreviewService.class);

    private org.apache.hadoop.io.compress.CompressionCodecFactory compressionCodecFactory;

    public FilePreviewService(org.apache.ambari.view.ViewContext context) {
        super(context);
        initCompressionCodecFactory();
    }

    private void initCompressionCodecFactory() {
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("io.compression.codecs", "org.apache.hadoop.io.compress.GzipCodec," + ("org.apache.hadoop.io.compress.DefaultCodec,org.apache.hadoop.io.compress.SnappyCodec," + "org.apache.hadoop.io.compress.BZip2Codec"));
        compressionCodecFactory = new org.apache.hadoop.io.compress.CompressionCodecFactory(conf);
    }

    public FilePreviewService(org.apache.ambari.view.ViewContext context, java.util.Map<java.lang.String, java.lang.String> viewConfigs) {
        super(context, viewConfigs);
        initCompressionCodecFactory();
    }

    @org.apache.ambari.view.filebrowser.GET
    @org.apache.ambari.view.filebrowser.Path("/file")
    @org.apache.ambari.view.filebrowser.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response previewFile(@org.apache.ambari.view.filebrowser.QueryParam("path")
    java.lang.String path, @org.apache.ambari.view.filebrowser.QueryParam("start")
    int start, @org.apache.ambari.view.filebrowser.QueryParam("end")
    int end) {
        org.apache.ambari.view.filebrowser.FilePreviewService.LOG.info("previewing file {}, from start {}, till end {}", path, start, end);
        try {
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            org.apache.hadoop.fs.FileStatus status = api.getFileStatus(path);
            org.apache.hadoop.io.compress.CompressionCodec codec = compressionCodecFactory.getCodec(status.getPath());
            java.io.InputStream stream = (codec != null) ? codec.createInputStream(api.open(path)) : api.open(path);
            int length = end - start;
            byte[] bytes = new byte[length];
            if (start != 0)
                org.apache.commons.io.IOUtils.skip(stream, start);

            int readBytes = org.apache.commons.io.IOUtils.read(stream, bytes);
            boolean isFileEnd = false;
            if (readBytes < length)
                isFileEnd = true;

            org.json.simple.JSONObject response = new org.json.simple.JSONObject();
            response.put("data", new java.lang.String(bytes));
            response.put("readbytes", readBytes);
            response.put("isFileEnd", isFileEnd);
            return javax.ws.rs.core.Response.ok(response).build();
        } catch (WebApplicationException ex) {
            org.apache.ambari.view.filebrowser.FilePreviewService.LOG.error("Error occurred while previewing {} : ", path, ex);
            throw ex;
        } catch (java.io.FileNotFoundException ex) {
            org.apache.ambari.view.filebrowser.FilePreviewService.LOG.error("Error occurred while previewing {} : ", path, ex);
            throw new org.apache.ambari.view.commons.exceptions.NotFoundFormattedException(ex.getMessage(), ex);
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.filebrowser.FilePreviewService.LOG.error("Error occurred while previewing {} : ", path, ex);
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }
}