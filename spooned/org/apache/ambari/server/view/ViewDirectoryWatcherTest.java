package org.apache.ambari.server.view;
import javax.annotation.Nullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class ViewDirectoryWatcherTest {
    private static final org.apache.ambari.server.configuration.Configuration configuration = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);

    private static final org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);

    private java.io.File testDir;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        EasyMock.reset(org.apache.ambari.server.view.ViewDirectoryWatcherTest.configuration, org.apache.ambari.server.view.ViewDirectoryWatcherTest.viewRegistry);
        testDir = new java.io.File(java.lang.System.getProperty("java.io.tmpdir"), "test_dir");
        if (testDir.exists()) {
            org.apache.commons.io.FileDeleteStrategy.FORCE.delete(testDir);
        }
        testDir.mkdirs();
    }

    @org.junit.Test
    public void testDirectoryWatcherStart() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewDirectoryWatcher viewDirectoryWatcher = new org.apache.ambari.server.view.ViewDirectoryWatcher();
        EasyMock.expect(org.apache.ambari.server.view.ViewDirectoryWatcherTest.configuration.getViewsDir()).andReturn(testDir).once();
        viewDirectoryWatcher.configuration = org.apache.ambari.server.view.ViewDirectoryWatcherTest.configuration;
        viewDirectoryWatcher.viewRegistry = org.apache.ambari.server.view.ViewDirectoryWatcherTest.viewRegistry;
        EasyMock.replay(org.apache.ambari.server.view.ViewDirectoryWatcherTest.configuration);
        final java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
        viewDirectoryWatcher.addHook(new com.google.common.base.Function<java.nio.file.Path, java.lang.Boolean>() {
            @javax.annotation.Nullable
            @java.lang.Override
            public java.lang.Boolean apply(@javax.annotation.Nullable
            java.nio.file.Path path) {
                countDownLatch.countDown();
                return true;
            }
        });
        viewDirectoryWatcher.start();
        countDownLatch.await(1, java.util.concurrent.TimeUnit.SECONDS);
        org.junit.Assert.assertTrue(viewDirectoryWatcher.isRunning());
        EasyMock.verify(org.apache.ambari.server.view.ViewDirectoryWatcherTest.configuration);
    }

    @org.junit.Test
    public void testDirectoryExtractionOnFileAdd() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewDirectoryWatcher viewDirectoryWatcher = new org.apache.ambari.server.view.ViewDirectoryWatcher();
        EasyMock.expect(org.apache.ambari.server.view.ViewDirectoryWatcherTest.configuration.getViewsDir()).andReturn(testDir).once();
        viewDirectoryWatcher.configuration = org.apache.ambari.server.view.ViewDirectoryWatcherTest.configuration;
        viewDirectoryWatcher.viewRegistry = org.apache.ambari.server.view.ViewDirectoryWatcherTest.viewRegistry;
        org.apache.ambari.server.view.ViewDirectoryWatcherTest.viewRegistry.readViewArchive(java.nio.file.Paths.get(testDir.getAbsolutePath(), "file.jar"));
        EasyMock.replay(org.apache.ambari.server.view.ViewDirectoryWatcherTest.configuration, org.apache.ambari.server.view.ViewDirectoryWatcherTest.viewRegistry);
        final java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
        viewDirectoryWatcher.addHook(new com.google.common.base.Function<java.nio.file.Path, java.lang.Boolean>() {
            @javax.annotation.Nullable
            @java.lang.Override
            public java.lang.Boolean apply(@javax.annotation.Nullable
            java.nio.file.Path path) {
                countDownLatch.countDown();
                return true;
            }
        });
        viewDirectoryWatcher.start();
        createZipFile();
        countDownLatch.await(30, java.util.concurrent.TimeUnit.SECONDS);
        EasyMock.verify(org.apache.ambari.server.view.ViewDirectoryWatcherTest.configuration, org.apache.ambari.server.view.ViewDirectoryWatcherTest.viewRegistry);
    }

    @org.junit.Test
    public void testDirectoryWatcherStop() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewDirectoryWatcher viewDirectoryWatcher = new org.apache.ambari.server.view.ViewDirectoryWatcher();
        EasyMock.expect(org.apache.ambari.server.view.ViewDirectoryWatcherTest.configuration.getViewsDir()).andReturn(testDir).once();
        viewDirectoryWatcher.configuration = org.apache.ambari.server.view.ViewDirectoryWatcherTest.configuration;
        viewDirectoryWatcher.viewRegistry = org.apache.ambari.server.view.ViewDirectoryWatcherTest.viewRegistry;
        EasyMock.replay(org.apache.ambari.server.view.ViewDirectoryWatcherTest.configuration);
        viewDirectoryWatcher.start();
        java.lang.Thread.sleep(100);
        viewDirectoryWatcher.stop();
        org.junit.Assert.assertFalse(viewDirectoryWatcher.isRunning());
        EasyMock.verify(org.apache.ambari.server.view.ViewDirectoryWatcherTest.configuration);
    }

    private void createZipFile() throws java.io.IOException {
        java.io.File file = new java.io.File((java.lang.System.getProperty("java.io.tmpdir") + java.io.File.separator) + "view.xml");
        file.createNewFile();
        java.io.FileInputStream in = new java.io.FileInputStream(file);
        java.util.zip.ZipOutputStream out = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(new java.io.File(testDir, "file.jar")));
        out.putNextEntry(new java.util.zip.ZipEntry("view.xml"));
        byte[] b = new byte[1024];
        int count;
        while ((count = in.read(b)) > 0) {
            java.lang.System.out.println();
            out.write(b, 0, count);
        } 
        out.close();
        in.close();
    }
}