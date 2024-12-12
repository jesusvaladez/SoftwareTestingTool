package org.apache.ambari.server.configuration;
import org.awaitility.core.ConditionTimeoutException;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
public class SingleFileWatchTest {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder tmp = new org.junit.rules.TemporaryFolder();

    private org.apache.ambari.server.configuration.SingleFileWatch watchDog;

    private java.io.File fileToWatch;

    private volatile int numberOfEventsReceived = 0;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        tmp.create();
        fileToWatch = tmp.newFile();
        watchDog = new org.apache.ambari.server.configuration.SingleFileWatch(fileToWatch, file -> numberOfEventsReceived++);
        watchDog.start();
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        watchDog.stop();
    }

    @org.junit.Test
    public void testTriggersEventsOnMultipleFileChange() throws java.lang.Exception {
        changeFile("change1");
        hasReceivedChangeEvents(1);
        changeFile("change2");
        hasReceivedChangeEvents(2);
        changeFile("change3");
        hasReceivedChangeEvents(3);
    }

    private void hasReceivedChangeEvents(int expectedNumberOfEvents) {
        try {
            Awaitility.await().atMost(8, java.util.concurrent.TimeUnit.SECONDS).until(() -> numberOfEventsReceived == expectedNumberOfEvents);
        } catch (org.awaitility.core.ConditionTimeoutException e) {
            org.junit.Assert.fail((("Expected number of file change events: " + expectedNumberOfEvents) + " but received: ") + numberOfEventsReceived);
        }
    }

    private void changeFile(java.lang.String content) throws java.lang.Exception {
        long lastModified = fileToWatch.lastModified();
        while (lastModified == fileToWatch.lastModified()) {
            org.apache.commons.io.FileUtils.writeStringToFile(fileToWatch, content, "UTF-8");
        } 
    }
}