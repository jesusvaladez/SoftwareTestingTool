package org.apache.ambari.server.security.authorization.internal;
@java.lang.annotation.Inherited
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ java.lang.annotation.ElementType.METHOD })
public @interface RunWithInternalSecurityContext {
    java.lang.String token();
}