package org.apache.ambari.server.controller;
public class OrderedRequestStageContainer {
    private final org.apache.ambari.server.stageplanner.RoleGraphFactory roleGraphFactory;

    private final org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder;

    private final org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer;

    public OrderedRequestStageContainer(org.apache.ambari.server.stageplanner.RoleGraphFactory roleGraphFactory, org.apache.ambari.server.metadata.RoleCommandOrder roleCommandOrder, org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer) {
        this.roleGraphFactory = roleGraphFactory;
        this.roleCommandOrder = roleCommandOrder;
        this.requestStageContainer = requestStageContainer;
    }

    public void addStage(org.apache.ambari.server.actionmanager.Stage stage) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(roleCommandOrder);
        roleGraph.build(stage);
        requestStageContainer.addStages(roleGraph.getStages());
    }

    public long getLastStageId() {
        return requestStageContainer.getLastStageId();
    }

    public long getId() {
        return requestStageContainer.getId();
    }

    public org.apache.ambari.server.controller.internal.RequestStageContainer getRequestStageContainer() {
        return requestStageContainer;
    }
}