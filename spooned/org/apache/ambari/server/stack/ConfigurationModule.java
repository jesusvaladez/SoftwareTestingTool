package org.apache.ambari.server.stack;
public class ConfigurationModule extends org.apache.ambari.server.stack.BaseModule<org.apache.ambari.server.stack.ConfigurationModule, org.apache.ambari.server.stack.ConfigurationInfo> implements org.apache.ambari.server.stack.Validable {
    private java.lang.String configType;

    org.apache.ambari.server.stack.ConfigurationInfo info;

    private boolean isDeleted;

    protected boolean valid = true;

    private java.util.Set<java.lang.String> errorSet = new java.util.HashSet<>();

    @java.lang.Override
    public void addError(java.lang.String error) {
        errorSet.add(error);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getErrors() {
        return errorSet;
    }

    public ConfigurationModule(java.lang.String configType, org.apache.ambari.server.stack.ConfigurationInfo info) {
        this.configType = configType;
        this.info = info;
        if ((info != null) && (!info.isValid())) {
            setValid(info.isValid());
            addErrors(info.getErrors());
        }
    }

    @java.lang.Override
    public void resolve(org.apache.ambari.server.stack.ConfigurationModule parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        if (parent != null) {
            if (parent.info != null) {
                if ((!parent.isValid()) || (!parent.info.isValid())) {
                    setValid(false);
                    info.setValid(false);
                    addErrors(parent.getErrors());
                    addErrors(parent.info.getErrors());
                    info.addErrors(parent.getErrors());
                    info.addErrors(parent.info.getErrors());
                }
            }
            mergeProperties(parent);
            if (isExtensionEnabled()) {
                mergeAttributes(parent);
            }
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.stack.ConfigurationInfo getModuleInfo() {
        return info;
    }

    @java.lang.Override
    public boolean isDeleted() {
        return isDeleted;
    }

    @java.lang.Override
    public java.lang.String getId() {
        return getConfigType();
    }

    public java.lang.String getConfigType() {
        return configType;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    private void mergeProperties(org.apache.ambari.server.stack.ConfigurationModule parent) {
        java.util.Collection<java.lang.String> existingProps = new java.util.HashSet<>();
        java.util.Iterator<org.apache.ambari.server.state.PropertyInfo> iter = info.getProperties().iterator();
        while (iter.hasNext()) {
            org.apache.ambari.server.state.PropertyInfo prop = iter.next();
            existingProps.add((prop.getFilename() + "/") + prop.getName());
            if (prop.isDeleted()) {
                iter.remove();
            }
        } 
        if (isExtensionEnabled()) {
            for (org.apache.ambari.server.state.PropertyInfo prop : parent.info.getProperties()) {
                if (!existingProps.contains((prop.getFilename() + "/") + prop.getName())) {
                    info.getProperties().add(prop);
                }
            }
        }
    }

    private void mergeAttributes(org.apache.ambari.server.stack.ConfigurationModule parent) {
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentCategoryEntry : parent.info.getAttributes().entrySet()) {
            java.lang.String category = parentCategoryEntry.getKey();
            java.util.Map<java.lang.String, java.lang.String> categoryAttributeMap = info.getAttributes().get(category);
            if (categoryAttributeMap == null) {
                categoryAttributeMap = new java.util.HashMap<>();
                info.getAttributes().put(category, categoryAttributeMap);
            }
            for (java.util.Map.Entry<java.lang.String, java.lang.String> parentAttributeEntry : parentCategoryEntry.getValue().entrySet()) {
                java.lang.String attributeName = parentAttributeEntry.getKey();
                if (!categoryAttributeMap.containsKey(attributeName)) {
                    categoryAttributeMap.put(attributeName, parentAttributeEntry.getValue());
                }
            }
        }
    }

    private boolean isExtensionEnabled() {
        java.util.Map<java.lang.String, java.lang.String> supportsMap = getModuleInfo().getAttributes().get(org.apache.ambari.server.stack.ConfigurationInfo.Supports.KEYWORD);
        if (supportsMap == null) {
            return true;
        }
        java.lang.String val = supportsMap.get(org.apache.ambari.server.stack.ConfigurationInfo.Supports.DO_NOT_EXTEND.getPropertyName());
        return (val == null) || val.equals("false");
    }

    @java.lang.Override
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @java.lang.Override
    public void addErrors(java.util.Collection<java.lang.String> errors) {
        this.errorSet.addAll(errors);
    }
}