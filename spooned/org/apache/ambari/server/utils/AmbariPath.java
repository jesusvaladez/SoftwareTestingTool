package org.apache.ambari.server.utils;
public class AmbariPath {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.utils.AmbariPath.class);

    public static final java.lang.String AMBARI_SERVER_ROOT_ENV_VARIABLE = "ROOT";

    public static final java.lang.String rootDirectory = java.lang.System.getenv(org.apache.ambari.server.utils.AmbariPath.AMBARI_SERVER_ROOT_ENV_VARIABLE);

    public static java.lang.String getPath(java.lang.String path) {
        if (org.apache.ambari.server.utils.AmbariPath.rootDirectory == null) {
            org.apache.ambari.server.utils.AmbariPath.LOG.warn("Cannot get $ROOT enviroment variable. Installed to custom root directory Ambari might not work correctly.");
            return path;
        }
        java.lang.String result = (org.apache.ambari.server.utils.AmbariPath.rootDirectory + path).replaceAll("/+", "/");
        return result;
    }
}