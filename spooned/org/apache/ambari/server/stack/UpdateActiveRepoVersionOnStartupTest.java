package org.apache.ambari.server.stack;
import org.mockito.Mockito;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
public class UpdateActiveRepoVersionOnStartupTest {
    private static java.lang.String CLUSTER_NAME = "c1";

    private static java.lang.String ADD_ON_REPO_ID = "MSFT_R-8.0";

    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDao;

    private org.apache.ambari.server.stack.UpdateActiveRepoVersionOnStartup activeRepoUpdater;

    @org.junit.Test
    public void addAServiceRepoToExistingRepoVersion() throws java.lang.Exception {
        init(true);
        activeRepoUpdater.process();
        verifyRepoIsAdded();
    }

    @org.junit.Test
    public void missingClusterVersionShouldNotCauseException() throws java.lang.Exception {
        init(false);
        activeRepoUpdater.process();
    }

    private void verifyRepoIsAdded() throws java.lang.Exception {
        Mockito.verify(repositoryVersionDao, Mockito.atLeast(1)).merge(org.mockito.Mockito.any(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class));
    }

    public void init(boolean addClusterVersion) throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDao = Mockito.mock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        repositoryVersionDao = Mockito.mock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        final org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repositoryVersionHelper = new org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper();
        java.lang.reflect.Field field = org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper.class.getDeclaredField("gson");
        field.setAccessible(true);
        field.set(repositoryVersionHelper, new com.google.gson.Gson());
        final org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.stack.StackManager stackManager = Mockito.mock(org.apache.ambari.server.stack.StackManager.class);
        Mockito.when(metaInfo.getStackManager()).thenReturn(stackManager);
        org.apache.ambari.server.orm.entities.ClusterEntity cluster = new org.apache.ambari.server.orm.entities.ClusterEntity();
        cluster.setClusterName(org.apache.ambari.server.stack.UpdateActiveRepoVersionOnStartupTest.CLUSTER_NAME);
        Mockito.when(clusterDao.findAll()).thenReturn(com.google.common.collect.ImmutableList.of(cluster));
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
        stackEntity.setStackName("HDP");
        stackEntity.setStackVersion("2.3");
        cluster.setDesiredStack(stackEntity);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        desiredRepositoryVersion.setStack(stackEntity);
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystems = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity1 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repoDefinitionEntity1.setRepoID("HDP-UTILS-1.1.0.20");
        repoDefinitionEntity1.setBaseUrl("http://192.168.99.100/repos/HDP-UTILS-1.1.0.20/");
        repoDefinitionEntity1.setRepoName("HDP-UTILS");
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity2 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repoDefinitionEntity2.setRepoID("HDP-2.4");
        repoDefinitionEntity2.setBaseUrl("http://192.168.99.100/repos/HDP-2.4.0.0/");
        repoDefinitionEntity2.setRepoName("HDP");
        org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity1 = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        repoOsEntity1.setFamily("redhat6");
        repoOsEntity1.setAmbariManaged(true);
        repoOsEntity1.addRepoDefinition(repoDefinitionEntity1);
        repoOsEntity1.addRepoDefinition(repoDefinitionEntity2);
        operatingSystems.add(repoOsEntity1);
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity3 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repoDefinitionEntity3.setRepoID("HDP-UTILS-1.1.0.20");
        repoDefinitionEntity3.setBaseUrl("http://s3.amazonaws.com/dev.hortonworks.com/HDP-UTILS-1.1.0.20/repos/centos7");
        repoDefinitionEntity3.setRepoName("HDP-UTILS");
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity4 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repoDefinitionEntity4.setRepoID("HDP-2.4");
        repoDefinitionEntity4.setBaseUrl("http://s3.amazonaws.com/dev.hortonworks.com/HDP/centos7/2.x/BUILDS/2.4.3.0-207");
        repoDefinitionEntity4.setRepoName("HDP");
        org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity2 = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        repoOsEntity2.setFamily("redhat7");
        repoOsEntity2.setAmbariManaged(true);
        repoOsEntity2.addRepoDefinition(repoDefinitionEntity3);
        repoOsEntity2.addRepoDefinition(repoDefinitionEntity4);
        operatingSystems.add(repoOsEntity2);
        desiredRepositoryVersion.addRepoOsEntities(operatingSystems);
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity = new org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity();
        serviceDesiredStateEntity.setDesiredRepositoryVersion(desiredRepositoryVersion);
        org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity = new org.apache.ambari.server.orm.entities.ClusterServiceEntity();
        clusterServiceEntity.setServiceDesiredStateEntity(serviceDesiredStateEntity);
        cluster.setClusterServiceEntities(java.util.Collections.singletonList(clusterServiceEntity));
        org.apache.ambari.server.state.StackInfo stackInfo = new org.apache.ambari.server.state.StackInfo();
        stackInfo.setName("HDP");
        stackInfo.setVersion("2.3");
        org.apache.ambari.server.state.RepositoryInfo repositoryInfo = new org.apache.ambari.server.state.RepositoryInfo();
        repositoryInfo.setBaseUrl("http://msft.r");
        repositoryInfo.setRepoId(org.apache.ambari.server.stack.UpdateActiveRepoVersionOnStartupTest.ADD_ON_REPO_ID);
        repositoryInfo.setRepoName("MSFT_R");
        repositoryInfo.setOsType("redhat6");
        stackInfo.getRepositories().add(repositoryInfo);
        Mockito.when(stackManager.getStack("HDP", "2.3")).thenReturn(stackInfo);
        final com.google.inject.Provider<org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper> repositoryVersionHelperProvider = Mockito.mock(com.google.inject.Provider.class);
        Mockito.when(repositoryVersionHelperProvider.get()).thenReturn(repositoryVersionHelper);
        org.apache.ambari.server.orm.InMemoryDefaultTestModule testModule = new org.apache.ambari.server.orm.InMemoryDefaultTestModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper.class).toProvider(repositoryVersionHelperProvider);
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toProvider(new com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo>() {
                    @java.lang.Override
                    public org.apache.ambari.server.api.services.AmbariMetaInfo get() {
                        return metaInfo;
                    }
                });
                requestStaticInjection(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
            }
        };
        com.google.inject.Guice.createInjector(testModule);
        if (addClusterVersion) {
            org.apache.ambari.server.state.RepositoryInfo info = new org.apache.ambari.server.state.RepositoryInfo();
            info.setBaseUrl("http://msft.r");
            info.setRepoId(org.apache.ambari.server.stack.UpdateActiveRepoVersionOnStartupTest.ADD_ON_REPO_ID);
            info.setRepoName("MSFT_R1");
            info.setOsType("redhat6");
            stackInfo.getRepositories().add(info);
        }
        activeRepoUpdater = new org.apache.ambari.server.stack.UpdateActiveRepoVersionOnStartup(clusterDao, repositoryVersionDao, repositoryVersionHelper, metaInfo);
    }

    private static java.lang.String resourceAsString(java.lang.String resourceName) throws java.io.IOException {
        return com.google.common.io.Resources.toString(com.google.common.io.Resources.getResource(resourceName), com.google.common.base.Charsets.UTF_8);
    }
}