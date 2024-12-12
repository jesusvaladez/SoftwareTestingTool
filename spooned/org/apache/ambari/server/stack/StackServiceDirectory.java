package org.apache.ambari.server.stack;
import javax.annotation.Nullable;
public class StackServiceDirectory extends org.apache.ambari.server.stack.ServiceDirectory {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.StackServiceDirectory.class);

    @javax.annotation.Nullable
    private org.apache.ambari.server.state.stack.RepositoryXml repoFile;

    @javax.annotation.Nullable
    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    private java.lang.String repoDir;

    public StackServiceDirectory(java.lang.String servicePath) throws org.apache.ambari.server.AmbariException {
        super(servicePath);
    }

    @javax.annotation.Nullable
    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    public org.apache.ambari.server.state.stack.RepositoryXml getRepoFile() {
        return repoFile;
    }

    @javax.annotation.Nullable
    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    public java.lang.String getRepoDir() {
        return repoDir;
    }

    @java.lang.Override
    public java.lang.String getAdvisorName(java.lang.String serviceName) {
        if ((getAdvisorFile() == null) || (serviceName == null))
            return null;

        java.io.File serviceDir = new java.io.File(getAbsolutePath());
        java.io.File stackVersionDir = serviceDir.getParentFile().getParentFile();
        java.io.File stackDir = stackVersionDir.getParentFile();
        java.lang.String stackName = stackDir.getName();
        java.lang.String versionString = stackVersionDir.getName().replaceAll("\\.", "");
        java.lang.String advisorClassName = ((stackName + versionString) + serviceName) + "ServiceAdvisor";
        advisorClassName = advisorClassName.replaceAll("[^a-zA-Z0-9]+", "");
        return advisorClassName;
    }

    private void parseRepoFile(java.util.Collection<java.lang.String> subDirs) {
        org.apache.ambari.server.stack.RepositoryFolderAndXml repoDirAndXml = org.apache.ambari.server.stack.RepoUtil.parseRepoFile(directory, subDirs, unmarshaller);
        repoDir = repoDirAndXml.repoDir.orNull();
        repoFile = repoDirAndXml.repoXml.orNull();
        if ((repoFile == null) || (!repoFile.isValid())) {
            org.apache.ambari.server.stack.StackServiceDirectory.LOG.info(((((("No repository information defined for " + ", serviceName=") + getName()) + ", repoFolder=") + getPath()) + java.io.File.separator) + org.apache.ambari.server.stack.RepoUtil.REPOSITORY_FOLDER_NAME);
        }
    }

    @java.lang.Override
    protected void parsePath() throws org.apache.ambari.server.AmbariException {
        super.parsePath();
        java.util.Collection<java.lang.String> subDirs = java.util.Arrays.asList(directory.list());
        parseRepoFile(subDirs);
    }

    @java.lang.Override
    protected java.io.File getResourcesDirectory() {
        java.io.File serviceDir = new java.io.File(getAbsolutePath());
        return serviceDir.getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
    }

    @java.lang.Override
    public java.lang.String getService() {
        java.io.File serviceDir = new java.io.File(getAbsolutePath());
        return serviceDir.getName();
    }

    @java.lang.Override
    public java.lang.String getStack() {
        java.io.File serviceDir = new java.io.File(getAbsolutePath());
        java.io.File stackVersionDir = serviceDir.getParentFile().getParentFile();
        java.io.File stackDir = stackVersionDir.getParentFile();
        java.lang.String stackId = java.lang.String.format("%s-%s", stackDir.getName(), stackVersionDir.getName());
        return stackId;
    }
}