package org.apache.ambari.checkstyle;
import com.google.inject.persist.Transactional;
public class InputTransactionalOnPrivateMethods {
    @com.google.inject.persist.Transactional
    public void publicMethodWithTransactional() {
    }

    @com.google.inject.persist.Transactional
    private void privateMethodWithTransactional() {
    }

    private void privateMethodWithoutTransactional() {
    }

    @com.google.inject.persist.Transactional
    private void otherPrivateMethodWithTransactional() {
    }
}