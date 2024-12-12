package org.apache.ambari.server.cleanup;
import org.easymock.Capture;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
public class CleanupServiceImplTest {
    private static final java.lang.String CLUSTER_NAME = "cluster-1";

    private static final java.lang.Long FROM_DATE_TIMESTAMP = 10L;

    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock
    private org.apache.ambari.server.orm.dao.Cleanable cleanableDao;

    private org.apache.ambari.server.cleanup.CleanupServiceImpl cleanupServiceImpl;

    private org.apache.ambari.server.cleanup.TimeBasedCleanupPolicy cleanupPolicy;

    private org.easymock.Capture<org.apache.ambari.server.cleanup.TimeBasedCleanupPolicy> timeBasedCleanupPolicyCapture;

    private java.util.Set<org.apache.ambari.server.orm.dao.Cleanable> cleanables;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        EasyMock.reset(cleanableDao);
        timeBasedCleanupPolicyCapture = EasyMock.newCapture();
        cleanupPolicy = new org.apache.ambari.server.cleanup.TimeBasedCleanupPolicy(org.apache.ambari.server.cleanup.CleanupServiceImplTest.CLUSTER_NAME, org.apache.ambari.server.cleanup.CleanupServiceImplTest.FROM_DATE_TIMESTAMP);
    }

    @org.junit.Test
    public void testShouldDaosBeCalledWithTheCleanupPolicy() throws java.lang.Exception {
        cleanables = new java.util.HashSet<>();
        cleanables.add(cleanableDao);
        EasyMock.expect(cleanableDao.cleanup(EasyMock.capture(timeBasedCleanupPolicyCapture))).andReturn(2L);
        EasyMock.replay(cleanableDao);
        cleanupServiceImpl = new org.apache.ambari.server.cleanup.CleanupServiceImpl(cleanables);
        cleanupServiceImpl.cleanup(cleanupPolicy);
        junit.framework.Assert.assertNotNull("The argument is null", timeBasedCleanupPolicyCapture.getValue());
        junit.framework.Assert.assertEquals("The cluster name is wrong!", timeBasedCleanupPolicyCapture.getValue().getClusterName(), org.apache.ambari.server.cleanup.CleanupServiceImplTest.CLUSTER_NAME);
        junit.framework.Assert.assertEquals("The to date is wrong!", timeBasedCleanupPolicyCapture.getValue().getToDateInMillis(), org.apache.ambari.server.cleanup.CleanupServiceImplTest.FROM_DATE_TIMESTAMP);
    }

    @org.junit.Test
    public void testAffectedRowsNoError() throws java.lang.Exception {
        cleanables = new java.util.HashSet<>();
        cleanables.add(cleanableDao);
        EasyMock.expect(cleanableDao.cleanup(cleanupPolicy)).andReturn(2L);
        EasyMock.replay(cleanableDao);
        cleanupServiceImpl = new org.apache.ambari.server.cleanup.CleanupServiceImpl(cleanables);
        org.apache.ambari.server.cleanup.CleanupService.CleanupResult res = cleanupServiceImpl.cleanup(cleanupPolicy);
        junit.framework.Assert.assertEquals("The affected rows count is wrong", 2L, res.getAffectedRows());
        junit.framework.Assert.assertEquals("The error count is wrong", 0L, res.getErrorCount());
    }

    @org.junit.Test
    public void testAffectedRowsWithErrors() throws java.lang.Exception {
        cleanables = new java.util.HashSet<>();
        cleanables.add(cleanableDao);
        EasyMock.expect(cleanableDao.cleanup(cleanupPolicy)).andThrow(new java.lang.RuntimeException());
        EasyMock.replay(cleanableDao);
        cleanupServiceImpl = new org.apache.ambari.server.cleanup.CleanupServiceImpl(cleanables);
        org.apache.ambari.server.cleanup.CleanupService.CleanupResult res = cleanupServiceImpl.cleanup(cleanupPolicy);
        junit.framework.Assert.assertEquals("The affected rows count is wrong", 0L, res.getAffectedRows());
        junit.framework.Assert.assertEquals("The error count is wrong", 1L, res.getErrorCount());
    }
}