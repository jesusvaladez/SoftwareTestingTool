package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class StackVersionResponse implements org.apache.ambari.server.stack.Validable {
    private java.lang.String minJdk;

    private java.lang.String maxJdk;

    private java.lang.String stackName;

    private java.lang.String stackVersion;

    private java.lang.String minUpgradeVersion;

    private boolean active;

    private boolean valid;

    private java.lang.String parentVersion;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configTypes;

    private java.util.Collection<java.io.File> serviceKerberosDescriptorFiles;

    private java.util.Set<java.lang.String> upgradePacks = java.util.Collections.emptySet();

    public StackVersionResponse(java.lang.String stackVersion, boolean active, java.lang.String parentVersion, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configTypes, java.util.Collection<java.io.File> serviceKerberosDescriptorFiles, java.util.Set<java.lang.String> upgradePacks, boolean valid, java.util.Collection<java.lang.String> errorSet, java.lang.String minJdk, java.lang.String maxJdk) {
        setStackVersion(stackVersion);
        setActive(active);
        setParentVersion(parentVersion);
        setConfigTypes(configTypes);
        setServiceKerberosDescriptorFiles(serviceKerberosDescriptorFiles);
        setUpgradePacks(upgradePacks);
        setValid(valid);
        addErrors(errorSet);
        setMinJdk(minJdk);
        setMaxJdk(maxJdk);
    }

    @java.lang.Override
    @io.swagger.annotations.ApiModelProperty(name = "valid")
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private java.util.Set<java.lang.String> errorSet = new java.util.HashSet<>();

    @java.lang.Override
    public void addError(java.lang.String error) {
        errorSet.add(error);
    }

    @java.lang.Override
    @io.swagger.annotations.ApiModelProperty(name = "stack-errors")
    public java.util.Collection<java.lang.String> getErrors() {
        return errorSet;
    }

    @java.lang.Override
    public void addErrors(java.util.Collection<java.lang.String> errors) {
        this.errorSet.addAll(errors);
    }

    @io.swagger.annotations.ApiModelProperty(name = "min_jdk")
    public java.lang.String getMinJdk() {
        return minJdk;
    }

    public void setMinJdk(java.lang.String minJdk) {
        this.minJdk = minJdk;
    }

    @io.swagger.annotations.ApiModelProperty(name = "max_jdk")
    public java.lang.String getMaxJdk() {
        return maxJdk;
    }

    public void setMaxJdk(java.lang.String maxJdk) {
        this.maxJdk = maxJdk;
    }

    @io.swagger.annotations.ApiModelProperty(name = "stack_name")
    public java.lang.String getStackName() {
        return stackName;
    }

    public void setStackName(java.lang.String stackName) {
        this.stackName = stackName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "stack_version")
    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    public void setStackVersion(java.lang.String stackVersion) {
        this.stackVersion = stackVersion;
    }

    @io.swagger.annotations.ApiModelProperty(name = "active")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @io.swagger.annotations.ApiModelProperty(name = "parent_stack_version")
    public java.lang.String getParentVersion() {
        return parentVersion;
    }

    public void setParentVersion(java.lang.String parentVersion) {
        this.parentVersion = parentVersion;
    }

    @io.swagger.annotations.ApiModelProperty(name = "config_types")
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> getConfigTypes() {
        return configTypes;
    }

    public void setConfigTypes(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configTypes) {
        this.configTypes = configTypes;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.util.Collection<java.io.File> getServiceKerberosDescriptorFiles() {
        return serviceKerberosDescriptorFiles;
    }

    public void setServiceKerberosDescriptorFiles(java.util.Collection<java.io.File> serviceKerberosDescriptorFiles) {
        this.serviceKerberosDescriptorFiles = serviceKerberosDescriptorFiles;
    }

    public void setUpgradePacks(java.util.Set<java.lang.String> upgradePacks) {
        this.upgradePacks = upgradePacks;
    }

    @io.swagger.annotations.ApiModelProperty(name = "upgrade_packs")
    public java.util.Set<java.lang.String> getUpgradePacks() {
        return upgradePacks;
    }

    public interface StackVersionResponseSwagger extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = "Versions")
        org.apache.ambari.server.controller.StackVersionResponse getStackVersionResponse();
    }
}