package org.apache.ambari.server.serveraction.kerberos.stageutils;
import javax.annotation.Nullable;
import org.apache.commons.collections.MapUtils;
@com.google.inject.Singleton
public class KerberosKeytabController {
    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosKeytabDAO kerberosKeytabDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    private static org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    public static void setKerberosHelper(org.apache.ambari.server.controller.KerberosHelper kerberosHelper) {
        org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController.kerberosHelper = kerberosHelper;
    }

    public org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab getKeytabByFile(java.lang.String file) {
        return getKeytabByFile(file, true);
    }

    public org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab getKeytabByFile(java.lang.String file, boolean resolvePrincipals) {
        return fromKeytabEntity(kerberosKeytabDAO.find(file), resolvePrincipals, false);
    }

    public java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> getAllKeytabs() {
        return fromKeytabEntities(kerberosKeytabDAO.findAll(), false);
    }

    public java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> getFromPrincipal(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal rkp) {
        return fromKeytabEntities(kerberosKeytabDAO.findByPrincipalAndHost(rkp.getPrincipal(), rkp.getHostId()), false);
    }

    public java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> getFromPrincipalExceptServiceMapping(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal rkp) {
        return fromKeytabEntities(kerberosKeytabDAO.findByPrincipalAndHost(rkp.getPrincipal(), rkp.getHostId()), true);
    }

