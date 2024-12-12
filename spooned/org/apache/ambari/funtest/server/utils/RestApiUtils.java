package org.apache.ambari.funtest.server.utils;
import org.apache.http.HttpStatus;
public class RestApiUtils {
    public static com.google.gson.JsonElement executeRequest(org.apache.ambari.funtest.server.WebRequest request) throws java.lang.Exception {
        org.apache.ambari.funtest.server.WebResponse response = request.getResponse();
        int responseCode = response.getStatusCode();
        java.lang.String responseBody = response.getContent();
        if (((responseCode != org.apache.http.HttpStatus.SC_OK) && (responseCode != org.apache.http.HttpStatus.SC_CREATED)) && (responseCode != org.apache.http.HttpStatus.SC_ACCEPTED)) {
            throw new java.lang.RuntimeException(java.lang.String.format("%d:%s", responseCode, responseBody));
        }
        return new com.google.gson.JsonParser().parse(new com.google.gson.stream.JsonReader(new java.io.StringReader(responseBody)));
    }
}