package org.apache.ambari.view;
public interface View {
    public void onDeploy(org.apache.ambari.view.ViewDefinition definition);

    public void onCreate(org.apache.ambari.view.ViewInstanceDefinition definition);

    public void onDestroy(org.apache.ambari.view.ViewInstanceDefinition definition);

    public void onUpdate(org.apache.ambari.view.ViewInstanceDefinition definition);
}