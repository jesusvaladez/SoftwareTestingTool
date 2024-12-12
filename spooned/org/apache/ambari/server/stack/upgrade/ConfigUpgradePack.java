package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@javax.xml.bind.annotation.XmlRootElement(name = "upgrade-config-changes")
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class ConfigUpgradePack {
    @javax.xml.bind.annotation.XmlElementWrapper(name = "services")
    @javax.xml.bind.annotation.XmlElement(name = "service")
    public java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedService> services;

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition> changesById;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.class);

    public ConfigUpgradePack() {
    }

    public ConfigUpgradePack(java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedService> services) {
        this.services = services;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedService> getServiceMap() {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedService> result = new java.util.HashMap<>();
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedService service : services) {
            result.put(service.name, service);
        }
        return result;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition> enumerateConfigChangesByID() {
        if (changesById == null) {
            changesById = new java.util.HashMap<>();
            for (org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedService service : services) {
                for (org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent component : service.components) {
                    for (org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition changeDefinition : component.changes) {
                        if (changeDefinition.id == null) {
                            org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.LOG.warn(java.lang.String.format("Config upgrade change definition for service %s," + " component %s has no id", service.name, component.name));
                        } else if (changesById.containsKey(changeDefinition.id)) {
                            org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.LOG.warn("Duplicate config upgrade change definition with ID " + changeDefinition.id);
                        }
                        changesById.put(changeDefinition.id, changeDefinition);
                    }
                }
            }
        }
        return changesById;
    }

    public static org.apache.ambari.server.stack.upgrade.ConfigUpgradePack merge(java.util.ArrayList<org.apache.ambari.server.stack.upgrade.ConfigUpgradePack> cups) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent>> mergedServiceMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configUpgradePack : cups) {
            for (org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedService service : configUpgradePack.services) {
                if (!mergedServiceMap.containsKey(service.name)) {
                    mergedServiceMap.put(service.name, new java.util.HashMap<>());
                }
                java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent> mergedComponentMap = mergedServiceMap.get(service.name);
                for (org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent component : service.components) {
                    if (!mergedComponentMap.containsKey(component.name)) {
                        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent mergedComponent = new org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent();
                        mergedComponent.name = component.name;
                        mergedComponent.changes = new java.util.ArrayList<>();
                        mergedComponentMap.put(component.name, mergedComponent);
                    }
                    org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent mergedComponent = mergedComponentMap.get(component.name);
                    mergedComponent.changes.addAll(component.changes);
                }
            }
        }
        java.util.ArrayList<org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedService> mergedServices = new java.util.ArrayList<>();
        for (java.lang.String serviceName : mergedServiceMap.keySet()) {
            org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedService mergedService = new org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedService();
            java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent> mergedComponentMap = mergedServiceMap.get(serviceName);
            mergedService.name = serviceName;
            mergedService.components = new java.util.ArrayList<>(mergedComponentMap.values());
            mergedServices.add(mergedService);
        }
        return new org.apache.ambari.server.stack.upgrade.ConfigUpgradePack(mergedServices);
    }

    public static class AffectedService {
        @javax.xml.bind.annotation.XmlAttribute
        public java.lang.String name;

        @javax.xml.bind.annotation.XmlElement(name = "component")
        public java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent> components;

        public java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent> getComponentMap() {
            java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent> result = new java.util.HashMap<>();
            for (org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.AffectedComponent component : components) {
                result.put(component.name, component);
            }
            return result;
        }
    }

    public static class AffectedComponent {
        @javax.xml.bind.annotation.XmlAttribute
        public java.lang.String name;

        @javax.xml.bind.annotation.XmlElementWrapper(name = "changes")
        @javax.xml.bind.annotation.XmlElement(name = "definition")
        public java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition> changes;
    }
}