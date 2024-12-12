package org.apache.ambari.server.controller.utilities;
@org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.ORPHAN_KERBEROS_IDENTITY_REMOVAL)
public class RemovableIdentities {
    private final java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> candidateIdentities;

    private final org.apache.ambari.server.controller.utilities.UsedIdentities usedIdentities;

    private final org.apache.ambari.server.state.Cluster cluster;

    private final java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> components;

    public static org.apache.ambari.server.controller.utilities.RemovableIdentities ofService(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.events.ServiceRemovedEvent event, org.apache.ambari.server.controller.KerberosHelper kerberosHelper) throws org.apache.ambari.server.AmbariException {
        if (cluster.getSecurityType() != org.apache.ambari.server.state.SecurityType.KERBEROS) {
            return org.apache.ambari.server.controller.utilities.RemovableIdentities.none();
        }
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = kerberosHelper.getKerberosDescriptor(cluster, false).getService(event.getServiceName());
        if (serviceDescriptor == null) {
            return org.apache.ambari.server.controller.utilities.RemovableIdentities.none();
        }
        org.apache.ambari.server.controller.utilities.UsedIdentities usedIdentities = org.apache.ambari.server.controller.utilities.UsedIdentities.populate(cluster, org.apache.ambari.server.controller.utilities.RemovableIdentities.excludeService(event.getServiceName()), org.apache.ambari.server.controller.utilities.UsedIdentities.ComponentExclude.NONE, kerberosHelper);
        return new org.apache.ambari.server.controller.utilities.RemovableIdentities(serviceDescriptor.getIdentitiesSkipReferences(), usedIdentities, cluster, event.getComponents());
    }

    public static org.apache.ambari.server.controller.utilities.RemovableIdentities ofComponent(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.events.ServiceComponentUninstalledEvent event, org.apache.ambari.server.controller.KerberosHelper kerberosHelper) throws org.apache.ambari.server.AmbariException {
        if (cluster.getSecurityType() != org.apache.ambari.server.state.SecurityType.KERBEROS) {
            return org.apache.ambari.server.controller.utilities.RemovableIdentities.none();
        }
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = kerberosHelper.getKerberosDescriptor(cluster, false).getService(event.getServiceName());
        if (serviceDescriptor == null) {
            return org.apache.ambari.server.controller.utilities.RemovableIdentities.none();
        }
        org.apache.ambari.server.controller.utilities.UsedIdentities usedIdentities = org.apache.ambari.server.controller.utilities.UsedIdentities.populate(cluster, org.apache.ambari.server.controller.utilities.UsedIdentities.ServiceExclude.NONE, org.apache.ambari.server.controller.utilities.RemovableIdentities.excludeComponent(event.getServiceName(), event.getComponentName(), event.getHostName()), kerberosHelper);
        return new org.apache.ambari.server.controller.utilities.RemovableIdentities(org.apache.ambari.server.controller.utilities.RemovableIdentities.componentIdentities(java.util.Collections.singletonList(event.getComponentName()), serviceDescriptor), usedIdentities, cluster, java.util.Collections.singletonList(event.getComponent()));
    }

    public static org.apache.ambari.server.controller.utilities.RemovableIdentities none() throws org.apache.ambari.server.AmbariException {
        return new org.apache.ambari.server.controller.utilities.RemovableIdentities(java.util.Collections.emptyList(), org.apache.ambari.server.controller.utilities.UsedIdentities.none(), null, null);
    }

    private static org.apache.ambari.server.controller.utilities.UsedIdentities.ServiceExclude excludeService(java.lang.String excludedServiceName) {
        return serviceName -> excludedServiceName.equals(serviceName);
    }

    private static org.apache.ambari.server.controller.utilities.UsedIdentities.ComponentExclude excludeComponent(java.lang.String excludedServiceName, java.lang.String excludedComponentName, java.lang.String excludedHostName) {
        return (serviceName, componentName, hosts) -> (excludedServiceName.equals(serviceName) && excludedComponentName.equals(componentName)) && org.apache.ambari.server.controller.utilities.RemovableIdentities.hostNames(hosts).equals(java.util.Collections.singletonList(excludedHostName));
    }

    private static java.util.List<java.lang.String> hostNames(java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> hosts) {
        return hosts.stream().map(org.apache.ambari.server.state.ServiceComponentHost::getHostName).collect(java.util.stream.Collectors.toList());
    }

    private static java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> componentIdentities(java.util.List<java.lang.String> componentNames, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor) throws org.apache.ambari.server.AmbariException {
        return componentNames.stream().map(componentName -> serviceDescriptor.getComponent(componentName)).filter(java.util.Objects::nonNull).flatMap(componentDescriptor -> componentDescriptor.getIdentitiesSkipReferences().stream()).collect(java.util.stream.Collectors.toList());
    }

    private RemovableIdentities(java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> candidateIdentities, org.apache.ambari.server.controller.utilities.UsedIdentities usedIdentities, org.apache.ambari.server.state.Cluster cluster, java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> components) {
        this.candidateIdentities = candidateIdentities;
        this.usedIdentities = usedIdentities;
        this.cluster = cluster;
        this.components = components;
    }

    public void remove(org.apache.ambari.server.controller.KerberosHelper kerberosHelper) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
        kerberosHelper.deleteIdentities(cluster, components, null);
    }

    private java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> skipUsed() throws org.apache.ambari.server.AmbariException {
        return candidateIdentities.stream().filter(each -> !usedIdentities.contains(each)).collect(java.util.stream.Collectors.toList());
    }
}