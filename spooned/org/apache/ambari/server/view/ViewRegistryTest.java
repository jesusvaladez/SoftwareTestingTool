package org.apache.ambari.server.view;
import javax.xml.bind.JAXBException;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class ViewRegistryTest {
    private static final java.lang.String VIEW_XML_1 = "<view>\n" + ((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "</view>");

    private static final java.lang.String VIEW_XML_2 = "<view>\n" + ((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>2.0.0</version>\n") + "</view>");

    private static final java.lang.String XML_VALID_INSTANCE = "<view>\n" + (((((((((((((((((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <parameter>\n") + "        <name>p1</name>\n") + "        <description>Parameter 1.</description>\n") + "        <required>true</required>\n") + "    </parameter>\n") + "    <parameter>\n") + "        <name>p2</name>\n") + "        <description>Parameter 2.</description>\n") + "        <masked>true</masked>") + "        <required>false</required>\n") + "    </parameter>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "        <label>My Instance 1!</label>\n") + "        <property>\n") + "            <key>p1</key>\n") + "            <value>v1-1</value>\n") + "        </property>\n") + "        <property>\n") + "            <key>p2</key>\n") + "            <value>v2-1</value>\n") + "        </property>\n") + "    </instance>\n") + "</view>");

    private static final java.lang.String XML_INVALID_INSTANCE = "<view>\n" + ((((((((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <parameter>\n") + "        <name>p1</name>\n") + "        <description>Parameter 1.</description>\n") + "        <required>true</required>\n") + "    </parameter>\n") + "    <parameter>\n") + "        <name>p2</name>\n") + "        <description>Parameter 2.</description>\n") + "        <required>false</required>\n") + "    </parameter>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "        <label>My Instance 1!</label>\n") + "    </instance>\n") + "</view>");

    private static final java.lang.String AUTO_VIEW_XML = "<view>\n" + (((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <auto-instance>\n") + "        <name>AUTO-INSTANCE</name>\n") + "        <stack-id>HDP-2.0</stack-id>\n") + "        <services><service>HIVE</service><service>HDFS</service></services>\n") + "    </auto-instance>\n") + "</view>");

    private static final java.lang.String AUTO_VIEW_WILD_STACK_XML = "<view>\n" + (((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <auto-instance>\n") + "        <name>AUTO-INSTANCE</name>\n") + "        <stack-id>HDP-2.*</stack-id>\n") + "        <services><service>HIVE</service><service>HDFS</service></services>\n") + "    </auto-instance>\n") + "</view>");

    private static final java.lang.String AUTO_VIEW_WILD_ALL_STACKS_XML = "<view>\n" + (((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <auto-instance>\n") + "        <name>AUTO-INSTANCE</name>\n") + "        <stack-id>*</stack-id>\n") + "        <services><service>HIVE</service><service>HDFS</service></services>\n") + "    </auto-instance>\n") + "</view>");

    private static final java.lang.String AUTO_VIEW_BAD_STACK_XML = "<view>\n" + (((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <auto-instance>\n") + "        <name>AUTO-INSTANCE</name>\n") + "        <stack-id>HDP-2.5</stack-id>\n") + "        <services><service>HIVE</service><service>HDFS</service></services>\n") + "    </auto-instance>\n") + "</view>");

    private static final java.lang.String EXPECTED_HDP_2_0_STACK_NAME = "HDP-2.0";

    private static final org.apache.ambari.server.orm.dao.ViewDAO viewDAO = EasyMock.createMock(org.apache.ambari.server.orm.dao.ViewDAO.class);

    private static final org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);

    private static final org.apache.ambari.server.view.ViewInstanceOperationHandler viewInstanceOperationHandler = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewInstanceOperationHandler.class);

    private static final org.apache.ambari.server.orm.dao.UserDAO userDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.UserDAO.class);

    private static final org.apache.ambari.server.orm.dao.MemberDAO memberDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.MemberDAO.class);

    private static final org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);

    private static final org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.PermissionDAO.class);

    private static final org.apache.ambari.server.orm.dao.ResourceDAO resourceDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ResourceDAO.class);

    private static final org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ResourceTypeDAO.class);

    private static final org.apache.ambari.server.security.SecurityHelper securityHelper = EasyMock.createNiceMock(org.apache.ambari.server.security.SecurityHelper.class);

    private static final org.apache.ambari.server.configuration.Configuration configuration = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);

    private static final org.apache.ambari.server.view.ViewInstanceHandlerList handlerList = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewInstanceHandlerList.class);

    private static final org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);

    private static final org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);

    @org.junit.Before
    public void resetGlobalMocks() {
        org.apache.ambari.server.view.ViewRegistry.initInstance(org.apache.ambari.server.view.ViewRegistryTest.getRegistry(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceOperationHandler, org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.userDAO, org.apache.ambari.server.view.ViewRegistryTest.memberDAO, org.apache.ambari.server.view.ViewRegistryTest.privilegeDAO, org.apache.ambari.server.view.ViewRegistryTest.permissionDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceTypeDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.handlerList, null, null, org.apache.ambari.server.view.ViewRegistryTest.ambariMetaInfo, org.apache.ambari.server.view.ViewRegistryTest.clusters));
        EasyMock.reset(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceOperationHandler, org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.userDAO, org.apache.ambari.server.view.ViewRegistryTest.memberDAO, org.apache.ambari.server.view.ViewRegistryTest.privilegeDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceTypeDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.configuration, org.apache.ambari.server.view.ViewRegistryTest.handlerList, org.apache.ambari.server.view.ViewRegistryTest.ambariMetaInfo, org.apache.ambari.server.view.ViewRegistryTest.clusters);
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testReadViewArchives() throws java.lang.Exception {
        testReadViewArchives(false, false, false);
    }

    @org.junit.Test
    public void testReadViewArchives_removeUndeployed() throws java.lang.Exception {
        testReadViewArchives(false, true, false);
    }

    @org.junit.Test
    public void testReadViewArchives_badArchive() throws java.lang.Exception {
        testReadViewArchives(true, false, false);
    }

    @org.junit.Ignore("this will get refactored when divorced from the stack")
    public void testReadViewArchives_viewAutoInstanceCreation() throws java.lang.Exception {
        testReadViewArchives(false, false, true);
    }

    private void testReadViewArchives(boolean badArchive, boolean removeUndeployed, boolean checkAutoInstanceCreation) throws java.lang.Exception {
        java.io.File viewDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File extractedArchiveDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File viewArchive = EasyMock.createNiceMock(java.io.File.class);
        java.io.File archiveDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File entryFile = EasyMock.createNiceMock(java.io.File.class);
        java.io.File classesDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File libDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File metaInfDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File fileEntry = EasyMock.createNiceMock(java.io.File.class);
        java.util.jar.JarInputStream viewJarFile = EasyMock.createNiceMock(java.util.jar.JarInputStream.class);
        java.util.jar.JarEntry jarEntry = EasyMock.createNiceMock(java.util.jar.JarEntry.class);
        java.io.FileOutputStream fos = EasyMock.createMock(java.io.FileOutputStream.class);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(10);
        resourceTypeEntity.setName("MY_VIEW{1.0.0}");
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewDefinition.setResourceType(resourceTypeEntity);
        java.util.Set<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceEntities = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntities(viewDefinition);
        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity : viewInstanceEntities) {
            viewInstanceEntity.putInstanceData("p1", "v1");
            java.util.Collection<org.apache.ambari.server.orm.entities.ViewEntityEntity> entities = new java.util.HashSet<>();
            org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity = new org.apache.ambari.server.orm.entities.ViewEntityEntity();
            viewEntityEntity.setId(99L);
            viewEntityEntity.setIdProperty("id");
            viewEntityEntity.setViewName("MY_VIEW{1.0.0}");
            viewEntityEntity.setClassName("class");
            viewEntityEntity.setViewInstanceName(viewInstanceEntity.getName());
            viewEntityEntity.setViewInstance(viewInstanceEntity);
            entities.add(viewEntityEntity);
            viewInstanceEntity.setEntities(entities);
        }
        viewDefinition.setInstances(viewInstanceEntities);
        java.util.Map<java.io.File, org.apache.ambari.server.view.configuration.ViewConfig> viewConfigs = java.util.Collections.singletonMap(viewArchive, viewDefinition.getConfiguration());
        long resourceId = 99L;
        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity : viewInstanceEntities) {
            org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
            resourceEntity.setId(resourceId);
            resourceEntity.setResourceType(resourceTypeEntity);
            viewInstanceEntity.setResource(resourceEntity);
        }
        java.util.Map<java.lang.String, java.io.File> files = new java.util.HashMap<>();
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work", extractedArchiveDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}", archiveDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\view.xml", entryFile);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\WEB-INF/classes", classesDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\WEB-INF/lib", libDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\META-INF", metaInfDir);
        } else {
            files.put("/var/lib/ambari-server/resources/views/work", extractedArchiveDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}", archiveDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/view.xml", entryFile);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/WEB-INF/classes", classesDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/WEB-INF/lib", libDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/META-INF", metaInfDir);
        }
        java.util.Map<java.io.File, java.io.FileOutputStream> outputStreams = new java.util.HashMap<>();
        outputStreams.put(entryFile, fos);
        java.util.Map<java.io.File, java.util.jar.JarInputStream> jarFiles = new java.util.HashMap<>();
        jarFiles.put(viewArchive, viewJarFile);
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.getViewsDir()).andReturn(viewDir).anyTimes();
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            EasyMock.expect(viewDir.getAbsolutePath()).andReturn("\\var\\lib\\ambari-server\\resources\\views").anyTimes();
        } else {
            EasyMock.expect(viewDir.getAbsolutePath()).andReturn("/var/lib/ambari-server/resources/views").anyTimes();
        }
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.getViewExtractionThreadPoolCoreSize()).andReturn(2).anyTimes();
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.getViewExtractionThreadPoolMaxSize()).andReturn(3).anyTimes();
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.getViewExtractionThreadPoolTimeout()).andReturn(10000L).anyTimes();
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.extractViewsAfterClusterConfig()).andReturn(java.lang.Boolean.FALSE).anyTimes();
        EasyMock.expect(viewDir.listFiles()).andReturn(new java.io.File[]{ viewArchive });
        EasyMock.expect(viewArchive.isDirectory()).andReturn(false);
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            EasyMock.expect(viewArchive.getAbsolutePath()).andReturn("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}").anyTimes();
        } else {
            EasyMock.expect(viewArchive.getAbsolutePath()).andReturn("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}").anyTimes();
        }
        EasyMock.expect(archiveDir.exists()).andReturn(false).anyTimes();
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            EasyMock.expect(archiveDir.getAbsolutePath()).andReturn("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}").anyTimes();
        } else {
            EasyMock.expect(archiveDir.getAbsolutePath()).andReturn("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}").anyTimes();
        }
        EasyMock.expect(archiveDir.mkdir()).andReturn(true).anyTimes();
        EasyMock.expect(archiveDir.toURI()).andReturn(new java.net.URI("file:./")).anyTimes();
        EasyMock.expect(metaInfDir.mkdir()).andReturn(true).anyTimes();
        if (!badArchive) {
            EasyMock.expect(viewJarFile.getNextJarEntry()).andReturn(jarEntry);
            EasyMock.expect(viewJarFile.getNextJarEntry()).andReturn(null);
            EasyMock.expect(jarEntry.getName()).andReturn("view.xml");
            EasyMock.expect(jarEntry.isDirectory()).andReturn(false);
            EasyMock.expect(viewJarFile.read(EasyMock.anyObject(byte[].class))).andReturn(10);
            EasyMock.expect(viewJarFile.read(EasyMock.anyObject(byte[].class))).andReturn(-1);
            fos.write(EasyMock.anyObject(byte[].class), EasyMock.eq(0), EasyMock.eq(10));
            fos.flush();
            fos.close();
            viewJarFile.closeEntry();
            viewJarFile.close();
            EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewDAO.findByName("MY_VIEW{1.0.0}")).andReturn(viewDefinition);
        }
        EasyMock.expect(extractedArchiveDir.exists()).andReturn(false).anyTimes();
        EasyMock.expect(extractedArchiveDir.mkdir()).andReturn(true).anyTimes();
        EasyMock.expect(classesDir.exists()).andReturn(true).anyTimes();
        EasyMock.expect(classesDir.toURI()).andReturn(new java.net.URI("file:./")).anyTimes();
        EasyMock.expect(libDir.exists()).andReturn(true).anyTimes();
        EasyMock.expect(libDir.listFiles()).andReturn(new java.io.File[]{ fileEntry }).anyTimes();
        EasyMock.expect(fileEntry.toURI()).andReturn(new java.net.URI("file:./")).anyTimes();
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.isViewRemoveUndeployedEnabled()).andReturn(removeUndeployed).anyTimes();
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewAutoInstanceEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewAutoInstanceCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity autoInstanceDataEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceDataEntity.class);
        EasyMock.expect(autoInstanceDataEntity.getName()).andReturn("p1").anyTimes();
        EasyMock.expect(autoInstanceDataEntity.getUser()).andReturn(" ").anyTimes();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> serviceMap = new java.util.HashMap<>();
        serviceMap.put("HDFS", service);
        serviceMap.put("HIVE", service);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.0");
        if (checkAutoInstanceCreation) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> allClusters = new java.util.HashMap<>();
            EasyMock.expect(cluster.getClusterName()).andReturn("c1").anyTimes();
            EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(stackId).anyTimes();
            EasyMock.expect(cluster.getServices()).andReturn(serviceMap).anyTimes();
            allClusters.put("c1", cluster);
            EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.clusters.getClusters()).andReturn(allClusters);
            EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.merge(EasyMock.capture(viewAutoInstanceCapture))).andReturn(viewAutoInstanceEntity).anyTimes();
            EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.findByName("MY_VIEW{1.0.0}", "AUTO-INSTANCE")).andReturn(viewAutoInstanceEntity).anyTimes();
            EasyMock.expect(viewAutoInstanceEntity.getInstanceData("p1")).andReturn(autoInstanceDataEntity).anyTimes();
        } else {
            EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.clusters.getClusters()).andReturn(new java.util.HashMap<>());
        }
        if (removeUndeployed) {
            EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewDAO.findAll()).andReturn(java.util.Collections.emptyList());
        }
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.configuration, viewDir, extractedArchiveDir, viewArchive, archiveDir, entryFile, classesDir, libDir, metaInfDir, fileEntry, viewJarFile, jarEntry, fos, org.apache.ambari.server.view.ViewRegistryTest.resourceDAO, org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.clusters, cluster, viewAutoInstanceEntity);
        org.apache.ambari.server.view.ViewRegistryTest.TestViewArchiveUtility archiveUtility = new org.apache.ambari.server.view.ViewRegistryTest.TestViewArchiveUtility(viewConfigs, files, outputStreams, jarFiles, badArchive);
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistryTest.getRegistry(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceOperationHandler, org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.userDAO, org.apache.ambari.server.view.ViewRegistryTest.memberDAO, org.apache.ambari.server.view.ViewRegistryTest.privilegeDAO, org.apache.ambari.server.view.ViewRegistryTest.permissionDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceTypeDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.handlerList, null, archiveUtility, org.apache.ambari.server.view.ViewRegistryTest.ambariMetaInfo, org.apache.ambari.server.view.ViewRegistryTest.clusters);
        registry.readViewArchives();
        org.apache.ambari.server.orm.entities.ViewEntity view = null;
        long timeout = java.lang.System.currentTimeMillis() + 10000L;
        while (((!archiveUtility.isDeploymentFailed()) && ((view == null) || (!view.getStatus().equals(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYED)))) && (java.lang.System.currentTimeMillis() < timeout)) {
            view = registry.getDefinition("MY_VIEW", "1.0.0");
        } 
        int instanceDefinitionSize = (checkAutoInstanceCreation) ? 3 : 2;
        if (badArchive) {
            org.junit.Assert.assertNull(view);
            org.junit.Assert.assertTrue(archiveUtility.isDeploymentFailed());
        } else {
            org.junit.Assert.assertNotNull(view);
            org.junit.Assert.assertEquals(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYED, view.getStatus());
            java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> instanceDefinitions = registry.getInstanceDefinitions(view);
            java.util.ArrayList<org.apache.ambari.server.orm.entities.ViewInstanceEntity> filteredInstanceDefinition = new java.util.ArrayList<>();
            org.junit.Assert.assertEquals(instanceDefinitionSize, instanceDefinitions.size());
            if (checkAutoInstanceCreation) {
                org.junit.Assert.assertEquals(viewAutoInstanceCapture.getValue(), registry.getInstanceDefinition("MY_VIEW", "1.0.0", "AUTO-INSTANCE"));
            }
            for (org.apache.ambari.server.orm.entities.ViewInstanceEntity entity : instanceDefinitions) {
                if (!entity.getName().equals("AUTO-INSTANCE")) {
                    filteredInstanceDefinition.add(entity);
                }
            }
            for (org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity : filteredInstanceDefinition) {
                org.junit.Assert.assertEquals("v1", viewInstanceEntity.getInstanceData("p1").getValue());
                java.util.Collection<org.apache.ambari.server.orm.entities.ViewEntityEntity> entities = viewInstanceEntity.getEntities();
                org.junit.Assert.assertEquals(1, entities.size());
                org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity = entities.iterator().next();
                org.junit.Assert.assertEquals(99L, ((long) (viewEntityEntity.getId())));
                org.junit.Assert.assertEquals(viewInstanceEntity.getName(), viewEntityEntity.getViewInstanceName());
            }
        }
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.configuration, viewDir, extractedArchiveDir, viewArchive, archiveDir, entryFile, classesDir, libDir, metaInfDir, fileEntry, viewJarFile, jarEntry, fos, org.apache.ambari.server.view.ViewRegistryTest.resourceDAO, org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO);
    }

    @org.junit.Test
    public void testReadViewArchives_exception() throws java.lang.Exception {
        java.io.File viewDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File extractedArchiveDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File viewArchive = EasyMock.createNiceMock(java.io.File.class);
        java.io.File archiveDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File entryFile = EasyMock.createNiceMock(java.io.File.class);
        java.io.File classesDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File libDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File metaInfDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File fileEntry = EasyMock.createNiceMock(java.io.File.class);
        java.util.jar.JarInputStream viewJarFile = EasyMock.createNiceMock(java.util.jar.JarInputStream.class);
        java.util.jar.JarEntry jarEntry = EasyMock.createNiceMock(java.util.jar.JarEntry.class);
        java.io.FileOutputStream fos = EasyMock.createMock(java.io.FileOutputStream.class);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(10);
        resourceTypeEntity.setName("MY_VIEW{1.0.0}");
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewDefinition.setResourceType(resourceTypeEntity);
        java.util.Set<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceEntities = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntities(viewDefinition);
        viewDefinition.setInstances(viewInstanceEntities);
        java.util.Map<java.io.File, org.apache.ambari.server.view.configuration.ViewConfig> viewConfigs = java.util.Collections.singletonMap(viewArchive, viewDefinition.getConfiguration());
        long resourceId = 99L;
        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity : viewInstanceEntities) {
            org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
            resourceEntity.setId(resourceId);
            resourceEntity.setResourceType(resourceTypeEntity);
            viewInstanceEntity.setResource(resourceEntity);
        }
        java.util.Map<java.lang.String, java.io.File> files = new java.util.HashMap<>();
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work", extractedArchiveDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}", archiveDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\view.xml", entryFile);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\WEB-INF/classes", classesDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\WEB-INF/lib", libDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\META-INF", metaInfDir);
        } else {
            files.put("/var/lib/ambari-server/resources/views/work", extractedArchiveDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}", archiveDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/view.xml", entryFile);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/WEB-INF/classes", classesDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/WEB-INF/lib", libDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/META-INF", metaInfDir);
        }
        java.util.Map<java.io.File, java.io.FileOutputStream> outputStreams = new java.util.HashMap<>();
        outputStreams.put(entryFile, fos);
        java.util.Map<java.io.File, java.util.jar.JarInputStream> jarFiles = new java.util.HashMap<>();
        jarFiles.put(viewArchive, viewJarFile);
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.getViewsDir()).andReturn(viewDir).anyTimes();
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            EasyMock.expect(viewDir.getAbsolutePath()).andReturn("\\var\\lib\\ambari-server\\resources\\views").anyTimes();
        } else {
            EasyMock.expect(viewDir.getAbsolutePath()).andReturn("/var/lib/ambari-server/resources/views").anyTimes();
        }
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.getViewExtractionThreadPoolCoreSize()).andReturn(2).anyTimes();
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.getViewExtractionThreadPoolMaxSize()).andReturn(3).anyTimes();
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.getViewExtractionThreadPoolTimeout()).andReturn(10000L).anyTimes();
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.extractViewsAfterClusterConfig()).andReturn(java.lang.Boolean.FALSE).anyTimes();
        EasyMock.expect(viewDir.listFiles()).andReturn(new java.io.File[]{ viewArchive }).anyTimes();
        EasyMock.expect(viewArchive.isDirectory()).andReturn(false);
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            EasyMock.expect(viewArchive.getAbsolutePath()).andReturn("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}").anyTimes();
        } else {
            EasyMock.expect(viewArchive.getAbsolutePath()).andReturn("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}").anyTimes();
        }
        EasyMock.expect(archiveDir.exists()).andReturn(false);
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            EasyMock.expect(archiveDir.getAbsolutePath()).andReturn("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}").anyTimes();
        } else {
            EasyMock.expect(archiveDir.getAbsolutePath()).andReturn("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}").anyTimes();
        }
        EasyMock.expect(archiveDir.mkdir()).andReturn(true);
        EasyMock.expect(archiveDir.toURI()).andReturn(new java.net.URI("file:./"));
        EasyMock.expect(metaInfDir.mkdir()).andReturn(true);
        EasyMock.expect(viewJarFile.getNextJarEntry()).andReturn(jarEntry);
        EasyMock.expect(viewJarFile.getNextJarEntry()).andReturn(null);
        EasyMock.expect(jarEntry.getName()).andReturn("view.xml");
        EasyMock.expect(jarEntry.isDirectory()).andReturn(false);
        EasyMock.expect(viewJarFile.read(EasyMock.anyObject(byte[].class))).andReturn(10);
        EasyMock.expect(viewJarFile.read(EasyMock.anyObject(byte[].class))).andReturn(-1);
        fos.write(EasyMock.anyObject(byte[].class), EasyMock.eq(0), EasyMock.eq(10));
        fos.flush();
        fos.close();
        viewJarFile.closeEntry();
        viewJarFile.close();
        EasyMock.expect(extractedArchiveDir.exists()).andReturn(false);
        EasyMock.expect(extractedArchiveDir.mkdir()).andReturn(true);
        EasyMock.expect(classesDir.exists()).andReturn(true);
        EasyMock.expect(classesDir.toURI()).andReturn(new java.net.URI("file:./"));
        EasyMock.expect(libDir.exists()).andReturn(true);
        EasyMock.expect(libDir.listFiles()).andReturn(new java.io.File[]{ fileEntry });
        EasyMock.expect(fileEntry.toURI()).andReturn(new java.net.URI("file:./"));
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewDAO.findByName("MY_VIEW{1.0.0}")).andThrow(new java.lang.IllegalArgumentException("Expected exception."));
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.configuration, viewDir, extractedArchiveDir, viewArchive, archiveDir, entryFile, classesDir, libDir, metaInfDir, fileEntry, viewJarFile, jarEntry, fos, org.apache.ambari.server.view.ViewRegistryTest.viewDAO);
        org.apache.ambari.server.view.ViewRegistryTest.TestViewArchiveUtility archiveUtility = new org.apache.ambari.server.view.ViewRegistryTest.TestViewArchiveUtility(viewConfigs, files, outputStreams, jarFiles, false);
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistryTest.getRegistry(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.userDAO, org.apache.ambari.server.view.ViewRegistryTest.memberDAO, org.apache.ambari.server.view.ViewRegistryTest.privilegeDAO, org.apache.ambari.server.view.ViewRegistryTest.permissionDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceTypeDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.handlerList, null, archiveUtility, org.apache.ambari.server.view.ViewRegistryTest.ambariMetaInfo);
        registry.readViewArchives();
        org.apache.ambari.server.orm.entities.ViewEntity view = null;
        long timeout = java.lang.System.currentTimeMillis() + 10000L;
        while (((view == null) || (!view.getStatus().equals(org.apache.ambari.view.ViewDefinition.ViewStatus.ERROR))) && (java.lang.System.currentTimeMillis() < timeout)) {
            view = registry.getDefinition("MY_VIEW", "1.0.0");
        } 
        org.junit.Assert.assertNotNull(view);
        org.junit.Assert.assertEquals(org.apache.ambari.view.ViewDefinition.ViewStatus.ERROR, view.getStatus());
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.configuration, viewDir, extractedArchiveDir, viewArchive, archiveDir, entryFile, classesDir, libDir, metaInfDir, fileEntry, viewJarFile, jarEntry, fos, org.apache.ambari.server.view.ViewRegistryTest.viewDAO);
    }

    @org.junit.Test
    public void testListener() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        org.apache.ambari.server.view.ViewRegistryTest.TestListener listener = new org.apache.ambari.server.view.ViewRegistryTest.TestListener();
        registry.registerListener(listener, "MY_VIEW", "1.0.0");
        org.apache.ambari.server.view.events.EventImpl event = org.apache.ambari.server.view.events.EventImplTest.getEvent("MyEvent", java.util.Collections.emptyMap(), org.apache.ambari.server.view.ViewRegistryTest.VIEW_XML_1);
        registry.fireEvent(event);
        org.junit.Assert.assertEquals(event, listener.getLastEvent());
        listener.clear();
        event = org.apache.ambari.server.view.events.EventImplTest.getEvent("MyEvent", java.util.Collections.emptyMap(), org.apache.ambari.server.view.ViewRegistryTest.VIEW_XML_2);
        registry.fireEvent(event);
        org.junit.Assert.assertNull(listener.getLastEvent());
        registry.unregisterListener(listener, "MY_VIEW", "1.0.0");
        event = org.apache.ambari.server.view.events.EventImplTest.getEvent("MyEvent", java.util.Collections.emptyMap(), org.apache.ambari.server.view.ViewRegistryTest.VIEW_XML_1);
        registry.fireEvent(event);
        org.junit.Assert.assertNull(listener.getLastEvent());
    }

    @org.junit.Test
    public void testListener_allVersions() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        org.apache.ambari.server.view.ViewRegistryTest.TestListener listener = new org.apache.ambari.server.view.ViewRegistryTest.TestListener();
        registry.registerListener(listener, "MY_VIEW", null);
        org.apache.ambari.server.view.events.EventImpl event = org.apache.ambari.server.view.events.EventImplTest.getEvent("MyEvent", java.util.Collections.emptyMap(), org.apache.ambari.server.view.ViewRegistryTest.VIEW_XML_1);
        registry.fireEvent(event);
        org.junit.Assert.assertEquals(event, listener.getLastEvent());
        listener.clear();
        event = org.apache.ambari.server.view.events.EventImplTest.getEvent("MyEvent", java.util.Collections.emptyMap(), org.apache.ambari.server.view.ViewRegistryTest.VIEW_XML_2);
        registry.fireEvent(event);
        org.junit.Assert.assertEquals(event, listener.getLastEvent());
        listener.clear();
        registry.unregisterListener(listener, "MY_VIEW", null);
        event = org.apache.ambari.server.view.events.EventImplTest.getEvent("MyEvent", java.util.Collections.emptyMap(), org.apache.ambari.server.view.ViewRegistryTest.VIEW_XML_1);
        registry.fireEvent(event);
        org.junit.Assert.assertNull(listener.getLastEvent());
        event = org.apache.ambari.server.view.events.EventImplTest.getEvent("MyEvent", java.util.Collections.emptyMap(), org.apache.ambari.server.view.ViewRegistryTest.VIEW_XML_2);
        registry.fireEvent(event);
        org.junit.Assert.assertNull(listener.getLastEvent());
    }

    @org.junit.Test
    public void testGetResourceProviders() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        viewDefinition.setConfiguration(config);
        registry.setupViewDefinition(viewDefinition, getClass().getClassLoader());
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.controller.spi.ResourceProvider> providerMap = registry.getResourceProviders();
        org.junit.Assert.assertEquals(3, providerMap.size());
        org.junit.Assert.assertTrue(providerMap.containsKey(org.apache.ambari.server.controller.spi.Resource.Type.valueOf("MY_VIEW{1.0.0}/resource")));
        org.junit.Assert.assertTrue(providerMap.containsKey(org.apache.ambari.server.controller.spi.Resource.Type.valueOf("MY_VIEW{1.0.0}/subresource")));
        org.junit.Assert.assertTrue(providerMap.containsKey(org.apache.ambari.server.controller.spi.Resource.Type.valueOf("MY_VIEW{1.0.0}/resources")));
    }

    @org.junit.Test
    public void testAddGetDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        registry.addDefinition(viewDefinition);
        org.junit.Assert.assertEquals(viewDefinition, registry.getDefinition("MY_VIEW", "1.0.0"));
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewEntity> viewDefinitions = registry.getDefinitions();
        org.junit.Assert.assertEquals(1, viewDefinitions.size());
        org.junit.Assert.assertEquals(viewDefinition, viewDefinitions.iterator().next());
    }

    @org.junit.Test
    public void testGetDefinition() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(10);
        resourceTypeEntity.setName(viewDefinition.getName());
        viewDefinition.setResourceType(resourceTypeEntity);
        registry.addDefinition(viewDefinition);
        viewDefinition.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYING);
        org.junit.Assert.assertNull(registry.getDefinition(resourceTypeEntity));
        viewDefinition.setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYED);
        org.junit.Assert.assertEquals(viewDefinition, registry.getDefinition(resourceTypeEntity));
    }

    @org.junit.Test
    public void testAddGetInstanceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        registry.addDefinition(viewDefinition);
        registry.addInstanceDefinition(viewDefinition, viewInstanceDefinition);
        org.junit.Assert.assertEquals(viewInstanceDefinition, registry.getInstanceDefinition("MY_VIEW", "1.0.0", "INSTANCE1"));
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceDefinitions = registry.getInstanceDefinitions(viewDefinition);
        org.junit.Assert.assertEquals(1, viewInstanceDefinitions.size());
        org.junit.Assert.assertEquals(viewInstanceDefinition, viewInstanceDefinitions.iterator().next());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        viewDefinition.setConfiguration(config);
        registry.setupViewDefinition(viewDefinition, getClass().getClassLoader());
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = registry.getSubResourceDefinitions(viewDefinition.getCommonName(), viewDefinition.getVersion());
        org.junit.Assert.assertEquals(3, subResourceDefinitions.size());
        java.util.Set<java.lang.String> names = new java.util.HashSet<>();
        for (org.apache.ambari.server.api.resources.SubResourceDefinition definition : subResourceDefinitions) {
            names.add(definition.getType().name());
        }
        org.junit.Assert.assertTrue(names.contains("MY_VIEW{1.0.0}/resources"));
        org.junit.Assert.assertTrue(names.contains("MY_VIEW{1.0.0}/resource"));
        org.junit.Assert.assertTrue(names.contains("MY_VIEW{1.0.0}/subresource"));
    }

    @org.junit.Test
    public void testAddInstanceDefinition() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewEntity, instanceConfig);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(10);
        resourceTypeEntity.setName(viewEntity.getName());
        viewEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setId(20L);
        resourceEntity.setResourceType(resourceTypeEntity);
        viewInstanceEntity.setResource(resourceEntity);
        registry.addDefinition(viewEntity);
        registry.addInstanceDefinition(viewEntity, viewInstanceEntity);
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceDefinitions = registry.getInstanceDefinitions(viewEntity);
        org.junit.Assert.assertEquals(1, viewInstanceDefinitions.size());
        org.junit.Assert.assertEquals(viewInstanceEntity, viewInstanceDefinitions.iterator().next());
    }

    @org.junit.Test
    public void testInstallViewInstance() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewRegistryTest.XML_VALID_INSTANCE);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.merge(viewInstanceEntity)).andReturn(viewInstanceEntity);
        org.apache.ambari.server.view.ViewRegistryTest.handlerList.addViewInstance(viewInstanceEntity);
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceTypeDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.handlerList);
        registry.addDefinition(viewEntity);
        registry.installViewInstance(viewInstanceEntity);
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceDefinitions = registry.getInstanceDefinitions(viewEntity);
        org.junit.Assert.assertEquals(1, viewInstanceDefinitions.size());
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity = viewInstanceDefinitions.iterator().next();
        org.junit.Assert.assertEquals("v2-1", instanceEntity.getProperty("p2").getValue());
        org.junit.Assert.assertEquals(viewInstanceEntity, viewInstanceDefinitions.iterator().next());
        org.junit.Assert.assertEquals("MY_VIEW{1.0.0}", viewInstanceEntity.getResource().getResourceType().getName());
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceTypeDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.handlerList);
    }

    @org.junit.Test
    public void testInstallViewInstance_invalid() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewRegistryTest.XML_INVALID_INSTANCE);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.resourceTypeDAO);
        registry.addDefinition(viewEntity);
        try {
            registry.installViewInstance(viewInstanceEntity);
            org.junit.Assert.fail("expected an IllegalStateException");
        } catch (org.apache.ambari.server.view.validation.ValidationException e) {
        }
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.resourceTypeDAO);
    }

    @org.junit.Test
    public void testInstallViewInstance_validatorPass() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.view.validation.Validator validator = EasyMock.createNiceMock(org.apache.ambari.view.validation.Validator.class);
        org.apache.ambari.view.validation.ValidationResult result = EasyMock.createNiceMock(org.apache.ambari.view.validation.ValidationResult.class);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewRegistryTest.XML_VALID_INSTANCE);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        viewEntity.setValidator(validator);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.merge(viewInstanceEntity)).andReturn(viewInstanceEntity);
        org.apache.ambari.server.view.ViewRegistryTest.handlerList.addViewInstance(viewInstanceEntity);
        EasyMock.expect(validator.validateInstance(viewInstanceEntity, org.apache.ambari.view.validation.Validator.ValidationContext.PRE_CREATE)).andReturn(result).anyTimes();
        EasyMock.expect(result.isValid()).andReturn(true).anyTimes();
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.handlerList, validator, result);
        registry.addDefinition(viewEntity);
        registry.installViewInstance(viewInstanceEntity);
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceDefinitions = registry.getInstanceDefinitions(viewEntity);
        org.junit.Assert.assertEquals(1, viewInstanceDefinitions.size());
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity = viewInstanceDefinitions.iterator().next();
        org.junit.Assert.assertEquals("v2-1", instanceEntity.getProperty("p2").getValue());
        org.junit.Assert.assertEquals(viewInstanceEntity, viewInstanceDefinitions.iterator().next());
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.handlerList, validator, result);
    }

    @org.junit.Test
    public void testInstallViewInstance_validatorFail() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.view.validation.Validator validator = EasyMock.createNiceMock(org.apache.ambari.view.validation.Validator.class);
        org.apache.ambari.view.validation.ValidationResult result = EasyMock.createNiceMock(org.apache.ambari.view.validation.ValidationResult.class);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewRegistryTest.XML_VALID_INSTANCE);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        viewEntity.setValidator(validator);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        EasyMock.expect(validator.validateInstance(viewInstanceEntity, org.apache.ambari.view.validation.Validator.ValidationContext.PRE_CREATE)).andReturn(result).anyTimes();
        EasyMock.expect(result.isValid()).andReturn(false).anyTimes();
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.handlerList, validator, result);
        registry.addDefinition(viewEntity);
        try {
            registry.installViewInstance(viewInstanceEntity);
            org.junit.Assert.fail("expected a ValidationException");
        } catch (org.apache.ambari.server.view.validation.ValidationException e) {
        }
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceDefinitions = registry.getInstanceDefinitions(viewEntity);
        org.junit.Assert.assertTrue(viewInstanceDefinitions.isEmpty());
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.handlerList, validator, result);
    }

    @org.junit.Test
    public void testInstallViewInstance_unknownView() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewRegistryTest.XML_VALID_INSTANCE);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        viewInstanceEntity.setViewName("BOGUS_VIEW");
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.resourceTypeDAO);
        registry.addDefinition(viewEntity);
        try {
            registry.installViewInstance(viewInstanceEntity);
            org.junit.Assert.fail("expected an IllegalArgumentException");
        } catch (java.lang.IllegalArgumentException e) {
        }
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.resourceTypeDAO);
    }

    @org.junit.Test
    public void testUpdateViewInstance() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewRegistryTest.XML_VALID_INSTANCE);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        org.apache.ambari.server.orm.entities.ViewInstanceEntity updateInstance = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.merge(viewInstanceEntity)).andReturn(viewInstanceEntity);
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.findByName("MY_VIEW{1.0.0}", viewInstanceEntity.getInstanceName())).andReturn(viewInstanceEntity).anyTimes();
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper);
        registry.addDefinition(viewEntity);
        registry.installViewInstance(viewInstanceEntity);
        org.junit.Assert.assertTrue(viewInstanceEntity.isVisible());
        updateInstance.setLabel("new label");
        updateInstance.setDescription("new description");
        updateInstance.setVisible(false);
        registry.updateViewInstance(updateInstance);
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceDefinitions = registry.getInstanceDefinitions(viewEntity);
        org.junit.Assert.assertEquals(1, viewInstanceDefinitions.size());
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity = viewInstanceDefinitions.iterator().next();
        org.junit.Assert.assertEquals("v2-1", instanceEntity.getProperty("p2").getValue());
        org.junit.Assert.assertEquals("new label", instanceEntity.getLabel());
        org.junit.Assert.assertEquals("new description", instanceEntity.getDescription());
        org.junit.Assert.assertFalse(instanceEntity.isVisible());
        org.junit.Assert.assertEquals(viewInstanceEntity, viewInstanceDefinitions.iterator().next());
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper);
    }

    @org.junit.Test
    public void testSetViewInstanceProperties() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewRegistryTest.XML_VALID_INSTANCE);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        java.util.Map<java.lang.String, java.lang.String> instanceProperties = new java.util.HashMap<>();
        instanceProperties.put("p1", "newV1");
        instanceProperties.put("p2", "newV2");
        registry.setViewInstanceProperties(viewInstanceEntity, instanceProperties, viewEntity.getConfiguration(), viewEntity.getClassLoader());
        org.junit.Assert.assertEquals("newV1", viewInstanceEntity.getProperty("p1").getValue());
        org.junit.Assert.assertEquals("bmV3VjI=", viewInstanceEntity.getProperty("p2").getValue());
    }

    @org.junit.Test
    public void testUninstallViewInstance() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(new java.util.Properties());
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewRegistryTest.XML_VALID_INSTANCE);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        org.apache.ambari.server.view.ViewRegistryTest.viewInstanceOperationHandler.uninstallViewInstance(viewInstanceEntity);
        org.apache.ambari.server.view.ViewRegistryTest.handlerList.removeViewInstance(viewInstanceEntity);
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceOperationHandler, org.apache.ambari.server.view.ViewRegistryTest.handlerList);
        registry.addDefinition(viewEntity);
        registry.addInstanceDefinition(viewEntity, viewInstanceEntity);
        registry.uninstallViewInstance(viewInstanceEntity);
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceDefinitions = registry.getInstanceDefinitions(viewEntity);
        org.junit.Assert.assertEquals(0, viewInstanceDefinitions.size());
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceOperationHandler, org.apache.ambari.server.view.ViewRegistryTest.handlerList);
    }

    @org.junit.Test
    public void testUpdateViewInstance_invalid() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewRegistryTest.XML_VALID_INSTANCE);
        org.apache.ambari.server.view.configuration.ViewConfig invalidConfig = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewRegistryTest.XML_INVALID_INSTANCE);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        org.apache.ambari.server.orm.entities.ViewInstanceEntity updateInstance = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, invalidConfig.getInstances().get(0));
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.merge(viewInstanceEntity)).andReturn(viewInstanceEntity);
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper);
        registry.addDefinition(viewEntity);
        registry.installViewInstance(viewInstanceEntity);
        try {
            registry.updateViewInstance(updateInstance);
            org.junit.Assert.fail("expected an IllegalStateException");
        } catch (org.apache.ambari.server.view.validation.ValidationException e) {
        }
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper);
    }

    @org.junit.Test
    public void testUpdateViewInstance_validatorPass() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.view.validation.Validator validator = EasyMock.createNiceMock(org.apache.ambari.view.validation.Validator.class);
        org.apache.ambari.view.validation.ValidationResult result = EasyMock.createNiceMock(org.apache.ambari.view.validation.ValidationResult.class);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewRegistryTest.XML_VALID_INSTANCE);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        viewEntity.setValidator(validator);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        org.apache.ambari.server.orm.entities.ViewInstanceEntity updateInstance = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.merge(viewInstanceEntity)).andReturn(viewInstanceEntity);
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.findByName("MY_VIEW{1.0.0}", viewInstanceEntity.getInstanceName())).andReturn(viewInstanceEntity).anyTimes();
        EasyMock.expect(validator.validateInstance(viewInstanceEntity, org.apache.ambari.view.validation.Validator.ValidationContext.PRE_UPDATE)).andReturn(result).anyTimes();
        EasyMock.expect(result.isValid()).andReturn(true).anyTimes();
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, validator, result);
        registry.addDefinition(viewEntity);
        registry.installViewInstance(viewInstanceEntity);
        registry.updateViewInstance(updateInstance);
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceDefinitions = registry.getInstanceDefinitions(viewEntity);
        org.junit.Assert.assertEquals(1, viewInstanceDefinitions.size());
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity = viewInstanceDefinitions.iterator().next();
        org.junit.Assert.assertEquals("v2-1", instanceEntity.getProperty("p2").getValue());
        org.junit.Assert.assertEquals(viewInstanceEntity, viewInstanceDefinitions.iterator().next());
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, validator, result);
    }

    @org.junit.Test
    public void testUpdateViewInstance_validatorFail() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Properties properties = new java.util.Properties();
        properties.put("p1", "v1");
        org.apache.ambari.server.configuration.Configuration ambariConfig = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.view.validation.Validator validator = EasyMock.createNiceMock(org.apache.ambari.view.validation.Validator.class);
        org.apache.ambari.view.validation.ValidationResult result = EasyMock.createNiceMock(org.apache.ambari.view.validation.ValidationResult.class);
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(org.apache.ambari.server.view.ViewRegistryTest.XML_VALID_INSTANCE);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewEntity(config, ambariConfig, getClass().getClassLoader(), "");
        viewEntity.setValidator(validator);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        org.apache.ambari.server.orm.entities.ViewInstanceEntity updateInstance = org.apache.ambari.server.view.ViewRegistryTest.getViewInstanceEntity(viewEntity, config.getInstances().get(0));
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.merge(viewInstanceEntity)).andReturn(viewInstanceEntity);
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.findByName("MY_VIEW{1.0.0}", viewInstanceEntity.getInstanceName())).andReturn(viewInstanceEntity).anyTimes();
        EasyMock.expect(validator.validateInstance(viewInstanceEntity, org.apache.ambari.view.validation.Validator.ValidationContext.PRE_UPDATE)).andReturn(result).anyTimes();
        EasyMock.expect(result.isValid()).andReturn(false).anyTimes();
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, validator, result);
        registry.addDefinition(viewEntity);
        registry.installViewInstance(viewInstanceEntity);
        try {
            registry.updateViewInstance(updateInstance);
            org.junit.Assert.fail("expected a ValidationException");
        } catch (org.apache.ambari.server.view.validation.ValidationException e) {
        }
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceDefinitions = registry.getInstanceDefinitions(viewEntity);
        org.junit.Assert.assertEquals(1, viewInstanceDefinitions.size());
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity = viewInstanceDefinitions.iterator().next();
        org.junit.Assert.assertEquals("v2-1", instanceEntity.getProperty("p2").getValue());
        org.junit.Assert.assertEquals(viewInstanceEntity, viewInstanceDefinitions.iterator().next());
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, validator, result);
    }

    @org.junit.Test
    public void testRemoveInstanceData() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        viewInstanceEntity.putInstanceData("foo", "value");
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity dataEntity = viewInstanceEntity.getInstanceData("foo");
        org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.removeData(dataEntity);
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.merge(viewInstanceEntity)).andReturn(viewInstanceEntity);
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper);
        registry.removeInstanceData(viewInstanceEntity, "foo");
        org.junit.Assert.assertNull(viewInstanceEntity.getInstanceData("foo"));
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper);
    }

    @org.junit.Test
    public void testIncludeDefinitionForAdmin() {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.configuration);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.junit.Assert.assertTrue(registry.includeDefinition(viewEntity));
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.configuration);
    }

    @org.junit.Test
    public void testIncludeDefinitionForUserNoInstances() {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        EasyMock.expect(viewEntity.getInstances()).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.replay(viewEntity, org.apache.ambari.server.view.ViewRegistryTest.configuration);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(1L));
        org.junit.Assert.assertFalse(registry.includeDefinition(viewEntity));
        EasyMock.verify(viewEntity, org.apache.ambari.server.view.ViewRegistryTest.configuration);
    }

    @org.junit.Test
    public void testIncludeDefinitionForUserHasAccess() {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> instances = new java.util.ArrayList<>();
        instances.add(instanceEntity);
        EasyMock.expect(viewEntity.getInstances()).andReturn(instances);
        EasyMock.expect(instanceEntity.getResource()).andReturn(resourceEntity);
        EasyMock.expect(resourceEntity.getId()).andReturn(54L).anyTimes();
        EasyMock.expect(resourceEntity.getResourceType()).andReturn(resourceTypeEntity).anyTimes();
        EasyMock.expect(resourceTypeEntity.getId()).andReturn(org.apache.ambari.server.security.authorization.ResourceType.VIEW.getId()).anyTimes();
        EasyMock.expect(resourceTypeEntity.getName()).andReturn(org.apache.ambari.server.security.authorization.ResourceType.VIEW.name()).anyTimes();
        EasyMock.replay(viewEntity, instanceEntity, resourceEntity, resourceTypeEntity, org.apache.ambari.server.view.ViewRegistryTest.configuration);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(resourceEntity.getId()));
        org.junit.Assert.assertTrue(registry.includeDefinition(viewEntity));
        EasyMock.verify(viewEntity, instanceEntity, resourceEntity, resourceTypeEntity, org.apache.ambari.server.view.ViewRegistryTest.configuration);
    }

    @org.junit.Test
    public void testOnAmbariEventServiceCreation() throws java.lang.Exception {
        java.util.Set<java.lang.String> serviceNames = new java.util.HashSet<>();
        serviceNames.add("HDFS");
        serviceNames.add("HIVE");
        testOnAmbariEventServiceCreation(org.apache.ambari.server.view.ViewRegistryTest.AUTO_VIEW_XML, serviceNames, org.apache.ambari.server.view.ViewRegistryTest.EXPECTED_HDP_2_0_STACK_NAME, true);
    }

    @org.junit.Test
    public void testOnAmbariEventServiceCreation_widcardStackVersion() throws java.lang.Exception {
        java.util.Set<java.lang.String> serviceNames = new java.util.HashSet<>();
        serviceNames.add("HDFS");
        serviceNames.add("HIVE");
        testOnAmbariEventServiceCreation(org.apache.ambari.server.view.ViewRegistryTest.AUTO_VIEW_WILD_STACK_XML, serviceNames, org.apache.ambari.server.view.ViewRegistryTest.EXPECTED_HDP_2_0_STACK_NAME, true);
    }

    @org.junit.Test
    public void testOnAmbariEventServiceCreation_widcardAllStacks() throws java.lang.Exception {
        java.util.Set<java.lang.String> serviceNames = new java.util.HashSet<>();
        serviceNames.add("HDFS");
        serviceNames.add("HIVE");
        testOnAmbariEventServiceCreation(org.apache.ambari.server.view.ViewRegistryTest.AUTO_VIEW_WILD_ALL_STACKS_XML, serviceNames, "HDF-3.1", true);
    }

    @org.junit.Test
    public void testOnAmbariEventServiceCreation_mismatchStackVersion() throws java.lang.Exception {
        java.util.Set<java.lang.String> serviceNames = new java.util.HashSet<>();
        serviceNames.add("HDFS");
        serviceNames.add("HIVE");
        testOnAmbariEventServiceCreation(org.apache.ambari.server.view.ViewRegistryTest.AUTO_VIEW_BAD_STACK_XML, serviceNames, org.apache.ambari.server.view.ViewRegistryTest.EXPECTED_HDP_2_0_STACK_NAME, false);
    }

    @org.junit.Test
    public void testOnAmbariEventServiceCreation_missingClusterService() throws java.lang.Exception {
        java.util.Set<java.lang.String> serviceNames = new java.util.HashSet<>();
        serviceNames.add("STORM");
        serviceNames.add("HIVE");
        testOnAmbariEventServiceCreation(org.apache.ambari.server.view.ViewRegistryTest.AUTO_VIEW_XML, serviceNames, org.apache.ambari.server.view.ViewRegistryTest.EXPECTED_HDP_2_0_STACK_NAME, false);
    }

    @org.junit.Test
    public void testCheckViewVersions() {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        org.apache.ambari.server.view.configuration.ViewConfig config = EasyMock.createNiceMock(org.apache.ambari.server.view.configuration.ViewConfig.class);
        EasyMock.expect(viewEntity.getConfiguration()).andReturn(config).anyTimes();
        EasyMock.expect(config.getMinAmbariVersion()).andReturn(null);
        EasyMock.expect(config.getMaxAmbariVersion()).andReturn(null);
        EasyMock.expect(config.getMinAmbariVersion()).andReturn("1.0.0");
        EasyMock.expect(config.getMaxAmbariVersion()).andReturn("3.0.0");
        EasyMock.expect(config.getMinAmbariVersion()).andReturn("1.0.0");
        EasyMock.expect(config.getMaxAmbariVersion()).andReturn("1.5");
        EasyMock.expect(config.getMinAmbariVersion()).andReturn("2.5");
        EasyMock.expect(config.getMinAmbariVersion()).andReturn(null);
        EasyMock.expect(config.getMaxAmbariVersion()).andReturn("3.0.0");
        EasyMock.expect(config.getMinAmbariVersion()).andReturn("1.0.0");
        EasyMock.expect(config.getMaxAmbariVersion()).andReturn(null);
        EasyMock.expect(config.getMinAmbariVersion()).andReturn("1.0.0");
        EasyMock.expect(config.getMaxAmbariVersion()).andReturn("*");
        EasyMock.expect(config.getMinAmbariVersion()).andReturn("*");
        EasyMock.expect(config.getMaxAmbariVersion()).andReturn("3.0.0");
        EasyMock.expect(config.getMinAmbariVersion()).andReturn("1.0.0");
        EasyMock.expect(config.getMaxAmbariVersion()).andReturn("2.*");
        EasyMock.expect(config.getMinAmbariVersion()).andReturn("1.*");
        EasyMock.expect(config.getMaxAmbariVersion()).andReturn("3.0.0");
        EasyMock.expect(config.getMinAmbariVersion()).andReturn("1.0.0");
        EasyMock.expect(config.getMaxAmbariVersion()).andReturn("2.1.*");
        EasyMock.expect(config.getMinAmbariVersion()).andReturn("1.5.*");
        EasyMock.expect(config.getMaxAmbariVersion()).andReturn("3.0.0");
        EasyMock.expect(config.getMinAmbariVersion()).andReturn("1.0.0");
        EasyMock.expect(config.getMaxAmbariVersion()).andReturn("1.9.9.*");
        EasyMock.expect(config.getMinAmbariVersion()).andReturn("2.0.0.1.*");
        EasyMock.replay(viewEntity, config);
        org.junit.Assert.assertTrue(registry.checkViewVersions(viewEntity, "2.0.0"));
        org.junit.Assert.assertTrue(registry.checkViewVersions(viewEntity, "2.0.0"));
        org.junit.Assert.assertFalse(registry.checkViewVersions(viewEntity, "2.0.0"));
        org.junit.Assert.assertFalse(registry.checkViewVersions(viewEntity, "2.0.0"));
        org.junit.Assert.assertTrue(registry.checkViewVersions(viewEntity, "2.0.0"));
        org.junit.Assert.assertTrue(registry.checkViewVersions(viewEntity, "2.0.0"));
        org.junit.Assert.assertTrue(registry.checkViewVersions(viewEntity, "2.0.0"));
        org.junit.Assert.assertTrue(registry.checkViewVersions(viewEntity, "2.0.0"));
        org.junit.Assert.assertTrue(registry.checkViewVersions(viewEntity, "2.0.0"));
        org.junit.Assert.assertTrue(registry.checkViewVersions(viewEntity, "2.0.0"));
        org.junit.Assert.assertTrue(registry.checkViewVersions(viewEntity, "2.0.0"));
        org.junit.Assert.assertTrue(registry.checkViewVersions(viewEntity, "2.0.0"));
        org.junit.Assert.assertFalse(registry.checkViewVersions(viewEntity, "2.0.0"));
        org.junit.Assert.assertFalse(registry.checkViewVersions(viewEntity, "2.0.0"));
        EasyMock.verify(viewEntity, config);
    }

    @org.junit.Test
    public void testExtractViewArchive() throws java.lang.Exception {
        java.io.File viewDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File extractedArchiveDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File viewArchive = EasyMock.createNiceMock(java.io.File.class);
        java.io.File archiveDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File entryFile = EasyMock.createNiceMock(java.io.File.class);
        java.io.File classesDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File libDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File metaInfDir = EasyMock.createNiceMock(java.io.File.class);
        java.io.File fileEntry = EasyMock.createNiceMock(java.io.File.class);
        java.util.jar.JarInputStream viewJarFile = EasyMock.createNiceMock(java.util.jar.JarInputStream.class);
        java.util.jar.JarEntry jarEntry = EasyMock.createNiceMock(java.util.jar.JarEntry.class);
        java.io.InputStream is = EasyMock.createMock(java.io.InputStream.class);
        java.io.FileOutputStream fos = EasyMock.createMock(java.io.FileOutputStream.class);
        org.apache.ambari.server.view.ViewExtractor viewExtractor = EasyMock.createMock(org.apache.ambari.server.view.ViewExtractor.class);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(10);
        resourceTypeEntity.setName("MY_VIEW{1.0.0}");
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewDefinition.setResourceType(resourceTypeEntity);
        java.util.Set<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceEntities = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntities(viewDefinition);
        viewDefinition.setInstances(viewInstanceEntities);
        java.util.Map<java.io.File, org.apache.ambari.server.view.configuration.ViewConfig> viewConfigs = java.util.Collections.singletonMap(viewArchive, viewDefinition.getConfiguration());
        long resourceId = 99L;
        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity : viewInstanceEntities) {
            org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
            resourceEntity.setId(resourceId);
            resourceEntity.setResourceType(resourceTypeEntity);
            viewInstanceEntity.setResource(resourceEntity);
        }
        java.util.Map<java.lang.String, java.io.File> files = new java.util.HashMap<>();
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            files.put("\\var\\lib\\ambari-server\\resources\\views\\my_view-1.0.0.jar", viewArchive);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work", extractedArchiveDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}", archiveDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\view.xml", entryFile);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\WEB-INF\\classes", classesDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\WEB-INF\\lib", libDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\META-INF", metaInfDir);
        } else {
            files.put("/var/lib/ambari-server/resources/views/my_view-1.0.0.jar", viewArchive);
            files.put("/var/lib/ambari-server/resources/views/work", extractedArchiveDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}", archiveDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/view.xml", entryFile);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/WEB-INF/classes", classesDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/WEB-INF/lib", libDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/META-INF", metaInfDir);
        }
        java.util.Map<java.io.File, java.io.FileOutputStream> outputStreams = new java.util.HashMap<>();
        outputStreams.put(entryFile, fos);
        java.util.Map<java.io.File, java.util.jar.JarInputStream> jarFiles = new java.util.HashMap<>();
        jarFiles.put(viewArchive, viewJarFile);
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.getViewsDir()).andReturn(viewDir);
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            EasyMock.expect(viewDir.getAbsolutePath()).andReturn("\\var\\lib\\ambari-server\\resources\\views");
        } else {
            EasyMock.expect(viewDir.getAbsolutePath()).andReturn("/var/lib/ambari-server/resources/views");
        }
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.getViewExtractionThreadPoolCoreSize()).andReturn(2).anyTimes();
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.getViewExtractionThreadPoolMaxSize()).andReturn(3).anyTimes();
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.configuration.getViewExtractionThreadPoolTimeout()).andReturn(10000L).anyTimes();
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            EasyMock.expect(viewArchive.getAbsolutePath()).andReturn("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}").anyTimes();
        } else {
            EasyMock.expect(viewArchive.getAbsolutePath()).andReturn("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}").anyTimes();
        }
        EasyMock.expect(archiveDir.exists()).andReturn(false);
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            EasyMock.expect(archiveDir.getAbsolutePath()).andReturn("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}").anyTimes();
        } else {
            EasyMock.expect(archiveDir.getAbsolutePath()).andReturn("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}").anyTimes();
        }
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ViewEntity> viewEntityCapture = org.easymock.EasyMock.newCapture();
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            EasyMock.expect(viewExtractor.ensureExtractedArchiveDirectory("\\var\\lib\\ambari-server\\resources\\views\\work")).andReturn(true);
        } else {
            EasyMock.expect(viewExtractor.ensureExtractedArchiveDirectory("/var/lib/ambari-server/resources/views/work")).andReturn(true);
        }
        EasyMock.expect(viewExtractor.extractViewArchive(EasyMock.capture(viewEntityCapture), EasyMock.eq(viewArchive), EasyMock.eq(archiveDir), EasyMock.anyObject(java.util.List.class))).andReturn(null);
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.configuration, viewDir, extractedArchiveDir, viewArchive, archiveDir, entryFile, classesDir, libDir, metaInfDir, fileEntry, viewJarFile, jarEntry, is, fos, viewExtractor, org.apache.ambari.server.view.ViewRegistryTest.resourceDAO, org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO);
        org.apache.ambari.server.view.ViewRegistryTest.TestViewArchiveUtility archiveUtility = new org.apache.ambari.server.view.ViewRegistryTest.TestViewArchiveUtility(viewConfigs, files, outputStreams, jarFiles, false);
        org.apache.ambari.server.view.ViewRegistryTest.TestViewModule module = new org.apache.ambari.server.view.ViewRegistryTest.TestViewModule(viewExtractor, archiveUtility, org.apache.ambari.server.view.ViewRegistryTest.configuration);
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            org.junit.Assert.assertTrue(org.apache.ambari.server.view.ViewRegistry.extractViewArchive("\\var\\lib\\ambari-server\\resources\\views\\my_view-1.0.0.jar", module, true));
        } else {
            org.junit.Assert.assertTrue(org.apache.ambari.server.view.ViewRegistry.extractViewArchive("/var/lib/ambari-server/resources/views/my_view-1.0.0.jar", module, true));
        }
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.configuration, viewDir, extractedArchiveDir, viewArchive, archiveDir, entryFile, classesDir, libDir, metaInfDir, fileEntry, viewJarFile, jarEntry, is, fos, viewExtractor, org.apache.ambari.server.view.ViewRegistryTest.resourceDAO, org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO);
    }

    @org.junit.Test
    public void testSetViewInstanceRoleAccess() throws java.lang.Exception {
        final java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.PermissionEntity> permissions = new java.util.HashMap<>();
        permissions.put("CLUSTER.ADMINISTRATOR", org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministratorPermission());
        permissions.put("CLUSTER.OPERATOR", org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperatorPermission());
        permissions.put("SERVICE.ADMINISTRATOR", org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministratorPermission());
        permissions.put("SERVICE.OPERATOR", org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperatorPermission());
        permissions.put("CLUSTER.USER", org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUserPermission());
        org.apache.ambari.server.orm.entities.PermissionEntity permissionViewUser = org.apache.ambari.server.security.TestAuthenticationFactory.createViewUserPermission();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = viewInstanceEntity.getResource();
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.PrivilegeEntity> expectedPrivileges = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.orm.entities.PermissionEntity> entry : permissions.entrySet()) {
            if (!entry.getKey().equals("CLUSTER.ADMINISTRATOR")) {
                expectedPrivileges.put(entry.getKey(), org.apache.ambari.server.security.TestAuthenticationFactory.createPrivilegeEntity(resourceEntity, permissionViewUser, entry.getValue().getPrincipal()));
            }
        }
        org.easymock.Capture<org.apache.ambari.server.orm.entities.PrivilegeEntity> captureCreatedPrivilegeEntity = org.easymock.Capture.newInstance(CaptureType.ALL);
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.orm.entities.PermissionEntity> entry : permissions.entrySet()) {
            EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.permissionDAO.findByName(entry.getKey())).andReturn(entry.getValue()).atLeastOnce();
        }
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.permissionDAO.findViewUsePermission()).andReturn(permissionViewUser).atLeastOnce();
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.privilegeDAO.exists(org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class), EasyMock.eq(resourceEntity), EasyMock.eq(permissionViewUser))).andAnswer(new org.easymock.IAnswer<java.lang.Boolean>() {
            @java.lang.Override
            public java.lang.Boolean answer() throws java.lang.Throwable {
                return org.easymock.EasyMock.getCurrentArguments()[0] == permissions.get("CLUSTER.ADMINISTRATOR").getPrincipal();
            }
        }).anyTimes();
        org.apache.ambari.server.view.ViewRegistryTest.privilegeDAO.create(EasyMock.capture(captureCreatedPrivilegeEntity));
        EasyMock.expectLastCall().times(expectedPrivileges.size());
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.privilegeDAO, org.apache.ambari.server.view.ViewRegistryTest.permissionDAO);
        org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        viewRegistry.setViewInstanceRoleAccess(viewInstanceEntity, permissions.keySet());
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.privilegeDAO, org.apache.ambari.server.view.ViewRegistryTest.permissionDAO);
        org.junit.Assert.assertTrue(expectedPrivileges.size() != permissions.size());
        org.junit.Assert.assertTrue(captureCreatedPrivilegeEntity.hasCaptured());
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> capturedValues = captureCreatedPrivilegeEntity.getValues();
        org.junit.Assert.assertNotNull(capturedValues);
        java.util.Set<org.apache.ambari.server.orm.entities.PrivilegeEntity> uniqueCapturedValues = new java.util.HashSet<>(capturedValues);
        org.junit.Assert.assertEquals(expectedPrivileges.size(), uniqueCapturedValues.size());
        for (org.apache.ambari.server.orm.entities.PrivilegeEntity capturedValue : uniqueCapturedValues) {
            org.junit.Assert.assertTrue(expectedPrivileges.containsValue(capturedValue));
        }
    }

    public static class TestViewModule extends org.apache.ambari.server.view.ViewRegistry.ViewModule {
        private final org.apache.ambari.server.view.ViewExtractor extractor;

        private final org.apache.ambari.server.view.ViewArchiveUtility archiveUtility;

        private final org.apache.ambari.server.configuration.Configuration configuration;

        public TestViewModule(org.apache.ambari.server.view.ViewExtractor extractor, org.apache.ambari.server.view.ViewArchiveUtility archiveUtility, org.apache.ambari.server.configuration.Configuration configuration) {
            this.extractor = extractor;
            this.archiveUtility = archiveUtility;
            this.configuration = configuration;
        }

        @java.lang.Override
        protected void configure() {
            bind(org.apache.ambari.server.view.ViewExtractor.class).toInstance(extractor);
            bind(org.apache.ambari.server.view.ViewArchiveUtility.class).toInstance(archiveUtility);
            bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
            org.apache.ambari.server.state.stack.OsFamily osFamily = EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
            EasyMock.replay(osFamily);
            bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(osFamily);
        }
    }

    public static class TestViewArchiveUtility extends org.apache.ambari.server.view.ViewArchiveUtility {
        private final java.util.Map<java.io.File, org.apache.ambari.server.view.configuration.ViewConfig> viewConfigs;

        private final java.util.Map<java.lang.String, java.io.File> files;

        private final java.util.Map<java.io.File, java.io.FileOutputStream> outputStreams;

        private final java.util.Map<java.io.File, java.util.jar.JarInputStream> jarFiles;

        private final boolean badArchive;

        private boolean deploymentFailed = false;

        public TestViewArchiveUtility(java.util.Map<java.io.File, org.apache.ambari.server.view.configuration.ViewConfig> viewConfigs, java.util.Map<java.lang.String, java.io.File> files, java.util.Map<java.io.File, java.io.FileOutputStream> outputStreams, java.util.Map<java.io.File, java.util.jar.JarInputStream> jarFiles, boolean badArchive) {
            this.viewConfigs = viewConfigs;
            this.files = files;
            this.outputStreams = outputStreams;
            this.jarFiles = jarFiles;
            this.badArchive = badArchive;
        }

        @java.lang.Override
        public org.apache.ambari.server.view.configuration.ViewConfig getViewConfigFromArchive(java.io.File archiveFile) throws java.net.MalformedURLException, javax.xml.bind.JAXBException {
            if (badArchive) {
                deploymentFailed = true;
                throw new java.lang.IllegalStateException("Bad archive");
            }
            return viewConfigs.get(archiveFile);
        }

        @java.lang.Override
        public org.apache.ambari.server.view.configuration.ViewConfig getViewConfigFromExtractedArchive(java.lang.String archivePath, boolean validate) throws javax.xml.bind.JAXBException, java.io.FileNotFoundException {
            if (badArchive) {
                deploymentFailed = true;
                throw new java.lang.IllegalStateException("Bad archive");
            }
            for (java.io.File viewConfigKey : viewConfigs.keySet()) {
                if (viewConfigKey.getAbsolutePath().equals(archivePath)) {
                    return viewConfigs.get(viewConfigKey);
                }
            }
            return null;
        }

        @java.lang.Override
        public java.io.File getFile(java.lang.String path) {
            return files.get(path);
        }

        @java.lang.Override
        public java.io.FileOutputStream getFileOutputStream(java.io.File file) throws java.io.FileNotFoundException {
            return outputStreams.get(file);
        }

        @java.lang.Override
        public java.util.jar.JarInputStream getJarFileStream(java.io.File file) throws java.io.IOException {
            return jarFiles.get(file);
        }

        public boolean isDeploymentFailed() {
            return deploymentFailed;
        }
    }

    private static class TestListener implements org.apache.ambari.view.events.Listener {
        private org.apache.ambari.view.events.Event lastEvent = null;

        @java.lang.Override
        public void notify(org.apache.ambari.view.events.Event event) {
            lastEvent = event;
        }

        public org.apache.ambari.view.events.Event getLastEvent() {
            return lastEvent;
        }

        public void clear() {
            lastEvent = null;
        }
    }

    public static org.apache.ambari.server.view.ViewRegistry getRegistry(org.apache.ambari.server.orm.dao.ViewDAO viewDAO, org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO, org.apache.ambari.server.orm.dao.UserDAO userDAO, org.apache.ambari.server.orm.dao.MemberDAO memberDAO, org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO, org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO, org.apache.ambari.server.orm.dao.ResourceDAO resourceDAO, org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO, org.apache.ambari.server.security.SecurityHelper securityHelper, org.apache.ambari.server.view.ViewInstanceHandlerList handlerList, org.apache.ambari.server.view.ViewExtractor viewExtractor, org.apache.ambari.server.view.ViewArchiveUtility archiveUtility, org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo) {
        return org.apache.ambari.server.view.ViewRegistryTest.getRegistry(null, viewDAO, viewInstanceDAO, userDAO, memberDAO, privilegeDAO, permissionDAO, resourceDAO, resourceTypeDAO, securityHelper, handlerList, viewExtractor, archiveUtility, ambariMetaInfo, null);
    }

    public static org.apache.ambari.server.view.ViewRegistry getRegistry(org.apache.ambari.server.view.ViewInstanceOperationHandler viewInstanceOperationHandler, org.apache.ambari.server.orm.dao.ViewDAO viewDAO, org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO, org.apache.ambari.server.orm.dao.UserDAO userDAO, org.apache.ambari.server.orm.dao.MemberDAO memberDAO, org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO, org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO, org.apache.ambari.server.orm.dao.ResourceDAO resourceDAO, org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO, org.apache.ambari.server.security.SecurityHelper securityHelper, org.apache.ambari.server.view.ViewInstanceHandlerList handlerList, org.apache.ambari.server.view.ViewExtractor viewExtractor, org.apache.ambari.server.view.ViewArchiveUtility archiveUtility, org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo, org.apache.ambari.server.state.Clusters clusters) {
        org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        EasyMock.replay(publisher);
        org.apache.ambari.server.view.ViewRegistry instance = new org.apache.ambari.server.view.ViewRegistry(publisher);
        instance.viewInstanceOperationHandler = viewInstanceOperationHandler;
        instance.viewDAO = viewDAO;
        instance.resourceDAO = resourceDAO;
        instance.instanceDAO = viewInstanceDAO;
        instance.userDAO = userDAO;
        instance.memberDAO = memberDAO;
        instance.privilegeDAO = privilegeDAO;
        instance.resourceTypeDAO = resourceTypeDAO;
        instance.permissionDAO = permissionDAO;
        instance.securityHelper = securityHelper;
        instance.configuration = org.apache.ambari.server.view.ViewRegistryTest.configuration;
        instance.handlerList = handlerList;
        instance.extractor = (viewExtractor == null) ? new org.apache.ambari.server.view.ViewExtractor() : viewExtractor;
        instance.archiveUtility = (archiveUtility == null) ? new org.apache.ambari.server.view.ViewArchiveUtility() : archiveUtility;
        instance.extractor.archiveUtility = instance.archiveUtility;
        final org.apache.ambari.server.api.services.AmbariMetaInfo finalMetaInfo = ambariMetaInfo;
        instance.ambariMetaInfoProvider = new com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo>() {
            @java.lang.Override
            public org.apache.ambari.server.api.services.AmbariMetaInfo get() {
                return finalMetaInfo;
            }
        };
        final org.apache.ambari.server.state.Clusters finalClusters = clusters;
        instance.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return finalClusters;
            }
        };
        return instance;
    }

    public static org.apache.ambari.server.orm.entities.ViewEntity getViewEntity(org.apache.ambari.server.view.configuration.ViewConfig viewConfig, org.apache.ambari.server.configuration.Configuration ambariConfig, java.lang.ClassLoader cl, java.lang.String archivePath) throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistryTest.getRegistry(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.userDAO, org.apache.ambari.server.view.ViewRegistryTest.memberDAO, org.apache.ambari.server.view.ViewRegistryTest.privilegeDAO, org.apache.ambari.server.view.ViewRegistryTest.permissionDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceTypeDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.handlerList, null, null, null);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = new org.apache.ambari.server.orm.entities.ViewEntity(viewConfig, ambariConfig, archivePath);
        registry.setupViewDefinition(viewDefinition, cl);
        return viewDefinition;
    }

    public static org.apache.ambari.server.orm.entities.ViewInstanceEntity getViewInstanceEntity(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition, org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig) throws java.lang.Exception {
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistryTest.getRegistry(org.apache.ambari.server.view.ViewRegistryTest.viewDAO, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.userDAO, org.apache.ambari.server.view.ViewRegistryTest.memberDAO, org.apache.ambari.server.view.ViewRegistryTest.privilegeDAO, org.apache.ambari.server.view.ViewRegistryTest.permissionDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceDAO, org.apache.ambari.server.view.ViewRegistryTest.resourceTypeDAO, org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.handlerList, null, null, null);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        for (org.apache.ambari.server.view.configuration.PropertyConfig propertyConfig : instanceConfig.getProperties()) {
            viewInstanceDefinition.putProperty(propertyConfig.getKey(), propertyConfig.getValue());
        }
        registry.bindViewInstance(viewDefinition, viewInstanceDefinition);
        return viewInstanceDefinition;
    }

    private void testOnAmbariEventServiceCreation(java.lang.String xml, java.util.Set<java.lang.String> serviceNames, java.lang.String stackName, boolean success) throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(xml);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity(config);
        org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        registry.addDefinition(viewDefinition);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stackName);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> serviceMap = new java.util.HashMap<>();
        for (java.lang.String serviceName : serviceNames) {
            serviceMap.put(serviceName, service);
            EasyMock.expect(cluster.getService(serviceName)).andReturn(service);
        }
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.clusters.getClusterById(99L)).andReturn(cluster);
        EasyMock.expect(cluster.getClusterName()).andReturn("c1").anyTimes();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(stackId).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(serviceMap).anyTimes();
        EasyMock.expect(service.getDesiredStackId()).andReturn(stackId).anyTimes();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.merge(EasyMock.capture(viewInstanceCapture))).andReturn(viewInstanceEntity).anyTimes();
        EasyMock.expect(org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO.findByName("MY_VIEW{1.0.0}", "AUTO-INSTANCE")).andReturn(viewInstanceEntity).anyTimes();
        EasyMock.replay(org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.configuration, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.clusters, cluster, service, viewInstanceEntity);
        org.apache.ambari.server.events.ServiceInstalledEvent event = new org.apache.ambari.server.events.ServiceInstalledEvent(99L, "HDP", "2.0", "HIVE");
        registry.onAmbariEvent(event);
        if (success) {
            org.junit.Assert.assertEquals(viewInstanceCapture.getValue(), registry.getInstanceDefinition("MY_VIEW", "1.0.0", "AUTO-INSTANCE"));
        }
        EasyMock.verify(org.apache.ambari.server.view.ViewRegistryTest.securityHelper, org.apache.ambari.server.view.ViewRegistryTest.configuration, org.apache.ambari.server.view.ViewRegistryTest.viewInstanceDAO, org.apache.ambari.server.view.ViewRegistryTest.clusters, cluster, viewInstanceEntity);
    }
}