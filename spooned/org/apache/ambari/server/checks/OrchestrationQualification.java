package org.apache.ambari.server.checks;
import org.apache.commons.lang.ArrayUtils;
public final class OrchestrationQualification implements org.apache.ambari.spi.upgrade.CheckQualification {
    private final java.lang.Class<? extends org.apache.ambari.spi.upgrade.UpgradeCheck> m_checkClass;

    public OrchestrationQualification(java.lang.Class<? extends org.apache.ambari.spi.upgrade.UpgradeCheck> checkClass) {
        m_checkClass = checkClass;
    }

    @java.lang.Override
    public boolean isApplicable(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.RepositoryVersion repositoryVersion = request.getTargetRepositoryVersion();
        org.apache.ambari.spi.RepositoryType repositoryType = repositoryVersion.getRepositoryType();
        org.apache.ambari.annotations.UpgradeCheckInfo annotation = m_checkClass.getAnnotation(org.apache.ambari.annotations.UpgradeCheckInfo.class);
        if (null == annotation) {
            return true;
        }
        org.apache.ambari.spi.RepositoryType[] repositoryTypes = annotation.orchestration();
        if (org.apache.commons.lang.ArrayUtils.isEmpty(repositoryTypes) || org.apache.commons.lang.ArrayUtils.contains(repositoryTypes, repositoryType)) {
            return true;
        }
        return false;
    }
}