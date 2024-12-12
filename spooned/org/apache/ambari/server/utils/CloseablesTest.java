package org.apache.ambari.server.utils;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
public class CloseablesTest {
    @org.junit.Test
    public void silentIfSucceeds() throws java.lang.Exception {
        java.io.Closeable normalCloseable = EasyMock.createNiceMock(java.io.Closeable.class);
        normalCloseable.close();
        replayAll();
        org.apache.ambari.server.utils.Closeables.closeSilently(normalCloseable);
        verifyAll();
    }

    @org.junit.Test
    public void silentIfThrows() throws java.lang.Exception {
        java.io.Closeable throwingCloseable = EasyMock.createNiceMock(java.io.Closeable.class);
        throwingCloseable.close();
        EasyMock.expectLastCall().andThrow(new java.io.IOException());
        replayAll();
        org.apache.ambari.server.utils.Closeables.closeSilently(throwingCloseable);
        verifyAll();
    }

    @org.junit.Test
    public void succeedsWithoutLog() throws java.lang.Exception {
        java.io.Closeable normalCloseable = EasyMock.createNiceMock(java.io.Closeable.class);
        org.slf4j.Logger logger = EasyMock.createStrictMock(org.slf4j.Logger.class);
        normalCloseable.close();
        replayAll();
        org.apache.ambari.server.utils.Closeables.closeLoggingExceptions(normalCloseable, logger);
        verifyAll();
    }

    @org.junit.Test
    public void warnWithThrownException() throws java.lang.Exception {
        java.io.Closeable throwingCloseable = EasyMock.createNiceMock(java.io.Closeable.class);
        org.slf4j.Logger logger = EasyMock.createNiceMock(org.slf4j.Logger.class);
        java.io.IOException e = new java.io.IOException();
        throwingCloseable.close();
        EasyMock.expectLastCall().andThrow(e);
        logger.warn(EasyMock.anyString(), EasyMock.eq(e));
        replayAll();
        org.apache.ambari.server.utils.Closeables.closeLoggingExceptions(throwingCloseable, logger);
        verifyAll();
    }

    @org.junit.Test
    public void ignoresNullCloseable() {
        org.apache.ambari.server.utils.Closeables.closeSilently(null);
    }
}