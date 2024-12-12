package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.UnitOfWork;
public class MpackDAOTest {
    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.orm.dao.MpackDAO m_dao;

    @org.junit.Before
    public void init() {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).begin();
        m_dao = m_injector.getInstance(org.apache.ambari.server.orm.dao.MpackDAO.class);
    }

    @org.junit.Test
    public void testCreateFind() {
        java.util.List<org.apache.ambari.server.orm.entities.MpackEntity> eDefinitions = new java.util.ArrayList<>();
        for (int i = 1; i < 3; i++) {
            org.apache.ambari.server.orm.entities.MpackEntity definition = new org.apache.ambari.server.orm.entities.MpackEntity();
            definition.setId(new java.lang.Long(100) + i);
            definition.setMpackName("testMpack" + i);
            definition.setRegistryId(java.lang.Long.valueOf(i));
            definition.setMpackVersion("3.0.0.0-12" + i);
            definition.setMpackUri(("http://c6401.ambari.apache.org:8080/resources/mpacks-repo/testMpack" + i) + "-3.0.0.0-123.tar.gz");
            eDefinitions.add(definition);
            m_dao.create(definition);
        }
        java.util.List<org.apache.ambari.server.orm.entities.MpackEntity> definitions = m_dao.findAll();
        org.junit.Assert.assertNotNull(definitions);
        org.junit.Assert.assertEquals(2, definitions.size());
        definitions = m_dao.findByNameVersion("testMpack1", "3.0.0.0-121");
        org.junit.Assert.assertEquals(1, definitions.size());
        org.junit.Assert.assertEquals(new java.lang.Long(101), ((java.lang.Long) (definitions.get(0).getId())));
        org.apache.ambari.server.orm.entities.MpackEntity entity = m_dao.findById(new java.lang.Long(102));
        org.junit.Assert.assertEquals(entity.getMpackName(), "testMpack2");
        org.junit.Assert.assertEquals(entity.getMpackVersion(), "3.0.0.0-122");
    }
}