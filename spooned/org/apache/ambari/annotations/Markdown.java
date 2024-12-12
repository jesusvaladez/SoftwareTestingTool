package org.apache.ambari.annotations;
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD })
public @interface Markdown {
    java.lang.String description();

    java.lang.String[] examples() default {  };

    java.lang.String relatedTo() default "";

    boolean internal() default false;
}