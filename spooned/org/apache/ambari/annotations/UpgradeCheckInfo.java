package org.apache.ambari.annotations;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
@java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE })
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface UpgradeCheckInfo {
    org.apache.ambari.spi.upgrade.UpgradeCheckGroup group() default org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT;

    float order() default 1.0F;

    org.apache.ambari.spi.upgrade.UpgradeType[] required() default {  };

    org.apache.ambari.spi.RepositoryType[] orchestration() default { org.apache.ambari.spi.RepositoryType.STANDARD, org.apache.ambari.spi.RepositoryType.PATCH, org.apache.ambari.spi.RepositoryType.MAINT, org.apache.ambari.spi.RepositoryType.SERVICE };
}