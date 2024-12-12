package org.apache.ambari.server.state;
public interface ServiceComponentFactory {
    org.apache.ambari.server.state.ServiceComponent createNew(org.apache.ambari.server.state.Service service, java.lang.String componentName);

    org.apache.ambari.server.state.ServiceComponent createExisting(org.apache.ambari.server.state.Service service, org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity);
}