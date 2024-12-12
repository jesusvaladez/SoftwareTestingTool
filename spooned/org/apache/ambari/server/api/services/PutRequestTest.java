package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class PutRequestTest extends org.apache.ambari.server.api.services.BaseRequestTest {
    @org.junit.Test
    public void testRequestType() throws java.lang.Exception {
        org.apache.ambari.server.api.services.Request r = new org.apache.ambari.server.api.services.PutRequest(null, new org.apache.ambari.server.api.services.RequestBody(), null, null);
        org.junit.Assert.assertSame(org.apache.ambari.server.api.services.Request.Type.PUT, r.getRequestType());
    }

    protected org.apache.ambari.server.api.services.Request getTestRequest(javax.ws.rs.core.HttpHeaders headers, org.apache.ambari.server.api.services.RequestBody body, javax.ws.rs.core.UriInfo uriInfo, final org.apache.ambari.server.api.predicate.PredicateCompiler compiler, final org.apache.ambari.server.api.handlers.RequestHandler handler, final org.apache.ambari.server.api.services.ResultPostProcessor processor, org.apache.ambari.server.api.resources.ResourceInstance resource) {
        return new org.apache.ambari.server.api.services.PutRequest(headers, body, uriInfo, resource) {
            @java.lang.Override
            protected org.apache.ambari.server.api.predicate.PredicateCompiler getPredicateCompiler() {
                return compiler;
            }

            @java.lang.Override
            protected org.apache.ambari.server.api.handlers.RequestHandler getRequestHandler() {
                return handler;
            }

            @java.lang.Override
            public org.apache.ambari.server.api.services.ResultPostProcessor getResultPostProcessor() {
                return processor;
            }
        };
    }
}