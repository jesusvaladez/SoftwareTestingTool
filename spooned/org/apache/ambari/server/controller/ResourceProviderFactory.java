package org.apache.ambari.server.controller;
public interface ResourceProviderFactory {
    @javax.inject.Named("host")
    org.apache.ambari.server.controller.spi.ResourceProvider getHostResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController);

    @javax.inject.Named("hostComponent")
    org.apache.ambari.server.controller.spi.ResourceProvider getHostComponentResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController);

    @javax.inject.Named("service")
    org.apache.ambari.server.controller.spi.ResourceProvider getServiceResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController);

    @javax.inject.Named("component")
    org.apache.ambari.server.controller.spi.ResourceProvider getComponentResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController);

    @javax.inject.Named("member")
    org.apache.ambari.server.controller.spi.ResourceProvider getMemberResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController);

    @javax.inject.Named("user")
    org.apache.ambari.server.controller.spi.ResourceProvider getUserResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController);

    @javax.inject.Named("auth")
    org.apache.ambari.server.controller.spi.ResourceProvider getAuthResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController);

    @javax.inject.Named("userAuthenticationSource")
    org.apache.ambari.server.controller.spi.ResourceProvider getUserAuthenticationSourceResourceProvider();

    @javax.inject.Named("hostKerberosIdentity")
    org.apache.ambari.server.controller.spi.ResourceProvider getHostKerberosIdentityResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController);

    @javax.inject.Named("credential")
    org.apache.ambari.server.controller.spi.ResourceProvider getCredentialResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController);

    @javax.inject.Named("repositoryVersion")
    org.apache.ambari.server.controller.spi.ResourceProvider getRepositoryVersionResourceProvider();

    @javax.inject.Named("kerberosDescriptor")
    org.apache.ambari.server.controller.spi.ResourceProvider getKerberosDescriptorResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController);

    @javax.inject.Named("upgrade")
    org.apache.ambari.server.controller.internal.UpgradeResourceProvider getUpgradeResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController);

    @javax.inject.Named("rootServiceHostComponentConfiguration")
    org.apache.ambari.server.controller.spi.ResourceProvider getRootServiceHostComponentConfigurationResourceProvider();

    @javax.inject.Named("clusterStackVersion")
    org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProvider getClusterStackVersionResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController);

    @javax.inject.Named("alertTarget")
    org.apache.ambari.server.controller.internal.AlertTargetResourceProvider getAlertTargetResourceProvider();

    @javax.inject.Named("viewInstance")
    org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider getViewInstanceResourceProvider();
}