package org.apache.ambari.server.state.kerberos;
abstract class AbstractKerberosDescriptorFactory {
    protected java.util.Map<java.lang.String, java.lang.Object> parseFile(java.io.File file) throws java.io.IOException {
        if (file == null) {
            return java.util.Collections.emptyMap();
        } else if ((!file.isFile()) || (!file.canRead())) {
            throw new java.io.IOException(java.lang.String.format("%s is not a readable file", file.getAbsolutePath()));
        } else {
            try {
                return new com.google.gson.Gson().fromJson(new java.io.FileReader(file), new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.Object>>() {}.getType());
            } catch (com.google.gson.JsonSyntaxException e) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Failed to parse JSON-formatted file: %s", file.getAbsolutePath()), e);
            }
        }
    }

    protected java.util.Map<java.lang.String, java.lang.Object> parseJSON(java.lang.String json) throws org.apache.ambari.server.AmbariException {
        if ((json == null) || json.isEmpty()) {
            return java.util.Collections.emptyMap();
        } else {
            try {
                return new com.google.gson.Gson().fromJson(json, new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.Object>>() {}.getType());
            } catch (com.google.gson.JsonSyntaxException e) {
                throw new org.apache.ambari.server.AmbariException("Failed to parse JSON-formatted string", e);
            }
        }
    }
}