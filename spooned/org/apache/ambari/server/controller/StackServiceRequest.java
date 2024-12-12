package org.apache.ambari.server.controller;
public class StackServiceRequest extends org.apache.ambari.server.controller.StackVersionRequest {
    private java.lang.String serviceName;

    private java.lang.String credentialStoreSupported;

    private java.lang.String credentialStoreEnabled;

    public StackServiceRequest(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName) {
        this(stackName, stackVersion, serviceName, null, null);
    }

    public StackServiceRequest(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String credentialStoreSupported, java.lang.String credentialStoreEnabled) {
        super(stackName, stackVersion);
        this.setServiceName(serviceName);
        this.setCredentialStoreSupported(credentialStoreSupported);
        this.setCredentialStoreEnabled(credentialStoreEnabled);
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getCredentialStoreSupported() {
        return credentialStoreSupported;
    }

    public void setCredentialStoreSupported(java.lang.String credentialStoreSupported) {
        this.credentialStoreSupported = credentialStoreSupported;
    }

    public java.lang.String getCredentialStoreEnabled() {
        return credentialStoreEnabled;
    }

    public void setCredentialStoreEnabled(java.lang.String credentialStoreEnabled) {
        this.credentialStoreEnabled = credentialStoreEnabled;
    }
}