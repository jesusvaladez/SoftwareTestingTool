package org.apache.ambari.server.orm.dao;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
public class KerberosDescriptorDAOTest {
    public static final java.lang.String TEST_KERBEROS_DESCRIPTOR_ENTITY_NAME = "test-kerberos-descriptor-entity";

    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private javax.persistence.EntityManager entityManager;

    @org.easymock.TestSubject
    private org.apache.ambari.server.orm.dao.KerberosDescriptorDAO kerberosDescriptorDAO = new org.apache.ambari.server.orm.dao.KerberosDescriptorDAO();

    @org.junit.Before
    public void before() {
        EasyMock.reset(entityManagerProvider);
        EasyMock.expect(entityManagerProvider.get()).andReturn(entityManager).atLeastOnce();
        EasyMock.replay(entityManagerProvider);
    }

    @org.junit.Test
    public void testPersistNewKerberosDescriptorEntity() {
        org.apache.ambari.server.orm.entities.KerberosDescriptorEntity kerberosDescriptorEntity = new org.apache.ambari.server.orm.entities.KerberosDescriptorEntity();
        kerberosDescriptorEntity.setName(org.apache.ambari.server.orm.dao.KerberosDescriptorDAOTest.TEST_KERBEROS_DESCRIPTOR_ENTITY_NAME);
        org.easymock.Capture<org.apache.ambari.server.orm.entities.KerberosDescriptorEntity> capturedArgument = org.easymock.EasyMock.newCapture();
        entityManager.persist(EasyMock.capture(capturedArgument));
        EasyMock.replay(entityManager);
        kerberosDescriptorDAO.create(kerberosDescriptorEntity);
        org.junit.Assert.assertNotNull(capturedArgument);
        org.junit.Assert.assertEquals("The persisted object is not the expected one", kerberosDescriptorEntity, capturedArgument.getValue());
    }
}