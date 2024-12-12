package org.apache.ambari.view.capacityscheduler;
import javax.ws.rs.Path;
public class CapacitySchedulerService {
    @com.google.inject.Inject
    org.apache.ambari.view.ViewContext context;

    @javax.ws.rs.Path("/help")
    public org.apache.ambari.view.capacityscheduler.HelpService help() {
        return new org.apache.ambari.view.capacityscheduler.HelpService(context);
    }

    @javax.ws.rs.Path("/configuration")
    public org.apache.ambari.view.capacityscheduler.ConfigurationService configuration() {
        return new org.apache.ambari.view.capacityscheduler.ConfigurationService(context);
    }
}