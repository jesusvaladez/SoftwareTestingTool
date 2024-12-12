package org.apache.ambari.view.pig.test;
import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.replay;
public class IntegrationalTest extends org.apache.ambari.view.pig.HDFSTest {
    private org.apache.ambari.view.pig.resources.jobs.JobService jobService;

    private org.apache.ambari.view.pig.resources.scripts.ScriptService scriptService;

    @org.junit.BeforeClass
    public static void startUp() throws java.lang.Exception {
        org.apache.ambari.view.pig.HDFSTest.startUp();
    }

    @org.junit.AfterClass
    public static void shutDown() throws java.lang.Exception {
        org.apache.ambari.view.pig.HDFSTest.shutDown();
        org.apache.ambari.view.utils.UserLocal.dropAllConnections(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
    }

    @java.lang.Override
    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        super.setUp();
        jobService = org.apache.ambari.view.pig.BasePigTest.getService(org.apache.ambari.view.pig.resources.jobs.JobService.class, handler, context);
        scriptService = org.apache.ambari.view.pig.BasePigTest.getService(org.apache.ambari.view.pig.resources.scripts.ScriptService.class, handler, context);
    }

    @java.lang.Override
    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        super.tearDown();
        org.apache.ambari.view.utils.UserLocal.dropAllConnections(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.utils.UserLocal.dropAllConnections(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
    }

    @org.junit.Test
    public void testHdfsApiDependsOnInstance() throws java.lang.Exception {
        org.apache.ambari.view.utils.UserLocal.dropAllConnections(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        org.apache.ambari.view.ViewContext context1 = createNiceMock(org.apache.ambari.view.ViewContext.class);
        org.apache.ambari.view.ViewContext context2 = createNiceMock(org.apache.ambari.view.ViewContext.class);
        org.apache.ambari.view.ViewContext context3 = createNiceMock(org.apache.ambari.view.ViewContext.class);
        expect(context1.getProperties()).andReturn(properties).anyTimes();
        expect(context1.getUsername()).andReturn("ambari-qa").anyTimes();
        expect(context1.getInstanceName()).andReturn("Pig1").anyTimes();
        expect(context2.getProperties()).andReturn(properties).anyTimes();
        expect(context2.getUsername()).andReturn("ambari-qa").anyTimes();
        expect(context2.getInstanceName()).andReturn("Pig2").anyTimes();
        expect(context3.getProperties()).andReturn(properties).anyTimes();
        expect(context3.getUsername()).andReturn("ambari-qa").anyTimes();
        expect(context3.getInstanceName()).andReturn("Pig1").anyTimes();
        EasyMock.replay(context1, context2, context3);
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi1 = org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context1);
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi2 = org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context2);
        org.junit.Assert.assertNotSame(hdfsApi1, hdfsApi2);
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi1_2 = org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context1);
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi2_2 = org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context1);
        org.junit.Assert.assertSame(hdfsApi1_2, hdfsApi2_2);
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi1_3 = org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context1);
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi3_3 = org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context3);
        org.junit.Assert.assertSame(hdfsApi1_3, hdfsApi3_3);
    }

    @org.junit.Test
    public void testStorageDependsOnInstance() throws java.lang.Exception {
        org.apache.ambari.view.pig.persistence.utils.StorageUtil.dropAllConnections();
        org.apache.ambari.view.ViewContext context1 = createNiceMock(org.apache.ambari.view.ViewContext.class);
        org.apache.ambari.view.ViewContext context2 = createNiceMock(org.apache.ambari.view.ViewContext.class);
        org.apache.ambari.view.ViewContext context3 = createNiceMock(org.apache.ambari.view.ViewContext.class);
        expect(context1.getProperties()).andReturn(properties).anyTimes();
        expect(context1.getUsername()).andReturn("ambari-qa").anyTimes();
        expect(context1.getInstanceName()).andReturn("Pig1").anyTimes();
        expect(context2.getProperties()).andReturn(properties).anyTimes();
        expect(context2.getUsername()).andReturn("ambari-qa").anyTimes();
        expect(context2.getInstanceName()).andReturn("Pig2").anyTimes();
        expect(context3.getProperties()).andReturn(properties).anyTimes();
        expect(context3.getUsername()).andReturn("ambari-qa").anyTimes();
        expect(context3.getInstanceName()).andReturn("Pig1").anyTimes();
        EasyMock.replay(context1, context2, context3);
        org.apache.ambari.view.pig.persistence.Storage storage1 = org.apache.ambari.view.pig.persistence.utils.StorageUtil.getInstance(context1).getStorage();
        org.apache.ambari.view.pig.persistence.Storage storage2 = org.apache.ambari.view.pig.persistence.utils.StorageUtil.getInstance(context2).getStorage();
        org.junit.Assert.assertNotSame(storage1, storage2);
        org.apache.ambari.view.pig.persistence.Storage storage1_2 = org.apache.ambari.view.pig.persistence.utils.StorageUtil.getInstance(context1).getStorage();
        org.apache.ambari.view.pig.persistence.Storage storage2_2 = org.apache.ambari.view.pig.persistence.utils.StorageUtil.getInstance(context1).getStorage();
        org.junit.Assert.assertSame(storage1_2, storage2_2);
        org.apache.ambari.view.pig.persistence.Storage storage1_3 = org.apache.ambari.view.pig.persistence.utils.StorageUtil.getInstance(context1).getStorage();
        org.apache.ambari.view.pig.persistence.Storage storage3_3 = org.apache.ambari.view.pig.persistence.utils.StorageUtil.getInstance(context3).getStorage();
        org.junit.Assert.assertSame(storage1_3, storage3_3);
    }
}