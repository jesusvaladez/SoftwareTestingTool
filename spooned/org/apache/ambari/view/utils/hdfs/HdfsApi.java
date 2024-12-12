package org.apache.ambari.view.utils.hdfs;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.Trash;
import org.apache.hadoop.fs.TrashPolicy;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.protocol.HdfsFileStatus;
import org.apache.hadoop.security.UserGroupInformation;
import org.json.simple.JSONArray;
public class HdfsApi {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.utils.hdfs.HdfsApi.class);

    public static java.lang.String KeyIsErasureCoded = "isErasureCoded";

    public static java.lang.String KeyIsEncrypted = "isEncrypted";

    public static java.lang.String KeyErasureCodingPolicyName = "erasureCodingPolicyName";

    private final org.apache.hadoop.conf.Configuration conf;

    private java.util.Map<java.lang.String, java.lang.String> authParams;

    private org.apache.hadoop.fs.FileSystem fs;

    private org.apache.hadoop.security.UserGroupInformation ugi;

    public HdfsApi(org.apache.ambari.view.utils.hdfs.ConfigurationBuilder configurationBuilder, java.lang.String username) throws java.io.IOException, java.lang.InterruptedException, org.apache.ambari.view.utils.hdfs.HdfsApiException {
        this.authParams = configurationBuilder.buildAuthenticationConfig();
        conf = configurationBuilder.buildConfig();
        org.apache.hadoop.security.UserGroupInformation.setConfiguration(conf);
        ugi = org.apache.hadoop.security.UserGroupInformation.createProxyUser(username, getProxyUser());
        fs = execute(new java.security.PrivilegedExceptionAction<org.apache.hadoop.fs.FileSystem>() {
            public org.apache.hadoop.fs.FileSystem run() throws java.io.IOException {
                return org.apache.hadoop.fs.FileSystem.get(conf);
            }
        });
    }

    HdfsApi(org.apache.hadoop.conf.Configuration configuration, org.apache.hadoop.fs.FileSystem fs, org.apache.hadoop.security.UserGroupInformation ugi) throws java.io.IOException, java.lang.InterruptedException, org.apache.ambari.view.utils.hdfs.HdfsApiException {
        if (null != configuration) {
            conf = configuration;
        } else {
            conf = new org.apache.hadoop.conf.Configuration();
        }
        org.apache.hadoop.security.UserGroupInformation.setConfiguration(conf);
        if (null != ugi) {
            this.ugi = ugi;
        } else {
            this.ugi = org.apache.hadoop.security.UserGroupInformation.getCurrentUser();
        }
        if (null != fs) {
            this.fs = fs;
        } else {
            this.fs = execute(new java.security.PrivilegedExceptionAction<org.apache.hadoop.fs.FileSystem>() {
                public org.apache.hadoop.fs.FileSystem run() throws java.io.IOException {
                    return org.apache.hadoop.fs.FileSystem.get(conf);
                }
            });
        }
    }

    private org.apache.hadoop.security.UserGroupInformation getProxyUser() throws java.io.IOException {
        org.apache.hadoop.security.UserGroupInformation proxyuser;
        if (authParams.containsKey("proxyuser")) {
            proxyuser = org.apache.hadoop.security.UserGroupInformation.createRemoteUser(authParams.get("proxyuser"));
        } else {
            proxyuser = org.apache.hadoop.security.UserGroupInformation.getCurrentUser();
        }
        proxyuser.setAuthenticationMethod(getAuthenticationMethod());
        return proxyuser;
    }

    private UserGroupInformation.AuthenticationMethod getAuthenticationMethod() {
        org.apache.hadoop.security.UserGroupInformation.AuthenticationMethod authMethod;
        if (authParams.containsKey("auth")) {
            java.lang.String authName = authParams.get("auth");
            authMethod = UserGroupInformation.AuthenticationMethod.valueOf(authName.toUpperCase());
        } else {
            authMethod = UserGroupInformation.AuthenticationMethod.SIMPLE;
        }
        return authMethod;
    }

    public org.apache.hadoop.fs.FileStatus[] listdir(final java.lang.String path) throws java.io.FileNotFoundException, java.io.IOException, java.lang.InterruptedException {
        return execute(new java.security.PrivilegedExceptionAction<org.apache.hadoop.fs.FileStatus[]>() {
            public org.apache.hadoop.fs.FileStatus[] run() throws java.io.FileNotFoundException, java.lang.Exception {
                return fs.listStatus(new org.apache.hadoop.fs.Path(path));
            }
        });
    }

    public org.apache.ambari.view.utils.hdfs.DirStatus listdir(final java.lang.String path, final java.lang.String nameFilter, int maxAllowedSize) throws java.io.FileNotFoundException, java.io.IOException, java.lang.InterruptedException {
        org.apache.hadoop.fs.FileStatus[] fileStatuses = this.listdir(path);
        return filterAndTruncateDirStatus(nameFilter, maxAllowedSize, fileStatuses);
    }

    public org.apache.ambari.view.utils.hdfs.DirStatus filterAndTruncateDirStatus(java.lang.String nameFilter, int maxAllowedSize, org.apache.hadoop.fs.FileStatus[] fileStatuses) {
        if (null == fileStatuses) {
            return new org.apache.ambari.view.utils.hdfs.DirStatus(null, new org.apache.ambari.view.utils.hdfs.DirListInfo(0, false, 0, nameFilter));
        }
        int originalSize = fileStatuses.length;
        boolean truncated = false;
        if (!com.google.common.base.Strings.isNullOrEmpty(nameFilter)) {
            java.util.List<org.apache.hadoop.fs.FileStatus> filteredList = new java.util.LinkedList<>();
            for (org.apache.hadoop.fs.FileStatus fileStatus : fileStatuses) {
                if ((maxAllowedSize >= 0) && (maxAllowedSize <= filteredList.size())) {
                    truncated = true;
                    break;
                }
                if (fileStatus.getPath().getName().contains(nameFilter)) {
                    filteredList.add(fileStatus);
                }
            }
            fileStatuses = filteredList.toArray(new org.apache.hadoop.fs.FileStatus[0]);
        }
        if ((maxAllowedSize >= 0) && (fileStatuses.length > maxAllowedSize)) {
            truncated = true;
            fileStatuses = java.util.Arrays.copyOf(fileStatuses, maxAllowedSize);
        }
        int finalSize = fileStatuses.length;
        return new org.apache.ambari.view.utils.hdfs.DirStatus(fileStatuses, new org.apache.ambari.view.utils.hdfs.DirListInfo(originalSize, truncated, finalSize, nameFilter));
    }

    public org.apache.hadoop.fs.FileStatus getFileStatus(final java.lang.String path) throws java.io.IOException, java.io.FileNotFoundException, java.lang.InterruptedException {
        return execute(new java.security.PrivilegedExceptionAction<org.apache.hadoop.fs.FileStatus>() {
            public org.apache.hadoop.fs.FileStatus run() throws java.io.FileNotFoundException, java.io.IOException {
                return fs.getFileStatus(new org.apache.hadoop.fs.Path(path));
            }
        });
    }

    public boolean mkdir(final java.lang.String path) throws java.io.IOException, java.lang.InterruptedException {
        return execute(new java.security.PrivilegedExceptionAction<java.lang.Boolean>() {
            public java.lang.Boolean run() throws java.lang.Exception {
                return fs.mkdirs(new org.apache.hadoop.fs.Path(path));
            }
        });
    }

    public boolean rename(final java.lang.String src, final java.lang.String dst) throws java.io.IOException, java.lang.InterruptedException {
        return execute(new java.security.PrivilegedExceptionAction<java.lang.Boolean>() {
            public java.lang.Boolean run() throws java.lang.Exception {
                return fs.rename(new org.apache.hadoop.fs.Path(src), new org.apache.hadoop.fs.Path(dst));
            }
        });
    }

    public boolean trashEnabled() throws java.lang.Exception {
        return execute(new java.security.PrivilegedExceptionAction<java.lang.Boolean>() {
            public java.lang.Boolean run() throws java.io.IOException {
                org.apache.hadoop.fs.Trash tr = new org.apache.hadoop.fs.Trash(fs, conf);
                return tr.isEnabled();
            }
        });
    }

    public org.apache.hadoop.fs.Path getHomeDir() throws java.lang.Exception {
        return execute(new java.security.PrivilegedExceptionAction<org.apache.hadoop.fs.Path>() {
            public org.apache.hadoop.fs.Path run() throws java.io.IOException {
                return fs.getHomeDirectory();
            }
        });
    }

    public synchronized org.apache.hadoop.fs.FsStatus getStatus() throws java.lang.Exception {
        return execute(new java.security.PrivilegedExceptionAction<org.apache.hadoop.fs.FsStatus>() {
            public org.apache.hadoop.fs.FsStatus run() throws java.io.IOException {
                return fs.getStatus();
            }
        });
    }

    public org.apache.hadoop.fs.Path getTrashDir() throws java.lang.Exception {
        return execute(new java.security.PrivilegedExceptionAction<org.apache.hadoop.fs.Path>() {
            public org.apache.hadoop.fs.Path run() throws java.io.IOException {
                org.apache.hadoop.fs.TrashPolicy trashPolicy = org.apache.hadoop.fs.TrashPolicy.getInstance(conf, fs, fs.getHomeDirectory());
                return trashPolicy.getCurrentTrashDir().getParent();
            }
        });
    }

    public java.lang.String getTrashDirPath() throws java.lang.Exception {
        org.apache.hadoop.fs.Path trashDir = getTrashDir();
        return trashDir.toUri().getRawPath();
    }

    public java.lang.String getTrashDirPath(java.lang.String filePath) throws java.lang.Exception {
        java.lang.String trashDirPath = getTrashDirPath();
        org.apache.hadoop.fs.Path path = new org.apache.hadoop.fs.Path(filePath);
        trashDirPath = (trashDirPath + "/") + path.getName();
        return trashDirPath;
    }

    public java.lang.Void emptyTrash() throws java.lang.Exception {
        return execute(new java.security.PrivilegedExceptionAction<java.lang.Void>() {
            public java.lang.Void run() throws java.io.IOException {
                org.apache.hadoop.fs.Trash tr = new org.apache.hadoop.fs.Trash(fs, conf);
                tr.expunge();
                return null;
            }
        });
    }

    public boolean moveToTrash(final java.lang.String path) throws java.io.IOException, java.lang.InterruptedException {
        return execute(new java.security.PrivilegedExceptionAction<java.lang.Boolean>() {
            public java.lang.Boolean run() throws java.lang.Exception {
                return org.apache.hadoop.fs.Trash.moveToAppropriateTrash(fs, new org.apache.hadoop.fs.Path(path), conf);
            }
        });
    }

    public boolean delete(final java.lang.String path, final boolean recursive) throws java.io.IOException, java.lang.InterruptedException {
        return execute(new java.security.PrivilegedExceptionAction<java.lang.Boolean>() {
            public java.lang.Boolean run() throws java.lang.Exception {
                return fs.delete(new org.apache.hadoop.fs.Path(path), recursive);
            }
        });
    }

    public org.apache.hadoop.fs.FSDataOutputStream create(final java.lang.String path, final boolean overwrite) throws java.io.IOException, java.lang.InterruptedException {
        return execute(new java.security.PrivilegedExceptionAction<org.apache.hadoop.fs.FSDataOutputStream>() {
            public org.apache.hadoop.fs.FSDataOutputStream run() throws java.lang.Exception {
                return fs.create(new org.apache.hadoop.fs.Path(path), overwrite);
            }
        });
    }

    public org.apache.hadoop.fs.FSDataInputStream open(final java.lang.String path) throws java.io.IOException, java.lang.InterruptedException {
        return execute(new java.security.PrivilegedExceptionAction<org.apache.hadoop.fs.FSDataInputStream>() {
            public org.apache.hadoop.fs.FSDataInputStream run() throws java.lang.Exception {
                return fs.open(new org.apache.hadoop.fs.Path(path));
            }
        });
    }

    public boolean chmod(final java.lang.String path, final java.lang.String permissions) throws java.io.IOException, java.lang.InterruptedException {
        return execute(new java.security.PrivilegedExceptionAction<java.lang.Boolean>() {
            public java.lang.Boolean run() throws java.lang.Exception {
                try {
                    fs.setPermission(new org.apache.hadoop.fs.Path(path), org.apache.hadoop.fs.permission.FsPermission.valueOf(permissions));
                } catch (java.lang.Exception ex) {
                    return false;
                }
                return true;
            }
        });
    }

    public void copy(final java.lang.String src, final java.lang.String dest) throws java.io.IOException, java.lang.InterruptedException, org.apache.ambari.view.utils.hdfs.HdfsApiException {
        boolean result = execute(new java.security.PrivilegedExceptionAction<java.lang.Boolean>() {
            public java.lang.Boolean run() throws java.lang.Exception {
                return org.apache.hadoop.fs.FileUtil.copy(fs, new org.apache.hadoop.fs.Path(src), fs, new org.apache.hadoop.fs.Path(dest), false, false, conf);
            }
        });
        if (!result) {
            throw new org.apache.ambari.view.utils.hdfs.HdfsApiException("HDFS010 Can\'t copy source file from \" + src + \" to \" + dest");
        }
    }

    public boolean exists(final java.lang.String newFilePath) throws java.io.IOException, java.lang.InterruptedException {
        return execute(new java.security.PrivilegedExceptionAction<java.lang.Boolean>() {
            public java.lang.Boolean run() throws java.lang.Exception {
                return fs.exists(new org.apache.hadoop.fs.Path(newFilePath));
            }
        });
    }

    public <T> T execute(java.security.PrivilegedExceptionAction<T> action) throws java.io.IOException, java.lang.InterruptedException {
        return this.execute(action, false);
    }

    public <T> T execute(java.security.PrivilegedExceptionAction<T> action, boolean alwaysRetry) throws java.io.IOException, java.lang.InterruptedException {
        T result = null;
        int tryNumber = 0;
        boolean succeeded = false;
        do {
            tryNumber += 1;
            try {
                result = ugi.doAs(action);
                succeeded = true;
            } catch (java.io.IOException ex) {
                if ((!com.google.common.base.Strings.isNullOrEmpty(ex.getMessage())) && (!ex.getMessage().contains("Cannot obtain block length for"))) {
                    throw ex;
                }
                if (tryNumber >= 3) {
                    throw ex;
                }
                org.apache.ambari.view.utils.hdfs.HdfsApi.LOG.info(("HDFS threw 'IOException: Cannot obtain block length' exception. " + "Retrying... Try #") + (tryNumber + 1));
                org.apache.ambari.view.utils.hdfs.HdfsApi.LOG.error("Retrying: " + ex.getMessage(), ex);
                java.lang.Thread.sleep(1000);
            }
        } while (!succeeded );
        return result;
    }

    private static java.lang.String permissionToString(org.apache.hadoop.fs.permission.FsPermission p) {
        return p == null ? "default" : (("-" + p.getUserAction().SYMBOL) + p.getGroupAction().SYMBOL) + p.getOtherAction().SYMBOL;
    }

    public java.util.Map<java.lang.String, java.lang.Object> fileStatusToJSON(org.apache.hadoop.fs.FileStatus status) {
        java.util.Map<java.lang.String, java.lang.Object> json = new java.util.LinkedHashMap<java.lang.String, java.lang.Object>();
        json.put("path", org.apache.hadoop.fs.Path.getPathWithoutSchemeAndAuthority(status.getPath()).toString());
        json.put("isDirectory", status.isDirectory());
        json.put("len", status.getLen());
        json.put("owner", status.getOwner());
        json.put("group", status.getGroup());
        json.put("permission", org.apache.ambari.view.utils.hdfs.HdfsApi.permissionToString(status.getPermission()));
        json.put("accessTime", status.getAccessTime());
        json.put("modificationTime", status.getModificationTime());
        json.put("blockSize", status.getBlockSize());
        json.put("replication", status.getReplication());
        json.put("readAccess", org.apache.ambari.view.utils.hdfs.HdfsApi.checkAccessPermissions(status, FsAction.READ, ugi));
        json.put("writeAccess", org.apache.ambari.view.utils.hdfs.HdfsApi.checkAccessPermissions(status, FsAction.WRITE, ugi));
        json.put("executeAccess", org.apache.ambari.view.utils.hdfs.HdfsApi.checkAccessPermissions(status, FsAction.EXECUTE, ugi));
        json.put(org.apache.ambari.view.utils.hdfs.HdfsApi.KeyIsErasureCoded, status.isErasureCoded());
        json.put(org.apache.ambari.view.utils.hdfs.HdfsApi.KeyIsEncrypted, status.isEncrypted());
        if (status instanceof org.apache.hadoop.hdfs.protocol.HdfsFileStatus) {
            org.apache.hadoop.hdfs.protocol.HdfsFileStatus hdfsFileStatus = ((org.apache.hadoop.hdfs.protocol.HdfsFileStatus) (status));
            if (null != hdfsFileStatus.getErasureCodingPolicy()) {
                json.put(org.apache.ambari.view.utils.hdfs.HdfsApi.KeyErasureCodingPolicyName, hdfsFileStatus.getErasureCodingPolicy().getName());
            }
        }
        return json;
    }

    @java.lang.SuppressWarnings("unchecked")
    public org.json.simple.JSONArray fileStatusToJSON(org.apache.hadoop.fs.FileStatus[] status) {
        org.json.simple.JSONArray json = new org.json.simple.JSONArray();
        if (status != null) {
            for (org.apache.hadoop.fs.FileStatus s : status) {
                json.add(fileStatusToJSON(s));
            }
        }
        return json;
    }

    public static boolean checkAccessPermissions(org.apache.hadoop.fs.FileStatus stat, org.apache.hadoop.fs.permission.FsAction mode, org.apache.hadoop.security.UserGroupInformation ugi) {
        org.apache.hadoop.fs.permission.FsPermission perm = stat.getPermission();
        java.lang.String user = ugi.getShortUserName();
        java.util.List<java.lang.String> groups = java.util.Arrays.asList(ugi.getGroupNames());
        if (user.equals(stat.getOwner())) {
            if (perm.getUserAction().implies(mode)) {
                return true;
            }
        } else if (groups.contains(stat.getGroup())) {
            if (perm.getGroupAction().implies(mode)) {
                return true;
            }
        } else if (perm.getOtherAction().implies(mode)) {
            return true;
        }
        return false;
    }
}