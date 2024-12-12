package org.apache.ambari.server.actionmanager;
import com.google.inject.assistedinject.Assisted;
public interface RequestFactory {
    org.apache.ambari.server.actionmanager.Request createNew(long requestId, @com.google.inject.assistedinject.Assisted("clusterId")
    java.lang.Long clusterName) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.actionmanager.Request createNewFromStages(java.util.Collection<org.apache.ambari.server.actionmanager.Stage> stages, java.lang.String clusterHostInfo);

    org.apache.ambari.server.actionmanager.Request createNewFromStages(java.util.Collection<org.apache.ambari.server.actionmanager.Stage> stages, java.lang.String clusterHostInfo, org.apache.ambari.server.controller.ExecuteActionRequest actionRequest);

    org.apache.ambari.server.actionmanager.Request createExisting(org.apache.ambari.server.orm.entities.RequestEntity entity);
}