package org.apache.ambari.server.serveraction.kerberos;
public class KerberosMissingAdminCredentialsException extends org.apache.ambari.server.serveraction.kerberos.KerberosOperationException {
    private static final java.lang.String DEFAULT_MESSAGE = "Missing KDC administrator credentials.\n" + (((((("The KDC administrator credentials must be set as a persisted or temporary credential resource." + "This may be done by issuing a POST to the /api/v1/clusters/:clusterName/credentials/kdc.admin.credential API entry point with the following payload:\n") + "{\n") + "  \"Credential\" : {\n") + "    \"principal\" : \"(PRINCIPAL)\", \"key\" : \"(PASSWORD)\", \"type\" : \"(persisted|temporary)\"}\n") + "  }\n") + "}");

    public KerberosMissingAdminCredentialsException() {
        this(org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException.DEFAULT_MESSAGE);
    }

    public KerberosMissingAdminCredentialsException(java.lang.String message) {
        super(message);
    }

    public KerberosMissingAdminCredentialsException(java.lang.Throwable cause) {
        this(org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException.DEFAULT_MESSAGE, cause);
    }

    public KerberosMissingAdminCredentialsException(java.lang.String message, java.lang.Throwable cause) {
        super(message, cause);
    }
}