package org.apache.ambari.server.state;
public interface ServiceComponentHostFactory {
    org.apache.ambari.server.state.ServiceComponentHost createNew(org.apache.ambari.server.state.ServiceComponent serviceComponent, java.lang.String hostName);

    org.apache.ambari.server.state.ServiceComponentHost createNew(org.apache.ambari.server.state.ServiceComponent serviceComponent, java.lang.String hostName, org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity);

    org.apache.ambari.server.state.ServiceComponentHost createExisting(org.apache.ambari.server.state.ServiceComponent serviceComponent, org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity, org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity);
}