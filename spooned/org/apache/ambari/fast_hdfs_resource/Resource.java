package org.apache.ambari.fast_hdfs_resource;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
public class Resource {
    private java.lang.String source;

    private java.lang.String target;

    private java.lang.String type;

    private java.lang.String action;

    private java.lang.String owner;

    private java.lang.String group;

    private java.lang.String mode;

    private java.lang.String nameservice;

    private boolean recursiveChown;

    private boolean recursiveChmod;

    private boolean changePermissionforParents;

    private boolean manageIfExists;

    public java.lang.String getSource() {
        return source;
    }

    public void setSource(java.lang.String source) {
        this.source = source;
    }

    public java.lang.String getTarget() {
        return target;
    }

    public void setTarget(java.lang.String target) {
        this.target = target;
    }

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public java.lang.String getAction() {
        return action;
    }

    public void setAction(java.lang.String action) {
        this.action = action;
    }

    public java.lang.String getOwner() {
        return owner;
    }

    public void setOwner(java.lang.String owner) {
        this.owner = owner;
    }

    public java.lang.String getGroup() {
        return group;
    }

    public void setGroup(java.lang.String group) {
        this.group = group;
    }

    public java.lang.String getMode() {
        return mode;
    }

    public void setMode(java.lang.String mode) {
        this.mode = mode;
    }

    public java.lang.String getNameservice() {
        return nameservice;
    }

    public void setNameservice(java.lang.String nameservice) {
        this.nameservice = nameservice;
    }

    public boolean isRecursiveChown() {
        return recursiveChown;
    }

    public void setRecursiveChown(boolean recursiveChown) {
        this.recursiveChown = recursiveChown;
    }

    public boolean isRecursiveChmod() {
        return recursiveChmod;
    }

    public void setRecursiveChmod(boolean recursiveChmod) {
        this.recursiveChmod = recursiveChmod;
    }

    public boolean isChangePermissionOnParents() {
        return changePermissionforParents;
    }

    public void setChangePermissionOnParents(boolean changePermissionforParents) {
        this.changePermissionforParents = changePermissionforParents;
    }

    public boolean isManageIfExists() {
        return manageIfExists;
    }

