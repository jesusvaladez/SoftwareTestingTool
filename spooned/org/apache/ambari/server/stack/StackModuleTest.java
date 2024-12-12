package org.apache.ambari.server.stack;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class StackModuleTest {
    @org.junit.Test
    public void stackServiceReposAreRead() throws java.lang.Exception {
        org.apache.ambari.server.stack.StackModule sm = org.apache.ambari.server.stack.StackModuleTest.createStackModule("FooBar", "2.4", com.google.common.base.Optional.of(com.google.common.collect.Lists.newArrayList(repoInfo("foo", "1.0.1", "http://foo.org"))), com.google.common.collect.Lists.newArrayList(repoInfo("bar", "2.0.1", "http://bar.org")));
        java.util.Set<java.lang.String> repoIds = getIds(sm.getModuleInfo().getRepositories());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("foo:1.0.1", "bar:2.0.1"), repoIds);
    }

    @org.junit.Test
    public void duplicateStackServiceReposAreDiscarded() throws java.lang.Exception {
        org.apache.ambari.server.stack.StackModule sm = org.apache.ambari.server.stack.StackModuleTest.createStackModule("FooBar", "2.4", com.google.common.base.Optional.of(com.google.common.collect.Lists.newArrayList(repoInfo("StackRepoA", "1.1.1", "http://repos.org/stackrepoA"), repoInfo("StackRepoB", "2.2.2", "http://repos.org/stackrepoB"))), com.google.common.collect.Lists.newArrayList(repoInfo("serviceRepoA", "1.0.0", "http://bar.org/1_0_0")), com.google.common.collect.Lists.newArrayList(repoInfo("serviceRepoA", "1.0.0", "http://bar.org/1_0_0")), com.google.common.collect.Lists.newArrayList(repoInfo("serviceRepoB", "1.2.1", "http://bar.org/1_1_1")), com.google.common.collect.Lists.newArrayList(repoInfo("serviceRepoB", "1.2.3", "http://bar.org/1_1_1")), com.google.common.collect.Lists.newArrayList(repoInfo("StackRepoA", "2.0.0", "http://repos.org/stackrepoA_200"), repoInfo("ShouldBeJustAWarning1", "3.1.1", "http://repos.org/stackrepoA"), repoInfo("ShouldBeJustAWarning2", "1.0.0", "http://bar.org/1_0_0")));
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> repos = sm.getModuleInfo().getRepositories();
        java.util.Set<java.lang.String> repoIds = getIds(repos);
        org.junit.Assert.assertEquals("Unexpected number of repos. Each repo should be added only once", repoIds.size(), repos.size());
        org.junit.Assert.assertEquals("Unexpected repositories", com.google.common.collect.ImmutableSet.of("StackRepoA:1.1.1", "StackRepoB:2.2.2", "serviceRepoA:1.0.0", "ShouldBeJustAWarning1:3.1.1", "ShouldBeJustAWarning2:1.0.0"), repoIds);
    }

    @org.junit.Test
    public void serviceReposAreProcessedEvenIfNoStackRepo() throws java.lang.Exception {
        org.apache.ambari.server.stack.StackModule sm = org.apache.ambari.server.stack.StackModuleTest.createStackModule("FooBar", "2.4", com.google.common.base.Optional.absent(), com.google.common.collect.Lists.newArrayList(repoInfo("bar", "2.0.1", "http://bar.org")));
        java.util.Set<java.lang.String> repoIds = getIds(sm.getModuleInfo().getRepositories());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("bar:2.0.1"), repoIds);
    }

    @org.junit.Test
    public void duplicateStackServiceReposAreCheckedPerOs() throws java.lang.Exception {
        org.apache.ambari.server.stack.StackModule sm = org.apache.ambari.server.stack.StackModuleTest.createStackModule("FooBar", "2.4", com.google.common.base.Optional.absent(), com.google.common.collect.Lists.newArrayList(repoInfo("bar", "2.0.1", "http://bar.org", "centos6")), com.google.common.collect.Lists.newArrayList(repoInfo("bar", "2.0.1", "http://bar.org", "centos7")));
        com.google.common.collect.Multiset<java.lang.String> repoIds = getIdsMultiple(sm.getModuleInfo().getRepositories());
        org.junit.Assert.assertEquals("Repo should be occur exactly twice, once for each os type.", com.google.common.collect.ImmutableMultiset.of("bar:2.0.1", "bar:2.0.1"), repoIds);
    }

    @org.junit.Test
    public void removedServicesInitialValue() throws java.lang.Exception {
        org.apache.ambari.server.stack.StackModule sm = org.apache.ambari.server.stack.StackModuleTest.createStackModule("FooBar", "2.4", com.google.common.base.Optional.absent(), com.google.common.collect.Lists.newArrayList(repoInfo("bar", "2.0.1", "http://bar.org", "centos6")), com.google.common.collect.Lists.newArrayList(repoInfo("bar", "2.0.1", "http://bar.org", "centos7")));
        java.util.List<java.lang.String> removedServices = sm.getModuleInfo().getRemovedServices();
        org.junit.Assert.assertEquals(removedServices.size(), 0);
    }

    @org.junit.Test
    public void servicesWithNoConfigsInitialValue() throws java.lang.Exception {
        org.apache.ambari.server.stack.StackModule sm = org.apache.ambari.server.stack.StackModuleTest.createStackModule("FooBar", "2.4", com.google.common.base.Optional.absent(), com.google.common.collect.Lists.newArrayList(repoInfo("bar", "2.0.1", "http://bar.org", "centos6")), com.google.common.collect.Lists.newArrayList(repoInfo("bar", "2.0.1", "http://bar.org", "centos7")));
        java.util.List<java.lang.String> servicesWithNoConfigs = sm.getModuleInfo().getServicesWithNoConfigs();
        org.junit.Assert.assertEquals(servicesWithNoConfigs.size(), 0);
    }

    @java.lang.SafeVarargs
    private static org.apache.ambari.server.stack.StackModule createStackModule(java.lang.String stackName, java.lang.String stackVersion, com.google.common.base.Optional<? extends java.util.List<org.apache.ambari.server.state.RepositoryInfo>> stackRepos, java.util.List<org.apache.ambari.server.state.RepositoryInfo>... serviceRepoLists) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.StackDirectory sd = Mockito.mock(org.apache.ambari.server.stack.StackDirectory.class);
        java.util.List<org.apache.ambari.server.stack.ServiceDirectory> serviceDirectories = com.google.common.collect.Lists.newArrayList();
        for (java.util.List<org.apache.ambari.server.state.RepositoryInfo> serviceRepoList : serviceRepoLists) {
            org.apache.ambari.server.stack.StackServiceDirectory svd = Mockito.mock(org.apache.ambari.server.stack.StackServiceDirectory.class);
            org.apache.ambari.server.state.stack.RepositoryXml serviceRepoXml = Mockito.mock(org.apache.ambari.server.state.stack.RepositoryXml.class);
            Mockito.when(svd.getRepoFile()).thenReturn(serviceRepoXml);
            Mockito.when(serviceRepoXml.getRepositories()).thenReturn(serviceRepoList);
            org.apache.ambari.server.state.stack.ServiceMetainfoXml serviceMetainfoXml = Mockito.mock(org.apache.ambari.server.state.stack.ServiceMetainfoXml.class);
            Mockito.when(serviceMetainfoXml.isValid()).thenReturn(true);
            org.apache.ambari.server.state.ServiceInfo serviceInfo = Mockito.mock(org.apache.ambari.server.state.ServiceInfo.class);
            Mockito.when(serviceInfo.isValid()).thenReturn(true);
            Mockito.when(serviceInfo.getName()).thenReturn(java.util.UUID.randomUUID().toString());
            Mockito.when(serviceMetainfoXml.getServices()).thenReturn(com.google.common.collect.Lists.newArrayList(serviceInfo));
            Mockito.when(svd.getMetaInfoFile()).thenReturn(serviceMetainfoXml);
            serviceDirectories.add(svd);
        }
        if (stackRepos.isPresent()) {
            org.apache.ambari.server.state.stack.RepositoryXml stackRepoXml = Mockito.mock(org.apache.ambari.server.state.stack.RepositoryXml.class);
            Mockito.when(sd.getRepoFile()).thenReturn(stackRepoXml);
            Mockito.when(stackRepoXml.getRepositories()).thenReturn(stackRepos.get());
        }
        Mockito.when(sd.getServiceDirectories()).thenReturn(serviceDirectories);
        Mockito.when(sd.getStackDirName()).thenReturn(stackName);
        Mockito.when(sd.getDirectory()).thenReturn(new java.io.File(stackVersion));
        org.apache.ambari.server.stack.StackContext ctx = Mockito.mock(org.apache.ambari.server.stack.StackContext.class);
        org.apache.ambari.server.stack.StackModule sm = new org.apache.ambari.server.stack.StackModule(sd, ctx);
        sm.resolve(null, com.google.common.collect.ImmutableMap.of(java.lang.String.format("%s:%s", stackName, stackVersion), sm), com.google.common.collect.ImmutableMap.of(), com.google.common.collect.ImmutableMap.of());
        return sm;
    }

    private org.apache.ambari.server.state.RepositoryInfo repoInfo(java.lang.String repoName, java.lang.String repoVersion, java.lang.String url) {
        return repoInfo(repoName, repoVersion, url, "centos6");
    }

    private java.util.List<org.apache.ambari.server.state.RepositoryInfo> repoInfosForAllOs(java.lang.String repoName, java.lang.String repoVersion, java.lang.String url) {
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> repos = new java.util.ArrayList<>(3);
        for (java.lang.String os : new java.lang.String[]{ "centos5", "centos6", "centos7" }) {
            repos.add(repoInfo(repoName, repoVersion, url, os));
        }
        return repos;
    }

    private org.apache.ambari.server.state.RepositoryInfo repoInfo(java.lang.String repoName, java.lang.String repoVersion, java.lang.String url, java.lang.String osType) {
        org.apache.ambari.server.state.RepositoryInfo info = new org.apache.ambari.server.state.RepositoryInfo();
        info.setRepoId(java.lang.String.format("%s:%s", repoName, repoVersion));
        info.setRepoName(repoName);
        info.setBaseUrl(url);
        info.setOsType(osType);
        return info;
    }

    private java.util.Set<java.lang.String> getIds(java.util.List<org.apache.ambari.server.state.RepositoryInfo> repoInfos) {
        return com.google.common.collect.ImmutableSet.copyOf(com.google.common.collect.Lists.transform(repoInfos, org.apache.ambari.server.state.RepositoryInfo.GET_REPO_ID_FUNCTION));
    }

    private com.google.common.collect.Multiset<java.lang.String> getIdsMultiple(java.util.List<org.apache.ambari.server.state.RepositoryInfo> repoInfos) {
        return com.google.common.collect.ImmutableMultiset.copyOf(com.google.common.collect.Lists.transform(repoInfos, org.apache.ambari.server.state.RepositoryInfo.GET_REPO_ID_FUNCTION));
    }
}