package org.apache.ambari.server.api.services.stackadvisor.commands;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;
public abstract class StackAdvisorCommand<T extends org.apache.ambari.server.api.services.stackadvisor.StackAdvisorResponse> extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.Class<T> type;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.class);

    private static final java.lang.String GET_HOSTS_INFO_URI = "/api/v1/hosts" + "?fields=Hosts/*&Hosts/host_name.in(%s)";

    private static final java.lang.String GET_SERVICES_INFO_URI = "/api/v1/stacks/%s/versions/%s/" + (((((((("?fields=Versions/stack_name,Versions/stack_version,Versions/parent_stack_version" + ",services/StackServices/service_name,services/StackServices/service_version") + ",services/components/StackServiceComponents,services/components/dependencies/Dependencies/scope") + ",services/components/dependencies/Dependencies/type") + ",services/components/dependencies/Dependencies/conditions,services/components/auto_deploy") + ",services/configurations/StackConfigurations/property_depends_on") + ",services/configurations/dependencies/StackConfigurationDependency/dependency_name") + ",services/configurations/dependencies/StackConfigurationDependency/dependency_type,services/configurations/StackConfigurations/type") + "&services/StackServices/service_name.in(%s)");

    private static final java.lang.String SERVICES_PROPERTY = "services";

    private static final java.lang.String SERVICES_COMPONENTS_PROPERTY = "components";

    private static final java.lang.String CONFIG_GROUPS_PROPERTY = "config-groups";

    private static final java.lang.String STACK_SERVICES_PROPERTY = "StackServices";

    private static final java.lang.String COMPONENT_INFO_PROPERTY = "StackServiceComponents";

    private static final java.lang.String COMPONENT_NAME_PROPERTY = "component_name";

    private static final java.lang.String COMPONENT_HOSTNAMES_PROPERTY = "hostnames";

    private static final java.lang.String CONFIGURATIONS_PROPERTY = "configurations";

    private static final java.lang.String CHANGED_CONFIGURATIONS_PROPERTY = "changed-configurations";

    private static final java.lang.String USER_CONTEXT_PROPERTY = "user-context";

    private static final java.lang.String GPL_LICENSE_ACCEPTED = "gpl-license-accepted";

    private static final java.lang.String AMBARI_SERVER_PROPERTIES_PROPERTY = "ambari-server-properties";

    private static final java.lang.String AMBARI_SERVER_CONFIGURATIONS_PROPERTY = "ambari-server-configuration";

    private final java.util.Map<java.lang.String, org.codehaus.jackson.JsonNode> hostInfoCache;

    private java.io.File recommendationsDir;

    private java.lang.String recommendationsArtifactsLifetime;

    private org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType;

    private int requestId;

    private java.io.File requestDirectory;

    private org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner;

    protected org.codehaus.jackson.map.ObjectMapper mapper;

    private final org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo;

    private final org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler ambariServerConfigurationHandler;

    @java.lang.SuppressWarnings("unchecked")
    public StackAdvisorCommand(java.io.File recommendationsDir, java.lang.String recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType, int requestId, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo, org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler ambariServerConfigurationHandler, java.util.Map<java.lang.String, org.codehaus.jackson.JsonNode> hostInfoCache) {
        this.type = ((java.lang.Class<T>) (((java.lang.reflect.ParameterizedType) (getClass().getGenericSuperclass())).getActualTypeArguments()[0]));
        this.mapper = new org.codehaus.jackson.map.ObjectMapper();
        this.mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        this.recommendationsDir = recommendationsDir;
        this.recommendationsArtifactsLifetime = recommendationsArtifactsLifetime;
        this.serviceAdvisorType = serviceAdvisorType;
        this.requestId = requestId;
        this.saRunner = saRunner;
        this.metaInfo = metaInfo;
        this.ambariServerConfigurationHandler = ambariServerConfigurationHandler;
        this.hostInfoCache = hostInfoCache;
    }

    public StackAdvisorCommand(java.io.File recommendationsDir, java.lang.String recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType, int requestId, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo, org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler ambariServerConfigurationHandler) {
        this(recommendationsDir, recommendationsArtifactsLifetime, serviceAdvisorType, requestId, saRunner, metaInfo, ambariServerConfigurationHandler, null);
    }

    protected abstract org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType getCommandType();

    public static class StackAdvisorData {
        protected java.lang.String hostsJSON;

        protected java.lang.String servicesJSON;

        public StackAdvisorData(java.lang.String hostsJSON, java.lang.String servicesJSON) {
            this.hostsJSON = hostsJSON;
            this.servicesJSON = servicesJSON;
        }
    }

    protected abstract java.lang.String getResultFileName();

    protected abstract void validate(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException;

    protected org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.StackAdvisorData adjust(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.StackAdvisorData data, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) {
        try {
            org.codehaus.jackson.node.ObjectNode root = ((org.codehaus.jackson.node.ObjectNode) (this.mapper.readTree(data.servicesJSON)));
            populateStackHierarchy(root);
            populateComponentHostsMap(root, request.getComponentHostsMap());
            populateServiceAdvisors(root);
            populateConfigurations(root, request);
            populateConfigGroups(root, request);
            populateAmbariServerInfo(root);
            populateAmbariConfiguration(root);
            data.servicesJSON = mapper.writeValueAsString(root);
        } catch (java.lang.Exception e) {
            java.lang.String message = "Error parsing services.json file content: " + e.getMessage();
            org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.LOG.warn(message, e);
            throw new javax.ws.rs.WebApplicationException(javax.ws.rs.core.Response.status(Status.BAD_REQUEST).entity(message).build());
        }
        return data;
    }

    void populateAmbariConfiguration(org.codehaus.jackson.node.ObjectNode root) {
        root.put(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.AMBARI_SERVER_CONFIGURATIONS_PROPERTY, mapper.valueToTree(ambariServerConfigurationHandler.getConfigurations()));
    }

    protected void populateAmbariServerInfo(org.codehaus.jackson.node.ObjectNode root) {
        java.util.Map<java.lang.String, java.lang.String> serverProperties = metaInfo.getAmbariServerProperties();
        if ((serverProperties != null) && (!serverProperties.isEmpty())) {
            org.codehaus.jackson.JsonNode serverPropertiesNode = mapper.convertValue(serverProperties, org.codehaus.jackson.JsonNode.class);
            root.put(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.AMBARI_SERVER_PROPERTIES_PROPERTY, serverPropertiesNode);
        }
    }

    private void populateConfigurations(org.codehaus.jackson.node.ObjectNode root, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurations = request.getConfigurations();
        org.codehaus.jackson.node.ObjectNode configurationsNode = root.putObject(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.CONFIGURATIONS_PROPERTY);
        for (java.lang.String siteName : configurations.keySet()) {
            org.codehaus.jackson.node.ObjectNode siteNode = configurationsNode.putObject(siteName);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> siteMap = configurations.get(siteName);
            for (java.lang.String properties : siteMap.keySet()) {
                org.codehaus.jackson.node.ObjectNode propertiesNode = siteNode.putObject(properties);
                java.util.Map<java.lang.String, java.lang.String> propertiesMap = siteMap.get(properties);
                for (java.lang.String propertyName : propertiesMap.keySet()) {
                    java.lang.String propertyValue = propertiesMap.get(propertyName);
                    propertiesNode.put(propertyName, propertyValue);
                }
            }
        }
        org.codehaus.jackson.JsonNode changedConfigs = mapper.valueToTree(request.getChangedConfigurations());
        root.put(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.CHANGED_CONFIGURATIONS_PROPERTY, changedConfigs);
        org.codehaus.jackson.JsonNode userContext = mapper.valueToTree(request.getUserContext());
        root.put(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.USER_CONTEXT_PROPERTY, userContext);
        root.put(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.GPL_LICENSE_ACCEPTED, request.getGplLicenseAccepted());
    }

    private void populateConfigGroups(org.codehaus.jackson.node.ObjectNode root, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) {
        if ((request.getConfigGroups() != null) && (!request.getConfigGroups().isEmpty())) {
            org.codehaus.jackson.JsonNode configGroups = mapper.valueToTree(request.getConfigGroups());
            root.put(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.CONFIG_GROUPS_PROPERTY, configGroups);
        }
    }

    protected void populateStackHierarchy(org.codehaus.jackson.node.ObjectNode root) {
        org.codehaus.jackson.node.ObjectNode version = ((org.codehaus.jackson.node.ObjectNode) (root.get("Versions")));
        org.codehaus.jackson.node.TextNode stackName = ((org.codehaus.jackson.node.TextNode) (version.get("stack_name")));
        org.codehaus.jackson.node.TextNode stackVersion = ((org.codehaus.jackson.node.TextNode) (version.get("stack_version")));
        org.codehaus.jackson.node.ObjectNode stackHierarchy = version.putObject("stack_hierarchy");
        stackHierarchy.put("stack_name", stackName);
        org.codehaus.jackson.node.ArrayNode parents = stackHierarchy.putArray("stack_versions");
        for (java.lang.String parentVersion : metaInfo.getStackParentVersions(stackName.asText(), stackVersion.asText())) {
            parents.add(parentVersion);
        }
    }

    private void populateComponentHostsMap(org.codehaus.jackson.node.ObjectNode root, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostsMap) {
        org.codehaus.jackson.node.ArrayNode services = ((org.codehaus.jackson.node.ArrayNode) (root.get(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.SERVICES_PROPERTY)));
        java.util.Iterator<org.codehaus.jackson.JsonNode> servicesIter = services.getElements();
        while (servicesIter.hasNext()) {
            org.codehaus.jackson.JsonNode service = servicesIter.next();
            org.codehaus.jackson.node.ArrayNode components = ((org.codehaus.jackson.node.ArrayNode) (service.get(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.SERVICES_COMPONENTS_PROPERTY)));
            java.util.Iterator<org.codehaus.jackson.JsonNode> componentsIter = components.getElements();
            while (componentsIter.hasNext()) {
                org.codehaus.jackson.JsonNode component = componentsIter.next();
                org.codehaus.jackson.node.ObjectNode componentInfo = ((org.codehaus.jackson.node.ObjectNode) (component.get(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.COMPONENT_INFO_PROPERTY)));
                java.lang.String componentName = componentInfo.get(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.COMPONENT_NAME_PROPERTY).getTextValue();
                java.util.Set<java.lang.String> componentHosts = componentHostsMap.get(componentName);
                org.codehaus.jackson.node.ArrayNode hostnames = componentInfo.putArray(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.COMPONENT_HOSTNAMES_PROPERTY);
                if (null != componentHosts) {
                    for (java.lang.String hostName : componentHosts) {
                        hostnames.add(hostName);
                    }
                }
            } 
        } 
    }

    private void populateServiceAdvisors(org.codehaus.jackson.node.ObjectNode root) {
        org.codehaus.jackson.node.ArrayNode services = ((org.codehaus.jackson.node.ArrayNode) (root.get(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.SERVICES_PROPERTY)));
        java.util.Iterator<org.codehaus.jackson.JsonNode> servicesIter = services.getElements();
        org.codehaus.jackson.node.ObjectNode version = ((org.codehaus.jackson.node.ObjectNode) (root.get("Versions")));
        java.lang.String stackName = version.get("stack_name").asText();
        java.lang.String stackVersion = version.get("stack_version").asText();
        while (servicesIter.hasNext()) {
            org.codehaus.jackson.JsonNode service = servicesIter.next();
            org.codehaus.jackson.node.ObjectNode serviceVersion = ((org.codehaus.jackson.node.ObjectNode) (service.get(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.STACK_SERVICES_PROPERTY)));
            java.lang.String serviceName = serviceVersion.get("service_name").getTextValue();
            try {
                org.apache.ambari.server.state.ServiceInfo serviceInfo = metaInfo.getService(stackName, stackVersion, serviceName);
                if (serviceInfo.getAdvisorFile() != null) {
                    serviceVersion.put("advisor_name", serviceInfo.getAdvisorName());
                    serviceVersion.put("advisor_path", serviceInfo.getAdvisorFile().getAbsolutePath());
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.LOG.error("Error adding service advisor information to services.json", e);
            }
        } 
    }

    public synchronized T invoke(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        validate(request);
        java.lang.String hostsJSON = getHostsInformation(request);
        java.lang.String servicesJSON = getServicesInformation(request);
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.StackAdvisorData adjusted = adjust(new org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.StackAdvisorData(hostsJSON, servicesJSON), request);
        try {
            createRequestDirectory();
            org.apache.commons.io.FileUtils.writeStringToFile(new java.io.File(requestDirectory, "hosts.json"), adjusted.hostsJSON, java.nio.charset.Charset.defaultCharset());
            org.apache.commons.io.FileUtils.writeStringToFile(new java.io.File(requestDirectory, "services.json"), adjusted.servicesJSON, java.nio.charset.Charset.defaultCharset());
            saRunner.runScript(serviceAdvisorType, getCommandType(), requestDirectory);
            java.lang.String result = org.apache.commons.io.FileUtils.readFileToString(new java.io.File(requestDirectory, getResultFileName()), java.nio.charset.Charset.defaultCharset());
            T response = this.mapper.readValue(result, this.type);
            return updateResponse(request, setRequestId(response));
        } catch (org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException ex) {
            throw ex;
        } catch (java.lang.Exception e) {
            java.lang.String message = "Error occured during stack advisor command invocation: ";
            org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.LOG.warn(message, e);
            throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException(message + e.getMessage());
        }
    }

    protected abstract T updateResponse(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request, T response);

    private T setRequestId(T response) {
        response.setId(requestId);
        return response;
    }

    private void createRequestDirectory() throws java.io.IOException {
        if (!recommendationsDir.exists()) {
            if (!recommendationsDir.mkdirs()) {
                throw new java.io.IOException("Cannot create " + recommendationsDir);
            }
        }
        cleanupRequestDirectory();
        requestDirectory = new java.io.File(recommendationsDir, java.lang.Integer.toString(requestId));
        if (requestDirectory.exists()) {
            org.apache.commons.io.FileUtils.deleteDirectory(requestDirectory);
        }
        if (!requestDirectory.mkdirs()) {
            throw new java.io.IOException("Cannot create " + requestDirectory);
        }
    }

    private void cleanupRequestDirectory() throws java.io.IOException {
        final java.util.Date cutoffDate = org.apache.ambari.server.utils.DateUtils.getDateSpecifiedTimeAgo(recommendationsArtifactsLifetime);
        java.lang.String[] oldDirectories = recommendationsDir.list(new java.io.FilenameFilter() {
            @java.lang.Override
            public boolean accept(java.io.File current, java.lang.String name) {
                java.io.File file = new java.io.File(current, name);
                return file.isDirectory() && (!org.apache.commons.io.FileUtils.isFileNewer(file, cutoffDate));
            }
        });
        if (oldDirectories.length > 0) {
            org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.LOG.info(java.lang.String.format("Deleting old directories %s from %s", org.apache.commons.lang.StringUtils.join(oldDirectories, ", "), recommendationsDir));
        }
        for (java.lang.String oldDirectory : oldDirectories) {
            org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(recommendationsDir, oldDirectory));
        }
    }

    java.lang.String getHostsInformation(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        java.util.List<java.lang.String> hostNames = new java.util.ArrayList<>(request.getHosts());
        java.util.List<org.codehaus.jackson.JsonNode> resultInfos = new java.util.ArrayList<>();
        if ((hostInfoCache != null) && (!hostInfoCache.isEmpty())) {
            java.util.Iterator<java.lang.String> hostNamesIterator = hostNames.iterator();
            while (hostNamesIterator.hasNext()) {
                java.lang.String hostName = hostNamesIterator.next();
                org.codehaus.jackson.JsonNode node = hostInfoCache.get(hostName);
                if (node != null) {
                    resultInfos.add(node);
                    hostNamesIterator.remove();
                }
            } 
        }
        java.lang.String hostsJSON = null;
        if (!hostNames.isEmpty()) {
            org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.LOG.info(java.lang.String.format("Fire host info request for hosts: " + hostNames.toString()));
            java.lang.String hostsURI = java.lang.String.format(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.GET_HOSTS_INFO_URI, java.lang.String.join(",", hostNames));
            javax.ws.rs.core.Response response = handleRequest(null, null, new org.apache.ambari.server.api.services.LocalUriInfo(hostsURI), org.apache.ambari.server.api.services.Request.Type.GET, createHostResource());
            if (response.getStatus() != Status.OK.getStatusCode()) {
                java.lang.String message = java.lang.String.format("Error occured during hosts information retrieving, status=%s, response=%s", response.getStatus(), ((java.lang.String) (response.getEntity())));
                org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.LOG.warn(message);
                throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException(message);
            }
            hostsJSON = ((java.lang.String) (response.getEntity()));
            if (org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.LOG.isDebugEnabled()) {
                org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.LOG.debug("Hosts information: {}", hostsJSON);
            }
        }
        if (hostInfoCache != null) {
            if ((hostsJSON != null) && (!hostsJSON.isEmpty())) {
                try {
                    org.codehaus.jackson.JsonNode root = mapper.readTree(hostsJSON);
                    java.util.Iterator<org.codehaus.jackson.JsonNode> iterator = root.get("items").getElements();
                    while (iterator.hasNext()) {
                        org.codehaus.jackson.JsonNode next = iterator.next();
                        java.lang.String hostName = next.get("Hosts").get("host_name").getTextValue();
                        hostInfoCache.put(hostName, next);
                        resultInfos.add(next);
                    } 
                } catch (java.io.IOException e) {
                    throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException("Error occured during parsing result host infos", e);
                }
            }
            java.lang.String fullHostsURI = java.lang.String.format(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.GET_HOSTS_INFO_URI, request.getHostsCommaSeparated());
            org.codehaus.jackson.node.JsonNodeFactory f = org.codehaus.jackson.node.JsonNodeFactory.instance;
            org.codehaus.jackson.node.ObjectNode resultRoot = f.objectNode();
            resultRoot.put("href", fullHostsURI);
            org.codehaus.jackson.node.ArrayNode resultArray = resultRoot.putArray("items");
            resultArray.addAll(resultInfos);
            hostsJSON = resultRoot.toString();
        }
        java.util.Collection<java.lang.String> unregistered = getUnregisteredHosts(hostsJSON, request.getHosts());
        if (unregistered.size() > 0) {
            java.lang.String message = java.lang.String.format("There are unregistered hosts in the request, %s", java.util.Arrays.toString(unregistered.toArray()));
            org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.LOG.warn(message);
            throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException(message);
        }
        return hostsJSON;
    }

    @java.lang.SuppressWarnings("unchecked")
    private java.util.Collection<java.lang.String> getUnregisteredHosts(java.lang.String hostsJSON, java.util.List<java.lang.String> hosts) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        java.util.List<java.lang.String> registeredHosts = new java.util.ArrayList<>();
        try {
            org.codehaus.jackson.JsonNode root = mapper.readTree(hostsJSON);
            java.util.Iterator<org.codehaus.jackson.JsonNode> iterator = root.get("items").getElements();
            while (iterator.hasNext()) {
                org.codehaus.jackson.JsonNode next = iterator.next();
                java.lang.String hostName = next.get("Hosts").get("host_name").getTextValue();
                registeredHosts.add(hostName);
            } 
            return org.apache.commons.collections.CollectionUtils.subtract(hosts, registeredHosts);
        } catch (java.lang.Exception e) {
            throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException("Error occured during calculating unregistered hosts", e);
        }
    }

    java.lang.String getServicesInformation(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        java.lang.String stackName = request.getStackName();
        java.lang.String stackVersion = request.getStackVersion();
        java.lang.String servicesURI = java.lang.String.format(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.GET_SERVICES_INFO_URI, stackName, stackVersion, request.getServicesCommaSeparated());
        javax.ws.rs.core.Response response = handleRequest(null, null, new org.apache.ambari.server.api.services.LocalUriInfo(servicesURI), org.apache.ambari.server.api.services.Request.Type.GET, createStackVersionResource(stackName, stackVersion));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            java.lang.String message = java.lang.String.format("Error occured during services information retrieving, status=%s, response=%s", response.getStatus(), ((java.lang.String) (response.getEntity())));
            org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.LOG.warn(message);
            throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException(message);
        }
        java.lang.String servicesJSON = ((java.lang.String) (response.getEntity()));
        if (org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.LOG.isDebugEnabled()) {
            org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand.LOG.debug("Services information: {}", servicesJSON);
        }
        return servicesJSON;
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createHostResource() {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Host, mapIds);
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createConfigResource() {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.RootService, org.apache.ambari.server.controller.RootService.AMBARI.name());
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponent, org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name());
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponentConfiguration, mapIds);
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createStackVersionResource(java.lang.String stackName, java.lang.String stackVersion) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, mapIds);
    }
}