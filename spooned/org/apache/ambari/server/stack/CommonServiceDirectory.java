package org.apache.ambari.server.stack;
public class CommonServiceDirectory extends org.apache.ambari.server.stack.ServiceDirectory {
    public CommonServiceDirectory(java.lang.String servicePath) throws org.apache.ambari.server.AmbariException {
        super(servicePath);
    }

    @java.lang.Override
    public java.lang.String getAdvisorName(java.lang.String serviceName) {
        if ((getAdvisorFile() == null) || (serviceName == null))
            return null;

        java.io.File serviceVersionDir = new java.io.File(getAbsolutePath());
        java.lang.String serviceVersion = serviceVersionDir.getName().replaceAll("\\.", "");
        java.lang.String advisorName = (serviceName + serviceVersion) + "ServiceAdvisor";
        return advisorName;
    }

    @java.lang.Override
    public java.lang.String getService() {
        java.io.File serviceVersionDir = new java.io.File(getAbsolutePath());
        java.io.File serviceDir = serviceVersionDir.getParentFile();
        java.lang.String service = java.lang.String.format("%s-%s", serviceDir.getName(), serviceVersionDir.getName());
        return service;
    }

    @java.lang.Override
    protected java.io.File getResourcesDirectory() {
        java.io.File serviceVersionDir = new java.io.File(getAbsolutePath());
        return serviceVersionDir.getParentFile().getParentFile().getParentFile();
    }

    @java.lang.Override
    public java.lang.String getStack() {
        return "common-services";
    }
}