    private java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> getFilteredKeytabs(java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter, java.util.Set<java.lang.String> hostFilter, java.util.Collection<java.lang.String> identityFilter) {
        if (((serviceComponentFilter == null) && (hostFilter == null)) && (identityFilter == null)) {
            return getAllKeytabs();
        }
        java.util.List<org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter> filters = splitServiceFilter(serviceComponentFilter);
        for (org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter filter : filters) {
            filter.setHostNames(hostFilter);
            filter.setPrincipals(identityFilter);
        }
        java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal> filteredPrincipals = fromPrincipalEntities(kerberosKeytabPrincipalDAO.findByFilters(filters), false);
        java.util.HashMap<java.lang.String, org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> resultMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal principal : filteredPrincipals) {
            if (!resultMap.containsKey(principal.getKeytabPath())) {
                resultMap.put(principal.getKeytabPath(), getKeytabByFile(principal.getKeytabPath(), false));
            }
            org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab keytab = resultMap.get(principal.getKeytabPath());
            keytab.addPrincipal(principal);
        }
        return com.google.common.collect.Sets.newHashSet(resultMap.values());
    }

    public java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> getFilteredKeytabs(java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> serviceIdentities, java.util.Set<java.lang.String> hostFilter, java.util.Collection<java.lang.String> identityFilters) {
        final java.util.Collection<java.lang.String> enhancedIdentityFilters = populateIdentityFilter(identityFilters, serviceIdentities);
        return getFilteredKeytabs(((java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>>) (null)), hostFilter, enhancedIdentityFilters);
    }

    private java.util.List<org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter> splitServiceFilter(java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter) {
        if ((serviceComponentFilter != null) && (serviceComponentFilter.size() > 0)) {
            java.util.Set<java.lang.String> serviceSet = new java.util.HashSet<>();
            java.util.Set<java.lang.String> componentSet = new java.util.HashSet<>();
            java.util.Set<java.lang.String> serviceOnlySet = new java.util.HashSet<>();
            for (java.util.Map.Entry<java.lang.String, ? extends java.util.Collection<java.lang.String>> entry : serviceComponentFilter.entrySet()) {
                java.lang.String serviceName = entry.getKey();
                java.util.Collection<java.lang.String> serviceComponents = entry.getValue();
                if ((serviceComponents == null) || serviceComponents.contains("*")) {
                    serviceOnlySet.add(serviceName);
                } else {
                    serviceSet.add(serviceName);
                    componentSet.addAll(serviceComponents);
                }
            }
            java.util.List<org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter> result = new java.util.ArrayList<>();
            if (serviceSet.size() > 0) {
                result.add(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter.createFilter(null, serviceSet, componentSet, null));
            }
            if (serviceOnlySet.size() > 0) {
                result.add(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter.createFilter(null, serviceOnlySet, null, null));
            }
            if (result.size() > 0) {
                return result;
            }
        }
        return com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter.createEmptyFilter());
    }

    private org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab fromKeytabEntity(org.apache.ambari.server.orm.entities.KerberosKeytabEntity kke, boolean resolvePrincipals, boolean exceptServiceMapping) {
        java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal> principals = (resolvePrincipals) ? fromPrincipalEntities(kke.getKerberosKeytabPrincipalEntities(), exceptServiceMapping) : new java.util.HashSet<>();
        return new org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab(kke.getKeytabPath(), kke.getOwnerName(), kke.getOwnerAccess(), kke.getGroupName(), kke.getGroupAccess(), principals, kke.isAmbariServerKeytab(), kke.isWriteAmbariJaasFile());
    }

    private org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab fromKeytabEntity(org.apache.ambari.server.orm.entities.KerberosKeytabEntity kke) {
        return fromKeytabEntity(kke, true, false);
    }

    private java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> fromKeytabEntities(java.util.Collection<org.apache.ambari.server.orm.entities.KerberosKeytabEntity> keytabEntities, boolean exceptServiceMapping) {
        com.google.common.collect.ImmutableSet.Builder<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab> builder = com.google.common.collect.ImmutableSet.builder();
        for (org.apache.ambari.server.orm.entities.KerberosKeytabEntity kke : keytabEntities) {
            builder.add(fromKeytabEntity(kke, true, exceptServiceMapping));
        }
        return builder.build();
    }

    private java.util.Set<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal> fromPrincipalEntities(java.util.Collection<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> principalEntities, boolean exceptServiceMapping) {
        com.google.common.collect.ImmutableSet.Builder<org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal> builder = com.google.common.collect.ImmutableSet.builder();
        for (org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kkpe : principalEntities) {
            org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kpe = kkpe.getKerberosPrincipalEntity();
            if (kpe != null) {
                org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal rkp = new org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal(kkpe.getHostId(), kkpe.getHostName(), kkpe.getPrincipalName(), kpe.isService(), kpe.getCachedKeytabPath(), kkpe.getKeytabPath(), exceptServiceMapping ? null : kkpe.getServiceMappingAsMultimap());
                builder.add(rkp);
            }
        }
        return builder.build();
    }

    public java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> adjustServiceComponentFilter(org.apache.ambari.server.state.Cluster cluster, boolean includeAmbariAsService, java.util.Map<java.lang.String, ? extends java.util.Collection<java.lang.String>> serviceComponentFilter) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> adjustedFilter = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> installedServices = (cluster == null) ? null : cluster.getServices();
        if (includeAmbariAsService) {
            installedServices = (installedServices == null) ? new java.util.HashMap<>() : new java.util.HashMap<>(installedServices);
            installedServices.put("AMBARI", null);
        }
        if (!org.apache.commons.collections.MapUtils.isEmpty(installedServices)) {
            if (serviceComponentFilter != null) {
                for (java.util.Map.Entry<java.lang.String, ? extends java.util.Collection<java.lang.String>> entry : serviceComponentFilter.entrySet()) {
                    java.lang.String serviceName = entry.getKey();
                    if (installedServices.containsKey(serviceName)) {
                        adjustedFilter.put(serviceName, entry.getValue());
                    }
                }
            } else {
                for (java.lang.String serviceName : installedServices.keySet()) {
                    adjustedFilter.put(serviceName, java.util.Collections.singletonList("*"));
                }
            }
        }
        return adjustedFilter;
    }

    public java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> getServiceIdentities(java.lang.String clusterName, java.util.Collection<java.lang.String> services, @javax.annotation.Nullable
    java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        final java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> serviceIdentities = new java.util.ArrayList<>();
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        if (desiredConfigs == null) {
            desiredConfigs = cluster.getDesiredConfigs();
        }
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController.kerberosHelper.getKerberosDescriptor(cluster, false);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor = org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController.kerberosHelper.getKerberosDescriptorUpdates(cluster);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> hostConfigurations = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hostMap = clusters.getHostsForCluster(clusterName);
        java.util.Set<java.lang.String> hosts = new java.util.HashSet<>(hostMap.keySet());
        java.util.Map<java.lang.String, java.lang.String> componentHosts = new java.util.HashMap<>();
        for (java.lang.String hostName : hosts) {
            hostConfigurations.put(hostName, org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController.kerberosHelper.calculateConfigurations(cluster, hostName, kerberosDescriptor, userDescriptor, false, false, componentHosts, desiredConfigs));
        }
        java.lang.String ambariServerHostname = org.apache.ambari.server.utils.StageUtils.getHostName();
        hosts.add(ambariServerHostname);
        for (java.lang.String service : services) {
            org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController.kerberosHelper.getActiveIdentities(clusterName, null, service, null, true, hostConfigurations, kerberosDescriptor, desiredConfigs).values().forEach(serviceIdentities::addAll);
        }
        return serviceIdentities;
    }

    private java.util.Collection<java.lang.String> populateIdentityFilter(java.util.Collection<java.lang.String> identityFilters, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> serviceIdentities) {
        if (serviceIdentities != null) {
            identityFilters = (identityFilters == null) ? new java.util.HashSet<>() : identityFilters;
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor serviceIdentity : serviceIdentities) {
                if (!org.apache.ambari.server.controller.KerberosHelper.AMBARI_SERVER_KERBEROS_IDENTITY_NAME.equals(serviceIdentity.getName())) {
                    identityFilters.add(serviceIdentity.getPrincipalDescriptor().getName());
                }
            }
        }
        return identityFilters;
    }
}