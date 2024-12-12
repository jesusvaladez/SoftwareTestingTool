package org.apache.ambari.server.api.services;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public abstract class BaseRequestTest {
    @org.junit.Test
    public void testGetBody() {
        org.apache.ambari.server.api.services.RequestBody body = EasyMock.createNiceMock(org.apache.ambari.server.api.services.RequestBody.class);
        org.apache.ambari.server.api.services.Request request = getTestRequest(null, body, null, null, null, null, null);
        org.junit.Assert.assertSame(body, request.getBody());
    }

    @org.junit.Test
    public void testGetResource() {
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.services.Request request = getTestRequest(null, null, null, null, null, null, resource);
        org.junit.Assert.assertSame(resource, request.getResource());
    }

    @org.junit.Test
    public void testGetApiVersion() {
        org.apache.ambari.server.api.services.Request request = getTestRequest(null, null, null, null, null, null, null);
        org.junit.Assert.assertEquals(1, request.getAPIVersion());
    }

    @org.junit.Test
    public void testGetHttpHeaders() {
        javax.ws.rs.core.HttpHeaders headers = EasyMock.createNiceMock(javax.ws.rs.core.HttpHeaders.class);
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> mapHeaders = new com.sun.jersey.core.util.MultivaluedMapImpl();
        org.apache.ambari.server.api.services.Request request = getTestRequest(headers, null, null, null, null, null, null);
        EasyMock.expect(headers.getRequestHeaders()).andReturn(mapHeaders);
        EasyMock.replay(headers);
        org.junit.Assert.assertSame(mapHeaders, request.getHttpHeaders());
        EasyMock.verify(headers);
    }

    @org.junit.Test
    public void testProcess_noBody() throws java.lang.Exception {
        java.lang.String uriString = "http://localhost.com:8080/api/v1/clusters/c1";
        java.net.URI uri = new java.net.URI(java.net.URLEncoder.encode(uriString, "UTF-8"));
        org.apache.ambari.server.api.predicate.PredicateCompiler compiler = EasyMock.createStrictMock(org.apache.ambari.server.api.predicate.PredicateCompiler.class);
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        @java.lang.SuppressWarnings("unchecked")
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = EasyMock.createMock(javax.ws.rs.core.MultivaluedMap.class);
        org.apache.ambari.server.api.handlers.RequestHandler handler = EasyMock.createStrictMock(org.apache.ambari.server.api.handlers.RequestHandler.class);
        org.apache.ambari.server.api.services.Result result = EasyMock.createMock(org.apache.ambari.server.api.services.Result.class);
        org.apache.ambari.server.api.services.ResultStatus resultStatus = EasyMock.createMock(org.apache.ambari.server.api.services.ResultStatus.class);
        org.apache.ambari.server.api.services.ResultPostProcessor processor = EasyMock.createStrictMock(org.apache.ambari.server.api.services.ResultPostProcessor.class);
        org.apache.ambari.server.api.services.RequestBody body = EasyMock.createNiceMock(org.apache.ambari.server.api.services.RequestBody.class);
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.api.query.render.Renderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        org.apache.ambari.server.api.services.Request request = getTestRequest(null, body, uriInfo, compiler, handler, processor, resource);
        EasyMock.expect(uriInfo.getQueryParameters()).andReturn(queryParams).anyTimes();
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_MINIMAL)).andReturn(null);
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_FORMAT)).andReturn(null);
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition);
        EasyMock.expect(resourceDefinition.getRenderer(null)).andReturn(renderer);
        EasyMock.expect(uriInfo.getRequestUri()).andReturn(uri).anyTimes();
        EasyMock.expect(handler.handleRequest(request)).andReturn(result);
        EasyMock.expect(result.getStatus()).andReturn(resultStatus).anyTimes();
        EasyMock.expect(resultStatus.isErrorState()).andReturn(false).anyTimes();
        processor.process(result);
        EasyMock.replay(compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, body);
        org.apache.ambari.server.api.services.Result processResult = request.process();
        EasyMock.verify(compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, body);
        org.junit.Assert.assertSame(result, processResult);
        org.junit.Assert.assertNull(request.getQueryPredicate());
    }

    @org.junit.Test
    public void testProcess_withDirectives() throws java.lang.Exception {
        javax.ws.rs.core.HttpHeaders headers = EasyMock.createNiceMock(javax.ws.rs.core.HttpHeaders.class);
        java.lang.String path = java.net.URLEncoder.encode("http://localhost.com:8080/api/v1/clusters/c1", "UTF-8");
        java.lang.String query = java.net.URLEncoder.encode("foo=foo-value&bar=bar-value", "UTF-8");
        java.net.URI uri = new java.net.URI((path + "?") + query);
        org.apache.ambari.server.api.predicate.PredicateCompiler compiler = EasyMock.createStrictMock(org.apache.ambari.server.api.predicate.PredicateCompiler.class);
        org.apache.ambari.server.controller.spi.Predicate predicate = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Predicate.class);
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        @java.lang.SuppressWarnings("unchecked")
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = new com.sun.jersey.core.util.MultivaluedMapImpl();
        org.apache.ambari.server.api.handlers.RequestHandler handler = EasyMock.createStrictMock(org.apache.ambari.server.api.handlers.RequestHandler.class);
        org.apache.ambari.server.api.services.Result result = EasyMock.createMock(org.apache.ambari.server.api.services.Result.class);
        org.apache.ambari.server.api.services.ResultStatus resultStatus = EasyMock.createMock(org.apache.ambari.server.api.services.ResultStatus.class);
        org.apache.ambari.server.api.services.ResultPostProcessor processor = EasyMock.createStrictMock(org.apache.ambari.server.api.services.ResultPostProcessor.class);
        org.apache.ambari.server.api.services.RequestBody body = EasyMock.createNiceMock(org.apache.ambari.server.api.services.RequestBody.class);
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        java.util.Set<java.lang.String> directives = java.util.Collections.singleton("my_directive");
        org.apache.ambari.server.api.query.render.Renderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        org.apache.ambari.server.api.services.Request request = getTestRequest(headers, body, uriInfo, compiler, handler, processor, resource);
        EasyMock.expect(uriInfo.getQueryParameters()).andReturn(queryParams).anyTimes();
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition).anyTimes();
        EasyMock.expect(resourceDefinition.getUpdateDirectives()).andReturn(directives).anyTimes();
        EasyMock.expect(resourceDefinition.getCreateDirectives()).andReturn(directives).anyTimes();
        EasyMock.expect(resourceDefinition.getDeleteDirectives()).andReturn(directives).anyTimes();
        EasyMock.expect(resourceDefinition.getRenderer(null)).andReturn(renderer);
        EasyMock.expect(uriInfo.getRequestUri()).andReturn(uri).anyTimes();
        EasyMock.expect(body.getQueryString()).andReturn(null);
        if (((request.getRequestType() == org.apache.ambari.server.api.services.Request.Type.POST) || (request.getRequestType() == org.apache.ambari.server.api.services.Request.Type.PUT)) || (request.getRequestType() == org.apache.ambari.server.api.services.Request.Type.DELETE)) {
            EasyMock.expect(compiler.compile("foo=foo-value&bar=bar-value", directives)).andReturn(predicate);
        } else {
            EasyMock.expect(compiler.compile("foo=foo-value&bar=bar-value")).andReturn(predicate);
        }
        EasyMock.expect(handler.handleRequest(request)).andReturn(result);
        EasyMock.expect(result.getStatus()).andReturn(resultStatus).anyTimes();
        EasyMock.expect(resultStatus.isErrorState()).andReturn(false).anyTimes();
        processor.process(result);
        EasyMock.replay(headers, compiler, uriInfo, handler, resource, resourceDefinition, result, resultStatus, processor, predicate, body);
        org.apache.ambari.server.api.services.Result processResult = request.process();
        EasyMock.verify(headers, compiler, uriInfo, handler, resource, resourceDefinition, result, resultStatus, processor, predicate, body);
        org.junit.Assert.assertSame(processResult, result);
        org.junit.Assert.assertSame(predicate, request.getQueryPredicate());
    }

    @org.junit.Test
    public void testProcess_WithBody() throws java.lang.Exception {
        java.lang.String uriString = "http://localhost.com:8080/api/v1/clusters/c1";
        java.net.URI uri = new java.net.URI(java.net.URLEncoder.encode(uriString, "UTF-8"));
        org.apache.ambari.server.api.predicate.PredicateCompiler compiler = EasyMock.createStrictMock(org.apache.ambari.server.api.predicate.PredicateCompiler.class);
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        @java.lang.SuppressWarnings("unchecked")
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = EasyMock.createMock(javax.ws.rs.core.MultivaluedMap.class);
        org.apache.ambari.server.api.handlers.RequestHandler handler = EasyMock.createStrictMock(org.apache.ambari.server.api.handlers.RequestHandler.class);
        org.apache.ambari.server.api.services.Result result = EasyMock.createMock(org.apache.ambari.server.api.services.Result.class);
        org.apache.ambari.server.api.services.ResultStatus resultStatus = EasyMock.createMock(org.apache.ambari.server.api.services.ResultStatus.class);
        org.apache.ambari.server.api.services.ResultPostProcessor processor = EasyMock.createStrictMock(org.apache.ambari.server.api.services.ResultPostProcessor.class);
        org.apache.ambari.server.api.services.RequestBody body = EasyMock.createNiceMock(org.apache.ambari.server.api.services.RequestBody.class);
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.api.query.render.Renderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        org.apache.ambari.server.api.services.Request request = getTestRequest(null, body, uriInfo, compiler, handler, processor, resource);
        EasyMock.expect(uriInfo.getQueryParameters()).andReturn(queryParams).anyTimes();
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_MINIMAL)).andReturn(null);
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_FORMAT)).andReturn(null);
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition);
        EasyMock.expect(resourceDefinition.getRenderer(null)).andReturn(renderer);
        EasyMock.expect(uriInfo.getRequestUri()).andReturn(uri).anyTimes();
        EasyMock.expect(handler.handleRequest(request)).andReturn(result);
        EasyMock.expect(result.getStatus()).andReturn(resultStatus).anyTimes();
        EasyMock.expect(resultStatus.isErrorState()).andReturn(false).anyTimes();
        processor.process(result);
        EasyMock.expect(body.getQueryString()).andReturn(null);
        EasyMock.replay(compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, body);
        org.apache.ambari.server.api.services.Result processResult = request.process();
        EasyMock.verify(compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, body);
        org.junit.Assert.assertSame(result, processResult);
        org.junit.Assert.assertNull(request.getQueryPredicate());
    }

    @org.junit.Test
    public void testProcess_QueryInURI() throws java.lang.Exception {
        javax.ws.rs.core.HttpHeaders headers = EasyMock.createNiceMock(javax.ws.rs.core.HttpHeaders.class);
        java.lang.String path = java.net.URLEncoder.encode("http://localhost.com:8080/api/v1/clusters/c1", "UTF-8");
        java.lang.String query = java.net.URLEncoder.encode("foo=foo-value&bar=bar-value", "UTF-8");
        java.net.URI uri = new java.net.URI((path + "?") + query);
        org.apache.ambari.server.api.predicate.PredicateCompiler compiler = EasyMock.createStrictMock(org.apache.ambari.server.api.predicate.PredicateCompiler.class);
        org.apache.ambari.server.controller.spi.Predicate predicate = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Predicate.class);
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        @java.lang.SuppressWarnings("unchecked")
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = EasyMock.createMock(javax.ws.rs.core.MultivaluedMap.class);
        org.apache.ambari.server.api.handlers.RequestHandler handler = EasyMock.createStrictMock(org.apache.ambari.server.api.handlers.RequestHandler.class);
        org.apache.ambari.server.api.services.Result result = EasyMock.createMock(org.apache.ambari.server.api.services.Result.class);
        org.apache.ambari.server.api.services.ResultStatus resultStatus = EasyMock.createMock(org.apache.ambari.server.api.services.ResultStatus.class);
        org.apache.ambari.server.api.services.ResultPostProcessor processor = EasyMock.createStrictMock(org.apache.ambari.server.api.services.ResultPostProcessor.class);
        org.apache.ambari.server.api.services.RequestBody body = EasyMock.createNiceMock(org.apache.ambari.server.api.services.RequestBody.class);
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.api.query.render.Renderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        org.apache.ambari.server.api.services.Request request = getTestRequest(headers, body, uriInfo, compiler, handler, processor, resource);
        EasyMock.expect(uriInfo.getQueryParameters()).andReturn(queryParams).anyTimes();
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_MINIMAL)).andReturn(null);
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_FORMAT)).andReturn(null);
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition).anyTimes();
        EasyMock.expect(resourceDefinition.getRenderer(null)).andReturn(renderer);
        EasyMock.expect(uriInfo.getRequestUri()).andReturn(uri).anyTimes();
        EasyMock.expect(body.getQueryString()).andReturn(null);
        EasyMock.expect(compiler.compile("foo=foo-value&bar=bar-value")).andReturn(predicate);
        EasyMock.expect(handler.handleRequest(request)).andReturn(result);
        EasyMock.expect(result.getStatus()).andReturn(resultStatus).anyTimes();
        EasyMock.expect(resultStatus.isErrorState()).andReturn(false).anyTimes();
        processor.process(result);
        EasyMock.replay(headers, compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, predicate, body);
        org.apache.ambari.server.api.services.Result processResult = request.process();
        EasyMock.verify(headers, compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, predicate, body);
        org.junit.Assert.assertSame(processResult, result);
        org.junit.Assert.assertSame(predicate, request.getQueryPredicate());
    }

    @org.junit.Test
    public void testProcess_QueryInBody() throws java.lang.Exception {
        javax.ws.rs.core.HttpHeaders headers = EasyMock.createNiceMock(javax.ws.rs.core.HttpHeaders.class);
        java.lang.String uriString = "http://localhost.com:8080/api/v1/clusters/c1";
        java.net.URI uri = new java.net.URI(java.net.URLEncoder.encode(uriString, "UTF-8"));
        @java.lang.SuppressWarnings("unchecked")
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = EasyMock.createMock(javax.ws.rs.core.MultivaluedMap.class);
        org.apache.ambari.server.api.predicate.PredicateCompiler compiler = EasyMock.createStrictMock(org.apache.ambari.server.api.predicate.PredicateCompiler.class);
        org.apache.ambari.server.controller.spi.Predicate predicate = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Predicate.class);
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        org.apache.ambari.server.api.handlers.RequestHandler handler = EasyMock.createStrictMock(org.apache.ambari.server.api.handlers.RequestHandler.class);
        org.apache.ambari.server.api.services.Result result = EasyMock.createMock(org.apache.ambari.server.api.services.Result.class);
        org.apache.ambari.server.api.services.ResultStatus resultStatus = EasyMock.createMock(org.apache.ambari.server.api.services.ResultStatus.class);
        org.apache.ambari.server.api.services.ResultPostProcessor processor = EasyMock.createStrictMock(org.apache.ambari.server.api.services.ResultPostProcessor.class);
        org.apache.ambari.server.api.services.RequestBody body = EasyMock.createNiceMock(org.apache.ambari.server.api.services.RequestBody.class);
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.api.query.render.Renderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        org.apache.ambari.server.api.services.Request request = getTestRequest(headers, body, uriInfo, compiler, handler, processor, resource);
        EasyMock.expect(uriInfo.getQueryParameters()).andReturn(queryParams).anyTimes();
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_MINIMAL)).andReturn(null);
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_FORMAT)).andReturn(null);
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition).anyTimes();
        EasyMock.expect(resourceDefinition.getRenderer(null)).andReturn(renderer);
        EasyMock.expect(uriInfo.getRequestUri()).andReturn(uri).anyTimes();
        EasyMock.expect(body.getQueryString()).andReturn("foo=bar");
        EasyMock.expect(compiler.compile("foo=bar")).andReturn(predicate);
        EasyMock.expect(handler.handleRequest(request)).andReturn(result);
        EasyMock.expect(result.getStatus()).andReturn(resultStatus).anyTimes();
        EasyMock.expect(resultStatus.isErrorState()).andReturn(false).anyTimes();
        processor.process(result);
        EasyMock.replay(headers, compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, predicate, body);
        org.apache.ambari.server.api.services.Result processResult = request.process();
        EasyMock.verify(headers, compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, predicate, body);
        org.junit.Assert.assertSame(processResult, result);
        org.junit.Assert.assertSame(predicate, request.getQueryPredicate());
    }

    @org.junit.Test
    public void testProcess_QueryInBodyAndURI() throws java.lang.Exception {
        javax.ws.rs.core.HttpHeaders headers = EasyMock.createNiceMock(javax.ws.rs.core.HttpHeaders.class);
        java.lang.String uriString = "http://localhost.com:8080/api/v1/clusters/c1?bar=value";
        java.net.URI uri = new java.net.URI(java.net.URLEncoder.encode(uriString, "UTF-8"));
        org.apache.ambari.server.api.predicate.PredicateCompiler compiler = EasyMock.createStrictMock(org.apache.ambari.server.api.predicate.PredicateCompiler.class);
        org.apache.ambari.server.controller.spi.Predicate predicate = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Predicate.class);
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        @java.lang.SuppressWarnings("unchecked")
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = EasyMock.createMock(javax.ws.rs.core.MultivaluedMap.class);
        org.apache.ambari.server.api.handlers.RequestHandler handler = EasyMock.createStrictMock(org.apache.ambari.server.api.handlers.RequestHandler.class);
        org.apache.ambari.server.api.services.Result result = EasyMock.createMock(org.apache.ambari.server.api.services.Result.class);
        org.apache.ambari.server.api.services.ResultStatus resultStatus = EasyMock.createMock(org.apache.ambari.server.api.services.ResultStatus.class);
        org.apache.ambari.server.api.services.ResultPostProcessor processor = EasyMock.createStrictMock(org.apache.ambari.server.api.services.ResultPostProcessor.class);
        org.apache.ambari.server.api.services.RequestBody body = EasyMock.createNiceMock(org.apache.ambari.server.api.services.RequestBody.class);
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.api.query.render.Renderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        org.apache.ambari.server.api.services.Request request = getTestRequest(headers, body, uriInfo, compiler, handler, processor, resource);
        EasyMock.expect(uriInfo.getQueryParameters()).andReturn(queryParams).anyTimes();
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_MINIMAL)).andReturn(null);
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_FORMAT)).andReturn(null);
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition).anyTimes();
        EasyMock.expect(resourceDefinition.getRenderer(null)).andReturn(renderer);
        EasyMock.expect(uriInfo.getRequestUri()).andReturn(uri).anyTimes();
        EasyMock.expect(body.getQueryString()).andReturn("foo=bar");
        EasyMock.expect(compiler.compile("foo=bar")).andReturn(predicate);
        EasyMock.expect(handler.handleRequest(request)).andReturn(result);
        EasyMock.expect(result.getStatus()).andReturn(resultStatus).anyTimes();
        EasyMock.expect(resultStatus.isErrorState()).andReturn(false).anyTimes();
        processor.process(result);
        EasyMock.replay(headers, compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, predicate, body);
        org.apache.ambari.server.api.services.Result processResult = request.process();
        EasyMock.verify(headers, compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, predicate, body);
        org.junit.Assert.assertSame(processResult, result);
        org.junit.Assert.assertSame(predicate, request.getQueryPredicate());
    }

    @org.junit.Test
    public void testProcess_WithBody_InvalidQuery() throws java.lang.Exception {
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        java.lang.String uriString = "http://localhost.com:8080/api/v1/clusters/c1";
        @java.lang.SuppressWarnings("unchecked")
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = EasyMock.createMock(javax.ws.rs.core.MultivaluedMap.class);
        java.net.URI uri = new java.net.URI(java.net.URLEncoder.encode(uriString, "UTF-8"));
        org.apache.ambari.server.api.predicate.PredicateCompiler compiler = EasyMock.createStrictMock(org.apache.ambari.server.api.predicate.PredicateCompiler.class);
        org.apache.ambari.server.api.services.RequestBody body = EasyMock.createNiceMock(org.apache.ambari.server.api.services.RequestBody.class);
        java.lang.Exception exception = new org.apache.ambari.server.api.predicate.InvalidQueryException("test");
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.api.query.render.Renderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        org.apache.ambari.server.api.services.Request request = getTestRequest(null, body, uriInfo, compiler, null, null, resource);
        EasyMock.expect(uriInfo.getQueryParameters()).andReturn(queryParams).anyTimes();
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_MINIMAL)).andReturn(null);
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_FORMAT)).andReturn(null);
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition).anyTimes();
        EasyMock.expect(resourceDefinition.getRenderer(null)).andReturn(renderer);
        EasyMock.expect(uriInfo.getRequestUri()).andReturn(uri).anyTimes();
        EasyMock.expect(body.getQueryString()).andReturn("blahblahblah");
        EasyMock.expect(compiler.compile("blahblahblah")).andThrow(exception);
        EasyMock.replay(compiler, uriInfo, queryParams, resource, resourceDefinition, body);
        org.apache.ambari.server.api.services.Result processResult = request.process();
        EasyMock.verify(compiler, uriInfo, queryParams, resource, resourceDefinition, body);
        org.junit.Assert.assertEquals(400, processResult.getStatus().getStatusCode());
        org.junit.Assert.assertTrue(processResult.getStatus().isErrorState());
        org.junit.Assert.assertEquals("Unable to compile query predicate: test", processResult.getStatus().getMessage());
    }

    @org.junit.Test
    public void testProcess_noBody_ErrorStateResult() throws java.lang.Exception {
        java.lang.String uriString = "http://localhost.com:8080/api/v1/clusters/c1";
        java.net.URI uri = new java.net.URI(java.net.URLEncoder.encode(uriString, "UTF-8"));
        org.apache.ambari.server.api.predicate.PredicateCompiler compiler = EasyMock.createStrictMock(org.apache.ambari.server.api.predicate.PredicateCompiler.class);
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        @java.lang.SuppressWarnings("unchecked")
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = EasyMock.createMock(javax.ws.rs.core.MultivaluedMap.class);
        org.apache.ambari.server.api.handlers.RequestHandler handler = EasyMock.createStrictMock(org.apache.ambari.server.api.handlers.RequestHandler.class);
        org.apache.ambari.server.api.services.Result result = EasyMock.createMock(org.apache.ambari.server.api.services.Result.class);
        org.apache.ambari.server.api.services.ResultStatus resultStatus = EasyMock.createMock(org.apache.ambari.server.api.services.ResultStatus.class);
        org.apache.ambari.server.api.services.ResultPostProcessor processor = EasyMock.createStrictMock(org.apache.ambari.server.api.services.ResultPostProcessor.class);
        org.apache.ambari.server.api.services.RequestBody body = EasyMock.createNiceMock(org.apache.ambari.server.api.services.RequestBody.class);
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.api.query.render.Renderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        org.apache.ambari.server.api.services.Request request = getTestRequest(null, body, uriInfo, compiler, handler, processor, resource);
        EasyMock.expect(uriInfo.getQueryParameters()).andReturn(queryParams).anyTimes();
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_MINIMAL)).andReturn(null);
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_FORMAT)).andReturn(null);
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition).anyTimes();
        EasyMock.expect(resourceDefinition.getRenderer(null)).andReturn(renderer);
        EasyMock.expect(uriInfo.getRequestUri()).andReturn(uri).anyTimes();
        EasyMock.expect(handler.handleRequest(request)).andReturn(result);
        EasyMock.expect(result.getStatus()).andReturn(resultStatus).anyTimes();
        EasyMock.expect(resultStatus.isErrorState()).andReturn(true).anyTimes();
        EasyMock.replay(compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, body);
        org.apache.ambari.server.api.services.Result processResult = request.process();
        EasyMock.verify(compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, body);
        org.junit.Assert.assertSame(result, processResult);
        org.junit.Assert.assertNull(request.getQueryPredicate());
    }

    @org.junit.Test
    public void testGetFields() throws java.lang.Exception {
        java.lang.String fields = "prop,category/prop1,category2/category3/prop2[1,2,3],prop3[4,5,6],category4[7,8,9],sub-resource/*[10,11,12],finalProp";
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        @java.lang.SuppressWarnings("unchecked")
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> mapQueryParams = EasyMock.createMock(javax.ws.rs.core.MultivaluedMap.class);
        EasyMock.expect(uriInfo.getQueryParameters()).andReturn(mapQueryParams);
        EasyMock.expect(mapQueryParams.getFirst("fields")).andReturn(fields);
        EasyMock.replay(uriInfo, mapQueryParams);
        org.apache.ambari.server.api.services.Request request = getTestRequest(null, null, uriInfo, null, null, null, null);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> mapFields = request.getFields();
        org.junit.Assert.assertEquals(7, mapFields.size());
        java.lang.String prop = "prop";
        org.junit.Assert.assertTrue(mapFields.containsKey(prop));
        org.junit.Assert.assertNull(mapFields.get(prop));
        java.lang.String prop1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category", "prop1");
        org.junit.Assert.assertTrue(mapFields.containsKey(prop1));
        org.junit.Assert.assertNull(mapFields.get(prop1));
        java.lang.String prop2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("category2/category3", "prop2");
        org.junit.Assert.assertTrue(mapFields.containsKey(prop2));
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1, 2, 3), mapFields.get(prop2));
        java.lang.String prop3 = "prop3";
        org.junit.Assert.assertTrue(mapFields.containsKey(prop3));
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.internal.TemporalInfoImpl(4, 5, 6), mapFields.get(prop3));
        java.lang.String category4 = "category4";
        org.junit.Assert.assertTrue(mapFields.containsKey(category4));
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.internal.TemporalInfoImpl(7, 8, 9), mapFields.get(category4));
        java.lang.String subResource = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("sub-resource", "*");
        org.junit.Assert.assertTrue(mapFields.containsKey(subResource));
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10, 11, 12), mapFields.get(subResource));
        java.lang.String finalProp = "finalProp";
        org.junit.Assert.assertTrue(mapFields.containsKey(finalProp));
        org.junit.Assert.assertNull(mapFields.get(finalProp));
        EasyMock.verify(uriInfo, mapQueryParams);
    }

    @org.junit.Test
    public void testParseRenderer_minimalResponse() throws java.lang.Exception {
        java.lang.String uriString = "http://localhost.com:8080/api/v1/clusters/c1";
        java.net.URI uri = new java.net.URI(java.net.URLEncoder.encode(uriString, "UTF-8"));
        org.apache.ambari.server.api.predicate.PredicateCompiler compiler = EasyMock.createStrictMock(org.apache.ambari.server.api.predicate.PredicateCompiler.class);
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        @java.lang.SuppressWarnings("unchecked")
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = EasyMock.createMock(javax.ws.rs.core.MultivaluedMap.class);
        org.apache.ambari.server.api.handlers.RequestHandler handler = EasyMock.createStrictMock(org.apache.ambari.server.api.handlers.RequestHandler.class);
        org.apache.ambari.server.api.services.Result result = EasyMock.createMock(org.apache.ambari.server.api.services.Result.class);
        org.apache.ambari.server.api.services.ResultStatus resultStatus = EasyMock.createMock(org.apache.ambari.server.api.services.ResultStatus.class);
        org.apache.ambari.server.api.services.ResultPostProcessor processor = EasyMock.createStrictMock(org.apache.ambari.server.api.services.ResultPostProcessor.class);
        org.apache.ambari.server.api.services.RequestBody body = EasyMock.createNiceMock(org.apache.ambari.server.api.services.RequestBody.class);
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.api.query.render.Renderer renderer = new org.apache.ambari.server.api.query.render.MinimalRenderer();
        org.apache.ambari.server.api.services.Request request = getTestRequest(null, body, uriInfo, compiler, handler, processor, resource);
        EasyMock.expect(uriInfo.getQueryParameters()).andReturn(queryParams).anyTimes();
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_MINIMAL)).andReturn("true");
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition).anyTimes();
        EasyMock.expect(resourceDefinition.getRenderer("minimal")).andReturn(renderer);
        EasyMock.expect(uriInfo.getRequestUri()).andReturn(uri).anyTimes();
        EasyMock.expect(handler.handleRequest(request)).andReturn(result);
        EasyMock.expect(result.getStatus()).andReturn(resultStatus).anyTimes();
        EasyMock.expect(resultStatus.isErrorState()).andReturn(false).anyTimes();
        processor.process(result);
        EasyMock.replay(compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, body);
        request.process();
        EasyMock.verify(compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, body);
        org.junit.Assert.assertSame(renderer, request.getRenderer());
    }

    @org.junit.Test
    public void testParseRenderer_formatSpecified() throws java.lang.Exception {
        java.lang.String uriString = "http://localhost.com:8080/api/v1/clusters/c1";
        java.net.URI uri = new java.net.URI(java.net.URLEncoder.encode(uriString, "UTF-8"));
        org.apache.ambari.server.api.predicate.PredicateCompiler compiler = EasyMock.createStrictMock(org.apache.ambari.server.api.predicate.PredicateCompiler.class);
        javax.ws.rs.core.UriInfo uriInfo = EasyMock.createMock(javax.ws.rs.core.UriInfo.class);
        @java.lang.SuppressWarnings("unchecked")
        javax.ws.rs.core.MultivaluedMap<java.lang.String, java.lang.String> queryParams = EasyMock.createMock(javax.ws.rs.core.MultivaluedMap.class);
        org.apache.ambari.server.api.handlers.RequestHandler handler = EasyMock.createStrictMock(org.apache.ambari.server.api.handlers.RequestHandler.class);
        org.apache.ambari.server.api.services.Result result = EasyMock.createMock(org.apache.ambari.server.api.services.Result.class);
        org.apache.ambari.server.api.services.ResultStatus resultStatus = EasyMock.createMock(org.apache.ambari.server.api.services.ResultStatus.class);
        org.apache.ambari.server.api.services.ResultPostProcessor processor = EasyMock.createStrictMock(org.apache.ambari.server.api.services.ResultPostProcessor.class);
        org.apache.ambari.server.api.services.RequestBody body = EasyMock.createNiceMock(org.apache.ambari.server.api.services.RequestBody.class);
        org.apache.ambari.server.api.resources.ResourceInstance resource = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceInstance.class);
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = EasyMock.createNiceMock(org.apache.ambari.server.api.resources.ResourceDefinition.class);
        org.apache.ambari.server.api.query.render.Renderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        org.apache.ambari.server.api.services.Request request = getTestRequest(null, body, uriInfo, compiler, handler, processor, resource);
        EasyMock.expect(uriInfo.getQueryParameters()).andReturn(queryParams).anyTimes();
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_MINIMAL)).andReturn(null);
        EasyMock.expect(queryParams.getFirst(org.apache.ambari.server.api.predicate.QueryLexer.QUERY_FORMAT)).andReturn("default");
        EasyMock.expect(resource.getResourceDefinition()).andReturn(resourceDefinition).anyTimes();
        EasyMock.expect(resourceDefinition.getRenderer("default")).andReturn(renderer);
        EasyMock.expect(uriInfo.getRequestUri()).andReturn(uri).anyTimes();
        EasyMock.expect(handler.handleRequest(request)).andReturn(result);
        EasyMock.expect(result.getStatus()).andReturn(resultStatus).anyTimes();
        EasyMock.expect(resultStatus.isErrorState()).andReturn(false).anyTimes();
        processor.process(result);
        EasyMock.replay(compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, body);
        request.process();
        EasyMock.verify(compiler, uriInfo, handler, queryParams, resource, resourceDefinition, result, resultStatus, processor, body);
        org.junit.Assert.assertSame(renderer, request.getRenderer());
    }

    protected abstract org.apache.ambari.server.api.services.Request getTestRequest(javax.ws.rs.core.HttpHeaders headers, org.apache.ambari.server.api.services.RequestBody body, javax.ws.rs.core.UriInfo uriInfo, org.apache.ambari.server.api.predicate.PredicateCompiler compiler, org.apache.ambari.server.api.handlers.RequestHandler handler, org.apache.ambari.server.api.services.ResultPostProcessor processor, org.apache.ambari.server.api.resources.ResourceInstance resource);
}