package org.apache.ambari.server.controller.internal;
public class CompatibleRepositoryVersion {
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity;

    private java.util.Set<org.apache.ambari.spi.upgrade.UpgradeType> supportedTypes;

    public CompatibleRepositoryVersion(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity) {
        this.repositoryVersionEntity = repositoryVersionEntity;
        this.supportedTypes = new java.util.HashSet<>();
    }

    public void addUpgradePackType(org.apache.ambari.spi.upgrade.UpgradeType type) {
        supportedTypes.add(type);
    }

    public java.util.Set<org.apache.ambari.spi.upgrade.UpgradeType> getSupportedTypes() {
        return supportedTypes;
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getRepositoryVersionEntity() {
        return repositoryVersionEntity;
    }
}