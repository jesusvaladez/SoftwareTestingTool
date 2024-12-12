package org.apache.ambari.fast_hdfs_resource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
public class Runner {
    public static void main(java.lang.String[] args) throws java.io.IOException, java.net.URISyntaxException {
        if (args.length != 1) {
            java.lang.System.err.println("Incorrect number of arguments. Please provide:\n" + ("1) Path to json file\n" + "Exiting..."));
            java.lang.System.exit(1);
        }
        final java.lang.String jsonFilePath = args[0];
        java.io.File file = new java.io.File(jsonFilePath);
        if (!file.isFile()) {
            java.lang.System.err.println(("File " + jsonFilePath) + " doesn\'t exist.\nExiting...");
            java.lang.System.exit(1);
        }
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.fast_hdfs_resource.Resource[] resources = null;
        java.util.Map<java.lang.String, org.apache.hadoop.fs.FileSystem> fileSystemNameToInstance = new java.util.HashMap<java.lang.String, org.apache.hadoop.fs.FileSystem>();
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.fast_hdfs_resource.Resource>> fileSystemToResource = new java.util.HashMap<java.lang.String, java.util.List<org.apache.ambari.fast_hdfs_resource.Resource>>();
        boolean failed = false;
        try {
            resources = ((org.apache.ambari.fast_hdfs_resource.Resource[]) (gson.fromJson(new java.io.FileReader(jsonFilePath), org.apache.ambari.fast_hdfs_resource.Resource[].class)));
            org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
            org.apache.hadoop.fs.FileSystem dfs = null;
            java.lang.String defaultFsSchema = org.apache.hadoop.fs.FileSystem.getDefaultUri(conf).getScheme();
            for (org.apache.ambari.fast_hdfs_resource.Resource resource : resources) {
                java.lang.String fsName = null;
                java.net.URI targetURI = new java.net.URI(resource.getTarget());
                java.lang.String targetSchema = targetURI.getScheme();
                if ((targetSchema != null) && (!targetSchema.equals(defaultFsSchema))) {
                    java.lang.String authority = targetURI.getAuthority();
                    if (authority == null) {
                        authority = "";
                    }
                    fsName = java.lang.String.format("%s://%s/", targetSchema, authority);
                } else if (resource.getNameservice() != null) {
                    fsName = resource.getNameservice();
                }
                if (!fileSystemNameToInstance.containsKey(fsName)) {
                    java.net.URI fileSystemUrl;
                    if (fsName == null) {
                        fileSystemUrl = org.apache.hadoop.fs.FileSystem.getDefaultUri(conf);
                    } else {
                        fileSystemUrl = new java.net.URI(fsName);
                    }
                    dfs = org.apache.hadoop.fs.FileSystem.get(fileSystemUrl, conf);
                    java.lang.System.out.println("Initializing filesystem uri: " + fileSystemUrl);
                    dfs.initialize(fileSystemUrl, conf);
                    fileSystemNameToInstance.put(fsName, dfs);
                }
                if (!fileSystemToResource.containsKey(fsName)) {
                    fileSystemToResource.put(fsName, new java.util.ArrayList<org.apache.ambari.fast_hdfs_resource.Resource>());
                }
                fileSystemToResource.get(fsName).add(resource);
            }
            for (java.util.Map.Entry<java.lang.String, java.util.List<org.apache.ambari.fast_hdfs_resource.Resource>> entry : fileSystemToResource.entrySet()) {
                java.lang.String nameservice = entry.getKey();
                java.util.List<org.apache.ambari.fast_hdfs_resource.Resource> resourcesNameservice = entry.getValue();
                for (org.apache.ambari.fast_hdfs_resource.Resource resource : resourcesNameservice) {
                    if (nameservice != null) {
                        java.lang.System.out.println((("Creating: " + resource) + " in ") + nameservice);
                    } else {
                        java.lang.System.out.println(("Creating: " + resource) + " in default filesystem");
                    }
                    dfs = fileSystemNameToInstance.get(nameservice);
                    org.apache.ambari.fast_hdfs_resource.Resource.checkResourceParameters(resource, dfs);
                    org.apache.hadoop.fs.Path pathHadoop = null;
                    if (resource.getAction().equals("download")) {
                        pathHadoop = new org.apache.hadoop.fs.Path(resource.getSource());
                    } else {
                        java.lang.String path = resource.getTarget();
                        pathHadoop = new org.apache.hadoop.fs.Path(path);
                        if ((!resource.isManageIfExists()) && dfs.exists(pathHadoop)) {
                            java.lang.System.out.println(java.lang.String.format("Skipping the operation for not managed DFS directory %s  since immutable_paths contains it.", path));
                            continue;
                        }
                    }
                    if (resource.getAction().equals("create")) {
                        org.apache.ambari.fast_hdfs_resource.Resource.createResource(resource, dfs, pathHadoop);
                        org.apache.ambari.fast_hdfs_resource.Resource.setMode(resource, dfs, pathHadoop);
                        org.apache.ambari.fast_hdfs_resource.Resource.setOwner(resource, dfs, pathHadoop);
                    } else if (resource.getAction().equals("delete")) {
                        dfs.delete(pathHadoop, true);
                    } else if (resource.getAction().equals("download")) {
                        dfs.copyToLocalFile(pathHadoop, new org.apache.hadoop.fs.Path(resource.getTarget()));
                    }
                }
            }
        } catch (java.lang.Exception e) {
            java.lang.System.out.println("Exception occurred, Reason: " + e.getMessage());
            e.printStackTrace();
            failed = true;
        } finally {
            for (org.apache.hadoop.fs.FileSystem dfs : fileSystemNameToInstance.values()) {
                dfs.close();
            }
        }
        if (!failed) {
            java.lang.System.out.println("All resources created.");
        } else {
            java.lang.System.exit(1);
        }
    }
}