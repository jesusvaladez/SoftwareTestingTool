package org.apache.ambari.server.audit;
import nl.jqno.equalsverifier.EqualsVerifier;
public class ActionDBAAccessorAuditlogTest {
    @org.junit.Test
    public void equalsVerifierTest() {
        java.lang.Class<?>[] innerClasses = org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class.getDeclaredClasses();
        for (java.lang.Class<?> clazz : innerClasses) {
            if (clazz.getSimpleName().contains("RequestDetails")) {
                java.lang.Class<?>[] innerClasses2 = clazz.getDeclaredClasses();
                for (java.lang.Class<?> clazz2 : innerClasses2) {
                    if (clazz2.getSimpleName().contains("Component")) {
                        nl.jqno.equalsverifier.EqualsVerifier.forClass(clazz2).verify();
                    }
                }
            }
        }
    }
}