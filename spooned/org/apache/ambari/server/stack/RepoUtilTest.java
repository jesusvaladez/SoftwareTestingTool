package org.apache.ambari.server.stack;
public class RepoUtilTest {
    private static final java.util.List<java.lang.String> OPERATING_SYSTEMS = com.google.common.collect.ImmutableList.of("redhat6", "sles11", "ubuntu12");

    @org.junit.Test
    public void testAddServiceReposToOperatingSystemEntities_SimpleCase() {
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystems = new java.util.ArrayList<>();
        for (java.lang.String os : org.apache.ambari.server.stack.RepoUtilTest.OPERATING_SYSTEMS) {
            org.apache.ambari.server.orm.entities.RepoDefinitionEntity repo1 = org.apache.ambari.server.stack.RepoUtilTest.repoEntity("HDP", "HDP-2.3", "http://hdp.org/2.3");
            org.apache.ambari.server.orm.entities.RepoDefinitionEntity repo2 = org.apache.ambari.server.stack.RepoUtilTest.repoEntity("HDP-UTILS", "HDP-UTILS-1.1.0", "http://hdp.org/utils/1.1.0");
            operatingSystems.add(org.apache.ambari.server.stack.RepoUtilTest.osEntity(os, repo1, repo2));
        }
        com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> serviceRepos = org.apache.ambari.server.stack.RepoUtilTest.serviceRepos(com.google.common.collect.ImmutableList.of("redhat5", "redhat6", "sles11"), "MSFT_R", "MSFT_R-8.1", "http://msft.r");
        org.apache.ambari.server.stack.RepoUtil.addServiceReposToOperatingSystemEntities(operatingSystems, serviceRepos);
        for (org.apache.ambari.server.orm.entities.RepoOsEntity os : operatingSystems) {
            org.junit.Assert.assertNotSame("Redhat5 should not be added as new operating system.", "redhat5", os.getFamily());
            com.google.common.base.Optional<org.apache.ambari.server.orm.entities.RepoDefinitionEntity> msft_r = org.apache.ambari.server.stack.RepoUtilTest.findRepoEntityById(os.getRepoDefinitionEntities(), "MSFT_R-8.1");
            org.junit.Assert.assertTrue(java.lang.String.format("Only redhat6 and sles11 should contain the service repo. os: %s, repo: %s", os.getFamily(), msft_r), org.apache.ambari.server.stack.RepoUtilTest.findRepoEntityById(os.getRepoDefinitionEntities(), "MSFT_R-8.1").isPresent() == com.google.common.collect.ImmutableList.of("redhat6", "sles11").contains(os.getFamily()));
        }
    }

    @org.junit.Test
    public void testAddServiceReposToOperatingSystemEntities_RepoAlreadExists() {
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystems = new java.util.ArrayList<>();
        for (java.lang.String os : org.apache.ambari.server.stack.RepoUtilTest.OPERATING_SYSTEMS) {
            org.apache.ambari.server.orm.entities.RepoDefinitionEntity repo1 = org.apache.ambari.server.stack.RepoUtilTest.repoEntity("HDP", "HDP-2.3", "http://hdp.org/2.3");
            org.apache.ambari.server.orm.entities.RepoDefinitionEntity repo2 = org.apache.ambari.server.stack.RepoUtilTest.repoEntity("HDP-UTILS", "HDP-UTILS-1.1.0", "http://hdp.org/utils/1.1.0");
            org.apache.ambari.server.orm.entities.RepoDefinitionEntity repo3 = org.apache.ambari.server.stack.RepoUtilTest.repoEntity("MSFT_R", "MSFT_R-8.1", "http://msft.r.ORIGINAL");
            operatingSystems.add(org.apache.ambari.server.stack.RepoUtilTest.osEntity(os, repo1, repo2, repo3));
        }
        com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> serviceRepos = org.apache.ambari.server.stack.RepoUtilTest.serviceRepos(com.google.common.collect.ImmutableList.of("redhat6"), "MSFT_R", "MSFT_R-8.2", "http://msft.r.NEW");
        org.apache.ambari.server.stack.RepoUtil.addServiceReposToOperatingSystemEntities(operatingSystems, serviceRepos);
        for (org.apache.ambari.server.orm.entities.RepoOsEntity os : operatingSystems) {
            com.google.common.base.Optional<org.apache.ambari.server.orm.entities.RepoDefinitionEntity> msft_r_orig = org.apache.ambari.server.stack.RepoUtilTest.findRepoEntityById(os.getRepoDefinitionEntities(), "MSFT_R-8.1");
            com.google.common.base.Optional<org.apache.ambari.server.orm.entities.RepoDefinitionEntity> msft_r_new = org.apache.ambari.server.stack.RepoUtilTest.findRepoEntityById(os.getRepoDefinitionEntities(), "MSFT_R-8.2");
            org.junit.Assert.assertTrue("Original repo is missing", msft_r_orig.isPresent());
            org.junit.Assert.assertTrue("Service repo with duplicate name should not have been added", !msft_r_new.isPresent());
        }
    }

