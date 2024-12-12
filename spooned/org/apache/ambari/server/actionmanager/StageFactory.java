package org.apache.ambari.server.actionmanager;
import com.google.inject.assistedinject.Assisted;
public interface StageFactory {
    org.apache.ambari.server.actionmanager.Stage createNew(long requestId, @com.google.inject.assistedinject.Assisted("logDir")
    java.lang.String logDir, @com.google.inject.assistedinject.Assisted("clusterName")
    java.lang.String clusterName, @com.google.inject.assistedinject.Assisted("clusterId")
    long clusterId, @com.google.inject.assistedinject.Assisted("requestContext")
    java.lang.String requestContext, @com.google.inject.assistedinject.Assisted("commandParamsStage")
    java.lang.String commandParamsStage, @com.google.inject.assistedinject.Assisted("hostParamsStage")
    java.lang.String hostParamsStage);

    org.apache.ambari.server.actionmanager.Stage createExisting(org.apache.ambari.server.orm.entities.StageEntity stageEntity);
}