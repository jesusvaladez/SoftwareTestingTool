package org.apache.ambari.server.checks;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.mockito.Mockito;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
public class ClusterCheckTest extends org.easymock.EasyMockSupport {
    @org.easymock.Mock
    private org.apache.ambari.server.state.Clusters clusters;

    @org.easymock.Mock
    private org.apache.ambari.server.state.repository.ClusterVersionSummary m_clusterVersionSummary;

    @org.easymock.Mock
    private org.apache.ambari.server.state.repository.VersionDefinitionXml m_vdfXml;

    private static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription m_description = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("Test Check", org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER, "Test Check", "Test Failure Reason");

    private org.apache.ambari.server.checks.MockCheckHelper m_mockCheckHelper = new org.apache.ambari.server.checks.MockCheckHelper();

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injectMocks(this);
    }

    @org.junit.Test
    public void testFormatEntityList() {
        org.apache.ambari.server.checks.ClusterCheck check = new org.apache.ambari.server.checks.ClusterCheckTest.TestCheckImpl(org.apache.ambari.spi.upgrade.UpgradeCheckType.HOST);
        junit.framework.Assert.assertEquals("", check.formatEntityList(null));
        final java.util.LinkedHashSet<java.lang.String> failedOn = new java.util.LinkedHashSet<>();
        junit.framework.Assert.assertEquals("", check.formatEntityList(failedOn));
        failedOn.add("host1");
        junit.framework.Assert.assertEquals("host1", check.formatEntityList(failedOn));
        failedOn.add("host2");
        junit.framework.Assert.assertEquals("host1 and host2", check.formatEntityList(failedOn));
        failedOn.add("host3");
        junit.framework.Assert.assertEquals("host1, host2 and host3", check.formatEntityList(failedOn));
        check = new org.apache.ambari.server.checks.ClusterCheckTest.TestCheckImpl(org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER);
        junit.framework.Assert.assertEquals("host1, host2 and host3", check.formatEntityList(failedOn));
        check = new org.apache.ambari.server.checks.ClusterCheckTest.TestCheckImpl(org.apache.ambari.spi.upgrade.UpgradeCheckType.SERVICE);
        junit.framework.Assert.assertEquals("host1, host2 and host3", check.formatEntityList(failedOn));
        check = new org.apache.ambari.server.checks.ClusterCheckTest.TestCheckImpl(null);
        junit.framework.Assert.assertEquals("host1, host2 and host3", check.formatEntityList(failedOn));
    }

    @org.junit.Test
    public void testIsApplicable() throws java.lang.Exception {
        final java.lang.String clusterName = "c1";
        final org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
            {
                put("SERVICE1", null);
                put("SERVICE2", null);
                put("SERVICE3", null);
            }
        };
        java.util.Set<java.lang.String> oneServiceList = com.google.common.collect.Sets.newHashSet("SERVICE1");
        java.util.Set<java.lang.String> atLeastOneServiceList = com.google.common.collect.Sets.newHashSet("SERVICE1", "MISSING_SERVICE");
        java.util.Set<java.lang.String> allServicesList = com.google.common.collect.Sets.newHashSet("SERVICE1", "SERVICE2");
        java.util.Set<java.lang.String> missingServiceList = com.google.common.collect.Sets.newHashSet("MISSING_SERVICE");
        EasyMock.expect(clusters.getCluster(EasyMock.anyString())).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getServices()).andReturn(services).atLeastOnce();
        org.apache.ambari.spi.RepositoryVersion repositoryVersion = createNiceMock(org.apache.ambari.spi.RepositoryVersion.class);
        EasyMock.expect(repositoryVersion.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(repositoryVersion.getRepositoryType()).andReturn(org.apache.ambari.spi.RepositoryType.STANDARD).anyTimes();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repositoryVersionEntity.getType()).andReturn(org.apache.ambari.spi.RepositoryType.STANDARD).anyTimes();
        EasyMock.expect(repositoryVersionEntity.getRepositoryXml()).andReturn(m_vdfXml).atLeastOnce();
        EasyMock.expect(m_vdfXml.getClusterSummary(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.api.services.AmbariMetaInfo.class))).andReturn(m_clusterVersionSummary).atLeastOnce();
        EasyMock.expect(m_clusterVersionSummary.getAvailableServiceNames()).andReturn(allServicesList).atLeastOnce();
        final org.apache.ambari.server.api.services.AmbariMetaInfo ami = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        m_mockCheckHelper.setMetaInfoProvider(new com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo>() {
            @java.lang.Override
            public org.apache.ambari.server.api.services.AmbariMetaInfo get() {
                return ami;
            }
        });
        org.mockito.Mockito.when(m_mockCheckHelper.m_repositoryVersionDAO.findByPK(org.mockito.Mockito.anyLong())).thenReturn(repositoryVersionEntity);
        replayAll();
        org.apache.ambari.server.checks.ClusterCheckTest.TestCheckImpl check = new org.apache.ambari.server.checks.ClusterCheckTest.TestCheckImpl(org.apache.ambari.spi.upgrade.UpgradeCheckType.SERVICE);
        check.checkHelperProvider = new com.google.inject.Provider<org.apache.ambari.server.state.CheckHelper>() {
            @java.lang.Override
            public org.apache.ambari.server.state.CheckHelper get() {
                return m_mockCheckHelper;
            }
        };
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(clusterName, false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion, null, null);
        check.setApplicableServices(oneServiceList);
        junit.framework.Assert.assertTrue(m_mockCheckHelper.getApplicableChecks(request, com.google.common.collect.Lists.newArrayList(check)).size() == 1);
        check.setApplicableServices(atLeastOneServiceList);
        junit.framework.Assert.assertTrue(m_mockCheckHelper.getApplicableChecks(request, com.google.common.collect.Lists.newArrayList(check)).size() == 1);
        check.setApplicableServices(missingServiceList);
        junit.framework.Assert.assertTrue(m_mockCheckHelper.getApplicableChecks(request, com.google.common.collect.Lists.newArrayList(check)).size() == 0);
    }

    @org.junit.Test
    public void testIsApplicableForPatch() throws java.lang.Exception {
        final java.lang.String clusterName = "c1";
        final org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Service>() {
            {
                put("SERVICE1", null);
                put("SERVICE2", null);
                put("SERVICE3", null);
            }
        };
        java.util.Set<java.lang.String> oneServiceList = com.google.common.collect.Sets.newHashSet("SERVICE1");
        EasyMock.expect(clusters.getCluster(EasyMock.anyString())).andReturn(cluster).atLeastOnce();
        EasyMock.expect(cluster.getServices()).andReturn(services).atLeastOnce();
        org.apache.ambari.spi.RepositoryVersion repositoryVersion = createNiceMock(org.apache.ambari.spi.RepositoryVersion.class);
        EasyMock.expect(repositoryVersion.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(repositoryVersion.getRepositoryType()).andReturn(org.apache.ambari.spi.RepositoryType.STANDARD).anyTimes();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repositoryVersionEntity.getType()).andReturn(org.apache.ambari.spi.RepositoryType.STANDARD).anyTimes();
        EasyMock.expect(repositoryVersionEntity.getRepositoryXml()).andReturn(m_vdfXml).atLeastOnce();
        EasyMock.expect(m_vdfXml.getClusterSummary(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.api.services.AmbariMetaInfo.class))).andReturn(m_clusterVersionSummary).atLeastOnce();
        EasyMock.expect(m_clusterVersionSummary.getAvailableServiceNames()).andReturn(oneServiceList).atLeastOnce();
        m_mockCheckHelper.m_clusters = clusters;
        org.mockito.Mockito.when(m_mockCheckHelper.m_repositoryVersionDAO.findByPK(org.mockito.Mockito.anyLong())).thenReturn(repositoryVersionEntity);
        final org.apache.ambari.server.api.services.AmbariMetaInfo ami = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        m_mockCheckHelper.setMetaInfoProvider(new com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo>() {
            @java.lang.Override
            public org.apache.ambari.server.api.services.AmbariMetaInfo get() {
                return ami;
            }
        });
        replayAll();
        org.apache.ambari.server.checks.ClusterCheckTest.TestCheckImpl check = new org.apache.ambari.server.checks.ClusterCheckTest.TestCheckImpl(org.apache.ambari.spi.upgrade.UpgradeCheckType.SERVICE);
        check.checkHelperProvider = new com.google.inject.Provider<org.apache.ambari.server.state.CheckHelper>() {
            @java.lang.Override
            public org.apache.ambari.server.state.CheckHelper get() {
                return m_mockCheckHelper;
            }
        };
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(clusterName, false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion, null, null);
        check.setApplicableServices(com.google.common.collect.Sets.newHashSet("SERVICE2"));
        junit.framework.Assert.assertTrue(m_mockCheckHelper.getApplicableChecks(request, com.google.common.collect.Lists.newArrayList(check)).size() == 0);
        check.setApplicableServices(com.google.common.collect.Sets.newHashSet("SERVICE1"));
        junit.framework.Assert.assertTrue(m_mockCheckHelper.getApplicableChecks(request, com.google.common.collect.Lists.newArrayList(check)).size() == 1);
    }

    @org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT, order = 1.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
    private class TestCheckImpl extends org.apache.ambari.server.checks.ClusterCheck {
        private org.apache.ambari.spi.upgrade.UpgradeCheckType m_type;

        private java.util.Set<java.lang.String> m_applicableServices = com.google.common.collect.Sets.newHashSet();

        TestCheckImpl(org.apache.ambari.spi.upgrade.UpgradeCheckType type) {
            super(null);
            m_type = type;
            clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
                @java.lang.Override
                public org.apache.ambari.server.state.Clusters get() {
                    return clusters;
                }
            };
        }

        @java.lang.Override
        public org.apache.ambari.spi.upgrade.UpgradeCheckDescription getCheckDescription() {
            return org.apache.ambari.server.checks.ClusterCheckTest.m_description;
        }

        @java.lang.Override
        public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
            return new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> getApplicableServices() {
            return m_applicableServices;
        }

        void setApplicableServices(java.util.Set<java.lang.String> applicableServices) {
            m_applicableServices = applicableServices;
        }
    }

    @org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT, order = 1.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING })
    private class RollingTestCheckImpl extends org.apache.ambari.server.checks.ClusterCheck {
        private org.apache.ambari.spi.upgrade.UpgradeCheckType m_type;

        RollingTestCheckImpl(org.apache.ambari.spi.upgrade.UpgradeCheckType type) {
            super(null);
            m_type = type;
            clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
                @java.lang.Override
                public org.apache.ambari.server.state.Clusters get() {
                    return clusters;
                }
            };
        }

        @java.lang.Override
        public org.apache.ambari.spi.upgrade.UpgradeCheckDescription getCheckDescription() {
            return org.apache.ambari.server.checks.ClusterCheckTest.m_description;
        }

        @java.lang.Override
        public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
            return new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        }
    }

    @org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT, order = 1.0F)
    private class NotRequiredCheckTest extends org.apache.ambari.server.checks.ClusterCheck {
        private org.apache.ambari.spi.upgrade.UpgradeCheckType m_type;

        NotRequiredCheckTest(org.apache.ambari.spi.upgrade.UpgradeCheckType type) {
            super(null);
            m_type = type;
            clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
                @java.lang.Override
                public org.apache.ambari.server.state.Clusters get() {
                    return clusters;
                }
            };
        }

        @java.lang.Override
        public org.apache.ambari.spi.upgrade.UpgradeCheckDescription getCheckDescription() {
            return org.apache.ambari.server.checks.ClusterCheckTest.m_description;
        }

        @java.lang.Override
        public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
            return new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        }
    }
}