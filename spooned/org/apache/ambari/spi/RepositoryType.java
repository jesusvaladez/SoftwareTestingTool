package org.apache.ambari.spi;
public enum RepositoryType {

    STANDARD,
    PATCH,
    MAINT,
    SERVICE;
    public static final java.util.EnumSet<org.apache.ambari.spi.RepositoryType> REVERTABLE = java.util.EnumSet.of(org.apache.ambari.spi.RepositoryType.MAINT, org.apache.ambari.spi.RepositoryType.PATCH);

    public static final java.util.EnumSet<org.apache.ambari.spi.RepositoryType> PARTIAL = java.util.EnumSet.of(org.apache.ambari.spi.RepositoryType.MAINT, org.apache.ambari.spi.RepositoryType.PATCH, org.apache.ambari.spi.RepositoryType.SERVICE);

    public boolean isRevertable() {
        switch (this) {
            case MAINT :
            case PATCH :
                return true;
            case SERVICE :
            case STANDARD :
                return false;
            default :
                return false;
        }
    }

    public boolean isPartial() {
        return org.apache.ambari.spi.RepositoryType.PARTIAL.contains(this);
    }
}