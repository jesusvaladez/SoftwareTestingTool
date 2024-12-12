package org.apache.ambari.server.controller.utilities;
@com.google.inject.Singleton
@org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.ORPHAN_KERBEROS_IDENTITY_REMOVAL, comment = "This might need to have a switch so that it can be turned off if it is found to be desctructive to certain clusters")
public class KerberosIdentityCleaner {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.utilities.KerberosIdentityCleaner.class);

    private final org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    private final org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    private final org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    public KerberosIdentityCleaner(org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher, org.apache.ambari.server.controller.KerberosHelper kerberosHelper, org.apache.ambari.server.state.Clusters clusters) {
        this.eventPublisher = eventPublisher;
        this.kerberosHelper = kerberosHelper;
        this.clusters = clusters;
    }

    public void register() {
        eventPublisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    public void componentRemoved(org.apache.ambari.server.events.ServiceComponentUninstalledEvent event) throws org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException {
        try {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(event.getClusterId());
            if (cluster.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS) {
                if (null != cluster.getUpgradeInProgress()) {
                    org.apache.ambari.server.controller.utilities.KerberosIdentityCleaner.LOG.info("Skipping removal of identities for {} since there is an upgrade in progress", event.getComponentName());
                    return;
                }
                org.apache.ambari.server.controller.utilities.KerberosIdentityCleaner.LOG.info("Removing identities after {}", event);
                org.apache.ambari.server.controller.utilities.RemovableIdentities.ofComponent(clusters.getCluster(event.getClusterId()), event, kerberosHelper).remove(kerberosHelper);
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.utilities.KerberosIdentityCleaner.LOG.error("Error while deleting kerberos identity after an event: " + event, e);
        }
    }

    @com.google.common.eventbus.Subscribe
    public void serviceRemoved(org.apache.ambari.server.events.ServiceRemovedEvent event) {
        try {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(event.getClusterId());
            if (cluster.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS) {
                if (null != cluster.getUpgradeInProgress()) {
                    org.apache.ambari.server.controller.utilities.KerberosIdentityCleaner.LOG.info("Skipping removal of identities for {} since there is an upgrade in progress", event.getServiceName());
                    return;
                }
                org.apache.ambari.server.controller.utilities.KerberosIdentityCleaner.LOG.info("Removing identities after {}", event);
                org.apache.ambari.server.controller.utilities.RemovableIdentities.ofService(clusters.getCluster(event.getClusterId()), event, kerberosHelper).remove(kerberosHelper);
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.utilities.KerberosIdentityCleaner.LOG.error("Error while deleting kerberos identity after an event: " + event, e);
        }
    }
}