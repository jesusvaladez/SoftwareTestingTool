package org.apache.ambari.server.controller.utilities;
public class UsedIdentities {
    private final java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> used;

    public static org.apache.ambari.server.controller.utilities.UsedIdentities none() throws org.apache.ambari.server.AmbariException {
        return new org.apache.ambari.server.controller.utilities.UsedIdentities(java.util.Collections.emptyList());
    }

    public static org.apache.ambari.server.controller.utilities.UsedIdentities populate(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.controller.utilities.UsedIdentities.ServiceExclude serviceExclude, org.apache.ambari.server.controller.utilities.UsedIdentities.ComponentExclude componentExclude, org.apache.ambari.server.controller.KerberosHelper kerberosHelper) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> result = new java.util.ArrayList<>();
        org.apache.ambari.server.state.kerberos.KerberosDescriptor root = kerberosHelper.getKerberosDescriptor(cluster, false);
        result.addAll(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.nullToEmpty(root.getIdentities()));
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            if (serviceExclude.shouldExclude(service.getName())) {
                continue;
            }
            org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = root.getService(service.getName());
            if (serviceDescriptor != null) {
                result.addAll(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.nullToEmpty(serviceDescriptor.getIdentities()));
                result.addAll(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor.nullToEmpty(org.apache.ambari.server.controller.utilities.UsedIdentities.componentIdentities(serviceDescriptor, service, componentExclude)));
            }
        }
        return new org.apache.ambari.server.controller.utilities.UsedIdentities(result);
    }

    private static java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> componentIdentities(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor, org.apache.ambari.server.state.Service service, org.apache.ambari.server.controller.utilities.UsedIdentities.ComponentExclude componentExclude) {
        return service.getServiceComponents().values().stream().filter(component -> !org.apache.ambari.server.controller.utilities.UsedIdentities.isComponentExcluded(service, componentExclude, component)).flatMap(component -> serviceDescriptor.getComponentIdentities(component.getName()).stream()).collect(java.util.stream.Collectors.toList());
    }

    private static boolean isComponentExcluded(org.apache.ambari.server.state.Service service, org.apache.ambari.server.controller.utilities.UsedIdentities.ComponentExclude componentExclude, org.apache.ambari.server.state.ServiceComponent component) {
        return component.getServiceComponentHosts().isEmpty() || componentExclude.shouldExclude(service.getName(), component.getName(), component.getServiceComponentHosts().values());
    }

    private UsedIdentities(java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> used) {
        this.used = used;
    }

    public boolean contains(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity) {
        return used.stream().anyMatch(each -> identity.isShared(each));
    }

    public interface ServiceExclude {
        boolean shouldExclude(java.lang.String serviceName);

        org.apache.ambari.server.controller.utilities.UsedIdentities.ServiceExclude NONE = serviceName -> false;
    }

    public interface ComponentExclude {
        boolean shouldExclude(java.lang.String serviceName, java.lang.String componentName, java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> hosts);

        org.apache.ambari.server.controller.utilities.UsedIdentities.ComponentExclude NONE = (serviceName, componentName, hosts) -> false;
    }
}