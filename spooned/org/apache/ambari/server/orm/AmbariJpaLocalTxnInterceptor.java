package org.apache.ambari.server.orm;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import org.eclipse.persistence.exceptions.EclipseLinkException;
public class AmbariJpaLocalTxnInterceptor implements org.aopalliance.intercept.MethodInterceptor {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.class);

    private static final java.lang.ThreadLocal<java.util.LinkedList<org.apache.ambari.annotations.TransactionalLock>> s_transactionalLocks = new java.lang.ThreadLocal<java.util.LinkedList<org.apache.ambari.annotations.TransactionalLock>>() {
        @java.lang.Override
        protected java.util.LinkedList<org.apache.ambari.annotations.TransactionalLock> initialValue() {
            return new java.util.LinkedList<>();
        }
    };

    @com.google.inject.Inject
    private final org.apache.ambari.server.orm.TransactionalLocks transactionLocks = null;

    @com.google.inject.Inject
    private final com.google.inject.persist.jpa.AmbariJpaPersistService emProvider = null;

    @com.google.inject.Inject
    private final com.google.inject.persist.UnitOfWork unitOfWork = null;

    private final java.lang.ThreadLocal<java.lang.Boolean> didWeStartWork = new java.lang.ThreadLocal<>();

    @java.lang.Override
    public java.lang.Object invoke(org.aopalliance.intercept.MethodInvocation methodInvocation) throws java.lang.Throwable {
        if (!emProvider.isWorking()) {
            emProvider.begin();
            didWeStartWork.set(true);
        }
        com.google.inject.persist.Transactional transactional = readTransactionMetadata(methodInvocation);
        javax.persistence.EntityManager em = emProvider.get();
        lockTransaction(methodInvocation);
        if (em.getTransaction().isActive()) {
            return methodInvocation.proceed();
        }
        try {
            final javax.persistence.EntityTransaction txn = em.getTransaction();
            txn.begin();
            java.lang.Object result;
            try {
                result = methodInvocation.proceed();
            } catch (java.lang.Exception e) {
                if (org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.rollbackIfNecessary(transactional, e, txn)) {
                    txn.commit();
                }
                detailedLogForPersistenceError(e);
                throw e;
            } finally {
                if ((null != didWeStartWork.get()) && (!txn.isActive())) {
                    didWeStartWork.remove();
                    unitOfWork.end();
                }
            }
            try {
                txn.commit();
            } catch (java.lang.Exception e) {
                detailedLogForPersistenceError(e);
                throw e;
            } finally {
                if (null != didWeStartWork.get()) {
                    didWeStartWork.remove();
                    unitOfWork.end();
                }
            }
            return result;
        } finally {
            unlockTransaction();
        }
    }

    private void detailedLogForPersistenceError(java.lang.Exception e) {
        if (e instanceof javax.persistence.PersistenceException) {
            javax.persistence.PersistenceException rbe = ((javax.persistence.PersistenceException) (e));
            java.lang.Throwable cause = rbe.getCause();
            if ((cause != null) && (cause instanceof org.eclipse.persistence.exceptions.EclipseLinkException)) {
                org.eclipse.persistence.exceptions.EclipseLinkException de = ((org.eclipse.persistence.exceptions.EclipseLinkException) (cause));
                org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.LOG.error("[DETAILED ERROR] Rollback reason: ", cause);
                java.lang.Throwable internal = de.getInternalException();
                int exIndent = 1;
                if ((internal != null) && (internal instanceof java.sql.SQLException)) {
                    java.sql.SQLException exception = ((java.sql.SQLException) (internal));
                    while (exception != null) {
                        org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.LOG.error(("[DETAILED ERROR] Internal exception (" + exIndent) + ") : ", exception);
                        exception = exception.getNextException();
                        exIndent++;
                    } 
                }
            }
        }
    }

    private com.google.inject.persist.Transactional readTransactionMetadata(org.aopalliance.intercept.MethodInvocation methodInvocation) {
        com.google.inject.persist.Transactional transactional;
        java.lang.reflect.Method method = methodInvocation.getMethod();
        java.lang.Class<?> targetClass = methodInvocation.getThis().getClass();
        transactional = method.getAnnotation(com.google.inject.persist.Transactional.class);
        if (null == transactional) {
            transactional = targetClass.getAnnotation(com.google.inject.persist.Transactional.class);
        }
        if (null == transactional) {
            transactional = org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.Internal.class.getAnnotation(com.google.inject.persist.Transactional.class);
        }
        return transactional;
    }

    static boolean rollbackIfNecessary(com.google.inject.persist.Transactional transactional, java.lang.Exception e, javax.persistence.EntityTransaction txn) {
        if (txn.getRollbackOnly()) {
            txn.rollback();
            return false;
        }
        boolean commit = true;
        for (java.lang.Class<? extends java.lang.Exception> rollBackOn : transactional.rollbackOn()) {
            if (rollBackOn.isInstance(e)) {
                commit = false;
                for (java.lang.Class<? extends java.lang.Exception> exceptOn : transactional.ignore()) {
                    if (exceptOn.isInstance(e)) {
                        commit = true;
                        break;
                    }
                }
                if (!commit) {
                    txn.rollback();
                }
                break;
            }
        }
        return commit;
    }

    private void lockTransaction(org.aopalliance.intercept.MethodInvocation methodInvocation) {
        org.apache.ambari.annotations.TransactionalLock annotation = methodInvocation.getMethod().getAnnotation(org.apache.ambari.annotations.TransactionalLock.class);
        if (null == annotation) {
            return;
        }
        if (org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.s_transactionalLocks.get().contains(annotation)) {
            return;
        }
        org.apache.ambari.annotations.TransactionalLock.LockArea lockArea = annotation.lockArea();
        org.apache.ambari.annotations.TransactionalLock.LockType lockType = annotation.lockType();
        java.util.concurrent.locks.ReadWriteLock rwLock = transactionLocks.getLock(lockArea);
        java.util.concurrent.locks.Lock lock = (lockType == org.apache.ambari.annotations.TransactionalLock.LockType.READ) ? rwLock.readLock() : rwLock.writeLock();
        lock.lock();
        org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.s_transactionalLocks.get().add(annotation);
    }

    private void unlockTransaction() {
        java.util.LinkedList<org.apache.ambari.annotations.TransactionalLock> annotations = org.apache.ambari.server.orm.AmbariJpaLocalTxnInterceptor.s_transactionalLocks.get();
        if (annotations.isEmpty()) {
            return;
        }
        java.util.Iterator<org.apache.ambari.annotations.TransactionalLock> iterator = annotations.descendingIterator();
        while (iterator.hasNext()) {
            org.apache.ambari.annotations.TransactionalLock annotation = iterator.next();
            org.apache.ambari.annotations.TransactionalLock.LockArea lockArea = annotation.lockArea();
            org.apache.ambari.annotations.TransactionalLock.LockType lockType = annotation.lockType();
            java.util.concurrent.locks.ReadWriteLock rwLock = transactionLocks.getLock(lockArea);
            java.util.concurrent.locks.Lock lock = (lockType == org.apache.ambari.annotations.TransactionalLock.LockType.READ) ? rwLock.readLock() : rwLock.writeLock();
            lock.unlock();
            iterator.remove();
        } 
    }

    @com.google.inject.persist.Transactional
    private static class Internal {}
}