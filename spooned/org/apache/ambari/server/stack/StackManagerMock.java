package org.apache.ambari.server.stack;
import com.google.inject.assistedinject.Assisted;
import javax.annotation.Nullable;
public class StackManagerMock extends org.apache.ambari.server.stack.StackManager {
    private static final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();

    private static final java.util.Map<org.apache.ambari.server.stack.StackManagerMock.ModulesPathsKey, org.apache.ambari.server.stack.StackManagerMock.CachedModules> pathsToCachedModulesMap = new java.util.HashMap<>();

    public static void invalidateKey(java.io.File stackRoot, java.io.File commonServicesRoot, java.io.File extensionRoot) {
        org.apache.ambari.server.stack.StackManagerMock.ModulesPathsKey pathsKey = new org.apache.ambari.server.stack.StackManagerMock.ModulesPathsKey(stackRoot, commonServicesRoot, extensionRoot);
        org.apache.ambari.server.stack.StackManagerMock.pathsToCachedModulesMap.remove(pathsKey);
    }

    private java.io.File currentStackRoot;

    private java.io.File currentCommonServicesRoot;

    private java.io.File currentExtensionRoot;

    public void invalidateCurrentPaths() {
        org.apache.ambari.server.stack.StackManagerMock.invalidateKey(currentStackRoot, currentCommonServicesRoot, currentExtensionRoot);
    }

    private static class ModulesPathsKey {
        private java.lang.String stackRoot;

        private java.lang.String commonServicesRoot;

        private java.lang.String extensionRoot;

        public ModulesPathsKey(java.io.File stackRoot, java.io.File commonServicesRoot, java.io.File extensionRoot) {
            this.stackRoot = (stackRoot == null) ? "" : stackRoot.getPath();
            this.commonServicesRoot = (commonServicesRoot == null) ? "" : commonServicesRoot.getPath();
            this.extensionRoot = (extensionRoot == null) ? "" : extensionRoot.getPath();
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            org.apache.ambari.server.stack.StackManagerMock.ModulesPathsKey that = ((org.apache.ambari.server.stack.StackManagerMock.ModulesPathsKey) (o));
            if (stackRoot != null ? !stackRoot.equals(that.stackRoot) : that.stackRoot != null) {
                return false;
            }
            if (commonServicesRoot != null ? !commonServicesRoot.equals(that.commonServicesRoot) : that.commonServicesRoot != null) {
                return false;
            }
            return !(extensionRoot != null ? !extensionRoot.equals(that.extensionRoot) : that.extensionRoot != null);
        }

        @java.lang.Override
        public int hashCode() {
            int result = (stackRoot != null) ? stackRoot.hashCode() : 0;
            result = (31 * result) + (commonServicesRoot != null ? commonServicesRoot.hashCode() : 0);
            result = (31 * result) + (extensionRoot != null ? extensionRoot.hashCode() : 0);
            return result;
        }
    }

    private static class CachedModules {
        private java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> cachedCommonServiceModules;

        private java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> cachedStackModules;

        private java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> cachedExtensionModules;

        private java.util.NavigableMap<java.lang.String, org.apache.ambari.server.state.StackInfo> cachedStackMap;

        public CachedModules(java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> cachedCommonServiceModules, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> cachedStackModules, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> cachedExtensionModules, java.util.NavigableMap<java.lang.String, org.apache.ambari.server.state.StackInfo> cachedStackMap) {
            this.cachedCommonServiceModules = cachedCommonServiceModules;
            this.cachedStackModules = cachedStackModules;
            this.cachedExtensionModules = cachedExtensionModules;
            this.cachedStackMap = cachedStackMap;
        }

        public java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> getCachedCommonServiceModules() {
            return cachedCommonServiceModules;
        }

        public java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> getCachedStackModules() {
            return cachedStackModules;
        }

        public java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> getCachedExtensionModules() {
            return cachedExtensionModules;
        }

        public java.util.NavigableMap<java.lang.String, org.apache.ambari.server.state.StackInfo> getCachedStackMap() {
            return cachedStackMap;
        }
    }

    @com.google.inject.Inject
    public StackManagerMock(@com.google.inject.assistedinject.Assisted("stackRoot")
    java.io.File stackRoot, @javax.annotation.Nullable
    @com.google.inject.assistedinject.Assisted("commonServicesRoot")
    java.io.File commonServicesRoot, @com.google.inject.assistedinject.Assisted("extensionRoot")
    @javax.annotation.Nullable
    java.io.File extensionRoot, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.stack.OsFamily osFamily, @com.google.inject.assistedinject.Assisted
    boolean validate, org.apache.ambari.server.orm.dao.MetainfoDAO metaInfoDAO, org.apache.ambari.server.metadata.ActionMetadata actionMetadata, org.apache.ambari.server.orm.dao.StackDAO stackDao, org.apache.ambari.server.orm.dao.ExtensionDAO extensionDao, org.apache.ambari.server.orm.dao.ExtensionLinkDAO linkDao, org.apache.ambari.server.controller.AmbariManagementHelper helper) throws org.apache.ambari.server.AmbariException {
        super(stackRoot, commonServicesRoot, extensionRoot, osFamily, validate, metaInfoDAO, actionMetadata, stackDao, extensionDao, linkDao, helper);
        currentStackRoot = stackRoot;
        currentCommonServicesRoot = commonServicesRoot;
        currentExtensionRoot = extensionRoot;
    }

    @java.lang.Override
    protected void parseDirectories(java.io.File stackRoot, java.io.File commonServicesRoot, java.io.File extensionRoot) throws org.apache.ambari.server.AmbariException {
        try {
            org.apache.ambari.server.stack.StackManagerMock.lock.lock();
            org.apache.ambari.server.stack.StackManagerMock.ModulesPathsKey pathsKey = new org.apache.ambari.server.stack.StackManagerMock.ModulesPathsKey(stackRoot, commonServicesRoot, extensionRoot);
            org.apache.ambari.server.stack.StackManagerMock.CachedModules cachedModules = org.apache.ambari.server.stack.StackManagerMock.pathsToCachedModulesMap.get(pathsKey);
            if (cachedModules == null) {
                super.parseDirectories(stackRoot, commonServicesRoot, extensionRoot);
                org.apache.ambari.server.stack.StackManagerMock.CachedModules newEntry = new org.apache.ambari.server.stack.StackManagerMock.CachedModules(commonServiceModules, stackModules, extensionModules, stackMap);
                org.apache.ambari.server.stack.StackManagerMock.pathsToCachedModulesMap.put(pathsKey, newEntry);
            } else {
                commonServiceModules = cachedModules.getCachedCommonServiceModules();
                stackModules = cachedModules.getCachedStackModules();
                extensionModules = cachedModules.getCachedExtensionModules();
                stackMap = cachedModules.getCachedStackMap();
            }
        } finally {
            org.apache.ambari.server.stack.StackManagerMock.lock.unlock();
        }
    }
}