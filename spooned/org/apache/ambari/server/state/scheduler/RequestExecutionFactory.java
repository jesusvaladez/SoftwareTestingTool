package org.apache.ambari.server.state.scheduler;
import com.google.inject.assistedinject.Assisted;
public interface RequestExecutionFactory {
    org.apache.ambari.server.state.scheduler.RequestExecution createNew(@com.google.inject.assistedinject.Assisted("cluster")
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted("batch")
    org.apache.ambari.server.state.scheduler.Batch batch, @com.google.inject.assistedinject.Assisted("schedule")
    org.apache.ambari.server.state.scheduler.Schedule schedule);

    org.apache.ambari.server.state.scheduler.RequestExecution createExisting(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.RequestScheduleEntity requestScheduleEntity);
}