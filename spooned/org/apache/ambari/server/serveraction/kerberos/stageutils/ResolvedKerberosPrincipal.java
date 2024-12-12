package org.apache.ambari.server.serveraction.kerberos.stageutils;
public class ResolvedKerberosPrincipal {
    private java.lang.Long hostId;

    private java.lang.String hostName;

    private java.lang.String principal;

    private boolean isService;

    private java.lang.String cacheFile;

    private com.google.common.collect.Multimap<java.lang.String, java.lang.String> serviceMapping = com.google.common.collect.ArrayListMultimap.create();

    private java.lang.String keytabPath;

    private org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab resolvedKerberosKeytab;

    public ResolvedKerberosPrincipal(java.lang.Long hostId, java.lang.String hostName, java.lang.String principal, boolean isService, java.lang.String cacheFile, java.lang.String serviceName, java.lang.String componentName, java.lang.String keytabPath) {
        this.hostId = hostId;
        this.hostName = hostName;
        this.principal = principal;
        this.isService = isService;
        this.cacheFile = cacheFile;
        this.keytabPath = keytabPath;
        addComponentMapping(serviceName, componentName);
    }

    public ResolvedKerberosPrincipal(java.lang.Long hostId, java.lang.String hostName, java.lang.String principal, boolean isService, java.lang.String cacheFile, java.lang.String keytabPath) {
        this.hostId = hostId;
        this.hostName = hostName;
        this.principal = principal;
        this.isService = isService;
        this.cacheFile = cacheFile;
        this.keytabPath = keytabPath;
    }

    public ResolvedKerberosPrincipal(java.lang.Long hostId, java.lang.String hostName, java.lang.String principal, boolean isService, java.lang.String cacheFile, java.lang.String keytabPath, com.google.common.collect.Multimap<java.lang.String, java.lang.String> serviceMapping) {
        this.hostId = hostId;
        this.hostName = hostName;
        this.principal = principal;
        this.isService = isService;
        this.cacheFile = cacheFile;
        this.keytabPath = keytabPath;
        this.serviceMapping = serviceMapping;
    }

    public void addComponentMapping(java.lang.String serviceName, java.lang.String componentName) {
        if (serviceName == null) {
            serviceName = "";
        }
        if (componentName == null) {
            componentName = "*";
        }
        serviceMapping.get(serviceName).add(componentName);
    }

    public void mergeComponentMapping(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal other) {
        serviceMapping.putAll(other.getServiceMapping());
    }

    public java.lang.String getKeytabPath() {
        return keytabPath;
    }

    public void setKeytabPath(java.lang.String keytabPath) {
        this.keytabPath = keytabPath;
    }

    public java.lang.Long getHostId() {
        return hostId;
    }

    public void setHostId(java.lang.Long hostId) {
        this.hostId = hostId;
    }

    public java.lang.String getHostName() {
        if (hostName == null) {
            return org.apache.ambari.server.utils.StageUtils.getHostName();
        }
        return hostName;
    }

    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }

    public java.lang.String getPrincipal() {
        return principal;
    }

    public void setPrincipal(java.lang.String principal) {
        this.principal = principal;
    }

    public boolean isService() {
        return isService;
    }

    public void setService(boolean service) {
        isService = service;
    }

    public java.lang.String getCacheFile() {
        return cacheFile;
    }

    public void setCacheFile(java.lang.String cacheFile) {
        this.cacheFile = cacheFile;
    }

    public com.google.common.collect.Multimap<java.lang.String, java.lang.String> getServiceMapping() {
        return serviceMapping;
    }

    public void setServiceMapping(com.google.common.collect.Multimap<java.lang.String, java.lang.String> serviceMapping) {
        this.serviceMapping = serviceMapping;
    }

    public org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab getResolvedKerberosKeytab() {
        return resolvedKerberosKeytab;
    }

    public void setResolvedKerberosKeytab(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab resolvedKerberosKeytab) {
        this.resolvedKerberosKeytab = resolvedKerberosKeytab;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal principal1 = ((org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal) (o));
        return ((((((isService == principal1.isService) && com.google.common.base.Objects.equal(hostId, principal1.hostId)) && com.google.common.base.Objects.equal(hostName, principal1.hostName)) && com.google.common.base.Objects.equal(principal, principal1.principal)) && com.google.common.base.Objects.equal(cacheFile, principal1.cacheFile)) && com.google.common.base.Objects.equal(serviceMapping, principal1.serviceMapping)) && com.google.common.base.Objects.equal(keytabPath, principal1.keytabPath);
    }

    @java.lang.Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(hostId, hostName, principal, isService, cacheFile, serviceMapping, keytabPath);
    }
}