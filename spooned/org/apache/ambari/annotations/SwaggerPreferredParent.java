package org.apache.ambari.annotations;
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE)
public @interface SwaggerPreferredParent {
    java.lang.Class<?> preferredParent();
}