package org.apache.ambari.server.api.services.parsers;
public interface RequestBodyParser {
    java.lang.String REQUEST_INFO_PATH = "RequestInfo";

    java.lang.String SLASH = "/";

    java.lang.String REQUEST_BLOB_TITLE = "RequestBodyInfo";

    java.lang.String QUERY_FIELD_NAME = "query";

    java.lang.String BODY_TITLE = "Body";

    java.util.Set<org.apache.ambari.server.api.services.RequestBody> parse(java.lang.String body) throws org.apache.ambari.server.api.services.parsers.BodyParseException;
}