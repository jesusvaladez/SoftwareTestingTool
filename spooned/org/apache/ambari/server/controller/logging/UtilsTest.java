package org.apache.ambari.server.controller.logging;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
public class UtilsTest {
    @org.junit.Test
    public void testLogErrorMsgWaitDefault() throws java.lang.Exception {
        final java.lang.String expectedErrorMessage = "This is a test error message!";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.slf4j.Logger loggerMock = mockSupport.createMock(org.slf4j.Logger.class);
        java.util.concurrent.atomic.AtomicInteger testAtomicInteger = new java.util.concurrent.atomic.AtomicInteger(0);
        loggerMock.error(expectedErrorMessage);
        EasyMock.expectLastCall().times(1);
        mockSupport.replayAll();
        for (int i = 0; i < 1000; i++) {
            org.apache.ambari.server.controller.logging.Utils.logErrorMessageWithCounter(loggerMock, testAtomicInteger, expectedErrorMessage);
        }
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testLogErrorMsgWaitDefaultExceedsMaxCount() throws java.lang.Exception {
        final java.lang.String expectedErrorMessage = "This is a test error message that should only repeat once!";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.slf4j.Logger loggerMock = mockSupport.createMock(org.slf4j.Logger.class);
        java.util.concurrent.atomic.AtomicInteger testAtomicInteger = new java.util.concurrent.atomic.AtomicInteger(0);
        loggerMock.error(expectedErrorMessage);
        EasyMock.expectLastCall().times(2);
        mockSupport.replayAll();
        for (int i = 0; i < 2000; i++) {
            org.apache.ambari.server.controller.logging.Utils.logErrorMessageWithCounter(loggerMock, testAtomicInteger, expectedErrorMessage);
        }
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testLogErrorMsgWithCustomWaitMax() throws java.lang.Exception {
        final java.lang.String expectedErrorMessage = "This is a test error message!";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.slf4j.Logger loggerMock = mockSupport.createMock(org.slf4j.Logger.class);
        java.util.concurrent.atomic.AtomicInteger testAtomicInteger = new java.util.concurrent.atomic.AtomicInteger(0);
        loggerMock.error(expectedErrorMessage);
        EasyMock.expectLastCall().times(1);
        mockSupport.replayAll();
        for (int i = 0; i < 5; i++) {
            org.apache.ambari.server.controller.logging.Utils.logErrorMessageWithCounter(loggerMock, testAtomicInteger, expectedErrorMessage, 5);
        }
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testLogErrorMsgWaitExceedsCustomMaxCount() throws java.lang.Exception {
        final java.lang.String expectedErrorMessage = "This is a test error message that should only repeat once!";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.slf4j.Logger loggerMock = mockSupport.createMock(org.slf4j.Logger.class);
        java.util.concurrent.atomic.AtomicInteger testAtomicInteger = new java.util.concurrent.atomic.AtomicInteger(0);
        loggerMock.error(expectedErrorMessage);
        EasyMock.expectLastCall().times(2);
        mockSupport.replayAll();
        for (int i = 0; i < 10; i++) {
            org.apache.ambari.server.controller.logging.Utils.logErrorMessageWithCounter(loggerMock, testAtomicInteger, expectedErrorMessage, 5);
        }
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testLogErrorMsgAndThrowableWaitDefault() throws java.lang.Exception {
        final java.lang.String expectedErrorMessage = "This is a test error message!";
        final java.lang.Exception expectedException = new java.lang.Exception("test exception");
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.slf4j.Logger loggerMock = mockSupport.createMock(org.slf4j.Logger.class);
        java.util.concurrent.atomic.AtomicInteger testAtomicInteger = new java.util.concurrent.atomic.AtomicInteger(0);
        org.easymock.Capture<java.lang.Exception> exceptionCaptureOne = org.easymock.EasyMock.newCapture();
        loggerMock.error(EasyMock.eq(expectedErrorMessage), EasyMock.capture(exceptionCaptureOne));
        EasyMock.expectLastCall().times(1);
        mockSupport.replayAll();
        for (int i = 0; i < 1000; i++) {
            org.apache.ambari.server.controller.logging.Utils.logErrorMessageWithThrowableWithCounter(loggerMock, testAtomicInteger, expectedErrorMessage, expectedException);
        }
        org.junit.Assert.assertSame("Exception passed to Logger should have been the same instance passed into the Utils method", expectedException, exceptionCaptureOne.getValue());
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testLogErrorMsgAndThrowableWaitDefaultExceedsMaxCount() throws java.lang.Exception {
        final java.lang.String expectedErrorMessage = "This is a test error message that should only repeat once!";
        final java.lang.Exception expectedException = new java.lang.Exception("test exception");
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.slf4j.Logger loggerMock = mockSupport.createMock(org.slf4j.Logger.class);
        java.util.concurrent.atomic.AtomicInteger testAtomicInteger = new java.util.concurrent.atomic.AtomicInteger(0);
        org.easymock.Capture<java.lang.Exception> exceptionCaptureOne = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<java.lang.Exception> exceptionCaptureTwo = org.easymock.EasyMock.newCapture();
        loggerMock.error(EasyMock.eq(expectedErrorMessage), EasyMock.capture(exceptionCaptureOne));
        loggerMock.error(EasyMock.eq(expectedErrorMessage), EasyMock.capture(exceptionCaptureTwo));
        mockSupport.replayAll();
        for (int i = 0; i < 2000; i++) {
            org.apache.ambari.server.controller.logging.Utils.logErrorMessageWithThrowableWithCounter(loggerMock, testAtomicInteger, expectedErrorMessage, expectedException);
        }
        org.junit.Assert.assertSame("Exception passed to Logger should have been the same instance passed into the Utils method", expectedException, exceptionCaptureOne.getValue());
        org.junit.Assert.assertSame("Exception passed to Logger should have been the same instance passed into the Utils method", expectedException, exceptionCaptureTwo.getValue());
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testLogErrorMsgAndThrowableWithCustomWaitMax() throws java.lang.Exception {
        final java.lang.String expectedErrorMessage = "This is a test error message!";
        final java.lang.Exception expectedException = new java.lang.Exception("test exception");
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.slf4j.Logger loggerMock = mockSupport.createMock(org.slf4j.Logger.class);
        java.util.concurrent.atomic.AtomicInteger testAtomicInteger = new java.util.concurrent.atomic.AtomicInteger(0);
        org.easymock.Capture<java.lang.Exception> exceptionCaptureOne = org.easymock.EasyMock.newCapture();
        loggerMock.error(EasyMock.eq(expectedErrorMessage), EasyMock.capture(exceptionCaptureOne));
        EasyMock.expectLastCall().times(1);
        mockSupport.replayAll();
        for (int i = 0; i < 5; i++) {
            org.apache.ambari.server.controller.logging.Utils.logErrorMessageWithThrowableWithCounter(loggerMock, testAtomicInteger, expectedErrorMessage, expectedException, 5);
        }
        org.junit.Assert.assertSame("Exception passed to Logger should have been the same instance passed into the Utils method", expectedException, exceptionCaptureOne.getValue());
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testLogErrorMsgAndThrowableWaitExceedsCustomMaxCount() throws java.lang.Exception {
        final java.lang.String expectedErrorMessage = "This is a test error message that should only repeat once!";
        final java.lang.Exception expectedException = new java.lang.Exception("test exception");
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.slf4j.Logger loggerMock = mockSupport.createMock(org.slf4j.Logger.class);
        java.util.concurrent.atomic.AtomicInteger testAtomicInteger = new java.util.concurrent.atomic.AtomicInteger(0);
        org.easymock.Capture<java.lang.Exception> exceptionCaptureOne = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<java.lang.Exception> exceptionCaptureTwo = org.easymock.EasyMock.newCapture();
        loggerMock.error(EasyMock.eq(expectedErrorMessage), EasyMock.capture(exceptionCaptureOne));
        loggerMock.error(EasyMock.eq(expectedErrorMessage), EasyMock.capture(exceptionCaptureTwo));
        mockSupport.replayAll();
        for (int i = 0; i < 10; i++) {
            org.apache.ambari.server.controller.logging.Utils.logErrorMessageWithThrowableWithCounter(loggerMock, testAtomicInteger, expectedErrorMessage, expectedException, 5);
        }
        org.junit.Assert.assertSame("Exception passed to Logger should have been the same instance passed into the Utils method", expectedException, exceptionCaptureOne.getValue());
        org.junit.Assert.assertSame("Exception passed to Logger should have been the same instance passed into the Utils method", expectedException, exceptionCaptureTwo.getValue());
        mockSupport.verifyAll();
    }
}