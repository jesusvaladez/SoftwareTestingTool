package org.apache.ambari.server.state.quicklinksprofile;
public class QuickLinksProfileParser {
    private final com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

    public QuickLinksProfileParser() {
        com.fasterxml.jackson.databind.module.SimpleModule module = new com.fasterxml.jackson.databind.module.SimpleModule("Quick Links Parser", new com.fasterxml.jackson.core.Version(1, 0, 0, null, null, null));
        module.addDeserializer(org.apache.ambari.server.state.quicklinksprofile.Filter.class, new org.apache.ambari.server.state.quicklinksprofile.QuickLinksFilterDeserializer());
        mapper.registerModule(module);
        mapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY);
    }

    public org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile parse(byte[] input) throws java.io.IOException {
        return mapper.readValue(input, org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile.class);
    }

    public org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile parse(java.net.URL url) throws java.io.IOException {
        return parse(com.google.common.io.Resources.toByteArray(url));
    }

    public java.lang.String encode(org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfile profile) throws java.io.IOException {
        return mapper.writeValueAsString(profile);
    }
}