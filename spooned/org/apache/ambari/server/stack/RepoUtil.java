package org.apache.ambari.server.stack;
public class RepoUtil {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.RepoUtil.class);

    static final java.lang.String REPOSITORY_FOLDER_NAME = "repos";

    static final java.lang.String REPOSITORY_FILE_NAME = "repoinfo.xml";

    private static final com.google.common.base.Function<org.apache.ambari.server.orm.entities.RepoDefinitionEntity, java.lang.String> REPO_ENTITY_TO_NAME = new com.google.common.base.Function<org.apache.ambari.server.orm.entities.RepoDefinitionEntity, java.lang.String>() {
        @java.lang.Override
        public java.lang.String apply(org.apache.ambari.server.orm.entities.RepoDefinitionEntity input) {
            return input.getRepoName();
        }
    };

    public static org.apache.ambari.server.stack.RepositoryFolderAndXml parseRepoFile(java.io.File directory, java.util.Collection<java.lang.String> subDirs, org.apache.ambari.server.stack.ModuleFileUnmarshaller unmarshaller) {
        java.io.File repositoryFile = null;
        java.lang.String repoDir = null;
        org.apache.ambari.server.state.stack.RepositoryXml repoFile = null;
        if (subDirs.contains(org.apache.ambari.server.stack.RepoUtil.REPOSITORY_FOLDER_NAME)) {
            repoDir = (directory.getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.stack.RepoUtil.REPOSITORY_FOLDER_NAME;
            repositoryFile = new java.io.File((((directory.getPath() + java.io.File.separator) + org.apache.ambari.server.stack.RepoUtil.REPOSITORY_FOLDER_NAME) + java.io.File.separator) + org.apache.ambari.server.stack.RepoUtil.REPOSITORY_FILE_NAME);
            if (repositoryFile.exists()) {
                try {
                    repoFile = unmarshaller.unmarshal(org.apache.ambari.server.state.stack.RepositoryXml.class, repositoryFile);
                } catch (java.lang.Exception e) {
                    repoFile = new org.apache.ambari.server.state.stack.RepositoryXml();
                    repoFile.setValid(false);
                    java.lang.String msg = "Unable to parse repo file at location: " + repositoryFile.getAbsolutePath();
                    repoFile.addError(msg);
                    org.apache.ambari.server.stack.RepoUtil.LOG.warn(msg);
                }
            }
        }
        return new org.apache.ambari.server.stack.RepositoryFolderAndXml(com.google.common.base.Optional.fromNullable(repoDir), com.google.common.base.Optional.fromNullable(repoFile));
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    public static boolean addServiceReposToOperatingSystemEntities(java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystems, com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> stackReposByOs) {
        java.util.Set<java.lang.String> addedRepos = new java.util.HashSet<>();
        for (org.apache.ambari.server.orm.entities.RepoOsEntity os : operatingSystems) {
            java.util.List<org.apache.ambari.server.state.RepositoryInfo> serviceReposForOs = stackReposByOs.get(os.getFamily());
            com.google.common.collect.ImmutableSet<java.lang.String> repoNames = com.google.common.collect.ImmutableSet.copyOf(com.google.common.collect.Lists.transform(os.getRepoDefinitionEntities(), org.apache.ambari.server.stack.RepoUtil.REPO_ENTITY_TO_NAME));
            for (org.apache.ambari.server.state.RepositoryInfo repoInfo : serviceReposForOs)
                if (!repoNames.contains(repoInfo.getRepoName())) {
                    os.addRepoDefinition(org.apache.ambari.server.stack.RepoUtil.toRepositoryEntity(repoInfo));
                    addedRepos.add(java.lang.String.format("%s (%s)", repoInfo.getRepoId(), os.getFamily()));
                }

        }
        org.apache.ambari.server.stack.RepoUtil.LOG.info("Added {} service repos: {}", addedRepos.size(), com.google.common.collect.Iterables.toString(addedRepos));
        return org.apache.commons.collections.CollectionUtils.isNotEmpty(addedRepos);
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    public static java.util.List<org.apache.ambari.server.state.RepositoryInfo> getServiceRepos(java.util.List<org.apache.ambari.server.state.RepositoryInfo> vdfRepos, com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> stackReposByOs) {
        java.util.Set<java.lang.String> serviceRepoIds = new java.util.HashSet<>();
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> serviceRepos = new java.util.ArrayList<>();
        com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> vdfReposByOs = com.google.common.collect.Multimaps.index(vdfRepos, org.apache.ambari.server.state.RepositoryInfo.GET_OSTYPE_FUNCTION);
        for (java.lang.String os : vdfReposByOs.keySet()) {
            java.util.Set<java.lang.String> vdfRepoNames = com.google.common.collect.Sets.newHashSet(com.google.common.collect.Lists.transform(vdfReposByOs.get(os), org.apache.ambari.server.state.RepositoryInfo.GET_REPO_NAME_FUNCTION));
            for (org.apache.ambari.server.state.RepositoryInfo repo : stackReposByOs.get(os)) {
                if (!vdfRepoNames.contains(repo.getRepoName())) {
                    serviceRepos.add(repo);
                    serviceRepoIds.add(repo.getRepoId());
                }
            }
        }
        org.apache.ambari.server.stack.RepoUtil.LOG.debug("Found {} service repos: {}", serviceRepoIds.size(), com.google.common.collect.Iterables.toString(serviceRepoIds));
        return serviceRepos;
    }

    public static java.util.List<org.apache.ambari.server.controller.RepositoryResponse> asResponses(java.util.List<org.apache.ambari.server.state.RepositoryInfo> repositoryInfos, @javax.annotation.Nullable
    java.lang.String versionDefinitionId, @javax.annotation.Nullable
    java.lang.String stackName, @javax.annotation.Nullable
    java.lang.String stackVersion) {
        java.util.List<org.apache.ambari.server.controller.RepositoryResponse> responses = new java.util.ArrayList<>(repositoryInfos.size());
        for (org.apache.ambari.server.state.RepositoryInfo repoInfo : repositoryInfos) {
            org.apache.ambari.server.controller.RepositoryResponse response = repoInfo.convertToResponse();
            response.setVersionDefinitionId(versionDefinitionId);
            response.setStackName(stackName);
            response.setStackVersion(stackVersion);
            responses.add(response);
        }
        return responses;
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    private static org.apache.ambari.server.orm.entities.RepoDefinitionEntity toRepositoryEntity(org.apache.ambari.server.state.RepositoryInfo repoInfo) {
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity re = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        re.setBaseUrl(repoInfo.getBaseUrl());
        re.setRepoName(repoInfo.getRepoName());
        re.setRepoID(repoInfo.getRepoId());
        re.setDistribution(repoInfo.getDistribution());
        re.setComponents(repoInfo.getComponents());
        re.setTags(repoInfo.getTags());
        re.setApplicableServices(repoInfo.getApplicableServices());
        return re;
    }
}