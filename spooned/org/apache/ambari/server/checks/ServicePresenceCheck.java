package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT)
public class ServicePresenceCheck extends org.apache.ambari.server.checks.ClusterCheck {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.checks.ServicePresenceCheck.class);

    static final java.lang.String KEY_SERVICE_REPLACED = "service_replaced";

    static final java.lang.String KEY_SERVICE_REMOVED = "service_removed";

    static final java.lang.String NO_UPGRADE_SUPPORT_SERVICES_PROPERTY_NAME = "no-upgrade-support-service-names";

    static final java.lang.String REMOVED_SERVICES_PROPERTY_NAME = "removed-service-names";

    static final java.lang.String REPLACED_SERVICES_PROPERTY_NAME = "replaced-service-names";

    static final java.lang.String NEW_SERVICES_PROPERTY_NAME = "new-service-names";

    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription SERVICE_PRESENCE_CHECK = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("SERVICE_PRESENCE_CHECK", org.apache.ambari.spi.upgrade.UpgradeCheckType.SERVICE, "Service Is Not Supported For Upgrades", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "The %s service is currently installed on the cluster. " + ("This service does not support upgrades and must be removed before the upgrade can continue. " + "After upgrading, %s can be reinstalled")).put(org.apache.ambari.server.checks.ServicePresenceCheck.KEY_SERVICE_REMOVED, "The %s service is currently installed on the cluster. " + "This service is removed from the new release and must be removed before the upgrade can continue.").build());

    public ServicePresenceCheck() {
        super(org.apache.ambari.server.checks.ServicePresenceCheck.SERVICE_PRESENCE_CHECK);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(request.getClusterName());
        java.util.Set<java.lang.String> installedServices = cluster.getServices().keySet();
        java.util.List<java.lang.String> noUpgradeSupportServices = getNoUpgradeSupportServices(request);
        java.util.Map<java.lang.String, java.lang.String> replacedServices = getReplacedServices(request);
        java.util.List<java.lang.String> removedServices = getRemovedServices(request);
        java.util.List<java.lang.String> failReasons = new java.util.ArrayList<>();
        java.lang.String reason = getFailReason(result, request);
        for (java.lang.String service : noUpgradeSupportServices) {
            if (installedServices.contains(service.toUpperCase())) {
                result.getFailedOn().add(service);
                java.lang.String msg = java.lang.String.format(reason, service, service);
                failReasons.add(msg);
            }
        }
        reason = getFailReason(org.apache.ambari.server.checks.ServicePresenceCheck.KEY_SERVICE_REPLACED, result, request);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : replacedServices.entrySet()) {
            java.lang.String removedService = entry.getKey();
            if (installedServices.contains(removedService.toUpperCase())) {
                result.getFailedOn().add(removedService);
                java.lang.String newService = entry.getValue();
                java.lang.String msg = java.lang.String.format(reason, removedService, newService);
                failReasons.add(msg);
            }
        }
        reason = getFailReason(org.apache.ambari.server.checks.ServicePresenceCheck.KEY_SERVICE_REMOVED, result, request);
        for (java.lang.String service : removedServices) {
            if (installedServices.contains(service.toUpperCase())) {
                result.getFailedOn().add(service);
                java.lang.String msg = java.lang.String.format(reason, service, service);
                failReasons.add(msg);
            }
        }
        if (!failReasons.isEmpty()) {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.setFailReason(org.apache.commons.lang.StringUtils.join(failReasons, '\n'));
        }
        return result;
    }

    private java.lang.String getPropertyValue(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request, java.lang.String propertyKey) {
        java.lang.String value = null;
        java.util.Map<java.lang.String, java.lang.String> checkProperties = request.getCheckConfigurations();
        if ((checkProperties != null) && checkProperties.containsKey(propertyKey)) {
            value = checkProperties.get(propertyKey);
        }
        return value;
    }

    private java.util.List<java.lang.String> getNoUpgradeSupportServices(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) {
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        java.lang.String value = getPropertyValue(request, org.apache.ambari.server.checks.ServicePresenceCheck.NO_UPGRADE_SUPPORT_SERVICES_PROPERTY_NAME);
        if (null != value) {
            java.lang.String[] services = value.split(",");
            for (java.lang.String service : services) {
                service = service.trim();
                if (!service.isEmpty()) {
                    result.add(service);
                }
            }
        }
        return result;
    }

    private java.util.List<java.lang.String> getRemovedServices(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) {
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        java.lang.String value = getPropertyValue(request, org.apache.ambari.server.checks.ServicePresenceCheck.REMOVED_SERVICES_PROPERTY_NAME);
        if (null != value) {
            java.lang.String[] services = value.split(",");
            for (java.lang.String service : services) {
                service = service.trim();
                if (!service.isEmpty()) {
                    result.add(service);
                }
            }
        }
        return result;
    }

    private java.util.Map<java.lang.String, java.lang.String> getReplacedServices(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.lang.String> result = new java.util.LinkedHashMap<>();
        java.lang.String value = getPropertyValue(request, org.apache.ambari.server.checks.ServicePresenceCheck.REPLACED_SERVICES_PROPERTY_NAME);
        java.lang.String newValue = getPropertyValue(request, org.apache.ambari.server.checks.ServicePresenceCheck.NEW_SERVICES_PROPERTY_NAME);
        if ((value == null) && (newValue == null)) {
            return result;
        } else if ((value == null) || (newValue == null)) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Both %s and %s list must be specified in the upgrade XML file.", org.apache.ambari.server.checks.ServicePresenceCheck.REPLACED_SERVICES_PROPERTY_NAME, org.apache.ambari.server.checks.ServicePresenceCheck.NEW_SERVICES_PROPERTY_NAME));
        } else {
            java.util.List<java.lang.String> oldServices = java.util.Arrays.asList(value.split(","));
            java.util.List<java.lang.String> newServices = java.util.Arrays.asList(newValue.split(","));
            if (oldServices.size() != newServices.size()) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("%s must have the same number of services as the %s list.", org.apache.ambari.server.checks.ServicePresenceCheck.NEW_SERVICES_PROPERTY_NAME, org.apache.ambari.server.checks.ServicePresenceCheck.REPLACED_SERVICES_PROPERTY_NAME));
            } else {
                for (int i = 0; i < oldServices.size(); i++) {
                    java.lang.String oldService = oldServices.get(i).trim();
                    java.lang.String newService = newServices.get(i).trim();
                    if (oldService.isEmpty() || newService.isEmpty()) {
                        throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Make sure both %s and %s list only contain comma separated list of services.", org.apache.ambari.server.checks.ServicePresenceCheck.NEW_SERVICES_PROPERTY_NAME, org.apache.ambari.server.checks.ServicePresenceCheck.REPLACED_SERVICES_PROPERTY_NAME));
                    } else {
                        result.put(oldService, newService);
                    }
                }
            }
        }
        return result;
    }
}