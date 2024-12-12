package org.apache.ambari.view.commons.hdfs;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.JSONObject;
public class FileOperationService extends org.apache.ambari.view.commons.hdfs.HdfsService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.commons.hdfs.FileOperationService.class);

    private static final java.lang.String FILES_VIEW_MAX_FILE_PER_PAGE = "views.files.max.files.per.page";

    private static final int DEFAULT_FILES_VIEW_MAX_FILE_PER_PAGE = 5000;

    private java.lang.Integer maxFilesPerPage = org.apache.ambari.view.commons.hdfs.FileOperationService.DEFAULT_FILES_VIEW_MAX_FILE_PER_PAGE;

    public FileOperationService(org.apache.ambari.view.ViewContext context) {
        super(context);
        setMaxFilesPerPage(context);
    }

    private void setMaxFilesPerPage(org.apache.ambari.view.ViewContext context) {
        java.lang.String maxFilesPerPageProperty = context.getAmbariProperty(org.apache.ambari.view.commons.hdfs.FileOperationService.FILES_VIEW_MAX_FILE_PER_PAGE);
        org.apache.ambari.view.commons.hdfs.FileOperationService.LOG.info("maxFilesPerPageProperty = {}", maxFilesPerPageProperty);
        if (!com.google.common.base.Strings.isNullOrEmpty(maxFilesPerPageProperty)) {
            try {
                maxFilesPerPage = java.lang.Integer.parseInt(maxFilesPerPageProperty);
            } catch (java.lang.Exception e) {
                org.apache.ambari.view.commons.hdfs.FileOperationService.LOG.error("{} should be integer, but it is {}, using default value of {}", org.apache.ambari.view.commons.hdfs.FileOperationService.FILES_VIEW_MAX_FILE_PER_PAGE, maxFilesPerPageProperty, org.apache.ambari.view.commons.hdfs.FileOperationService.DEFAULT_FILES_VIEW_MAX_FILE_PER_PAGE);
            }
        }
    }

    public FileOperationService(org.apache.ambari.view.ViewContext context, java.util.Map<java.lang.String, java.lang.String> customProperties) {
        super(context, customProperties);
        this.setMaxFilesPerPage(context);
    }

    @org.apache.ambari.view.commons.hdfs.GET
    @org.apache.ambari.view.commons.hdfs.Path("/listdir")
    @org.apache.ambari.view.commons.hdfs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.commons.hdfs.Response listdir(@org.apache.ambari.view.commons.hdfs.QueryParam("path")
    java.lang.String path, @org.apache.ambari.view.commons.hdfs.QueryParam("nameFilter")
    java.lang.String nameFilter) {
        try {
            org.json.simple.JSONObject response = new org.json.simple.JSONObject();
            java.util.Map<java.lang.String, java.lang.Object> parentInfo = getApi().fileStatusToJSON(getApi().getFileStatus(path));
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus = getApi().listdir(path, nameFilter, maxFilesPerPage);
            org.apache.ambari.view.utils.hdfs.DirListInfo dirListInfo = dirStatus.getDirListInfo();
            parentInfo.put("originalSize", dirListInfo.getOriginalSize());
            parentInfo.put("truncated", dirListInfo.isTruncated());
            parentInfo.put("finalSize", dirListInfo.getFinalSize());
            parentInfo.put("nameFilter", dirListInfo.getNameFilter());
            response.put("files", getApi().fileStatusToJSON(dirStatus.getFileStatuses()));
            response.put("meta", parentInfo);
            return Response.ok(response).build();
        } catch (WebApplicationException ex) {
            throw ex;
        } catch (java.io.FileNotFoundException ex) {
            throw new org.apache.ambari.view.commons.exceptions.NotFoundFormattedException(ex.getMessage(), ex);
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @org.apache.ambari.view.commons.hdfs.POST
    @org.apache.ambari.view.commons.hdfs.Path("/rename")
    @org.apache.ambari.view.commons.hdfs.Consumes(MediaType.APPLICATION_JSON)
    @org.apache.ambari.view.commons.hdfs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.commons.hdfs.Response rename(final org.apache.ambari.view.commons.hdfs.FileOperationService.SrcDstFileRequest request) {
        try {
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            javax.ws.rs.core.Response.ResponseBuilder result;
            if (api.rename(request.src, request.dst)) {
                result = Response.ok(getApi().fileStatusToJSON(api.getFileStatus(request.dst)));
            } else {
                result = Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(false, ((("Can't move '" + request.src) + "' to '") + request.dst) + "'")).status(422);
            }
            return result.build();
        } catch (WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @org.apache.ambari.view.commons.hdfs.POST
    @org.apache.ambari.view.commons.hdfs.Path("/chmod")
    @org.apache.ambari.view.commons.hdfs.Consumes(MediaType.APPLICATION_JSON)
    @org.apache.ambari.view.commons.hdfs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.commons.hdfs.Response chmod(final org.apache.ambari.view.commons.hdfs.FileOperationService.ChmodRequest request) {
        try {
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            javax.ws.rs.core.Response.ResponseBuilder result;
            if (api.chmod(request.path, request.mode)) {
                result = Response.ok(getApi().fileStatusToJSON(api.getFileStatus(request.path)));
            } else {
                result = Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(false, ("Can't chmod '" + request.path) + "'")).status(422);
            }
            return result.build();
        } catch (WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @org.apache.ambari.view.commons.hdfs.POST
    @org.apache.ambari.view.commons.hdfs.Path("/move")
    @org.apache.ambari.view.commons.hdfs.Consumes(MediaType.APPLICATION_JSON)
    @org.apache.ambari.view.commons.hdfs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.commons.hdfs.Response move(final org.apache.ambari.view.commons.hdfs.FileOperationService.MultiSrcDstFileRequest request, @org.apache.ambari.view.commons.hdfs.Context
    HttpHeaders headers, @org.apache.ambari.view.commons.hdfs.Context
    UriInfo ui) {
        try {
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            javax.ws.rs.core.Response.ResponseBuilder result;
            java.lang.String message = "";
            java.util.List<java.lang.String> sources = request.sourcePaths;
            java.lang.String destination = request.destinationPath;
            if (sources.isEmpty()) {
                result = Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(false, ("Can't move 0 file/folder to '" + destination) + "'")).status(422);
                return result.build();
            }
            int index = 0;
            for (java.lang.String src : sources) {
                java.lang.String fileName = getFileName(src);
                java.lang.String finalDestination = getDestination(destination, fileName);
                try {
                    if (api.rename(src, finalDestination)) {
                        index++;
                    } else {
                        message = ((("Failed to move '" + src) + "' to '") + finalDestination) + "'";
                        break;
                    }
                } catch (java.io.IOException exception) {
                    message = exception.getMessage();
                    org.apache.ambari.view.commons.hdfs.HdfsService.logger.error("Failed to move '{}' to '{}'. Exception: {}", src, finalDestination, exception.getMessage());
                    break;
                }
            }
            if (index == sources.size()) {
                result = Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(true)).status(200);
            } else {
                org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult errorResult = getFailureFileOperationResult(sources, index, message);
                result = Response.ok(errorResult).status(422);
            }
            return result.build();
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @org.apache.ambari.view.commons.hdfs.POST
    @org.apache.ambari.view.commons.hdfs.Path("/copy")
    @org.apache.ambari.view.commons.hdfs.Consumes(MediaType.APPLICATION_JSON)
    @org.apache.ambari.view.commons.hdfs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.commons.hdfs.Response copy(final org.apache.ambari.view.commons.hdfs.FileOperationService.MultiSrcDstFileRequest request, @org.apache.ambari.view.commons.hdfs.Context
    HttpHeaders headers, @org.apache.ambari.view.commons.hdfs.Context
    UriInfo ui) {
        try {
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            javax.ws.rs.core.Response.ResponseBuilder result;
            java.lang.String message = "";
            java.util.List<java.lang.String> sources = request.sourcePaths;
            java.lang.String destination = request.destinationPath;
            if (sources.isEmpty()) {
                result = Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(false, ("Can't copy 0 file/folder to '" + destination) + "'")).status(422);
                return result.build();
            }
            int index = 0;
            for (java.lang.String src : sources) {
                java.lang.String fileName = getFileName(src);
                java.lang.String finalDestination = getDestination(destination, fileName);
                try {
                    api.copy(src, finalDestination);
                    index++;
                } catch (java.io.IOException | org.apache.ambari.view.utils.hdfs.HdfsApiException exception) {
                    message = exception.getMessage();
                    org.apache.ambari.view.commons.hdfs.HdfsService.logger.error("Failed to copy '{}' to '{}'. Exception: {}", src, finalDestination, exception.getMessage());
                    break;
                }
            }
            if (index == sources.size()) {
                result = Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(true)).status(200);
            } else {
                org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult errorResult = getFailureFileOperationResult(sources, index, message);
                result = Response.ok(errorResult).status(422);
            }
            return result.build();
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @org.apache.ambari.view.commons.hdfs.PUT
    @org.apache.ambari.view.commons.hdfs.Path("/mkdir")
    @org.apache.ambari.view.commons.hdfs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.commons.hdfs.Response mkdir(final org.apache.ambari.view.commons.hdfs.FileOperationService.MkdirRequest request) {
        try {
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            javax.ws.rs.core.Response.ResponseBuilder result;
            if (api.mkdir(request.path)) {
                result = Response.ok(getApi().fileStatusToJSON(api.getFileStatus(request.path)));
            } else {
                result = Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(false, ("Can't create dir '" + request.path) + "'")).status(422);
            }
            return result.build();
        } catch (WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @org.apache.ambari.view.commons.hdfs.DELETE
    @org.apache.ambari.view.commons.hdfs.Path("/trash/emptyTrash")
    @org.apache.ambari.view.commons.hdfs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.commons.hdfs.Response emptyTrash() {
        try {
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            api.emptyTrash();
            return Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(true)).build();
        } catch (WebApplicationException ex) {
            throw ex;
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @org.apache.ambari.view.commons.hdfs.POST
    @org.apache.ambari.view.commons.hdfs.Path("/moveToTrash")
    @org.apache.ambari.view.commons.hdfs.Consumes(MediaType.APPLICATION_JSON)
    @org.apache.ambari.view.commons.hdfs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.commons.hdfs.Response moveToTrash(org.apache.ambari.view.commons.hdfs.FileOperationService.MultiRemoveRequest request) {
        try {
            javax.ws.rs.core.Response.ResponseBuilder result;
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            java.lang.String trash = api.getTrashDirPath();
            java.lang.String message = "";
            if (request.paths.size() == 0) {
                result = Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(false, "No path entries provided.")).status(422);
            } else {
                if (!api.exists(trash)) {
                    if (!api.mkdir(trash)) {
                        result = Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(false, (("Trash dir does not exists. Can't create dir for " + "trash '") + trash) + "'")).status(422);
                        return result.build();
                    }
                }
                int index = 0;
                for (org.apache.ambari.view.commons.hdfs.FileOperationService.MultiRemoveRequest.PathEntry entry : request.paths) {
                    java.lang.String trashFilePath = api.getTrashDirPath(entry.path);
                    try {
                        if (api.rename(entry.path, trashFilePath)) {
                            index++;
                        } else {
                            message = ((("Failed to move '" + entry.path) + "' to '") + trashFilePath) + "'";
                            break;
                        }
                    } catch (java.io.IOException exception) {
                        message = exception.getMessage();
                        org.apache.ambari.view.commons.hdfs.HdfsService.logger.error("Failed to move '{}' to '{}'. Exception: {}", entry.path, trashFilePath, exception.getMessage());
                        break;
                    }
                }
                if (index == request.paths.size()) {
                    result = Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(true)).status(200);
                } else {
                    org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult errorResult = getFailureFileOperationResult(getPathsFromPathsEntries(request.paths), index, message);
                    result = Response.ok(errorResult).status(422);
                }
            }
            return result.build();
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    @org.apache.ambari.view.commons.hdfs.POST
    @org.apache.ambari.view.commons.hdfs.Path("/remove")
    @org.apache.ambari.view.commons.hdfs.Consumes(MediaType.APPLICATION_JSON)
    @org.apache.ambari.view.commons.hdfs.Produces(MediaType.APPLICATION_JSON)
    public org.apache.ambari.view.commons.hdfs.Response remove(org.apache.ambari.view.commons.hdfs.FileOperationService.MultiRemoveRequest request, @org.apache.ambari.view.commons.hdfs.Context
    HttpHeaders headers, @org.apache.ambari.view.commons.hdfs.Context
    UriInfo ui) {
        try {
            org.apache.ambari.view.utils.hdfs.HdfsApi api = getApi();
            javax.ws.rs.core.Response.ResponseBuilder result;
            java.lang.String message = "";
            if (request.paths.size() == 0) {
                result = Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(false, "No path entries provided."));
            } else {
                int index = 0;
                for (org.apache.ambari.view.commons.hdfs.FileOperationService.MultiRemoveRequest.PathEntry entry : request.paths) {
                    try {
                        if (api.delete(entry.path, entry.recursive)) {
                            index++;
                        } else {
                            message = ("Failed to remove '" + entry.path) + "'";
                            break;
                        }
                    } catch (java.io.IOException exception) {
                        message = exception.getMessage();
                        org.apache.ambari.view.commons.hdfs.HdfsService.logger.error("Failed to remove '{}'. Exception: {}", entry.path, exception.getMessage());
                        break;
                    }
                }
                if (index == request.paths.size()) {
                    result = Response.ok(new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(true)).status(200);
                } else {
                    org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult errorResult = getFailureFileOperationResult(getPathsFromPathsEntries(request.paths), index, message);
                    result = Response.ok(errorResult).status(422);
                }
            }
            return result.build();
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.view.commons.exceptions.ServiceFormattedException(ex.getMessage(), ex);
        }
    }

    private java.util.List<java.lang.String> getPathsFromPathsEntries(java.util.List<org.apache.ambari.view.commons.hdfs.FileOperationService.MultiRemoveRequest.PathEntry> paths) {
        java.util.List<java.lang.String> entries = new java.util.ArrayList<>();
        for (org.apache.ambari.view.commons.hdfs.FileOperationService.MultiRemoveRequest.PathEntry path : paths) {
            entries.add(path.path);
        }
        return entries;
    }

    private org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult getFailureFileOperationResult(java.util.List<java.lang.String> paths, int failedIndex, java.lang.String message) {
        java.util.List<java.lang.String> succeeded = new java.util.ArrayList<>();
        java.util.List<java.lang.String> unprocessed = new java.util.ArrayList<>();
        java.util.List<java.lang.String> failed = new java.util.ArrayList<>();
        java.util.ListIterator<java.lang.String> iter = paths.listIterator();
        while (iter.hasNext()) {
            int index = iter.nextIndex();
            java.lang.String path = iter.next();
            if (index < failedIndex) {
                succeeded.add(path);
            } else if (index == failedIndex) {
                failed.add(path);
            } else {
                unprocessed.add(path);
            }
        } 
        return new org.apache.ambari.view.commons.hdfs.HdfsService.FileOperationResult(false, message, succeeded, failed, unprocessed);
    }

    private java.lang.String getDestination(java.lang.String baseDestination, java.lang.String fileName) {
        if (baseDestination.endsWith("/")) {
            return baseDestination + fileName;
        } else {
            return (baseDestination + "/") + fileName;
        }
    }

    private java.lang.String getFileName(java.lang.String srcPath) {
        return srcPath.substring(srcPath.lastIndexOf('/') + 1);
    }

    @javax.xml.bind.annotation.XmlRootElement
    public static class MkdirRequest {
        @javax.xml.bind.annotation.XmlElement(nillable = false, required = true)
        public java.lang.String path;
    }

    @javax.xml.bind.annotation.XmlRootElement
    public static class ChmodRequest {
        @javax.xml.bind.annotation.XmlElement(nillable = false, required = true)
        public java.lang.String path;

        @javax.xml.bind.annotation.XmlElement(nillable = false, required = true)
        public java.lang.String mode;
    }

    @javax.xml.bind.annotation.XmlRootElement
    public static class SrcDstFileRequest {
        @javax.xml.bind.annotation.XmlElement(nillable = false, required = true)
        public java.lang.String src;

        @javax.xml.bind.annotation.XmlElement(nillable = false, required = true)
        public java.lang.String dst;
    }

    @javax.xml.bind.annotation.XmlRootElement
    public static class MultiSrcDstFileRequest {
        @javax.xml.bind.annotation.XmlElement(nillable = false, required = true)
        public java.util.List<java.lang.String> sourcePaths = new java.util.ArrayList<>();

        @javax.xml.bind.annotation.XmlElement(nillable = false, required = true)
        public java.lang.String destinationPath;
    }

    @javax.xml.bind.annotation.XmlRootElement
    public static class MultiRemoveRequest {
        @javax.xml.bind.annotation.XmlElement(nillable = false, required = true)
        public java.util.List<org.apache.ambari.view.commons.hdfs.FileOperationService.MultiRemoveRequest.PathEntry> paths = new java.util.ArrayList<>();

        public static class PathEntry {
            @javax.xml.bind.annotation.XmlElement(nillable = false, required = true)
            public java.lang.String path;

            public boolean recursive;
        }
    }
}