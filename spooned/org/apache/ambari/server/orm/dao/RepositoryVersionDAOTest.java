package org.apache.ambari.server.orm.dao;
public class RepositoryVersionDAOTest {
    private static com.google.inject.Injector injector;

    private static final org.apache.ambari.server.state.StackId HDP_206 = new org.apache.ambari.server.state.StackId("HDP", "2.0.6");

    private static final org.apache.ambari.server.state.StackId OTHER_10 = new org.apache.ambari.server.state.StackId("OTHER", "1.0");

    private static final org.apache.ambari.server.state.StackId BAD_STACK = new org.apache.ambari.server.state.StackId("BADSTACK", "1.0");

    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    @org.junit.Before
    public void before() {
        org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        repositoryVersionDAO = org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        stackDAO = org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
    }

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity createSingleRecord() {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.HDP_206.getStackName(), org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.HDP_206.getStackVersion());
        org.junit.Assert.assertNotNull(stackEntity);
        final org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        entity.setDisplayName("display name");
        entity.addRepoOsEntities(new java.util.ArrayList<>());
        entity.setStack(stackEntity);
        entity.setVersion("version");
        repositoryVersionDAO.create(entity);
        return entity;
    }

    @org.junit.Test
    public void testCreate() {
        java.util.UUID uuid = java.util.UUID.randomUUID();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity first = createSingleRecord();
        org.junit.Assert.assertNotNull(first);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(first.getStackName(), first.getStackVersion());
        org.junit.Assert.assertNotNull(stackEntity);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity dupVersion = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        dupVersion.setDisplayName("display name " + uuid);
        dupVersion.addRepoOsEntities(new java.util.ArrayList<>());
        dupVersion.setStack(stackEntity);
        dupVersion.setVersion(first.getVersion());
        boolean exceptionThrown = false;
        try {
            repositoryVersionDAO.create(stackEntity, dupVersion.getVersion(), dupVersion.getDisplayName(), dupVersion.getRepoOsEntities());
        } catch (org.apache.ambari.server.AmbariException e) {
            exceptionThrown = true;
            org.junit.Assert.assertTrue(e.getMessage().contains("already exists"));
        }
        org.junit.Assert.assertTrue(exceptionThrown);
        exceptionThrown = false;
        dupVersion.setVersion("2.3-1234");
        try {
            repositoryVersionDAO.create(stackEntity, dupVersion.getVersion(), dupVersion.getDisplayName(), dupVersion.getRepoOsEntities());
        } catch (org.apache.ambari.server.AmbariException e) {
            exceptionThrown = true;
            org.junit.Assert.assertTrue(e.getMessage().contains("needs to belong to stack"));
        }
        org.junit.Assert.assertTrue(exceptionThrown);
        dupVersion.setVersion(stackEntity.getStackVersion() + "-1234");
        try {
            repositoryVersionDAO.create(stackEntity, dupVersion.getVersion(), dupVersion.getDisplayName(), dupVersion.getRepoOsEntities());
        } catch (org.apache.ambari.server.AmbariException e) {
            org.junit.Assert.fail("Did not expect a failure creating the Repository Version");
        }
    }

    @org.junit.Test
    public void testFindByDisplayName() {
        createSingleRecord();
        org.junit.Assert.assertNull(repositoryVersionDAO.findByDisplayName("non existing"));
        org.junit.Assert.assertNotNull(repositoryVersionDAO.findByDisplayName("display name"));
    }

    @org.junit.Test
    public void testFindByStackAndVersion() {
        createSingleRecord();
        org.junit.Assert.assertNull(repositoryVersionDAO.findByStackAndVersion(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.BAD_STACK, "non existing"));
        org.junit.Assert.assertNotNull(repositoryVersionDAO.findByStackAndVersion(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.HDP_206, "version"));
    }

    @org.junit.Test
    public void testFindByStack() {
        createSingleRecord();
        org.junit.Assert.assertEquals(0, repositoryVersionDAO.findByStack(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.BAD_STACK).size());
        org.junit.Assert.assertEquals(1, repositoryVersionDAO.findByStack(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.HDP_206).size());
    }

    @org.junit.Test
    public void testDelete() {
        createSingleRecord();
        org.junit.Assert.assertNotNull(repositoryVersionDAO.findByStackAndVersion(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.HDP_206, "version"));
        final org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = repositoryVersionDAO.findByStackAndVersion(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.HDP_206, "version");
        repositoryVersionDAO.remove(entity);
        org.junit.Assert.assertNull(repositoryVersionDAO.findByStackAndVersion(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.HDP_206, "version"));
    }

    @org.junit.Test
    public void testRemovePrefixFromVersion() {
        org.apache.ambari.server.orm.entities.StackEntity hdp206StackEntity = stackDAO.find(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.HDP_206.getStackName(), org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.HDP_206.getStackVersion());
        org.junit.Assert.assertNotNull(hdp206StackEntity);
        final org.apache.ambari.server.orm.entities.RepositoryVersionEntity hdp206RepoEntity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        hdp206RepoEntity.setDisplayName("HDP-2.0.6.0-1234");
        hdp206RepoEntity.addRepoOsEntities(new java.util.ArrayList<>());
        hdp206RepoEntity.setStack(hdp206StackEntity);
        hdp206RepoEntity.setVersion("HDP-2.0.6.0-1234");
        repositoryVersionDAO.create(hdp206RepoEntity);
        org.junit.Assert.assertEquals("Failed to remove HDP stack prefix from version", "2.0.6.0-1234", hdp206RepoEntity.getVersion());
        org.junit.Assert.assertNotNull(repositoryVersionDAO.findByDisplayName("HDP-2.0.6.0-1234"));
        org.junit.Assert.assertNotNull(repositoryVersionDAO.findByStackAndVersion(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.HDP_206, "2.0.6.0-1234"));
        org.apache.ambari.server.orm.entities.StackEntity other10StackEntity = stackDAO.find(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.OTHER_10.getStackName(), org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.OTHER_10.getStackVersion());
        org.junit.Assert.assertNotNull(other10StackEntity);
        final org.apache.ambari.server.orm.entities.RepositoryVersionEntity other10RepoEntity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        other10RepoEntity.setDisplayName("OTHER-1.0.1.0-1234");
        other10RepoEntity.addRepoOsEntities(new java.util.ArrayList<>());
        other10RepoEntity.setStack(other10StackEntity);
        other10RepoEntity.setVersion("OTHER-1.0.1.0-1234");
        repositoryVersionDAO.create(other10RepoEntity);
        org.junit.Assert.assertEquals("Failed to remove OTHER stack prefix from version", "1.0.1.0-1234", other10RepoEntity.getVersion());
        org.junit.Assert.assertNotNull(repositoryVersionDAO.findByDisplayName("OTHER-1.0.1.0-1234"));
        org.junit.Assert.assertNotNull(repositoryVersionDAO.findByStackAndVersion(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.OTHER_10, "1.0.1.0-1234"));
    }

    @org.junit.Test
    public void testFindByStackAndType() {
        createSingleRecord();
        org.junit.Assert.assertEquals(1, repositoryVersionDAO.findByStackAndType(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.HDP_206, org.apache.ambari.spi.RepositoryType.STANDARD).size());
        org.junit.Assert.assertEquals(0, repositoryVersionDAO.findByStackAndType(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.HDP_206, org.apache.ambari.spi.RepositoryType.MAINT).size());
    }

    @org.junit.After
    public void after() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.injector);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAOTest.injector = null;
    }
}