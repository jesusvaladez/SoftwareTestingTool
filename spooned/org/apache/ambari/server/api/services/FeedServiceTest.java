package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class FeedServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.FeedService feedService = new org.apache.ambari.server.api.services.FeedServiceTest.TestFeedService("feedName");
        java.lang.reflect.Method m = feedService.getClass().getMethod("getFeed", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "feedName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, feedService, m, args, null));
        feedService = new org.apache.ambari.server.api.services.FeedServiceTest.TestFeedService(null);
        m = feedService.getClass().getMethod("getFeeds", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, feedService, m, args, null));
        feedService = new org.apache.ambari.server.api.services.FeedServiceTest.TestFeedService("feedName");
        m = feedService.getClass().getMethod("createFeed", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "feedName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, feedService, m, args, "body"));
        feedService = new org.apache.ambari.server.api.services.FeedServiceTest.TestFeedService("feedName");
        m = feedService.getClass().getMethod("updateFeed", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "feedName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, feedService, m, args, "body"));
        feedService = new org.apache.ambari.server.api.services.FeedServiceTest.TestFeedService("feedName");
        m = feedService.getClass().getMethod("deleteFeed", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "feedName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, feedService, m, args, null));
        return listInvocations;
    }

    private class TestFeedService extends org.apache.ambari.server.api.services.FeedService {
        private java.lang.String m_feedId;

        private TestFeedService(java.lang.String feedId) {
            m_feedId = feedId;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createFeedResource(java.lang.String feedName) {
            org.junit.Assert.assertEquals(m_feedId, feedName);
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.services.RequestFactory getRequestFactory() {
            return getTestRequestFactory();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.services.parsers.RequestBodyParser getBodyParser() {
            return getTestBodyParser();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.services.serializers.ResultSerializer getResultSerializer() {
            return getTestResultSerializer();
        }
    }
}