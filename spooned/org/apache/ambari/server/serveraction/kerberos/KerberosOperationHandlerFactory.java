package org.apache.ambari.server.serveraction.kerberos;
@com.google.inject.Singleton
public class KerberosOperationHandlerFactory {
    @com.google.inject.Inject
    private com.google.inject.Injector injector;

    public org.apache.ambari.server.serveraction.kerberos.KerberosOperationHandler getKerberosOperationHandler(org.apache.ambari.server.serveraction.kerberos.KDCType kdcType) {
        if (kdcType == null) {
            throw new java.lang.IllegalArgumentException("kdcType may not be null");
        }
        switch (kdcType) {
            case MIT_KDC :
                return injector.getInstance(org.apache.ambari.server.serveraction.kerberos.MITKerberosOperationHandler.class);
            case ACTIVE_DIRECTORY :
                return injector.getInstance(org.apache.ambari.server.serveraction.kerberos.ADKerberosOperationHandler.class);
            case IPA :
                return injector.getInstance(org.apache.ambari.server.serveraction.kerberos.IPAKerberosOperationHandler.class);
            default :
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Unexpected kdcType value: %s", kdcType.name()));
        }
    }
}