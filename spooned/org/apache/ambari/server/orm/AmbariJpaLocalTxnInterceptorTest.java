package org.apache.ambari.server.orm;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityTransaction;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class AmbariJpaLocalTxnInterceptorTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void canBeCommittedIfExceptionsToBeRolledBackOnIsEmpty() {
        com.google.inject.persist.Transactional transactional = createNiceMock(com.google.inject.persist.Transactional.class);
        javax.persistence.EntityTransaction transaction = createStrictMock(javax.persistence.EntityTransaction.class);
        EasyMock.expect(transaction.getRollbackOnly()).andReturn(false);
        EasyMock.expect(transactional.rollbackOn()).andReturn(org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptorTest.asArray());
        replayAll();
        boolean canCommit = org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.rollbackIfNecessary(transactional, new java.lang.RuntimeException(), transaction);
        org.junit.Assert.assertTrue("Should be allowed to commit, since rollbackOn clause is empty", canCommit);
        verifyAll();
    }

    @org.junit.Test
    public void canBeCommittedIfUnknownExceptionThrown() {
        com.google.inject.persist.Transactional transactional = createNiceMock(com.google.inject.persist.Transactional.class);
        javax.persistence.EntityTransaction transaction = createStrictMock(javax.persistence.EntityTransaction.class);
        EasyMock.expect(transaction.getRollbackOnly()).andReturn(false);
        EasyMock.expect(transactional.rollbackOn()).andReturn(org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptorTest.asArray(java.lang.IllegalArgumentException.class));
        replayAll();
        boolean canCommit = org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.rollbackIfNecessary(transactional, new java.lang.RuntimeException(), transaction);
        org.junit.Assert.assertTrue("Should be allowed to commit, exception thrown does not match rollbackOn clause", canCommit);
        verifyAll();
    }

    @org.junit.Test
    public void rolledBackForKnownException() {
        com.google.inject.persist.Transactional transactional = createNiceMock(com.google.inject.persist.Transactional.class);
        javax.persistence.EntityTransaction transaction = createStrictMock(javax.persistence.EntityTransaction.class);
        EasyMock.expect(transaction.getRollbackOnly()).andReturn(false);
        EasyMock.expect(transactional.rollbackOn()).andReturn(org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptorTest.asArray(java.lang.NullPointerException.class, java.lang.IllegalArgumentException.class));
        EasyMock.expect(transactional.ignore()).andReturn(org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptorTest.asArray());
        transaction.rollback();
        replayAll();
        boolean canCommit = org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.rollbackIfNecessary(transactional, new java.lang.IllegalArgumentException("rolling back"), transaction);
        org.junit.Assert.assertFalse("Should be rolled back, since exception matches rollbackOn clause", canCommit);
        verifyAll();
    }

    @org.junit.Test
    public void rolledBackForSubclassOfKnownException() {
        com.google.inject.persist.Transactional transactional = createNiceMock(com.google.inject.persist.Transactional.class);
        javax.persistence.EntityTransaction transaction = createStrictMock(javax.persistence.EntityTransaction.class);
        EasyMock.expect(transaction.getRollbackOnly()).andReturn(false);
        EasyMock.expect(transactional.rollbackOn()).andReturn(org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptorTest.asArray(java.lang.RuntimeException.class));
        EasyMock.expect(transactional.ignore()).andReturn(org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptorTest.asArray());
        transaction.rollback();
        replayAll();
        boolean canCommit = org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.rollbackIfNecessary(transactional, new java.lang.IllegalArgumentException("rolling back"), transaction);
        org.junit.Assert.assertFalse("Should be rolled back, since exception is subclass of the one in rollbackOn clause", canCommit);
        verifyAll();
    }

    @org.junit.Test
    public void canBeCommittedIfIgnoredExceptionThrown() {
        com.google.inject.persist.Transactional transactional = createNiceMock(com.google.inject.persist.Transactional.class);
        javax.persistence.EntityTransaction transaction = createStrictMock(javax.persistence.EntityTransaction.class);
        EasyMock.expect(transaction.getRollbackOnly()).andReturn(false);
        EasyMock.expect(transactional.rollbackOn()).andReturn(org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptorTest.asArray(java.lang.IllegalArgumentException.class));
        EasyMock.expect(transactional.ignore()).andReturn(org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptorTest.asArray(java.lang.NumberFormatException.class));
        replayAll();
        boolean canCommit = org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.rollbackIfNecessary(transactional, new java.lang.NumberFormatException("rolling back"), transaction);
        org.junit.Assert.assertTrue("Should be allowed to commit, since ignored exception was thrown", canCommit);
        verifyAll();
    }

    @org.junit.Test
    public void canBeCommittedIfSubclassOfIgnoredExceptionThrown() {
        com.google.inject.persist.Transactional transactional = createNiceMock(com.google.inject.persist.Transactional.class);
        javax.persistence.EntityTransaction transaction = createStrictMock(javax.persistence.EntityTransaction.class);
        EasyMock.expect(transaction.getRollbackOnly()).andReturn(false);
        EasyMock.expect(transactional.rollbackOn()).andReturn(org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptorTest.asArray(java.lang.Exception.class));
        EasyMock.expect(transactional.ignore()).andReturn(org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptorTest.asArray(java.io.IOException.class));
        replayAll();
        boolean canCommit = org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.rollbackIfNecessary(transactional, new java.io.FileNotFoundException("rolling back"), transaction);
        org.junit.Assert.assertTrue("Should be allowed to commit, since subclass of ignored exception was thrown", canCommit);
        verifyAll();
    }

    @org.junit.Test
    public void rolledBackIfTransactionMarkedRollbackOnly() {
        com.google.inject.persist.Transactional transactional = createNiceMock(com.google.inject.persist.Transactional.class);
        javax.persistence.EntityTransaction transaction = createStrictMock(javax.persistence.EntityTransaction.class);
        EasyMock.expect(transaction.getRollbackOnly()).andReturn(true);
        transaction.rollback();
        replayAll();
        boolean canCommit = org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.rollbackIfNecessary(transactional, null, transaction);
        org.junit.Assert.assertFalse("Should be rolled back, since transaction was marked rollback-only", canCommit);
        verifyAll();
    }

    @java.lang.SafeVarargs
    private static java.lang.Class<? extends java.lang.Exception>[] asArray(java.lang.Class<? extends java.lang.Exception>... exceptions) {
        return exceptions;
    }
}