package org.apache.ambari.view.pig;
import javax.ws.rs.Path;
public class PigServiceRouter {
    @com.google.inject.Inject
    org.apache.ambari.view.ViewContext context;

    @com.google.inject.Inject
    protected org.apache.ambari.view.ViewResourceHandler handler;

    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.PigServiceRouter.class);

    private org.apache.ambari.view.pig.persistence.Storage storage = null;

    @javax.ws.rs.Path("/help")
    public org.apache.ambari.view.pig.services.HelpService help() {
        return new org.apache.ambari.view.pig.services.HelpService(context, handler);
    }
}