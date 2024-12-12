package org.apache.ambari.view.filebrowser;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.security.AccessControlException;
import org.json.simple.JSONObject;
public class DownloadService extends org.apache.ambari.view.commons.hdfs.HdfsService {
    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.filebrowser.DownloadService.class);

    public DownloadService(org.apache.ambari.view.ViewContext context) {
        super(context);
    }

    public DownloadService(org.apache.ambari.view.ViewContext context, java.util.Map<java.lang.String, java.lang.String> customProperties) {
        super(context, customProperties);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/browse")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public javax.ws.rs.core.Response browse(@javax.ws.rs.QueryParam("path")
    java.lang.String path, @javax.ws.rs.QueryParam("download")
    boolean download, @javax.ws.rs.QueryParam("checkperm")
    boolean checkperm, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        org.apache.ambari.view.filebrowser.DownloadService.LOG.debug("browsing path : {} with download : {}", path, download);
        try {
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            org.apache.hadoop.fs.FileStatus status = api.getFileStatus(path);
            org.apache.hadoop.fs.FSDataInputStream fs = api.open(path);
            if (checkperm) {
                org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
                jsonObject.put("allowed", true);
                return javax.ws.rs.core.Response.ok(jsonObject).header("Content-Type", MediaType.APPLICATION_JSON).build();
            }
            javax.ws.rs.core.Response.ResponseBuilder result = javax.ws.rs.core.Response.ok(fs);
            if (download) {
                result.header("Content-Disposition", ("attachment; filename=\"" + java.net.URLEncoder.encode(status.getPath().getName(), "UTF-8")) + "\"").type(MediaType.APPLICATION_OCTET_STREAM);
            } else {
                java.net.FileNameMap fileNameMap = java.net.URLConnection.getFileNameMap();
                java.lang.String mimeType = fileNameMap.getContentTypeFor(status.getPath().getName());
                result.header("Content-Disposition", ("filename=\"" + java.net.URLEncoder.encode(status.getPath().getName(), "UTF-8")) + "\"").type(mimeType);
            }
            return result.build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Exception while browsing : {}", path, ex);
            throw ex;
        } catch (java.io.FileNotFoundException ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("File not found while browsing : {}", path, ex);
            throw new org.apache.ambari.view.commons.exceptions.NotFoundFormattedException(ex.getMessage(), ex);
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Exception while browsing : {}", path, ex);
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    private void zipFile(java.util.zip.ZipOutputStream zip, java.lang.String path) {
        try {
            org.apache.hadoop.fs.FSDataInputStream in = getApi().open(path);
            zip.putNextEntry(new java.util.zip.ZipEntry(path.substring(1)));
            byte[] chunk = new byte[1024];
            int readLen = 0;
            while (readLen != (-1)) {
                zip.write(chunk, 0, readLen);
                readLen = in.read(chunk);
            } 
        } catch (java.io.IOException ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error zipping file {}  (file ignored): ", path, ex);
        } catch (java.lang.InterruptedException ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error zipping file {} (file ignored): ", path, ex);
        } finally {
            try {
                zip.closeEntry();
            } catch (java.io.IOException ex) {
                org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error closing entry {} (file ignored): ", path, ex);
            }
        }
    }

    private void zipDirectory(java.util.zip.ZipOutputStream zip, java.lang.String path) {
        try {
            zip.putNextEntry(new java.util.zip.ZipEntry(path.substring(1) + "/"));
        } catch (java.io.IOException ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error zipping directory {} (directory ignored).", path, ex);
        } finally {
            try {
                zip.closeEntry();
            } catch (java.io.IOException ex) {
                org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error zipping directory {} (directory ignored).", path, ex);
            }
        }
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/zip")
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM)
    public javax.ws.rs.core.Response downloadGZip(final org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest request) {
        org.apache.ambari.view.filebrowser.DownloadService.LOG.debug("downloadGZip requested for : {} ", request.entries);
        try {
            java.lang.String name = "hdfs.zip";
            if (request.entries.length == 1) {
                name = new java.io.File(request.entries[0]).getName() + ".zip";
            }
            javax.ws.rs.core.StreamingOutput result = new javax.ws.rs.core.StreamingOutput() {
                public void write(java.io.OutputStream output) throws java.io.IOException, org.apache.ambari.view.commons.exceptions.ServiceFormattedException {
                    java.util.zip.ZipOutputStream zip = new java.util.zip.ZipOutputStream(output);
                    try {
                        org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
                        java.util.Queue<java.lang.String> files = new java.util.LinkedList<java.lang.String>();
                        for (java.lang.String file : request.entries) {
                            files.add(file);
                        }
                        while (!files.isEmpty()) {
                            java.lang.String path = files.poll();
                            org.apache.hadoop.fs.FileStatus status = api.getFileStatus(path);
                            if (status.isDirectory()) {
                                org.apache.hadoop.fs.FileStatus[] subdir;
                                try {
                                    subdir = api.listdir(path);
                                } catch (org.apache.hadoop.security.AccessControlException ex) {
                                    org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error zipping directory {}/ (directory ignored) : ", path.substring(1), ex);
                                    continue;
                                }
                                for (org.apache.hadoop.fs.FileStatus file : subdir) {
                                    files.add(org.apache.hadoop.fs.Path.getPathWithoutSchemeAndAuthority(file.getPath()).toString());
                                }
                                zipDirectory(zip, path);
                            } else {
                                zipFile(zip, path);
                            }
                        } 
                    } catch (java.lang.Exception ex) {
                        org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error occurred: ", ex);
                        throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
                    } finally {
                        zip.close();
                    }
                }
            };
            return javax.ws.rs.core.Response.ok(result).header("Content-Disposition", ("inline; filename=\"" + java.net.URLEncoder.encode(name, "UTF-8")) + "\"").build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error occurred : ", ex);
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/concat")
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM)
    public javax.ws.rs.core.Response concat(final org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest request) {
        org.apache.ambari.view.filebrowser.DownloadService.LOG.info("Starting concat files.");
        try {
            javax.ws.rs.core.StreamingOutput result = new javax.ws.rs.core.StreamingOutput() {
                public void write(java.io.OutputStream output) throws java.io.IOException, org.apache.ambari.view.commons.exceptions.ServiceFormattedException {
                    org.apache.hadoop.fs.FSDataInputStream in = null;
                    for (java.lang.String path : request.entries) {
                        try {
                            try {
                                in = getApi().open(path);
                            } catch (org.apache.hadoop.security.AccessControlException ex) {
                                org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error in opening file {}. Ignoring concat of this files.", path.substring(1), ex);
                                continue;
                            }
                            long bytesCopied = org.apache.commons.io.IOUtils.copyLarge(in, output);
                            org.apache.ambari.view.filebrowser.DownloadService.LOG.info("concated file : {}, total bytes added = {}", path, bytesCopied);
                        } catch (java.lang.Exception ex) {
                            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error occurred : ", ex);
                            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
                        } finally {
                            if (in != null)
                                in.close();

                        }
                    }
                }
            };
            javax.ws.rs.core.Response.ResponseBuilder response = javax.ws.rs.core.Response.ok(result);
            if (request.download) {
                response.header("Content-Disposition", "attachment; filename=\"concatResult.txt\"").type(MediaType.APPLICATION_OCTET_STREAM);
            } else {
                response.header("Content-Disposition", "filename=\"concatResult.txt\"").type(MediaType.TEXT_PLAIN);
            }
            return response.build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error occurred ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error occurred ", ex);
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/zip")
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces("application/zip")
    public javax.ws.rs.core.Response zipByRequestId(@javax.ws.rs.QueryParam("requestId")
    java.lang.String requestId) {
        org.apache.ambari.view.filebrowser.DownloadService.LOG.info("Starting zip download requestId : {}", requestId);
        try {
            org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest request = getDownloadRequest(requestId);
            return downloadGZip(request);
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error occurred : ", ex);
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/zip/generate-link")
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response zipGenerateLink(final org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest request) {
        org.apache.ambari.view.filebrowser.DownloadService.LOG.info("starting generate-link");
        return generateLink(request);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/concat")
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM)
    public javax.ws.rs.core.Response concatByRequestId(@javax.ws.rs.QueryParam("requestId")
    java.lang.String requestId) {
        org.apache.ambari.view.filebrowser.DownloadService.LOG.info("Starting concat for requestId : {}", requestId);
        try {
            org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest request = getDownloadRequest(requestId);
            return concat(request);
        } catch (javax.ws.rs.WebApplicationException ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error occurred : ", ex);
            throw ex;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Error occurred : ", ex);
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/concat/generate-link")
    @javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response concatGenerateLink(final org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest request) {
        org.apache.ambari.view.filebrowser.DownloadService.LOG.info("Starting link generation for concat");
        return generateLink(request);
    }

    private javax.ws.rs.core.Response generateLink(org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest request) {
        try {
            java.lang.String requestId = generateUniqueIdentifer(request);
            org.apache.ambari.view.filebrowser.DownloadService.LOG.info("returning generated requestId : {}", requestId);
            org.json.simple.JSONObject json = new org.json.simple.JSONObject();
            json.put("requestId", requestId);
            return javax.ws.rs.core.Response.ok(json).build();
        } catch (javax.ws.rs.WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    private org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest getDownloadRequest(java.lang.String requestId) throws org.apache.ambari.view.utils.hdfs.HdfsApiException, java.io.IOException, java.lang.InterruptedException {
        java.lang.String fileName = getFileNameForRequestData(requestId);
        java.lang.String json = org.apache.ambari.view.utils.hdfs.HdfsUtil.readFile(getApi(), fileName);
        org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest request = gson.fromJson(json, org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest.class);
        deleteFileFromHdfs(fileName);
        return request;
    }

    private com.google.gson.Gson gson = new com.google.gson.Gson();

    private java.lang.String generateUniqueIdentifer(org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest request) {
        java.lang.String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        java.lang.String json = gson.toJson(request);
        writeToHdfs(uuid, json);
        return uuid;
    }

    private void writeToHdfs(java.lang.String uuid, java.lang.String json) {
        java.lang.String fileName = getFileNameForRequestData(uuid);
        try {
            org.apache.ambari.view.utils.hdfs.HdfsUtil.putStringToFile(getApi(), fileName, json);
        } catch (org.apache.ambari.view.utils.hdfs.HdfsApiException e) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("Failed to write request data to HDFS", e);
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException("Failed to write request data to HDFS", e);
        }
    }

    private java.lang.String getFileNameForRequestData(java.lang.String uuid) {
        java.lang.String tmpPath = context.getProperties().get("tmp.dir");
        if (tmpPath == null) {
            org.apache.ambari.view.filebrowser.DownloadService.LOG.error("tmp.dir is not configured!");
            throw new org.apache.ambari.view.commons.exceptions.MisconfigurationFormattedException("tmp.dir");
        }
        return java.lang.String.format(tmpPath + "/%s.json", uuid);
    }

    private void deleteFileFromHdfs(java.lang.String fileName) throws java.io.IOException, java.lang.InterruptedException {
        getApi().delete(fileName, true);
    }

    public static class DownloadRequest {
        @javax.xml.bind.annotation.XmlElement(nillable = false, required = true)
        public java.lang.String[] entries;

        @javax.xml.bind.annotation.XmlElement(required = false)
        public boolean download;
    }
}