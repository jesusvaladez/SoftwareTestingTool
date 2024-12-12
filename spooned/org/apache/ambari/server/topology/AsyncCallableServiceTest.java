package org.apache.ambari.server.topology;
import org.easymock.Capture;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.captureLong;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class AsyncCallableServiceTest extends org.easymock.EasyMockSupport {
    private static final long TIMEOUT = 1000;

    private static final long RETRY_DELAY = 50;

    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private java.util.concurrent.Callable<java.lang.Boolean> taskMock;

    @org.easymock.Mock
    private java.util.concurrent.ScheduledExecutorService executorServiceMock;

    @org.easymock.Mock
    private java.util.concurrent.ScheduledFuture<java.lang.Boolean> futureMock;

    @org.easymock.Mock
    private java.util.function.Consumer<java.lang.Throwable> onErrorMock;

    private org.apache.ambari.server.topology.AsyncCallableService<java.lang.Boolean> asyncCallableService;

    @org.junit.Test
    public void testCallableServiceShouldCancelTaskWhenTimeoutExceeded() throws java.lang.Exception {
        long timeout = -1;
        java.util.concurrent.TimeoutException timeoutException = new java.util.concurrent.TimeoutException("Testing the timeout exceeded case");
        EasyMock.expect(futureMock.get(timeout, java.util.concurrent.TimeUnit.MILLISECONDS)).andThrow(timeoutException);
        EasyMock.expect(futureMock.isDone()).andReturn(java.lang.Boolean.FALSE);
        EasyMock.expect(futureMock.cancel(true)).andReturn(java.lang.Boolean.TRUE);
        EasyMock.expect(executorServiceMock.submit(taskMock)).andReturn(futureMock);
        onErrorMock.accept(timeoutException);
        replayAll();
        asyncCallableService = new org.apache.ambari.server.topology.AsyncCallableService<>(taskMock, timeout, org.apache.ambari.server.topology.AsyncCallableServiceTest.RETRY_DELAY, "test", executorServiceMock, onErrorMock);
        java.lang.Boolean serviceResult = asyncCallableService.call();
        verifyAll();
        org.junit.Assert.assertNull("No result expected in case of timeout", serviceResult);
    }

    @org.junit.Test
    public void lastErrorIsReturnedIfSubsequentAttemptTimesOut() throws java.lang.Exception {
        java.lang.Exception computationException = new java.util.concurrent.ExecutionException(new java.lang.ArithmeticException("Computation error during first attempt"));
        java.lang.Exception timeoutException = new java.util.concurrent.TimeoutException("Timeout during second attempt");
        EasyMock.expect(futureMock.get(org.apache.ambari.server.topology.AsyncCallableServiceTest.TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS)).andThrow(computationException);
        EasyMock.expect(executorServiceMock.schedule(taskMock, org.apache.ambari.server.topology.AsyncCallableServiceTest.RETRY_DELAY, java.util.concurrent.TimeUnit.MILLISECONDS)).andReturn(futureMock);
        org.easymock.Capture<java.lang.Long> timeoutCapture = org.easymock.Capture.newInstance();
        EasyMock.expect(futureMock.get(EasyMock.captureLong(timeoutCapture), EasyMock.eq(java.util.concurrent.TimeUnit.MILLISECONDS))).andThrow(timeoutException);
        EasyMock.expect(futureMock.isDone()).andReturn(java.lang.Boolean.FALSE);
        EasyMock.expect(futureMock.cancel(true)).andReturn(java.lang.Boolean.TRUE);
        EasyMock.expect(executorServiceMock.submit(taskMock)).andReturn(futureMock);
        onErrorMock.accept(computationException.getCause());
        replayAll();
        asyncCallableService = new org.apache.ambari.server.topology.AsyncCallableService<>(taskMock, org.apache.ambari.server.topology.AsyncCallableServiceTest.TIMEOUT, org.apache.ambari.server.topology.AsyncCallableServiceTest.RETRY_DELAY, "test", executorServiceMock, onErrorMock);
        java.lang.Boolean serviceResult = asyncCallableService.call();
        verifyAll();
        org.junit.Assert.assertTrue(timeoutCapture.getValue() <= (org.apache.ambari.server.topology.AsyncCallableServiceTest.TIMEOUT - org.apache.ambari.server.topology.AsyncCallableServiceTest.RETRY_DELAY));
        org.junit.Assert.assertNull("No result expected in case of timeout", serviceResult);
    }

    @org.junit.Test
    public void testCallableServiceShouldCancelTaskWhenTaskHangsAndTimeoutExceeded() throws java.lang.Exception {
        java.util.concurrent.Callable<java.lang.Boolean> hangingTask = () -> {
            java.lang.Thread.sleep(10000000);
            return false;
        };
        onErrorMock.accept(EasyMock.anyObject(java.util.concurrent.TimeoutException.class));
        replayAll();
        asyncCallableService = new org.apache.ambari.server.topology.AsyncCallableService<>(hangingTask, org.apache.ambari.server.topology.AsyncCallableServiceTest.TIMEOUT, org.apache.ambari.server.topology.AsyncCallableServiceTest.RETRY_DELAY, "test", onErrorMock);
        java.lang.Boolean serviceResult = asyncCallableService.call();
        verifyAll();
        org.junit.Assert.assertNull("No result expected from hanging task", serviceResult);
    }

    @org.junit.Test
    public void testCallableServiceShouldExitWhenTaskCompleted() throws java.lang.Exception {
        EasyMock.expect(taskMock.call()).andReturn(java.lang.Boolean.TRUE);
        onErrorMock.accept(EasyMock.anyObject(java.util.concurrent.TimeoutException.class));
        EasyMock.expectLastCall().andThrow(new java.lang.AssertionError("No error expected")).anyTimes();
        replayAll();
        asyncCallableService = new org.apache.ambari.server.topology.AsyncCallableService<>(taskMock, org.apache.ambari.server.topology.AsyncCallableServiceTest.TIMEOUT, org.apache.ambari.server.topology.AsyncCallableServiceTest.RETRY_DELAY, "test", onErrorMock);
        java.lang.Boolean serviceResult = asyncCallableService.call();
        verifyAll();
        org.junit.Assert.assertEquals(java.lang.Boolean.TRUE, serviceResult);
    }

    @org.junit.Test
    public void testCallableServiceShouldRetryTaskExecutionTillTimeoutExceededWhenTaskThrowsException() throws java.lang.Exception {
        EasyMock.expect(taskMock.call()).andThrow(new java.lang.IllegalStateException("****************** TESTING ****************")).times(2, 3);
        onErrorMock.accept(EasyMock.anyObject(java.lang.IllegalStateException.class));
        replayAll();
        asyncCallableService = new org.apache.ambari.server.topology.AsyncCallableService<>(taskMock, org.apache.ambari.server.topology.AsyncCallableServiceTest.TIMEOUT, org.apache.ambari.server.topology.AsyncCallableServiceTest.RETRY_DELAY, "test", onErrorMock);
        java.lang.Boolean serviceResult = asyncCallableService.call();
        verifyAll();
        org.junit.Assert.assertNull("No result expected from throwing task", serviceResult);
    }

    @org.junit.Test
    public void testShouldAsyncCallableServiceRetryExecutionWhenTaskThrowsException() throws java.lang.Exception {
        java.util.concurrent.Callable<java.lang.Boolean> throwingTask = () -> {
            throw new java.lang.IllegalStateException("****************** TESTING ****************");
        };
        onErrorMock.accept(EasyMock.anyObject(java.lang.IllegalStateException.class));
        replayAll();
        asyncCallableService = new org.apache.ambari.server.topology.AsyncCallableService<>(throwingTask, org.apache.ambari.server.topology.AsyncCallableServiceTest.TIMEOUT, org.apache.ambari.server.topology.AsyncCallableServiceTest.RETRY_DELAY, "test", onErrorMock);
        java.lang.Boolean serviceResult = asyncCallableService.call();
        verifyAll();
        org.junit.Assert.assertNull("No result expected from throwing task", serviceResult);
    }
}