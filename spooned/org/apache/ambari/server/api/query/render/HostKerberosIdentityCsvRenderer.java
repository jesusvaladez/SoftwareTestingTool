package org.apache.ambari.server.api.query.render;
public class HostKerberosIdentityCsvRenderer extends org.apache.ambari.server.api.query.render.DefaultRenderer {
    @java.lang.Override
    public org.apache.ambari.server.api.services.Result finalizeResult(org.apache.ambari.server.api.services.Result queryResult) {
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = queryResult.getResultTree();
        if (resultTree != null) {
            java.util.Map<java.lang.String, java.lang.String> columnMap = new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put("KerberosIdentity/host_name", "host");
                    put("KerberosIdentity/description", "description");
                    put("KerberosIdentity/principal_name", "principal name");
                    put("KerberosIdentity/principal_type", "principal type");
                    put("KerberosIdentity/principal_local_username", "local username");
                    put("KerberosIdentity/keytab_file_path", "keytab file path");
                    put("KerberosIdentity/keytab_file_owner", "keytab file owner");
                    put("KerberosIdentity/keytab_file_owner_access", "keytab file owner access");
                    put("KerberosIdentity/keytab_file_group", "keytab file group");
                    put("KerberosIdentity/keytab_file_group_access", "keytab file group access");
                    put("KerberosIdentity/keytab_file_mode", "keytab file mode");
                    put("KerberosIdentity/keytab_file_installed", "keytab file installed");
                }
            };
            java.util.List<java.lang.String> columnOrder = new java.util.ArrayList<java.lang.String>() {
                {
                    add("KerberosIdentity/host_name");
                    add("KerberosIdentity/description");
                    add("KerberosIdentity/principal_name");
                    add("KerberosIdentity/principal_type");
                    add("KerberosIdentity/principal_local_username");
                    add("KerberosIdentity/keytab_file_path");
                    add("KerberosIdentity/keytab_file_owner");
                    add("KerberosIdentity/keytab_file_owner_access");
                    add("KerberosIdentity/keytab_file_group");
                    add("KerberosIdentity/keytab_file_group_access");
                    add("KerberosIdentity/keytab_file_mode");
                    add("KerberosIdentity/keytab_file_installed");
                }
            };
            resultTree.setProperty(org.apache.ambari.server.api.services.serializers.CsvSerializer.PROPERTY_COLUMN_MAP, columnMap);
            resultTree.setProperty(org.apache.ambari.server.api.services.serializers.CsvSerializer.PROPERTY_COLUMN_ORDER, columnOrder);
        }
        return queryResult;
    }
}