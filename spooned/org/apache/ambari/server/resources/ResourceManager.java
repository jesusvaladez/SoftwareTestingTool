package org.apache.ambari.server.resources;
@com.google.inject.Singleton
public class ResourceManager {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.resources.ResourceManager.class);

    @com.google.inject.Inject
    org.apache.ambari.server.configuration.Configuration configs;

    public java.io.File getResource(java.lang.String resourcePath) {
        java.lang.String resDir = configs.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.RESOURCES_DIR.getKey());
        java.lang.String resourcePathIndep = resourcePath.replace("/", java.io.File.separator);
        java.io.File resourceFile = new java.io.File((resDir + java.io.File.separator) + resourcePathIndep);
        if (org.apache.ambari.server.resources.ResourceManager.LOG.isDebugEnabled()) {
            org.apache.ambari.server.resources.ResourceManager.LOG.debug("Resource requested from ResourceManager, resourceDir={}, resourcePath={}, fileExists={}", resDir, resourcePathIndep, resourceFile.exists());
        }
        return resourceFile;
    }
}