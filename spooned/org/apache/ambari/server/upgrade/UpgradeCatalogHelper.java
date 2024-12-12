package org.apache.ambari.server.upgrade;
import com.google.inject.persist.Transactional;
public class UpgradeCatalogHelper {
    protected org.apache.ambari.server.orm.entities.ClusterEntity createCluster(com.google.inject.Injector injector, java.lang.String clusterName, org.apache.ambari.server.orm.entities.StackEntity desiredStackEntity, java.lang.String repositoryVersion) {
        org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ResourceTypeDAO.class);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = resourceTypeDAO.findById(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
        if (resourceTypeEntity == null) {
            resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
            resourceTypeEntity.setId(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
            resourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
            resourceTypeEntity = resourceTypeDAO.merge(resourceTypeEntity);
        }
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
        clusterEntity.setClusterId(1L);
        clusterEntity.setClusterName(clusterName);
        clusterEntity.setDesiredStack(desiredStackEntity);
        clusterEntity.setProvisioningState(org.apache.ambari.server.state.State.INIT);
        clusterEntity.setResource(resourceEntity);
        clusterDAO.create(clusterEntity);
        org.apache.ambari.server.orm.OrmTestHelper ormTestHelper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        ormTestHelper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId(desiredStackEntity.getStackName(), desiredStackEntity.getStackVersion()), repositoryVersion);
        return clusterEntity;
    }

    protected org.apache.ambari.server.orm.entities.ClusterServiceEntity createService(com.google.inject.Injector injector, org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity, java.lang.String serviceName) {
        org.apache.ambari.server.orm.dao.ClusterServiceDAO clusterServiceDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterServiceDAO.class);
        org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity = new org.apache.ambari.server.orm.entities.ClusterServiceEntity();
        clusterServiceEntity.setClusterId(1L);
        clusterServiceEntity.setClusterEntity(clusterEntity);
        clusterServiceEntity.setServiceName(serviceName);
        clusterServiceDAO.create(clusterServiceEntity);
        return clusterServiceEntity;
    }

    protected org.apache.ambari.server.orm.entities.ClusterServiceEntity addService(com.google.inject.Injector injector, org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity, java.lang.String serviceName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion) {
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity = createService(injector, clusterEntity, serviceName);
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity = new org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity();
        serviceDesiredStateEntity.setDesiredRepositoryVersion(desiredRepositoryVersion);
        serviceDesiredStateEntity.setClusterId(1L);
        serviceDesiredStateEntity.setServiceName(serviceName);
        serviceDesiredStateEntity.setClusterServiceEntity(clusterServiceEntity);
        clusterServiceEntity.setServiceDesiredStateEntity(serviceDesiredStateEntity);
        clusterEntity.getClusterServiceEntities().add(clusterServiceEntity);
        clusterDAO.merge(clusterEntity);
        return clusterServiceEntity;
    }

    protected org.apache.ambari.server.orm.entities.HostEntity createHost(com.google.inject.Injector injector, org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity, java.lang.String hostName) {
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity.setHostName(hostName);
        hostEntity.setClusterEntities(java.util.Collections.singletonList(clusterEntity));
        hostDAO.create(hostEntity);
        clusterEntity.getHostEntities().add(hostEntity);
        clusterDAO.merge(clusterEntity);
        return hostEntity;
    }

    @com.google.inject.persist.Transactional
    protected void addComponent(com.google.inject.Injector injector, org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity, org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity, org.apache.ambari.server.orm.entities.HostEntity hostEntity, java.lang.String componentName, org.apache.ambari.server.orm.entities.StackEntity desiredStackEntity, org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion) {
        org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO.class);
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity componentDesiredStateEntity = new org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity();
        componentDesiredStateEntity.setClusterServiceEntity(clusterServiceEntity);
        componentDesiredStateEntity.setComponentName(componentName);
        componentDesiredStateEntity.setServiceName(clusterServiceEntity.getServiceName());
        componentDesiredStateEntity.setClusterServiceEntity(clusterServiceEntity);
        componentDesiredStateEntity.setClusterId(clusterServiceEntity.getClusterId());
        componentDesiredStateEntity.setDesiredRepositoryVersion(desiredRepositoryVersion);
        serviceComponentDesiredStateDAO.create(componentDesiredStateEntity);
        org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO.class);
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity = new org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity();
        hostComponentDesiredStateEntity.setClusterId(clusterEntity.getClusterId());
        hostComponentDesiredStateEntity.setComponentName(componentName);
        hostComponentDesiredStateEntity.setServiceName(clusterServiceEntity.getServiceName());
        hostComponentDesiredStateEntity.setAdminState(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE);
        hostComponentDesiredStateEntity.setServiceComponentDesiredStateEntity(componentDesiredStateEntity);
        hostComponentDesiredStateEntity.setHostEntity(hostEntity);
        hostComponentDesiredStateDAO.create(hostComponentDesiredStateEntity);
        org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentStateEntity = new org.apache.ambari.server.orm.entities.HostComponentStateEntity();
        hostComponentStateEntity.setHostEntity(hostEntity);
        hostComponentStateEntity.setComponentName(componentName);
        hostComponentStateEntity.setServiceName(clusterServiceEntity.getServiceName());
        hostComponentStateEntity.setClusterId(clusterEntity.getClusterId());
        hostComponentStateEntity.setServiceComponentDesiredStateEntity(componentDesiredStateEntity);
        componentDesiredStateEntity.setHostComponentStateEntities(java.util.Collections.singletonList(hostComponentStateEntity));
        componentDesiredStateEntity.setHostComponentDesiredStateEntities(java.util.Collections.singletonList(hostComponentDesiredStateEntity));
        hostEntity.addHostComponentStateEntity(hostComponentStateEntity);
        hostEntity.addHostComponentDesiredStateEntity(hostComponentDesiredStateEntity);
        clusterServiceEntity.getServiceComponentDesiredStateEntities().add(componentDesiredStateEntity);
        org.apache.ambari.server.orm.dao.ClusterServiceDAO clusterServiceDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterServiceDAO.class);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        serviceComponentDesiredStateDAO.merge(componentDesiredStateEntity);
        hostDAO.merge(hostEntity);
        clusterServiceDAO.merge(clusterServiceEntity);
    }
}