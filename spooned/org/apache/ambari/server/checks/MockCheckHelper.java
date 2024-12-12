package org.apache.ambari.server.checks;
import org.mockito.Mockito;
public class MockCheckHelper extends org.apache.ambari.server.state.CheckHelper {
    public org.apache.ambari.server.orm.dao.RepositoryVersionDAO m_repositoryVersionDAO = org.mockito.Mockito.mock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);

    public org.apache.ambari.server.state.Clusters m_clusters = org.mockito.Mockito.mock(org.apache.ambari.server.state.Clusters.class);

    public MockCheckHelper() {
        clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return m_clusters;
            }
        };
        repositoryVersionDaoProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.RepositoryVersionDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.RepositoryVersionDAO get() {
                return m_repositoryVersionDAO;
            }
        };
    }

    public void setMetaInfoProvider(com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> provider) {
        metaInfoProvider = provider;
    }
}