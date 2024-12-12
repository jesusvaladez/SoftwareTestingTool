package org.apache.ambari.server.api;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
@javax.ws.rs.ext.Provider
@javax.ws.rs.Consumes({ javax.ws.rs.core.MediaType.APPLICATION_JSON, "text/json" })
@javax.ws.rs.Produces({ javax.ws.rs.core.MediaType.APPLICATION_JSON, "text/json" })
public class GsonJsonProvider implements javax.ws.rs.ext.MessageBodyReader<java.lang.Object> , javax.ws.rs.ext.MessageBodyWriter<java.lang.Object> {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.GsonJsonProvider.class);

    static final com.google.gson.Gson gson = new com.google.gson.GsonBuilder().create();

    @java.lang.Override
    public boolean isReadable(java.lang.Class<?> type, java.lang.reflect.Type genericType, java.lang.annotation.Annotation[] annotations, javax.ws.rs.core.MediaType mediaType) {
        return true;
    }

    @java.lang.Override
    public java.lang.Object readFrom(java.lang.Class<java.lang.Object> type, java.lang.reflect.Type genericType, java.lang.annotation.Annotation[] annotations, javax.ws.rs.core.MediaType mediaType, javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> httpHeaders, java.io.InputStream entityStream) throws java.io.IOException, javax.ws.rs.WebApplicationException {
        if (type.equals(java.io.InputStream.class))
            return entityStream;

        java.io.Reader reader = new java.io.InputStreamReader(entityStream);
        try {
            return org.apache.ambari.server.api.GsonJsonProvider.gson.fromJson(reader, genericType);
        } finally {
            reader.close();
        }
    }

    @java.lang.Override
    public boolean isWriteable(java.lang.Class<?> type, java.lang.reflect.Type genericType, java.lang.annotation.Annotation[] annotations, javax.ws.rs.core.MediaType mediaType) {
        return true;
    }

    @java.lang.Override
    public long getSize(java.lang.Object o, java.lang.Class<?> type, java.lang.reflect.Type genericType, java.lang.annotation.Annotation[] annotations, javax.ws.rs.core.MediaType mediaType) {
        return -1;
    }

    @java.lang.Override
    public void writeTo(java.lang.Object o, java.lang.Class<?> type, java.lang.reflect.Type genericType, java.lang.annotation.Annotation[] annotations, javax.ws.rs.core.MediaType mediaType, javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.Object> httpHeaders, java.io.OutputStream entityStream) throws java.io.IOException, javax.ws.rs.WebApplicationException {
        java.io.Writer writer = new java.io.OutputStreamWriter(entityStream);
        try {
            org.apache.ambari.server.api.GsonJsonProvider.gson.toJson(o, genericType, writer);
        } finally {
            writer.close();
        }
    }
}