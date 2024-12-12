package org.apache.ambari.server.state.stack;
public class JsonOsFamilyRoot {
    private java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.JsonOsFamilyEntry> mapping;

    private java.util.Map<java.lang.String, java.lang.String> aliases;

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.JsonOsFamilyEntry> getMapping() {
        return mapping;
    }

    public void setMapping(java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.JsonOsFamilyEntry> mapping) {
        this.mapping = mapping;
    }

    public java.util.Map<java.lang.String, java.lang.String> getAliases() {
        return aliases;
    }

    public void setAliases(java.util.Map<java.lang.String, java.lang.String> aliases) {
        this.aliases = aliases;
    }
}