    public void setManageIfExists(boolean manageIfExists) {
        this.manageIfExists = manageIfExists;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((((((((((((("Resource [source=" + source) + ", target=") + target) + ", type=") + type) + ", action=") + action) + ", owner=") + owner) + ", group=") + group) + ", mode=") + mode) + ", recursiveChown=") + recursiveChown) + ", recursiveChmod=") + recursiveChmod) + ", changePermissionforParents=") + changePermissionforParents) + ", manageIfExists=") + manageIfExists) + "]";
    }

    public static void checkResourceParameters(org.apache.ambari.fast_hdfs_resource.Resource resource, org.apache.hadoop.fs.FileSystem dfs) throws java.lang.IllegalArgumentException, java.io.IOException {
        java.util.ArrayList<java.lang.String> actionsAvailable = new java.util.ArrayList<java.lang.String>();
        actionsAvailable.add("create");
        actionsAvailable.add("delete");
        actionsAvailable.add("download");
        java.util.ArrayList<java.lang.String> typesAvailable = new java.util.ArrayList<java.lang.String>();
        typesAvailable.add("file");
        typesAvailable.add("directory");
        if ((resource.getAction() == null) || (!actionsAvailable.contains(resource.getAction()))) {
            throw new java.lang.IllegalArgumentException("Action is not supported.");
        }
        java.lang.String dfsPath = resource.getTarget();
        java.lang.String localPath = resource.getSource();
        if (resource.getAction().equals("download")) {
            dfsPath = resource.getSource();
            localPath = resource.getTarget();
        }
        if (dfsPath == null) {
            throw new java.lang.IllegalArgumentException("Path to resource in HadoopFs must be filled.");
        }
        if ((resource.getType() == null) || (!typesAvailable.contains(resource.getType()))) {
            throw new java.lang.IllegalArgumentException("Type is not supported.");
        }
        if (dfs.isFile(new org.apache.hadoop.fs.Path(dfsPath)) && (!"file".equals(resource.getType()))) {
            throw new java.lang.IllegalArgumentException(("Cannot create a directory " + dfsPath) + " because file is present on the given path.");
        } else if (dfs.isDirectory(new org.apache.hadoop.fs.Path(dfsPath)) && (!"directory".equals(resource.getType()))) {
            throw new java.lang.IllegalArgumentException(("Cannot create a file " + dfsPath) + " because directory is present on the given path.");
        }
        if (localPath != null) {
            java.io.File local = new java.io.File(localPath);
            if (local.isFile() && (!"file".equals(resource.getType()))) {
                throw new java.lang.IllegalArgumentException(((("Cannot create a directory " + dfsPath) + " because source ") + localPath) + "is a file");
            } else if (local.isDirectory() && (!"directory".equals(resource.getType()))) {
                throw new java.lang.IllegalArgumentException(((("Cannot create a file " + dfsPath) + " because source ") + localPath) + "is a directory");
            }
        }
    }

    public static void createResource(org.apache.ambari.fast_hdfs_resource.Resource resource, org.apache.hadoop.fs.FileSystem dfs, org.apache.hadoop.fs.Path pathHadoop) throws java.io.IOException {
        boolean isCreate = (resource.getSource() == null) ? true : false;
        if (isCreate && resource.getType().equals("directory")) {
            dfs.mkdirs(pathHadoop);
        } else if (isCreate && resource.getType().equals("file")) {
            dfs.createNewFile(pathHadoop);
        } else if (dfs.exists(pathHadoop) && dfs.getFileStatus(pathHadoop).isDir()) {
            java.lang.System.out.println(("Skipping copy from local, as target " + pathHadoop) + " is an existing directory.");
        } else {
            dfs.copyFromLocalFile(new org.apache.hadoop.fs.Path(resource.getSource()), pathHadoop);
        }
    }

    public static void setMode(org.apache.ambari.fast_hdfs_resource.Resource resource, org.apache.hadoop.fs.FileSystem dfs, org.apache.hadoop.fs.Path pathHadoop) throws java.io.IOException {
        if (resource.getMode() != null) {
            org.apache.hadoop.fs.permission.FsPermission permission = new org.apache.hadoop.fs.permission.FsPermission(((short) (java.lang.Integer.parseInt(resource.getMode(), 8))));
            dfs.setPermission(pathHadoop, permission);
            java.util.HashSet<java.lang.String> resultSet = new java.util.HashSet<java.lang.String>();
            if (resource.isRecursiveChmod())
                resource.fillDirectoryList(dfs, resource.getTarget(), resultSet);

            if (resource.isChangePermissionOnParents())
                resource.fillInParentDirectories(dfs, resource.getTarget(), resultSet);

            for (java.lang.String path : resultSet) {
                dfs.setPermission(new org.apache.hadoop.fs.Path(path), permission);
            }
        }
    }

    public static void setOwner(org.apache.ambari.fast_hdfs_resource.Resource resource, org.apache.hadoop.fs.FileSystem dfs, org.apache.hadoop.fs.Path pathHadoop) throws java.io.IOException {
        if (!((resource.getOwner() == null) && (resource.getGroup() == null))) {
            dfs.setOwner(pathHadoop, resource.getOwner(), resource.getGroup());
            java.util.HashSet<java.lang.String> resultSet = new java.util.HashSet<java.lang.String>();
            if (resource.isRecursiveChown())
                resource.fillDirectoryList(dfs, resource.getTarget(), resultSet);

            if (resource.isChangePermissionOnParents())
                resource.fillInParentDirectories(dfs, resource.getTarget(), resultSet);

            for (java.lang.String path : resultSet) {
                dfs.setOwner(new org.apache.hadoop.fs.Path(path), resource.getOwner(), resource.getGroup());
            }
        }
    }

    public void fillInParentDirectories(org.apache.hadoop.fs.FileSystem dfs, java.lang.String path, java.util.HashSet<java.lang.String> resultSet) throws java.io.IOException {
        org.apache.hadoop.fs.Path filePath = new org.apache.hadoop.fs.Path(path);
        while (true) {
            filePath = filePath.getParent();
            if (filePath.getParent() == null) {
                break;
            }
            resultSet.add(filePath.toString());
        } 
    }

    public void fillDirectoryList(org.apache.hadoop.fs.FileSystem dfs, java.lang.String path, java.util.HashSet<java.lang.String> resultSet) throws java.io.IOException {
        org.apache.hadoop.fs.FileStatus[] fileStatus = dfs.listStatus(new org.apache.hadoop.fs.Path(path));
        if (fileStatus != null) {
            for (org.apache.hadoop.fs.FileStatus fs : fileStatus) {
                java.lang.String pathToResource = (path + "/") + fs.getPath().getName();
                resultSet.add(pathToResource);
                if (fs.isDir()) {
                    fillDirectoryList(dfs, pathToResource, resultSet);
                }
            }
        }
    }
}