    @org.junit.Test
    public void testGetServiceRepos() {
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> vdfRepos = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.stack.RepoUtilTest.repoInfo("HDP", "HDP-2.3", "redhat6"), org.apache.ambari.server.stack.RepoUtilTest.repoInfo("HDP-UTILS", "HDP-UTILS-1.1.0.20", "redhat6"), org.apache.ambari.server.stack.RepoUtilTest.repoInfo("HDP", "HDP-2.3", "redhat5"), org.apache.ambari.server.stack.RepoUtilTest.repoInfo("HDP-UTILS", "HDP-UTILS-1.1.0.20", "redhat5"));
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> stackRepos = com.google.common.collect.Lists.newArrayList(vdfRepos);
        stackRepos.add(org.apache.ambari.server.stack.RepoUtilTest.repoInfo("MSFT_R", "MSFT_R-8.1", "redhat6"));
        com.google.common.collect.ImmutableListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> stackReposByOs = com.google.common.collect.Multimaps.index(stackRepos, org.apache.ambari.server.state.RepositoryInfo.GET_OSTYPE_FUNCTION);
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> serviceRepos = org.apache.ambari.server.stack.RepoUtil.getServiceRepos(vdfRepos, stackReposByOs);
        org.junit.Assert.assertEquals("Expected 1 service repo", 1, serviceRepos.size());
        org.junit.Assert.assertEquals("Expected MSFT_R service repo", "MSFT_R", serviceRepos.get(0).getRepoName());
    }

    @org.junit.Test
    public void testAsRepositoryResponses() {
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> repos = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.stack.RepoUtilTest.repoInfo("HDP", "HDP-2.3", "redhat6"), org.apache.ambari.server.stack.RepoUtilTest.repoInfo("HDP-UTILS", "HDP-UTILS-1.1.0.20", "redhat6"), org.apache.ambari.server.stack.RepoUtilTest.repoInfo("HDP", "HDP-2.3", "redhat5"), org.apache.ambari.server.stack.RepoUtilTest.repoInfo("HDP-UTILS", "HDP-UTILS-1.1.0.20", "redhat5"));
        java.util.List<org.apache.ambari.server.controller.RepositoryResponse> responses = org.apache.ambari.server.stack.RepoUtil.asResponses(repos, "HDP-2.3", "HDP", "2.3");
        org.junit.Assert.assertEquals("Wrong number of responses", repos.size(), responses.size());
        for (org.apache.ambari.server.controller.RepositoryResponse response : responses) {
            org.junit.Assert.assertEquals("Unexpected version definition id", "HDP-2.3", response.getVersionDefinitionId());
            org.junit.Assert.assertEquals("Unexpected stack name", "HDP", response.getStackName());
            org.junit.Assert.assertEquals("Unexpected stack version", "2.3", response.getStackVersion());
        }
    }

    private static com.google.common.base.Optional<org.apache.ambari.server.orm.entities.RepoDefinitionEntity> findRepoEntityById(java.lang.Iterable<org.apache.ambari.server.orm.entities.RepoDefinitionEntity> repos, java.lang.String repoId) {
        for (org.apache.ambari.server.orm.entities.RepoDefinitionEntity repo : repos)
            if (java.util.Objects.equals(repo.getRepoID(), repoId)) {
                return com.google.common.base.Optional.of(repo);
            }

        return com.google.common.base.Optional.absent();
    }

    private static org.apache.ambari.server.orm.entities.RepoOsEntity osEntity(java.lang.String os, org.apache.ambari.server.orm.entities.RepoDefinitionEntity... repoEntities) {
        org.apache.ambari.server.orm.entities.RepoOsEntity entity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        entity.setFamily(os);
        for (org.apache.ambari.server.orm.entities.RepoDefinitionEntity repo : repoEntities) {
            entity.addRepoDefinition(repo);
        }
        return entity;
    }

    private static org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoEntity(java.lang.String name, java.lang.String repoId, java.lang.String baseUrl) {
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repo = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repo.setRepoName(name);
        repo.setRepoID(repoId);
        repo.setBaseUrl(baseUrl);
        return repo;
    }

    private static org.apache.ambari.server.state.RepositoryInfo repoInfo(java.lang.String name, java.lang.String repoId, java.lang.String osType) {
        org.apache.ambari.server.state.RepositoryInfo repo = new org.apache.ambari.server.state.RepositoryInfo();
        repo.setRepoName(name);
        repo.setRepoId(repoId);
        repo.setOsType(osType);
        return repo;
    }

    private static com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> serviceRepos(java.util.List<java.lang.String> operatingSystems, java.lang.String repoName, java.lang.String repoId, java.lang.String baseUrl) {
        com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> multimap = com.google.common.collect.ArrayListMultimap.create();
        for (java.lang.String os : operatingSystems) {
            org.apache.ambari.server.state.RepositoryInfo repoInfo = new org.apache.ambari.server.state.RepositoryInfo();
            repoInfo.setOsType(os);
            repoInfo.setRepoId(repoId);
            repoInfo.setRepoName(repoName);
            repoInfo.setBaseUrl(baseUrl);
            multimap.put(os, repoInfo);
        }
        return multimap;
    }
}