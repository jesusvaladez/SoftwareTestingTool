package org.apache.ambari.annotations;
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE)
public @interface SwaggerOverwriteNestedAPI {
    java.lang.Class<?> parentApi();

    java.lang.String parentApiPath();

    java.lang.String parentMethodPath();

    java.lang.String[] pathParameters();
}