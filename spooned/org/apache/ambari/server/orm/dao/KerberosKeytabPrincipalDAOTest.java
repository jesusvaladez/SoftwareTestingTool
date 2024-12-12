package org.apache.ambari.server.orm.dao;
import javax.persistence.EntityManager;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
public class KerberosKeytabPrincipalDAOTest {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private javax.persistence.EntityManager entityManager;

    @org.easymock.TestSubject
    private final org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO = new org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO();

    @org.junit.Before
    public void before() {
        EasyMock.reset(entityManagerProvider);
        EasyMock.expect(entityManagerProvider.get()).andReturn(entityManager).atLeastOnce();
        EasyMock.replay(entityManagerProvider);
    }

    @org.junit.Test
    public void testFindOrCreate() {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity.setHostName("h1");
        hostEntity.setHostId(1L);
        org.apache.ambari.server.orm.entities.KerberosKeytabEntity kke = new org.apache.ambari.server.orm.entities.KerberosKeytabEntity();
        kke.setKeytabPath("/some/path");
        org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kpe = new org.apache.ambari.server.orm.entities.KerberosPrincipalEntity();
        kpe.setPrincipalName("test@EXAMPLE.COM");
        java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> keytabList = new java.util.ArrayList<>();
        keytabList.add(null);
        kerberosKeytabPrincipalDAO.findOrCreate(kke, hostEntity, kpe, keytabList);
    }
}