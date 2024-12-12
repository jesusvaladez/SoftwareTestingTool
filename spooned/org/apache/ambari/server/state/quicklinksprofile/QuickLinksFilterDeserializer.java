package org.apache.ambari.server.state.quicklinksprofile;
class QuickLinksFilterDeserializer extends com.fasterxml.jackson.databind.deser.std.StdDeserializer<org.apache.ambari.server.state.quicklinksprofile.Filter> {
    static final java.lang.String PARSE_ERROR_MESSAGE_AMBIGUOUS_FILTER = "A filter is not allowed to declare both link_name and link_attribute at the same time.";

    static final java.lang.String PARSE_ERROR_MESSAGE_INVALID_JSON_TAG = "Invalid attribute(s) in filter declaration: ";

    QuickLinksFilterDeserializer() {
        super(org.apache.ambari.server.state.quicklinksprofile.Filter.class);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.quicklinksprofile.Filter deserialize(com.fasterxml.jackson.core.JsonParser parser, com.fasterxml.jackson.databind.DeserializationContext context) throws java.io.IOException, com.fasterxml.jackson.core.JsonProcessingException {
        com.fasterxml.jackson.databind.ObjectMapper mapper = ((com.fasterxml.jackson.databind.ObjectMapper) (parser.getCodec()));
        com.fasterxml.jackson.databind.node.ObjectNode root = mapper.readTree(parser);
        java.lang.Class<? extends org.apache.ambari.server.state.quicklinksprofile.Filter> filterClass = null;
        java.util.List<java.lang.String> invalidAttributes = new java.util.ArrayList<>();
        for (java.lang.String fieldName : com.google.common.collect.ImmutableList.copyOf(root.fieldNames())) {
            switch (fieldName) {
                case org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter.LINK_ATTRIBUTE :
                    if (null != filterClass) {
                        throw new com.fasterxml.jackson.core.JsonParseException(parser, org.apache.ambari.server.state.quicklinksprofile.QuickLinksFilterDeserializer.PARSE_ERROR_MESSAGE_AMBIGUOUS_FILTER, parser.getCurrentLocation());
                    }
                    filterClass = org.apache.ambari.server.state.quicklinksprofile.LinkAttributeFilter.class;
                    break;
                case org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_NAME :
                case org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.LINK_URL :
                    if ((null != filterClass) && (!filterClass.equals(org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.class))) {
                        throw new com.fasterxml.jackson.core.JsonParseException(parser, org.apache.ambari.server.state.quicklinksprofile.QuickLinksFilterDeserializer.PARSE_ERROR_MESSAGE_AMBIGUOUS_FILTER, parser.getCurrentLocation());
                    }
                    filterClass = org.apache.ambari.server.state.quicklinksprofile.LinkNameFilter.class;
                    break;
                case org.apache.ambari.server.state.quicklinksprofile.Filter.VISIBLE :
                    break;
                default :
                    invalidAttributes.add(fieldName);
            }
        }
        if (!invalidAttributes.isEmpty()) {
            throw new com.fasterxml.jackson.core.JsonParseException(parser, org.apache.ambari.server.state.quicklinksprofile.QuickLinksFilterDeserializer.PARSE_ERROR_MESSAGE_INVALID_JSON_TAG + invalidAttributes, parser.getCurrentLocation());
        }
        if (null == filterClass) {
            filterClass = org.apache.ambari.server.state.quicklinksprofile.AcceptAllFilter.class;
        }
        return mapper.readValue(root.traverse(), filterClass);
    }
}