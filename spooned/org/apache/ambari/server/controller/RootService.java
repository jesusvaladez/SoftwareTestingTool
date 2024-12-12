package org.apache.ambari.server.controller;
public enum RootService {

    AMBARI(org.apache.ambari.server.controller.RootComponent.values());
    private final org.apache.ambari.server.controller.RootComponent[] components;

    RootService(org.apache.ambari.server.controller.RootComponent[] components) {
        this.components = components;
    }

    public org.apache.ambari.server.controller.RootComponent[] getComponents() {
        return components;
    }
}