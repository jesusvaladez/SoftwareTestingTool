package org.apache.ambari.server.api.services.parsers;
public class BodyParseException extends java.lang.Exception {
    public BodyParseException(java.lang.String msg) {
        super(msg);
    }

    public BodyParseException(java.lang.Exception e) {
        super("Invalid Request: Malformed Request Body.  An exception occurred parsing the request body: " + e.getMessage());
    }
}