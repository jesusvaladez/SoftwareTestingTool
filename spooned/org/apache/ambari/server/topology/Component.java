package org.apache.ambari.server.topology;
public class Component {
    private final java.lang.String name;

    private final org.apache.ambari.server.controller.internal.ProvisionAction provisionAction;

    public Component(java.lang.String name) {
        this(name, null);
    }

    public Component(java.lang.String name, org.apache.ambari.server.controller.internal.ProvisionAction provisionAction) {
        this.name = name;
        this.provisionAction = provisionAction;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public org.apache.ambari.server.controller.internal.ProvisionAction getProvisionAction() {
        return this.provisionAction;
    }
}