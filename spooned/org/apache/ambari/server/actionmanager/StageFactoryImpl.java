package org.apache.ambari.server.actionmanager;
import com.google.inject.assistedinject.Assisted;
@com.google.inject.Singleton
public class StageFactoryImpl implements org.apache.ambari.server.actionmanager.StageFactory {
    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    public StageFactoryImpl(com.google.inject.Injector injector) {
        this.injector = injector;
    }

    @java.lang.Override
    public org.apache.ambari.server.actionmanager.Stage createNew(long requestId, @com.google.inject.assistedinject.Assisted("logDir")
    java.lang.String logDir, @com.google.inject.assistedinject.Assisted("clusterName")
    java.lang.String clusterName, @com.google.inject.assistedinject.Assisted("clusterId")
    long clusterId, @com.google.inject.assistedinject.Assisted("requestContext")
    java.lang.String requestContext, @com.google.inject.assistedinject.Assisted("commandParamsStage")
    java.lang.String commandParamsStage, @com.google.inject.assistedinject.Assisted("hostParamsStage")
    java.lang.String hostParamsStage) {
        return new org.apache.ambari.server.actionmanager.Stage(requestId, logDir, clusterName, clusterId, requestContext, commandParamsStage, hostParamsStage, injector.getInstance(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class), injector.getInstance(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory.class));
    }

    @java.lang.Override
    public org.apache.ambari.server.actionmanager.Stage createExisting(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.StageEntity stageEntity) {
        return new org.apache.ambari.server.actionmanager.Stage(stageEntity, injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class), injector.getInstance(org.apache.ambari.server.actionmanager.ActionDBAccessor.class), injector.getInstance(org.apache.ambari.server.state.Clusters.class), injector.getInstance(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class), injector.getInstance(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory.class));
    }
}