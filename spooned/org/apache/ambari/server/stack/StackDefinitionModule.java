package org.apache.ambari.server.stack;
public interface StackDefinitionModule<T, I> {
    void resolve(T parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException;

    I getModuleInfo();

    boolean isDeleted();

    java.lang.String getId();

    void finalizeModule();

    org.apache.ambari.server.stack.ModuleState getModuleState();

    boolean isValid();

    void setValid(boolean valid);
}