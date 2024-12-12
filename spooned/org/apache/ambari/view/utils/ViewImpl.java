package org.apache.ambari.view.utils;
public class ViewImpl implements org.apache.ambari.view.View {
    @java.lang.Override
    public void onDeploy(org.apache.ambari.view.ViewDefinition definition) {
    }

    @java.lang.Override
    public void onCreate(org.apache.ambari.view.ViewInstanceDefinition definition) {
    }

    @java.lang.Override
    public void onDestroy(org.apache.ambari.view.ViewInstanceDefinition definition) {
    }

    @java.lang.Override
    public void onUpdate(org.apache.ambari.view.ViewInstanceDefinition definition) {
        org.apache.ambari.view.utils.UserLocal.dropInstanceCache(definition.getInstanceName());
    }
}