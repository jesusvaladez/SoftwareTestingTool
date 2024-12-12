package org.apache.ambari.server.orm.dao;
public class CrudDAOTest {
    private static com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.CrudDAO<org.apache.ambari.server.orm.entities.RepositoryVersionEntity, java.lang.Long> repositoryVersionDAO;

    private int uniqueCounter = 0;

    private static final long FIRST_ID = 1L;

    private static final org.apache.ambari.server.state.StackId HDP_206 = new org.apache.ambari.server.state.StackId("HDP", "2.0.6");

    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    @org.junit.Before
    public void before() {
        org.apache.ambari.server.orm.dao.CrudDAOTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.H2DatabaseCleaner.resetSequences(org.apache.ambari.server.orm.dao.CrudDAOTest.injector);
        org.apache.ambari.server.orm.dao.CrudDAOTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        stackDAO = org.apache.ambari.server.orm.dao.CrudDAOTest.injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        repositoryVersionDAO = org.apache.ambari.server.orm.dao.CrudDAOTest.injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.dao.CrudDAOTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
    }

    private void createSingleRecord() {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(org.apache.ambari.server.orm.dao.CrudDAOTest.HDP_206.getStackName(), org.apache.ambari.server.orm.dao.CrudDAOTest.HDP_206.getStackVersion());
        org.junit.Assert.assertNotNull(stackEntity);
        final org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        entity.setDisplayName("display name" + uniqueCounter);
        entity.addRepoOsEntities(new java.util.ArrayList<>());
        entity.setStack(stackEntity);
        entity.setVersion("version" + uniqueCounter);
        repositoryVersionDAO.create(entity);
        uniqueCounter++;
    }

    @org.junit.Test
    public void testFindByPK() {
        org.junit.Assert.assertNull(repositoryVersionDAO.findByPK(org.apache.ambari.server.orm.dao.CrudDAOTest.FIRST_ID));
        createSingleRecord();
        org.junit.Assert.assertNotNull(repositoryVersionDAO.findByPK(org.apache.ambari.server.orm.dao.CrudDAOTest.FIRST_ID));
    }

    @org.junit.Test
    public void testFindAll() {
        org.junit.Assert.assertEquals(0, repositoryVersionDAO.findAll().size());
        createSingleRecord();
        createSingleRecord();
        org.junit.Assert.assertEquals(2, repositoryVersionDAO.findAll().size());
        repositoryVersionDAO.remove(repositoryVersionDAO.findByPK(org.apache.ambari.server.orm.dao.CrudDAOTest.FIRST_ID));
        org.junit.Assert.assertEquals(1, repositoryVersionDAO.findAll().size());
    }

    @org.junit.Test
    public void testCreate() {
        createSingleRecord();
        org.junit.Assert.assertTrue(repositoryVersionDAO.findAll().size() == 1);
        createSingleRecord();
        org.junit.Assert.assertTrue(repositoryVersionDAO.findAll().size() == 2);
    }

    @org.junit.Test
    public void testMerge() {
        createSingleRecord();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = repositoryVersionDAO.findByPK(org.apache.ambari.server.orm.dao.CrudDAOTest.FIRST_ID);
        entity.setDisplayName("newname");
        repositoryVersionDAO.merge(entity);
        entity = repositoryVersionDAO.findByPK(org.apache.ambari.server.orm.dao.CrudDAOTest.FIRST_ID);
        org.junit.Assert.assertEquals("newname", entity.getDisplayName());
    }

    @org.junit.Test
    public void testRemove() {
        createSingleRecord();
        createSingleRecord();
        org.junit.Assert.assertEquals(2, repositoryVersionDAO.findAll().size());
        repositoryVersionDAO.remove(repositoryVersionDAO.findByPK(org.apache.ambari.server.orm.dao.CrudDAOTest.FIRST_ID));
        org.junit.Assert.assertEquals(1, repositoryVersionDAO.findAll().size());
        org.junit.Assert.assertNull(repositoryVersionDAO.findByPK(1L));
    }

    @org.junit.After
    public void after() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.orm.dao.CrudDAOTest.injector);
    }
}