package org.apache.ambari.server.state.repository;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.StringUtils;
@javax.xml.bind.annotation.XmlRootElement(name = "repository-version")
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class VersionDefinitionXml {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.repository.VersionDefinitionXml.class);

    public static final java.lang.String SCHEMA_LOCATION = "version_definition.xsd";

    @javax.xml.bind.annotation.XmlElement(name = "release")
    public org.apache.ambari.server.state.repository.Release release;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "manifest")
    @javax.xml.bind.annotation.XmlElement(name = "service")
    java.util.List<org.apache.ambari.server.state.repository.ManifestService> manifestServices = new java.util.ArrayList<>();

    @javax.xml.bind.annotation.XmlElementWrapper(name = "available-services")
    @javax.xml.bind.annotation.XmlElement(name = "service")
    java.util.List<org.apache.ambari.server.state.repository.AvailableServiceReference> availableServices = new java.util.ArrayList<>();

    @javax.xml.bind.annotation.XmlElement(name = "repository-info")
    public org.apache.ambari.server.state.stack.RepositoryXml repositoryInfo;

    @javax.xml.bind.annotation.XmlTransient
    public java.lang.String xsdLocation;

    @javax.xml.bind.annotation.XmlTransient
    private java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.AvailableService> m_availableMap;

    @javax.xml.bind.annotation.XmlTransient
    private java.util.List<org.apache.ambari.server.state.repository.ManifestServiceInfo> m_manifest = null;

    @javax.xml.bind.annotation.XmlTransient
    private boolean m_stackDefault = false;

    @javax.xml.bind.annotation.XmlTransient
    private java.util.Map<java.lang.String, java.lang.String> m_packageVersions = null;

    public synchronized java.util.Collection<org.apache.ambari.server.state.repository.AvailableService> getAvailableServices(org.apache.ambari.server.state.StackInfo stack) {
        if (null == m_availableMap) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.ManifestService> manifests = buildManifest();
            m_availableMap = new java.util.HashMap<>();
            if (availableServices.isEmpty()) {
                for (org.apache.ambari.server.state.repository.ManifestService ms : manifests.values()) {
                    addToAvailable(ms, stack, java.util.Collections.emptySet());
                }
            } else {
                for (org.apache.ambari.server.state.repository.AvailableServiceReference ref : availableServices) {
                    org.apache.ambari.server.state.repository.ManifestService ms = manifests.get(ref.serviceIdReference);
                    addToAvailable(ms, stack, ref.components);
                }
            }
        }
        return m_availableMap.values();
    }

    private java.util.Set<java.lang.String> getAvailableServiceNames() {
        if (availableServices.isEmpty()) {
            return java.util.Collections.emptySet();
        } else {
            java.util.Set<java.lang.String> serviceNames = new java.util.HashSet<>();
            java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.ManifestService> manifest = buildManifest();
            for (org.apache.ambari.server.state.repository.AvailableServiceReference ref : availableServices) {
                org.apache.ambari.server.state.repository.ManifestService ms = manifest.get(ref.serviceIdReference);
                serviceNames.add(ms.serviceName);
            }
            return serviceNames;
        }
    }

    public void setStackDefault(boolean stackDefault) {
        m_stackDefault = stackDefault;
    }

    public boolean isStackDefault() {
        return m_stackDefault;
    }

    public synchronized java.util.List<org.apache.ambari.server.state.repository.ManifestServiceInfo> getStackServices(org.apache.ambari.server.state.StackInfo stack) {
        if (null != m_manifest) {
            return m_manifest;
        }
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> manifestVersions = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.repository.ManifestService manifest : manifestServices) {
            java.lang.String name = manifest.serviceName;
            if (!manifestVersions.containsKey(name)) {
                manifestVersions.put(manifest.serviceName, new java.util.TreeSet<>());
            }
            manifestVersions.get(manifest.serviceName).add(manifest.version);
        }
        m_manifest = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.ServiceInfo si : stack.getServices()) {
            java.util.Set<java.lang.String> versions = (manifestVersions.containsKey(si.getName())) ? manifestVersions.get(si.getName()) : java.util.Collections.singleton(null == si.getVersion() ? "" : si.getVersion());
            m_manifest.add(new org.apache.ambari.server.state.repository.ManifestServiceInfo(si.getName(), si.getDisplayName(), si.getComment(), versions));
        }
        return m_manifest;
    }

    public java.lang.String getPackageVersion(java.lang.String osFamily) {
        if (null == m_packageVersions) {
            m_packageVersions = new java.util.HashMap<>();
            for (org.apache.ambari.server.state.stack.RepositoryXml.Os os : repositoryInfo.getOses()) {
                m_packageVersions.put(os.getFamily(), os.getPackageVersion());
            }
        }
        return m_packageVersions.get(osFamily);
    }

    public java.lang.String toXml() throws java.lang.Exception {
        javax.xml.bind.JAXBContext ctx = javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.state.repository.VersionDefinitionXml.class);
        javax.xml.bind.Marshaller marshaller = ctx.createMarshaller();
        javax.xml.validation.SchemaFactory factory = javax.xml.validation.SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        java.io.InputStream xsdStream = org.apache.ambari.server.state.repository.VersionDefinitionXml.class.getClassLoader().getResourceAsStream(xsdLocation);
        if (null == xsdStream) {
            throw new java.lang.Exception(java.lang.String.format("Could not load XSD identified by '%s'", xsdLocation));
        }
        try {
            javax.xml.validation.Schema schema = factory.newSchema(new javax.xml.transform.stream.StreamSource(xsdStream));
            marshaller.setSchema(schema);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, java.lang.Boolean.TRUE);
            marshaller.setProperty("jaxb.noNamespaceSchemaLocation", xsdLocation);
            java.io.StringWriter w = new java.io.StringWriter();
            marshaller.marshal(this, w);
            return w.toString();
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(xsdStream);
        }
    }

    public org.apache.ambari.server.state.repository.ClusterVersionSummary getClusterSummary(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.ManifestService> manifests = buildManifestByService();
        java.util.Set<java.lang.String> available = getAvailableServiceNames();
        available = (available.isEmpty()) ? manifests.keySet() : available;
        java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.ServiceVersionSummary> summaries = new java.util.HashMap<>();
        for (java.lang.String serviceName : available) {
            org.apache.ambari.server.state.Service service = cluster.getServices().get(serviceName);
            if (null == service) {
                continue;
            }
            org.apache.ambari.server.state.repository.ServiceVersionSummary summary = new org.apache.ambari.server.state.repository.ServiceVersionSummary();
            summaries.put(service.getName(), summary);
            org.apache.ambari.server.state.StackId stackId = service.getDesiredRepositoryVersion().getStackId();
            org.apache.ambari.server.state.StackInfo stack = metaInfo.getStack(stackId);
            org.apache.ambari.spi.stack.StackReleaseVersion stackReleaseVersion = stack.getReleaseVersion();
            org.apache.ambari.spi.stack.StackReleaseInfo serviceVersion = stackReleaseVersion.parse(service.getDesiredRepositoryVersion().getVersion());
            org.apache.ambari.server.state.repository.ManifestService manifest = manifests.get(serviceName);
            final org.apache.ambari.spi.stack.StackReleaseInfo versionToCompare;
            final java.lang.String summaryReleaseVersion;
            if (org.apache.commons.lang.StringUtils.isEmpty(manifest.releaseVersion)) {
                versionToCompare = release.getReleaseInfo();
                summaryReleaseVersion = release.version;
            } else {
                versionToCompare = stackReleaseVersion.parse(manifest.releaseVersion);
                summaryReleaseVersion = manifest.releaseVersion;
            }
            summary.setVersions(manifest.version, summaryReleaseVersion);
            if (org.apache.ambari.spi.RepositoryType.STANDARD == release.repositoryType) {
                summary.setUpgrade(true);
            } else {
                java.util.Comparator<org.apache.ambari.spi.stack.StackReleaseInfo> comparator = stackReleaseVersion.getComparator();
                if (comparator.compare(versionToCompare, serviceVersion) > 0) {
                    summary.setUpgrade(true);
                }
            }
        }
        return new org.apache.ambari.server.state.repository.ClusterVersionSummary(summaries);
    }

    public java.util.Set<java.lang.String> getMissingDependencies(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> missingDependencies = com.google.common.collect.Sets.newTreeSet();
        java.lang.String stackPackagesJson = cluster.getClusterProperty(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_STACK_PACKAGES_PROPERTY, null);
        if (org.apache.commons.lang.StringUtils.isEmpty(stackPackagesJson)) {
            return missingDependencies;
        }
        com.google.gson.GsonBuilder gsonBuilder = new com.google.gson.GsonBuilder();
        gsonBuilder.registerTypeAdapter(org.apache.ambari.server.state.repository.StackPackage.UpgradeDependencies.class, new org.apache.ambari.server.state.repository.StackPackage.UpgradeDependencyDeserializer());
        com.google.gson.Gson gson = gsonBuilder.create();
        java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.StackPackage>>() {}.getType();
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.StackPackage> stackPackages;
        try {
            stackPackages = gson.fromJson(stackPackagesJson, type);
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.state.repository.VersionDefinitionXml.LOG.warn("Unable to deserialize the stack packages JSON, assuming no service dependencies", exception);
            return missingDependencies;
        }
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(release.stackId);
        org.apache.ambari.server.state.repository.StackPackage stackPackage = stackPackages.get(stackId.getStackName());
        if ((null == stackPackage) || (null == stackPackage.upgradeDependencies)) {
            return missingDependencies;
        }
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> dependencies = stackPackage.upgradeDependencies.dependencies;
        if ((null == dependencies) || dependencies.isEmpty()) {
            return missingDependencies;
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> installedServices = cluster.getServices();
        org.apache.ambari.server.state.repository.ClusterVersionSummary clusterVersionSummary = getClusterSummary(cluster, metaInfo);
        java.util.Set<java.lang.String> servicesInUpgrade = clusterVersionSummary.getAvailableServiceNames();
        java.util.Set<java.lang.String> servicesInRepository = getAvailableServiceNames();
        for (java.lang.String serviceInUpgrade : servicesInUpgrade) {
            java.util.List<java.lang.String> servicesRequired = dependencies.get(serviceInUpgrade);
            if (null == servicesRequired) {
                continue;
            }
            for (java.lang.String serviceRequired : servicesRequired) {
                if ((!servicesInRepository.contains(serviceRequired)) && installedServices.containsKey(serviceRequired)) {
                    missingDependencies.add(serviceRequired);
                }
            }
        }
        missingDependencies = getRecursiveDependencies(missingDependencies, dependencies, servicesInUpgrade, installedServices.keySet());
        return missingDependencies;
    }

    java.util.Set<java.lang.String> getRecursiveDependencies(java.util.Set<java.lang.String> missingDependencies, java.util.Map<java.lang.String, java.util.List<java.lang.String>> dependencies, java.util.Set<java.lang.String> servicesInUpgrade, java.util.Set<java.lang.String> installedServices) {
        java.util.Set<java.lang.String> results = com.google.common.collect.Sets.newHashSet();
        results.addAll(missingDependencies);
        for (java.lang.String missingDependency : missingDependencies) {
            if (dependencies.containsKey(missingDependency)) {
                java.util.List<java.lang.String> subDependencies = dependencies.get(missingDependency);
                for (java.lang.String subDependency : subDependencies) {
                    if (((!missingDependencies.contains(subDependency)) && installedServices.contains(subDependency)) && (!servicesInUpgrade.contains(subDependency))) {
                        results.add(subDependency);
                        results.addAll(getRecursiveDependencies(results, dependencies, servicesInUpgrade, installedServices));
                    }
                }
            }
        }
        return results;
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.ManifestService> buildManifestByService() {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.ManifestService> manifests = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.repository.ManifestService manifest : manifestServices) {
            if (!manifests.containsKey(manifest.serviceName)) {
                manifests.put(manifest.serviceName, manifest);
            }
        }
        return manifests;
    }

    private void addToAvailable(org.apache.ambari.server.state.repository.ManifestService manifestService, org.apache.ambari.server.state.StackInfo stack, java.util.Set<java.lang.String> components) {
        org.apache.ambari.server.state.ServiceInfo service = stack.getService(manifestService.serviceName);
        if (!m_availableMap.containsKey(manifestService.serviceName)) {
            java.lang.String display = (null == service) ? manifestService.serviceName : service.getDisplayName();
            org.apache.ambari.server.state.repository.AvailableService available = new org.apache.ambari.server.state.repository.AvailableService(manifestService.serviceName, display);
            m_availableMap.put(manifestService.serviceName, available);
        }
        org.apache.ambari.server.state.repository.AvailableService as = m_availableMap.get(manifestService.serviceName);
        as.getVersions().add(new org.apache.ambari.server.state.repository.AvailableVersion(manifestService.version, manifestService.versionId, manifestService.releaseVersion, buildComponents(service, components)));
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.ManifestService> buildManifest() {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.ManifestService> normalized = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.repository.ManifestService ms : manifestServices) {
            normalized.put(ms.serviceId, ms);
        }
        return normalized;
    }

    private java.util.Set<org.apache.ambari.server.state.repository.AvailableVersion.Component> buildComponents(org.apache.ambari.server.state.ServiceInfo service, java.util.Set<java.lang.String> components) {
        java.util.Set<org.apache.ambari.server.state.repository.AvailableVersion.Component> set = new java.util.HashSet<>();
        for (java.lang.String component : components) {
            org.apache.ambari.server.state.ComponentInfo ci = service.getComponentByName(component);
            java.lang.String display = (null == ci) ? component : ci.getDisplayName();
            set.add(new org.apache.ambari.server.state.repository.AvailableVersion.Component(component, display));
        }
        return set;
    }

    public static org.apache.ambari.server.state.repository.VersionDefinitionXml load(java.net.URL url) throws java.lang.Exception {
        java.io.InputStream stream = null;
        try {
            stream = url.openStream();
            return org.apache.ambari.server.state.repository.VersionDefinitionXml.load(stream);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(stream);
        }
    }

    public static org.apache.ambari.server.state.repository.VersionDefinitionXml load(java.lang.String xml) throws java.lang.Exception {
        return org.apache.ambari.server.state.repository.VersionDefinitionXml.load(new java.io.ByteArrayInputStream(xml.getBytes("UTF-8")));
    }

    private static org.apache.ambari.server.state.repository.VersionDefinitionXml load(java.io.InputStream stream) throws java.lang.Exception {
        javax.xml.stream.XMLInputFactory xmlFactory = javax.xml.stream.XMLInputFactory.newInstance();
        javax.xml.stream.XMLStreamReader xmlReader = xmlFactory.createXMLStreamReader(stream);
        xmlReader.nextTag();
        java.lang.String xsdName = xmlReader.getAttributeValue(javax.xml.XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "noNamespaceSchemaLocation");
        if (null == xsdName) {
            throw new java.lang.IllegalArgumentException("Provided XML does not have a Schema defined using 'noNamespaceSchemaLocation'");
        }
        java.io.InputStream xsdStream = org.apache.ambari.server.state.repository.VersionDefinitionXml.class.getClassLoader().getResourceAsStream(xsdName);
        if (null == xsdStream) {
            throw new java.lang.Exception(java.lang.String.format("Could not load XSD identified by '%s'", xsdName));
        }
        javax.xml.bind.JAXBContext ctx = javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.state.repository.VersionDefinitionXml.class);
        javax.xml.bind.Unmarshaller unmarshaller = ctx.createUnmarshaller();
        javax.xml.validation.SchemaFactory factory = javax.xml.validation.SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        javax.xml.validation.Schema schema = factory.newSchema(new javax.xml.transform.stream.StreamSource(xsdStream));
        unmarshaller.setSchema(schema);
        try {
            org.apache.ambari.server.state.repository.VersionDefinitionXml xml = ((org.apache.ambari.server.state.repository.VersionDefinitionXml) (unmarshaller.unmarshal(xmlReader)));
            xml.xsdLocation = xsdName;
            return xml;
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(xsdStream);
        }
    }

    public static org.apache.ambari.server.state.repository.VersionDefinitionXml build(org.apache.ambari.server.state.StackInfo stackInfo) {
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = new org.apache.ambari.server.state.repository.VersionDefinitionXml();
        xml.m_stackDefault = true;
        xml.release = new org.apache.ambari.server.state.repository.Release();
        xml.repositoryInfo = new org.apache.ambari.server.state.stack.RepositoryXml();
        xml.xsdLocation = org.apache.ambari.server.state.repository.VersionDefinitionXml.SCHEMA_LOCATION;
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stackInfo.getName(), stackInfo.getVersion());
        xml.release.repositoryType = org.apache.ambari.spi.RepositoryType.STANDARD;
        xml.release.stackId = stackId.toString();
        xml.release.version = stackInfo.getVersion();
        xml.release.releaseNotes = "NONE";
        xml.release.display = stackId.toString();
        for (org.apache.ambari.server.state.ServiceInfo si : stackInfo.getServices()) {
            if (!si.isVersionAdvertised()) {
                continue;
            }
            org.apache.ambari.server.state.repository.ManifestService ms = new org.apache.ambari.server.state.repository.ManifestService();
            ms.serviceName = si.getName();
            ms.version = org.apache.commons.lang.StringUtils.trimToEmpty(si.getVersion());
            ms.serviceId = (ms.serviceName + "-") + ms.version.replace(".", "");
            xml.manifestServices.add(ms);
        }
        if (null != stackInfo.getRepositoryXml()) {
            xml.repositoryInfo.getOses().addAll(stackInfo.getRepositoryXml().getOses());
        }
        try {
            xml.toXml();
        } catch (java.lang.Exception e) {
            throw new java.lang.IllegalArgumentException(e);
        }
        return xml;
    }

    public static class Merger {
        private org.apache.ambari.server.state.repository.VersionDefinitionXml m_xml = new org.apache.ambari.server.state.repository.VersionDefinitionXml();

        private boolean m_seeded = false;

        public Merger() {
            m_xml.release = new org.apache.ambari.server.state.repository.Release();
            m_xml.repositoryInfo = new org.apache.ambari.server.state.stack.RepositoryXml();
        }

        public void add(java.lang.String version, org.apache.ambari.server.state.repository.VersionDefinitionXml xml) {
            if (!m_seeded) {
                m_xml.xsdLocation = xml.xsdLocation;
                org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(xml.release.stackId);
                m_xml.release.build = null;
                m_xml.release.compatibleWith = xml.release.compatibleWith;
                m_xml.release.display = (stackId.getStackName() + "-") + xml.release.version;
                m_xml.release.repositoryType = org.apache.ambari.spi.RepositoryType.STANDARD;
                m_xml.release.releaseNotes = xml.release.releaseNotes;
                m_xml.release.stackId = xml.release.stackId;
                m_xml.release.version = version;
                m_xml.manifestServices.addAll(xml.manifestServices);
                m_seeded = true;
            }
            m_xml.repositoryInfo.getOses().addAll(xml.repositoryInfo.getOses());
        }

        public org.apache.ambari.server.state.repository.VersionDefinitionXml merge() {
            return m_seeded ? m_xml : null;
        }
    }
}