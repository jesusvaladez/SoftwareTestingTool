package org.apache.ambari.server.orm.entities;
public class ServiceStackEntity {
    private final java.lang.String serviceName;

    private final org.apache.ambari.server.orm.entities.StackEntity stackEntity;

    public ServiceStackEntity(java.lang.String serviceName, org.apache.ambari.server.orm.entities.StackEntity stackEntity) {
        this.serviceName = serviceName;
        this.stackEntity = stackEntity;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public org.apache.ambari.server.orm.entities.StackEntity getStackEntity() {
        return stackEntity;
    }
}