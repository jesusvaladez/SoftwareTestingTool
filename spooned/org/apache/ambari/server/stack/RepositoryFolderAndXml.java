package org.apache.ambari.server.stack;
class RepositoryFolderAndXml {
    final com.google.common.base.Optional<java.lang.String> repoDir;

    final com.google.common.base.Optional<org.apache.ambari.server.state.stack.RepositoryXml> repoXml;

    public RepositoryFolderAndXml(com.google.common.base.Optional<java.lang.String> repoDir, com.google.common.base.Optional<org.apache.ambari.server.state.stack.RepositoryXml> repoXml) {
        this.repoDir = repoDir;
        this.repoXml = repoXml;
    }
}