package org.apache.ambari.server.stack.upgrade;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class RepositoryVersionHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper.class);

    @com.google.inject.Inject
    private com.google.gson.Gson gson;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> ami;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.configuration.Configuration> configuration;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.stack.OsFamily> os_family;

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clusters;

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity getRepositoryVersionEntity(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponent component) throws org.apache.ambari.server.controller.spi.SystemException {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryEntity = null;
        if (null != component) {
            repositoryEntity = component.getDesiredRepositoryVersion();
        } else {
            org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper.LOG.info("Service component not passed in, attempt to resolve the repository for cluster {}", cluster.getClusterName());
        }
        if ((null == repositoryEntity) && (null != component)) {
            try {
                org.apache.ambari.server.state.Service service = cluster.getService(component.getServiceName());
                repositoryEntity = service.getDesiredRepositoryVersion();
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException("Unhandled exception", e);
            }
        }
        if (null == repositoryEntity) {
            org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper.LOG.info("Cluster {} has no specific Repository Versions.  Using stack-defined values", cluster.getClusterName());
            return null;
        }
        return repositoryEntity;
    }

    public java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> parseOperatingSystems(java.lang.String repositoriesJson) throws java.lang.Exception {
        final java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystems = new java.util.ArrayList<>();
        final com.google.gson.JsonArray rootJson = new com.google.gson.JsonParser().parse(repositoriesJson).getAsJsonArray();
        for (com.google.gson.JsonElement operatingSystemJson : rootJson) {
            com.google.gson.JsonObject osObj = operatingSystemJson.getAsJsonObject();
            final org.apache.ambari.server.orm.entities.RepoOsEntity operatingSystemEntity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
            operatingSystemEntity.setFamily(osObj.get(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_OS_TYPE_PROPERTY_ID).getAsString());
            if (osObj.has(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_AMBARI_MANAGED_REPOS)) {
                operatingSystemEntity.setAmbariManaged(osObj.get(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_AMBARI_MANAGED_REPOS).getAsBoolean());
            }
            for (com.google.gson.JsonElement repositoryElement : osObj.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_REPOSITORIES_PROPERTY_ID).getAsJsonArray()) {
                final org.apache.ambari.server.orm.entities.RepoDefinitionEntity repositoryEntity = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
                final com.google.gson.JsonObject repositoryJson = repositoryElement.getAsJsonObject();
                repositoryEntity.setBaseUrl(repositoryJson.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID).getAsString());
                repositoryEntity.setRepoName(repositoryJson.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_NAME_PROPERTY_ID).getAsString());
                repositoryEntity.setRepoID(repositoryJson.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID).getAsString());
                if (repositoryJson.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_DISTRIBUTION_PROPERTY_ID) != null) {
                    repositoryEntity.setDistribution(repositoryJson.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_DISTRIBUTION_PROPERTY_ID).getAsString());
                }
                if (repositoryJson.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_COMPONENTS_PROPERTY_ID) != null) {
                    repositoryEntity.setComponents(repositoryJson.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_COMPONENTS_PROPERTY_ID).getAsString());
                }
                if (repositoryJson.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_MIRRORS_LIST_PROPERTY_ID) != null) {
                    repositoryEntity.setMirrors(repositoryJson.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_MIRRORS_LIST_PROPERTY_ID).getAsString());
                }
                if (repositoryJson.getAsJsonObject().get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_UNIQUE_PROPERTY_ID) != null) {
                    repositoryEntity.setUnique(repositoryJson.getAsJsonObject().get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_UNIQUE_PROPERTY_ID).getAsBoolean());
                }
                if (repositoryJson.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_APPLICABLE_SERVICES_PROPERTY_ID) != null) {
                    java.util.List<java.lang.String> applicableServices = new java.util.LinkedList<>();
                    com.google.gson.JsonArray jsonArray = repositoryJson.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_APPLICABLE_SERVICES_PROPERTY_ID).getAsJsonArray();
                    for (com.google.gson.JsonElement je : jsonArray) {
                        applicableServices.add(je.getAsString());
                    }
                    repositoryEntity.setApplicableServices(applicableServices);
                }
                if (null != repositoryJson.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_TAGS_PROPERTY_ID)) {
                    java.util.Set<org.apache.ambari.server.state.stack.RepoTag> tags = new java.util.HashSet<>();
                    com.google.gson.JsonArray jsonArray = repositoryJson.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_TAGS_PROPERTY_ID).getAsJsonArray();
                    for (com.google.gson.JsonElement je : jsonArray) {
                        tags.add(org.apache.ambari.server.state.stack.RepoTag.valueOf(je.getAsString()));
                    }
                    repositoryEntity.setTags(tags);
                }
                operatingSystemEntity.addRepoDefinition(repositoryEntity);
            }
            operatingSystems.add(operatingSystemEntity);
        }
        return operatingSystems;
    }

    public java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> createRepoOsEntities(java.util.List<org.apache.ambari.server.state.RepositoryInfo> repositories) {
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> repoOsEntities = new java.util.ArrayList<>();
        final com.google.common.collect.Multimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> operatingSystems = com.google.common.collect.ArrayListMultimap.create();
        for (org.apache.ambari.server.state.RepositoryInfo repository : repositories) {
            operatingSystems.put(repository.getOsType(), repository);
        }
        for (java.util.Map.Entry<java.lang.String, java.util.Collection<org.apache.ambari.server.state.RepositoryInfo>> operatingSystem : operatingSystems.asMap().entrySet()) {
            org.apache.ambari.server.orm.entities.RepoOsEntity operatingSystemEntity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
            java.util.List<org.apache.ambari.server.orm.entities.RepoDefinitionEntity> repositoriesList = new java.util.ArrayList<>();
            for (org.apache.ambari.server.state.RepositoryInfo repository : operatingSystem.getValue()) {
                org.apache.ambari.server.orm.entities.RepoDefinitionEntity repositoryDefinition = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
                repositoryDefinition.setBaseUrl(repository.getBaseUrl());
                repositoryDefinition.setRepoName(repository.getRepoName());
                repositoryDefinition.setRepoID(repository.getRepoId());
                repositoryDefinition.setDistribution(repository.getDistribution());
                repositoryDefinition.setComponents(repository.getComponents());
                repositoryDefinition.setMirrors(repository.getMirrorsList());
                repositoryDefinition.setUnique(repository.isUnique());
                repositoryDefinition.setTags(repository.getTags());
                repositoryDefinition.setApplicableServices(repository.getApplicableServices());
                repositoriesList.add(repositoryDefinition);
                operatingSystemEntity.setAmbariManaged(repository.isAmbariManagedRepositories());
            }
            operatingSystemEntity.addRepoDefinitionEntities(repositoriesList);
            operatingSystemEntity.setFamily(operatingSystem.getKey());
            repoOsEntities.add(operatingSystemEntity);
        }
        return repoOsEntities;
    }

    public java.lang.String getUpgradePackageName(java.lang.String stackName, java.lang.String stackVersion, java.lang.String repositoryVersion, org.apache.ambari.spi.upgrade.UpgradeType upgradeType) throws org.apache.ambari.server.AmbariException {
        final java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgradePacks = ami.get().getUpgradePacks(stackName, stackVersion);
        for (org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack : upgradePacks.values()) {
            final java.lang.String upgradePackName = upgradePack.getName();
            if ((null != upgradeType) && (upgradePack.getType() != upgradeType)) {
                continue;
            }
            if (org.apache.commons.lang.StringUtils.isBlank(upgradePack.getTarget())) {
                org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper.LOG.error(("Upgrade pack " + upgradePackName) + " is corrupted, it should contain <target> node");
                continue;
            }
            if (upgradePack.canBeApplied(repositoryVersion)) {
                return upgradePackName;
            }
        }
        throw new org.apache.ambari.server.AmbariException(((("There were no suitable upgrade packs for stack " + stackName) + " ") + stackVersion) + (null != upgradeType ? " and upgrade type " + upgradeType : ""));
    }

    public java.util.Map<java.lang.String, java.lang.String> buildRoleParams(org.apache.ambari.server.controller.AmbariManagementController amc, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion, java.lang.String osFamily, java.util.Set<java.lang.String> servicesOnHost) throws org.apache.ambari.server.controller.spi.SystemException {
        org.apache.ambari.server.state.StackId stackId = repoVersion.getStackId();
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> packages = new java.util.ArrayList<>();
        for (java.lang.String serviceName : servicesOnHost) {
            org.apache.ambari.server.state.ServiceInfo info;
            try {
                if (ami.get().isServiceRemovedInStack(stackId.getStackName(), stackId.getStackVersion(), serviceName)) {
                    org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper.LOG.info(java.lang.String.format("%s has been removed from stack %s-%s. Skip calculating its installation packages", stackId.getStackName(), stackId.getStackVersion(), serviceName));
                    continue;
                }
                info = ami.get().getService(stackId.getStackName(), stackId.getStackVersion(), serviceName);
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Cannot obtain stack information for %s-%s", stackId.getStackName(), stackId.getStackVersion()), e);
            }
            java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> packagesForService = amc.getPackagesForServiceHost(info, new java.util.HashMap<>(), osFamily);
            java.util.List<java.lang.String> blacklistedPackagePrefixes = configuration.get().getRollingUpgradeSkipPackagesPrefixes();
            for (org.apache.ambari.server.state.ServiceOsSpecific.Package aPackage : packagesForService) {
                if (!aPackage.getSkipUpgrade()) {
                    boolean blacklisted = false;
                    for (java.lang.String prefix : blacklistedPackagePrefixes) {
                        if (aPackage.getName().startsWith(prefix)) {
                            blacklisted = true;
                            break;
                        }
                    }
                    if (!blacklisted) {
                        packages.add(aPackage);
                    }
                }
            }
        }
        java.util.Map<java.lang.String, java.lang.String> roleParams = new java.util.HashMap<>();
        roleParams.put("stack_id", stackId.getStackId());
        roleParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.PACKAGE_LIST, gson.toJson(packages));
        return roleParams;
    }

    public org.apache.ambari.server.orm.entities.RepoOsEntity getOSEntityForHost(org.apache.ambari.server.state.Host host, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion) throws org.apache.ambari.server.controller.spi.SystemException {
        java.lang.String osFamily = host.getOsFamily();
        org.apache.ambari.server.orm.entities.RepoOsEntity osEntity = null;
        for (org.apache.ambari.server.orm.entities.RepoOsEntity operatingSystem : repoVersion.getRepoOsEntities()) {
            if (osFamily.equals(operatingSystem.getFamily())) {
                osEntity = operatingSystem;
                break;
            }
        }
        if (null == osEntity) {
            throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Operating System matching %s could not be found", osFamily));
        }
        return osEntity;
    }

    public org.apache.ambari.server.agent.CommandRepository getCommandRepository(final org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion, final org.apache.ambari.server.orm.entities.RepoOsEntity osEntity) throws org.apache.ambari.server.controller.spi.SystemException {
        final org.apache.ambari.server.agent.CommandRepository commandRepo = new org.apache.ambari.server.agent.CommandRepository();
        final boolean sysPreppedHost = configuration.get().areHostsSysPrepped().equalsIgnoreCase("true");
        if (null == repoVersion) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Repository version entity is not provided");
        }
        commandRepo.setRepositories(osEntity.getFamily(), osEntity.getRepoDefinitionEntities());
        commandRepo.setRepoVersion(repoVersion.getVersion());
        commandRepo.setRepositoryVersionId(repoVersion.getId());
        commandRepo.setResolved(repoVersion.isResolved());
        commandRepo.setStackName(repoVersion.getStackId().getStackName());
        commandRepo.getFeature().setPreInstalled(configuration.get().areHostsSysPrepped());
        commandRepo.getFeature().setIsScoped(!sysPreppedHost);
        if (!osEntity.isAmbariManaged()) {
            commandRepo.setNonManaged();
        } else if (repoVersion.isLegacy()) {
            commandRepo.setLegacyRepoFileName(repoVersion.getStackName(), repoVersion.getVersion());
            commandRepo.setLegacyRepoId(repoVersion.getVersion());
            commandRepo.getFeature().setIsScoped(false);
        } else {
            commandRepo.setRepoFileName(repoVersion.getStackName(), repoVersion.getId());
            commandRepo.setUniqueSuffix(java.lang.String.format("-repo-%s", repoVersion.getId()));
        }
        if (configuration.get().arePackagesLegacyOverridden()) {
            org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper.LOG.warn("Legacy override option is turned on, disabling CommandRepositoryFeature.scoped feature");
            commandRepo.getFeature().setIsScoped(false);
        }
        return commandRepo;
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
    public org.apache.ambari.server.agent.CommandRepository getCommandRepository(final org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponent component, final org.apache.ambari.server.state.Host host) throws org.apache.ambari.server.controller.spi.SystemException {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = getRepositoryVersionEntity(cluster, component);
        org.apache.ambari.server.orm.entities.RepoOsEntity osEntity = getOSEntityForHost(host, repoVersion);
        return getCommandRepository(repoVersion, osEntity);
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
    public org.apache.ambari.server.agent.CommandRepository getCommandRepository(final org.apache.ambari.server.state.Cluster cluster, final org.apache.ambari.server.state.Service service, final org.apache.ambari.server.state.Host host, final java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.agent.CommandRepository commandRepository = null;
        try {
            if (null != componentName) {
                org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(componentName);
                commandRepository = getCommandRepository(cluster, serviceComponent, host);
            } else {
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = service.getDesiredRepositoryVersion();
                org.apache.ambari.server.orm.entities.RepoOsEntity osEntity = getOSEntityForHost(host, repoVersion);
                commandRepository = getCommandRepository(repoVersion, osEntity);
            }
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
            org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper.LOG.debug("Unable to find command repository with a correct operating system for host {}", host, e);
        }
        return commandRepository;
    }

    @java.lang.Deprecated
    public void addRepoInfoToHostLevelParams(final org.apache.ambari.server.state.Cluster cluster, final org.apache.ambari.server.controller.ActionExecutionContext actionContext, final org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion, final java.util.Map<java.lang.String, java.lang.String> hostLevelParams, final java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        if (null == repositoryVersion) {
            if (null != actionContext.getStackId()) {
                org.apache.ambari.server.state.StackId stackId = actionContext.getStackId();
                hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_NAME, stackId.getStackName());
                hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_VERSION, stackId.getStackVersion());
            }
            return;
        } else {
            org.apache.ambari.server.state.StackId stackId = repositoryVersion.getStackId();
            hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_NAME, stackId.getStackName());
            hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_VERSION, stackId.getStackVersion());
        }
    }

    @java.lang.Deprecated
    private com.google.gson.JsonArray getBaseUrls(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponent component, org.apache.ambari.server.state.Host host) throws org.apache.ambari.server.controller.spi.SystemException {
        java.lang.String hostOsType = host.getOsType();
        java.lang.String hostOsFamily = host.getOsFamily();
        java.lang.String hostName = host.getHostName();
        org.apache.ambari.server.state.StackId stackId = component.getDesiredStackId();
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.RepositoryInfo>> repos;
        try {
            repos = ami.get().getRepository(stackId.getStackName(), stackId.getStackVersion());
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Unhandled exception", e);
        }
        java.lang.String family = os_family.get().find(hostOsType);
        if (null == family) {
            family = hostOsFamily;
        }
        final java.util.List<org.apache.ambari.server.state.RepositoryInfo> repoInfoList;
        if (repos.containsKey(hostOsType)) {
            repoInfoList = repos.get(hostOsType);
        } else if ((null != family) && repos.containsKey(family)) {
            repoInfoList = repos.get(family);
        } else {
            repoInfoList = null;
            org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper.LOG.warn(((((("Could not retrieve repo information for host" + ", hostname=") + hostName) + ", clusterName=") + cluster.getClusterName()) + ", stackInfo=") + stackId.getStackId());
        }
        return null == repoInfoList ? null : ((com.google.gson.JsonArray) (gson.toJsonTree(repoInfoList)));
    }

    public void addCommandRepositoryToContext(org.apache.ambari.server.controller.ActionExecutionContext context, final org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion, org.apache.ambari.server.orm.entities.RepoOsEntity osEntity) throws org.apache.ambari.server.controller.spi.SystemException {
        final org.apache.ambari.server.agent.CommandRepository commandRepo = getCommandRepository(repoVersion, osEntity);
        org.apache.ambari.server.state.repository.ClusterVersionSummary summary = null;
        if (org.apache.ambari.spi.RepositoryType.STANDARD != repoVersion.getType()) {
            try {
                final org.apache.ambari.server.state.Cluster cluster = clusters.get().getCluster(context.getClusterName());
                org.apache.ambari.server.state.repository.VersionDefinitionXml xml = repoVersion.getRepositoryXml();
                summary = xml.getClusterSummary(cluster, ami.get());
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper.LOG.warn("Could not determine repository from %s/%s.  Will not pass cluster version.");
            }
        }
        final org.apache.ambari.server.state.repository.ClusterVersionSummary clusterSummary = summary;
        context.addVisitor(command -> {
            if (null == command.getRepositoryFile()) {
                command.setRepositoryFile(commandRepo);
            }
            if (null != clusterSummary) {
                java.util.Map<java.lang.String, java.lang.Object> params = command.getRoleParameters();
                if (null == params) {
                    params = new java.util.HashMap<>();
                    command.setRoleParameters(params);
                }
                params.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.CLUSTER_VERSION_SUMMARY, clusterSummary);
            }
        });
    }

    public java.lang.String getRepoInfoString(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponent component, org.apache.ambari.server.state.Host host) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.controller.spi.SystemException {
        return gson.toJson(getCommandRepository(cluster, component, host));
    }
}