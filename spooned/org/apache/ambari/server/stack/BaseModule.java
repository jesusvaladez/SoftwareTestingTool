package org.apache.ambari.server.stack;
public abstract class BaseModule<T, I> implements org.apache.ambari.server.stack.StackDefinitionModule<T, I> {
    protected org.apache.ambari.server.stack.ModuleState moduleState = org.apache.ambari.server.stack.ModuleState.INIT;

    @java.lang.Override
    public org.apache.ambari.server.stack.ModuleState getModuleState() {
        return moduleState;
    }

    protected <T extends org.apache.ambari.server.stack.StackDefinitionModule<T, ?>> java.util.Collection<T> mergeChildModules(java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions, java.util.Map<java.lang.String, T> modules, java.util.Map<java.lang.String, T> parentModules) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> addedModules = new java.util.HashSet<>();
        java.util.Collection<T> mergedModules = new java.util.HashSet<>();
        for (T module : modules.values()) {
            java.lang.String id = module.getId();
            addedModules.add(id);
            if (!module.isDeleted()) {
                if (parentModules.containsKey(id)) {
                    module.resolve(parentModules.get(id), allStacks, commonServices, extensions);
                }
            }
            mergedModules.add(module);
        }
        for (T parentModule : parentModules.values()) {
            java.lang.String id = parentModule.getId();
            if (!addedModules.contains(id)) {
                mergedModules.add(parentModule);
            }
        }
        return mergedModules;
    }

    protected void finalizeChildModules(java.util.Collection<? extends org.apache.ambari.server.stack.StackDefinitionModule> modules) {
        java.util.Iterator<? extends org.apache.ambari.server.stack.StackDefinitionModule> iter = modules.iterator();
        while (iter.hasNext()) {
            org.apache.ambari.server.stack.StackDefinitionModule module = iter.next();
            module.finalizeModule();
            if (module.isDeleted()) {
                iter.remove();
            }
        } 
    }

    @java.lang.Override
    public void finalizeModule() {
    }
}